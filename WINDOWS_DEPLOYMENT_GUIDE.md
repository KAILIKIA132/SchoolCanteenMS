# Push Demo Project - Windows Server Deployment Guide

## 🎯 Overview

This guide provides detailed instructions for deploying the Push Demo biometric device management system on Windows Server. The system manages ZK biometric terminals, processes attendance logs, and handles real-time device communications.

## 🏗️ Windows Server Requirements

### Supported Versions
- **Windows Server 2019** (Recommended)
- **Windows Server 2022** (Latest)
- **Windows Server 2016** (Minimum supported)

### Hardware Specifications
| Component | Minimum | Recommended |
|-----------|---------|-------------|
| **CPU** | Dual-core 2.0 GHz | Quad-core 2.4 GHz+ |
| **RAM** | 8 GB | 16 GB+ |
| **Storage** | 50 GB Free Space | 100 GB SSD |
| **Network** | 1 Gbps NIC | 10 Gbps NIC |

## 🛠️ Prerequisites Installation

### 1. Install Java 8

1. **Download OpenJDK 8**:
   - Visit: https://adoptium.net/temurin/releases/?version=8
   - Download the Windows x64 MSI installer

2. **Install Java**:
   - Run the installer as Administrator
   - Accept default installation path (typically `C:\Program Files\Eclipse Adoptium\jdk-8.XX.X-hotspot`)

3. **Set Environment Variables**:
   - Open **System Properties** → **Advanced** → **Environment Variables**
   - Add new system variable:
     - **Variable name**: `JAVA_HOME`
     - **Variable value**: Path to Java installation (e.g., `C:\Program Files\Eclipse Adoptium\jdk-8.0.XX.X-hotspot`)
   - Edit the `PATH` system variable and add: `%JAVA_HOME%\bin`

4. **Verify Installation**:
   ```cmd
   java -version
   javac -version
   ```

### 2. Install MySQL 8.0

1. **Download MySQL Community Server**:
   - Visit: https://dev.mysql.com/downloads/mysql/8.0.html
   - Download the Windows (x86, 64-bit) MSI Installer

2. **Install MySQL**:
   - Run the installer as Administrator
   - Choose "Developer Default" or "Server only" configuration
   - Complete the wizard, noting your root password
   - Allow the installer to start the MySQL service

3. **Configure MySQL**:
   - Add MySQL to PATH: `%PROGRAMFILES%\MySQL\MySQL Server 8.0\bin`
   - Verify installation: `mysql -u root -p`

### 3. Install Apache Tomcat 9

1. **Download Tomcat 9**:
   - Visit: https://tomcat.apache.org/download-90.cgi
   - Download "64-bit Windows zip" (e.g., `apache-tomcat-9.0.XX-windows-x64.zip`)

2. **Extract Tomcat**:
   - Extract to `C:\apache-tomcat-9.0.XX`
   - Create system variable:
     - **Variable name**: `CATALINA_HOME`
     - **Variable value**: `C:\apache-tomcat-9.0.XX`

3. **Add Tomcat to PATH**:
   - Edit the `PATH` system variable
   - Add: `%CATALINA_HOME%\bin`

## 📦 Project Deployment Steps

### 1. Prepare Project Files

1. **Copy Project Directory**:
   - Copy entire `pushdemoNew` folder to Windows Server (e.g., `C:\pushdemoNew`)

2. **Verify Required Files**:
   - Confirm `mysql-connector-java-8.0.33.jar` exists in `WebContent\WEB-INF\lib\`
   - Check that `config.xml` is in both `src\` and `WebContent\WEB-INF\classes\`

### 2. Set Up Database

1. **Open MySQL Command Line**:
   - Press `Win + R`, type `cmd`, press Enter
   - Navigate to MySQL bin directory: `cd "%PROGRAMFILES%\MySQL\MySQL Server 8.0\bin"`
   - Connect to MySQL: `mysql -u root -p`

2. **Create Database**:
   ```sql
   CREATE DATABASE pushdemo DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
   ```

3. **Import Schema**:
   ```sql
   USE pushdemo;
   SOURCE C:\\pushdemoNew\\doc\\pushdemo.sql;
   ```

4. **Exit MySQL**:
   ```sql
   EXIT;
   ```

### 3. Configure Database Connection

1. **Navigate to Config File**:
   - Go to `C:\pushdemoNew\WebContent\WEB-INF\classes\`

2. **Edit config.xml**:
   - Open `config.xml` in a text editor (like Notepad++)
   - Update with your MySQL credentials:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <root>
       <databaseconnect>
           <driverclass>com.mysql.cj.jdbc.Driver</driverclass>
           <url>jdbc:mysql://localhost:3306/pushdemo?useSSL=false&amp;serverTimezone=UTC&amp;allowPublicKeyRetrieval=true</url>
           <user>root</user>
           <password>[YOUR_MYSQL_ROOT_PASSWORD]</password>
       </databaseconnect>
       <option>
           <pagesize>10</pagesize>
           <monitorsize>10</monitorsize>
           <ver>2</ver>
       </option>
   </root>
   ```

