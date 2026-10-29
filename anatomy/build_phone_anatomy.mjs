import { writeFileSync } from "node:fs";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";

// Import from the cloned anatomy kit
import * as k from "../../../scratch/anatomy/kit/iso-kit.mjs";

const WIDTH = 920;
const HEIGHT = 680;

// Exploded Moto E phone dimensions (scaled in world mm/units)
// Moto E physical size: 129.9 mm tall, 66.8 mm wide, 12.3 mm thick
const W = 110;       // width
const LENGTH = 210;  // length/height along y
const R = 18;        // rounded corner radius

// Layer Plans
const BASE_SHELL = { x: -W/2, y: -LENGTH/2, w: W, d: LENGTH, r: R };
const BATTERY    = { x: -W/2 + 10, y: -LENGTH/2 + 25, w: W - 20, d: 110, r: 8 };
const PCB_BOARD  = { x: -W/2 + 6, y: -LENGTH/2 + 10, w: W - 12, d: LENGTH - 20, r: 12 };
const SCREEN     = { x: -W/2 + 8, y: -LENGTH/2 + 22, w: W - 16, d: LENGTH - 44, r: 6 };
const GLASS      = { x: -W/2, y: -LENGTH/2, w: W, d: LENGTH, r: R };
const SPEAKER    = { x: -20, y: LENGTH/2 - 18, w: 40, d: 8, r: 3 };

// Exploded Z-heights
const Z_SHELL   = 0;
const Z_BATTERY = 45;
const Z_PCB     = 95;
const Z_SCREEN  = 160;
const Z_GLASS   = 225;

const P = k.fitProjection(
  [
    ...k.boxCorners(BASE_SHELL, Z_SHELL, Z_SHELL + 12),
    ...k.boxCorners(GLASS, Z_GLASS, Z_GLASS + 6),
    [0, LENGTH/2 + 30, Z_GLASS + 40]
  ],
  WIDTH,
  HEIGHT,
  { pad: 40, azimuth: 45, elevation: 32 }
);

const at = (point) => k.iso(point, P);
const S = (paths, style) => k.solidSvg(paths, style);
const L = (d, style) => k.lineSvg(d, style);
const DOTS = (points, style) => k.dotsSvg(points, style);

