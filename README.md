# Financial Manager Backend

A Spring Boot-based backend application providing secure authentication (JWT and OAuth2), role-based access control, and APIs for managing family financial assets, accounts, transactions, documents, and alerts.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture Overview](#architecture-overview)
- [Folder Structure](#folder-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Database Setup & Migrations](#database-setup--migrations)
  - [Running the Application](#running-the-application)
  - [Running Tests](#running-tests)
- [API Documentation](#api-documentation)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- JWT-based OAuth2 and Credential authentication flow
- Refresh token persistence and revocation
- Fine-grained Role-Based Access Control (RBAC) with Roles and Privileges
- Multi-device login support with token limits
- Application-wide caching with Redis
- Secure logout with blacklisted JWTs
- CRUD operations for:
  - Users & Roles
  - Families & Memberships
  - Financial Accounts & Assets
  - Transactions with Categories
  - Documents with expiration reminders
  - Alerts

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.4.3
- **Security:** Spring Security (OAuth2, Resource Server, Nimbus JWT)
- **Persistence:** Spring Data JPA, PostgreSQL, Hibernate
- **Caching:** Redis via Spring Cache
- **Build Tool:** Gradle
- **Testing:** JUnit, Mockito
- **Mapping:** MapStruct

## Architecture Overview

> **TODO:** Provide a high-level diagram or link to detailed architecture docs.

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for detailed design, sequence diagrams, and component interactions.

## Folder Structure

```
src/
├── main/
│   ├── java/com/ratnesh/financialmanager
│   │   ├── config          # Security, JWT, Redis configs
│   │   ├── controller      # REST controllers
│   │   ├── dto             # Data Transfer Objects
│   │   ├── exception       # Custom exceptions & handlers
│   │   ├── mapper          # MapStruct mappers
│   │   ├── model           # JPA entities & enums
│   │   ├── repository      # Spring Data repositories
│   │   ├── security        # Filters, providers, and services
│   │   └── service         # Business logic services
│   └── resources/
│       ├── application.yml # Application properties
│       └── db/
│           └── migrations/  # Flyway or Liquibase scripts
└── test/
    └── java/com/ratnesh/financialmanager  # Unit and integration tests
```

## Getting Started

### Prerequisites

- Java 21
- Maven 3.16.4+
- PostgreSQL 12+
- Redis

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/theyorouzoya/financial-manager.git
   cd financial-manager
   ```

2. Build the project:

   ```bash
   ./gradlew clean build
   ```

### Configuration

The `application.yml` file contains the necessary configurations for the application. The `spring.config.import` variable allows importing environment values via a `.env` file in the root folder. While some defaults are provided for most variables, you need to set some of the environment variables to their proper values for the app to run.

#### Environment Variables

To support OAuth login via GitHub, [create a GitHub OAuth app as shown here](https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/creating-an-oauth-app). Once you've obtained
the your GitHub client ID and have generated a client secret, set the following environment
variables to their appropriate value:
- `GITHUB_CLIENT_ID`
- `GITHUB_CLIENT_SECRET`

This application uses a hardcoded asymmetric key-pair to sign and encode JWTs. First, generate an RSA key pair. For example, you can use OpenSSL to generate them for you:

```bash
# Generate private key (2048-bit RSA)
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048

# Extract the public key from the private key
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

Place the generated keys in a location of your choice (preferably on the classpath, for example, `/resources/keys/`) and set the following environment variables to point to their location:
- `PUBLIC_KEY_PATH`
- `PRIVATE_KEY_PATH`
If you're putting them on the classpath as shown above, then you can refer to it directly as `classpath:keys/`. Otherwise, you will need to supply the full directory path to the location.

The rest of the environemnt variables have provided default values, so as long as you have the expected defaults on everything, the app should run just fine. Here's the list of variables along with their default values:
- `REDIS_HOST`: `localhost`. Your default host url for the Redis server.
- `REDIS_PORT`: `6379`. The post the Redis server accepts connections on.
- `REDIS_TIMEOUT`: `30000`. The connection timeout value (in ms) for the Redis server.
- `JWT_EXPIRATION_MS`: `900000`. The expiration time (in ms) for a JWT. Default is 15 minutes.
- `REFRESH_TOKEN_EXPIRATION_MS`: `604800000`. The expiration time (in ms) for refresh tokens. Default is 7 days.

### Database Setup & Migrations

By default, the application expects a database by the name of `familyfinance` to exist. So if you want to use the default value, create a postgres database by that name. Otherwise, setup your db and supply the following environment variables:
- `DATABASE_URL`: `jdbc:postgresql://localhost:5432/familyfinance`. Your postgres database connection url.
- `DATABASE_USERNAME`: `postgres`
- `DATABASE_PASSWORD`: `postgres`

This project uses Flyway for database migrations. Migration scripts are located under `src/main/resources/db/migrations`.

To run migrations:

```bash
mvn flyway:migrate
```

### Running the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api/` by default.

### Running Tests

Run all unit and integration tests:

```bash
mvn test
```

## API Documentation

> **TODO:** Integrate Swagger/OpenAPI and link here.

See [docs/API.md](docs/API.md) for full endpoint documentation, request/response schemas, and examples.

## Deployment

> **TODO:** Describe CI/CD pipeline and deployment steps.

### Docker

> **TODO:** Add Dockerfile and docker-compose instructions.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/YourFeature`
3. Commit your changes: `git commit -m "Add some feature"`
4. Push to the branch: `git push origin feature/YourFeature`
5. Open a Pull Request.

Please ensure tests pass and code follows the project's Checkstyle rules.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

## Contact

- **Project Maintainer:** Your Name ([your.email@example.com](mailto\:your.email@example.com))
- **Repository:** [https://github.com/your-org/family-finance-backend](https://github.com/your-org/family-finance-backend)

