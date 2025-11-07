#!/bin/bash

# Database Import Script
# This script imports the pushdemo.sql file into the MySQL database

echo "=========================================="
echo "Push Demo Database Import Script"
echo "=========================================="

# Check if MySQL container is running
if ! docker ps | grep -q pushdemo-mysql; then
    echo "Error: MySQL container is not running!"
    echo "Please start the containers first: docker-compose up -d"
    exit 1
fi

# Wait for MySQL to be ready
echo "Waiting for MySQL to be ready..."
sleep 5

# Import database
echo "Importing database..."
docker exec -i pushdemo-mysql mysql -uroot -proot pushdemo < doc/pushdemo.sql

if [ $? -eq 0 ]; then
    echo "=========================================="
    echo "Database imported successfully!"
    echo "=========================================="
else
    echo "=========================================="
    echo "Error: Database import failed!"
    echo "=========================================="
    exit 1
fi





