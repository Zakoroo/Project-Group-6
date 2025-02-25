# Project-Group-6 Chat Application

## Table of Contents
- [Description](#description)
- [What's New](#whats-new)
- [Considerations](#considerations)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Description
The Chat Application is a fully featured, real-time communication system designed to enable seamless chat functionality between users. This repository includes the source code for both the server and client components, built in Java and managed with Maven. In addition, configuration automation is provided through a Makefile that generates necessary configuration files and sets up the database.

## What's New
- **Efficient Database Connection Pooling:** Utilizes [HikariCP](https://github.com/brettwooldridge/HikariCP) for fast and efficient connection pooling.
- **Automated Configuration:** A Makefile in the project root automatically generates configuration files (`config.properties` and `hikari.properties`), reducing setup complexity.
- **Maven Integration:** The project now leverages Maven for dependency management, building, and testing, providing a standardized project structure.

## Considerations
> [!IMPORTANT] 
> Before running the application, please update the placeholder parameters in the Makefile to match your local database configuration:
>
> ```shell
> DB_HOST ?= localhost
> DB_NAME ?= postgres
> DB_USER ?= postgres
> DB_PASSWORD ?= localhost123456
> DB_PORT ?= 5432
> ```

## Prerequisites
- **Java 17 or higher** – Ensure you have Java 17 installed.
- **PostgreSQL** – Required for the server database.
- **make** – Used for automated configuration file generation.
- **Maven** – For building and managing project dependencies.

## Installation
1. **Clone the Repository:**
    ```bash
    git clone https://github.com/Zakoroo/Project-Group-6.git
    cd Project-Group-6
    ```
2. **Generate Configuration Files:**
    ```bash
    make config
    ```
    This command creates `config.properties` and `hikari.properties` with your environment-specific settings.
3. **Set Up the Database:**
    ```bash
    make db-setup
    ```
    This command uses psql to run the SQL setup script (e.g., creating tables and triggers).
4. **Build the Project:**
    ```bash
    mvn clean package
    ```

## Usage
- **Run the Server Application:**
    ```bash
    make server
    ```
- **Run the Client Application:**
    ```bash
    make client
    ```

For further usage details, please refer to our [documentation](https://docs.google.com/document/d/1dgtpqeWMZV0WBx9NvzfhCxS4wcOeWNz22uFpQ_BT8z8/edit?usp=sharing).

## Contributing
Contributions are welcome! Please review the [Contributing Guidelines](CONTRIBUTING.md) for instructions on how to contribute to this project.

## License
This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.

## Contact
For questions, issues, or suggestions, please contact:
- **Name:** Hussein Hafid
- **Email:** husseinh@chalmers.se
- **GitHub:** [Hussein Hafid (aka. Zakoroo)](https://github.com/Zakoroo)
