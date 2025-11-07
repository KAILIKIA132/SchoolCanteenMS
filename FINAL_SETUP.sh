#!/bin/bash
cd /Users/aaron/pushdemoNew

echo "═══════════════════════════════════════════════════════════"
echo "🚀 FINAL SETUP - Push Demo Project"
echo "═══════════════════════════════════════════════════════════"
echo ""

# Step 1: Remove old connectors
echo "Step 1: Removing old MySQL connectors..."
rm -f WebContent/WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar
rm -f WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar

# Step 2: Try to download MySQL connector
echo ""
echo "Step 2: Downloading MySQL connector..."
echo "Attempting download from Maven Central..."

# Try multiple download methods
DOWNLOADED=0

# Method 1: Try curl with different URL
if command -v curl &> /dev/null; then
    echo "Trying curl with Maven Central..."
    curl -L --fail --silent --show-error \
        -o WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
        "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar" 2>&1
    
    if [ -f "WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar" ]; then
        SIZE=$(stat -f%z WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || stat -c%s WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || echo 0)
        if [ "$SIZE" -gt 1000000 ]; then
            echo "✅ Download successful ($SIZE bytes)"
            DOWNLOADED=1
        fi
    fi
fi

# Method 2: If curl failed, try alternative URL
if [ "$DOWNLOADED" -eq 0 ] && command -v curl &> /dev/null; then
    echo "Trying alternative download URL..."
    rm -f WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar
    curl -L --fail --silent --show-error \
        -o WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
        "https://search.maven.org/remote_content?g=mysql&a=mysql-connector-java&v=8.0.33" 2>&1
    
    if [ -f "WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar" ]; then
        SIZE=$(stat -f%z WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || stat -c%s WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || echo 0)
        if [ "$SIZE" -gt 1000000 ]; then
            echo "✅ Download successful ($SIZE bytes)"
            DOWNLOADED=1
        fi
    fi
fi

# Step 3: Check if download succeeded
if [ "$DOWNLOADED" -eq 0 ]; then
    echo ""
    echo "⚠️  WARNING: Automatic download failed"
    echo ""
    echo "Please download manually:"
    echo ""
    echo "1. Go to: https://dev.mysql.com/downloads/connector/j/"
    echo "2. Select 'Platform Independent'"
    echo "3. Download: mysql-connector-java-8.0.33.jar"
    echo "4. Save to: WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar"
    echo ""
    echo "Then run: docker-compose build tomcat && docker-compose up -d"
    echo ""
    exit 1
fi

# Step 4: Verify file
echo ""
echo "Step 3: Verifying JAR file..."
SIZE=$(stat -f%z WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || stat -c%s WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar 2>/dev/null || echo 0)
echo "File size: $SIZE bytes"

# Step 5: Rebuild and start
echo ""
echo "Step 4: Rebuilding Tomcat container..."
docker-compose build tomcat

echo ""
echo "Step 5: Starting containers..."
docker-compose up -d

echo ""
echo "Step 6: Waiting for services to be ready..."
sleep 30

echo ""
echo "Step 7: Checking status..."
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
echo "   Host: localhost"
echo "   Port: 3306"
echo "   Database: pushdemo"
echo "   User: root"
echo "   Password: root"
echo ""
echo "═══════════════════════════════════════════════════════════"


