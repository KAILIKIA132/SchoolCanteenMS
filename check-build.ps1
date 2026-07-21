#requires -version 5.1
# Verify School Canteen MS rebuild is complete on Windows.
# Does NOT modify files or MySQL.
#
# Usage:
#   .\check-build.ps1
#   powershell -ExecutionPolicy Bypass -File .\check-build.ps1

$ErrorActionPreference = "Continue"

function Write-Pass([string]$msg) {
    Write-Host ("  [PASS] " + $msg) -ForegroundColor Green
}
function Write-Fail([string]$msg) {
    Write-Host ("  [FAIL] " + $msg) -ForegroundColor Red
}
function Write-Info([string]$msg) {
    Write-Host ("  [INFO] " + $msg) -ForegroundColor Gray
}
function Write-Head([string]$msg) {
    Write-Host ""
    Write-Host $msg -ForegroundColor Cyan
}

$Project = if ($PSScriptRoot) { $PSScriptRoot } else { (Get-Location).Path }
Set-Location $Project

$Classes = Join-Path $Project "WebContent\WEB-INF\classes"
$Lib     = Join-Path $Project "WebContent\WEB-INF\lib"
$WarFile = Join-Path $Project "SchoolCanteenMS.war"
$Config  = Join-Path $Classes "config.xml"

$fail = 0
$pass = 0

