# Bdates Visual & Brand Identity (Brand Bible)

This document is the definitive source of truth for the Bdates Android application design system (Jetpack Compose / Material 3). All UI components must adhere to these specifications to maintain brand consistency.

See also: [Typography Guide](./typography.md) | [Color Guide](./colors.md)

## 1. Core Design Philosophy
The Bdates UI is built on **Material 3 (Non-Expressive)** principles. It prioritizes clarity, modern geometry, and a premium "layered" feel.

---

## 2. Global Layout & Spacing
- **Content Margin**: Standard horizontal screen margin is **16dp**. Consistent alignment to this gutter is mandatory for all primary content.
- **Purposeful Whitespace**:
    - Favor a spacious, breathable layout.
    - Padding should define hierarchy and improve tap targets (minimum **48dp** for interactive elements).
- **Vertical Rhythm**: 
    - Category Title to Category Item: **12dp**.
    - Category to Category: **24dp**.
    - List Item Internal Vertical Padding: **6dp to 8dp** (Refined for density).

---

## 3. Shape & Geometry (The Squircle)
Bdates uses high-radius rounded corners for a modern, friendly aesthetic.
- **Surface Rounding (Containers)**: Use **28dp** corner radii (Squircle) for primary screen-level containers, large cards, and dashboard panels. Panels anchored to the bottom should use `BottomSheetDialogShape` (top corners rounded only).
- **Component Rounding**:
    - **Search Bars**: Fully rounded (Pill shape) with a **1dp** stroke.
    - **Floating Action Buttons (FAB)**: Material 3 Standard/Extended shape.
    - **Icon Containers**: Nested in rounded surfaces (Medium radius: **12-16dp**) to create depth.

---

## 4. Dark Mode & Theming Strategy
The app uses a dual-tone "Semantic" palette. 
- **Light Theme**: High-key, white/off-white surfaces with vibrant accents.
- **Dark Theme**: Tonal elevation. Primary surface is near-black (#121212). Elevation is communicated via lighter surface containers, not shadows.
- **Semantic Shifting**: 
    - **Brand Primary (Teal)**: Should Shift to a slightly more desaturated/brighter hue in dark mode to prevent "vibration" against dark backgrounds.
    - **Warning/Error**: Maintain standard M3 red, but ensure AA contrast.

---

## 5. Accessibility & Readability (WCAG 2.1)
- **Contrast**: All text must maintain a minimum **4.5:1** contrast ratio against its background.
- **Touch Targets**: All interactive elements (buttons, list items, chips) must have a minimum touch area of **48x48dp**.
- **Typography**: Minimum font size for primary body text is **14sp**. Captions/Overlines must be at least **12sp**.

---

## 6. Interaction & State Design
- **States**: Use the standard Material 3 state-layer (overlay) for interaction feedback:
    - **Pressed**: Overlay 12%
    - **Hovered**: Overlay 8%
    - **Focused**: Overlay 12%
- **Easing**: Standard Material 3 Emphasized easing for all scale/fade transitions.

---

## 7. Standard UI Archetypes

### Today's Occasions Tiles
- Large, bold numerical focus.
- **28dp Squircle** corner radius (all four corners rounded).
- Background color: `tertiaryContainer` (`Dolphin` #695C7E).
- Text color: `tertiary` (`Rajah` #F6AF65) for the dominant number.

### Event List Items
- **Left**: Rounded icon container.
- **Center**: Title (Bold) and Subtitle (Normal weight).
- **Bottom**: Subtle gray divider separating the next item.

### Search Bar
- Pill-shaped.
- Leading icon (Magnifying glass).
- Subtle border stroke.

### Add Event (Bottom Sheet)
- **Container**: Modal Bottom Sheet with **28dp** top-corner radii.
- **Form Sections**: 
    - Combined Icon (18dp) and Label (TitleMedium).
    - **Input Fields**: Large, rounded-corner surfaces with generous internal padding.
- **Date Picker**: Inline Material 3 Calendar. Selected date must use the brand **Tradewind** (#6EBEB1) circle.
- **Primary Action**: Full-width persistent button at the bottom of the sheet.
