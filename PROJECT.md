# Resurrect Phone — Zero-Bloat Android Hardware Appliance Engine
> Transforming legacy Android devices (Snapdragon 400/410, 1GB RAM) from digital scrap into ultra-reliable, dedicated single-purpose hardware appliances. Flagship: Bhakti Sangrah (Devotional Hub for the Elderly).

## Status
| Field | Value |
|---|---|
| **Status** | 🟢 Production Active |
| **Version** | v2.1.0-Appliance |
| **Category** | personal-projects |
| **Author** | Swetang Gajjar |
| **Last Updated** | 2026-10-31 |

## Tech Stack
| Layer | Technology |
|---|---|
| **Target OS** | Android 5.0+ (API 21+) |
| **Build System** | Zero-Dependency SDK (AAPT + Javac --release 8 + D8 + Apksigner) |
| **Language** | Native Java (Zero runtime overhead, 45 KB binary) |
| **Debloating Engine** | User-0 ADB Package Manager Scripting |
| **Anatomy Visuals** | Isometric SVG Architecture via Anatomy Kit |
| **Deployment** | GitHub Repository + GitHub Pages Showcase |

## Links
| Type | URL |
|---|---|
| **Live Showcase** | [https://sgajjar24.github.io/resurrect-phone/](https://sgajjar24.github.io/resurrect-phone/) |
| **GitHub Repo** | [https://github.com/SGajjar24/resurrect-phone](https://github.com/SGajjar24/resurrect-phone) |
| **Direct APK** | [BhaktiPlayer.apk](apk/BhaktiPlayer.apk) |

## Key Features & Achievements
- **Hardware Resurrection:** Proven on Motorola Moto E 2nd Gen LTE (`XT1528`).
- **Memory Footprint:** Idle RAM reduced from 940MB (94% choke) to 210MB (780MB free).
- **Standby Battery:** Increased from 6 hours to 4+ days by eliminating GApps telemetry wake-locks.
- **Bhakti Sangrah UI:** Gerontology-focused sacred dark theme (`#12100E`), divine deity emblems, 64dp full-width cards, and direct NewPipe search integration.
- **Universal Appliance Blueprints:** Full architecture guides for Handheld Barcode Scanner, Smart Home Kiosk, and IP Camera.

## AI Conversation Log
| Date | Topic | Key Decisions |
|---|---|---|
| 2026-10-30 | UI Overhaul v2.0 | Implemented sacred dark theme, deity badges, active card glowing borders, and pinned bottom dock |
| 2026-10-30 | Public Release & Architecture | Created GitHub repo `SGajjar24/resurrect-phone`, generated isometric Anatomy figures, built GitHub Pages showcase |
| 2026-10-31 | SystemUI Crash Fix & Appliance v2.1 | Diagnosed and fixed density=0 SystemUI VectorDrawable crash loop; implemented Immersive Sticky Kiosk mode, Sleep Timer (15/30/60m), BootReceiver auto-launch, and turnkey `setup_appliance.ps1` |
