# URL Shortener Service

A modern URL shortening service built with Spring Boot that provides fast and reliable URL shortening capabilities with built-in rate limiting and validation.

## Features

- URL shortening using Base62 encoding
- Retrieval of original URLs from shortened versions
- Input validation for URLs
- Rate limiting to prevent abuse
- Automatic URL expiration after 30 days
- PostgreSQL persistence
- RESTful API with proper error handling

## Tech Stack

- Java 21
- Spring Boot 3.4.3
- Spring Data JPA
- PostgreSQL
- Lombok
- Resilience4j for rate limiting
- Jakarta Bean Validation

## API Endpoints

### Shorten URL
```http
POST /api/v1/urls
Content-Type: application/json

{
    "longUrl": "https://example.com/very/long/url/that/needs/shortening"
}
```

#### Response
```json
{
    "success": true,
    "message": "URL shortened successfully",
    "data": "abc123xyz",
    "timestamp": "2025-02-27T12:21:15+01:00"
}
```

### Retrieve Original URL
```http
GET /api/v1/urls/{shortUrl}
```

#### Response
```json
{
    "success": true,
    "message": "URL retrieved successfully",
    "data": "https://example.com/very/long/url/that/needs/shortening",
    "timestamp": "2025-02-27T12:21:15+01:00"
}
```

## Error Handling

The API uses standard HTTP status codes and returns consistent error responses:

- 400 Bad Request: Invalid input
- 404 Not Found: URL not found
- 429 Too Many Requests: Rate limit exceeded
- 500 Internal Server Error: Server-side errors

Error Response Format:
```json
{
    "success": false,
    "message": "Error description",
    "timestamp": "2025-02-27T12:21:15+01:00"
}
```

## Getting Started

### Prerequisites

- JDK 21
- Maven
- PostgreSQL

### Configuration

Create an `application.properties` file in `src/main/resources`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Rate Limiting
resilience4j.ratelimiter.instances.apiRateLimiter.limitForPeriod=100
resilience4j.ratelimiter.instances.apiRateLimiter.limitRefreshPeriod=1m
resilience4j.ratelimiter.instances.apiRateLimiter.timeoutDuration=0
```

### Building

```bash
mvn clean install
```

### Running

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Project Structure

```
src/main/java/com/example/shorten/
├── controller/
│   └── UrlController.java
├── service/
│   └── UrlShortenService.java
├── repository/
│   └── UrlRepository.java
├── entity/
│   └── UrlEntity.java
├── dto/
│   ├── ApiResponse.java
│   └── UrlRequest.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── HashGenerationException.java
│   └── NotFoundException.java
└── utility/
    └── Base62.java
```

## Security Considerations

- Input validation for URLs
- Rate limiting to prevent abuse
- No sensitive data exposure
- Safe URL handling
- Proper exception handling

## Performance Optimization

- Database indexing on frequently queried fields
- Optimistic locking for concurrency
- Efficient Base62 encoding
- Transaction management

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
