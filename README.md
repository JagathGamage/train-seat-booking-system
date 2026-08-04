# 🚆 Train Seat Booking System

A **segment-based train seat booking system** for the **Colombo Fort → Badulla** railway route.

Unlike traditional booking systems, this application allows **multiple passengers to reserve the same seat for different journey segments**, while preventing reservations on overlapping portions of the route.

---

## ✨ Features

- Search trains by travel date and stations
- View train schedules
- View available seats
- Interactive seat selection
- Segment-based seat booking
- Dynamic fare calculation
- Store passenger details
- Prevent overlapping bookings
- Manage trains
- Manage stations
- Manage coaches
- Manage seats
- Manage train schedules
- Configure fare settings

---

# 🌟 Extra Credit Features

## 1️⃣ Interactive Seat Map

Instead of entering seat numbers manually, passengers can visually select seats.

### Features

- Coach layout visualization
- Available seats highlighted
- Reserved seats disabled
- Easy seat selection
- Better user experience

---

## 2️⃣ Dynamic Fare Calculation

Fare is calculated dynamically instead of using a fixed value.

Pricing considers:

- Distance travelled
- Base fare
- Reserved coach premium
- Peak hour surcharge
- Weekend surcharge
- Demand-based pricing

The fare calculation logic is isolated from booking logic, making future pricing changes simple.

---

# 🛠 Technology Stack

| Layer | Technology |
|---------|------------|
| Backend | Java 21 |
| Framework | Spring Boot |
| ORM | Spring Data JPA + Hibernate |
| Build Tool | Maven |
| Frontend | React + Vite |
| UI | Material UI |
| Database | Microsoft SQL Server 2022 |
| Deployment | Docker + Docker Compose |
| Web Server | Nginx |

---

# 🏗 System Architecture

```
React + Material UI
          │
          │ REST API
          ▼
Spring Boot Backend
          │
          │ JPA/Hibernate
          ▼
SQL Server Database
```

---

# 📦 Core Design Decisions

## 1️⃣ Segment-Based Seat Reservation

### Problem

A traditional booking system simply marks a seat as **Booked**.

```
Seat A1
Status = BOOKED
```

This works for buses or flights but **not for trains**.

Example:

Passenger A

```
Colombo Fort ─────────► Kandy
```

Passenger B

```
Kandy ─────────► Badulla
```

Both passengers should be able to use the same seat.

---

### ✅ Solution

Each booking stores:

- Seat
- Train Schedule
- Origin Station
- Destination Station

instead of only a seat status.

Example

```
Booking 1

Seat : A1

Colombo Fort ─────► Kandy
```

```
Booking 2

Seat : A1

Kandy ─────────────► Badulla
```

Both bookings are valid because their journey segments do not overlap.

---

## 🔍 Conflict Detection Logic

A booking is rejected **only if journey segments overlap.**

```
Existing Origin      < New Destination
AND
Existing Destination > New Origin
```

If both conditions are true

❌ Booking Rejected

Otherwise

✅ Booking Allowed

This approach maximizes seat utilization while preventing double booking.

---

### ❌ Alternative Considered

Store seat status.

```
Seat A1

BOOKED
```

### Why rejected?

- Cannot support partial journeys
- Wastes available seats
- Cannot reuse seats after passengers leave

---

# 🗄 Database Design

## Main Entities

```
Train
│
├── TrainSchedule
│
├── Coach
│
├── Seat
│
└── Booking

Station

FareConfig
```

---

## Train

Stores static train information.

Examples

- Train Number
- Train Name

---

## Train Schedule

Stores daily operations.

Contains

- Travel Date
- Departure Time
- Status

### Why?

A train operates on many different dates.

Keeping schedules separate avoids duplication.

---

## Booking

Stores

- Passenger
- Seat
- Origin
- Destination
- Fare
- Booking Time

A booking represents an entire passenger journey.

---

### ❌ Alternative Considered

Store booking information directly in Seat.

```
Seat

id

seat_number

status
```

### Why rejected?

- Only one booking possible
- Cannot store passenger details
- Cannot support segment booking

---

# 💰 Fare Calculation Design

## Design

Fare calculation is implemented as a separate service.

```
BookingService
       │
       ▼
FareCalculationService
       │
       ▼
FareConfig
```

---

## Current Fare Formula

```
Final Fare =
Round(

Base Fare
+ Distance Fare
+ Reserved Premium
+ Peak Hour Charge
+ Weekend Charge
+ Demand Charge

)
```

### Pricing Factors

✅ Base Fare

Fixed charge for every booking.

---

✅ Distance Fare

```
(Destination Order − Origin Order)
× Price Per Segment
```

---

✅ Reserved Coach Premium

Extra charge for reserved coaches.

