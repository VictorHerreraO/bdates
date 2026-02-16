# Branching & Commit Strategy

This document outlines the standard workflow for all contributors to ensure code quality, traceability, and a clean repository history.

---

## Protected Branches

The following branches are **protected**. Direct pushes are strictly disabled; all changes must be introduced via a Pull Request (PR) and pass all required status checks.

* **`main`**: Represents the stable, production-ready state of the software.
* **`develop`**: The primary integration branch for the next release cycle.

---

## Branching Convention

To keep the repository organized, all non-protected branches must use the following naming conventions:

| Branch Prefix  | Purpose                                                         |
| :------------- | :-------------------------------------------------------------- |
| **`feature/`** | New implementations of user stories or functional enhancements. |
| **`bugfix/`**  | Addressing code issues, UI glitches, or logic errors.           |
| **`hotfix/`**  | Urgent fixes applied to production (branched from `main`).      |

> **Note:** Always branch off of `develop` for features and bugfixes. Code should be pushed into these dedicated branches instead of the protected branches.

---

## Commit Guidelines

To maintain a searchable history, every commit must be linked to a tracking issue.

### Format
Every commit message must start with the issue number followed by a colon:
`#<issue_number>: <Short, descriptive summary>`

* **Example:** `#34: Implement user authentication logic`

### AI Co-Authoring
If an AI agent is used to generate or significantly refactor code, include a **Co-authored-by** trailer at the end of the commit message.

```text
#102: Refactor database schema for scalability

Co-authored-by: Gemini <gemini@google.com>
```

---

## Pull Request (PR) Requirements

Before a PR can be merged into a protected branch, it must satisfy the following criteria:

- Mandatory Review: At least one (1) approved review from a designated code owner or senior developer is required.
- Status Checks: All automated CI/CD pipelines, including unit tests and linting, must pass.
- No Conflicts: The branch must be up-to-date with the target branch (develop or main) and free of merge conflicts.
- Documentation: Any new features must include updated documentation or inline comments where applicable.
- Issue Linking: The PR description must use keywords (e.g., "Closes #34") to automatically link and close the relevant issue upon merging.

### AI Disclosure & Attribution

To maintain transparency and help reviewers focus on high-impact areas, any use of AI agents or automation tools in the creation of a PR must be disclosed.

When to Disclose: If an AI agent was used to generate code, perform significant refactoring, or automate the PR creation process.

Format: The AI agent should include its details as a footnote at the bottom of the PR description.

Example PR Footnote:

```text
This PR was partially generated with the assistance of [AI Name/Tool]. Significant logic in auth_service.py was refactored by the agent.
```

---

Last Updated: February 2026