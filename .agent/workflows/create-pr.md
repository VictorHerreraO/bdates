---
description: How to create a Pull Request following project standards
---

# Create Pull Request Workflow

Follow these steps to ensure your changes are correctly branched, committed, and submitted as a Pull Request.

## 1. Verify Issue Number
Before proceeding, ensure you have a GitHub issue number for the task you are working on.
- If you don't have one, **ask the user for the issue number** or create one.
- This number is required for commit messages (e.g., `#123`).

## 2. Check Your Branch
Check which branch you are currently on. 
- **Protected Branches**: `main`, `master`, `develop`, or any `epic/` branch.
- If you are on a protected branch, you **MUST** create a new branch for your changes.

### Switch to a New Branch (if needed)
If you have pending changes on a protected branch, move them to a new branch:
```bash
git checkout -b feature/<issue-number>-<short-description>
# OR
git checkout -b bugfix/<issue-number>-<short-description>
# OR
git checkout -b hotfix/<issue-number>-<short-description>
```
*Note: Prefix your branch as per `CONTRIBUTING.md` guidelines.*

## 3. Commit Pending Changes
Commit any pending changes in small, logical batches.
- **Commit Format**: `#<issue_number>: <Short, descriptive summary>`
- **AI Attribution**: If you are an AI agent, include the co-author trailer:
  ```text
  Co-authored-by: Gemini <gemini@google.com>
  ```

## 4. Create the Pull Request
Once all changes are committed and pushed to your feature/bugfix branch, create the PR using the `gh` CLI.

### Preparation
1. **Push your branch**: `git push -u origin <your-branch-name>`
2. **Locate the Template**: Use the template at `.github/pull_request_template.md`.

### Execution
Run the following command to create the PR:
```bash
gh pr create --title "#<issue_number>: <PR Title>" --body-file .github/pull_request_template.md --draft
```
*Note: Adding `--draft` is recommended for initial review. Remove it if you want to publish immediately.*

## 5. Verify the PR
After creation, verify that:
- The PR links to the issue (e.g., "Closes #123" in the body).
- The template fields are populated.
- Any AI assistance is disclosed at the bottom of the description.

> [!IMPORTANT]
> Never push directly to `develop` or `main`. Always use this PR workflow.
