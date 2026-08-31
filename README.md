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

## Configuration

| Property | Description | Default |
|---|---|---|
| `catalog.service.url` | Base URL of catalog-service | `http://localhost:8081` |

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

### List all orders

GET /orders

Returns `200 OK` with a list of all orders.

### Get order by ID

GET /orders/{id}

Returns `200 OK` with the order or `404 Not Found`.

### Create order

POST /orders

```json
{
    "productId": "uuid-of-existing-product",
    "quantity": 2
}
```
Returns `201 Created` with the created order, or `400 Bad Request` if product does not exist in catalog-service.

### Update order status

PATCH /orders/{id}/status

```json
{
    "status": "CONFIRMED"
}
```
Returns `200 OK` with the updated order, or `400 Bad Request` if the order is cancelled or status is invalid. Returns `404 Not Found` if order not found.

## Testing

Unit tests implemented with JUnit 5 and Mockito, covering all use cases:

- `CreateOrderUseCase` — product exists (success) and product not found (exception)
- `GetOrderByIdUseCase` — order found and order not found
- `GetAllOrdersUseCase` — returns all orders
- `UpdateOrderStatusUseCase` — status updated successfully and order not found

Run tests:
```bash
./mvnw test
```

## Communication

This service communicates with **catalog-service** via REST:

POST /orders
→ Validates product existence: GET http://localhost:8081/products/{productId}
→ If product exists: creates and saves the order
→ If product not found: returns 400 Bad Request

## API Documentation

Swagger UI is available when the application is running:
`http://localhost:8082/swagger-ui/index.html`