### 4. Deploy to Tomcat

1. **Copy MySQL Connector**:
   - Copy `C:\pushdemoNew\WebContent\WEB-INF\lib\mysql-connector-java-8.0.33.jar`
   - Paste to `C:\apache-tomcat-9.0.XX\lib\`

2. **Deploy Web Application**:
   - Copy entire `C:\pushdemoNew\WebContent` folder
   - Paste to `C:\apache-tomcat-9.0.XX\webapps\`
   - Rename the copied folder to `pushdemo`

### 5. Start Services

1. **Open Command Prompt as Administrator**

2. **Start MySQL (if not already running)**:
   ```cmd
   net start mysql80
   ```

3. **Start Tomcat**:
   ```cmd
   cd C:\apache-tomcat-9.0.XX\bin
   catalina.bat run
   ```

## 🌐 Windows Service Configuration (Recommended)

### Install Tomcat as Windows Service

1. **Navigate to Tomcat Bin Directory**:
   ```cmd
   cd C:\apache-tomcat-9.0.XX\bin
   ```

2. **Install Service**:
   ```cmd
   service.bat install TomcatPushDemo
   ```

3. **Configure Service Properties**:
   - Open **Services.msc**
   - Find "TomcatPushDemo" service
   - Right-click → Properties
   - Set "Startup type" to "Automatic"
   - Click "Recovery" tab and configure restart options

4. **Start Service**:
   ```cmd
   net start TomcatPushDemo
   ```

### Configure MySQL Service
- MySQL should already be installed as a service
- Verify in Services.msc that MySQL service is set to "Automatic"

## 🔒 Security Configuration

### Windows Firewall Settings

1. **Open Windows Defender Firewall**:
   - Search for "Windows Defender Firewall with Advanced Security"

2. **Create Inbound Rules**:
   - Click "Inbound Rules" → "New Rule"
   - Select "Port" → "TCP"
   - Specific local ports: `8080` (for Tomcat)
   - Action: "Allow the connection"
   - Profile: Domain, Private, Public
   - Name: "Tomcat Web Server"
   - Repeat for MySQL port `3306` (restrict to internal network if possible)

### User Account Security

1. **Create Dedicated Service Accounts**:
   - Open "Computer Management" → "Local Users and Groups" → "Users"
   - Create service accounts for Tomcat and MySQL with minimal permissions

2. **Service Configuration**:
   - In Services.msc, configure Tomcat and MySQL to run under these service accounts
   - Assign necessary file system permissions to these accounts

## 🚀 Alternative: Docker Method on Windows Server

### Install Docker for Windows Server

1. **Enable Containers Feature**:
   - Open PowerShell as Administrator
   - Run: `Enable-WindowsOptionalFeature -Online -FeatureName Containers -All`

2. **Install Docker**:
   - Download Docker Enterprise Edition for Windows Server
   - Follow installation instructions

3. **Run Project with Docker**:
   ```powershell
   cd C:\pushdemoNew
   docker-compose build tomcat
   docker-compose up -d
   ```

## 🧪 Verification and Testing

### 1. Check Service Status

1. **Verify Services Are Running**:
   ```cmd
   sc query mysql80
   sc query TomcatPushDemo
   ```

2. **Check Ports Are Listening**:
   ```cmd
   netstat -an | findstr :8080
   netstat -an | findstr :3306
   ```

### 2. Access the Application

1. **Open Web Browser**
2. **Navigate to**: `http://localhost:8080/pushdemo` or `http://[SERVER_IP]:8080/pushdemo`
3. **Verify** the login page loads successfully

