#!/bin/bash

# Complete Docker Setup Script
# This script sets up everything from scratch

echo "=========================================="
echo "Push Demo - Complete Docker Setup"
echo "=========================================="

# Step 1: Update config.xml for Docker
echo ""
echo "Step 1: Updating config.xml for Docker..."
cp docker/config.xml src/config.xml
cp docker/config.xml WebContent/WEB-INF/classes/config.xml
cp docker/config.xml build/classes/config.xml
echo "✓ Config files updated"

# Step 2: Check if MySQL connector exists
echo ""
echo "Step 2: Checking MySQL connector..."
if [ ! -f "WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar" ] && [ ! -f "WebContent/WEB-INF/lib/mysql-connector-j-8.1.0.jar" ]; then
    echo "⚠ Warning: MySQL connector JAR not found!"
    echo "Please download MySQL Connector/J 8.0.33 or newer from:"
    echo "https://dev.mysql.com/downloads/connector/j/"
    echo "And place it in: WebContent/WEB-INF/lib/"
    echo ""
    read -p "Press Enter to continue anyway..."
else
    echo "✓ MySQL connector found"
fi

# Step 3: Stop existing containers
echo ""
echo "Step 3: Stopping existing containers..."
docker-compose down
echo "✓ Containers stopped"

# Step 4: Remove old volumes (optional - uncomment to reset database)
# echo ""
# echo "Step 4: Removing old volumes..."
# docker volume rm pushdemonew_mysql_data 2>/dev/null || true
# echo "✓ Volumes removed"

# Step 5: Start MySQL first
echo ""
echo "Step 5: Starting MySQL container..."
docker-compose up -d mysql
echo "✓ MySQL container started"

# Step 6: Wait for MySQL to be ready
echo ""
echo "Step 6: Waiting for MySQL to be ready..."
sleep 10
echo "✓ MySQL is ready"

# Step 7: Import database
echo ""
echo "Step 7: Importing database..."
docker exec -i pushdemo-mysql mysql -uroot -proot pushdemo < doc/pushdemo.sql
if [ $? -eq 0 ]; then
    echo "✓ Database imported successfully"
else
    echo "⚠ Database import had issues, but continuing..."
fi

# Step 8: Start Tomcat
echo ""
echo "Step 8: Starting Tomcat container..."
docker-compose up -d tomcat
echo "✓ Tomcat container started"

# Step 9: Show status
echo ""
echo "=========================================="
echo "Setup Complete!"
echo "=========================================="
echo ""
echo "Containers status:"
docker-compose ps
echo ""
echo "Application will be available at:"
echo "  http://localhost:8080/pushdemo"
echo ""
echo "MySQL connection:"
echo "  Host: localhost (from host) or mysql (from Docker containers)"
echo "  Port: 3307 (from host) or 3306 (from Docker containers)"
echo "  Database: pushdemo"
echo "  User: root"
echo "  Password: root"
echo ""
echo "To view logs:"
echo "  docker-compose logs -f"
echo ""
echo "To stop containers:"
echo "  docker-compose down"
echo ""

