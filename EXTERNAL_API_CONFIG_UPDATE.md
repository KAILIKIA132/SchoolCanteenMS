# External API Configuration Update

## Overview
The external API URL is now configurable through the `config.xml` file instead of being hardcoded in the Java source code.

## Changes Made

### 1. Updated `config.xml`
Added new `<externalapi>` section:
```xml
<externalapi>
    <url>http://10.35.200.1:8003/api/meal-cards/generate-with-check</url>
</externalapi>
```

### 2. Modified `ExternalApiUtil.java`
- Removed hardcoded `EXTERNAL_API_URL` constant
- Added `loadExternalApiUrl()` method to read URL from config.xml
- Added fallback mechanism to default URL if config reading fails
- Added proper XML parsing with error handling

### 3. Updated `deploy_preinstalled_env.ps1`
- Enhanced configuration step to handle external API URL
- Added logging for external API URL configuration
- Maintained backward compatibility

## How to Customize the External API URL

### Method 1: Edit config.xml directly
Edit `WebContent/WEB-INF/classes/config.xml`:
```xml
<externalapi>
    <url>http://your-server:port/api/endpoint</url>
</externalapi>
```

### Method 2: Using deployment script
The deployment script will automatically configure the URL. You can modify the script to set a custom URL by changing:
```powershell
$xml.root.externalapi.url = "http://your-server:port/api/endpoint"
```

## Verification
Run the deployment script - it will now:
1. Compile the Java source code (including the updated ExternalApiUtil)
2. Update database connection settings
3. Configure the external API URL from config.xml
4. Deploy to Tomcat

## Benefits
- ✅ No recompilation needed to change API URL
- ✅ Configuration centralized in config.xml
- ✅ Environment-specific configurations possible
- ✅ Better maintainability
- ✅ Easier deployment across different environments

## Troubleshooting

### Issue: Application still uses old URL after deployment

**Problem**: The application continues to use `http://host.docker.internal:8002/api/meal-cards/generate-with-check` instead of the configured URL.

**Cause**: Multiple config.xml files exist in different locations, and the application might be reading from `build/classes/config.xml` instead of `WebContent/WEB-INF/classes/config.xml`.

**Solution**:
1. Ensure ALL config.xml files are updated:
   ```bash
   # Check all config locations
   find . -name "config.xml" -type f
   
   # Update build/classes/config.xml if it exists
   cp src/config.xml build/classes/
   ```

2. Recompile the ExternalApiUtil class:
   ```bash
   javac -cp "WebContent/WEB-INF/lib/*:WebContent/WEB-INF/classes" -d build/classes src/com/zk/util/ExternalApiUtil.java
   ```

3. Redeploy the application

### Verification
Run the diagnostic tool to check which config file is being used:
```bash
java DiagnosticConfigReader  # (if available)
```

The application should now use the URL from the configuration file instead of the hardcoded value.