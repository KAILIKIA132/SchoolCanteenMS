# Push Demo Project - Setup Guide

This project has been updated to be compatible with modern MySQL versions (MySQL 8.0+) and current Java standards.

## Changes Made

### 1. MySQL Driver Updates
- **Updated driver class**: Changed from `com.mysql.jdbc.Driver` to `com.mysql.cj.jdbc.Driver` (required for MySQL 8+)
- **Updated JDBC URL**: Added timezone and SSL parameters for MySQL 8+ compatibility
- **Fixed PreparedStatement imports**: Replaced deprecated `com.mysql.jdbc.PreparedStatement` with standard `java.sql.PreparedStatement`
- **Fixed getIDENTITY method**: Changed from SQL Server syntax (`@@IDENTITY`) to MySQL syntax (`LAST_INSERT_ID()`)

### 2. Code Compatibility Fixes
- All DAO classes now use standard `java.sql.PreparedStatement` instead of MySQL-specific classes
- Removed unnecessary type casts from PreparedStatement creation
- Updated `BaseDao.getIDENTITY()` to use MySQL-compatible `LAST_INSERT_ID()`

## Required Setup Steps

### 1. Update MySQL Connector JAR

The current MySQL connector (`mysql-connector-java-5.0.8-bin.jar`) is outdated and incompatible with MySQL 8+. 

**Download and replace the MySQL connector:**

1. Download MySQL Connector/J 8.0.33 (or newer) from:
   https://dev.mysql.com/downloads/connector/j/

2. Replace the old JAR file:
   - Delete: `WebContent/WEB-INF/lib/mysql-connector-java-5.0.8-bin.jar`
   - Copy new JAR to: `WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar` (or newer version)

   **Alternative**: For MySQL 8.1+, you can use the newer connector:
   - Download: `mysql-connector-j-8.1.0.jar` (or newer)

### 2. Database Configuration

Update the database connection settings in `src/config.xml` (or copy to `WebContent/WEB-INF/classes/config.xml`):

```xml
<databaseconnect>
    <driverclass>com.mysql.cj.jdbc.Driver</driverclass>
    <url>jdbc:mysql://localhost:3306/pushdemo?useSSL=false&amp;serverTimezone=UTC&amp;allowPublicKeyRetrieval=true</url>
    <user>root</user>
    <password>root</password>
</databaseconnect>
```

**Note**: Update the `user`, `password`, and database URL (`localhost:3306/pushdemo`) according to your MySQL setup.

### 3. Database Setup

1. Create the database:
   ```sql
   CREATE DATABASE pushdemo DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
   ```

2. Run the SQL script to create tables:
   - Execute `doc/pushdemo.sql` in your MySQL database

### 4. Build and Deploy

1. Clean and rebuild the project to ensure all updated Java files are compiled
2. Make sure `config.xml` is copied to the `WEB-INF/classes/` directory during build
3. Deploy the application to your servlet container (Tomcat, etc.)

## Configuration Notes

### MySQL 8+ Compatibility
- **Timezone**: The JDBC URL includes `serverTimezone=UTC` to avoid timezone issues
- **SSL**: Set to `false` by default (set to `true` if your MySQL server requires SSL)
- **Public Key Retrieval**: `allowPublicKeyRetrieval=true` may be needed for some MySQL 8+ authentication methods

### Struts 2 Configuration
The project uses Struts 2.1.8.1. The filter class `org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter` is correct for this version.

## Troubleshooting

### Connection Issues
- Ensure MySQL server is running
- Verify database credentials in `config.xml`
- Check that the MySQL connector JAR is in the classpath
- For MySQL 8+, ensure `serverTimezone` is set in the JDBC URL

### ClassNotFoundException
- Ensure the new MySQL connector JAR is in `WebContent/WEB-INF/lib/`
- Clean and rebuild the project
- Restart your application server

### Timezone Warnings
If you see timezone warnings, ensure the JDBC URL includes `serverTimezone=UTC` (or your appropriate timezone).

## Testing

After setup, verify:
1. Application starts without errors
2. Database connection is successful
3. All DAO operations work correctly
4. No `ClassNotFoundException` for MySQL driver





