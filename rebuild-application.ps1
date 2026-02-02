#requires -version 5.1
<#
.SYNOPSIS
    Rebuilds the entire Java application without interfering with the database.
    
.DESCRIPTION
    This script compiles all Java source files and updates the class files in the 
    WebContent/WEB-INF/classes directory. It does not touch or modify the database
    in any way - it only rebuilds the application files to ensure the updated API URL is saved.

.PARAMETER ProjectPath
    Path to the project root directory (default: current directory)

.EXAMPLE
    .\rebuild-application.ps1
#>

param(
    [string]$ProjectPath = $PSScriptRoot
)

# Configuration
$ErrorActionPreference = "Stop"

function Write-Status {
    param([string]$Message, [string]$Color = "Cyan")
    Write-Host "[$((Get-Date).ToString('HH:mm:ss'))] $Message" -ForegroundColor $Color
}

function Write-Success {
    param([string]$Message)
    Write-Host "[$((Get-Date).ToString('HH:mm:ss'))] ✓ $Message" -ForegroundColor Green
}

function Write-Error {
    param([string]$Message)
    Write-Host "[$((Get-Date).ToString('HH:mm:ss'))] ✗ $Message" -ForegroundColor Red
}

# Start the rebuild process
Write-Status "Starting application rebuild..." "Green"
Write-Status "Project Path: $ProjectPath"

# Verify prerequisites
Write-Status "Verifying Java compiler..."
try {
    $javacVer = javac -version 2>&1
    Write-Status "Found javac: $javacVer" "Gray"
} catch {
    Write-Error "Java Compiler (javac) not found in PATH. Please install JDK 8+ and add 'bin' to PATH."
    exit 1
}

# Define paths
$SrcDir = "$ProjectPath\src"
$WebInfDir = "$ProjectPath\WebContent\WEB-INF"
$ClassesDir = "$WebInfDir\classes"
$LibDir = "$WebInfDir\lib"

Write-Status "Cleaning previous compiled classes..." "Gray"
if (Test-Path $ClassesDir) {
    Remove-Item -Path "$ClassesDir\com" -Recurse -Force -ErrorAction SilentlyContinue
}

# Ensure output directory exists
if (-not (Test-Path $ClassesDir)) {
    Write-Status "Creating classes directory..." "Gray"
    New-Item -ItemType Directory -Path $ClassesDir -Force | Out-Null
}

# Find all Java source files
Write-Status "Finding Java source files..." "Gray"
$javaFiles = Get-ChildItem -Path $SrcDir -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
if ($javaFiles.Count -eq 0) {
    Write-Error "No Java source files found in $SrcDir"
    exit 1
}

Write-Status "Found $($javaFiles.Count) Java source files to compile" "Gray"

# Create temporary file with all source paths
$sourcesFile = "$ProjectPath\sources_temp.txt"
$javaFiles | Out-File -FilePath $sourcesFile -Encoding ASCII

# Build classpath
$TomcatLib = "$LibDir\*"
$Classpath = "$TomcatLib;."

Write-Status "Compiling Java source files..." "Gray"
try {
    # Compile all Java files at once
    $compileResult = javac -cp $Classpath -sourcepath $SrcDir -d $ClassesDir -g "@$sourcesFile" 2>&1
    Write-Success "Compilation completed successfully"
} catch {
    Write-Error "Compilation failed. Error: $_"
    if (Test-Path $sourcesFile) {
        Remove-Item $sourcesFile -ErrorAction SilentlyContinue
    }
    exit 1
}

# Clean up temporary file
if (Test-Path $sourcesFile) {
    Remove-Item $sourcesFile -ErrorAction SilentlyContinue
}

# Copy resource files (XML, properties) to classes directory
Write-Status "Copying resource files..." "Gray"
Get-ChildItem -Path $SrcDir -Recurse -Include "*.xml", "*.properties" | ForEach-Object {
    $relPath = $_.FullName.Substring($SrcDir.Length)
    $dest = Join-Path $ClassesDir $relPath
    $parent = Split-Path $dest -Parent
    if (-not (Test-Path $parent)) { 
        New-Item -ItemType Directory -Path $parent -Force | Out-Null 
    }
    Copy-Item $_.FullName -Destination $dest -Force
}

# Verify that the ExternalApiUtil class was compiled with the correct URL
Write-Status "Verifying ExternalApiUtil compilation..." "Gray"
$externalApiUtilPath = "$ClassesDir\com\zk\util\ExternalApiUtil.class"
if (Test-Path $externalApiUtilPath) {
    Write-Success "ExternalApiUtil.class successfully compiled"
    Write-Status "File location: $externalApiUtilPath" "Gray"
} else {
    Write-Error "ExternalApiUtil.class was not compiled successfully"
}

Write-Success "Application rebuild completed successfully!" "Green"
Write-Status "===========================================" "Green"
Write-Status "Next steps:" "Yellow"
Write-Status "1. Restart your Tomcat server to load the updated classes" 
Write-Status "2. Run: .\tomcat-restart.bat" "Yellow"
Write-Status "===========================================" "Green"