### 3. Test Database Connection

1. **Connect to MySQL**:
   ```cmd
   mysql -u root -p -e "USE pushdemo; SHOW TABLES;"
   ```

## 🔧 Troubleshooting

### Common Issues and Solutions

#### Application Won't Start
- **Check Logs**: Look in `C:\apache-tomcat-9.0.XX\logs\`
- **Verify JAR**: Ensure MySQL connector is in `C:\apache-tomcat-9.0.XX\lib\`
- **Check Config**: Verify database credentials in `config.xml`

#### Database Connection Fails
- **Check Service**: Ensure MySQL service is running
- **Verify Credentials**: Double-check username/password in config.xml
- **Test Connection**: Try connecting to MySQL directly with command line

#### Port Conflicts
- **Check Usage**:
  ```cmd
  netstat -ano | findstr :8080
  netstat -ano | findstr :3306
  ```
- **Change Ports**: If needed, modify Tomcat server.xml and config.xml

#### Permission Errors
- **Run As Admin**: Ensure services run with appropriate permissions
- **File Permissions**: Check that service accounts have access to required files

### Log Locations
- **Tomcat Logs**: `C:\apache-tomcat-9.0.XX\logs\`
- **MySQL Logs**: `C:\ProgramData\MySQL\MySQL Server 8.0\Data\[servername].err`
- **Application Logs**: Will appear in Tomcat logs

## 📋 Maintenance Tasks

### Regular Monitoring
- Monitor disk space in MySQL data directory
- Check Tomcat logs for errors
- Monitor system resources (CPU, memory, disk)

### Backup Strategy
1. **Database Backups**:
   - Schedule regular MySQL dumps using Windows Task Scheduler
   - Example backup script:
   ```cmd
   @echo off
   "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqldump.exe" -u root -p pushdemo > "C:\backups\pushdemo_%date:~-4,4%%date:~-10,2%%date:~-7,2%.sql"
   ```

2. **Configuration Backups**:
   - Regularly backup `config.xml` and Tomcat configuration files
   - Version control critical configuration files

### Update Process
1. **Backup Current System**
2. **Download New Version**
3. **Update Configuration Files**
4. **Test in Staging Environment**
5. **Deploy to Production**

## 🔄 Automatic Startup Scripts

### Batch Script for Manual Control
Create `C:\pushdemoNew\start_services.bat`:
```batch
@echo off
echo Starting Push Demo Services...
echo.
echo Starting MySQL...
net start mysql80
if %errorlevel% neq 0 echo MySQL service failed to start or is already running
timeout /t 10 /nobreak
echo.
echo Starting Tomcat...
net start TomcatPushDemo
if %errorlevel% neq 0 echo Tomcat service failed to start or is already running
echo.
echo Services startup complete!
echo Check http://localhost:8080/pushdemo to verify operation
pause
```

Create `C:\pushdemoNew\stop_services.bat`:
```batch
@echo off
echo Stopping Push Demo Services...
echo.
echo Stopping Tomcat...
net stop TomcatPushDemo
if %errorlevel% neq 0 echo Tomcat service failed to stop or is already stopped
echo.
echo Stopping MySQL...
net stop mysql80
if %errorlevel% neq 0 echo MySQL service failed to stop or is already stopped
echo.
echo Services shutdown complete!
pause
```

## 📞 Support Information

### Key Configuration Files
- **Database Config**: `C:\pushdemoNew\WebContent\WEB-INF\classes\config.xml`
- **Tomcat Config**: `C:\apache-tomcat-9.0.XX\conf\server.xml`
- **Application Config**: `C:\pushdemoNew\WebContent\WEB-INF\web.xml`

### Documentation References
- **API Documentation**: `C:\pushdemoNew\API_DOCUMENTATION.md`
- **Setup Guide**: `C:\pushdemoNew\SETUP_GUIDE.md`
- **Troubleshooting**: `C:\pushdemoNew\TROUBLESHOOTING_NO_LOGS.md`

---

**🎉 Your Push Demo System is Ready!**

Once configured, access the application at: `http://[SERVER_IP]:8080/pushdemo`

The system is now ready to manage biometric devices, process attendance logs, and handle real-time communications with ZK terminals.