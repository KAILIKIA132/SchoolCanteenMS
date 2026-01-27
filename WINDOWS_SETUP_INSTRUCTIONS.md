# Windows Server Setup Instructions

This guide explains how to deploy the Push Demo system on a fresh Windows Server using the automated PowerShell script.

## 📋 Prerequisites

Before running the script, ensure you have:
1. **Administrator Access**: You must be logged in as an Administrator.
2. **Internet Connection**: The script needs to download Java, MySQL, and Tomcat.
3. **PowerShell 5.1+**: Standard on Windows Server 2016/2019/2022.

## 🚀 Quick Start

1. **Copy Files**:
   Copy the `setup.ps1` script to your Windows Server (e.g., to `C:\Users\Administrator\Desktop`).

2. **Run PowerShell as Administrator**:
   - Right-click the Start button and select **Windows PowerShell (Admin)**.

3. **Execute the Script**:
   Run the following command to start the installation:
   ```powershell
   # Navigate to where you copied the script
   cd $env:USERPROFILE\Desktop

   # Allow script execution if needed
   Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope Process -Force

   # Run the setup script
   .\setup.ps1
   ```

## ⚙️ Custom Configuration (Optional)

You can customize the installation by providing parameters:

```powershell
.\setup.ps1 -InstallPath "D:\Apps\PushDemo" -MySQLRootPassword "SecurePass123!"
```

| Parameter | Default | Description |
|-----------|---------|-------------|
| `-InstallPath` | `C:\pushdemoNew` | Where to install the application |
| `-GitUrl` | `...Kailikia132...` | Git repository to clone |
| `-MySQLRootPassword` | *(Prompt)* | Root password for MySQL |

## 🔍 Verification

After the script completes successfully:

1. **Web Interface**:
   Open a browser and visit: `http://localhost:8080/pushdemo`

2. **Services**:
   The script creates Windows Services that start automatically:
   - `MySQL80` (Database)
   - `TomcatPushDemo` (Web Server)

3. **Proxy API**:
   Since Python processes cannot easily run as native services without extra tools, a startup script is created:
   - Run `C:\pushdemoNew\start-proxy-api.bat` to start the proxy if needed.

## 🛠️ Management

Shortcut scripts are created in the installation folder (default `C:\pushdemoNew`):

- **start-services.bat**: Starts MySQL and Tomcat
- **stop-services.bat**: Stops MySQL and Tomcat
- **start-proxy-api.bat**: Starts the Python proxy for device communication

## ❓ Troubleshooting

- **Logs**: Check `C:\pushdemoNew\setup.log` for detailed installation logs.
- **Firewall**: The script attempts to open ports 8080 and 3306. If blocked, manually allow TCP ports 8080 and 3306 in Windows Firewall.
