import React, { useState } from 'react'
import api from '../api/axios'
import { Link } from 'react-router-dom'

export function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    try {
      const trimmedUsername = username.trim()
      const trimmedPassword = password
      if (!trimmedUsername || !trimmedPassword) {
        setError('Please enter username and password')
        return
      }
      const res = await api.post('/api/auth/login', { username: trimmedUsername, password: trimmedPassword })
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', res.data.username)
      window.location.href = '/'
    } catch (err: any) {
      const data = err?.response?.data
      const detailedMessage =
        (data && typeof data === 'object' && 'message' in data && (data as any).message) ||
        (typeof data === 'string' ? data : null)
      setError(detailedMessage || 'Login failed')
    }
  }
  return (
    <div className="auth-wrapper">
      <div className="auth-card">
        <h2 className="auth-title">Welcome back</h2>
        <div className="auth-subtitle">Sign in to continue</div>
        {error && <div className="auth-error">{error}</div>}
        <form onSubmit={onSubmit} className="auth-form">
          <div className="auth-field">
            <label>Username</label>
            <input className="auth-input" placeholder="Enter your username" value={username} onChange={e => setUsername(e.target.value)} />
          </div>
          <div className="auth-field">
            <label>Password</label>
            <input className="auth-input" type="password" placeholder="Enter your password" value={password} onChange={e => setPassword(e.target.value)} />
          </div>
          <button type="submit" className="auth-button">Login</button>
        </form>
        <div className="auth-footer">
          New here? <Link to="/register">Create an account</Link>
        </div>
      </div>
    </div>
  )
}

export function Register() {
  const [userName, setUserName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [passwordHint, setPasswordHint] = useState<string>('Must be at least 8 characters long.')
  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    try {
      // quick client-side checks to align with backend validation
      if (password.length < 8) {
        setError('Password must be at least 8 characters long')
        return
      }
      if (password !== confirmPassword) {
        setError('Passwords do not match')
        return
      }
      
      console.log('Attempting registration with:', { userName, email, password: '***', confirmPassword: '***' })
      
      const res = await api.post('/api/auth/register', { 
        username: userName,   // ✅ fixed: backend expects "username"
        email, 
        password, 
        confirmPassword 
      })
      console.log('Registration successful:', res.data)
      
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', res.data.username)
      window.location.href = '/'
    } catch (err: any) {
      console.error('Registration error details:', err)
      console.error('Error response:', err?.response)
      console.error('Error response data:', err?.response?.data)
      console.error('Error message:', err?.message)
      
      // Log the full error response for debugging
      if (err?.response?.data) {
        console.error('Full error response data:', JSON.stringify(err.response.data, null, 2))
      }
      
      const data = err?.response?.data
      const detailedMessage =
        (data && typeof data === 'object' && 'message' in data && (data as any).message) ||
        (Array.isArray((data as any)?.errors) && (data as any).errors.join(', ')) ||
        (typeof data === 'string' ? data : null)
      setError(detailedMessage || 'Registration failed')
    }
  }
  return (
    <div className="auth-wrapper">
      <div className="auth-card">
        <h2 className="auth-title">Create your account</h2>
        <div className="auth-subtitle">Join MovieAPI to save your watchlist</div>
        {error && <div className="auth-error">{error}</div>}
        <form onSubmit={onSubmit} className="auth-form">
          <div className="auth-field">
            <label>Username</label>
            <input className="auth-input" placeholder="Choose a username" value={userName} onChange={e => setUserName(e.target.value)} />
          </div>
          <div className="auth-field">
            <label>Email</label>
            <input className="auth-input" type="email" placeholder="you@example.com" value={email} onChange={e => setEmail(e.target.value)} />
          </div>
          <div className="auth-field">
            <label>Password</label>
            <input className="auth-input" type="password" placeholder="Enter a strong password" value={password} onChange={e => setPassword(e.target.value)} />
            <small style={{ color: '#6b7280' }}>{passwordHint}</small>
          </div>
          <div className="auth-field">
            <label>Confirm Password</label>
            <input className="auth-input" type="password" placeholder="Re-enter your password" value={confirmPassword} onChange={e => setConfirmPassword(e.target.value)} />
          </div>
          <button type="submit" className="auth-button">Create account</button>
        </form>
        <div className="auth-footer">
          Already have an account? <Link to="/login">Login</Link>
        </div>
      </div>
    </div>
  )
}


