#!/bin/bash
cd /Users/aaron/pushdemoNew

echo "═══════════════════════════════════════════════════════════"
echo "🚀 QUICK FIX - Push Demo Project"
echo "═══════════════════════════════════════════════════════════"
echo ""

# Check if MySQL connector exists and is valid
if [ -f "WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar" ]; then
    SIZE=$(stat -f%z WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || stat -c%s WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || echo 0)
    if [ "$SIZE" -gt 1000000 ]; then
        echo "✅ MySQL connector found ($SIZE bytes)"
        echo ""
        echo "Rebuilding and starting..."
        rm -f WebContent/WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar
        docker-compose build tomcat
        docker-compose up -d
        sleep 30
        docker-compose ps
        echo ""
        echo "🌐 Application: http://localhost:8080/pushdemo"
        exit 0
    fi
fi

echo "⚠️  MySQL connector JAR not found or invalid"
echo ""
echo "Please download manually:"
echo "1. Go to: https://dev.mysql.com/downloads/connector/j/"
echo "2. Download: mysql-connector-java-8.0.33.jar"
echo "3. Save to: WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar"
echo ""
echo "OR use this command:"
echo ""
echo "wget -O WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \\"
echo "  https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-8.0.33.jar"
echo ""
echo "Then run this script again."


