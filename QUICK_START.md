# Quick Start Guide - Push Demo Project

## Prerequisites

✅ Docker Desktop is running (as you mentioned)

## Setup Steps

### Step 1: Download MySQL Connector (If Needed)

If you don't have the MySQL connector JAR yet:

1. Download MySQL Connector/J 8.0.33 or newer from:
   https://dev.mysql.com/downloads/connector/j/

2. Save it as: `WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar`

3. Remove the old one (if exists):
   ```bash
   rm WebContent/WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar
   ```

### Step 2: Run Setup Script

```bash
./setup-docker.sh
```

This will:
- ✅ Update all config files for Docker
- ✅ Start MySQL container (on port 3307 to avoid conflicts)
- ✅ Automatically import the database
- ✅ Start Tomcat container (on port 8080)

### Step 3: Access the Application

Once setup completes:

- **Application**: http://localhost:8080/pushdemo
- **MySQL** (from host): localhost:3307
- **MySQL** (from containers): mysql:3306

## Database Connection

The database is automatically imported when MySQL starts for the first time.

**Connection Details:**
- Host: `mysql` (from within Docker) or `localhost` (from host)
- Port: `3306` (inside Docker) or `3307` (from host)
- Database: `pushdemo`
- User: `root`
- Password: `root`

## Common Commands

```bash
# View logs
docker-compose logs -f

# Stop everything
docker-compose down

# Restart everything
docker-compose restart

# Check status
docker-compose ps

# Re-import database (if needed)
./import-database.sh
```

## Troubleshooting

### Port 3306 Already in Use

The setup uses port **3307** for MySQL to avoid conflicts with your existing MySQL instance.

### MySQL Container Won't Start

Check if port 3307 is available:
```bash
lsof -i :3307
```

If it's in use, edit `docker-compose.yml` and change:
```yaml
ports:
  - "3308:3306"  # Change 3307 to 3308
```

### Application Not Loading

1. Check Tomcat logs:
   ```bash
   docker-compose logs tomcat
   ```

2. Verify MySQL connector JAR exists:
   ```bash
   ls -la WebContent/WEB-INF/lib/mysql-connector*.jar
   ```

3. Check config.xml:
   ```bash
   cat WebContent/WEB-INF/classes/config.xml
   ```

## Next Steps

After setup:
1. Access http://localhost:8080/pushdemo
2. Verify database connection works
3. Test creating devices/users
4. Check application functionality

For detailed setup instructions, see `DOCKER_SETUP.md`.





