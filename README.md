# Anti-Fraud System 🛡️

A Spring Boot application that analyzes card transactions in real time to detect and prevent fraudulent activity. The system classifies transactions based on risk, supports adaptive feedback to refine thresholds, and enforces role-based access control.

---

## 🧱 Modular Architecture

This project uses a **domain-oriented modular architecture** to reflect real-world fraud detection concerns and maintain clear separation of responsibilities.

### Modules:

- **user** – Handles registration, authentication, role management, and account access control.
- **transaction** – Evaluates transactions, applies fraud rules, and processes feedback for dynamic risk scoring.
- **ioc (Indicators of Compromise)** – Maintains blacklists for stolen cards and suspicious IPs, and supports rule-based fraud detection.

---

## 🔌 REST API Overview

| Method | Endpoint | Access Role | Description |
|--------|----------|-------------|-------------|
| POST | `/api/auth/user` | anonymous → `ADMINISTRATOR` | Create first user |
| POST | `/api/auth/user` | `ADMINISTRATOR` | Add user |
| DELETE | `/api/auth/user/{username}` | `ADMINISTRATOR` | Delete user |
| GET | `/api/auth/list` | `ADMINISTRATOR`, `SUPPORT` | List users |
| PUT | `/api/auth/role` | `ADMINISTRATOR` | Modify user role |
| PUT | `/api/auth/access` | `ADMINISTRATOR` | Lock/unlock user |
| POST | `/api/antifraud/transaction` | `MERCHANT` | Evaluate a transaction |
| PUT | `/api/antifraud/transaction` | `SUPPORT` | Submit feedback |
| GET | `/api/antifraud/history` | `SUPPORT` | View all transactions |
| GET | `/api/antifraud/history/{number}` | `SUPPORT` | View by card number |
| POST | `/api/antifraud/stolencard` | `SUPPORT` | Add stolen card |
| GET | `/api/antifraud/stolencard` | `SUPPORT` | List stolen cards |
| DELETE | `/api/antifraud/stolencard/{number}` | `SUPPORT` | Remove card |
| POST | `/api/antifraud/suspicious-ip` | `SUPPORT` | Add IP address |
| GET | `/api/antifraud/suspicious-ip` | `SUPPORT` | List suspicious IPs |
| DELETE | `/api/antifraud/suspicious-ip/{ip}` | `SUPPORT` | Remove IP |


---

## ⚙️ Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Spring Security 6**
- **Spring Web / MVC**
- **PostgreSQL / H2**
- **Gradle (multi-module)**
- **Lombok**

---

## 🔒 Security & Roles

| Role | Capabilities |
|------|--------------|
| `ADMINISTRATOR` | User/role management |
| `SUPPORT` | Fraud analysis, blacklists, feedback |
| `MERCHANT` | Transaction screening |

Authentication is stateless. Passwords are stored using BCrypt hashing.

---

## 🧠 Adaptive Fraud Detection

The system uses multiple fraud rules including:

- Amount thresholds
- IP & card blacklists
- Region and IP correlation

Feedback submitted by support agents dynamically adjusts transaction limits using the formula:

```
newLimit = 0.8 * currentLimit ± 0.2 * transactionAmount
```

---

## 🚀 Running the Project

### With embedded H2:

```bash
./gradlew :infra:bootRun
```

### With PostgreSQL using Docker:

```bash
docker-compose up -d
./gradlew :infra:bootRun
```


---

## 💬 Contributing

Pull requests and issues are welcome. For major changes, open a discussion first.


