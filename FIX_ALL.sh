#!/bin/bash
cd /Users/aaron/pushdemoNew

echo "=== Step 1: Download MySQL Connector ==="
rm -f WebContent/WEB-INF/lib/mysql-connector-java*.jar

# Try multiple download methods
echo "Attempting download..."
if command -v wget &> /dev/null; then
    wget --no-check-certificate --header="Accept: application/java-archive" \
        -O WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
        "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar" 2>&1
elif command -v curl &> /dev/null; then
    curl -L --header "Accept: application/java-archive" \
        -o WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
        "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar" 2>&1
fi

# Check file size
SIZE=$(stat -f%z WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || stat -c%s WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || echo "0")

if [ "$SIZE" -lt 1000000 ]; then
    echo ""
    echo "⚠️  WARNING: Download failed or file is too small ($SIZE bytes)"
    echo ""
    echo "Please download manually:"
    echo "1. Go to: https://dev.mysql.com/downloads/connector/j/"
    echo "2. Download mysql-connector-java-8.0.33.jar"
    echo "3. Save to: WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar"
    echo ""
    echo "Then run: docker-compose build tomcat && docker-compose up -d"
    exit 1
fi

echo "✅ MySQL connector downloaded successfully ($SIZE bytes)"

echo ""
echo "=== Step 2: Rebuilding Tomcat ==="
docker-compose build tomcat

echo ""
echo "=== Step 3: Starting containers ==="
docker-compose up -d

echo ""
echo "=== Step 4: Waiting for services ==="
sleep 30

echo ""
echo "=== Step 5: Verifying status ==="
docker-compose ps

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "✅ SETUP COMPLETE!"
echo "═══════════════════════════════════════════════════════════"
echo ""
echo "🌐 APPLICATION:"
echo "   👉 http://localhost:8080/pushdemo"
echo ""
echo "📊 DATABASE:"
echo "   localhost:3306 / pushdemo / root / root"
echo ""
echo "═══════════════════════════════════════════════════════════"


