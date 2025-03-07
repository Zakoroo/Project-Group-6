# Paths for the config file (for application settings)
CONFIG_TEMPLATE = src/main/resources/db/config.template
CONFIG_FILE = src/main/resources/db/config.properties

# Paths for the HikariCP configuration file
HIKARI_TEMPLATE = src/main/resources/db/hikari.template
HIKARI_FILE = src/main/resources/db/hikari.properties

# Path for your SQL setup script
SQL_FILE = src/main/resources/db/runsetup.sql

# Default environment variables (overridable)
DB_HOST ?= localhost
DB_NAME ?= postgres
DB_USER ?= postgres
DB_PASSWORD ?= localhost123456
DB_SERVER ?= $(DB_HOST)
DB_PORT ?= 5432

.PHONY: config clean-config db-setup help

# Help target: prints available commands
help:
	@echo "Available commands:"
	@echo "  make help         - Show this help message"
	@echo "  make config       - Generate configuration files (config.properties and hikari.properties)"
	@echo "  make db-setup     - Generate config files and run the database setup (runsetup.sql)"
	@echo "  make server       - Run the server-side application (mvn exec:java -Pserver)"
	@echo "  make client       - Run the client-side application (mvn exec:java -Pclient)"

# The config target first cleans old config files then generates new ones
config: clean-config $(CONFIG_FILE) $(HIKARI_FILE)
	@echo "Both config.properties and hikari.properties have been generated."

# Clean target: remove old config files
clean-config:
	@echo "Removing old configuration files..."
	@test -f $(CONFIG_FILE) && rm -f $(CONFIG_FILE) || true
	@test -f $(HIKARI_FILE) && rm -f $(HIKARI_FILE) || true

# Rule for generating config.properties
$(CONFIG_FILE): $(CONFIG_TEMPLATE)
	@echo "Generating config.properties..."
	@sed \
		-e "s|\$${DB_HOST}|$(DB_HOST)|g" \
		-e "s|\$${DB_NAME}|$(DB_NAME)|g" \
		-e "s|\$${DB_USER}|$(DB_USER)|g" \
		-e "s|\$${DB_PASSWORD}|$(DB_PASSWORD)|g" \
		$(CONFIG_TEMPLATE) > $(CONFIG_FILE)
	@echo "Created $(CONFIG_FILE)"

# Rule for generating hikari.properties
$(HIKARI_FILE): $(HIKARI_TEMPLATE)
	@echo "Generating hikari.properties..."
	@sed \
		-e "s|\$${DB_USER}|$(DB_USER)|g" \
		-e "s|\$${DB_PASSWORD}|$(DB_PASSWORD)|g" \
		-e "s|\$${DB_NAME}|$(DB_NAME)|g" \
		-e "s|\$${DB_SERVER}|$(DB_SERVER)|g" \
		-e "s|\$${DB_PORT}|$(DB_PORT)|g" \
		$(HIKARI_TEMPLATE) > $(HIKARI_FILE)
	@echo "Created $(HIKARI_FILE)"

# Target to run the database setup SQL script using psql
db-setup: config
	@echo "Running setup.sql on database $(DB_NAME) at $(DB_HOST)..."
	@PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -h $(DB_HOST) -p $(DB_PORT) -d $(DB_NAME) -f $(SQL_FILE)

# Target to run the server side application using Maven
server: $(CONFIG_FILE) $(HIKARI_FILE)
	@echo "Running server side application..."
	@mvn clean compile exec:java -Pserver

# Target to run the client side application using Maven
client: $(CONFIG_FILE) $(HIKARI_FILE)
	@echo "Running client side application..."
	@mvn clean compile exec:java -Pclient