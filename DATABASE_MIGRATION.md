# Database Migration Guide: Mac (Local) -> Windows Server

Follow these steps to replace your Windows Server database with your local Mac database.

## Phase 1: On Your Mac (Export Local Data)

1.  Open your **Terminal** on Mac.
2.  Run the following command to export your local database to a file named `local_backup.sql`.
    *   *Note: You may need to enter your local database password.*

```bash
/usr/local/bin/mysqldump -h 127.0.0.1 -P 3308 -u root -proot pushdemo > local_backup.sql
```

> **Note:** Your database is running in Docker on port **3308** with password `root`.


3.  Verify that `local_backup.sql` has been created in your current folder.

## Phase 2: Transfer File

1.  Copy the `local_backup.sql` file from your Mac to your Windows Server.
2.  Place it in the **same folder** where you have your project files (e.g., `C:\Meal_Management\SchoolCanteenMS` or wherever `setup-migration.bat` is located).

## Phase 3: On Windows Server (Import Data)

1.  Open the folder containing your project files on Windows.
2.  Double-click the **`setup-migration.bat`** file I created for you.
3.  Type `Y` and press Enter when prompted.

**What this script does:**
*   It **DROPS** (deletes) the current `pushdemo` database on Windows.
*   It **CREATES** a fresh, empty `pushdemo` database.
*   It **IMPORTS** all tables and data from your `local_backup.sql`.

## Phase 4: Verify

1.  After the script finishes, refresh your browser.
2.  Your device list and user data should now match exactly what you have on your Mac.
