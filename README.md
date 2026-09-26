# Guardian — Order Service

Microservice responsible for order management in an event-driven e-commerce system. Communicates with the catalog-service to validate product existence before creating an order, and publishes events to Kafka for asynchronous processing.

## Tech Stack

- Java 17
- Spring Boot 3.5.x
- Spring Data JPA
- PostgreSQL 16
- Flyway
- Docker
- Lombok
- RestTemplate
- Apache Kafka (Spring Kafka)

## Architecture

The project follows a layered architecture with clear separation of concerns:

- **domain** → `Order` entity with business rules, `OrderStatus` enum and `OrderStatusTransitionValidator`
- **application** → use cases representing system actions
- **infrastructure** → JPA repository, HTTP client (CatalogClient), and Kafka event (OrderCreatedEvent)
- **web** → REST controllers, DTOs, and global error handling

### Technical Decisions

- **UUID** as ID instead of Long — avoids collision between microservices
- **Flyway** instead of ddl-auto — schema version control
- **RestTemplate** for synchronous HTTP communication with catalog-service
- **Separate database** — each microservice owns its data (database per service pattern)
- **Environment variables** — credentials and URLs configured via environment variables
- **OrderStatus enum with transition validation** — prevents invalid status changes and out-of-order event handling (see DECISIONS.md)
- **Apache Kafka** — order-service publishes an `order.created` event after creating an order, enabling asynchronous communication with future consumers (payment-service)

## Configuration

| Property | Description | Default |
|---|---|---|
| `DB_URL` | Database URL | `jdbc:postgresql://localhost:5434/guardian_order` |
| `DB_USERNAME` | Database username | `guardian` |
| `DB_PASSWORD` | Database password | `guardian` |
| `CATALOG_SERVICE_URL` | Base URL of catalog-service | `http://localhost:8081` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka broker address | `localhost:9092` |

## How to Run

### Prerequisites
- Docker
- Java 17
- catalog-service running on port 8081
- Kafka broker running on port 9092

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
    "status": "AWAITING_PAYMENT"
}
```
Returns `200 OK` with the updated order, or `400 Bad Request` if the status transition is invalid. Returns `404 Not Found` if order not found.

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

## CI

GitHub Actions runs all unit tests automatically on every push to master.

## Communication

This service communicates with **catalog-service** via REST:

POST /orders
→ Validates product existence and retrieves its price: GET http://localhost:8081/products/{productId}
→ If product exists: creates and saves the order, then publishes an `order.created` event to Kafka
→ If product not found: returns 400 Bad Request

This service also publishes events to **Apache Kafka**:

- `order.created` — published after an order is successfully created, carrying the order ID, amount, currency and timestamp. Will be consumed by payment-service (Phase 2, in progress).

## API Documentation

Swagger UI is available when the application is running:
`http://localhost:8082/swagger-ui/index.html`