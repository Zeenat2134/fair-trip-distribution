# 🚖 FairCab: Enterprise Cab Allocation & Fairness Engine

> A deterministic, high-concurrency backend system for distributing cab trips fairly among vendors based on contractual percentages and real-world limits.

## 1. 📖 The "Why" (Project Overview)

Many organizations do not own their own fleets but instead hire cabs from multiple vendors, promising each vendor a specific share of the daily trips (e.g., V1=50%, V2=30%, V3=20%).

**The Problem:** Over thousands of trips, simple round-robin allocation fails to maintain exact mathematical percentages, and floating-point math slowly "drifts" (losing or gaining trips). Furthermore, real-world issues like vendor capacity limits, trip rejections, and concurrent booking race conditions complicate the logic.

**The Solution:** FairCab is a mathematically deterministic fair-division engine. It tracks the exact shortfall between what a vendor was promised and what they actually received, carrying fractions forward day-to-day to guarantee long-term contractual fairness.

## 2. 📸 Live Demo & Visuals

* 🎥 **Video Walkthrough:** [YouTube / Loom Link Here]

## 3. ✨ Key Features & Capabilities

* **Deterministic "Most-Owed-First" Algorithm:** Computes running shortfalls and carries leftover fractions forward. The same trips and configs will always produce the exact same allocation without randomness.
* **Zone & Category Routing:** Handles logic differently based on distance slabs (e.g., 0-15 km, 25+ km) and distinct trip categories (Escort vs. Normal).
* **Real-World Capacity Guards:** Automatically skips vendors that have 0 active cabs available.
* **Rejection & Cool-off Healing:** If a vendor rejects an assigned trip, the system immediately unassigns it, penalizes the vendor with a 15-minute cool-off period, and automatically reallocates the trip to the next most-owed vendor.
* **Zero-Trust Security:** API endpoints are secured via Spring Security using HTTP Basic Authentication.
* **Active System Monitoring:** Real-time health dashboards via Spring Boot Actuator to monitor DB status and disk space.

## 4. 🏗️ Architecture & System Design

* **Language & Framework:** Java 17/21, Spring Boot 3
* **Database & ORM:** PostgreSQL, Spring Data JPA, Hibernate
* **Security & Observability:** Spring Security, Spring Boot Actuator
* **Testing:** JUnit 5, Mockito
* **Optimization:** Spring Cache (`@Cacheable`, `@CacheEvict`)

**Architecture Flow:**
```text
Client Request 
  → Security Filter Chain (Auth) 
  → Global @ControllerAdvice (Error Handling) 
  → REST Controller 
  → Service Layer (Fairness Math) 
  → Pessimistic Locking & Caching 
  → PostgreSQL DB
```

## 5. 🗄️ Database Schema & Data Modeling

We chose PostgreSQL primarily for its robust ACID compliance and row-level locking capabilities, which are mandatory for handling high-concurrency race conditions.

* **Vendor:** Stores vendor credentials, total capacity, current active cabs, and a `coolOffUntil` timestamp for penalties.
* **VendorZoneConfig:** Stores the contractual target percentages (e.g., 50%) mapped by zone (0-15km) and category (Escort).
* **Trip:** Represents a physical trip, tracking its status (`PENDING`, `ALLOCATED`), zone, category, and `assignedVendor`.
* **AllocationLedger:** The financial core. Tracks promised percentages vs. actually allocated trips to compute the exact shortfall.

## 6. ⚖️ Technical Challenges, Assumptions & Trade-offs

### Core Assumptions
* Vendors begin Day 1 with a shortfall of zero.
* Escort trips (requiring a guard/marshal) and normal trips operate as two completely separate mathematical streams.

### Trade-off 1: Database Locking vs. Performance
* **The Problem:** Two threads/people allocating at the exact same millisecond must not grab the same "last" cab slot.
* **The Trade-off:** We chose Pessimistic Locking (`PESSIMISTIC_WRITE`) over Optimistic Locking. While optimistic locking generally offers faster read performance by avoiding database locks, cab allocation is a highly contentious inventory problem. Under heavy load, optimistic locking would result in massive transaction failures and infinite retry loops. Pessimistic locking forces sequential queueing at the database level, trading a slight latency increase for absolute race-condition safety.

### Trade-off 2: RAM Caching vs. Redis
* **The Problem:** Querying static vendor contracts (percentages) for every single trip causes massive, redundant disk I/O.
* **The Trade-off:** We implemented Spring's `@Cacheable` using an In-Memory RAM Cache (`ConcurrentHashMap`) rather than an external Redis cluster. This reduces database load significantly and keeps the system architecture simple and cheap. The trade-off is that if the app is scaled horizontally across multiple servers, cache invalidation (`@CacheEvict`) won't sync across instances.

### Technical Challenge: Preventing Floating-Point Drift
* **The Bug:** Standard Java `double` and `float` data types lose precision over thousands of iterative math operations, causing the system to slowly lose or gain trips.
* **The Solution:** We strictly utilized Java's `BigDecimal` class for all percentage and shortfall calculations. It requires more memory space and CPU cycles than primitive types, but it guarantees zero-drift exactness for the carry-forward fractions. Time complexity remains $\mathcal{O}(N)$ (where $N$ is the number of active vendors), and space complexity is $\mathcal{O}(V + T)$ (Vendors + Trips).

## 7. 🚀 Getting Started / Local Installation

### Prerequisites
* Java 17 or higher
* Maven 3.8+
* PostgreSQL 14+

### Installation Steps

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/FairCab-Allocation.git
   cd FairCab-Allocation
   ```

2. **Configure the Database (Environment Variables):**
   Open `src/main/resources/application.properties` (or create a `.env` file) and update your PostgreSQL credentials:
   ```properties
   # application.properties example
   spring.datasource.url=jdbc:postgresql://localhost:5432/cab_allocation
   spring.datasource.username=postgres
   spring.datasource.password=your_secure_password
   ```

3. **Run the Application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Test the API (Auth Required):**
   * The application will start on `http://localhost:8080`.
   * **Username:** `admin`
   * **Password:** `admin123`
   *(Test the monitoring dashboard at `/actuator/health`)*

## 8. 🗺️ Future Improvements / Roadmap

* **Migration to JWT OAuth2:** Replace Basic Auth with stateless JSON Web Tokens for secure mobile/client integrations.
* **Redis Integration:** Swap the in-memory cache for Redis to allow horizontal scaling (multiple instances of the application running simultaneously behind a load balancer).
* **WebSocket Real-Time Dashboard:** Push live allocation updates and capacity warnings to a React frontend without requiring the client to poll the API.
