#requires -version 5.1
<#
.SYNOPSIS
  Rebuild compiled classes (and optional WAR) for School Canteen MS on Windows.

.DESCRIPTION
  - Cleans WebContent\WEB-INF\classes\com
  - Compiles all src\**\*.java with Java 8 target
  - Copies *.xml / *.properties from src into classes
  - Optionally packages SchoolCanteenMS.war

  Does NOT touch MySQL. Safe for live data (app rebuild only).

.EXAMPLE
  .\rebuild.ps1

.EXAMPLE
  .\rebuild.ps1 -SkipWar

.EXAMPLE
  .\rebuild.ps1 -DeployTo "C:\apache-tomcat-9.0.84\webapps\pushdemo.war"
#>

param(
    [switch]$SkipWar,
    [string]$DeployTo = ""
)

$ErrorActionPreference = "Stop"

function Write-Step($msg) {
    Write-Host "[$((Get-Date).ToString('HH:mm:ss'))] $msg" -ForegroundColor Cyan
}

function Write-Ok($msg) {
    Write-Host "[$((Get-Date).ToString('HH:mm:ss'))] $msg" -ForegroundColor Green
}

function Write-Fail($msg) {
    Write-Host "[$((Get-Date).ToString('HH:mm:ss'))] $msg" -ForegroundColor Red
}

# Run from script directory (project root)
$Project = if ($PSScriptRoot) { $PSScriptRoot } else { (Get-Location).Path }
Set-Location $Project

$Src     = Join-Path $Project "src"
$WebInf  = Join-Path $Project "WebContent\WEB-INF"
$Classes = Join-Path $WebInf "classes"
$Lib     = Join-Path $WebInf "lib"
$WarFile = Join-Path $Project "SchoolCanteenMS.war"
$Sources = Join-Path $Project "sources.txt"

Write-Step "Project: $Project"

# --- Preconditions ---
if (-not (Test-Path $Src)) {
    throw "src folder not found. Run this from the SchoolCanteenMS project root."
}
if (-not (Test-Path $Lib)) {
    throw "WebContent\WEB-INF\lib not found."
}

$javac = Get-Command javac -ErrorAction SilentlyContinue
if (-not $javac) {
    throw "javac not found on PATH. Install JDK 8+ and add it to PATH."
}
Write-Step "Using javac: $($javac.Source)"
& javac -version 2>&1 | ForEach-Object { Write-Host "  $_" }

# --- Clean ---
Write-Step "Cleaning old classes under WEB-INF\classes\com ..."
$comDir = Join-Path $Classes "com"
if (Test-Path $comDir) {
    Remove-Item $comDir -Recurse -Force
}
New-Item -ItemType Directory -Force -Path $Classes | Out-Null

# --- Classpath ---
$jarFiles = @(Get-ChildItem -Path $Lib -Filter "*.jar" -ErrorAction SilentlyContinue)
if ($jarFiles.Count -eq 0) {
    throw "No JAR files in $Lib"
}
$cp = ($jarFiles | ForEach-Object { $_.FullName }) -join ";"
$cp = "$cp;$Src"

# --- Source list ---
Write-Step "Collecting Java sources..."
$javaFiles = @(Get-ChildItem -Path $Src -Recurse -Filter "*.java")
if ($javaFiles.Count -eq 0) {
    throw "No .java files under src"
}
$javaFiles | ForEach-Object { $_.FullName } | Set-Content -Path $Sources -Encoding ascii
Write-Step "Compiling $($javaFiles.Count) source file(s)..."

# --- Compile ---
& javac -source 1.8 -target 1.8 -encoding UTF-8 -cp $cp -d $Classes "@$Sources"
if ($LASTEXITCODE -ne 0) {
    Remove-Item $Sources -Force -ErrorAction SilentlyContinue
    Write-Fail "Compilation failed (exit $LASTEXITCODE)."
    exit $LASTEXITCODE
}
Remove-Item $Sources -Force -ErrorAction SilentlyContinue
Write-Ok "Compilation successful."

# --- Copy resources ---
Write-Step "Copying XML/properties from src into classes..."
Get-ChildItem -Path $Src -File | Where-Object {
    $_.Extension -in ".xml", ".properties"
} | ForEach-Object {
    Copy-Item $_.FullName -Destination $Classes -Force
}

# Quick config sanity (non-fatal)
$configPath = Join-Path $Classes "config.xml"
if (Test-Path $configPath) {
    $cfg = Get-Content $configPath -Raw
    if ($cfg -match "mysql:3306") {
        Write-Host "  WARNING: config.xml still points at Docker host 'mysql:3306'. Use localhost for Windows." -ForegroundColor Yellow
    }
    if ($cfg -match "pagesize") {
        if ($cfg -match "<pagesize>(\d+)</pagesize>") {
            Write-Step "  pagesize = $($Matches[1])"
        }
    }
}

# --- WAR ---
if (-not $SkipWar) {
    $jarCmd = Get-Command jar -ErrorAction SilentlyContinue
    if (-not $jarCmd) {
        Write-Host "  WARNING: 'jar' not on PATH — skipping WAR package. Classes are still rebuilt." -ForegroundColor Yellow
    } else {
        Write-Step "Packaging SchoolCanteenMS.war ..."
        if (Test-Path $WarFile) {
            Remove-Item $WarFile -Force
        }
        $webContent = Join-Path $Project "WebContent"
        Push-Location $webContent
        try {
            & jar -cf $WarFile .
            if ($LASTEXITCODE -ne 0) {
                throw "jar failed with exit $LASTEXITCODE"
            }
        } finally {
            Pop-Location
        }
        $wi = Get-Item $WarFile
        Write-Ok ("WAR created: {0} ({1:N1} MB)" -f $wi.FullName, ($wi.Length / 1MB))
    }
} else {
    Write-Step "SkipWar set — classes only, no WAR."
}

# --- Optional deploy copy ---
if ($DeployTo -and $DeployTo.Trim() -ne "") {
    if (-not (Test-Path $WarFile)) {
        throw "Cannot deploy: WAR not found at $WarFile (run without -SkipWar)."
    }
    $destDir = Split-Path -Parent $DeployTo
    if (-not (Test-Path $destDir)) {
        throw "Deploy destination folder does not exist: $destDir"
    }
    Write-Step "Copying WAR to $DeployTo ..."
    Copy-Item $WarFile -Destination $DeployTo -Force
    Write-Ok "Deployed WAR copy done. Restart Tomcat to load it."
}

Write-Ok "Rebuild complete."
Write-Host ""
Write-Host "Next:" -ForegroundColor Cyan
Write-Host "  1. Stop Tomcat"
Write-Host "  2. Copy SchoolCanteenMS.war to Tomcat webapps (or use -DeployTo)"
Write-Host "  3. Start Tomcat"
Write-Host "  MySQL was NOT modified."
Write-Host ""
