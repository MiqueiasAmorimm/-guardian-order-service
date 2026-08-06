# Guardian — Order Service

Microservice responsible for order management in an event-driven e-commerce system. Communicates with the catalog-service to validate product existence before creating an order.

## Tech Stack

- Java 17
- Spring Boot 3.5.x
- Spring Data JPA
- PostgreSQL 16
- Flyway
- Docker
- Lombok
- RestTemplate

## Architecture

The project follows a layered architecture with clear separation of concerns:

- **domain** → `Order` entity with business rules
- **application** → use cases representing system actions
- **infrastructure** → JPA repository and HTTP client (CatalogClient)
- **web** → REST controllers, DTOs, and global error handling

### Technical Decisions

- **UUID** as ID instead of Long — avoids collision between microservices
- **Flyway** instead of ddl-auto — schema version control
- **RestTemplate** for synchronous HTTP communication with catalog-service
- **Separate database** — each microservice owns its data (database per service pattern)

## How to Run

### Prerequisites
- Docker
- Java 17
- catalog-service running on port 8081

### Starting the database

```bash
docker-compose up -d postgres-order
```

### Running the application

```bash
./mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"
```

The application runs on port `8082`.

## Endpoints

### Get order by ID

GET /orders/{id}

Returns `200 OK` with the order or `404 Not Found`.

### Create order
```
POST /orders
```
```json
{
    "productId": "uuid-of-existing-product",
    "quantity": 2
}
```
Returns `201 Created` with the created order, or `400 Bad Request` if product does not exist in catalog-service.

## Communication

This service communicates with **catalog-service** via REST:

```
POST /orders
→ Validates product existence: GET http://localhost:8081/products/{productId}
→ If product exists: creates and saves the order
→ If product not found: returns 400 Bad Request
```