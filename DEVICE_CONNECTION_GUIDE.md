# ZK Device Connection Guide

## ✅ Server Status
- **Server IP:** 192.168.253.98
- **Port:** 8080
- **Context Path:** / (root context)
- **Status:** Running and ready to accept device connections

## 🔌 Device Connection URL

Your ZK devices should be configured with the following server URL:

```
http://192.168.253.98:8080/iclock/cdata
```

**Note:** The application is now deployed at the root context, so devices can connect directly using just the IP and port without any path prefix.

## 📋 Device Configuration Steps

1. **On your ZK device**, go to Network/Communication settings
2. **Set the Server IP:** `192.168.253.98`
3. **Set the Server Port:** `8080`
4. **Server Path/URL:** Not required! The application is at root context.
   - If your device requires a path, use: `/iclock/cdata`
   - Or full URL: `http://192.168.253.98:8080/iclock/cdata`
5. **Enable Push Communication** (if available)
6. **Save and restart** the device

## 🔄 How Automatic Registration Works

When a ZK device connects for the first time:

1. Device sends a GET request to: `/iclock/cdata?SN=<device_serial_number>&options=all`
2. Server automatically:
   - Creates a new device entry in the database
   - Sets device status to "Online"
   - Records device IP address
   - Returns configuration options to the device
3. Device appears automatically in the device list at: `http://localhost:8080/deviceList.action`

## ✅ Verify Device Connection

### Method 1: Check Device List in Web Interface
1. Open browser: `http://localhost:8080`
2. Navigate to **Device List** page
3. Your connected devices should appear automatically

### Method 2: Check Server Logs
```bash
cd /Users/aaron/pushdemoNew
docker-compose logs -f tomcat | grep -i "device\|cdata\|SN"
```

### Method 3: Check Database
```bash
docker-compose exec mysql mysql -u root -proot -e "SELECT device_sn, device_name, ipaddress, state, last_activity FROM pushdemo.device_info;"
```

## 🔍 Troubleshooting

### Device Not Appearing

1. **Check Network Connectivity**
   - Ensure device and server are on the same network
   - Ping the server from device: `ping 192.168.253.98`
   - Test from device browser: `http://192.168.253.98:8080/iclock/cdata?SN=YOUR_DEVICE_SN&options=all`

2. **Verify URL Format**
   - Application is at root context (no path prefix needed)
   - Full URL: `http://192.168.253.98:8080/iclock/cdata`
   - Some devices need just the path: `/iclock/cdata`

3. **Check Firewall**
   - Ensure port 8080 is open on the server
   - Check if firewall is blocking connections

4. **Check Server Logs for Errors**
   ```bash
   docker-compose logs tomcat | tail -50
   ```

5. **Verify Device Serial Number**
   - Device SN must be unique
   - Check device SN in device settings

### Common Issues

**Issue:** Device connects but doesn't appear in list
- **Solution:** Refresh the device list page or wait a few seconds

**Issue:** "Unknown column 'bioData_Stamp'" error
- **Status:** ✅ FIXED - Database schema updated

**Issue:** Device shows as "Offline"
- **Solution:** Device may have disconnected. Check network and device power

## 📊 Monitoring Device Connections

### Real-time Log Monitoring
```bash
# Watch for device connections
docker-compose logs -f tomcat | grep -E "cdata|device|SN="

# Watch for errors
docker-compose logs -f tomcat | grep -i error
```

### Check Connected Devices
```bash
# View all devices in database
docker-compose exec mysql mysql -u root -proot pushdemo -e "SELECT device_sn, device_name, ipaddress, state, last_activity FROM device_info ORDER BY last_activity DESC;"
```

## 🌐 Network Information

- **Server IP:** 192.168.253.98
- **Server Port:** 8080
- **Database:** Running in Docker (internal network)
- **Application:** Accessible from network

## 📝 Notes

- Devices are automatically registered when they first connect
- Device information is stored in the `device_info` table
- Each device must have a unique serial number (SN)
- Devices will appear in the web interface automatically after connection
- The server supports both GET and POST requests from devices

---

**Your server is ready! Configure your ZK devices with the URL above and they will connect automatically.**

