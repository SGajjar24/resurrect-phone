<#
.SYNOPSIS
    Zero-Dependency Native APK Build Script for Resurrect-Phone Appliances
#>

$BUILD_TOOLS = "C:\Users\sam\AppData\Local\Android\Sdk\build-tools\34.0.0"
$PLATFORM = "C:\Users\sam\AppData\Local\Android\Sdk\platforms\android-34\android.jar"
$JAVAC = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\javac.exe"

$REPO_ROOT = Split-Path -Parent $PSScriptRoot
Set-Location $REPO_ROOT

Remove-Item -Recurse -Force bin\* -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force obj\* -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path bin, obj, apk

Write-Host "1. AAPT package..." -ForegroundColor Cyan
& "$BUILD_TOOLS\aapt.exe" package -f -m -J src -M AndroidManifest.xml -S res -I $PLATFORM

Write-Host "2. Javac compile..." -ForegroundColor Cyan
$javaFiles = Get-ChildItem -Recurse src\*.java | ForEach-Object { $_.FullName }
& "$JAVAC" -d obj -cp $PLATFORM --release 8 $javaFiles

Write-Host "3. D8 dex..." -ForegroundColor Cyan
$classFiles = Get-ChildItem -Recurse obj\*.class | ForEach-Object { $_.FullName }
& "$BUILD_TOOLS\d8.bat" --output bin --lib $PLATFORM $classFiles

Write-Host "4. AAPT package APK..." -ForegroundColor Cyan
& "$BUILD_TOOLS\aapt.exe" package -f -M AndroidManifest.xml -S res -I $PLATFORM -F bin\unaligned.apk bin

Write-Host "5. Zipalign..." -ForegroundColor Cyan
& "$BUILD_TOOLS\zipalign.exe" -f -p 4 bin\unaligned.apk bin\BhaktiPlayer.apk

Write-Host "6. Sign APK with debug key..." -ForegroundColor Cyan
& "$BUILD_TOOLS\apksigner.bat" sign --ks "C:\Users\sam\.android\debug.keystore" --ks-pass pass:android --key-pass pass:android bin\BhaktiPlayer.apk

Copy-Item "bin\BhaktiPlayer.apk" "apk\BhaktiPlayer.apk"
Write-Host "Build complete! APK saved to apk\BhaktiPlayer.apk" -ForegroundColor Green
