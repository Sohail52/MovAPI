import React from 'react'

type Props = {
  title?: string
  children: React.ReactNode
}

type State = {
  hasError: boolean
  retryKey: number
  error?: Error
}

export default class SectionErrorBoundary extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props)
    this.state = { hasError: false, retryKey: 0 }
  }

  static getDerivedStateFromError(error: Error): Partial<State> {
    return { hasError: true, error }
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    // eslint-disable-next-line no-console
    console.error('SectionErrorBoundary caught an error', error, errorInfo)
  }

  private handleRetry = () => {
    this.setState(prev => ({ hasError: false, retryKey: prev.retryKey + 1, error: undefined }))
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="error-section" style={{ padding: 16, border: '1px solid #f5c2c0', background: '#fdecea', borderRadius: 8 }}>
          <h3 style={{ marginTop: 0 }}>{this.props.title ?? 'Section Error'}</h3>
          <p>{this.state.error?.message || 'This section failed to load.'}</p>
          <button className="retry-button" onClick={this.handleRetry}>Retry</button>
        </div>
      )
    }

    return (
      <React.Fragment key={this.state.retryKey}>
        {this.props.children}
      </React.Fragment>
    )
  }
}


