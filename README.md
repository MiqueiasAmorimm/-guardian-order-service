# Guardian — Order Service

Microservice responsible for order management in an event-driven e-commerce system. Communicates with the catalog-service to validate product existence and get its price before creating an order, publishes `order.created` to Kafka, and consumes `payment.approved` to update the order status.

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
- **infrastructure** → JPA repository, HTTP client (`CatalogClient`), and Kafka (`OrderCreatedEvent`, `PaymentApprovedEvent`, `PaymentApprovedConsumer`)
- **web** → REST controllers, DTOs, and global error handling

### Technical Decisions

- **UUID** as ID instead of Long — avoids collision between microservices
- **Flyway** instead of ddl-auto — schema version control
- **RestTemplate** for synchronous HTTP communication with catalog-service
- **Separate database** — each microservice owns its data (database per service pattern)
- **Environment variables** — credentials and URLs configured via environment variables
- **OrderStatus enum with transition validation** — prevents invalid status changes and out-of-order event handling (see DECISIONS.md)
- **Order moves to `AWAITING_PAYMENT` before it is saved and published** — the state is already correct when the payment-service answers
- **Apache Kafka** — order-service publishes `order.created` and consumes `payment.approved`, so it never waits for the payment-service
- **Own copy of `PaymentApprovedEvent`** — no shared code with payment-service; only the JSON contract connects them

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
- payment-service running on port 8083 (to see the full cycle)

### Starting the infrastructure

The `docker-compose.yml` lives in the catalog-service repository. From that folder:

```bash
docker-compose up -d
```

This starts the PostgreSQL instances (catalog, order and payment) and the Kafka broker.

### Running the application

```bash
./mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"
```

The application runs on port `8082`.

## Endpoints

### List all orders

GET /orders

Returns `200 OK` with a page of orders.

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
Returns `201 Created` with the created order (status `AWAITING_PAYMENT`), or `400 Bad Request` if the product is not found in catalog-service.

### Update order status

PATCH /orders/{id}/status

```json
{
    "status": "AWAITING_PAYMENT"
}
```
Returns `200 OK` with the updated order, or `400 Bad Request` if the status transition is invalid. Returns `404 Not Found` if order not found.

## Communication

This service communicates with **catalog-service** via REST:

POST /orders
→ Retrieves the product and its price: GET http://localhost:8081/products/{productId}
→ Creates the order, moves it to `AWAITING_PAYMENT`, saves it and publishes `order.created` to Kafka
→ If the product is not found: returns 400 Bad Request

This service also uses **Apache Kafka**:

- **Publishes** `order.created` — after an order is created. It carries the order ID, the order total (price × quantity), the currency and a timestamp. Consumed by payment-service.
- **Consumes** `payment.approved` — published by payment-service when a payment is approved. The order is moved to `APPROVED`.

Example `payment.approved` payload:

```json
{
  "orderId": "7508a9dc-1f38-4d1b-ae7b-99dc042b583a",
  "paymentId": "dc5644b2-de66-43d8-9d13-8ed362cca1e3",
  "amount": 200.00,
  "currency": "BRL"
}
```

If the transition is not allowed (for example, an order still in `CREATED` receives an approval), the state machine refuses it, the error is logged and the consumer keeps reading the next messages.

## Known limitations

- A repeated `payment.approved` for an order that is already `APPROVED` is refused by the state machine and logged as an error, instead of being ignored.
- `payment.rejected` is not consumed yet.
- Saving the order and publishing the event are not atomic (planned: transactional outbox).
- The Kafka consumer and the move to `AWAITING_PAYMENT` have no automated tests yet.

## Testing

Unit tests implemented with JUnit 5 and Mockito, covering the use cases (7 tests):

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

## Related repositories

- [catalog-service](https://github.com/MiqueiasAmorimm/guardian-catalog-service) — product catalog, and where the shared `docker-compose.yml` lives
- [payment-service](https://github.com/MiqueiasAmorimm/guardian-payment-service) — consumes `order.created`, stores the payment and publishes `payment.approved`

## API Documentation

Swagger UI is available when the application is running:
`http://localhost:8082/swagger-ui/index.html`