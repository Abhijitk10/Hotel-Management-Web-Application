# 🏨 Hotel Booking Application

A full-stack **Hotel Management & Booking System** built using **Spring Boot**, **React**, and **MySQL**. This application enables users to browse hotel rooms, check availability, and book stays, while providing administrators with powerful tools to manage rooms and monitor reservations.

---

## 🚀 Features

### 👤 For Guests

* **Room Browsing** – View available rooms with high-quality photos, room types, and pricing details.
* **Booking System** – Book rooms by selecting check-in/check-out dates and entering guest information.
* **Instant Confirmation** – Receive a unique **8-character confirmation code** upon successful booking.
* **Booking History** – Retrieve existing bookings using the guest’s email address.

### 🛠️ For Administrators

* **Room Management** – Add, edit, and delete room details.
* **Booking Oversight** – View all reservations made in the system.
* **Photo Management** – Upload and update room images directly from the admin dashboard.

---

## 🧰 Tech Stack

### 🔙 Backend

* Java 17+
* Spring Boot 3.x
* Spring Data JPA (Hibernate)
* Spring Security (Role-Based Access Control)
* Lombok (Boilerplate Code Reduction)
* MySQL (Relational Database)

### 🔜 Frontend

* React.js
* Axios (API Communication)
* Bootstrap (UI Styling)

---

## ⚙️ Setup & Installation

### 1️⃣ Prerequisites

Ensure the following are installed on your system:

* JDK 17 or higher
* Node.js and npm
* MySQL Server

---

### 2️⃣ Database Configuration

Create a database named `hotel_db` and execute the following SQL commands:

```sql
CREATE DATABASE hotel_db;

-- Ensure compatibility with booking logic
ALTER TABLE room MODIFY COLUMN is_booked TINYINT(1) DEFAULT 0;
ALTER TABLE booked_room MODIFY COLUMN confirmation_code VARCHAR(255);
```

---

### 3️⃣ Backend Setup

1. Navigate to the **backend** directory.
2. Open the file:

   ```
   src/main/resources/application.properties
   ```
3. Update your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

4. Run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

---

### 4️⃣ Frontend Setup

1. Navigate to the **frontend** directory.
2. Install required dependencies:

```bash
npm install
```

3. Start the React development server:

```bash
npm start
```

---

## 📂 Project Structure

```
HotelBookingApplication/
├── src/main/java/com/avin/HotelBookingApplication/
│   ├── controller/      # REST Controllers
│   ├── model/           # JPA Entities (Room, BookedRoom)
│   ├── repository/      # Data Access Layer
│   ├── response/        # DTOs for API Responses
│   └── service/         # Business Logic
└── src/main/resources/
    └── application.properties
```

---

## 🔐 API Endpoints

| Method | Endpoint                      | Description               | Access |
| ------ | ----------------------------- | ------------------------- | ------ |
| POST   | `/rooms/add/new-room`         | Add a new room with photo | Admin  |
| GET    | `/rooms/all-rooms`            | Fetch all rooms           | Public |
| POST   | `/bookings/room/{id}/booking` | Create a new reservation  | Public |
| GET    | `/bookings/all-bookings`      | View all hotel bookings   | Admin  |

---


## 👨‍💻 Author

**Abhijit Khemkar**
**Isha Koralli**


---

⭐ If you find this project helpful, feel free to star the repository!
