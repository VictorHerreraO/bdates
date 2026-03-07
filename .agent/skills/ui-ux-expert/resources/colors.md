# Bdates Color Guidelines

This document defines the color system for the Bdates project, following Material 3 guidelines. It ensures a consistent brand feel and accessibility across all themes.

## 1. Brand Palette

| Color Name      | Hex Code  | Visual Reference | Key Usage                               |
| :-------------- | :-------- | :--------------- | :-------------------------------------- |
| **Bossanova**   | `#3C2A59` | 深紫色           | Primary Brand Color / Header Background |
| **Paradiso**    | `#388A7C` | 深青色           | Secondary Action / Dark Surface Accent  |
| **Tradewind**   | `#6EBEB1` | 淺青色           | Primary Action / FAB / Light Accent     |
| **Rajah**       | `#F6AF65` | 橙橘色           | Highlight / Tertiary / Countdown Focus  |
| **Bittersweet** | `#FF6961` | 珊瑚紅           | Error / Destructive Actions             |
| **Dolphin**     | `#695C7E` | 灰紫色           | Secondary Text / Muted Elements         |
| **Salmon**      | `#FA8072` | 鮭魚紅           | Secondary Highlight / Emoji BG          |
| **Gallery**     | `#EEEEEE` | 淺灰色           | Input Surface (Light) / Divider         |
| **Cod_Gray**    | `#191919` | 深灰色           | Input Surface (Dark)                    |
| **Alabaster**   | `#F8F8F8` | 近白色           | Primary Background (Light)              |

## 2. Material 3 Color Roles

### Light Theme
- **Primary**: `Bossanova` (#3C2A59)
- **Secondary**: `Tradewind` (#6EBEB1)
- **Tertiary**: `Rajah` (#F6AF65)
- **Tertiary Container**: `Dolphin` (#695C7E)
- **On Tertiary Container**: `White` (#FFFFFF)
- **Surface**: `Alabaster` (#F8F8F8)
- **On Surface**: `Black` (#000000)
- **Surface Variant**: `Gallery` (#EEEEEE)

### Dark Theme
- **Tertiary**: `Rajah` (#F6AF65)
- **Tertiary Container**: `Dolphin` (#695C7E)
- **Surface**: `Cod_Gray` (#121212)
- **On Surface**: `White` (#FFFFFF)
- **Surface Variant**: `Cod_Gray` (#191919)

---

## 3. Usage & Accessibility (WCAG)

### Contrast Standards
- **Standard Text**: Must maintain at least **4.5:1** contrast against the background.
- **Large Text/UI Components**: Must maintain at least **3.0:1** contrast.
- **Dolphin (#695C7E)**: Use primarily for secondary metadata or on very light surfaces to ensure readability.

### Semantic Meaning
- **Primary (Bossanova)**: Used for identity-heavy elements like top app bars and section containers.
- **Secondary (Tradewind/Paradiso)**: Used for functional interaction points like FABs and toggles.
- **Error (Bittersweet)**: Reserved strictly for failure states, deletion warnings, or missing data.

---

## 4. Elevation & Surfaces
In Material 3, elevation is communicated through **color overlays** rather than deep shadows:
- **Surface Container Low**: Default background.
- **Surface Container Medium**: Base cards or list items.
- **Surface Container High**: Elevated dialogs or bottom sheets.
