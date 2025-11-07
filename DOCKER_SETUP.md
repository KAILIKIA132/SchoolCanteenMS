# Docker Setup Guide - Push Demo Project

This guide will help you set up the entire Push Demo project using Docker.

## Prerequisites

- Docker Desktop installed and running
- Docker Compose (usually included with Docker Desktop)

## Quick Start

### Option 1: Automated Setup (Recommended)

Run the automated setup script:

```bash
./setup-docker.sh
```

This script will:
1. Update all config files for Docker
2. Start MySQL container
3. Import the database automatically
4. Start Tomcat container
5. Show you the status

### Option 2: Manual Setup

#### Step 1: Update Configuration Files

Update config.xml to use Docker MySQL hostname:

```bash
cp docker/config.xml src/config.xml
cp docker/config.xml WebContent/WEB-INF/classes/config.xml
cp docker/config.xml build/classes/config.xml
```

#### Step 2: Start MySQL Container

```bash
docker-compose up -d mysql
```

Wait for MySQL to be ready (about 10-15 seconds).

#### Step 3: Import Database

The database will be automatically imported from `docker/mysql/init/02-pushdemo-schema.sql` when MySQL starts for the first time.

If you need to re-import:

```bash
./import-database.sh
```

Or manually:

```bash
docker exec -i pushdemo-mysql mysql -uroot -proot pushdemo < doc/pushdemo.sql
```

#### Step 4: Start Tomcat Container

```bash
docker-compose up -d tomcat
```

#### Step 5: Verify Everything is Running

```bash
docker-compose ps
```

## Accessing the Application

Once everything is running:

- **Web Application**: http://localhost:8080/pushdemo
- **MySQL**: localhost:3306
  - Database: `pushdemo`
  - User: `root`
  - Password: `root`

## Database Connection Details

From within Docker containers:
- Host: `mysql` (Docker service name)
- Port: `3306`
- Database: `pushdemo`
- User: `root`
- Password: `root`

From your host machine:
- Host: `localhost`
- Port: `3306`
- Database: `pushdemo`
- User: `root`
- Password: `root`

## Important Notes

### MySQL Connector JAR

The project requires MySQL Connector/J 8.0.33 or newer. 

**If you haven't added it yet:**

1. Download MySQL Connector/J 8.0.33 from:
   https://dev.mysql.com/downloads/connector/j/

2. Place it in: `WebContent/WEB-INF/lib/`

3. Remove the old connector (if present):
   ```bash
   rm WebContent/WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar
   ```

### First-Time Database Setup

When MySQL container starts for the first time, it will automatically:
- Create the `pushdemo` database
- Import all tables and stored procedures from `docker/mysql/init/02-pushdemo-schema.sql`

If you need to reset the database:

```bash
docker-compose down -v  # Removes containers and volumes
docker-compose up -d    # Starts fresh
```

## Useful Commands

### View Logs

```bash
# All services
docker-compose logs -f

# MySQL only
docker-compose logs -f mysql

# Tomcat only
docker-compose logs -f tomcat
```

### Stop Containers

```bash
docker-compose down
```

### Stop and Remove Volumes (Resets Database)

```bash
docker-compose down -v
```

### Restart Services

```bash
docker-compose restart
```

### Rebuild Tomcat Image

```bash
docker-compose build tomcat
docker-compose up -d tomcat
```

### Access MySQL Command Line

```bash
docker exec -it pushdemo-mysql mysql -uroot -proot pushdemo
```

### Check Database Tables

```bash
docker exec -it pushdemo-mysql mysql -uroot -proot pushdemo -e "SHOW TABLES;"
```

## Troubleshooting

### Port Already in Use

If port 3306 or 8080 is already in use:

1. Edit `docker-compose.yml`
2. Change the port mappings:
   ```yaml
   ports:
     - "3307:3306"  # MySQL (change 3306 to 3307)
     - "8081:8080"  # Tomcat (change 8080 to 8081)
   ```
3. Update `docker/config.xml` with the new MySQL port
4. Restart: `docker-compose down && docker-compose up -d`

### Database Connection Issues

1. Check MySQL is running:
   ```bash
   docker-compose ps
   ```

2. Check MySQL logs:
   ```bash
   docker-compose logs mysql
   ```

3. Test connection from host:
   ```bash
   docker exec -it pushdemo-mysql mysql -uroot -proot -e "SELECT 1;"
   ```

### Tomcat Won't Start

1. Check Tomcat logs:
   ```bash
   docker-compose logs tomcat
   ```

2. Verify MySQL connector JAR exists:
   ```bash
   ls -la WebContent/WEB-INF/lib/mysql-connector*.jar
   ```

3. Rebuild Tomcat:
   ```bash
   docker-compose build tomcat
   docker-compose up -d tomcat
   ```

### Application Not Loading

1. Check if application is deployed:
   ```bash
   docker exec -it pushdemo-tomcat ls -la /usr/local/tomcat/webapps/pushdemo
   ```

2. Check Tomcat logs for errors:
   ```bash
   docker-compose logs tomcat | tail -50
   ```

3. Verify config.xml is correct:
   ```bash
   cat WebContent/WEB-INF/classes/config.xml
   ```

## Project Structure

```
pushdemoNew/
├── docker/
│   ├── config.xml              # Docker-specific config (uses 'mysql' hostname)
│   └── mysql/
│       └── init/
│           ├── 01-init.sql      # Database initialization
│           └── 02-pushdemo-schema.sql  # Full database schema (auto-imported)
├── docker-compose.yml          # Docker Compose configuration
├── Dockerfile                  # Tomcat container definition
├── setup-docker.sh            # Automated setup script
├── import-database.sh         # Database import script
└── doc/
    └── pushdemo.sql           # Original database schema
```

## Next Steps

After setup is complete:

1. Access the application at http://localhost:8080/pushdemo
2. Verify database connection in application logs
3. Test creating a device/user to verify database operations
4. Check application functionality

## Support

For issues or questions:
- Check application logs: `docker-compose logs -f`
- Check MySQL logs: `docker-compose logs mysql`
- Verify database: `docker exec -it pushdemo-mysql mysql -uroot -proot pushdemo`





