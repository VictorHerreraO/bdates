# Address Issue #3 Observations & Plurals

This plan addresses the current language discrepancy where the app defaults to Spanish despite requirements being in English, and fixes formatting and pluralization issues in notification strings.

## Proposed Changes

### [Component Name] Localization and Strings

#### [NEW] [strings.xml](file:///Users/victor.herrera/Workspace/bdates/app/src/main/res/values-es/strings.xml)
- Create this file to host the original Spanish strings, making it an explicit translation.

#### [NEW] [plurals.xml](file:///Users/victor.herrera/Workspace/bdates/app/src/main/res/values-es/plurals.xml)
- Create this file for Spanish plural resources.

#### [MODIFY] [strings.xml](file:///Users/victor.herrera/Workspace/bdates/app/src/main/res/values/strings.xml)
- Replace Spanish content with English content.
- Fix formatting issues (extra quotes, leading spaces).
- Ensure placeholders match the code's argument passing logic.

#### [MODIFY] [plurals.xml](file:///Users/victor.herrera/Workspace/bdates/app/src/main/res/values/plurals.xml)
- Move English notification strings directly into the plurals items for cleaner resource management.
- Ensure spec-compliant English text for "one" and "other" quantities.

#### [DELETE] [values-en-rUS](file:///Users/victor.herrera/Workspace/bdates/app/src/main/res/values-en-rUS)
- Remove this directory as English will now be the default in `values/`.

## Verification Plan

### Automated Tests
- Run existing notification tests to ensure logic still holds:
  - `./gradlew test`

### Manual Verification
- Inspect the modified XML files to ensure placeholders (`%s`, `%d`, `%1$s`, etc.) are correctly placed and formatted.
- Verify that standard string resources are correctly migrated between default and Spanish folders.
