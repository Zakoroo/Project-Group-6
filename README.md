# Project-Group-6
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
This server-side application is designed to handle backend operations efficiently using connection pooling with HikariCP.

## What's New
- Previously, all users shared a single database connection, causing concurrency issues.
- Now, [HikariCP](https://github.com/brettwooldridge/HikariCP) provides efficient connection pooling.

## Considerations
> [!IMPORTANT]
> Create a `hikari.properties` file with your database credentials:

```shell
dataSourceClassName=org.postgresql.ds.PGSimpleDataSource

# Database credentials
dataSource.user=postgres
dataSource.password=password

# Host information
dataSource.databaseName=postgres
dataSource.portNumber=5432
dataSource.serverName=localhost

# Performance configurations
maximumPoolSize=10
minimumIdle=5
connectionTimeout=3000
idleTimeout=600000
```
## Prerequisites
* Java 11 or higher
* PostgreSQL

## Installation 
1. You can simply install the project by using the command:
```shell
git clone https://github.com/Zakoroo/Project-Group-6.git
```
2. Navigate to `/server` directory:
```shell
cd Project-Group-6/server
```
## Usage 
1. Compile the program:
```shell
javac ServerDriver.java
```

2. Run the `ServerDriver` class:
```shell
java SererDriver
```

## Contributing
Contributions are welcome! Please read the contributing 
[guidelines](../CONTRIBUTING.md) to get started.

## License
This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## Contact
`Place holder`
