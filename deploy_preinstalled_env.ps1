# #requires -version 5.1
<#
.SYNOPSIS
    Compiles only the Java part of the Push Demo / Struts 2 web application.

.DESCRIPTION
    This script compiles Java source files and copies resources to WEB-INF/classes.
    It does NOT deploy to Tomcat, set up DB, configure files, or handle Python parts.

    Requirements:
    - JDK (javac) in PATH
    - Project structure with src/ (Java sources) and WebContent/WEB-INF/ (lib/, classes/)

.PARAMETER ProjectPath
    Path to the project root (contains src/ and WebContent/). Default: current directory.

.EXAMPLE
    .\compile-java-only.ps1 -ProjectPath "C:\Projects\pushdemo"
#>

param(
    [string]$ProjectPath = $PSScriptRoot
)

# --- Configuration ---
$ErrorActionPreference = "Stop"

function Print-Msg {
    param(
        [string]$msg,
        [string]$color = "Cyan"
    )
    Write-Host -ForegroundColor $color "[$((Get-Date).ToString('HH:mm:ss'))] $msg"
}

Print-Msg "Starting Java-only compilation..." "Green"
Print-Msg "Project Path: $ProjectPath" "Yellow"

# 1. Verify Prerequisites
Print-Msg "Step 1: Verifying environment..." "Cyan"

try {
    $javacVer = javac -version 2>&1
    Print-Msg "Found javac: $javacVer" "Gray"
} catch {
    Write-Error "Java compiler (javac) not found in PATH. Install JDK (e.g. JDK 8) and add bin/ to PATH."
}

# 2. Define paths
$SrcDir    = Join-Path $ProjectPath "src"
$WebInfDir = Join-Path $ProjectPath "WebContent\WEB-INF"
$ClassesDir = Join-Path $WebInfDir "classes"
$LibDir    = Join-Path $WebInfDir "lib"

# Ensure output directory exists
if (-not (Test-Path $ClassesDir)) {
    New-Item -ItemType Directory -Path $ClassesDir -Force | Out-Null
    Print-Msg "Created output directory: $ClassesDir" "Gray"
}

# 3. Find all .java files
Print-Msg "Step 2: Locating Java source files..." "Cyan"

$javaFiles = Get-ChildItem -Path $SrcDir -Recurse -File -Filter "*.java" -ErrorAction SilentlyContinue

if ($javaFiles.Count -eq 0) {
    Write-Warning "No .java files found in $SrcDir"
    Print-Msg "Nothing to compile. Exiting." "Yellow"
    exit 0
}

Print-Msg "Found $($javaFiles.Count) Java source file(s)" "Gray"

# Write file list for @sources.txt (javac response file)
$sourcesList = Join-Path $ProjectPath "sources.txt"
$javaFiles.FullName | Out-File -FilePath $sourcesList -Encoding ascii

# 4. Build classpath
# Include project libs + Tomcat servlet-api.jar, jsp-api.jar, etc.
$TomcatHomeFromEnv = $env:TOMCAT_HOME
if (-not $TomcatHomeFromEnv -or -not (Test-Path $TomcatHomeFromEnv)) {
    Write-Warning "TOMCAT_HOME environment variable not set or invalid. Compilation may fail if servlet-api is missing."
    $TomcatLib = ""
} else {
    $TomcatLib = Join-Path $TomcatHomeFromEnv "lib\*"
}

$ProjectLib = "$LibDir\*"
$ClasspathParts = @()
if ($TomcatLib)   { $ClasspathParts += $TomcatLib }
if ($ProjectLib)  { $ClasspathParts += $ProjectLib }

$Classpath = $ClasspathParts -join ";"

if ($Classpath -eq "") {
    Print-Msg "No external libraries found → compiling without classpath" "Yellow"
} else {
    Print-Msg "Classpath includes: Tomcat lib + project WEB-INF/lib" "Gray"
}

# 5. Compile
Print-Msg "Step 3: Compiling $($javaFiles.Count) source file(s)..." "Cyan"

try {
    # Use response file to avoid command-line length limit
    javac -cp $Classpath -d $ClassesDir "@$sourcesList"
    Print-Msg "Compilation successful!" "Green"
} catch {
    Write-Error "Compilation failed. Check javac output above for details (missing jars, syntax errors, etc.)."
} finally {
    Remove-Item $sourcesList -Force -ErrorAction SilentlyContinue
}

# 6. Copy non-.java resources from src/ to classes/ (struts.xml, properties, etc.)
Print-Msg "Step 4: Copying resources (xml, properties, ...) to classes..." "Cyan"

$resourceFiles = Get-ChildItem -Path $SrcDir -Recurse -File |
    Where-Object { $_.Extension -in @(".xml", ".properties", ".tld") }  # Add more extensions if needed

foreach ($res in $resourceFiles) {
    $relPath = $res.FullName.Substring($SrcDir.Length).TrimStart('\','/')
    $destPath = Join-Path $ClassesDir $relPath
    $destDir = Split-Path $destPath -Parent

    if (-not (Test-Path $destDir)) {
        New-Item -ItemType Directory -Path $destDir -Force | Out-Null
    }

    Copy-Item $res.FullName -Destination $destPath -Force
}

Print-Msg "Copied $($resourceFiles.Count) resource file(s)" "Gray"

# Final message
Print-Msg "Java compilation complete!" "Green"
Print-Msg "Compiled classes are in: $ClassesDir"
Print-Msg "You can now manually copy WebContent/* to Tomcat's webapps/ROOT/ or webapps/pushdemo/"
Print-Msg "Or create a .war manually (e.g. using Compress-Archive or jar command)."