# Variables for database connection
DB_NAME := postgres
DB_HOST := localhost
DB_PASS := localhost123456
SQL_FILE := database/runsetup.sql

# Runnable script
run-all:
	clear
	psql -f $(SQL_FILE) postgresql://$(DB_NAME):$(DB_PASS)@127.0.0.1