---

✅ Peak Hour Charge

Applied for departures between:

- 6:00 AM – 9:00 AM
- 4:00 PM – 7:00 PM

---

✅ Weekend Charge

Applied on

- Saturday
- Sunday

---

✅ Demand Charge

Based on reserved seat occupancy.

| Occupancy | Charge |
|-----------|---------|
| ≥ 90% | Highest |
| ≥ 70% | Medium |
| ≥ 40% | Low |
| < 40% | None |

---

## Configuration

Pricing values are stored in the database.

Examples

```
BASE_FARE

PRICE_PER_SEGMENT

RESERVED_PREMIUM

PEAK_PERCENT

WEEKEND_PERCENT

DEMAND_PERCENT
```

No code changes are required to update pricing.

---

# 🐳 Docker Setup

Services included:

| Service | Purpose |
|-----------|---------|
| mssql | SQL Server |
| db-init | Database initialization |
| backend | Spring Boot API |
| frontend | React + Nginx |

---

## Environment Variables

Sensitive values are stored in:

```
.env
```

Example

```env
DB_URL=jdbc:sqlserver://mssql:1433;databaseName=train_booking;encrypt=true;trustServerCertificate=true

DB_USERNAME=sa

DB_PASSWORD=your_password

MSSQL_SA_PASSWORD=your_password
```
> [!IMPORTANT]
> **`DB_PASSWORD` and `MSSQL_SA_PASSWORD` must use the **same password**.**
>
> Using different passwords will prevent the backend from connecting to the SQL Server database. 
> A `.env.example` file is included.

---

# 🚀 Running the Application

## Prerequisites

- Docker Desktop
- Git

---

## Clone

```bash
git clone <repository-url>

cd train-seat-booking-system
```

---

## Create Environment File

Create

```
.env
```

using

```
.env.example
```

---

## Start

```bash
docker compose up --build
```

Docker automatically starts

- SQL Server
- Database Initialization
- Spring Boot Backend
- React Frontend

---

# 🌐 Application URLs

| Service | URL |
|----------|-----|
| Frontend | http://localhost:3000 |
| Backend | http://localhost:8080 |
| SQL Server | localhost:1433 |

---

# 🚧 Challenges Faced

## 1️⃣ Segment-Based Seat Booking

### Challenge

Allowing multiple passengers to reserve the same seat on different portions of the journey while preventing overlapping reservations.

### Solution

Bookings store origin and destination stations.

The booking service compares station order to detect overlaps.

This ensures:

- Maximum seat utilization
- No double booking
- Correct reservation validation

---

## 2️⃣ Flexible Fare Calculation

### Challenge

A fixed fare was too limited and embedding pricing logic inside the booking service reduced maintainability.

### Solution

A dedicated `FareCalculationService` calculates fares using configurable pricing values.

Benefits

- Easy pricing updates
- Better separation of concerns
- Future support for promotions and discounts

---

## 3️⃣ Docker Networking

### Challenge

The backend initially attempted to connect to SQL Server using `localhost`.

Inside Docker, this referred to the backend container itself.

### Solution

The backend connects using the Docker service name:

```
mssql
```

Docker's internal networking resolves the correct container automatically.

---

## 4️⃣ Database Initialization

### Challenge

Database scripts sometimes executed before SQL Server was fully ready.

### Solution

A dedicated `db-init` container waits for SQL Server to become healthy before creating the database schema.

This guarantees reliable startup on every deployment.

## 5️⃣ Preventing Simultaneous Double Booking

### Challenge

A critical challenge was preventing multiple users from booking the **same seat for the same or overlapping journey segments at nearly the same time**. If two booking requests are processed concurrently, both could see the seat as available before either reservation is committed, resulting in duplicate bookings.

### Solution

To ensure data consistency, the booking process is executed within a database transaction and uses **pessimistic locking** (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) when retrieving the seat for booking.

When a booking request is being processed, the selected seat is temporarily locked, preventing other transactions from modifying or reserving it until the current transaction completes. The system then performs the segment overlap validation and only confirms the booking if no conflicting reservation exists.

This approach provides:

- Safe concurrent booking handling
- Prevention of duplicate seat reservations
- Consistent booking data under high concurrency
- Reliable seat availability checks even when multiple users book simultaneously

---

# 🎯 Future Improvements

- JWT Authentication
- Role-based Authorization
- Payment Gateway Integration
- Email Ticket Confirmation
- Booking Cancellation
- Seat Hold Timeout
- Live Train Tracking
- Passenger Booking History
- Admin Dashboard
- Fare Discounts & Promotions

---

# 📄 License

This project was developed as part of a **technical assessment** to demonstrate backend architecture, database design, and segment-based booking logic.
