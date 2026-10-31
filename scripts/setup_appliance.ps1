<#
.SYNOPSIS
    Turnkey Appliance Provisioning Script for Resurrect-Phone (Moto E XT1528 / Android 5.1)
.DESCRIPTION
    Converts any connected stock or wiped device into a dedicated, locked Gujarati Devotional Appliance.
    Includes safeguards against the SystemUI density crash bug.
#>

[CmdletBinding()]
param()

$ErrorActionPreference = "Stop"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "  RESURRECT-PHONE: Dedicated Appliance Provisioner v2.1   " -ForegroundColor Yellow
Write-Host "  Target: Shree Bhakti Sangrah Kiosk Appliance            " -ForegroundColor Yellow
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Check ADB connection
Write-Host "`n[1/6] Checking ADB device connection..." -ForegroundColor Cyan
$devices = adb devices | Select-String "device$"
if (-not $devices) {
    Write-Error "No ADB device detected in normal mode! Connect phone via USB with USB Debugging enabled."
}
$deviceId = ($devices[0] -split '\s+')[0]
Write-Host "Connected device: $deviceId" -ForegroundColor Green

# 2. Density & Resolution Safeguard (CRITICAL FOR MOTO E XT1528)
Write-Host "`n[2/6] Verifying display density & safety rules..." -ForegroundColor Cyan
$densityCheck = adb shell wm density
Write-Host "Current density: $densityCheck"
if ($densityCheck -match "Override density: 0") {
    Write-Warning "Detected dangerous density override (0) which causes SystemUI crash! Resetting..."
    adb shell wm density reset
}
adb shell wm density reset
adb shell settings put system font_scale 1.0

# 3. Safe Carrier Debloating (User 0 Only)
Write-Host "`n[3/6] Debloating carrier & non-appliance packages..." -ForegroundColor Cyan
$bloatPackages = @(
    "com.vzw.hss.myverizon",
    "com.motorola.visualvoicemail",
    "com.gotv.nflgamecenter.us.lite",
    "com.slacker.radio",
    "com.amazon.mShop.android.shopping",
    "com.vzw.apnlib",
    "com.motorola.audiomonitor",
    "com.motorola.bug2go",
    "com.motorola.ccc.checkin",
    "com.motorola.setupwizard.phoneservice",
    "com.motorola.genie",
    "com.motorola.help",
    "com.google.android.apps.books",
    "com.google.android.apps.magazines",
    "com.google.android.videos",
    "com.google.android.music",
    "com.google.android.talk",
    "com.google.android.apps.plus"
)

foreach ($pkg in $bloatPackages) {
    $res = adb shell pm uninstall -k --user 0 $pkg 2>&1
    Write-Host "  Debloat $pkg : $res" -ForegroundColor Gray
}

# 4. OS-Level Appliance & Kiosk Optimizations
Write-Host "`n[4/6] Configuring OS-level Kiosk & Display Settings..." -ForegroundColor Cyan
# Stay awake on charger (AC + USB)
adb shell settings put global stay_on_while_plugged_in 3
# 10 minute display timeout on battery
adb shell settings put system screen_off_timeout 600000
# Silence popup heads-up notifications
adb shell settings put global heads_up_notifications_enabled 0
# Force permanent full-screen immersive mode for BhaktiPlayer
adb shell settings put global policy_control immersive.full=com.gujarati.bhaktiplayer
Write-Host "  Kiosk policies applied successfully." -ForegroundColor Green

# 5. Push Audio Files if needed
Write-Host "`n[5/6] Verifying devotional audio tracks on storage..." -ForegroundColor Cyan
$hasBhajans = adb shell "ls /sdcard/Bhajans 2>/dev/null"
if (-not $hasBhajans) {
    Write-Warning "Directory /sdcard/Bhajans not found. Creating..."
    adb shell mkdir -p /sdcard/Bhajans
} else {
    Write-Host "  Devotional audio directory exists." -ForegroundColor Green
}

# 6. Install & Launch BhaktiPlayer v2.1
Write-Host "`n[6/6] Installing BhaktiPlayer v2.1 Appliance APK..." -ForegroundColor Cyan
$SCRIPT_DIR = Split-Path -Parent $MyInvocation.MyCommand.Path
$REPO_ROOT = Split-Path -Parent $SCRIPT_DIR
$APK_PATH = Join-Path $REPO_ROOT "apk\BhaktiPlayer.apk"

if (-not (Test-Path $APK_PATH)) {
    Write-Error "APK not found at $APK_PATH! Run scripts\build.ps1 first."
}

$installRes = adb install -r "$APK_PATH" 2>&1
Write-Host "  Install result: $installRes" -ForegroundColor Green

# Launch into home screen
adb shell am start -n com.gujarati.bhaktiplayer/.MainActivity | Out-Null
Write-Host "`n==========================================================" -ForegroundColor Green
Write-Host "  SUCCESS: Device is now a Dedicated Bhakti Appliance!   " -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Green
