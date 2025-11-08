# How to Sync Users to ZK Device and Edit Them

## Overview

After importing users via bulk import, they are stored in the database. To see and edit them on your ZK device, you need to **sync them to the device**. This guide explains how to do that.

---

## Step 1: Send Users to Device

### Method 1: Send Selected Users to Their Associated Device

1. **Go to User List Page**
   - Navigate to: `http://localhost:8080/userAction!userList.action`
   - Or click on **Users** in the navigation menu

2. **Select Users to Send**
   - Check the checkbox next to each user you want to send to the device
   - You can select multiple users at once
   - Or use the "Select All" checkbox at the top to select all users

3. **Send Users to Device**
   - Click on **User Operations** → **Data Cmd** → **Send User to Device**
   - This will send the selected users to their associated device (based on the `deviceSn` field in the CSV)

### Method 2: Send Users to a Specific Device

1. **Go to User List Page**
   - Navigate to: `http://localhost:8080/userAction!userList.action`

2. **Select Users**
   - Check the checkbox next to each user you want to send

3. **Send to Specific Device**
   - Click on **User Operations** → **Data Cmd** → **Move User Info to New Device**
   - A dialog will appear showing available devices
   - Select the device(s) you want to send users to
   - Click OK

---

## Step 2: View Users on ZK Device

After sending users to the device, they will appear on your ZK device:

1. **On Your ZK Device:**
   - Navigate to **User Management** or **User List** menu
   - You should see all the users that were sent to the device
   - Users imported via bulk import will appear without images (as expected)

2. **Verify Users Are on Device:**
   - Check the user count on the device
   - Users should be listed with their PIN, Name, and Card Number
   - Images will be empty (you'll add them next)

---

## Step 3: Edit Users and Add Images on Device

### Option A: Edit Individual Users on Device

1. **On Your ZK Device:**
   - Go to **User Management** → **User List**
   - Select a user you want to edit
   - Choose **Edit** or **Modify**

2. **Add User Image:**
   - Select **Photo** or **Image** option
   - Take a photo using the device camera, or
   - Upload an image if the device supports it
   - Save the changes

3. **Edit Other User Information:**
   - You can also modify:
     - User Name
     - Card Number
     - Password
     - Privilege Level
     - Category
   - Save all changes

### Option B: Bulk Edit on Device (if supported)

Some ZK devices support bulk editing:
- Select multiple users
- Apply changes to all selected users
- Add images in batch (if supported)

---

## Step 4: Sync Changes Back to Server (Optional)

If you edit users on the device, you may want to sync those changes back to the server:

1. **On the Device:**
   - After making changes, the device will automatically sync with the server on the next connection
   - Or manually trigger a sync if your device supports it

2. **Verify on Web Interface:**
   - Go back to the User List page
   - Refresh the page
   - Users with images added on the device should now show images in the web interface

---

## Quick Reference: User List Page Actions

### User Operations Menu:

**Server Operations:**
- Delete User (Server) - Removes user from database only
- Delete User Face (Server) - Removes face templates from database
- Delete User Fingerprint (Server) - Removes fingerprints from database
- Delete User Photo (Server) - Removes photos from database

**Data Commands:**
- **Send User to Device** ⭐ - Sends selected users to their associated device
- **Move User Info to New Device** ⭐ - Sends users to a specific device
- Delete User (Device) - Removes user from device
- Delete User Fingerprint (Device) - Removes fingerprints from device
- Delete User Photo (Device) - Removes photos from device
- Delete User Face (Device) - Removes face templates from device

---

## Troubleshooting

### Users Not Appearing on Device

1. **Check Device Connection:**
   - Verify device is connected to the server
   - Check device status in Device List page
   - Ensure device can reach the server

2. **Check User's Device Assignment:**
   - In User List, verify the `deviceSn` column matches your device serial number
   - Users are only sent to their assigned device

3. **Check Command Status:**
   - Go to **Device Commands** page
   - Look for "DATA UPDATE" commands
   - Verify commands are being executed

4. **Resend Users:**
   - Try sending users again
   - Use "Move User Info to New Device" to send to a specific device

### Images Not Showing

1. **Verify Image Format:**
   - ZK devices typically support JPG/JPEG format
   - Image size should be reasonable (usually under 1MB)

2. **Check Image on Device:**
   - Verify the image was actually saved on the device
   - Some devices require specific image dimensions

3. **Sync Back to Server:**
   - After adding images on device, wait for automatic sync
   - Or manually trigger sync if available

---

## Best Practices

1. **Import Users First:**
   - Use bulk import to add all users to the database
   - Verify users appear in the User List page

2. **Send to Device:**
   - Send users to device after import
   - Use "Select All" to send all users at once

3. **Add Images on Device:**
   - Edit users directly on the device
   - This is the most reliable way to add images

4. **Verify Sync:**
   - Check that users appear on the device
   - Verify images sync back to the server

---

## Summary

**To see imported users on your ZK device:**

1. ✅ Import users via bulk import (already done)
2. ✅ Go to User List page: `http://localhost:8080/userAction!userList.action`
3. ✅ Select users (or select all)
4. ✅ Click **User Operations** → **Data Cmd** → **Send User to Device**
5. ✅ Users will appear on your ZK device
6. ✅ Edit users on the device to add images
7. ✅ Changes will sync back to the server automatically

---

**Note:** Users imported via bulk import don't have images initially. You add images directly on the ZK device, which is the standard workflow for ZK devices.

