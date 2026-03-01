# BDates Project - AI Agent Context

- Always. activate the serena project before starting any work.
- Always branch off `develop` for new work (see [CONTRIBUTING.md](CONTRIBUTING.md)).
- Never create files outside of the current working directory. Not even temporary files.

### AI Agent Task Organization

When working on a task, AI agents should:

1. **Create a task-specific directory**: `.tasks/{task-id}/` where `{task-id}` corresponds to the GitHub issue number or task identifier
2. **Store temporary files** in this directory during the working session, such as:
   - Planning documents
   - Analysis notes
   - Temporary scripts
   - Debug outputs
3. **Use `.ai.md` suffix** for all temporary markdown files (e.g., `analysis.ai.md`, `plan.ai.md`)

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

---

*Last Updated: March 2026*
