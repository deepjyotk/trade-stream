# Trade Stream

Trade Stream is a gRPC-based client-server trading platform that allows users to purchase and sell stocks in real-time. The platform leverages modern technologies to provide low-latency and efficient transactions, ensuring a seamless trading experience.

## Features

- **gRPC-Based Communication:** Implements gRPC for efficient and scalable client-server interactions.
- **Real-Time Stock Prices:** Integrated client-streaming with a third-party service to fetch real-time stock prices, reducing latency by 40%.
- **Spring Boot Backend:** Powered by Spring Boot for rapid development and robust backend services.
- **In-Memory Database:** Utilizes H2 in-memory database for fast data retrieval and storage during development.
- **Data Persistence:** Leveraged Spring Data JPA for simplified data access and management.

## Technologies Used

- **gRPC:** For low-latency, high-performance communication between client and server.
- **Spring Boot:** To create a robust and scalable backend.
- **H2 Database:** In-memory database for efficient testing and development.
- **Spring Data JPA:** Simplifies database interactions with easy-to-use abstractions.

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/trade-stream.git
   ```
2. **Navigate to the project directory:**
   ```bash
   cd trade-stream
   ```
3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
4. **Test the gRPC service using your preferred gRPC client.**

## Contributing

Contributions are welcome! Please fork this repository, make your changes, and submit a pull request.
