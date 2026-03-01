Closes #3

## Description

This PR implements the requested localization pivot and notification string optimizations:

- **Language Pivot**: English is now the default language in `res/values/`. Spanish strings have been moved to `res/values-es/`.
- **String Formatting**: Corrected formatting in English notification strings to match the specifications in Issue #3 (fixed quotes and leading spaces).
- **Plurals Optimization**: Consolidated notification strings directly into `plurals.xml` for both locales, improving maintainability and fixing resource duplication issues.
- **Cleanup**: Removed the redundant `res/values-en-rUS/` directory.

## Brain Documentation
As requested, including the internal brain documentation for this task:
- [Implementation Plan](.tasks/3/implementation_plan.md)
- [Verification Walkthrough](.tasks/3/walkthrough.md)
- [Task Log](.tasks/3/task.md)

## Screenshots

![Proof of added event](https://github.com/VictorHerreraO/bdates/blob/feature/3-localize-notifications/screenshots/event_added_proof.png?raw=true)

## Docs

Verified with emulator testing (Pixel 9 Pro) and a successful build/test run.

## Ready?

- [x] Documented what's new
- [x] Added in-code documentation (wherever needed)
- [x] Wrote tests for new components/features
- [x] Ran the linter to ensure style guidelines were followed
- [x] Created a demo

---
Co-authored-by: Gemini <gemini@google.com>
