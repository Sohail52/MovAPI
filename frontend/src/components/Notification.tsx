import React, { createContext, useCallback, useContext, useMemo, useRef, useState } from 'react'

type NotificationType = 'success' | 'error' | 'warning' | 'info'

type NewNotification = {
  message: string
  type?: NotificationType
  durationMs?: number
}

type NotificationItem = {
  id: number
  message: string
  type: NotificationType
  expiresAt?: number
}

type NotificationContextValue = {
  addNotification: (notification: NewNotification) => number
  removeNotification: (id: number) => void
  clearNotifications: () => void
}

const NotificationContext = createContext<NotificationContextValue | undefined>(undefined)

export function useNotification(): NotificationContextValue {
  const ctx = useContext(NotificationContext)
  if (!ctx) {
    throw new Error('useNotification must be used within a NotificationProvider')
  }
  return ctx
}

export const NotificationProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [notifications, setNotifications] = useState<NotificationItem[]>([])
  const nextIdRef = useRef<number>(1)

  const removeNotification = useCallback((id: number) => {
    setNotifications(prev => prev.filter(n => n.id !== id))
  }, [])

  const clearNotifications = useCallback(() => {
    setNotifications([])
  }, [])

  const addNotification = useCallback((notification: NewNotification) => {
    const id = nextIdRef.current++
    const type: NotificationType = notification.type ?? 'info'
    const durationMs = notification.durationMs ?? 3000
    const expiresAt = Number.isFinite(durationMs) ? Date.now() + durationMs : undefined

    const item: NotificationItem = {
      id,
      message: notification.message,
      type,
      expiresAt,
    }
    setNotifications(prev => [...prev, item])

    if (expiresAt) {
      window.setTimeout(() => removeNotification(id), durationMs)
    }
    return id
  }, [removeNotification])

  const value = useMemo<NotificationContextValue>(() => ({ addNotification, removeNotification, clearNotifications }), [addNotification, removeNotification, clearNotifications])

  const containerStyle: React.CSSProperties = {
    position: 'fixed',
    top: 16,
    right: 16,
    display: 'flex',
    flexDirection: 'column',
    gap: 8,
    zIndex: 1000,
    pointerEvents: 'none',
  }

  const cardBaseStyle: React.CSSProperties = {
    minWidth: 240,
    maxWidth: 360,
    padding: '10px 12px',
    borderRadius: 8,
    color: '#0b141a',
    backgroundColor: '#e6f0f6',
    boxShadow: '0 6px 24px rgba(0,0,0,0.12)',
    border: '1px solid rgba(0,0,0,0.06)',
    pointerEvents: 'auto',
  }

  function typeStyles(type: NotificationType): React.CSSProperties {
    switch (type) {
      case 'success':
        return { backgroundColor: '#e6f6ed', color: '#0a3f1c', borderColor: '#b7ebc5' }
      case 'error':
        return { backgroundColor: '#fdecea', color: '#611a15', borderColor: '#f5c2c0' }
      case 'warning':
        return { backgroundColor: '#fff4e5', color: '#663c00', borderColor: '#ffdd99' }
      default:
        return { backgroundColor: '#e6f0f6', color: '#0b141a', borderColor: '#c6d6e2' }
    }
  }

  return (
    <NotificationContext.Provider value={value}>
      {children}
      <div style={containerStyle} aria-live="polite" aria-atomic="true">
        {notifications.map(n => (
          <div key={n.id} style={{ ...cardBaseStyle, ...typeStyles(n.type) }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 8 }}>
              <div style={{ whiteSpace: 'pre-wrap' }}>{n.message}</div>
              <button
                onClick={() => removeNotification(n.id)}
                aria-label="Dismiss notification"
                style={{
                  marginLeft: 8,
                  border: 'none',
                  background: 'transparent',
                  cursor: 'pointer',
                  color: 'inherit',
                  fontSize: 16,
                }}
              >×</button>
            </div>
          </div>
        ))}
      </div>
    </NotificationContext.Provider>
  )
}


