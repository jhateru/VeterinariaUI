---
name: Clinical Calm
colors:
  surface: '#fbf9f8'
  surface-dim: '#dbd9d9'
  surface-bright: '#fbf9f8'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f5f3f3'
  surface-container: '#efeded'
  surface-container-high: '#eae8e7'
  surface-container-highest: '#e4e2e2'
  on-surface: '#1b1c1c'
  on-surface-variant: '#3e494a'
  inverse-surface: '#303030'
  inverse-on-surface: '#f2f0f0'
  outline: '#6e797a'
  outline-variant: '#bdc9ca'
  surface-tint: '#006970'
  primary: '#006067'
  on-primary: '#ffffff'
  primary-container: '#007b83'
  on-primary-container: '#d0fbff'
  inverse-primary: '#7ad5dd'
  secondary: '#315ea2'
  on-secondary: '#ffffff'
  secondary-container: '#8bb4fe'
  on-secondary-container: '#0e4487'
  tertiary: '#5f5546'
  on-tertiary: '#ffffff'
  tertiary-container: '#786d5d'
  on-tertiary-container: '#fff1df'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#96f1fa'
  primary-fixed-dim: '#7ad5dd'
  on-primary-fixed: '#002022'
  on-primary-fixed-variant: '#004f54'
  secondary-fixed: '#d7e3ff'
  secondary-fixed-dim: '#abc7ff'
  on-secondary-fixed: '#001b3f'
  on-secondary-fixed-variant: '#114689'
  tertiary-fixed: '#f0e0cc'
  tertiary-fixed-dim: '#d3c4b1'
  on-tertiary-fixed: '#221a0e'
  on-tertiary-fixed-variant: '#4f4537'
  background: '#fbf9f8'
  on-background: '#1b1c1c'
  surface-variant: '#e4e2e2'
typography:
  headline-lg:
    fontFamily: Manrope
    fontSize: 30px
    fontWeight: '700'
    lineHeight: 38px
  headline-md:
    fontFamily: Manrope
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-sm:
    fontFamily: Manrope
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-medical:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '700'
    lineHeight: 16px
    letterSpacing: 0.05em
  label-cap:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  margin-mobile: 16px
  margin-desktop: 32px
  gutter: 16px
  stack-sm: 4px
  stack-md: 12px
  stack-lg: 24px
---

## Brand & Style
The design system is built on the pillars of **clinical precision** and **empathetic care**. It targets veterinary professionals who require high-speed data entry and clear diagnostic visibility, balanced with an interface that feels approachable to pet owners. 

The aesthetic is **Corporate / Modern** with a lean toward **Minimalism**. By stripping away unnecessary ornamentation, the design system prioritizes medical data clarity while using a warm, natural accent palette to reduce the "coldness" often associated with medical software. The emotional response is one of organized tranquility—reassuring the user that the environment is both high-tech and high-touch.

## Colors
The palette uses a "Trust-Warmth" ratio. 

*   **Primary (Soft Teal):** Used for primary actions, success states, and brand identifiers. It suggests a modern, hygienic clinical environment.
*   **Secondary (Medical Blue):** Reserved for technical data, navigation, and professional headers to anchor the UI in reliability.
*   **Tertiary (Sand):** A warm, grounding accent used for backgrounds of cards or "soft" callouts to break the clinical monotony and provide a welcoming feel.
*   **Medical Alerts:** High-contrast Red (#D32F2F) and Amber (#F59E0B) are used strictly for critical health alerts and vitals.

## Typography
This design system utilizes **Manrope** for headlines to provide a modern, slightly rounded, and technical appearance. **Inter** is utilized for all functional text and medical data due to its exceptional legibility at small sizes and neutral character.

Medical data points (weight, temperature, heart rate) should always use the `label-medical` style to ensure high-contrast visibility against lighter backgrounds. Mobile headers are capped at 30px to maintain vertical space for patient records.

## Layout & Spacing
The layout follows a **Fluid Grid** model optimized for mobile-first veterinary workflows. 

*   **Mobile:** 4-column layout with 16px side margins. Elements typically stack vertically to maximize scanability of patient history.
*   **Tablet/Desktop:** 12-column layout. The UI shifts to a multi-pane view where the list of patients remains pinned to the left, and the medical record expands in the center.
*   **Rhythm:** An 8px linear scale is used for all padding and margins. Vertical rhythm is critical in patient charts; use `stack-md` for related data points and `stack-lg` to separate distinct medical sections.

## Elevation & Depth
To maintain a clean and professional look, this design system avoids heavy shadows. Instead, it utilizes **Tonal Layers** and **Low-contrast Outlines**:

1.  **Surface Tiers:** The main background is a very light grey or sand. Cards containing patient data use a pure white surface.
2.  **Ghost Borders:** Containers are defined by a 1px border (#E0E0E0) rather than a shadow to keep the interface looking "flat" and organized.
3.  **Active Elevation:** Only primary action buttons and floating "Add Record" buttons receive a soft, ambient shadow (8px blur, 10% opacity, tinted with the Secondary Blue) to signify interactability.

## Shapes
The design system employs a **Rounded** language (8px / 0.5rem) to convey friendliness and safety. 

*   **Small Components:** Checkboxes and small tags use a 4px radius.
*   **Standard Components:** Buttons, Input Fields, and Cards use the 8px base radius.
*   **Feature Components:** Large pet avatars and floating action buttons use 100% (pill-shaped) rounding to distinguish them as high-priority interactive elements.

## Components
*   **Buttons:** Primary buttons are solid Soft Teal with white text. Secondary buttons use a teal outline. For "Emergency" actions, a high-visibility red is permitted.
*   **Animal Type Chips:** Distinctive icons (minimalist line art) for Dogs, Cats, and Exotics. When selected, the chip background should change to a light version of the Primary color with a darkened icon.
*   **Medical Data Cards:** High-contrast white cards with a "Sand" left-border accent for regular checkups, and a "Medical Blue" border for surgical or critical history.
*   **Input Fields:** Bottom-aligned labels with 1px borders. Focused states use a 2px Medical Blue bottom border to highlight active data entry.
*   **Status Indicators:** Small, circular dots next to patient names. Green for "In Clinic," Yellow for "Pending Results," and Grey for "Discharged."
*   **Vitals Tracker:** A specialized horizontal scrolling component for mobile to track historical weight or temperature trends without occupying excessive vertical space.