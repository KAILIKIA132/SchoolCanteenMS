# How to Add/Insert a ZK Device

## Method 1: Automatic Registration (Recommended) ✅

Devices are **automatically registered** when they connect to the server. This is the easiest and recommended method.

### Steps:

1. **On your ZK device**, access the Network/Communication settings
2. **Configure the server connection:**
   - **Server IP:** `192.168.253.98` (or your server's IP address)
   - **Server Port:** `8080`
   - **Server URL/Path:** `/iclock/cdata`
   - Or use full URL: `http://192.168.253.98:8080/iclock/cdata`
3. **Enable Push Communication**
4. **Save settings and restart the device**

### What Happens:

When the device connects for the first time:
- Device sends: `GET /iclock/cdata?SN=<device_serial_number>&options=all`
- Server automatically creates a device entry in the database
- Device appears in the device list at: `http://localhost:8080/deviceAction!deviceList.action`
- Device status shows as "Online"

### Verify Connection:

```bash
# Check server logs for device connection
docker-compose logs -f tomcat | grep -i "cdata\|device\|SN="

# Check database for registered devices
docker-compose exec mysql mysql -uroot -proot -D pushdemo -e "SELECT device_sn, device_name, ipaddress, state FROM device_info;"
```

---

## Method 2: Manual Database Insertion

If you need to manually insert a device (e.g., device is offline but you want to pre-register it):

### Step 1: Get Device Information

You'll need:
- **Device Serial Number (SN)** - Found on device or in device settings
- **Device IP Address** - The IP address of the device on your network

### Step 2: Insert Device Manually

Run this SQL command (replace values with your device info):

```bash
docker-compose exec mysql mysql -uroot -proot -D pushdemo << 'SQL'
INSERT INTO device_info (
    device_sn,
    device_name,
    alias_name,
    dept_id,
    state,
    last_activity,
    trans_times,
    trans_interval,
    log_stamp,
    op_log_stamp,
    photo_stamp,
    ipaddress,
    dev_language,
    push_version,
    time_zone,
    bioData_Stamp,
    idCard_Stamp,
    errorLog_Stamp,
    palm,
    mask,
    temperature
) VALUES (
    'YOUR_DEVICE_SN',              -- Replace with your device serial number
    'YOUR_DEVICE_SN(192.168.1.100)', -- Replace with SN(IP)
    '192.168.1.100',               -- Replace with device IP
    1,
    'Online',
    NOW(),
    '00:00;14:05',
    1,
    '0',
    '0',
    '0',
    '192.168.1.100',               -- Replace with device IP
    '69',
    '2.4.1',
    '+0800',
    '0',
    '0',
    '0',
    0,
    1,
    NULL
);
SQL
```

### Example:

For a device with SN `TDBD250100333` and IP `192.168.1.100`:

```bash
docker-compose exec mysql mysql -uroot -proot -D pushdemo << 'SQL'
INSERT INTO device_info (
    device_sn, device_name, alias_name, dept_id, state, last_activity,
    trans_times, trans_interval, log_stamp, op_log_stamp, photo_stamp,
    ipaddress, dev_language, push_version, time_zone,
    bioData_Stamp, idCard_Stamp, errorLog_Stamp, palm, mask, temperature
) VALUES (
    'TDBD250100333', 'TDBD250100333(192.168.1.100)', '192.168.1.100',
    1, 'Online', NOW(), '00:00;14:05', 1, '0', '0', '0',
    '192.168.1.100', '69', '2.4.1', '+0800',
    '0', '0', '0', 0, 1, NULL
);
SQL
```

### Verify Manual Insertion:

```bash
# Check if device was added
docker-compose exec mysql mysql -uroot -proot -D pushdemo -e "SELECT device_sn, device_name, ipaddress, state FROM device_info WHERE device_sn='YOUR_DEVICE_SN';"
```

---

## Troubleshooting

### Device Not Appearing After Connection

1. **Check server logs:**
   ```bash
   docker-compose logs tomcat | tail -50 | grep -i error
   ```

2. **Verify device can reach server:**
   - From device, ping: `ping 192.168.253.98`
   - Test URL: `http://192.168.253.98:8080/iclock/cdata?SN=YOUR_SN&options=all`

3. **Check firewall:**
   - Ensure port 8080 is open on the server

4. **Verify device serial number:**
   - Must be unique (no duplicates in database)
   - Check: `SELECT device_sn FROM device_info;`

### Device Shows as Offline

- Device may have disconnected
- Check network connectivity
- Device will reconnect automatically when network is restored

---

## Current Server Configuration

- **Server IP:** 192.168.253.98
- **Port:** 8080
- **Endpoint:** `/iclock/cdata`
- **Full URL:** `http://192.168.253.98:8080/iclock/cdata`