function Expect-True([bool]$ok, [string]$passMsg, [string]$failMsg) {
    if ($ok) {
        Write-Pass $passMsg
        $script:pass++
    } else {
        Write-Fail $failMsg
        $script:fail++
    }
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " SchoolCanteenMS build check" -ForegroundColor Cyan
Write-Host (" Project: " + $Project) -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# --- Folders ---
Write-Head "1. Project layout"
Expect-True (Test-Path (Join-Path $Project "src")) "src folder exists" "src folder missing"
Expect-True (Test-Path $Classes) "WEB-INF\classes exists" "WEB-INF\classes missing - run rebuild.ps1"
Expect-True (Test-Path $Lib) "WEB-INF\lib exists" "WEB-INF\lib missing"
$jarCount = @(Get-ChildItem $Lib -Filter "*.jar" -ErrorAction SilentlyContinue).Count
Expect-True ($jarCount -gt 0) ("lib has " + $jarCount + " JAR(s)") "No JARs in WEB-INF\lib"

# --- Compiled classes ---
Write-Head "2. Compiled classes (required)"
$requiredClasses = @(
    "com\zk\action\UserAction.class",
    "com\zk\action\DeviceAction.class",
    "com\zk\manager\DeviceCommandManager.class",
    "com\zk\manager\UserInfoManager.class",
    "com\zk\dao\impl\UserInfoDao.class",
    "com\zk\dao\impl\PersonBioTemplateDao.class",
    "com\zk\pushsdk\util\PushUtil.class",
    "com\zk\pushsdk\PushCommServlet.class",
    "com\zk\util\ConfigUtil.class",
    "com\zk\util\PagenitionUtil.class"
)

foreach ($rel in $requiredClasses) {
    $path = Join-Path $Classes $rel
    Expect-True (Test-Path $path) $rel ("Missing: " + $rel)
}

$classCount = @(Get-ChildItem $Classes -Recurse -Filter "*.class" -ErrorAction SilentlyContinue).Count
Expect-True ($classCount -ge 50) ("Found " + $classCount + " .class files") ("Only " + $classCount + " .class files - rebuild may be incomplete")

# Freshness: compare newest class vs newest java under src
Write-Head "3. Class freshness vs source"
$newestJava = Get-ChildItem (Join-Path $Project "src") -Recurse -Filter "*.java" -ErrorAction SilentlyContinue |
    Sort-Object LastWriteTime -Descending | Select-Object -First 1
$newestClass = Get-ChildItem $Classes -Recurse -Filter "*.class" -ErrorAction SilentlyContinue |
    Sort-Object LastWriteTime -Descending | Select-Object -First 1

if ($newestJava -and $newestClass) {
    Write-Info ("Newest source: " + $newestJava.Name + " @ " + $newestJava.LastWriteTime)
    Write-Info ("Newest class:  " + $newestClass.Name + " @ " + $newestClass.LastWriteTime)
    if ($newestClass.LastWriteTime -ge $newestJava.LastWriteTime.AddMinutes(-1)) {
        Write-Pass "Classes look as new as (or newer than) sources"
        $pass++
    } else {
        Write-Fail ("Classes older than sources - run rebuild.ps1 (class: " + $newestClass.LastWriteTime + ", src: " + $newestJava.LastWriteTime + ")")
        $fail++
    }
} else {
    Write-Fail "Could not compare source/class timestamps"
    $fail++
}

# --- Config ---
Write-Head "4. config.xml (deploy classes)"
Expect-True (Test-Path $Config) "config.xml present in classes" "config.xml missing in WEB-INF\classes"

if (Test-Path $Config) {
    $cfg = Get-Content $Config -Raw
    if ($cfg -match "localhost:3306") {
        Write-Pass "JDBC host is localhost:3306 (Windows)"
        $pass++
    } elseif ($cfg -match "mysql:3306") {
        Write-Fail "JDBC host is Docker 'mysql:3306' - fix for Windows Server"
        $fail++
    } else {
        Write-Fail "Could not detect expected JDBC host in config.xml"
        $fail++
    }

    if ($cfg -match "pushdemo") {
        Write-Pass "Database name pushdemo found"
        $pass++
    } else {
        Write-Fail "Database name pushdemo not found in config.xml"
        $fail++
    }

    if ($cfg -match "<pagesize>(\d+)</pagesize>") {
        $ps = [int]$Matches[1]
        if ($ps -ge 100) {
            Write-Pass ("pagesize = " + $ps)
            $pass++
        } else {
            Write-Fail ("pagesize = " + $ps + " (expected 100)")
            $fail++
        }
    } else {
        Write-Fail "pagesize not found in config.xml"
        $fail++
    }

    if ($cfg -match "Canteen@2026") {
        Write-Info "Password still Canteen@2026 (OK if that is production password)"
    }
}

# --- Key JSPs ---
Write-Head "5. Web pages"
$jsps = @("userList.jsp", "deviceList.jsp", "addUser.jsp", "login.jsp")
foreach ($j in $jsps) {
    $p = Join-Path $Project ("WebContent\" + $j)
    Expect-True (Test-Path $p) $j ("Missing WebContent\" + $j)
}

# --- WAR ---
Write-Head "6. WAR package (optional)"
if (Test-Path $WarFile) {
    $wi = Get-Item $WarFile
    $mb = [math]::Round($wi.Length / 1MB, 1)
    Write-Pass ("SchoolCanteenMS.war exists (" + $mb + " MB, " + $wi.LastWriteTime + ")")
    $pass++
    if ($newestClass -and $wi.LastWriteTime -lt $newestClass.LastWriteTime.AddMinutes(-2)) {
        Write-Fail "WAR is older than newest class - re-run rebuild.ps1 to refresh WAR"
        $fail++
    } else {
        Write-Pass "WAR timestamp looks consistent with classes"
        $pass++
    }
} else {
    Write-Info "SchoolCanteenMS.war not found (OK if you deploy exploded WebContent only)"
}

# --- Tools ---
Write-Head "7. Build tools on PATH"
$javac = Get-Command javac -ErrorAction SilentlyContinue
$jar = Get-Command jar -ErrorAction SilentlyContinue
if ($javac) { Write-Pass ("javac: " + $javac.Source); $pass++ } else { Write-Fail "javac not on PATH"; $fail++ }
if ($jar) { Write-Pass ("jar: " + $jar.Source); $pass++ } else { Write-Info "jar not on PATH (needed only to build WAR)" }

# --- Summary ---
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host (" PASS: " + $pass + "   FAIL: " + $fail) -ForegroundColor $(if ($fail -eq 0) { "Green" } else { "Red" })
if ($fail -eq 0) {
    Write-Host " Build looks GOOD. Restart Tomcat if not done yet." -ForegroundColor Green
} else {
    Write-Host " Build incomplete. Run:  powershell -ExecutionPolicy Bypass -File .\rebuild.ps1" -ForegroundColor Yellow
}
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

exit $(if ($fail -eq 0) { 0 } else { 1 })
