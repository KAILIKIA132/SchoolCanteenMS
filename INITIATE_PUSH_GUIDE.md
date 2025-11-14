# How to Initiate Push from BioTime 9.5 and ZK Device

This guide explains how to trigger your ZK device to push attendance logs to the server.

## Understanding the Push Mechanism

The ZK device uses a **push protocol** where:
- The server sends configuration options to the device (including `TransFlag=TransData AttLog` and `Realtime=1`)
- The device connects periodically and checks for new logs
- When logs exist (`ATTLOGStamp > 0`), the device pushes them to the server
- With `Realtime=1`, logs are pushed immediately after each verification

## Current Server Configuration

Your server is already configured correctly:
- ✅ `TransFlag=TransData AttLog` - Tells device to send attendance logs
- ✅ `Realtime=1` - Enables real-time push
- ✅ `TransInterval=1` - Device checks every 1 minute
- ✅ Push endpoint: `http://192.168.253.98:8080/iclock/cdata`

## Methods to Initiate Push

### Method 1: Using the Web Interface (Recommended)

1. **Open Device List Page**
   - Navigate to: `http://localhost:8080/deviceAction!deviceList.action`
   - Find your device: `TDBD250100333`

2. **Send LOG Command**
   - Select your device (check the checkbox)
   - Go to **Device Commands** → **Check Data Cmd** → **Check and Send New Data**
   - This sends a `LOG` command to the device, instructing it to check for and send any new logs

3. **Reload Device Options** (if needed)
   - Select your device
   - Go to **Device Commands** → **Config Cmd** → **Refresh Device Option**
   - This forces the device to reload push configuration from the server

### Method 2: Configure BioTime 9.5

If you're using BioTime 9.5 software to manage your device:

1. **Open BioTime 9.5**
   - Connect to your device

2. **Configure Push Settings**
   - Go to **Device Management** → Select your device
   - Navigate to **Communication** or **Network Settings**
   - Set **Push Server URL**: `http://192.168.253.98:8080/iclock/cdata`
   - Enable **Real-time Push** or **Push Communication**
   - Enable **AttLog** in the push data types
   - Set **Push Interval**: 1 minute (or as needed)

3. **Apply and Restart**
   - Save the configuration
   - Restart the device or reload options

### Method 3: Direct Device Configuration

If you have access to the device's web interface or menu:

1. **Access Device Settings**
   - Connect to device via web interface (usually `http://[device-ip]`)
   - Or use the device's menu system

2. **Configure Push Server**
   - Go to **Communication** → **Push Settings**
   - Set **Server URL**: `http://192.168.253.98:8080/iclock/cdata`
   - Set **Device SN**: `TDBD250100333`
   - Enable **Real-time Push**: Yes
   - Enable **AttLog Push**: Yes
   - Set **Push Interval**: 1 minute

3. **Save and Restart**
   - Save configuration
   - Restart the device

### Method 4: Perform Verifications on Device

The simplest way to trigger push:

1. **Perform Biometric Verifications**
   - Scan fingerprint, face, or card on the physical device
   - Each verification creates a log entry

2. **Automatic Push**
   - With `Realtime=1`, logs are pushed immediately after verification
   - The device will connect to the server and send the log within seconds

3. **Verify on Server**
   - Check: `http://localhost:8080/attAction!verificationList.action`
   - The verification should appear within 3-5 seconds

## Troubleshooting

### Issue: Device shows `ATTLOGStamp=0`

**Problem**: Device has no logs stored to push.

**Solution**:
1. Perform actual biometric verifications on the device
2. Verify logs exist on the device (check device's attendance log menu)
3. Then use Method 1 (LOG command) to trigger push

### Issue: Device not connecting

**Check**:
1. Device IP address is correct: `192.168.253.98`
2. Server is accessible from device network
3. Port 8080 is open
4. Device shows "Online" status in web interface

**Solution**:
- Check network connectivity
- Verify firewall settings
- Use "Refresh Data" command in web interface

### Issue: Logs not appearing after push

**Check**:
1. Check Tomcat logs: `docker-compose logs tomcat | grep -i "attlog\|parse"`
2. Verify database: `docker-compose exec mysql mysql -uroot -proot pushdemo -e "SELECT COUNT(*) FROM att_log;"`
3. Check device is sending POST requests with `table=ATTLOG`

**Solution**:
- Check server logs for errors
- Verify database connection
- Ensure device has logs to send (`ATTLOGStamp > 0`)

## Quick Command Reference

### Via Web Interface URLs:

- **Send LOG Command**: 
  ```
  http://localhost:8080/deviceAction!logData.action?sn=TDBD250100333
  ```

- **Reload Options**: 
  ```
  http://localhost:8080/deviceAction!reloadOption.action?sn=TDBD250100333
  ```

- **Check Device Data**: 
  ```
  http://localhost:8080/deviceAction!checkDeviceData.action?sn=TDBD250100333
  ```

### Check Server Logs:

```bash
# Watch for attendance log processing
docker-compose logs -f tomcat | grep -i "attlog\|parse"

# Check device connection
docker-compose logs tomcat | grep -i "TDBD250100333"
```

### Check Database:

```bash
# Count attendance logs
docker-compose exec mysql mysql -uroot -proot pushdemo -e "SELECT COUNT(*) FROM att_log;"

# View recent logs
docker-compose exec mysql mysql -uroot -proot pushdemo -e "SELECT * FROM att_log ORDER BY verify_time DESC LIMIT 10;"
```

## Expected Behavior

Once push is working correctly:

1. **Device connects** every minute (or as configured)
2. **Server sends options** with `TransFlag=TransData AttLog` and `Realtime=1`
3. **Device checks** for new logs (`ATTLOGStamp`)
4. **If logs exist**, device sends POST request to `/iclock/cdata?table=ATTLOG`
5. **Server processes** logs and saves to database
6. **Logs appear** on verification responses page within seconds

## Next Steps

1. **Perform a test verification** on the physical device
2. **Send LOG command** via web interface (Method 1)
3. **Check verification responses page** for the log
4. **Monitor Tomcat logs** to see the push happening in real-time

---

**Note**: The server is already configured correctly. The main requirement is that the device must have attendance logs stored on it (`ATTLOGStamp > 0`) before it can push them. Perform verifications on the device first, then trigger the push.









