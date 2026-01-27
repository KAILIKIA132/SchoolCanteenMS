# Verification Response JSON API

## Endpoint

```
GET http://localhost:8080/attAction!getVerificationsJson.action
```

Returns real-time biometric verification responses in JSON format. This endpoint is designed for consumption by external applications.

## Base URL

```
http://localhost:8080/attAction!getVerificationsJson.action
```

## Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `limit` | integer | No | 100 | Number of records to return (max: 1000) |
| `deviceSn` | string | No | - | Filter by device serial number |
| `userPin` | string | No | - | Filter by user PIN |
| `since` | string | No | - | Return only records after this timestamp (format: `yyyy-MM-dd HH:mm:ss`) |

## Response Format

### Success Response

```json
{
  "success": true,
  "count": 5,
  "timestamp": "Fri Nov 07 12:00:00 UTC 2025",
  "verifications": [
    {
      "id": 123,
      "timestamp": "2025-11-07 15:51:00",
      "deviceSn": "TDBD250100333",
      "userPin": "1",
      "userName": "AARON",
      "verifyType": 15,
      "verifyTypeStr": "Face",
      "status": 0,
      "statusStr": "Check In",
      "mask": 0,
      "temperature": "36.5",
      "workCode": 0,
      "sensorNo": 0,
      "palm": 0
    }
  ]
}
```

### Error Response

```json
{
  "success": false,
  "error": "Error message here",
  "timestamp": "Fri Nov 07 12:00:00 UTC 2025"
}
```

## Response Fields

### Verification Object

| Field | Type | Description |
|-------|------|-------------|
| `id` | integer | Attendance log ID |
| `timestamp` | string | Verification timestamp (format: `yyyy-MM-dd HH:mm:ss`) |
| `deviceSn` | string | Device serial number |
| `userPin` | string | User PIN/ID |
| `userName` | string | User name |
| `verifyType` | integer | Verification type code (15 = Face, 1 = Fingerprint, etc.) |
| `verifyTypeStr` | string | Verification type description |
| `status` | integer | Status code (0 = Check In, 1 = Check Out, etc.) |
| `statusStr` | string | Status description |
| `mask` | integer | Mask flag (0 = No mask, 1 = Mask detected) |
| `temperature` | string | Temperature reading (if available) |
| `workCode` | integer | Work code |
| `sensorNo` | integer | Sensor number |
| `palm` | integer | Palm verification flag |

## Examples

### Get Latest 50 Verifications

```bash
GET http://localhost:8080/attAction!getVerificationsJson.action?limit=50
```

### Filter by Device

```bash
GET http://localhost:8080/attAction!getVerificationsJson.action?deviceSn=TDBD250100333&limit=100
```

### Filter by User

```bash
GET http://localhost:8080/attAction!getVerificationsJson.action?userPin=1&limit=50
```

### Get Verifications Since Timestamp

```bash
GET http://localhost:8080/attAction!getVerificationsJson.action?since=2025-11-07 10:00:00
```

### Combined Filters

```bash
GET http://localhost:8080/attAction!getVerificationsJson.action?deviceSn=TDBD250100333&limit=20&since=2025-11-07 10:00:00
```

## CORS Headers

The API includes CORS headers to allow cross-origin requests:

- `Access-Control-Allow-Origin: *`
- `Access-Control-Allow-Methods: GET, POST, OPTIONS`
- `Access-Control-Allow-Headers: Content-Type`

## Usage Examples

### JavaScript (Fetch API)

```javascript
fetch('http://localhost:8080/attAction!getVerificationsJson.action?limit=50')
  .then(response => response.json())
  .then(data => {
    if (data.success) {
      console.log(`Found ${data.count} verifications`);
      data.verifications.forEach(verification => {
        console.log(`${verification.userName} verified at ${verification.timestamp}`);
      });
    }
  })
  .catch(error => console.error('Error:', error));
```

### JavaScript (Polling for Real-time Updates)

```javascript
let lastTimestamp = null;

function pollVerifications() {
  let url = 'http://localhost:8080/attAction!getVerificationsJson.action?limit=100';
  if (lastTimestamp) {
    url += `&since=${lastTimestamp}`;
  }
  
  fetch(url)
    .then(response => response.json())
    .then(data => {
      if (data.success && data.verifications.length > 0) {
        data.verifications.forEach(verification => {
          console.log('New verification:', verification);
          // Process verification
        });
        // Update last timestamp
        lastTimestamp = data.verifications[0].timestamp;
      }
    })
    .catch(error => console.error('Error:', error));
}

// Poll every 3 seconds
setInterval(pollVerifications, 3000);
```

### cURL

```bash
# Get latest 50 verifications
curl "http://localhost:8080/attAction!getVerificationsJson.action?limit=50"

# Filter by device
curl "http://localhost:8080/attAction!getVerificationsJson.action?deviceSn=TDBD250100333"

# Get verifications since timestamp
curl "http://localhost:8080/attAction!getVerificationsJson.action?since=2025-11-07%2010:00:00"
```

### Python

```python
import requests
import json

url = "http://localhost:8080/attAction!getVerificationsJson.action"
params = {
    "limit": 50,
    "deviceSn": "TDBD250100333"
}

response = requests.get(url, params=params)
data = response.json()

if data["success"]:
    print(f"Found {data['count']} verifications")
    for verification in data["verifications"]:
        print(f"{verification['userName']} verified at {verification['timestamp']}")
```

## Notes

- Results are returned in reverse chronological order (newest first)
- Maximum limit is 1000 records per request
- The `since` parameter filters records where `verify_time > since`
- All timestamps are in the format `yyyy-MM-dd HH:mm:ss`
- The API returns empty array if no verifications match the criteria

## Error Handling

Always check the `success` field in the response:

```javascript
if (data.success) {
  // Process verifications
} else {
  console.error('API Error:', data.error);
}
```










