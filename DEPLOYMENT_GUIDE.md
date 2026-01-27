# Push Demo Project - Deployment Guide

## 🎯 Server Requirements

### Minimum Server Specifications

| Component | Minimum | Recommended |
|-----------|---------|-------------|
| **CPU** | 2 cores | 4+ cores |
| **RAM** | 4 GB | 8+ GB |
| **Storage** | 20 GB | 50+ GB SSD |
| **OS** | Linux (Ubuntu 20.04+/CentOS 7+) | Ubuntu 22.04 LTS |
| **Network** | 100 Mbps | 1 Gbps |

### Software Requirements

#### 1. Operating System
- **Linux**: Ubuntu 20.04/22.04, CentOS 7/8, RHEL 8+
- **Windows**: Windows Server 2019/2022 (with Docker Desktop)
- **macOS**: macOS 12+ (for development)

#### 2. Container Runtime (Recommended)
- **Docker Engine**: 20.10+ (Required for Docker deployment)
- **Docker Compose**: 2.0+ (Included with Docker Desktop)

#### 3. Manual Deployment Requirements
If not using Docker:
- **Java**: OpenJDK 8 or Oracle JDK 8
- **Tomcat**: Apache Tomcat 9.0.x
- **MySQL**: MySQL 8.0.x
- **Build Tools**: Apache Maven 3.6+ or Ant

## 🚀 Deployment Options

### Option 1: Docker Deployment (Recommended)

#### Prerequisites
1. Install Docker Engine:
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install docker.io docker-compose -y
   
   # CentOS/RHEL
   sudo yum install docker docker-compose -y
   sudo systemctl start docker
   sudo systemctl enable docker
   ```

2. Add user to docker group:
   ```bash
   sudo usermod -aG docker $USER
   # Logout and login again
   ```

#### Deployment Steps

1. **Clone/Upload Project**
   ```bash
   # If using git
   git clone <repository-url>
   cd pushdemoNew
   
   # Or upload project files to server
   ```

2. **Download MySQL Connector**
   ```bash
   cd /path/to/pushdemoNew
   curl -L -o WebContent/WEB-INF/lib/mysql-connector-java-8.0.33.jar \
     "https://search.maven.org/remote_content?g=mysql&a=mysql-connector-java&v=8.0.33&c=jar"
   ```

3. **Start Services**
   ```bash
   # Build and start containers
   docker-compose build tomcat
   docker-compose up -d
   
   # Wait for services to start (30-60 seconds)
   sleep 30
   
   # Check status
   docker-compose ps
   ```

4. **Access Application**
   - **URL**: `http://<server-ip>:8080/pushdemo`
   - **Default Credentials**: 
     - Database: `root` / `root`
     - Database Name: `pushdemo`

### Option 2: Manual Deployment

#### Prerequisites Installation

1. **Install Java 8**
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install openjdk-8-jdk -y
   
   # CentOS/RHEL
   sudo yum install java-1.8.0-openjdk-devel -y
   ```

2. **Install MySQL 8.0**
   ```bash
   # Ubuntu
   sudo apt install mysql-server -y
   sudo mysql_secure_installation
   
   # CentOS/RHEL
   sudo yum install mysql-server -y
   sudo systemctl start mysqld
   sudo systemctl enable mysqld
   ```

3. **Install Tomcat 9**
   ```bash
   # Download Tomcat
   cd /opt
   sudo wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.82/bin/apache-tomcat-9.0.82.tar.gz
   sudo tar -xzf apache-tomcat-9.0.82.tar.gz
   sudo ln -s apache-tomcat-9.0.82 tomcat
   sudo chown -R $USER:$USER tomcat
   ```

#### Manual Deployment Steps

1. **Database Setup**
   ```bash
   # Create database
   mysql -u root -p
   CREATE DATABASE pushdemo DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
   EXIT;
   
   # Import schema
   mysql -u root -p pushdemo < doc/pushdemo.sql
   ```

2. **Update Configuration**
   - Edit `src/config.xml`:
     ```xml
     <url>jdbc:mysql://localhost:3306/pushdemo?useSSL=false&amp;serverTimezone=UTC&amp;allowPublicKeyRetrieval=true</url>
     ```
   - Copy to `WebContent/WEB-INF/classes/config.xml`

3. **Compile and Package**
   - Use your IDE to compile and create WAR file
   - Or manually copy classes to `WebContent/WEB-INF/classes/`

4. **Deploy to Tomcat**
   ```bash
   # Copy application to Tomcat
   sudo cp -r WebContent /opt/tomcat/webapps/pushdemo
   
   # Start Tomcat
   /opt/tomcat/bin/startup.sh
   ```

5. **Access Application**
   - URL: `http://<server-ip>:8080/pushdemo`

## 🌐 Network Configuration

### Ports to Open

| Service | Port | Protocol | Purpose |
|---------|------|----------|---------|
| HTTP | 80 | TCP | Web interface |
| HTTPS | 443 | TCP | Secure web interface |
| Tomcat | 8080 | TCP | Application server |
| MySQL | 3306 | TCP | Database server |
| SSH | 22 | TCP | Remote access |

### Firewall Configuration (Linux)

