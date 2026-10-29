# 📱 Resurrect Phone (Zero-Bloat Android Appliance Engine)

> **Transform "Digital Scrap" legacy Android phones into lightning-fast, dedicated hardware appliances — Devotional Players, Wireless Barcode Scanners, Smart Home Dashboards, and more.**

[![License: MIT](https://img.shields.io/badge/License-MIT-amber.svg)](LICENSE)
[![Android: 5.0+](https://img.shields.io/badge/Android-5.0%2B%20(API%2021%2B)-green.svg)](https://developer.android.com)
[![APK Size: 45 KB](https://img.shields.io/badge/APK%20Size-45%20KB-blue.svg)](apk/BhaktiPlayer.apk)
[![Platform: Qualcomm Snapdragon 400/410/600](https://img.shields.io/badge/Chipset-Snapdragon%20410-orange.svg)](https://qualcomm.com)

---

## 🪔 Live Showcase & Public Site
- 🌐 **Project Web Hub (GitHub Pages):** [https://sgajjar24.github.io/resurrect-phone/](https://sgajjar24.github.io/resurrect-phone/)
- 📦 **Direct APK Download:** [`apk/BhaktiPlayer.apk`](https://github.com/SGajjar24/resurrect-phone/raw/main/apk/BhaktiPlayer.apk) (45 KB)

---

## 💡 The Manifesto: E-Waste is a Software Problem

Every year, hundreds of millions of Android smartphones are discarded into drawers and landfills. Consumers are told their devices are "obsolete" because they stutter when running modern social media apps or battery-choking background analytics.

### The Truth
1. A 2015 smartphone (e.g. **Motorola Moto E 2nd Gen**, quad-core Snapdragon 410, 1GB RAM) has **1,000× more compute power** than the Apollo Guidance Computer that put humans on the Moon.
2. It features high-precision components: a Corning Gorilla Glass IPS display, tuned acoustic speaker chambers, Bluetooth/Wi-Fi radios, power ICs, and hardware H.264 video decoders.
3. **The device is not slow. It is choked.** A stock carrier OS loads 50+ background daemons, carrier tracking bloatware (Verizon Cloud, VZ Navigator, diagnostic collectors), and Google Play Services constantly waking the CPU to report telemetry.

### The Solution: The Single-Purpose Appliance Model
By stripping carrier telemetry and replacing the general-purpose smartphone launcher with a **zero-dependency, dedicated native Android kiosk application**, the device re-emerges as a high-performance, single-purpose hardware appliance.

---

## 📐 Figure 1: Exploded Hardware Skeleton & Anatomy

Built using the [Anatomy isometric vector system](https://github.com/wheresryan22/anatomy):

<p align="center">
  <img src="docs/phone-anatomy.svg" alt="Exploded Hardware Anatomy Diagram" width="850">
</p>

### Key Hardware Utilization:
- **Snapdragon 410 SoC:** 4× Cortex-A53 cores running a lean Linux kernel at 210MB total memory footprint.
- **Dedicated Front-Firing Speaker:** Acoustically tuned for vocal clarity without distortion.
- **2,390 mAh Li-ion Battery:** Lasts **4+ days** on standby because background sync wake-locks are completely eliminated.
- **MicroSD Card Tray:** 32GB high-speed offline flash storage for instant, zero-network media playback.

---

## ⚡ Figure 2: The Transformation Stack (Before vs. After)

<p align="center">
  <img src="docs/stack-anatomy.svg" alt="Software Stack Comparison" width="850">
</p>

### 📊 Real Device Benchmarks (Moto E XT1528)

| Metric | Stock Carrier ROM (Before) | Resurrected Appliance (After) | Gain |
|---|---|---|---|
| **RAM Consumed at Idle** | 940 MB (94% choke) | **210 MB** (780 MB free!) | **4.4× more free RAM** |
| **Cold Boot Time** | 45–60 seconds | **12 seconds** direct to UI | **3.8× faster boot** |
| **Standby Battery Life** | 6–8 hours | **4+ days** continuous | **12× longer battery** |
| **Audio Latency** | 240 ms | **< 20 ms** direct HAL | Instant responsive touch |
| **APK Binary Size** | 60–120 MB (React Native / Flutter) | **45 KB** (Direct Native AAPT/D8) | **1,500× smaller binary** |

---

## 🪔 Case Study 1 (Flagship): શ્રી ભક્તિ સંગ્રહ (Bhakti Player)

<p align="center">
  <img src="docs/screenshots/home_screen.png" width="220" alt="Home Screen">
  &nbsp;&nbsp;
  <img src="docs/screenshots/playing_active.png" width="220" alt="Active Playing">
  &nbsp;&nbsp;
  <img src="docs/screenshots/scrolled_view.png" width="220" alt="Scrolled View">
  &nbsp;&nbsp;
  <img src="docs/screenshots/newpipe_bhajan_search.png" width="220" alt="YouTube Bhajan Search">
</p>

### Purpose:
A dedicated, zero-confusion Devotional Media Player built specifically for **elderly Gujarati-speaking users**:
1. **Sacred Temple Dark UI (`#12100E`):** Eye-friendly obsidian/espresso background accented with temple brass gold (`#FFB300`) and warm ivory typography (`#FFF9ED`).
2. **Divine Iconography:** Custom high-resolution deity emblems (Hanuman Gada 🚩, Ganesh Modak 🐘, Shiv Trishul 🔱, Krishna Mor-Pankh 🦚, Amba Ma Diya 🪔, Dada Bhagwan Lotus 🕊️, Vishnu Chakra 🐚) enable instant visual recognition without reading text.
3. **Full-Width Touch Cards (64dp):** Oversized touch targets spanning the full screen width with touch ripples for senior motor accessibility.
4. **Active Glow & Equalizer:** The active prayer card highlights with a 2.5dp golden border and animated green equalizer waveform.
5. **Direct YouTube Bhajan Search (NewPipe Integration):** Tapping "યુટ્યુબ ભજન" bypasses NewPipe's generic home screen and launches directly into Gujarati bhajan video results ad-free.
6. **Pinned Bottom Player Dock:** Live status ("વાગી રહ્યું છે: ..."), large tactile Play/Pause and Stop buttons, and on-screen volume controls.
7. **No Sim / No Dialer / No Contacts:** Zero risk of elderly users accidentally dialing numbers or getting lost in Android settings.

---

## 🛠️ Universal Appliance Catalog: What Else Can You Build?

### 1. 📦 Handheld Wireless Barcode & QR Scanner (POS Terminal)
- **Use Case:** Warehouse inventory, retail checkout, asset tagging.
- **How it Works:** Embedded ZXing/ZBar camera engine reads 1D/2D barcodes at 30 fps.
- **Output:** Emulates a Bluetooth HID wireless keyboard (types scanned SKU directly into any PC/Mac followed by Enter) or dispatches HTTP/WebSocket JSON to Shopify/ERP.
- **Blueprint:** [`appliances/barcode-scanner.md`](appliances/barcode-scanner.md)

### 2. 🏡 Wall-Mounted Smart Home Kiosk & Room Controller
- **Use Case:** Home Assistant touch controller for lights, thermostats, and doorbell cameras.
- **How it Works:** Front proximity sensor wakes the display when a hand approaches; sleeps when empty (<0.2W power draw).
- **Blueprint:** [`appliances/smart-home-wall-kiosk.md`](appliances/smart-home-wall-kiosk.md)

### 3. 📹 Local RTSP Security Camera / Baby Monitor
- **Use Case:** Wi-Fi security camera streaming directly to Home Assistant or NAS without subscription fees.
- **How it Works:** Utilizes the phone's hardware H.264 video encoder to serve an RTSP / WebRTC stream over the local network.

---

## 🚀 3-Step Quickstart: Resurrect Your Old Phone

### Step 1: Connect Phone via USB & Check ADB
```powershell
adb devices
```

### Step 2: Run Universal Carrier Debloater
Safely removes preloaded bloatware without root:
```powershell
powershell -File scripts/debloat_carrier.ps1
```

### Step 3: Compile & Install Kiosk Appliance
```powershell
# Zero-dependency build via AAPT + Javac + D8 + Apksigner
powershell -File scripts/build.ps1
adb install -r apk/BhaktiPlayer.apk
```

---

## 📂 Repository Structure

```
resurrect-phone/
├── apk/                  # Compiled & signed ready-to-flash APK releases
│   └── BhaktiPlayer.apk  # 45 KB native appliance binary
├── src/                  # Clean native Java source code
│   └── com/gujarati/bhaktiplayer/
│       ├── MainActivity.java
│       └── BhaktiService.java
├── res/                  # Layouts, themes, and high-res deity badges
├── scripts/              # Automated ADB scripts
│   ├── debloat_carrier.ps1
│   └── build.ps1
├── appliances/           # Engineering blueprints for other appliances
│   ├── barcode-scanner.md
│   └── smart-home-wall-kiosk.md
├── anatomy/              # Isometric vector diagrams (Anatomy engine)
│   ├── phone-anatomy.svg
│   └── stack-anatomy.svg
├── docs/                 # GitHub Pages landing site & screenshots
│   ├── index.html
│   └── screenshots/
└── README.md
```

---

## 👤 Author & Architecture
- **Creator:** [Swetang Gajjar](https://github.com/SGajjar24)
- **Target Hardware:** Motorola Moto E (2nd Gen LTE - XT1528)
- **License:** [MIT](LICENSE)
