# Windows Server Setup Guide

This repository contains automated scripts to deploy the Push Demo biometric device management system on Windows Server using a Python virtual environment.

## 📋 Prerequisites

Before running the setup script, ensure your Windows Server meets these requirements:

### Hardware Requirements
- **CPU**: Dual-core 2.0 GHz or better
- **RAM**: 8 GB minimum (16 GB recommended)
- **Storage**: 50 GB free space minimum
- **Network**: 1 Gbps network interface

### Software Requirements
- **Windows Server**: 2016, 2019, or 2022
- **PowerShell**: Version 5.1 or later
- **Administrator privileges**: Required for installation

## 🚀 Quick Start

### 1. Download the Repository
Clone or download this repository to your Windows Server.

### 2. Run the Setup Script
Open **PowerShell as Administrator** and navigate to the repository directory:

```powershell
cd C:\path\to\SchoolCanteenMS
.\setup-windows-server.ps1
```

Or run the batch file:
```cmd
setup-windows-server.bat
```

### 3. Follow the Prompts
The script will:
- Install Java 8, MySQL 8.0, and Tomcat 9
- Clone the project from GitHub
- Set up a Python virtual environment
- Configure the database
- Deploy the application
- Create startup scripts

## ⚙️ Script Parameters

You can customize the installation with these parameters:

```powershell
.\setup-windows-server.ps1 -InstallPath "D:\SchoolCanteen" -MySQLRootPassword "MySecurePassword123"
```

### Available Parameters:
- **`-InstallPath`**: Installation directory (default: `C:\pushdemoNew`)
- **`-GitUrl`**: Git repository URL (default: `https://github.com/KAILIKIA132/SchoolCanteenMS.git`)
- **`-MySQLRootPassword`**: MySQL root password (will prompt if not provided)
- **`-TomcatVersion`**: Tomcat version (default: `9.0.84`)
- **`-JavaVersion`**: Java version (default: `8.0.392`)

## 📁 Installation Structure

After successful installation, your system will have this structure:

```
C:\pushdemoNew\                    # Main installation directory
├── WebContent\                    # Web application files
├── src\                          # Java source files
├── doc\                          # Documentation and SQL scripts
├── venv\                         # Python virtual environment
├── start-services.bat            # Start all services
├── stop-services.bat             # Stop all services
├── start-proxy-api.bat           # Start Python proxy API
└── setup.log                     # Installation log

C:\apache-tomcat-9.0.84\          # Tomcat installation
├── bin\
├── lib\
├── webapps\
└── ...

C:\Program Files\MySQL\MySQL Server 8.0\  # MySQL installation
```

## 🌐 Accessing the Application

Once installation is complete, access the application at:
```
http://localhost:8080/pushdemo
```

Or from another machine:
```
http://[SERVER_IP]:8080/pushdemo
```

## 🔄 Managing Services

### Start All Services
```cmd
C:\pushdemoNew\start-services.bat
```

### Stop All Services
```cmd
C:\pushdemoNew\stop-services.bat
```

### Start Python Proxy API
```cmd
C:\pushdemoNew\start-proxy-api.bat
```

### Manual Service Control
```cmd
# Start services
net start MySQL80
net start TomcatPushDemo

# Stop services
net stop TomcatPushDemo
net stop MySQL80
```

## 🔧 Configuration Files

### Database Configuration
- **Location**: `C:\pushdemoNew\WebContent\WEB-INF\classes\config.xml`
- **Service**: MySQL 8.0
- **Port**: 3306
- **Database**: pushdemo

### Web Application Configuration
- **Location**: `C:\apache-tomcat-9.0.84\conf\server.xml`
- **Port**: 8080
- **Application Path**: /pushdemo

### Python Virtual Environment
- **Location**: `C:\pushdemoNew\venv`
- **Python Version**: 3.8+
- **Dependencies**: Flask, requests

## 🔒 Security Considerations

### Firewall Rules
The setup script automatically creates these firewall rules:
- **Port 8080**: Tomcat web server
- **Port 3306**: MySQL database (restrict to internal network in production)

### Service Accounts
For production environments, consider:
1. Creating dedicated service accounts for Tomcat and MySQL
2. Running services under these accounts instead of SYSTEM
3. Setting appropriate file system permissions

## 📊 Monitoring and Maintenance

### Log Files
- **Setup Log**: `C:\pushdemoNew\setup.log`
- **Tomcat Logs**: `C:\apache-tomcat-9.0.84\logs\`
- **MySQL Logs**: `C:\ProgramData\MySQL\MySQL Server 8.0\Data\[hostname].err`

### Backup Strategy
1. **Database Backups**:
   ```cmd
   mysqldump -u root -p pushdemo > backup.sql
   ```

2. **Configuration Backups**:
   - Regularly backup `config.xml`
   - Backup Tomcat configuration files

### Updates
To update the application:
1. Stop services
2. Pull latest changes from Git
3. Redeploy to Tomcat
4. Restart services

## 🛠️ Troubleshooting

### Common Issues

#### Services Won't Start
- Check Windows Event Viewer for detailed error messages
- Verify all required files are present
- Ensure sufficient system resources

#### Database Connection Failed
- Verify MySQL service is running
- Check credentials in `config.xml`
- Test connection manually:
  ```cmd
  mysql -u root -p
  ```

#### Application Not Accessible
- Check if Tomcat is running:
  ```cmd
  netstat -an | findstr :8080
  ```
- Verify firewall rules
- Check Tomcat logs for errors

### Getting Help
- Check log files for detailed error messages
- Review the setup log: `C:\pushdemoNew\setup.log`
- Consult the documentation in the `doc\` directory

## 📞 Support

For issues with the setup script:
1. Check the setup log file
2. Verify all prerequisites are met
3. Ensure you're running as Administrator
4. Review the troubleshooting section above

---

**🎉 Your Push Demo System is Ready!**

The system is now configured to manage biometric devices, process attendance logs, and handle real-time communications with ZK terminals.