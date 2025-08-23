import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../api/axios'

type Item = { userId: number; movieId: number; movieName: string; addAt: string }

export default function WatchlistPage() {
  const [items, setItems] = useState<Item[]>([])
  const [error, setError] = useState<string | null>(null)
  const [checkingAuth, setCheckingAuth] = useState(true)

  const fetchItems = async () => {
    try {
      const res = await api.get('/api/watchlist/get-all')
      setItems(res.data)
    } catch (err: any) {
      setError(err?.message || 'Failed to load watchlist')
    }
  }

  const remove = async (movieId: number) => {
    try {
      await api.delete(`/api/watchlist/${movieId}`)
      setItems(prev => prev.filter(i => i.movieId !== movieId))
    } catch (err: any) {
      setError(err?.message || 'Failed to remove')
    }
  }

  useEffect(() => {
    const token = localStorage.getItem('token')
    if (!token) {
      window.location.href = '/login'
      return
    }
    setCheckingAuth(false)
    fetchItems()
    const onUpdated = () => fetchItems()
    window.addEventListener('watchlist-updated', onUpdated as EventListener)
    const onFocus = () => fetchItems()
    const onVisibility = () => { if (document.visibilityState === 'visible') fetchItems() }
    window.addEventListener('focus', onFocus)
    document.addEventListener('visibilitychange', onVisibility)
    return () => {
      window.removeEventListener('watchlist-updated', onUpdated as EventListener)
      window.removeEventListener('focus', onFocus)
      document.removeEventListener('visibilitychange', onVisibility)
    }
  }, [])

  if (checkingAuth) return null

  const count = items.length
  return (
    <div className="app-container">
      <header className="app-header" style={{ paddingTop: 20, paddingBottom: 10, marginBottom: 20 }}>
        <h2>Your Watchlist {count > 0 ? `(${count})` : ''}</h2>
        <p>Movies you saved to watch later</p>
        <nav style={{ marginTop: 8 }}>
          <Link to="/" style={{ color: '#0066cc', textDecoration: 'none' }}>&larr; Back to Home</Link>
        </nav>
      </header>

      {error && <div className="error-message">{error}</div>}

      {items.length === 0 ? (
        <div className="no-results">No movies in your watchlist yet</div>
      ) : (
        <div className="movie-grid">
          {items.map(i => (
            <div key={i.movieId} className="movie-card">
              <h3 className="movie-title">{i.movieName}</h3>
              <div className="movie-meta">Added on: {new Date(i.addAt).toLocaleDateString()}</div>
              <div className="card-actions">
                <button className="danger-button" onClick={() => remove(i.movieId)}>Remove</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}


