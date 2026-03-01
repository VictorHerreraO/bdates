# Post Task Checklist

Before completing a task, ensure the following:
- Run `./gradlew lint` to ensure no lint warnings.
- Run `./gradlew test` to ensure unit tests pass.
- Make sure the changes follow Clean Architecture guidelines.
- If a pull request, ensure PR has issue linked (e.g., 'Closes #12') and a footprint disclosing AI usage if applicable.
- Clean up temporary files in `.tasks/{task-id}/`.
- Always use the GitHub Pull Request template for the PR body if one exists (e.g., `.github/pull_request_template.md`).