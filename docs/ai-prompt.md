# 🤖 The Universal AI Appliance Prompt (Copy & Paste)

> **Feed this prompt to any AI coding assistant (Claude Code, Gemini CLI, Antigravity, Cursor, or ChatGPT) while your old Android phone is connected via USB.**
> The AI will automatically inspect your hardware, debloat carrier bloat safely, build a custom single-purpose kiosk app, and configure it as an ultra-reliable hardware appliance.

---

```markdown
You are an expert embedded Android system engineer and hardware revitalization specialist.
I have connected an older Android device (Android 4.4 to 10+) via USB with USB Debugging enabled.
Our goal is to transform this device from discarded digital scrap into a dedicated, single-purpose hardware appliance (e.g. Elderly Devotional Player, Barcode Scanner, Smart Home Kiosk, or Kitchen Hub).

Follow this rigorous, 5-phase execution plan:

### PHASE 1: HARDWARE RECONNAISSANCE & HEALTH AUDIT
1. Run `adb devices` to verify connection.
2. Query system specifications:
   - Device Model: `adb shell getprop ro.product.model`
   - Android Version & API Level: `adb shell getprop ro.build.version.release` and `ro.build.version.sdk`
   - Screen Resolution & Physical Density: `adb shell wm size` and `adb shell wm density`
   - Physical RAM: `adb shell cat /proc/meminfo`
3. Safety Check: Verify that `wm density` does NOT have an `Override density: 0` set (a density of 0 causes an infinite VectorDrawable crash in SystemUI). If override exists, run `adb shell wm density reset`.

### PHASE 2: SAFE CARRIER & TELEMETRY DEBLOAT (NON-DESTRUCTIVE)
1. List third-party and carrier packages via `adb shell pm list packages -3` and `pm list packages`.
2. Uninstall carrier junk and telemetry for User 0 only using `pm uninstall -k --user 0 <pkg>`.
3. STRICT SAFETY RULES:
   - NEVER uninstall or disable: `com.android.systemui`, `com.android.settings`, `com.android.providers.settings`, `com.android.phone`, or `com.android.server.telecom`.
   - Never set display density to 0. Font scale should stay between 1.0 and 1.15.

### PHASE 3: APPLIANCE APPLICATION ARCHITECTURE
1. Design a native Android app in Java (zero-dependency build, no heavy frameworks).
2. UI/UX Principles:
   - High-contrast sacred dark theme (`#12100E` background, `#FFB300` brass gold accents, `#FFF9ED` text).
   - Oversized touch targets (minimum 60dp height, full screen width) for senior motor ease.
   - High-contrast visual emblem badges (so users can recognize functions without reading).
   - Pinned bottom dock with persistent controls (Status, Volume, Play/Pause, Stop).
3. Manifest Configuration:
   - Add `<category android:name="android.intent.category.HOME" />` to `MainActivity` so it functions as the default home launcher.
   - Add `RECEIVE_BOOT_COMPLETED` permission and a `BootReceiver` so the appliance auto-launches upon power-on.

### PHASE 4: KIOSK HARDENING & POWER MANAGEMENT
1. Implement Immersive Sticky Mode in `MainActivity`:
   - Call `setSystemUiVisibility` with `SYSTEM_UI_FLAG_IMMERSIVE_STICKY`, `SYSTEM_UI_FLAG_HIDE_NAVIGATION`, and `SYSTEM_UI_FLAG_FULLSCREEN` on `onResume()` and `onWindowFocusChanged()`.
   - Run `adb shell settings put global policy_control immersive.full=<package_name>`.
2. Configure Power & Wake Settings via ADB:
   - Keep screen awake when plugged into charging stand: `adb shell settings put global stay_on_while_plugged_in 3`.
   - Set 10-minute timeout on battery: `adb shell settings put system screen_off_timeout 600000`.
   - Disable annoying heads-up notifications: `adb shell settings put global heads_up_notifications_enabled 0`.
3. Add a Sleep Timer toggle (Off, 15m, 30m, 60m auto-shutoff) to gracefully stop media without user intervention.

### PHASE 5: ZERO-DEPENDENCY NATIVE COMPILATION
1. Compile resources using `aapt.exe package -f -m -J src -M AndroidManifest.xml -S res -I android.jar`.
2. Compile Java bytecode using `javac --release 8 -d obj -cp android.jar src/**/*.java`.
3. Convert bytecode to Dalvik executable using `d8.bat --output bin --lib android.jar obj/**/*.class`.
4. Package, zipalign (4-byte), and sign with `apksigner.bat` using a debug or release keystore.
5. Install the APK via `adb install -r <app.apk>`, grant needed permissions, and launch `MainActivity`.
6. Take a screenshot via `adb shell screencap` to verify the finished appliance running live on screen.

Begin Phase 1 now by probing the connected ADB device.
```

---

## 💡 How to Use This Prompt:
1. Connect your old Android phone to your PC via USB cable.
2. Enable **Developer Options** and turn on **USB Debugging** on the phone.
3. Open your favorite AI tool in your workspace:
   - In **Claude Code**: paste the prompt directly.
   - In **Gemini CLI / Antigravity**: paste into the active session.
   - In **Cursor / VS Code**: paste into Composer or Chat.
4. Watch as the AI transforms your phone step-by-step into a single-purpose appliance!
