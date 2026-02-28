# BDates Project - AI Agent Context

This document provides comprehensive context for AI agents working on the bdates project.

## Project Overview

**bdates** (short for "Cumpleaños" - Birthdays in Spanish) is an Android application designed to track birthdays of important contacts without requiring a social network.

- **Type**: Weekend project / Personal productivity app
- **Platform**: Android (minSdk 26, targetSdk 34)
- **Language**: Kotlin
- **Current Version**: 1.3.0 (versionCode 104)

For more details, see [README.md](README.md).

## Important Documentation

**Critical**: Always. activate the serena project before starting any work.

### Core Documentation
- **[CONTRIBUTING.md](CONTRIBUTING.md)**: Branching strategy, commit conventions, and PR requirements
  - Protected branches: `main` (production) and `develop` (integration)
  - Branch naming: `feature/`, `bugfix/`, `hotfix/`
  - Commit format: `#<issue_number>: <description>`
  - AI co-authoring attribution guidelines
  
- **[ASSESSMENT.ai.md](ASSESSMENT.ai.md)**: Current project state and roadmap
  - **Critical Context**: Project is pivoting from hybrid local/Firebase to 100% local-only
  - Multi-circle functionality being implemented (Family, Work, Friends)
  - Sprint-based roadmap with GitHub project tracking
  - Active branch: `feature/multi-circle-foundation`
  
- **[LICENSE](LICENSE)**: Apache License 2.0

## Technology Stack

### Core Technologies
- **Build System**: Gradle 8.5.0 with Android Gradle Plugin
- **Language**: Kotlin 1.6.21 (JVM Target 17)
- **Architecture**: Clean Architecture with modular structure

### Key Libraries
- **UI Framework**: Jetpack Compose (Material) + View Binding
- **Dependency Injection**: Hilt 2.46.1
- **Database**: Room 2.5.2 (SQLite with KTX extensions)
- **Navigation**: Navigation Component 2.6.0 with Safe Args
- **Async**: Kotlin Coroutines 1.6.2
- **Logging**: Timber 5.0.1

### Testing Frameworks
- **Unit Testing**: JUnit 4.13.2, Mockito 4.6.0, MockK 1.9.3, Truth 1.1.3
- **Instrumented Testing**: AndroidX Test, Espresso 3.5.1
- **Test Utilities**: Coroutines Test, Architecture Core Testing

## Project Structure

```
bdates/
├── app/                          # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/soyvictorherrera/bdates/
│   │   │   │   ├── core/         # Core utilities and base classes
│   │   │   │   └── modules/      # Feature modules
│   │   │   │       ├── circles/  # Circle management (multi-circle)
│   │   │   │       ├── eventList/# Event/birthday listing and management
│   │   │   │       └── notifications/  # Notification system
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                 # Unit tests
│   │   └── androidTest/          # Instrumented tests
│   ├── schemas/                  # Room database schemas
│   └── build.gradle              # App-level build configuration
├── functions/                    # [DEPRECATED] Firebase backend (to be removed)
├── build.gradle                  # Project-level build configuration
├── settings.gradle               # Project settings
└── gradle/                       # Gradle wrapper files
```

### Module Organization

Each feature module follows Clean Architecture:
- **`domain/`**: Business logic, use cases, and domain models
- **`data/`**: Repositories, data sources (local/Room), and entity mappings
- **`framework/`**: UI layer (Compose, ViewModels, Fragments)

## Build Instructions

### Prerequisites
- **JDK**: Version 17 or higher
- **Android SDK**: API 34 (compileSdk)
- **Android SDK Build Tools**: Compatible with Gradle 8.5.0
- **Minimum Android Version**: API 26 (Android 8.0)

### Building the Project

```bash
# Navigate to project directory
cd /Users/victor.herrera/Workspace/bdates

# Clean build
./gradlew clean

# Build debug variant
./gradlew assembleDebug

# Build release variant
./gradlew assembleRelease

# Build all variants
./gradlew build
```

### Build Variants
- **Debug**: `applicationId` = `com.soyvictorherrera.bdates.debug`
  - No minification
  - Includes debug tools
  
- **Release**: `applicationId` = `com.soyvictorherrera.bdates`
  - ProGuard rules defined but minification currently disabled

### Running on Emulator/Device

```bash
# Install debug build on connected device
./gradlew installDebug

# Install and run
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Testing Instructions

### Unit Tests

Unit tests are located in `app/src/test/java/` and test business logic without Android dependencies.

```bash
# Run all unit tests
./gradlew test

# Run unit tests for debug variant only
./gradlew testDebugUnitTest

# Run with coverage (if configured)
./gradlew testDebugUnitTest jacocoTestReport
```

**Key Test Suites:**
- ViewModels: `EventListViewModelTest`, `AddEventViewModelTest`
- Use Cases: `GetEventListUseCaseTest`, `CreateCircleUseCaseTest`
- Repositories: `EventRepositoryTest`, `CircleRepositoryTest`
- Mappers: Entity-to-Model mapping tests
- Utilities: `DateProviderTest`, `KeyValueStoreTest`

### Instrumented Tests

Instrumented tests are located in `app/src/androidTest/java/` and require an Android device or emulator.

```bash
# Run all instrumented tests
./gradlew connectedAndroidTest

