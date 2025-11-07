# External API cURL Command

## Exact cURL Command Used by the System

The system makes the following HTTP request when a verification occurs:

```bash
curl -X POST http://192.168.253.45:8001/api/meal-cards/generate-with-check \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"student_id":"{userId}","meal_type":"lunch"}'
```

## Example with Actual User ID

For user ID "1" (e.g., AARON), it will be formatted as "001":

```bash
curl -X POST http://192.168.253.45:8001/api/meal-cards/generate-with-check \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"student_id":"001","meal_type":"lunch"}'
```

## Single Line Version

```bash
curl -X POST http://192.168.253.45:8001/api/meal-cards/generate-with-check -H "Content-Type: application/json" -H "Accept: application/json" -d '{"student_id":"001","meal_type":"lunch"}'
```

## Request Details

- **Method**: POST
- **URL**: `http://192.168.253.45:8001/api/meal-cards/generate-with-check`
- **Headers**:
  - `Content-Type: application/json`
  - `Accept: application/json`
- **Body**:
  ```json
  {
    "student_id": "{formattedUserId}",
    "meal_type": "lunch"
  }
  ```
  
  **Note**: The `student_id` is automatically formatted with leading zeros to 3 digits:
  - User ID "1" → "001"
  - User ID "2" → "002"
  - User ID "1009" → "1009" (if already 4+ digits, uses as-is)

## Test the API

You can test the API manually using the same curl command:

```bash
# Test with user ID "1" (formatted as "001")
curl -X POST http://192.168.253.45:8001/api/meal-cards/generate-with-check \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"student_id":"001","meal_type":"lunch"}'

# Test with user ID "2" (formatted as "002")
curl -X POST http://192.168.253.45:8001/api/meal-cards/generate-with-check \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"student_id":"002","meal_type":"lunch"}'

# Test with user ID "1009" (kept as-is if 4+ digits)
curl -X POST http://192.168.253.45:8001/api/meal-cards/generate-with-check \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"student_id":"1009","meal_type":"lunch"}'
```

## What Happens Automatically

When a user verifies on the device:
1. The system receives the verification log
2. The system saves it to the database
3. The system automatically executes the curl command above with the user's ID (`userPin`)

## View in Logs

To see the actual API calls in the logs:

```bash
docker-compose logs -f tomcat | grep -i "external api"
```

You'll see entries like:
```
INFO - External API call successful for student_id: 1, Response Code: 200, Response: {...}
```

