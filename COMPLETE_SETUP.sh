#!/bin/bash
set -e

echo "═══════════════════════════════════════════════════════════"
echo "🚀 COMPLETE SETUP - Push Demo Project"
echo "═══════════════════════════════════════════════════════════"
echo ""

cd /Users/aaron/pushdemoNew

# Step 1: Remove corrupted MySQL connector
echo "Step 1: Removing corrupted MySQL connector..."
rm -f WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar
rm -f WebContent/WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar

# Step 2: Rebuild Tomcat with MySQL connector (Dockerfile downloads it)
echo ""
echo "Step 2: Rebuilding Tomcat container with MySQL connector..."
docker-compose build tomcat

# Step 3: Start all containers
echo ""
echo "Step 3: Starting all containers..."
docker-compose up -d

# Step 4: Wait for services to be ready
echo ""
echo "Step 4: Waiting for services to be ready..."
sleep 30

# Step 5: Verify MySQL is healthy
echo ""
echo "Step 5: Verifying MySQL..."
docker-compose ps mysql

# Step 6: Check application status
echo ""
echo "Step 6: Checking application status..."
sleep 10
docker-compose ps

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "✅ SETUP COMPLETE!"
echo "═══════════════════════════════════════════════════════════"
echo ""
echo "🌐 APPLICATION URL:"
echo "   👉 http://localhost:8080/pushdemo"
echo ""
echo "📊 DATABASE:"
echo "   Host: localhost"
echo "   Port: 3306"
echo "   Database: pushdemo"
echo "   User: root"
echo "   Password: root"
echo ""
echo "═══════════════════════════════════════════════════════════"


