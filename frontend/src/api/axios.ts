import axios from 'axios';

// Create axios instance with default config
const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for authentication
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers = config.headers || {};
      (config.headers as any).Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    
    // Implement retry logic for network errors or 5xx server errors
    if (
      (error.message.includes('Network Error') || 
      (error.response && error.response.status >= 500)) && 
      !originalRequest._retry && 
      originalRequest.method === 'get'
    ) {
      originalRequest._retry = true;
      
      // Wait 1 second before retrying
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Retry the request
      return api(originalRequest);
    }
    
    // Handle common errors here
    if (error.response) {
      // The request was made and the server responded with a status code
      // that falls out of the range of 2xx
      const status = error.response.status;
      
      switch (status) {
        case 401:
          console.error('Unauthorized: Authentication required');
          // You could redirect to login page or show auth modal here
          break;
        case 403:
          console.error('Forbidden: You don\'t have permission to access this resource');
          break;
        case 404:
          console.error('Not Found: The requested resource does not exist');
          break;
        case 400:
          console.error('Bad Request:', error.response.data);
          break;
        case 500:
          console.error('Server Error: Something went wrong on the server');
          break;
        default:
          console.error('Response error:', status, error.response.data);
      }
    } else if (error.request) {
      // The request was made but no response was received
      console.error('Network Error: No response received from server. Please check your connection.');
    } else {
      // Something happened in setting up the request that triggered an Error
      console.error('Error:', error.message);
    }
    return Promise.reject(error);
  }
);

export default api;