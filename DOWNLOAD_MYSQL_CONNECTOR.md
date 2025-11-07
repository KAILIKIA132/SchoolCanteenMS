# MySQL Connector Download Instructions

## Issue
The Maven repository URL is returning an HTML error page instead of the JAR file.

## Solution: Download Manually

### Option 1: Direct Download (Recommended)

1. **Download from MySQL Official Site:**
   - Go to: https://dev.mysql.com/downloads/connector/j/
   - Select "Platform Independent"
   - Download: `mysql-connector-java-8.0.33.zip` or `.tar.gz`
   - Extract the archive
   - Copy `mysql-connector-java-8.0.33.jar` to:
     ```
     WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar
     ```

### Option 2: Using Maven (if you have Maven installed)

```bash
cd /Users/aaron/pushdemoNew
mvn dependency:get -Dartifact=mysql:mysql-connector-java:8.0.33 -Ddest=WebContent/WEB-INF/lib/
```

### Option 3: Direct Download Command

Run this in terminal:

```bash
cd /Users/aaron/pushdemoNew
curl -L -o WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
  "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar" \
  -H "Accept: application/java-archive"

# Verify file size (should be ~2.5MB, not 554 bytes)
ls -lh WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar
```

### After Download

Once the JAR is in place (should be ~2.5MB):

```bash
cd /Users/aaron/pushdemoNew
docker-compose build tomcat
docker-compose up -d
```

Then access: **http://localhost:8080/pushdemo**


