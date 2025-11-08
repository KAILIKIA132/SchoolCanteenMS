# ZK Device Push Configuration Guide

## Current Status
✅ Device is using **PUSH Protocol** (confirmed in Device Type Settings)
✅ Device Type: **A&C PUSH** (Access Control Push)

## Steps to Enable Log Push

### Step 1: Access Logs Settings
From the **System** menu, select:
- **Access Logs Settings** (the orange document icon)

### Step 2: Configure Push Settings
Within **Access Logs Settings**, look for and configure:

1. **Push Communication / Push Server**
   - **Enable**: Turn ON push communication
   - **Server IP**: `192.168.253.98`
   - **Port**: `8080`
   - **URL/Path**: `/iclock/cdata` (or leave empty if device doesn't support path)
   - **Device SN**: `TDBD250100333` (should auto-fill or enter manually)

2. **Real-time Push / Immediate Push**
   - **Enable**: Turn ON real-time push
   - This ensures logs are sent immediately after each verification

3. **Push Interval**
   - Set to **1 minute** (or as needed)
   - This is how often device checks for new logs to push

4. **Data Types to Push**
   - **Enable "AttLog"** or **"Attendance Log"**
   - **Enable "OpLog"** (optional - operation logs)
   - **Enable "AttPhoto"** (optional - attendance photos)

5. **Log Storage**
   - Ensure **"Save Logs"** or **"Store Logs"** is **ENABLED**
   - Set to **"Device + Server"** or **"Device"** (not "Server Only")

### Step 3: Save and Restart
1. **Save** all settings
2. **Restart** the device (or use "Reload Options" from web interface)

### Step 4: Verify Configuration
After restart, check:

1. **Device Connection**
   - Go to: `http://localhost:8080/deviceAction!deviceList.action`
   - Device should show "Online" status

2. **Check Server Logs**
   ```bash
   docker-compose logs -f tomcat | grep -i "TDBD250100333"
   ```
   - Should see device connecting regularly
   - Should see `ATTLOGStamp` value (may still be 0 until first verification)

3. **Perform Test Verification**
   - Scan fingerprint/face/card on device
   - Check logs appear within seconds

4. **Check Logs**
   - Go to: `http://localhost:8080/attAction!verificationList.action`
   - Logs should appear within 3-5 seconds

## Common Settings Names

Different ZK device models may use different terminology. Look for:

**Push Communication:**
- "Push Communication"
- "Push Server"
- "Data Upload"
- "Network Push"
- "Server Communication"

**Real-time Push:**
- "Real-time Push"
- "Immediate Push"
- "Instant Push"
- "Push on Event"

**Attendance Logs:**
- "AttLog"
- "Attendance Log"
- "Verification Log"
- "Access Log"

**Server Settings:**
- "Server IP" or "Push Server IP"
- "Server Port" or "Push Port"
- "Server URL" or "Push URL"

## If "Access Logs Settings" Doesn't Have Push Options

If you don't see push settings in "Access Logs Settings", try:

1. **Communication Settings**
   - System → Communication Settings
   - Look for push/server configuration

2. **Network Settings**
   - System → Network Settings
   - Look for push communication options

3. **Device Type Settings** (where you are now)
   - May have additional push configuration options
   - Look for "Push Options" or "Communication Options"

4. **Advanced Settings**
   - System → Advanced Settings
   - Look for push/data upload settings

## Troubleshooting

### Issue: Can't find push settings
- Check device firmware version
- Some older devices may not support push
- May need to update firmware

### Issue: Settings saved but logs still not pushing
1. **Restart device** (power cycle)
2. **Send RELOAD OPTIONS command** from web interface:
   - Device List → Select device → Device Commands → Config Cmd → Refresh Device Option
3. **Check device logs** on device menu:
   - System → View Logs → Check if logs are stored
4. **Verify server accessibility**:
   - Device should be able to reach `192.168.253.98:8080`
   - Check firewall/network settings

### Issue: Device connects but ATTLOGStamp=0
- Device has no logs stored
- Perform biometric verifications on device
- Check "Save Logs" is enabled in Access Logs Settings

## Expected Result

After configuration:
- ✅ Device connects every 1 minute (or configured interval)
- ✅ `ATTLOGStamp > 0` in server logs (after first verification)
- ✅ POST requests with `table=ATTLOG` appear in Tomcat logs
- ✅ Logs appear in database and frontend within seconds

---

**Next Step**: Navigate to **Access Logs Settings** and enable push communication with the server details above.




