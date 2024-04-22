# Book Library System

This project is a Spring Boot application designed to manage a library of books. 
It provides RESTful API endpoints for listing, retrieving, adding, updating, and deleting book records.

## Features

- **List Books**: Retrieve a list of all books or search books by title or author.
- **Get Book**: Retrieve detailed information on a specific book by ID.
- **Add Book**: Create new book records.
- **Update Book**: Update existing book records.
- **Delete Book**: Remove book records from the library.

## Prerequisites

To run this application, you'll need:
- Java JDK 11 or newer.
- Maven 3.6+ (if building from source).
- An IDE like IntelliJ IDEA, Eclipse, or Spring Tool Suite (STS) for development.

## Setup and Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/hpursan/digicert_assessment
   cd digicert_assessment
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
4. **Additional notes**:

- The application runs on port 8080 by default and makes use of an in-memory H2 database.
- These properties can be modified in the `application.properties` file if required.

## API Endpoints

The following endpoints are available:

- `GET /api/books`: List all books or search by title/author.
- `GET /api/books/{id}`: Retrieve a book by its ID.
- `POST /api/books`: Add a new book.
- `PUT /api/books/{id}`: Update an existing book.
- `DELETE /api/books/{id}`: Delete a book.

## Swagger

http://localhost:8080/swagger-ui/index.html

## Usage

Here's how you can interact with the API using `curl`:

- **List Books**:
  ```bash
  curl -X GET "http://localhost:8080/api/books" -H "accept: application/json"
  ```

- **Get Book by ID**:
  ```bash
  curl -X GET "http://localhost:8080/api/books/1" -H "accept: application/json"
  ```

- **Add Book**:
  ```bash
  curl -X POST "http://localhost:8080/api/books" -H "Content-Type: application/json" -d '{"title":"New Book", "author":"Author Name", "isbn":"1234567890123"}'
  ```

- **Update Book**:
  ```bash
  curl -X PUT "http://localhost:8080/api/books/1" -H "Content-Type: application/json" -d '{"title":"Updated Book", "author":"Updated Author", "isbn":"1234567890123"}'
  ```

- **Delete Book**:
  ```bash
  curl -X DELETE "http://localhost:8080/api/books/1"
  ```

## Testing

To run the unit tests and integration tests included in the project:
```bash
mvn test
```

## Contributing

Contributions are welcome. Please fork the repository and submit pull requests to the main branch.

## License

This project is licensed under the [MIT License](LICENSE).
