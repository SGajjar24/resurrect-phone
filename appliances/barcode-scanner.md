# Appliance Blueprint: Handheld Wireless Barcode & QR Scanner (POS Terminal)

> Turn any legacy Android phone (Android 5.0+) into a high-speed, enterprise-grade wireless barcode scanner for retail checkout, warehouse inventory, or asset tracking.

---

## 🎯 The Problem with Dedicated Scanners
Commercial wireless barcode scanners (Zebra, Honeywell, Datalogic) cost **$150 to $400 each**. Meanwhile, billions of old smartphones sitting in drawers have:
- Autofocus rear cameras capable of reading 1D/2D barcodes in milliseconds.
- High-speed 2.4/5GHz Wi-Fi and Bluetooth.
- Bright screens for live price checks and SKU verification.
- Loud speakers for clear scan-confirmation beeps.

---

## ⚡ Architecture & Data Flow

```
[ Barcode / QR Label ] 
         │
         ▼ (Camera Frame 30fps)
[ Legacy Android Device ] 
    ├── Camera HAL / Preview
    ├── Embedded ZXing / ZBar C++ Core (Zero Cloud, 100% Offline)
    ├── Acoustic Beeper (Hardware Audio Confirmation)
    └── Output Dispatcher
         ├── Mode 1: Bluetooth HID Keyboard (Types SKU directly into PC/Mac)
         ├── Mode 2: Local Wi-Fi WebSocket / REST (Pushes JSON to ERP/Shopify)
         └── Mode 3: Local SQLite Cache (Offline batch inventory scanning)
```

---

## 🛠️ Implementation Steps

### 1. Minimal Camera Barcode Engine
Instead of heavy modern Google Play Services MLKit, use a lightweight, zero-dependency C++/NDK or Java ZXing wrapper that runs smoothly on Snapdragon 400/410/600 chips with less than 35MB of RAM:

```java
// BarcodeScannerActivity.java
public class BarcodeScannerActivity extends Activity implements BarcodeCallback {
    private CompoundBarcodeView barcodeView;
    private ToneGenerator beeper;
    private BluetoothHidDevice hidDevice; // For Mode 1
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lock screen ON during work shift
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        beeper = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        barcodeView = new CompoundBarcodeView(this);
        barcodeView.decodeContinuous(this);
        setContentView(barcodeView);
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        if (result.getText() != null) {
            String barcode = result.getText();
            
            // 1. Play instant tactile audio confirmation
            beeper.startTone(ToneGenerator.TONE_PROP_BEEP, 100);
            
            // 2. Transmit via Bluetooth HID (instant typing into PC)
            sendAsKeyboardKeystrokes(barcode + "\n");
            
            // 3. Or dispatch via local WebSocket
            // dispatchToInventoryServer(barcode);
        }
    }
}
```

### 2. Output Mode Configurations

#### Mode A: Bluetooth HID (Virtual Wireless Keyboard)
- Emulates a standard USB barcode gun.
- Pairs with any Windows POS, Mac, Linux, or iPad.
- When an item is scanned, the phone simulates typing the barcode numbers and pressing `Enter`.
- No software installation needed on the host computer.

#### Mode B: Direct ERP / Inventory API Webhook
- Dispatches a lightweight HTTP POST payload over local Wi-Fi:
  ```json
  {
    "device_id": "MOTO-E-WH-02",
    "timestamp": "2026-10-30T03:30:00Z",
    "barcode": "012345678905",
    "symbology": "UPC-A",
    "battery_pct": 84
  }
  ```
- Works out-of-the-box with Shopify, WooCommerce, Odoo, and custom warehouse management systems.

### 3. Kiosk Lockdown & Battery Preservation
1. **Set as Default Launcher:** Ensure dropping or bumping the phone never exits to a settings menu or dialer.
2. **Proximity / Accelerometer Wakeup:** The screen sleeps when laid flat on a table and wakes within 150ms when picked up by a worker.
3. **Battery Charge Limit (Optional / Rooted):** Cap maximum charging at 80% to prevent battery swelling when resting on a charging dock 24/7.
