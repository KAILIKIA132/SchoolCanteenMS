# Monitor External API Calls

## ✅ Logging Added

Detailed logging has been added to track external API calls. You will now see logs at every step of the process.

## How to Monitor

### Real-time Monitoring

Watch for external API calls in real-time:

```bash
docker-compose logs -f tomcat | grep -i "external api"
```

### View All Recent Logs

```bash
docker-compose logs tomcat | grep -i "external api" | tail -50
```

## What Logs to Look For

When a verification occurs, you should see these logs in sequence:

### 1. Attendance Log Saved
```
INFO - attlog size:1
```

### 2. External API Processing Started
```
INFO - === EXTERNAL API: Attendance logs saved, starting external API calls ===
INFO - EXTERNAL API: Processing 1 verification(s)
INFO - EXTERNAL API: Processing verification - User PIN: 1, User Name: AARON, Timestamp: 2025-11-08 10:02:10
```

### 3. External API Called
```
INFO - EXTERNAL API: Calling notifyVerification for User ID: 1
INFO - === EXTERNAL API: notifyVerification called with userId: 1 ===
INFO - EXTERNAL API: Submitting async task for userId: 1
INFO - EXTERNAL API: Async task submitted for userId: 1
```

### 4. API Request Details
```
INFO - EXTERNAL API: Async task started for userId: 1
INFO - === EXTERNAL API: callExternalApi started for userId: 1 ===
INFO - EXTERNAL API: URL: http://192.168.253.45:8001/api/meal-cards/generate-with-check
INFO - EXTERNAL API: Creating connection to: http://192.168.253.45:8001/api/meal-cards/generate-with-check
INFO - EXTERNAL API: Connection configured, formatting student ID...
INFO - EXTERNAL API: Student ID formatted - Original: 1, Formatted: 001
INFO - EXTERNAL API: Request body: {"student_id": "001", "meal_type": "lunch"}
```

### 5. API Request Sent
```
INFO - EXTERNAL API: Writing request body to connection...
INFO - EXTERNAL API: Request body written, getting response...
INFO - EXTERNAL API: Response code received: 200
```

### 6. API Response
```
INFO - EXTERNAL API call successful for student_id: 001 (original: 1), Response Code: 200, Response: {...}
```

### 7. Processing Complete
```
INFO - === EXTERNAL API: Finished processing all verifications ===
```

## Troubleshooting

### No Logs Appearing

If you don't see any "EXTERNAL API" logs:

1. **Check if verifications are being received:**
   ```bash
   docker-compose logs tomcat | grep -i "attlog size"
   ```

2. **Check if logs are being saved:**
   ```bash
   docker-compose exec mysql mysql -uroot -proot pushdemo -e "SELECT * FROM att_log ORDER BY verify_time DESC LIMIT 5;"
   ```

3. **Verify classes are compiled:**
   ```bash
   ls -la WebContent/WEB-INF/classes/com/zk/util/ExternalApiUtil.class
   ls -la WebContent/WEB-INF/classes/com/zk/manager/AttLogManager.class
   ```

4. **Check for errors:**
   ```bash
   docker-compose logs tomcat | grep -i "error\|exception" | tail -20
   ```

### Logs Stop at "notifyVerification called"

If logs stop after "notifyVerification called" but don't show "Async task started":

- The async task might not be executing
- Check for thread pool issues
- Check for exceptions in logs

### Logs Stop at "Creating connection"

If logs stop at "Creating connection":

- Network connectivity issue
- API server might be unreachable
- Check firewall/network settings

### Error Logs

If you see error logs:

```
ERROR - Error calling external API for student_id: 1, URL: ..., Error: ...
```

- Check the error message
- Verify API server is running
- Check network connectivity

## Test the API Manually

To verify the API is accessible:

```bash
curl --location 'http://192.168.253.45:8001/api/meal-cards/generate-with-check' \
  --header 'Content-Type: application/json' \
  --header 'Accept: application/json' \
  --data '{"student_id": "001", "meal_type": "lunch"}'
```

## Next Steps

1. **Perform a verification** on the device (scan fingerprint/face)
2. **Watch the logs** in real-time:
   ```bash
   docker-compose logs -f tomcat | grep -i "external api"
   ```
3. **Verify the flow** - You should see all the log messages above

## Summary

With the detailed logging, you can now:
- ✅ See when external API is called
- ✅ See the exact request being sent
- ✅ See the response received
- ✅ Debug any issues step-by-step

All logs are prefixed with "EXTERNAL API:" for easy filtering.