function renderLayers() {
  const out = [];

  // 1. BACK SHELL (Base Polycarbonate Chassis)
  out.push(`<!-- 1. Back Shell -->`);
  out.push(S(k.slabOf(BASE_SHELL, Z_SHELL, 14, P, 12, 2.5), { tone: "base" }));
  // Camera ring cutout
  out.push(L(k.ring(0, LENGTH/2 - 40, 10, Z_SHELL + 14, P, 24), { tone: "lo" }));
  out.push(L(k.ring(0, LENGTH/2 - 40, 7, Z_SHELL + 14, P, 24), { tone: "mid" }));
  // Motorola Batwing M Dimple
  out.push(L(k.ring(0, LENGTH/2 - 70, 9, Z_SHELL + 14, P, 24), { tone: "lo" }));
  out.push(DOTS([at([0, LENGTH/2 - 70, Z_SHELL + 14])], { size: 0.6 }));

  // Exploded connecting vertical guide lines
  const corners = k.corners(BASE_SHELL, 10);
  for (const [cx, cy] of corners) {
    const p0 = at([cx, cy, Z_SHELL + 14]);
    const p1 = at([cx, cy, Z_GLASS]);
    out.push(`<line x1="${p0[0]}" y1="${p0[1]}" x2="${p1[0]}" y2="${p1[1]}" stroke="#5c4a3d" stroke-width="0.8" stroke-dasharray="3 3"/>`);
  }

  // 2. BATTERY (2390 mAh Li-ion Cell)
  out.push(`<!-- 2. Li-ion Battery -->`);
  out.push(S(k.slabOf(BATTERY, Z_BATTERY, 8, P, 6, 1.2), { tone: "mid" }));
  out.push(L(k.planOutline(k.insetPlan(BATTERY, 4), Z_BATTERY + 8, P), { tone: "lo" }));
  // Battery terminal contacts
  out.push(S(k.slabOf({ x: -14, y: -LENGTH/2 + 25, w: 28, d: 8, r: 2 }, Z_BATTERY + 8, 2, P, 2), { tone: "hi" }));

  // 3. MOTHERBOARD (Snapdragon 410, eMMC, Front Speaker Chamber)
  out.push(`<!-- 3. Motherboard PCB -->`);
  out.push(S(k.slabOf(PCB_BOARD, Z_PCB, 5, P, 8, 1.0), { tone: "mid" }));
  // Snapdragon 410 SoC Shield Can
  const socCan = { x: -18, y: 15, w: 36, d: 36, r: 3 };
  out.push(S(k.slabOf(socCan, Z_PCB + 5, 4, P, 3, 0.6), { tone: "hi" }));
  out.push(L(k.planOutline(k.insetPlan(socCan, 3), Z_PCB + 9, P), { tone: "mid" }));
  
  // eMMC 8GB Flash + 1GB RAM chip
  const emmcCan = { x: -W/2 + 18, y: -20, w: 26, d: 24, r: 2 };
  out.push(S(k.slabOf(emmcCan, Z_PCB + 5, 3, P, 2, 0.5), { tone: "lo" }));
  
  // MicroSD Tray (32GB Storage Expansion)
  const sdTray = { x: W/2 - 32, y: -LENGTH/2 + 30, w: 20, d: 28, r: 2 };
  out.push(S(k.slabOf(sdTray, Z_PCB + 5, 2.5, P, 2, 0.4), { tone: "mid" }));
  
  // Front-Facing Speaker Acoustic Transducer
  out.push(S(k.slabOf(SPEAKER, Z_PCB + 5, 6, P, 3, 0.8), { tone: "hi" }));
  out.push(L(k.planOutline(k.insetPlan(SPEAKER, 1.5), Z_PCB + 11, P), { tone: "lo" }));

  // 4. DISPLAY PANEL (4.5" IPS 540x960 qHD)
  out.push(`<!-- 4. IPS Display Panel -->`);
  out.push(S(k.slabOf(SCREEN, Z_SCREEN, 3, P, 4, 0.5), { tone: "base" }));
  // Devotional UI Card representations on Screen
  const card1 = { x: SCREEN.x + 8, y: SCREEN.y + SCREEN.d - 45, w: SCREEN.w - 16, d: 34, r: 4 };
  const card2 = { x: SCREEN.x + 8, y: SCREEN.y + SCREEN.d - 85, w: SCREEN.w - 16, d: 34, r: 4 };
  const card3 = { x: SCREEN.x + 8, y: SCREEN.y + SCREEN.d - 125, w: SCREEN.w - 16, d: 34, r: 4 };
  out.push(S(k.slabOf(card1, Z_SCREEN + 3, 1.2, P, 2), { tone: "hi" })); // Active Hanuman Chalisa Card
  out.push(S(k.slabOf(card2, Z_SCREEN + 3, 0.8, P, 2), { tone: "mid" }));
  out.push(S(k.slabOf(card3, Z_SCREEN + 3, 0.8, P, 2), { tone: "mid" }));

  // 5. GORILLA GLASS 3 (Protective Front Cover + Bezel)
  out.push(`<!-- 5. Gorilla Glass 3 -->`);
  out.push(S(k.slabOf(GLASS, Z_GLASS, 4, P, 12, 1.8), { tone: "lo" }));
  out.push(L(k.planOutline(k.insetPlan(GLASS, 4), Z_GLASS + 4, P), { tone: "mid" }));
  // Top Speaker Earpiece Grille cutout
  out.push(S(k.slabOf({ x: -16, y: LENGTH/2 - 16, w: 32, d: 4, r: 2 }, Z_GLASS + 4, 0.5, P, 1), { tone: "base" }));

  // Soundwaves radiating from front-firing speaker
  const waveZ = Z_GLASS + 10;
  const spkCenter = at([0, LENGTH/2 - 14, waveZ]);
  for (let r = 20; r <= 60; r += 14) {
    out.push(`<ellipse cx="${spkCenter[0]}" cy="${spkCenter[1] - r*0.5}" rx="${r}" ry="${r*0.4}" fill="none" stroke="#f59e0b" stroke-width="1.8" opacity="${(80 - r)/80}" stroke-dasharray="4 2"/>`);
  }

  // Engineering Callout Annotations
  const callouts = [
    { pt: [W/2 + 5, -LENGTH/2 + 40, Z_PCB + 5], label: "Snapdragon 410 + 1GB RAM", desc: "Lean Linux Kernel (210MB Footprint)" },
    { pt: [W/2 + 5, -LENGTH/2 + 80, Z_BATTERY + 4], label: "2,390 mAh Li-ion Battery", desc: "4+ Days Standby (No GApps Drain)" },
    { pt: [-W/2 - 5, LENGTH/2 - 40, Z_SCREEN + 4], label: "4.5\" IPS High-Contrast Panel", desc: "Gorilla Glass 3 • Gerontology UI" },
    { pt: [0, LENGTH/2 + 5, Z_GLASS + 8], label: "Front-Firing Dedicated Acoustic Chamber", desc: "Tuned for Clear Vocal Aarti Audio" }
  ];

  for (const c of callouts) {
    const pIso = at(c.pt);
    const lineEndX = pIso[0] > WIDTH/2 ? pIso[0] + 60 : pIso[0] - 60;
    const lineEndY = pIso[1] - 30;
    out.push(`<circle cx="${pIso[0]}" cy="${pIso[1]}" r="3" fill="#f59e0b" stroke="#fff" stroke-width="1.2"/>`);
    out.push(`<polyline points="${pIso[0]},${pIso[1]} ${lineEndX},${lineEndY} ${lineEndX + (lineEndX > pIso[0] ? 40 : -40)},${lineEndY}" fill="none" stroke="#f59e0b" stroke-width="1.2"/>`);
    const anchor = lineEndX > pIso[0] ? "start" : "end";
    const textX = lineEndX + (lineEndX > pIso[0] ? 45 : -45);
    out.push(`<text x="${textX}" y="${lineEndY - 4}" fill="#fff9ed" font-family="system-ui, sans-serif" font-size="12" font-weight="bold" text-anchor="${anchor}">${c.label}</text>`);
    out.push(`<text x="${textX}" y="${lineEndY + 12}" fill="#d6c7b2" font-family="system-ui, sans-serif" font-size="10" text-anchor="${anchor}">${c.desc}</text>`);
  }

  return out.join("\n");
}