```bash
# Ubuntu (UFW)
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 8080/tcp
sudo ufw allow 3306/tcp
sudo ufw enable

# CentOS/RHEL (firewalld)
sudo firewall-cmd --permanent --add-service=ssh
sudo firewall-cmd --permanent --add-port=80/tcp
sudo firewall-cmd --permanent --add-port=443/tcp
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --permanent --add-port=3306/tcp
sudo firewall-cmd --reload
```

## 📦 Database Schema Overview

### Key Tables

1. **device_info** - Stores biometric device information
2. **user_info** - User enrollment data
3. **att_log** - Attendance logs
4. **device_command** - Device commands and responses
5. **pers_bio_template** - Biometric templates (fingerprints/faces)

### Storage Requirements
- **Minimum**: 500MB database
- **Recommended**: 10GB+ (for logs and biometric data)
- **Growth**: Plan for 100MB/month per 1000 users

## 🛠️ Environment Configuration

### 1. Update Configuration Files
- **Docker**: Use `docker/config.xml`
- **Manual**: Use `src/config.xml` with localhost MySQL

### 2. Logging Configuration
- **Log Level**: Can be configured in `WebContent/WEB-INF/classes/log4j.properties`
- **Log Location**: Default logs to Tomcat logs directory

### 3. Memory Configuration (Tomcat)
```bash
# Set in tomcat/bin/setenv.sh (create if not exists)
export JAVA_OPTS="-Xms1024m -Xmx4096m -XX:+UseG1GC"
```

## 🔐 Security Recommendations

### 1. Change Default Credentials
- **Database Password**: Change from `root` in `config.xml`
- **Admin Accounts**: Set secure passwords in application

### 2. SSL/TLS Setup
- **Generate SSL Certificate**: Use Let's Encrypt
- **Configure HTTPS**: Modify Tomcat server.xml

### 3. Network Security
- **Disable public database access**: Only allow localhost or VPN
- **Use reverse proxy**: Apache/Nginx for load balancing
- **Enable authentication**: At web server level

### 4. File Permissions
```bash
# Set secure permissions
chmod 750 WebContent/WEB-INF/lib/*
chmod 640 WebContent/WEB-INF/classes/config.xml
```

## 🔄 Maintenance Plan

### Regular Tasks

1. **Daily**:
   - Monitor application logs
   - Check database backup status
   - Review attendance log ingestion

2. **Weekly**:
   - Update dependencies and security patches
   - Clean up old logs (Tomcat/logs/*.log)

3. **Monthly**:
   - Full database backup
   - Review database size and performance
   - Clean up old biometric data

### Backup Strategy

#### Database Backup
```bash
# MySQL backup
mysqldump -u root -p pushdemo > pushdemo_backup_$(date +%F).sql
# Store in secure location (AWS S3, FTP, etc.)
```

#### Configuration Backup
```bash
# Backup key directories
tar -czf config_backup_$(date +%F).tar.gz WebContent/WEB-INF/
```

## 🧪 Testing Post-Deployment

1. **Verify Database Connection**
   - Access main dashboard: `http://server:8080/pushdemo`
   - Check user login: admin features

2. **Device Simulation**
   - Simulate a device POST: Test `/iclock/*` endpoints
   - Send heartbeat packet from emulated device

3. **End-to-End Functionality**
   - Register a sample device
   - Register a user via device dialog
   - Upload/verify templates or raw data
   - Attempt timestamp fixer

## 🔧 Monitoring and Debugging

### System Metrics
- **CPU**: Below 80% (when online + ID received)
- **Memory**: 768 MB limit (1st implementation has no page cleaning)
- **Database**: Queries returning expected times

### Application Logs
- **Access via CLI**: `docker logs` or `docker-compose logs`
- **Monitoring via Logging**: Runtime check logs in `/dev-push-mutex-service/:log`

### Errors to Watch For
- **Database Connection Issues**: Check MySQL service and credentials
- **Device Communication Errors**: Verify device IP and network access
- **Memory Leaks**: Monitor heap size and GC activity

## 🆘 Troubleshooting Common Issues

### 1. Application Not Starting
- **Check**: MySQL connector JAR presence and size (~2.5MB)
- **Check**: Database connection in logs
- **Check**: Port conflicts (8080, 3306)

### 2. Database Connection Failed
- **Verify**: MySQL service is running
- **Check**: Credentials in `config.xml`
- **Test**: `mysql -u root -p pushdemo`

### 3. Device Not Connecting
- **Verify**: Device IP can reach server port 8080
- **Check**: Firewall rules
- **Test**: `telnet server-ip 8080`

### 4. Performance Issues
- **Monitor**: CPU and memory usage
- **Check**: Database query performance
- **Review**: Log file sizes and rotation

## 📞 Support Information

### Documentation
- **API Documentation**: `API_DOCUMENTATION.md`
- **Setup Guide**: `SETUP_GUIDE.md`
- **Docker Setup**: `DOCKER_SETUP.md`

### Contact Points
- **Application Logs**: Primary source for debugging
- **Database Logs**: For connection and query issues
- **System Logs**: For infrastructure problems

---

**✅ Ready for Production Deployment!**

This guide provides all necessary information to deploy the Push Demo system to a production server. The Docker approach is recommended for ease of deployment and maintenance.