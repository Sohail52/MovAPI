This project is still under active development. 

# Movie API Development Project

A comprehensive Spring Boot-based Movie API with user authentication, movie management, and watchlist functionality.

## 🚀 Features

- **User Authentication & Authorization**: Secure user registration, login, and role-based access control
- **Movie Management**: CRUD operations for movies with detailed information
- **Watchlist System**: Users can create and manage their personal movie watchlists
- **RESTful API**: Clean, well-structured REST endpoints
- **Security**: JWT-based authentication with Spring Security
- **Database**: JPA/Hibernate with MySQL/PostgreSQL support
- **Validation**: Comprehensive input validation and error handling

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Build Tool**: Gradle
- **Database**: JPA/Hibernate
- **Security**: Spring Security, JWT
- **Validation**: Jakarta Validation

## 📚 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### Users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/password` - Change password
- `DELETE /api/users/profile` - Delete own profile
- `GET /api/users` - Get all users (Admin only)
- `DELETE /api/users/{id}` - Delete user by ID (Admin only)

### Movies
- `GET /api/movies` - Get all movies
- `GET /api/movies/{id}` - Get movie by ID
- `POST /api/movies` - Create new movie (Admin only)
- `PUT /api/movies/{id}` - Update movie (Admin only)
- `DELETE /api/movies/{id}` - Delete movie (Admin only)

### Watchlist
- `GET /api/watchlist` - Get user's watchlist
- `POST /api/watchlist/{movieId}` - Add movie to watchlist
- `DELETE /api/watchlist/{movieId}` - Remove movie from watchlist

## 🔐 Security

The application uses JWT-based authentication with the following security features:
- Password encryption using BCrypt
- Role-based access control (USER, ADMIN)
- JWT token validation
- Secure endpoints with proper authorization

## 🗄️ Database Schema

### Users Table
- `id` - Primary key
- `username` - Unique username
- `email` - Unique email address
- `password` - Encrypted password
- `role` - User role (USER/ADMIN)
- `created_at` - Account creation timestamp

### Movies Table
- `id` - Primary key
- `title` - Movie title
- `description` - Movie description
- `release_year` - Release year
- `genre` - Movie genre
- `rating` - Movie rating
- `created_at` - Record creation timestamp

### Watchlist Table
- `id` - Primary key
- `user_id` - Foreign key to users table
- `movie_id` - Foreign key to movies table
- `added_at` - Addition timestamp

```



If you have any questions or need help, please open an issue on GitHub or contact me directly.

---