const svgContent = `
<svg viewBox="0 0 ${WIDTH} ${HEIGHT}" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" style="background:#12100e;">
  <defs>
    <style>
      .iso-base { fill: #1c1815; stroke: #3a2e26; stroke-width: 1.2; }
      .iso-lo   { fill: #28211b; stroke: #4f3f35; stroke-width: 1.2; }
      .iso-mid  { fill: #382e26; stroke: #6b5548; stroke-width: 1.2; }
      .iso-hi   { fill: #544438; stroke: #a3826e; stroke-width: 1.4; }
      .iso-line { stroke: #e5a93c; stroke-width: 1.2; fill: none; }
      .iso-dot  { fill: #ffd54f; }
    </style>
  </defs>
  ${renderLayers()}
</svg>
`;

const htmlPage = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Anatomy: Resurrecting Old Android into Hardware Appliance</title>
  <style>
    body {
      margin: 0;
      padding: 24px;
      background: #0a0908;
      color: #fff9ed;
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
      display: flex;
      flex-direction: column;
      align-items: center;
    }
    .card {
      max-width: 920px;
      width: 100%;
      background: #14110f;
      border: 1px solid #3d3027;
      border-radius: 16px;
      overflow: hidden;
      box-shadow: 0 20px 40px rgba(0,0,0,0.8);
    }
    .header {
      padding: 20px 24px;
      border-bottom: 1px solid #3d3027;
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: #181411;
    }
    .title { font-size: 18px; font-weight: bold; color: #ffb300; }
    .badge {
      background: #2a1f18;
      color: #f59e0b;
      padding: 4px 10px;
      border-radius: 20px;
      font-size: 12px;
      border: 1px solid #d97706;
    }
    .caption {
      padding: 16px 24px;
      border-top: 1px solid #3d3027;
      background: #181411;
      font-size: 13px;
      color: #d6c7b2;
      line-height: 1.5;
    }
  </style>
</head>
<body>
  <div class="card">
    <div class="header">
      <div class="title">FIGURE 1.0 — Exploded Hardware Anatomy: Moto E (XT1528) Dedicated Appliance</div>
      <div class="badge">E-WASTE RESURRECTION</div>
    </div>
    ${svgContent}
    <div class="caption">
      <strong>Mechanical & Electrical Deconstruction:</strong> Exploded isometric projection showing how legacy Qualcomm Snapdragon 410 hardware is decoupled from cloud telemetry. By routing audio directly to the dedicated front-firing chamber and utilizing an offline flash partition, the device achieves high acoustic output with zero background battery drain.
    </div>
  </div>
</body>
</html>
`;

writeFileSync(join(dirname(fileURLToPath(import.meta.url)), "phone-anatomy.html"), htmlPage, "utf8");
writeFileSync(join(dirname(fileURLToPath(import.meta.url)), "phone-anatomy.svg"), svgContent.trim(), "utf8");
console.log("Successfully generated phone-anatomy.html and phone-anatomy.svg!");
