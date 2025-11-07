# Verification Responses JSON API

## Endpoint

```
GET http://localhost:8080/apiVerification!getVerificationsJson.action
```

## Description

Returns real-time biometric verification responses in JSON format. This endpoint is designed for consumption by external applications.

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
  "timestamp": "Fri Nov 07 11:15:30 UTC 2025",
  "verifications": [
    {
      "id": 123,
      "timestamp": "2025-11-07 11:15:25",
      "deviceSn": "TDBD250100333",
      "userPin": "12345",
      "userName": "John Doe",
      "verifyType": 1,
      "verifyTypeStr": "Fingerprint",
      "status": 0,
      "statusStr": "Check In",
      "mask": 1,
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
  "timestamp": "Fri Nov 07 11:15:30 UTC 2025"
}
```

## Field Descriptions

| Field | Type | Description |
|-------|------|-------------|
| `id` | integer | Attendance log ID |
| `timestamp` | string | Verification timestamp (format: `yyyy-MM-dd HH:mm:ss`) |
| `deviceSn` | string | Device serial number |
| `userPin` | string | User PIN/ID |
| `userName` | string | User name (if available) |
| `verifyType` | integer | Verification type code (1=Fingerprint, 2=Face, etc.) |
| `verifyTypeStr` | string | Verification type description |
| `status` | integer | Attendance status code (0=Check In, 1=Check Out, etc.) |
| `statusStr` | string | Attendance status description |
| `mask` | integer | Mask detection flag (0=No, 1=Yes) |
| `temperature` | string | Temperature reading (if available) |
| `workCode` | integer | Work code |
| `sensorNo` | integer | Sensor number |
| `palm` | integer | Palm detection flag (0=No, 1=Yes) |

## Example Requests

### Get last 50 verifications
```
GET http://localhost:8080/apiVerification!getVerificationsJson.action?limit=50
```

### Get verifications from specific device
```
GET http://localhost:8080/apiVerification!getVerificationsJson.action?deviceSn=TDBD250100333&limit=100
```

### Get verifications for specific user
```
GET http://localhost:8080/apiVerification!getVerificationsJson.action?userPin=12345&limit=50
```

### Get verifications since a specific time
```
GET http://localhost:8080/apiVerification!getVerificationsJson.action?since=2025-11-07 10:00:00
```

### Combined filters
```
GET http://localhost:8080/apiVerification!getVerificationsJson.action?deviceSn=TDBD250100333&limit=20&since=2025-11-07 10:00:00
```

## CORS Support

The API includes CORS headers to allow cross-origin requests:
- `Access-Control-Allow-Origin: *`
- `Access-Control-Allow-Methods: GET, POST, OPTIONS`
- `Access-Control-Allow-Headers: Content-Type`

## Real-time Usage

For real-time monitoring, external applications should poll this endpoint periodically (e.g., every 3-5 seconds) and use the `since` parameter to only retrieve new records:

1. First request: Get initial data
2. Store the latest `timestamp` from the response
3. Subsequent requests: Use `since=<last_timestamp>` to get only new records
4. Repeat polling with updated timestamp

## Response Headers

- `Content-Type: application/json; charset=utf-8`
- `Cache-Control: no-cache`
- `Access-Control-Allow-Origin: *`

## Notes

- Results are ordered by timestamp (newest first)
- Maximum limit is 1000 records per request
- Timestamp format must match: `yyyy-MM-dd HH:mm:ss`
- Empty arrays are returned if no records match the filters