# Run on specific device
./gradlew connectedDebugAndroidTest
```

**Key Test Areas:**
- Resource management
- Asset file operations
- Database integration

### Test Utilities
- **TimberTestRule**: JUnit rule for Timber logging in tests
- **MainCoroutineRule**: Coroutine testing with TestDispatcher
- **InMemoryKeyValueStore**: Fake implementation for testing
- **Test Data Factories**: `CircleTestData`, `EventTestData`

### Lint

Android Lint is available for code quality checks:

```bash
# Run lint on default variant
./gradlew lint

# Run lint on specific variant
./gradlew lintDebug
./gradlew lintRelease

# Run lint and apply safe fixes automatically
./gradlew lintFix

# Update lint baseline (suppresses current warnings)
./gradlew updateLintBaseline
```

Lint reports are generated in `app/build/reports/lint-results-*.html`.

## Development Workflow

### Branching Strategy
Always branch off `develop` for new work (see [CONTRIBUTING.md](CONTRIBUTING.md)):

```bash
# Create feature branch
git checkout develop
git pull origin develop
git checkout -b feature/your-feature-name

# Create bugfix branch
git checkout -b bugfix/issue-description
```

### Commit Guidelines
Every commit must reference a GitHub issue:

```bash
git commit -m "#42: Add circle selector to event creation dialog"

# With AI co-authoring
git commit -m "#42: Refactor circle repository for CRUD operations

Co-authored-by: Gemini <gemini@google.com>"
```

### AI Agent Task Organization

When working on a task, AI agents should:

1. **Create a task-specific directory**: `.tasks/{task-id}/` where `{task-id}` corresponds to the GitHub issue number or task identifier
2. **Store temporary files** in this directory during the working session, such as:
   - Planning documents
   - Analysis notes
   - Temporary scripts
   - Debug outputs
3. **Use `.ai.md` suffix** for all temporary markdown files (e.g., `analysis.ai.md`, `plan.ai.md`)
4. **Clean up** task directory after PR is merged or task is complete

**Example structure:**
```
.tasks/
├── 42/
│   ├── implementation-plan.ai.md
│   ├── analysis.ai.md
│   └── migration-script.sh
└── 43/
    └── refactoring-notes.ai.md
```

> **Note**: The `.tasks/` directory should be git-ignored to keep temporary AI working files out of version control.

### Pull Request Requirements
- ✅ At least 1 approved review
- ✅ All CI/CD checks passing
- ✅ No merge conflicts with target branch
- ✅ Issue linking (e.g., "Closes #42")
- ✅ Updated documentation if applicable

### Current Development Status
- **Active Branch**: `develop`
- **Current Sprint**: Sprint 1 - Multi-Circle Foundation
- **Next Milestone**: Circle Manager UI (Sprint 2)

For detailed roadmap, see [ASSESSMENT.ai.md](ASSESSMENT.ai.md).

## Key Files and Locations

### Configuration Files
- **Project Build**: `build.gradle` (root-level with buildscript dependencies)
- **App Build**: `app/build.gradle` (app-level with dependencies and SDK versions)
- **Gradle Settings**: `settings.gradle` (module includes)
- **Gradle Properties**: `gradle.properties` (JVM settings and build options)
- **ProGuard**: `app/proguard-rules.pro` (code obfuscation rules)

### Android Manifest
- **Location**: `app/src/main/AndroidManifest.xml`
- Contains app permissions, activities, services, and components

### Database Schemas
- **Location**: `app/schemas/`
- Room database schema exports for migration tracking

### Core Domain Models
| Model    | Path                                      | Purpose                                  |
| -------- | ----------------------------------------- | ---------------------------------------- |
| `Circle` | `modules/circles/domain/model/Circle.kt`  | Social circle domain model               |
| `Event`  | `modules/eventList/domain/model/Event.kt` | Birthday event with `circleId` reference |

### Key DAOs
| DAO         | Path                                                  | Purpose                      |
| ----------- | ----------------------------------------------------- | ---------------------------- |
| `CircleDao` | `modules/circles/data/datasource/local/CircleDao.kt`  | Local circle CRUD operations |
| `EventDao`  | `modules/eventList/data/datasource/local/EventDao.kt` | Local event CRUD operations  |

## Common Tasks

### Adding a New Feature Module
1. Create package under `app/src/main/java/com/soyvictorherrera/bdates/modules/`
2. Structure with `domain/`, `data/`, and `framework/` layers
3. Add Hilt modules for dependency injection
4. Create corresponding test packages in `app/src/test/`

### Database Migrations
1. Update entity classes in `data/datasource/local/`
2. Increment database version in Room database class
3. Add migration strategy
4. Export schema to `app/schemas/` directory

### Adding Dependencies
Edit `app/build.gradle` and sync:
```gradle
dependencies {
    implementation 'group:artifact:version'
}
```

## Deprecated/Legacy Code

> **⚠️ Warning**: The following components are being phased out as part of the local-only pivot:

- **`/functions/` directory**: Firebase Cloud Functions backend (to be deleted in Sprint 4)
- **Authentication logic**: JWT and user registration (obsolete for local-only)
- **Remote data sources**: Any Firebase REST API integration code

Refer to [ASSESSMENT.ai.md](ASSESSMENT.ai.md) Section 2.2 for the complete list of obsolete components.

---

*Last Updated: February 2026*
