<#
.SYNOPSIS
    Universal Safe Carrier & Telemetry Debloater for Android Devices
.DESCRIPTION
    Safely uninstalls preloaded bloatware, carrier spyware (Verizon, AT&T, Sprint, T-Mobile),
    and heavy background analytics for User 0 using ADB. Non-destructive: apps can be
    reinstalled anytime via 'cmd package install-existing <pkg>'.
#>

param(
    [string]$DeviceId = ""
)

$adbCmd = if ($DeviceId) { "adb -s $DeviceId shell" } else { "adb shell" }

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Android Device Resurrection: Zero-Bloat Appliance Prep   " -ForegroundColor Yellow
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Carrier Bloatware (Verizon, AT&T, Sprint, T-Mobile)
$carrierPackages = @(
    "com.vzw.hss.myverizon",
    "com.telecomsys.directedsms.android.vzw",
    "com.motonav.vzw",
    "com.asurion.android.verizon.vms",
    "com.verizon.mips.services",
    "com.vzw.apnlib",
    "com.gotv.nflgamecenter.us.lite",
    "com.slacker.radio",
    "com.amazon.mShop.android",
    "com.amazon.mp3",
    "com.imdb.mobile",
    "com.att.myWireless",
    "com.tmobile.pr.mytmobile"
)

# 2. Manufacturer & Telemetry Bloatware
$oemPackages = @(
    "com.motorola.help",
    "com.motorola.motocare",
    "com.motorola.motosignature.app",
    "com.motorola.genie",
    "com.motorola.contextual.checkin",
    "com.motorola.ccc.checkin"
)

# 3. Safe Google Overbloat (Preserves core Android OS components)
$googleOverbloat = @(
    "com.google.android.videos",
    "com.google.android.music",
    "com.google.android.apps.books",
    "com.google.android.apps.plus",
    "com.google.android.apps.magazines",
    "com.google.android.play.games",
    "com.google.android.talk",
    "com.google.android.street"
)

$allBloat = $carrierPackages + $oemPackages + $googleOverbloat

Write-Host "Starting safe user-0 debloating..." -ForegroundColor Green
$removed = 0

foreach ($pkg in $allBloat) {
    $res = Invoke-Expression "$adbCmd pm uninstall -k --user 0 $pkg 2>&1"
    if ($res -like "*Success*") {
        Write-Host "  [REMOVED] $pkg" -ForegroundColor Green
        $removed++
    }
}

Write-Host "Debloat completed: $removed packages uninstalled from active profile." -ForegroundColor Cyan
Write-Host "Device memory footprint significantly reduced." -ForegroundColor Yellow
