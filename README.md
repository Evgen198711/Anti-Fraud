# Anti-Fraud System

A modular, secure, and extensible Java-based anti-fraud system for processing and evaluating financial transactions in real-time.

---

## Modular Architecture

The system follows a **modular architecture**, organized into the following core modules:

- **user** – Handles user registration, authentication, role management, and account access control.
- **transaction** – Evaluates transactions, applies fraud rules, and processes feedback for dynamic risk scoring.
- **ioc (Indicators of Compromise)** – Maintains blacklists for stolen cards and suspicious IPs, supporting rule-based fraud detection.

---

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Security** for authentication and authorization
- **Spring Data JDBC / JdbcTemplate** for database access
- **H2 Database** (used only for testing purposes)
- **Gradle** for project build

---

## Database Access

The project uses **JdbcTemplate** for data access. An **H2 in-memory database** is used for testing and development. In a production environment, this can be swapped for a persistent relational database.

---

## Security and User Roles

- Authentication and authorization are implemented via **Spring Security**.
- The system defines four user roles:
  - **ADMINISTRATOR**: Manages users, access rights, and roles.
  - **MERCHANT**: Submits transactions and provides feedback.
  - **SUPPORT**: Views and manages IPs, stolen cards, and transaction history.
  - **Anonymous**: Limited to registration only.
- The **first registered user** becomes an **ADMINISTRATOR** by default.
- All other users are assigned the **MERCHANT** role and are **locked by default**.
- **Administrators** can **lock** or **unlock** accounts.

---

## Endpoint Access Control

| Endpoint                          | Method          | Anonymous | MERCHANT | ADMINISTRATOR | SUPPORT |
| --------------------------------- | --------------- | --------- | -------- | ------------- | ------- |
| `/api/auth/user`                  | POST            | +         | +        | +             | +       |
| `/api/auth/user`                  | DELETE          | -         | -        | +             | -       |
| `/api/auth/list`                  | GET             | -         | -        | +             | +       |
| `/api/antifraud/transaction`      | POST            | -         | +        | -             | -       |
| `/api/antifraud/suspicious-ip`    | GET/POST/DELETE | -         | -        | -             | +       |
| `/api/antifraud/stolencard`       | GET/POST/DELETE | -         | -        | -             | +       |
| `/api/antifraud/history`          | GET             | -         | -        | -             | +       |
| `/api/antifraud/transaction`      | PUT             | -         | -        | -             | +       |

Legend: `+` Access granted | `-` Access denied

---

## Transaction Evaluation

Transactions are processed using a set of rule classes that all implement a shared interface. The system is designed using the **Chain of Responsibility design pattern**, enabling modular, sequential rule evaluation.

### Evaluation Result

A transaction response includes:

- **Result**: One of `ALLOWED`, `MANUAL_PROCESSING`, or `PROHIBITED`

- **Info**: Justification for the decision. This may be `"none"` if the result is `ALLOWED`.

---

## Feedback and Risk Adjustment

- Feedback can be provided for each transaction.

- Based on feedback:

  - **Limits** for `ALLOWED` and `MANUAL_PROCESSING` are **adjusted**.

  - Positive feedback increases trust and raises limits.

  - Negative feedback reduces limits to tighten risk control.

---

## Development and Testing

- Start the application: `./gradlew bootRun`

- Use the H2 Console: Navigate to `/h2-console`

- All settings can be managed via `application.properties`

---
