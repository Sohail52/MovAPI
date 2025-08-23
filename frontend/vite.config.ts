import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
      '/movie': 'http://localhost:8080',
      '/genre': 'http://localhost:8080',
      '/users': 'http://localhost:8080',
      '/subscriptions': 'http://localhost:8080'
    }
  }
})


