# User Management Features

## Overview
The School Canteen Management System provides comprehensive user management functionality including adding, editing, and deleting users.

## User Deletion Functionality

### Delete User from Server (Database)
- **Action**: `userAction!deleteUserServ.action`
- **Purpose**: Permanently removes user from the server database
- **What gets deleted**: 
  - User basic information
  - Biometric templates (fingerprint, face, palm)
- **Usage**: Select users in user list and choose "Delete User" from the menu

### Delete User from Device
- **Action**: `userAction!deleteUserDev.action`
- **Purpose**: Sends command to remove user from connected device
- **What happens**: Device receives command to delete user data locally

### Partial Deletion Options
The system also supports selective deletion of user data:

| Action | Purpose |
|--------|---------|
| `userAction!deleteUserFpServ.action` | Delete user's fingerprints from server |
| `userAction!deleteUserFaceServ.action` | Delete user's face templates from server |
| `userAction!deleteUserPlamServ.action` | Delete user's palm templates from server |
| `userAction!deleteUserPicServ.action` | Delete user's photos from server |
| `userAction!deleteUserFpDev.action` | Delete user's fingerprints from device |
| `userAction!deleteUserFaceDev.action` | Delete user's face templates from device |
| `userAction!deleteUserPicDev.action` | Delete user's photos from device |

## How to Delete Users

### Via Web Interface
1. Navigate to **User List** page
2. Select checkbox(es) next to user(s) you want to delete
3. Click **Operations** menu
4. Select **Server** → **Delete User** to remove from database
5. Confirm the operation when prompted

### Via API
Send a POST request to:
```
userAction!deleteUserServ.action?userId=1,2,3
```
Where `userId` contains comma-separated user IDs to delete.

## Technical Implementation

### Backend Methods
- `UserInfoManager.deleteUserInfo(String[] userIds)` - Main deletion method
- `UserInfoDao.delete(String cond)` - Database deletion
- `PersonBioTemplateDao.delete(String cond)` - Biometric template deletion

### Frontend JavaScript
Located in `userList.jsp`, the `operateUser()` function handles the deletion operations.

## Security Considerations
- Only authenticated administrators can delete users
- Deletion is permanent and cannot be undone
- Consider backing up user data before deletion

## Best Practices
1. Always verify which users you're about to delete
2. Test deletion functionality in a development environment first
3. Maintain user access logs for audit purposes
4. Regularly backup the database before major user management operations
5. Note that a confirmation dialog will appear before deleting users to prevent accidental deletions