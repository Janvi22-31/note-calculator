# Note Calculator

Calculate the number of currency notes easily.

A simple full-stack web application that calculates how many currency notes
of a chosen denomination are needed for a given amount, and keeps a history
of past calculations.

---

## Project Description

You enter an amount (e.g. ₹5000) and pick a note denomination (e.g. ₹500).
The app calculates:

- **Number of notes** = amount ÷ denomination (whole number)
- **Remaining amount** = amount % denomination (what's left over)

Example: ₹530 with ₹500 notes → **1 note**, **₹30 remaining**.

Every successful calculation is saved to a MySQL database and shown on the
History page.

---

## Features

- Enter an amount and pick a denomination from a dropdown (₹10, ₹50, ₹100, ₹200, ₹500, ₹2000)
- Instant calculation via a REST API call to the backend
- Frontend **and** backend input validation with friendly error messages
- Calculation history stored in MySQL, viewable on a dedicated History page
- Delete a single history entry, or clear all history
- Clean, responsive UI that works on desktop, tablet, and mobile
- Proper layered Spring Boot backend (Controller → Service → Repository)
- Centralized JSON error handling
- Unit tests for the calculation logic

---

## Technology Stack

**Frontend:** HTML5, CSS3, JavaScript (`fetch()` for API calls)
**Backend:** Java, Spring Boot (Spring Web, Spring Data JPA), Maven
**Database:** MySQL
**Communication:** REST API (JSON over HTTP)

---

## Project Structure

```
note-calculator/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/notecalculator/
│   │   │   ├── NoteCalculatorApplication.java
│   │   │   ├── controller/CalculationController.java
│   │   │   ├── service/CalculationService.java
│   │   │   ├── repository/CalculationRepository.java
│   │   │   ├── entity/Calculation.java
│   │   │   ├── dto/CalculationRequest.java
│   │   │   ├── dto/CalculationResponse.java
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       ├── InvalidDenominationException.java
│   │   │       ├── CalculationNotFoundException.java
│   │   │       └── ErrorResponse.java
│   │   └── resources/application.properties
│   └── test/java/com/example/notecalculator/service/CalculationServiceTest.java
│
├── frontend/
│   ├── index.html      (Calculator page)
│   ├── history.html    (History page)
│   ├── about.html      (About page)
│   ├── style.css
│   └── script.js
│
├── pom.xml
└── README.md
```

---

## Database Setup

### 1. Install MySQL (if not already installed)

Make sure MySQL Server is installed and running on your machine.

### 2. Create the database

Open a MySQL client (MySQL Workbench, terminal, etc.) and run:

```sql
CREATE DATABASE note_calculator;
```

You don't need to create the `calculations` table yourself — Spring Boot
(via Hibernate/JPA) will automatically create and update it for you, based
on the `Calculation` entity, the first time you run the app.

### 3. Table that will be auto-created

```sql
CREATE TABLE calculations (
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    amount            DECIMAL(10,2) NOT NULL,
    denomination      INT NOT NULL,
    number_of_notes   INT NOT NULL,
    remaining_amount  DECIMAL(10,2) NOT NULL,
    calculated_at     DATETIME NOT NULL
);
```

---

## Backend Setup

### 1. Enter your MySQL credentials

Open `src/main/resources/application.properties` and replace the
placeholders with your own MySQL username and password:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/note_calculator?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=YOUR_USERNAME   <-- change this
spring.datasource.password=YOUR_PASSWORD   <-- change this
```

For a default local MySQL install, the username is often `root`, and the
password is whatever you set during installation.

### 2. Build and run the backend

From the project's root folder (where `pom.xml` is located):

```bash
mvn spring-boot:run
```

Or build a jar and run it:

```bash
mvn clean package
java -jar target/note-calculator-1.0.0.jar
```

The backend will start on **http://localhost:8080**.

You should see Hibernate logs in the console showing the `calculations`
table being created (because `spring.jpa.hibernate.ddl-auto=update`).

---

## Frontend Setup

The frontend is plain HTML/CSS/JavaScript — no build tools needed.

**Option A — Open directly in browser**
Just double-click `frontend/index.html` (or open it in your browser).

**Option B — Use a local server (recommended)**
If you have VS Code, install the "Live Server" extension, right-click
`frontend/index.html`, and choose "Open with Live Server". This avoids
some browsers' restrictions on `file://` pages calling APIs.

Either way, make sure the backend (step above) is already running on port
8080, since the frontend calls `http://localhost:8080/api/calculations`.

> CORS is already enabled on the backend (`@CrossOrigin(origins = "*")`),
> so the frontend can be served from any port without issues.

---

## How to Run (Quick Summary)

1. `CREATE DATABASE note_calculator;` in MySQL
2. Set your MySQL username/password in `application.properties`
3. `mvn spring-boot:run` to start the backend on port 8080
4. Open `frontend/index.html` in your browser
5. Enter an amount, pick a denomination, click **Calculate**

---

## REST API Documentation

Base URL: `http://localhost:8080/api/calculations`

| Method | Endpoint                 | Description                     |
|--------|---------------------------|----------------------------------|
| POST   | `/api/calculations`       | Create a new calculation         |
| GET    | `/api/calculations`       | Get all calculation history      |
| GET    | `/api/calculations/{id}`  | Get one calculation by ID        |
| DELETE | `/api/calculations/{id}`  | Delete one calculation by ID     |
| DELETE | `/api/calculations`       | Delete all history               |

### Example: Create a calculation

**Request**
```
POST /api/calculations
Content-Type: application/json

{
  "amount": 5000,
  "denomination": 500
}
```

**Response `201 CREATED`**
```json
{
  "id": 1,
  "amount": 5000,
  "denomination": 500,
  "numberOfNotes": 10,
  "remainingAmount": 0,
  "calculatedAt": "2026-09-13T10:15:30"
}
```

### Example: Amount with a remainder

**Request**
```json
{
  "amount": 530,
  "denomination": 500
}
```

**Response**
```json
{
  "id": 2,
  "amount": 530,
  "denomination": 500,
  "numberOfNotes": 1,
  "remainingAmount": 30,
  "calculatedAt": "2026-09-13T10:20:00"
}
```

### Example: Invalid amount

**Request**
```json
{
  "amount": -100,
  "denomination": 500
}
```

**Response `400 BAD REQUEST`**
```json
{
  "error": "Invalid amount",
  "message": "Amount must be greater than zero"
}
```

### Example: Invalid denomination

**Request**
```json
{
  "amount": 1000,
  "denomination": 300
}
```

**Response `400 BAD REQUEST`**
```json
{
  "error": "Invalid denomination",
  "message": "Denomination must be one of [10, 50, 100, 200, 500, 2000]"
}
```

### Example: Get calculation by ID that doesn't exist

**Response `404 NOT FOUND`**
```json
{
  "error": "Not found",
  "message": "Calculation not found with id: 999"
}
```

---

## Screenshots

_Add your own screenshots here after running the app, e.g.:_

- `screenshots/calculator-page.png`
- `screenshots/result-card.png`
- `screenshots/history-page.png`
- `screenshots/about-page.png`

---

## Future Enhancements

- User accounts / login so each user has their own history
- Pagination for large history lists
- Support for coins in addition to notes
- Export history as CSV/PDF
- Dark mode
- Deploy backend + frontend to the cloud (e.g. Render, Railway, Vercel)

---

## Running Tests

```bash
mvn test
```

This runs `CalculationServiceTest`, which covers:

1. Valid calculation
2. Amount smaller than denomination
3. Amount exactly divisible by denomination
4. Amount with a remainder
5. Zero amount
6. Negative amount
7. Invalid denomination
