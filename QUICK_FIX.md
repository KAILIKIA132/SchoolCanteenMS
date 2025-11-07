# Quick Fix - Run These Commands

## Issue: MySQL Connector JAR is corrupted and needs to be downloaded

**Run these commands in your terminal:**

```bash
cd /Users/aaron/pushdemoNew

# Remove corrupted JAR
rm -f WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar

# Download correct MySQL connector (choose ONE method):

# Method 1: Using wget
wget -O WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
  https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar

# OR Method 2: Using curl
curl -L -o WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
  https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar

# Remove old connector
rm -f WebContent/WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar

# Verify the JAR is correct (should be ~2.5MB, not 554 bytes)
ls -lh WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar

# Restart Tomcat
docker-compose restart tomcat

# Wait 30 seconds for Tomcat to start
sleep 30

# Check status
docker-compose ps

# Access application
echo "Open: http://localhost:8080/pushdemo"
```

## Alternative: Manual Download

If the download doesn't work:

1. Go to: https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/
2. Download: `mysql-connector-java-8.0.33.jar`
3. Save it to: `WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar`
4. Restart: `docker-compose restart tomcat`

## After Fix

The application should be accessible at:
- **http://localhost:8080/pushdemo**



