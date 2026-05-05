# Agents

This file provides context and configuration for AI agents working on this repository.

## Project Overview
**Name:** VeterinariaUI
**Description:** A JavaFX-based user interface for veterinary management systems.
**Tech Stack:**
- **Language:** Java 11
- **UI Framework:** JavaFX (OpenJFX)
- **Build Tool:** Maven
- **Package Name:** `society`

## Agent Skills
The following skills are available to agents. Use them to perform specialized tasks.

- **Changelog Generator**: Generate release notes from commits or feature lists.
- **brainstorming**: Explore requirements and design before implementation.
- **find-skills**: Search for available skills in the environment.
- **systematic-debugging**: Methodical, hypothesis-driven bug fixing.

## Domain Context
*Note: Detailed domain documentation (business rules, entities, etc.) should be maintained in `docs/domain/*.md`.*

## Development Workflow

### Build and Run
To run the application using Maven:
```bash
mvn clean javafx:run
```

### Project Structure
- **Source Code:** `veterui/src/main/java/`
- **Resources (FXML, CSS, Images):** `veterui/src/main/resources/`
- **Build Output:** `veterui/target/`

## Conventions
- Follow standard Java and JavaFX naming conventions.
- UI components are defined in FXML and controlled by Java classes in the `society` package.
- All new features should ideally be accompanied by tests (using the `tdd` skill).
