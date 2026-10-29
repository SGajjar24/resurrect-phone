# Appliance Blueprint: Wall-Mounted Smart Home Kiosk & Room Controller

> Convert any discarded 4.5"–6.0" Android device into a permanent, wall-mounted touch controller for Home Assistant, smart lighting, climate, and security.

---

## 🏛️ Architecture & Highlights

- **Touchscreen Control:** Displays live entity status (lights, fan, AC, doorbell camera feed).
- **Proximity Sensor Auto-Wake:** Screen remains completely off (consuming <0.2W); wakes automatically when a hand approaches within 5cm.
- **Always-On Local Network:** Connects via MQTT or WebSockets directly to the local Home Assistant server with zero external cloud dependencies.
- **Hardware Integration:** Front microphone can listen for local wake words (Wyoming Satellite / ESPHome voice assistant).

---

## 🛠️ Step-by-Step Setup

1. **Debloat the Device:**
   Run the universal debloat script (`scripts/debloat_carrier.ps1`) to remove all carrier apps and Google Play Services background battery drain.

2. **Configure Lockscreen & Home Intent:**
   ```bash
   # Remove lockscreen entirely
   adb shell locksettings set-disabled true
   
   # Prevent Wi-Fi sleep while plugged into wall power
   adb shell settings put global wifi_sleep_policy 2
   ```

3. **Install Fully Kiosk or Lightweight Native Webview:**
   Set the local Home Assistant dashboard URL (e.g., `http://homeassistant.local:8123/dashboard-wall`).
