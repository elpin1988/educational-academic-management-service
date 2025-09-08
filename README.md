# Academic Management Service

A microservice for managing grades and student grade enrollments in the school system, built with Spring Boot and Kotlin following Clean Architecture + Hexagonal Architecture principles.

## 🏗️ Architecture

This service follows Clean Architecture + Hexagonal Architecture patterns:

```
src/main/kotlin/co/edu/school/academic_management_service/
├── domain/                    # Domain layer (business logic)
│   ├── constant/             # Domain constants
│   ├── entity/               # Domain entities
│   └── repository/           # Repository interfaces
├── application/              # Application layer (use cases)
│   ├── port/
│   │   ├── input/           # Input ports (use case interfaces)
│   │   └── output/          # Output ports (repository interfaces)
│   ├── usecase/             # Use case implementations
│   └── service/impl/        # Application services
├── infrastructure/           # Infrastructure layer (technical details)
│   ├── adapter/             # Port adapters
│   ├── config/              # Configuration
│   ├── database/            # Database implementation
│   └── repository/          # Repository implementations
└── presentation/            # Presentation layer (REST API)
    └── controller/          # REST controllers
```

## 🚀 Features

- **Grade Management**: Create, read, update, and delete grades
- **Student Grade Enrollments**: Manage student enrollments in grades
- **Student Transfers**: Transfer students between grades
- **Graduation Management**: Handle student graduations
- **REST API**: Complete REST API with Swagger documentation
- **Docker Support**: Containerized deployment
- **Database**: PostgreSQL with Exposed ORM
- **Testing**: Comprehensive unit and integration tests

## 🛠️ Technology Stack

- **Language**: Kotlin
- **Framework**: Spring Boot 3.5.5
- **Database**: PostgreSQL 15
- **ORM**: Exposed 0.50.1
- **Documentation**: Swagger/OpenAPI 3
- **Testing**: JUnit 5, MockK, AssertJ
- **Containerization**: Docker & Docker Compose

## 📋 Prerequisites

- Java 21
- Docker & Docker Compose
- Gradle 8.x

## 🚀 Quick Start

### Using Docker Compose (Recommended)

1. Clone the repository
2. Run the application with Docker Compose:

```bash
docker-compose up -d
```

The application will be available at:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

### Local Development

1. Start PostgreSQL database:
```bash
docker-compose up postgres -d
```

2. Run the application:
```bash
./gradlew bootRun
```

## 📚 API Documentation

### Grade Management

#### Create Grade
```http
POST /api/v1/grades
Content-Type: application/json

{
  "name": "First Grade",
  "description": "First grade of elementary school",
  "level": 1
}
```

#### Get All Grades
```http
GET /api/v1/grades
```

#### Get Grade by ID
```http
GET /api/v1/grades/{id}
```

#### Update Grade
```http
PUT /api/v1/grades/{id}
Content-Type: application/json

{
  "name": "Updated Grade",
  "description": "Updated description",
  "level": 1
}
```

#### Deactivate Grade
```http
PATCH /api/v1/grades/{id}/deactivate
```

#### Delete Grade
```http
DELETE /api/v1/grades/{id}
```

### Student Grade Management

#### Enroll Student in Grade
```http
POST /api/v1/student-grades/enroll
Content-Type: application/json

{
  "studentId": 1,
  "gradeId": 1,
  "startDate": "2024-01-01T00:00:00"
}
```

#### Transfer Student Between Grades
```http
POST /api/v1/student-grades/transfer
Content-Type: application/json

{
  "studentId": 1,
  "fromGradeId": 1,
  "toGradeId": 2,
  "transferDate": "2024-06-01T00:00:00"
}
```

#### End Student Enrollment
```http
POST /api/v1/student-grades/end
Content-Type: application/json

{
  "studentId": 1,
  "gradeId": 1,
  "endDate": "2024-12-31T00:00:00"
}
```

#### Graduate Student
```http
POST /api/v1/student-grades/graduate
Content-Type: application/json

{
  "studentId": 1,
  "graduationDate": "2024-12-31T00:00:00"
}
```

#### Get Student Enrollments
```http
GET /api/v1/student-grades/student/{studentId}
```

#### Get Grade Enrollments
```http
GET /api/v1/student-grades/grade/{gradeId}
```

## 🧪 Testing

Run all tests:
```bash
./gradlew test
```

Run tests with coverage:
```bash
./gradlew test jacocoTestReport
```

## 🐳 Docker

### Build Image
```bash
docker build -t academic-management-service .
```

### Run Container
```bash
docker run -p 8080:8080 academic-management-service
```

### Docker Compose
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f academic-management-service
```

## 📊 Monitoring

The service includes Spring Boot Actuator endpoints for monitoring:

- **Health Check**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Info**: `/actuator/info`

## 🔧 Configuration

### Environment Variables

- `DB_HOST`: Database host (default: postgres)
- `DB_PORT`: Database port (default: 5432)
- `DB_NAME`: Database name (default: academic_management_service)
- `DB_USER`: Database user (default: postgres)
- `DB_PASSWORD`: Database password (default: password)

### Profiles

- `dev`: Development profile
- `docker`: Docker profile
- `prod`: Production profile

## 📝 Database Schema

### Grades Table
- `id`: Primary key
- `name`: Grade name (unique)
- `description`: Grade description
- `level`: Grade level (unique)
- `is_active`: Active status
- `created_at`: Creation timestamp
- `updated_at`: Last update timestamp

### Student Grades Table
- `id`: Primary key
- `student_id`: Student ID
- `grade_id`: Grade ID (foreign key)
- `start_date`: Enrollment start date
- `end_date`: Enrollment end date
- `is_active`: Active status
- `created_at`: Creation timestamp
- `updated_at`: Last update timestamp

## 🤝 Contributing

1. Follow Clean Architecture principles
2. Write comprehensive tests
3. Follow Kotlin coding standards
4. Update documentation as needed

## 📄 License

This project is part of the educational microservices system.