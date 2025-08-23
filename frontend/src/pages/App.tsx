import React, { useEffect, useState, useCallback } from 'react'
import api from '../api/axios'
import { useNotification } from '../components/Notification'
import SectionErrorBoundary from '../components/SectionErrorBoundary'
import { mockMovies, mockTopRatedMovies, mockUpcomingMovies } from '../mockData'
import { Login, Register } from './Auth'
import WatchlistPage from './Watchlist'
import { Routes, Route, Link } from 'react-router-dom'

type Movie = {
  id: number
  title: string
  overview: string
  release_date?: string
  vote_average?: number
}

function Section({ title, endpoint }: { title: string; endpoint: string }) {
  // Get the appropriate mock data based on endpoint
  const getMockData = () => {
    switch (endpoint) {
      case '/movie/popular':
        return mockMovies
      case '/movie/top_rated':
        return mockTopRatedMovies
      case '/movie/upcoming':
        return mockUpcomingMovies
      default:
        return mockMovies
    }
  }
  const [movies, setMovies] = useState<Movie[]>([])
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState<boolean>(false)
  const [retryKey, setRetryKey] = useState<number>(0) // Used to force re-fetch
  const { addNotification } = useNotification()
  const [justAddedId, setJustAddedId] = useState<number | null>(null)
  
  // Retry function for the error boundary
  const handleRetry = useCallback(() => {
    setRetryKey(prev => prev + 1) // Increment to trigger useEffect
  }, [])

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)
      setError(null)
      
      // Use mock data by default to avoid CORS issues
      setMovies(getMockData())
      setLoading(false)
      
      // Uncomment this block when backend CORS is configured
      /*
      try {
        const res = await api.get(endpoint, { params: { page: 1 } })
        
        // Debug the API response structure
        console.log('API response:', res)
        console.log('API response data:', res.data)
        console.log('API response data type:', typeof res.data)
        
        // Handle different response structures
        let moviesData: Movie[] = []
        
        if (Array.isArray(res.data)) {
          // If response is already an array
          moviesData = res.data
        } else if (res.data && typeof res.data === 'object') {
          // If response is an object that might contain an array
          // Check common API response patterns
          if (Array.isArray(res.data.results)) {
            moviesData = res.data.results
          } else if (Array.isArray(res.data.content)) {
            moviesData = res.data.content
          } else if (Array.isArray(res.data.data)) {
            moviesData = res.data.data
          } else if (Array.isArray(res.data.movies)) {
            moviesData = res.data.movies
          } else {
            // If it's a single movie object, wrap it in an array
            if (res.data.title) {
              moviesData = [res.data]
            } else if (res.data.message && res.data.message.includes('Connection reset')) {
              // Handle connection reset errors gracefully
              console.error('Connection reset error:', res.data)
              setError('Connection to movie database failed. Please try again later.')
              return // Exit early
            } else {
              console.error('Unexpected API response structure:', res.data)
              // Don't throw, just use empty array
              moviesData = []
            }
          }
        } else {
          // If we can't determine the structure, use empty array as fallback
          console.error('Invalid API response:', res.data)
          // Don't throw, just use empty array
          moviesData = []
        }
        
        console.log('Processed movies data:', moviesData)
        setMovies(moviesData)
      } catch (e: any) {
        console.error('Error fetching movies:', e)
        const errorMessage = e?.message || 'Failed to fetch movies'
        setError(errorMessage)
        addNotification({ message: 'Using mock data due to API error', type: 'warning' })
        
        // Use mock data as fallback
        setMovies(getMockData())
      } finally {
        setLoading(false)
      }
      */
    }
    fetchData()
  }, [endpoint, addNotification, retryKey])

  return (
    <section className="movie-section">
      <h2>{title}</h2>
      {error && (
        <div className="error-message">
          {error}
          <button onClick={handleRetry} className="retry-button">Retry</button>
        </div>
      )}
      {loading ? (
        <div className="loading-indicator">Loading...</div>
      ) : (
        <div className="movie-grid">
          {movies.length === 0 && !error ? (
            <div className="no-results">No movies found</div>
          ) : (
            movies.map((m) => (
              <div key={m.id} className="movie-card">
                <h3 className="movie-title">{m.title}</h3>
                <div className="movie-meta">
                  {m.release_date} • ⭐ {m.vote_average ?? '-'}
                </div>
                <p className="movie-overview">{m.overview}</p>
                <AddToWatchlistButton movieId={m.id} onAdded={() => setJustAddedId(m.id)} />
              </div>
            ))
          )}
        </div>
      )}
    </section>
  )
}

