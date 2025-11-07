# Troubleshooting: No Logs After Enrollment and Verification

## Problem
You've enrolled a user and verified successfully on the device, but logs are not appearing in the system.

## Current Status
- ✅ Device is connecting (`TDBD250100333`)
- ✅ Server is configured correctly (`TransFlag=TransData AttLog`, `Realtime=1`)
- ❌ Device reports `ATTLOGStamp=0` (no logs stored)
- ❌ No POST requests with `table=ATTLOG` received
- ❌ Database has 0 logs

## Root Cause
The device is **not storing attendance logs** or **not pushing them** to the server, even though verifications are successful.

## Solutions

### Solution 1: Check Device Settings (Most Common)

The device may not be configured to store attendance logs. Check these settings on the physical device:

1. **Access Device Menu/Web Interface**
   - Connect to device via web: `http://[device-ip]`
   - Or use the device's menu system

2. **Check Attendance Log Settings**
   - Go to **System Settings** → **Data Management** → **Attendance Log**
   - Ensure **"Save Attendance Log"** or **"Store Logs"** is **ENABLED**
   - Check **"Log Storage"** is set to **"Device"** or **"Device + Server"**

3. **Check Push Settings**
   - Go to **Communication** → **Push Settings**
   - Ensure **"Push Attendance Log"** is **ENABLED**
   - Verify **"Real-time Push"** is **ENABLED**
   - Check **"Push Interval"** is set (e.g., 1 minute)

4. **Save and Restart Device**
   - Save all settings
   - Restart the device

### Solution 2: Verify Log Storage on Device

Check if logs are actually stored on the device:

1. **On Device Menu:**
   - Go to **Data Management** → **Attendance Log** → **View Logs**
   - Check if your verification appears in the device's log list
   - If logs appear on device but not on server → Push configuration issue
   - If logs don't appear on device → Log storage is disabled

2. **Via BioTime 9.5:**
   - Open BioTime 9.5
   - Connect to device
   - Go to **Device Management** → Select device
   - Click **"Download Attendance Logs"** or **"Get Logs"**
   - Check if logs appear in BioTime

### Solution 3: Force Device to Push Logs

If logs exist on device but aren't pushing:

1. **Send RELOAD OPTIONS Command**
   - Go to Device List page
   - Select device `TDBD250100333`
   - Device Commands → Config Cmd → **Refresh Device Option**
   - This forces device to reload push configuration

2. **Send LOG Command Again**
   - After reloading options, send `LOG` command again
   - Device Commands → Check Data Cmd → **Check and Send New Data**

3. **Check Device Connection**
   - Ensure device shows "Online" status
   - Device should connect every 1 minute (based on `TransInterval=1`)

### Solution 4: Check Device Firmware/Version

Some device firmware versions have issues with log storage:

1. **Check Device Version**
   - On device menu: **System** → **About** → **Version**
   - Or via web interface: **System Info**

2. **Update Firmware (if needed)**
   - Download latest firmware from ZKTeco website
   - Update device firmware
   - Reconfigure push settings after update

### Solution 5: Verify User Enrollment

Ensure the user is properly enrolled:

1. **Check User on Device**
   - On device menu: **User Management** → **View Users**
   - Verify your enrolled user appears
   - Check user has biometric template (fingerprint/face)

2. **Check User on Server**
   - Go to: `http://localhost:8080/userAction!userList.action`
   - Verify user exists in server database
   - If user doesn't exist, enroll via web interface

3. **Re-enroll User (if needed)**
   - Delete user from device
   - Re-enroll user with biometric
   - Perform verification again

### Solution 6: Test with Direct Query

Query logs directly from device:

1. **Send DATA QUERY ATTLOG Command**
   - Go to Device Commands page
   - Select device `TDBD250100333`
   - Enter command:
     ```
     DATA QUERY ATTLOG StartTime=2025-11-07 00:00:00	EndTime=2025-11-07 23:59:59
     ```
   - Click "Send Command"
   - Check "Return Value" column for logs

2. **If Query Returns Logs**
   - Logs exist on device → Push configuration issue
   - Fix push settings (Solution 1)

3. **If Query Returns Empty**
   - Logs don't exist on device → Log storage disabled
   - Enable log storage (Solution 1)

## Diagnostic Commands

### Check Server Logs
```bash
# Watch for attendance log POST requests
docker-compose logs -f tomcat | grep -i "POST.*cdata.*table=ATTLOG"

# Watch for log parsing
docker-compose logs -f tomcat | grep -i "begin parse op attlog\|end parse op attlog"

# Check device stamps
docker-compose logs tomcat | grep -i "ATTLOGStamp"
```

### Check Database
```bash
# Count logs
docker-compose exec mysql mysql -uroot -proot pushdemo -e "SELECT COUNT(*) FROM att_log;"

# View recent logs
docker-compose exec mysql mysql -uroot -proot pushdemo -e "SELECT * FROM att_log ORDER BY verify_time DESC LIMIT 10;"

# Check device stamp
docker-compose exec mysql mysql -uroot -proot pushdemo -e "SELECT device_sn, log_stamp FROM device_info WHERE device_sn = 'TDBD250100333';"
```

### Check Device Connection
```bash
# Monitor device connections
docker-compose logs -f tomcat | grep -i "TDBD250100333"
```

## Expected Behavior After Fix

Once fixed, you should see:

1. **Device Reports Non-Zero Stamp**
   - `ATTLOGStamp > 0` in logs
   - `log_stamp > 0` in database

2. **POST Requests Received**
   - Logs show: `POST ... /iclock/cdata?table=ATTLOG`
   - Logs show: `begin parse op attlog`

3. **Logs in Database**
   - `SELECT COUNT(*) FROM att_log;` returns > 0
   - Logs appear on verification responses page

4. **Logs on Frontend**
   - `http://localhost:8080/attAction!verificationList.action` shows verification entries

## Most Likely Solution

Based on the symptoms (`ATTLOGStamp=0`), the most likely issue is:

**The device is not configured to store attendance logs.**

**Action:**
1. Access device settings (web interface or menu)
2. Enable **"Save Attendance Log"** or **"Store Logs"**
3. Enable **"Push Attendance Log"** and **"Real-time Push"**
4. Save and restart device
5. Perform a new verification
6. Check logs appear

## Next Steps

1. **Check device settings** (Solution 1) - Most common fix
2. **Verify logs exist on device** (Solution 2) - Diagnostic step
3. **If logs exist but not pushing** → Fix push settings (Solution 1)
4. **If logs don't exist** → Enable log storage (Solution 1)
5. **Test with query command** (Solution 6) - Verify logs exist

---

**Note**: The server is configured correctly. The issue is with the device configuration or log storage settings.

