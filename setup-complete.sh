#!/bin/bash
cd /Users/aaron/pushdemoNew

echo "Fixing MySQL connector and rebuilding..."
rm -f WebContent/WEB-INF/lib/mysql-connector-java*.jar

# Download MySQL connector using wget (more reliable)
if command -v wget &> /dev/null; then
    wget -q -O WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
        https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
else
    curl -L -o WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
        https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
fi

# Verify file size (should be ~2.5MB)
SIZE=$(stat -f%z WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || stat -c%s WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || echo "0")
if [ "$SIZE" -lt 1000000 ]; then
    echo "ERROR: MySQL connector download failed (only $SIZE bytes)"
    echo "Please download manually from: https://dev.mysql.com/downloads/connector/j/"
    exit 1
fi

echo "Rebuilding Tomcat..."
docker-compose build tomcat

echo "Starting containers..."
docker-compose up -d

echo "Waiting for services..."
sleep 30

echo "Status:"
docker-compose ps

echo ""
echo "Application: http://localhost:8080/pushdemo"


