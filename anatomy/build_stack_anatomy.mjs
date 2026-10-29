import { writeFileSync } from "node:fs";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";

import * as k from "../../../scratch/anatomy/kit/iso-kit.mjs";

const WIDTH = 940;
const HEIGHT = 580;

// Two comparison towers:
// Left: The Bloated E-Waste Stack (Heavy, Choked, 95% RAM)
// Right: The Lean Dedicated Appliance Stack (Instant, 210MB, Pure Utility)

const TOWER_W = 110;
const TOWER_D = 110;
const LEFT_X = -130;
const RIGHT_X = 130;

const P = k.fitProjection(
  [
    [-240, -100, 0], [240, 100, 240]
  ],
  WIDTH,
  HEIGHT,
  { pad: 40, azimuth: 44, elevation: 32 }
);

const at = (point) => k.iso(point, P);
const S = (paths, style) => k.solidSvg(paths, style);
const L = (d, style) => k.lineSvg(d, style);

function renderStacks() {
  const out = [];

  // ==================== LEFT TOWER: BLOATED PHONE ====================
  const planL = { x: LEFT_X - TOWER_W/2, y: -TOWER_D/2, w: TOWER_W, d: TOWER_D, r: 8 };

  // Base: Linux Kernel & Android Framework (40h)
  out.push(`<!-- Left 1: Base -->`);
  out.push(S(k.slabOf(planL, 0, 30, P, 6, 1.2), { tone: "base" }));

  // Layer 2: Google Play Services & Background Sync (60h)
  out.push(`<!-- Left 2: GMS Bloat -->`);
  out.push(S(k.slabOf(planL, 34, 45, P, 6, 1.2), { tone: "lo" }));

  // Layer 3: Carrier Bloat (Verizon Cloud, VZ Navigator, NFL Mobile, Demo Apps) (40h)
  out.push(`<!-- Left 3: Carrier Bloat -->`);
  out.push(S(k.slabOf(planL, 83, 38, P, 6, 1.2), { tone: "mid" }));

  // Layer 4: Heavy Stock Launcher, Widgets & Telemetry (30h)
  out.push(`<!-- Left 4: Stock Launcher -->`);
  out.push(S(k.slabOf(planL, 125, 32, P, 6, 1.2), { tone: "hi" }));

  // Top Warning Beacon / Choke Flag
  const topCenterL = at([LEFT_X, 0, 157]);
  out.push(`<circle cx="${topCenterL[0]}" cy="${topCenterL[1]}" r="6" fill="#ef4444" stroke="#fff" stroke-width="1.5"/>`);
  out.push(`<text x="${topCenterL[0]}" y="${topCenterL[1] - 14}" fill="#fca5a5" font-family="system-ui, sans-serif" font-size="12" font-weight="bold" text-anchor="middle">94% RAM PEGGED (940MB / 1GB)</text>`);
  out.push(`<text x="${topCenterL[0]}" y="${topCenterL[1] + 28}" fill="#ef4444" font-family="system-ui, sans-serif" font-size="11" font-weight="bold" text-anchor="middle">❌ Digital Scrap: 6hr Battery • Heavy Stutter</text>`);


  // ==================== RIGHT TOWER: DEDICATED APPLIANCE ====================
  const planR = { x: RIGHT_X - TOWER_W/2, y: -TOWER_D/2, w: TOWER_W, d: TOWER_D, r: 8 };

  // Base: Stripped Linux Kernel & Native HAL (30h)
  out.push(`<!-- Right 1: Lean Kernel -->`);
  out.push(S(k.slabOf(planR, 0, 30, P, 6, 1.2), { tone: "base" }));

  // Layer 2: Direct Hardware Media Driver & Wakelock (Zero GMS) (20h)
  out.push(`<!-- Right 2: Direct HAL -->`);
  out.push(S(k.slabOf(planR, 34, 22, P, 6, 1.2), { tone: "lo" }));

  // Layer 3: Single-Purpose Native Appliance Kiosk (BhaktiPlayer 45KB) (35h)
  out.push(`<!-- Right 3: Kiosk Appliance -->`);
  out.push(S(k.slabOf(planR, 60, 38, P, 6, 1.2), { tone: "hi" }));

  // Active Aura Ring around Right Stack
  out.push(L(k.ring(RIGHT_X, 0, 85, 0, P, 36), { tone: "hi" }));

  // Top Gold Diya Beacon
  const topCenterR = at([RIGHT_X, 0, 98]);
  out.push(`<circle cx="${topCenterR[0]}" cy="${topCenterR[1]}" r="6" fill="#f59e0b" stroke="#fff" stroke-width="1.5"/>`);
  out.push(`<text x="${topCenterR[0]}" y="${topCenterR[1] - 14}" fill="#fde68a" font-family="system-ui, sans-serif" font-size="12" font-weight="bold" text-anchor="middle">21% RAM USED (210MB / 1GB)</text>`);
  out.push(`<text x="${topCenterR[0]}" y="${topCenterR[1] + 28}" fill="#10b981" font-family="system-ui, sans-serif" font-size="11" font-weight="bold" text-anchor="middle">⚡ Dedicated Appliance: 4+ Days Standby • 0ms Delay</text>`);

  // Comparison Arrow / Transform Flow
  const midLeft = at([LEFT_X + TOWER_W/2 + 10, 0, 60]);
  const midRight = at([RIGHT_X - TOWER_W/2 - 10, 0, 60]);
  out.push(`<defs>
    <marker id="arrow" viewBox="0 0 10 10" refX="5" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 0 L 10 5 L 0 10 z" fill="#f59e0b"/>
    </marker>
  </defs>`);
  out.push(`<line x1="${midLeft[0]}" y1="${midLeft[1]}" x2="${midRight[0]}" y2="${midRight[1]}" stroke="#f59e0b" stroke-width="2.5" stroke-dasharray="6 4" marker-end="url(#arrow)"/>`);
  out.push(`<text x="${(midLeft[0] + midRight[0])/2}" y="${midLeft[1] - 12}" fill="#ffb300" font-family="system-ui, sans-serif" font-size="12" font-weight="bold" text-anchor="middle">TARGETED DEBLOAT & KIOSK LOCK</text>`);

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
  ${renderStacks()}
</svg>
`;

writeFileSync(join(dirname(fileURLToPath(import.meta.url)), "stack-anatomy.svg"), svgContent.trim(), "utf8");
console.log("Successfully generated stack-anatomy.svg!");