export function App() {
  // Create retry handlers for each section
  const [popularRetryKey, setPopularRetryKey] = useState(0)
  const [topRatedRetryKey, setTopRatedRetryKey] = useState(0)
  const [upcomingRetryKey, setUpcomingRetryKey] = useState(0)
  
  // Retry handlers for each section
  const handlePopularRetry = useCallback(() => setPopularRetryKey(prev => prev + 1), [])
  const handleTopRatedRetry = useCallback(() => setTopRatedRetryKey(prev => prev + 1), [])
  const handleUpcomingRetry = useCallback(() => setUpcomingRetryKey(prev => prev + 1), [])
  
  return (
    <div className="app-container">
      <header className="app-header">
        <h1>MovieAPI</h1>
        <p>Your personal movie discovery platform</p>
        <nav>
          <Link to="/login">Login</Link> | <Link to="/register">Register</Link> | <Link to="/watchlist">Watchlist</Link>
        </nav>
      </header>

      <main>
        <Routes>
          <Route
            path="/"
            element={
              <>
                <div className="subscription-container">
                  <h2>Stay Updated</h2>
                  <p>Subscribe to receive weekly updates about upcoming movies</p>
                  <SubscribeForm />
                </div>

                <SectionErrorBoundary title="Popular Movies">
                  <Section
                    key={`popular-${popularRetryKey}`}
                    title="Popular Movies"
                    endpoint="/movie/popular"
                  />
                </SectionErrorBoundary>

                <SectionErrorBoundary title="Top Rated Movies">
                  <Section
                    key={`top-rated-${topRatedRetryKey}`}
                    title="Top Rated Movies"
                    endpoint="/movie/top_rated"
                  />
                </SectionErrorBoundary>

                <SectionErrorBoundary title="Upcoming Movies">
                  <Section
                    key={`upcoming-${upcomingRetryKey}`}
                    title="Upcoming Movies"
                    endpoint="/movie/upcoming"
                  />
                </SectionErrorBoundary>
              </>
            }
          />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/watchlist" element={<WatchlistPage />} />
        </Routes>
      </main>

      <footer className="app-footer">
        <p>© {new Date().getFullYear()} MovieAPI - Powered by Spring Boot and React</p>
      </footer>
    </div>
  )
}

function SubscribeForm() {
  const [email, setEmail] = React.useState('')
  const [status, setStatus] = React.useState<string | null>(null)
  const [isLoading, setIsLoading] = React.useState(false)
  const [isSuccess, setIsSuccess] = React.useState(false)
  const { addNotification } = useNotification()
  
  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setStatus(null)
    setIsSuccess(false)
    setIsLoading(true)
    
    try {
      // Mock implementation for when backend is not running
      // Comment out the actual API call and use a timeout to simulate network request
      // await api.post('/subscriptions', { email })
      
      // Simulate successful API response after 1 second
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      const successMessage = 'Subscribed! You will receive weekly emails for upcoming movies.'
      setIsSuccess(true)
      setStatus(successMessage)
      setEmail('')
      addNotification({ message: successMessage, type: 'success' })
      
      // Log the email that would be sent to the backend
      console.log('Subscription email (mock):', email);
    } catch (err: any) {
      const errorMessage = 'Failed to subscribe: ' + (err?.message || 'Unknown error')
      setIsSuccess(false)
      setStatus(errorMessage)
      addNotification({ message: errorMessage, type: 'error' })
    } finally {
      setIsLoading(false)
    }
  }
  return (
    <form onSubmit={onSubmit} className="subscribe-form">
      <div className="form-input-group">
        <input
          type="email"
          placeholder="Enter your email for weekly digest"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          disabled={isLoading}
          className="subscribe-input"
        />
        <button 
          type="submit" 
          disabled={isLoading}
          className={`subscribe-button ${isLoading ? 'loading' : ''}`}
        >
          {isLoading ? 'Subscribing...' : 'Subscribe'}
        </button>
      </div>
      {status && (
        <div className={`status-message ${isSuccess ? 'success' : 'error'}`}>
          {status}
        </div>
      )}
    </form>
  )
}

function AddToWatchlistButton({ movieId, onAdded }: { movieId: number; onAdded?: () => void }) {
  const [loading, setLoading] = React.useState(false)
  const [status, setStatus] = React.useState<string | null>(null)

  const add = async () => {
    setLoading(true)
    setStatus(null)
    try {
      await api.post(`/api/watchlist/${movieId}`)
      setStatus('Added to watchlist')
      onAdded?.()
      // notify and redirect to watchlist so users can see it immediately
      window.dispatchEvent(new CustomEvent('watchlist-updated'))
      setTimeout(() => { window.location.href = '/watchlist' }, 400)
    } catch (e: any) {
      const data = e?.response?.data
      const detailedMessage =
        (data && typeof data === 'object' && 'message' in data && (data as any).message) ||
        (typeof data === 'string' ? data : null)
      setStatus(detailedMessage || 'Failed to add')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <button className="subscribe-button" onClick={add} disabled={loading}>
        {loading ? 'Adding…' : 'Add to watchlist'}
      </button>
      {status && <div className="status-message success">{status}</div>}
    </div>
  )
}


