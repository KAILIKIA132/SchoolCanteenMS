# How to Access MySQL Database on Windows Server

Follow these steps to log in to your MySQL database and check your data.

## Step 1: Open Command Prompt
1.  Press `Win + R` on your keyboard.
2.  Type `cmd` and press **Enter**.

## Step 2: Login to MySQL
Copy and paste the following command into your Command Prompt and press **Enter**:

```cmd
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -pCanteen@2026
```

*   **Note:** If you see "The system cannot find the path specified", check if MySQL is installed in a different location (e.g., `Program Files (x86)`).

## Step 3: Select the Database
Once you are logged in (you will see a `mysql>` prompt), type this command and press **Enter**:

```sql
USE pushdemo;
```

## Step 4: Check Your Data
You can now run SQL commands to see what is in your tables.

### Check Device Info
To see all devices:
```sql
SELECT * FROM device_info;
```

### Check Users
To see all users:
```sql
SELECT * FROM user_info;
```

### Check Attendance Logs
To see attendance records:
```sql
SELECT * FROM att_log;
```

### Count Records
To just see how many records are in each table:
```sql
SELECT COUNT(*) FROM device_info;
SELECT COUNT(*) FROM user_info;
SELECT COUNT(*) FROM att_log;
```

## Step 5: Exit
To exit MySQL, type:
```sql
exit
```
