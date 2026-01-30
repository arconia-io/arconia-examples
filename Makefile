.PHONY: run

run:
	@if [ -z "$(filter-out $@,$(MAKECMDGOALS))" ]; then \
		echo "Usage: make run '<command>'"; \
		echo "Example: make run 'arconia update gradle'"; \
		exit 1; \
	fi
	@cmd="$(filter-out $@,$(MAKECMDGOALS))"; \
	find . -maxdepth 3 -name "settings.gradle*" -type f | while read -r gradle_file; do \
		project_dir=$$(dirname "$$gradle_file"); \
		echo "Found Gradle project at: $$project_dir"; \
		echo "Running: $$cmd"; \
		(cd "$$project_dir" && eval "$$cmd") || echo "Command failed in $$project_dir"; \
		echo "---"; \
	done

%:
	@: