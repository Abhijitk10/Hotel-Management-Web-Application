🏨 Hotel Booking ApplicationA full-stack hotel management system built with Spring Boot, React, and MySQL. This application allows users to browse rooms, check availability, and book stays, while providing an administrative interface for managing room inventory and viewing all bookings.🚀 FeaturesFor GuestsRoom Browsing: View available rooms with high-quality photos, types, and prices.Booking System: Book rooms by selecting dates and providing guest details.Instant Confirmation: Receive a unique 8-character confirmation code for every successful booking.Booking History: View existing bookings using a guest email address.For AdministratorsRoom Management: Add, edit, and delete rooms.Booking Oversight: View a comprehensive list of all reservations made in the system.Photo Management: Upload and update room images directly from the dashboard.🛠️ Tech StackBackendJava 17+Spring Boot 3.xSpring Data JPA (Hibernate)Spring Security (Role-based access control)Lombok (Boilerplate reduction)MySQL (Relational Database)FrontendReact.jsAxios (API communication)Bootstrap (Styling)⚙️ Setup & Installation1. PrerequisitesInstall JDK 17 or higher.Install Node.js and npm.Install MySQL Server.2. Database ConfigurationCreate a database named hotel_db and run the following configuration to handle the booking logic correctly:SQLCREATE DATABASE hotel_db;

-- Use this to ensure compatibility with the application logic
ALTER TABLE room MODIFY COLUMN is_booked TINYINT(1) DEFAULT 0;
ALTER TABLE booked_room MODIFY COLUMN confirmation_code VARCHAR(255);
3. Backend SetupNavigate to the backend folder.Open src/main/resources/application.properties.Update your MySQL credentials:Propertiesspring.datasource.url=jdbc:mysql://localhost:3306/hotel_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
Run the application using your IDE or via command line:Bash./mvnw spring-boot:run
4. Frontend SetupNavigate to the frontend folder.Install dependencies:Bashnpm install
Start the development server:Bashnpm start
📂 Project StructurePlaintextHotelBookingApplication/
├── src/main/java/com/avin/HotelBookingApplication/
│   ├── controller/      # REST Endpoints
│   ├── model/           # JPA Entities (Room, BookedRoom)
│   ├── repository/      # Data Access Layer
│   ├── response/        # DTOs for clean API responses
│   └── service/         # Business Logic
└── src/main/resources/
    └── application.properties
🛡️ API EndpointsMethodEndpointDescriptionAccessPOST/rooms/add/new-roomAdd a new room with photoAdminGET/rooms/all-roomsFetch all roomsPublicPOST/bookings/room/{id}/bookingCreate a new reservationPublicGET/bookings/all-bookingsView all hotel bookingsAdmin
