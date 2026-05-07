---
name: Precision Tech Retail
colors:
  surface: '#f9f9ff'
  surface-dim: '#d8d9e3'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3fd'
  surface-container: '#ecedf7'
  surface-container-high: '#e6e8f2'
  surface-container-highest: '#e0e2ec'
  on-surface: '#191c23'
  on-surface-variant: '#414754'
  inverse-surface: '#2d3038'
  inverse-on-surface: '#eff0fa'
  outline: '#727785'
  outline-variant: '#c1c6d6'
  surface-tint: '#005bc0'
  primary: '#005bbf'
  on-primary: '#ffffff'
  primary-container: '#1a73e8'
  on-primary-container: '#ffffff'
  inverse-primary: '#adc7ff'
  secondary: '#515f74'
  on-secondary: '#ffffff'
  secondary-container: '#d5e3fc'
  on-secondary-container: '#57657a'
  tertiary: '#9e4300'
  on-tertiary: '#ffffff'
  tertiary-container: '#c55500'
  on-tertiary-container: '#0e0200'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d8e2ff'
  primary-fixed-dim: '#adc7ff'
  on-primary-fixed: '#001a41'
  on-primary-fixed-variant: '#004493'
  secondary-fixed: '#d5e3fc'
  secondary-fixed-dim: '#b9c7df'
  on-secondary-fixed: '#0d1c2e'
  on-secondary-fixed-variant: '#3a485b'
  tertiary-fixed: '#ffdbcb'
  tertiary-fixed-dim: '#ffb691'
  on-tertiary-fixed: '#341100'
  on-tertiary-fixed-variant: '#783100'
  background: '#f9f9ff'
  on-background: '#191c23'
  surface-variant: '#e0e2ec'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 48px
    fontWeight: '700'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.3'
    letterSpacing: -0.01em
  catalog-title:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: '1.4'
    letterSpacing: '0'
  body-main:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.6'
    letterSpacing: '0'
  data-tabular:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.4'
    letterSpacing: '0'
  label-caps:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '700'
    lineHeight: '1'
    letterSpacing: 0.05em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 8px
  sm: 12px
  md: 16px
  lg: 24px
  xl: 32px
  container-max: 1280px
  gutter: 24px
---

## Brand & Style

The design system is engineered for a premium, high-trust electronics retail environment. It balances the efficiency of a B2B productivity tool with the visual appeal of a consumer-facing flagship store. The brand personality is professional, reliable, and technically proficient, evoking an emotional response of confidence and clarity.

The aesthetic follows a **Corporate / Modern** style, utilizing a "clean-room" approach to whitespace. It avoids unnecessary decorative elements in favor of structural integrity, subtle depth, and functional high-fidelity details. The goal is to make complex technical specifications easy to digest while ensuring high-ticket items feel valuable and well-presented.

## Colors

The palette is anchored by a vibrant **Tech Blue**, used purposefully for primary actions and brand presence. The neutral foundation relies on **Deep Slate** for text and structural elements to provide high legibility without the harshness of pure black. 

Backgrounds utilize a very light gray to differentiate the canvas from white surface containers, creating a clear physical distinction between the page and the interactive components. Success and error states use high-saturation "vibrant" tones to ensure critical status updates are immediately visible against the otherwise understated professional palette.

## Typography

This design system uses **Inter** exclusively to maintain a systematic and utilitarian feel across different contexts. The hierarchy is divided into two distinct logical paths:

1.  **Consumer Path:** Uses larger scales and tighter letter spacing for product names and marketing headlines to create impact.
2.  **Data Path:** Utilizes smaller, highly-legible scales for technical specifications and comparison tables. 

Data-heavy views should prioritize vertical density, while catalog views should prioritize generous line heights to enhance the "browsing" experience.

## Layout & Spacing

The layout utilizes a **Fixed Grid** model for desktop views, centering content within a 1280px container to maintain optical focus. The underlying structure is a 12-column grid with 24px gutters.

Spacing follows a strict 4px base unit. Product catalogs should use `lg` (24px) spacing between cards to allow the eye to rest, while data tables and administrative interfaces should compress to `sm` (12px) padding to maximize information density. The header is fixed to the viewport top to ensure navigation is always accessible, while the footer follows the document flow.

## Elevation & Depth

Visual hierarchy is managed through **Ambient Shadows** and tonal layering. Surfaces do not "float" aggressively; instead, they use soft, diffused shadows with low opacity (typically 4-8%) to suggest a slight lift from the light gray background.

- **Level 0 (Background):** #f8f9fa.
- **Level 1 (Cards/Surface):** #ffffff with a 4px blur, 2px Y-offset shadow.
- **Level 2 (Active/Hover):** #ffffff with an 8px blur, 4px Y-offset shadow to indicate interactivity.
- **Level 3 (Modals/Overlays):** #ffffff with a 16px blur, 8px Y-offset for maximum prominence.

Low-contrast outlines (#e2e8f0) are used in conjunction with shadows on interactive inputs to define boundaries without adding visual noise.

## Shapes

The design system employs a **Rounded** shape language to soften the "industrial" nature of electronics. Standard components like buttons and cards use a 0.5rem (8px) radius. Larger containers or featured promotional banners may scale up to a 1rem (16px) radius to feel more approachable. This consistency in rounding helps link disparate elements like technical data tables and lifestyle imagery under a single cohesive identity.

## Components

### Interactive Product Cards
Cards are the primary vehicle for the catalog. They feature a white surface, a 1px border (#e2e8f0), and a subtle shadow. On hover, the shadow intensifies, and the primary blue appears as a subtle accent (e.g., a button or price highlight).

### Fixed Header
A persistent 72px tall bar with a white background and a subtle bottom border. It houses the brand logo, search bar, and utility icons. Search inputs should be high-contrast with a 12px corner radius.

### Notification Banners
Full-width or "toast" style notifications. Success banners use the vibrant green (#00c853) as a left-border accent or background tint with dark text. Error banners use the vibrant red (#d32f2f) to demand immediate attention.

### Buttons & Inputs
Primary buttons are solid Tech Blue with white text. Secondary buttons use a ghost style (slate outline). Inputs use a 12px radius to appear modern and friendly, with a 2px blue focus ring to signify active states.

### Data Tables
For technical specs, use a "zebra-stripe" or clean-line approach. Remove shadows from table rows; use thin #f1f5f9 dividers and the `data-tabular` type style for maximum readability.