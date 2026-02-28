# Style and Conventions

- **Branching**: Branch from `develop`. Use prefixes `feature/`, `bugfix/`.
- **Commits**: Format is `#<issue_number>: <description>`. Add `Co-authored-by: Gemini <gemini@google.com>` if AI assisted.
- **AI Workspace**: Use `.tasks/{task-id}/` for temporary files (including scripts and text files) instead of `/tmp/`. Use `.ai.md` suffix for markdown. Clean up after task is done.
- **Clean Architecture**: Place files in `domain/`, `data/`, or `framework/` within `modules/`.