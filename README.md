# CS 6650 Distributed Systems

# Assignment 1 - Ski Resort Load Testing Tool

Server Code: cs6650-lab
Client Code: Assignment_Client_1 and Assignment_Client_2

This Java application is designed to perform load testing on a ski resort's lift ride system. It simulates multiple skiers making requests to record their lift rides over a specified period. The tool measures the system's performance and generates statistics including response times, throughput, and success/failure rates.

## Features

- **Multi-threaded Requests**: Utilizes multiple threads(32 Initial + 75 Subsequent) to simulate concurrent requests from skiers.
- **Performance Metrics**: Calculates various performance metrics including mean response time, median response time, and throughput.
- **Error Handling**: Implements retries for failed requests to improve reliability.
- **CSV Output**: Generates a CSV file containing request details such as start time, latency, and response code.

## Dependencies

- [Swagger Codegen](https://github.com/swagger-api/swagger-codegen): Used for generating client API code from Swagger/OpenAPI definitions.
- [Java Concurrent API](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html): Utilized for managing concurrent tasks and synchronization.
- [Apache HttpClient](https://hc.apache.org/httpcomponents-client-4.5.x/index.html): Provides HTTP client functionality for making API requests.

## Usage

1. Ensure you have Java 17 or 18 installed on your system.
2. Clone the repository to your local machine.
3. Fetch the server code as well as the client code.
4. Run cs6650-lab (SkierServelt.java) using IntelliJ
5. Run Assgn_1 (Main.py) using IntelliJ

## Configuration

- `URL`: Base URL of the ski resort's API.
- `TOTAL_REQUESTS`: Total number of lift ride requests to be generated.
- `MAX_THREADS_INITIAL`: Maximum number of threads for initial requests.
- `MAX_THREADS_SUBSEQUENT`: Maximum number of threads for remaining requests.
- `NUM_OF_REQUEST_PER_THREAD_INITIAL`: Number of lift ride requests to be handled per initial thread.
- `NUM_OF_REQUEST_PER_THREAD_SUBSEQUENT`: Number of lift ride requests to be handled per subsequent thread.

Adjust these parameters in the `Main` class constructor to suit your testing requirements.

## Output

The program generates statistics on standard output, including mean response time, median response time, and throughput. It also creates a CSV file (`request_details.csv`) containing detailed information about each request made during the test.

## Contributors

- Riddhi Gohil(https://github.com/gohilriddhi21)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
