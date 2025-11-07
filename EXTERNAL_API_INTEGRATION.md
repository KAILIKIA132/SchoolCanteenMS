# External API Integration

## Overview

The system is now configured to automatically call an external API whenever a verification occurs. When a user verifies (fingerprint, face, or card scan), the system will:

1. Save the verification log to the database
2. Automatically call the external API with the user ID

## External API Endpoint

```
POST http://192.168.253.45:8001/api/meal-cards/generate-with-check
```

**Request Body:**
```json
{
  "student_id": "{userId}",
  "meal_type": "lunch"
}
```

Where `{userId}` is the `userPin` from the verification (e.g., "1" for user AARON).

## How It Works

1. **Device sends verification** → Server receives attendance log
2. **Server saves log** → Log is stored in database
3. **External API called** → System automatically calls `http://192.168.253.45:8001/api/meal-cards/generate-with-check` with POST request containing `student_id` and `meal_type: "lunch"`
4. **Asynchronous processing** → API call doesn't block main processing

## Configuration

The external API URL and meal type are configured in:
- **File**: `src/com/zk/util/ExternalApiUtil.java`
- **Variables**: 
  - `EXTERNAL_API_URL = "http://192.168.253.45:8001/api/meal-cards/generate-with-check"`
  - `MEAL_TYPE = "lunch"`

To change the API URL or meal type, edit this file and recompile:

```java
private static final String EXTERNAL_API_URL = "http://192.168.253.45:8001/api/meal-cards/generate-with-check";
private static final String MEAL_TYPE = "lunch";
```

## Example Flow

When user "AARON" (User ID: "1") verifies:

1. Device sends verification log
2. Server saves log to database
3. System automatically calls:
   ```
   POST http://192.168.253.45:8001/api/meal-cards/generate-with-check
   ```
   With request body:
   ```json
   {
     "student_id": "1",
     "meal_type": "lunch"
   }
   ```

## Logging

All external API calls are logged:
- **Success**: Logged at INFO level with response details
- **Errors**: Logged at ERROR level with error details
- **Warnings**: Logged at WARN level for non-200 responses

Check Tomcat logs:
```bash
docker-compose logs -f tomcat | grep -i "external api\|notified external"
```

## Features

- ✅ **Asynchronous**: API calls don't block main processing
- ✅ **Error handling**: Failures are logged but don't affect verification processing
- ✅ **Timeout protection**: 5-second timeout for API calls
- ✅ **Thread pool**: Uses thread pool for efficient concurrent API calls

## Testing

1. **Perform a verification** on the device (scan fingerprint/face)
2. **Check Tomcat logs** for API call:
   ```bash
   docker-compose logs -f tomcat | grep -i "external api"
   ```
3. **Verify API was called** - You should see:
   ```
   INFO - Notified external API for verification - User ID: 1, Timestamp: 2025-11-08 09:10:13
   INFO - External API call successful for userId: 1, Response: {...}
   ```

## Customization

### Change API URL

Edit `src/com/zk/util/ExternalApiUtil.java`:
```java
private static final String EXTERNAL_API_URL = "http://your-api-server:port/api/endpoint";
```

### Change Meal Type

Edit `src/com/zk/util/ExternalApiUtil.java`:
```java
private static final String MEAL_TYPE = "breakfast"; // or "dinner", etc.
```

### Add Request Headers

Edit the `callExternalApi` method:
```java
connection.setRequestProperty("Authorization", "Bearer your-token");
connection.setRequestProperty("Custom-Header", "value");
```

### Change HTTP Method

Edit the `callExternalApi` method:
```java
connection.setRequestMethod("POST"); // or PUT, PATCH, etc.
```

### Send Request Body (for POST/PUT)

Edit the `callExternalApi` method to add request body handling.

## Troubleshooting

### API Not Being Called

1. **Check logs**:
   ```bash
   docker-compose logs tomcat | grep -i "external api"
   ```

2. **Verify verification is being saved**:
   ```bash
   docker-compose exec mysql mysql -uroot -proot pushdemo -e "SELECT * FROM att_log ORDER BY verify_time DESC LIMIT 5;"
   ```

3. **Check if userPin is present**:
   - API is only called if `userPin` is not null or empty

### API Call Failing

1. **Check external API is running**:
   ```bash
   curl -X POST http://192.168.253.45:8001/api/meal-cards/generate-with-check \
     -H "Content-Type: application/json" \
     -d '{"student_id":"1","meal_type":"lunch"}'
   ```

2. **Check network connectivity**:
   - Ensure server can reach `localhost:5000`
   - Check firewall settings

3. **Check timeout**:
   - Default timeout is 5 seconds
   - Increase if needed in `ExternalApiUtil.java`

### API Called Multiple Times

- This is normal if multiple verifications occur
- Each verification triggers a separate API call
- API calls are asynchronous and don't block each other

## Notes

- API calls are made **after** the log is successfully saved to database
- If database save fails, API is **not** called
- API call failures are logged but don't affect verification processing
- Multiple verifications can trigger concurrent API calls (handled by thread pool)

