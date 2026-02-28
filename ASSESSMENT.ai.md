# BDates Project Assessment: Local Multi-Circle Pivot

## 1. Executive Summary
The BDates project is currently in a hybrid state with a fully functional local "Device Circle" and a partially implemented Firebase backend. This assessment outlines the strategy to pivot the application to a **100% Local-Only** experience, enabling users to create and manage multiple social circles (e.g., Family, Work, Friends) entirely on-device.

---

## 2. Technical Audit

### ✅ Reusable Architecture (High Value)
The project's Clean Architecture and modular structure make this pivot straightforward.
- **Relational Integrity:** The `Event` and `EventEntity` models already contain a `circleId` field.
- **Database Schema:** The Room database schema for `CircleEntity` and `EventEntity` already supports multi-circle storage without modification.
- **Dependency Injection:** Hilt is used to manage dependencies, allowing for easy swapping or removal of modules.

### 🗑️ Obsolete Components (To be removed)
- **Backend:** The entire `/functions` directory (TypeScript/Firebase).
- **Authentication:** All logic related to JWT, user registration, and login.
- **Remote Data Sources:** Any partial implementations designed to fetch data from the Firebase REST API.

---

## 3. Artifact Mapping

### GitHub Project Context
- **Project Name:** BDates
- **Project ID:** `PVT_kwHOAOp6OM4AJ7nB` (Project Number 1)

### Related Issues & Project Items
| ID        | Title                                | Status     | Relation to Pivot                                       |
| :-------- | :----------------------------------- | :--------- | :------------------------------------------------------ |
| **#4**    | EPIC: Circles functionality          | Todo       | **Primary Focus:** Redefine as local-only management.   |
| **#27**   | Defect: Mixed languages              | Open       | **Critical:** Affects UI labels across all circles.     |
| **#18**   | Task: remote circles in app          | **Closed** | Deprecated. PR #23 closed without merging.              |
| **#1**    | EPIC: Add unit tests for viewmodels  | Todo       | **Quality:** Ensure new local logic is tested.          |
| **Draft** | Task: split app into feature modules | Todo       | **Architecture:** Will help isolate local circle logic. |
| **Draft** | Task: refactor navigation            | Todo       | **UX:** Necessary for navigating between circles.       |

### 🔒 Vaulted Items (Deprecated Backend/Remote)
The following items have been moved to the **Vault** column of the project board as they are no longer within scope:
- **#14** EPIC: Add backend
- **#15** Task: infrastructure for circles functionality
- **#16** Task: infrastructure for user + auth functionality
- **#17** Task: optimize events response by using timestamps
- **#19** Bug: updating an event doesn't update the circle update date
- **Draft** Task: Add Push Notifications

### Core Classes for Pivot
| Class                 | Path                                                      | Role                                            |
| :-------------------- | :-------------------------------------------------------- | :---------------------------------------------- |
| `Circle`              | `.../modules/circles/domain/model/Circle.kt`              | Domain model for circles.                       |
| `Event`               | `.../modules/eventList/domain/model/Event.kt`             | Domain model for events (linked by `circleId`). |
| `CircleDao`           | `.../modules/circles/data/datasource/local/CircleDao.kt`  | Room DAO for local circle CRUD.                 |
| `CircleRepository`    | `.../modules/circles/data/repository/CircleRepository.kt` | Repository managing local circle data flow.     |
| `CreateCircleUseCase` | `.../modules/circles/domain/CreateCircleUseCase.kt`       | Generic use case for creating circles.          |

---

## 4. Detailed Roadmap (Sprint Breakdown)

### Sprint 1: Multi-Circle Foundation (In Progress)
*Goal: Infrastructure to support more than one circle.*
- [x] **#34 Task 1.1**: Refactor `CreateLocalCircleUseCase` to generic `CreateCircleUseCase`.
- [x] **#35 Task 1.2**: Update `CircleRepository` and `DAO` for CRUD (Update/Delete).
- [x] **#36 Task 1.3**: Unit tests for Multi-Circle domain logic.

### Sprint 2: Circle Manager UI
*Goal: CRUD interfaces for circle management.*
- [ ] **#37 Task 2.1**: Implement "My Circles" management screen.
- [ ] **#38 Task 2.2**: Implement "Add Circle" dialog/form.
- [ ] **#39 Task 2.3**: Implement "Delete Circle" with confirmation (cascade warning).

### Sprint 3: Circle Context & Selection
*Goal: Linking events to specific circles.*
- [ ] **#40 Task 3.1**: Add "Circle Selector" to `AddEventBottomSheet`.
- [ ] **#41 Task 3.2**: Refactor `EventListViewModel` to support filtering by `circleId`.

### Sprint 4: Unified View & Cleanup
*Goal: Polish and removal of legacy code.*
- [ ] **#42 Task 4.1**: Implement "Global View" (Aggregated chronological list from all circles).
- [ ] **#43 Task 4.2**: **Cleanup:** Delete `/functions` folder and remove Firebase/Auth dependencies.
- [ ] **#27 Bug Fix**: Resolve mixed languages issue in the new UI.

---

## 5. Development Progress
- **Active Branch:** `feature/multi-circle-foundation`
- **Current Milestone:** Sprint 1

## 6. Risk Assessment
- **Data Loss:** Care must be taken during the removal of "Remote" code to ensure local database migrations don't wipe existing local events.
- **UI Complexity:** Adding a "Circle Selector" to a small mobile screen requires careful UX design to maintain simplicity.

---
*Last Updated: Friday, February 27, 2026*
