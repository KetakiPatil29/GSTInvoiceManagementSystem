# GST Invoice Management System

A full-stack web application for creating and managing GST-compliant invoices. It covers customers, products, users, tax calculation (CGST / SGST / IGST), invoice numbering, and PDF generation.

The UI is branded **Smart Invoice Generator**. The seller company on generated PDFs is **Bright Traders**, located in **Maharashtra**.

## Features

- **Login** with email, password, and role (`admin`, `accountant`, `viewer`)
- **Role-based home actions**
  - Admin: create invoices, manage customers, manage products, add users, view invoices
  - Accountant: create invoices, view invoices
  - Viewer: view invoices
- **Customer management** — name, GSTIN, state, email, phone, address
- **Product catalog** — name, description, HSN code, GST rate, unit price
- **Invoice creation** — select customer and line items, auto-calculate tax and totals
- **Invoice numbering** in the format `INV-00001`, `INV-00002`, …
- **PDF invoices** generated with iText (seller, customer, line items, tax summary)
- **Safe delete checks** — customers and products linked to invoices cannot be deleted
- **OpenAPI / Swagger** documentation for REST APIs

## Tech Stack

| Layer | Technology |
| --- | --- |
| Backend | Java 17, Spring Boot 3.5.5 |
| Persistence | Spring Data JPA, Hibernate (`ddl-auto=update`) |
| Database | MySQL (`gstinvoice`) |
| API docs | springdoc-openapi |
| PDF | iText 5, Apache PDFBox |
| Frontend | React 19 (Create React App) |
| UI | Material UI (MUI), CSS |
| HTTP | Axios |
| Routing | React Router |

## Project Structure

```
GSTInvoiceManagementSystem/
├── pom.xml
├── src/main/java/com/gstinvoice/
│   ├── GstInvoiceManagementSystemApplication.java
│   ├── Controller/          # REST controllers
│   ├── service/             # Business logic and PDF generation
│   ├── Repository/          # JPA repositories
│   ├── entity/              # JPA entities
│   ├── model/request|response/
│   └── config/              # CORS (and commented security/swagger)
├── src/main/resources/application.properties
├── src/main/frontend/       # React UI
└── src/test/java/           # Service tests
```

## Prerequisites

- **JDK 17**
- **Maven 3.8+** (or use the included `mvnw` / `mvnw.cmd` wrapper)
- **Node.js 18+** and npm
- **MySQL 8** running locally

## Database Setup

1. Start MySQL.
2. Create the database:

```sql
CREATE DATABASE gstinvoice;
```

3. Update credentials in `src/main/resources/application.properties` if they differ from the defaults:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gstinvoice
spring.datasource.username=username
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

Tables (`users`, `customers`, `products`, `invoices`, `invoice_items`) are created/updated automatically on first backend start.

### First user

There is no public signup. Create an admin user either:

- Through the **Add User** screen after logging in as admin, or
- By inserting a row into `users` (roles stored as lowercase strings: `admin`, `accountant`, `viewer`):

```sql
INSERT INTO users (username, email, password, role, created_at)
VALUES ('Admin', 'admin@example.com', 'yourpassword', 'admin', NOW());
```

Login matches **email + password + role** exactly.

## How to Run

Run backend and frontend separately.

### 1. Backend (Spring Boot)

From the project root:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

API base URL: [http://localhost:8080](http://localhost:8080)

Swagger UI (springdoc): [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 2. Frontend (React)

```bash
cd src/main/frontend
npm install
npm start
```

UI: [http://localhost:3000](http://localhost:3000)

CORS is configured so the React app on port 3000 can call the API on port 8080.

## User Roles

| Role | Access |
| --- | --- |
| `admin` | Customers, products, users, create invoice, view invoices |
| `accountant` | Create invoice, view invoices |
| `viewer` | View invoices |

Invoice statuses used in the domain: `DRAFT`, `FINALIZED`, `PAID`.

## GST Calculation

Seller state is hardcoded as **Maharashtra**.

| Customer state | Tax applied |
| --- | --- |
| Same as seller (Maharashtra) | CGST + SGST (GST rate split equally) |
| Different state | IGST (full GST rate) |

Grand total = line-item amounts + applicable GST.

GSTIN is validated as a 15-character pattern. Phone numbers must be 10 digits.

## REST API

All endpoints are under `/api`.

### Auth

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/login` | Login (`email`, `password`, `role`) |

### Users

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/users` | Create user |
| GET | `/api/users` | List users |
| PUT | `/api/users/{id}` | Update user |
| GET | `/api/users/byRole?role=` | Filter by role |

### Customers

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/customers` | Create customer |
| GET | `/api/customers` | List customers |
| GET | `/api/customers/{id}` | Get by ID |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |
| GET | `/api/customers/{id}/can-delete` | `true` if not used on invoices |

### Products

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/products` | Create product |
| GET | `/api/products` | List products |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |
| GET | `/api/products/{id}/can-delete` | `true` if not used on invoices |

### Invoices

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/invoices` | Create invoice |
| GET | `/api/invoices` | List invoices |
| GET | `/api/invoices/{id}` | Get by ID |
| PUT | `/api/invoices/{id}` | Update invoice |
| DELETE | `/api/invoices/{id}` | Delete invoice |
| GET | `/api/invoices/{id}/pdf` | Download/view PDF |

#### Create invoice body (example)

```json
{
  "customer_id": 1,
  "invoiceDate": "2026-08-17",
  "status": "FINALIZED",
  "items": [
    {
      "product_id": 1,
      "quantity": 2,
      "rate": 100,
      "amount": 100
    }
  ]
}
```

CGST, SGST, IGST, and grand total are calculated on the server from customer state and product GST rates.

## Frontend Routes

| Path | Screen |
| --- | --- |
| `/`, `/Login` | Login |
| `/HomePage` | Dashboard |
| `/Customers` | Customer CRUD |
| `/Products` | Product CRUD |
| `/InvoiceForm` | Create invoice |
| `/User` | Add user |
| `/ViewInvoice` | Invoice list + PDF |

## Tests

Backend unit/service tests live under `src/test/java/com/gstinvoice/service/`.

```bash
./mvnw test
```

Frontend tests (Create React App):

```bash
cd src/main/frontend
npm test
```

## Configuration Notes

- Default Spring Boot port is **8080** (not overridden in `application.properties`).
- Hibernate `ddl-auto=update` is convenient for development; use a migration tool for production.
- Spring Security is present in the repo but currently commented out. Login is application-level, not JWT/session-based.
- Seller name, GSTIN, and address on PDFs are currently hardcoded in `InvoiceService`.
