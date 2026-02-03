# filepath: /school-canteen-ms-deploy/school-canteen-ms-deploy/config/deploy.config.ps1

param(
    [string]$TomcatHome = "C:\Path\To\Tomcat",
    [string]$MySQLRootPassword = "your_password_here",
    [string]$ProjectPath = "C:\Path\To\Project",
    [string]$DatabaseName = "pushdemo",
    [string]$MySqlExePath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
)

$Config = @{
    TomcatHome        = $TomcatHome
    MySQLRootPassword = $MySQLRootPassword
    ProjectPath       = $ProjectPath
    DatabaseName      = $DatabaseName
    MySqlExePath      = $MySqlExePath
}

$Config | ConvertTo-Json | Set-Content -Path "$ProjectPath\config\deploy.config.json" -Encoding UTF8

Write-Host "Deployment configuration has been set." -ForegroundColor Green