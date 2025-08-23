import React from 'react'
import { createRoot } from 'react-dom/client'
import { App } from './pages/App'
import './index.css'
import { NotificationProvider } from './components/Notification'
import ErrorBoundary from './components/ErrorBoundary'
import { BrowserRouter } from 'react-router-dom'

const container = document.getElementById('root')!
createRoot(container).render(
  <React.StrictMode>
    <BrowserRouter>
      <ErrorBoundary>
        <NotificationProvider>
          <App />
        </NotificationProvider>
      </ErrorBoundary>
    </BrowserRouter>
  </React.StrictMode>
)


