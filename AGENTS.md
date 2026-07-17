# Agent Instructions

## Core Rules
- **Agent Directory**: All agent-related configs, skills, or memory MUST reside in `.agents/`.
- **Module Corrections**: Whenever asked to "correct or fix a module", it means to fix ALL its corresponding MVC files (Model, DAO, Data, Controller), while explicitly NOT editing the `.fxml` file (focusing on programmatic implementations).

## Constraints
- **Databases**: NEVER delete, purge, or overwrite `*.sqlite`, `*.sqlite3`, `*.db`, `*.mdf`.
- **Secrets**: FORBIDDEN to read/modify/print `.env`, `secrets.json`, or PKI files (`*.pem`, `*.key`, `*.crt`, `*.p12`).

## Project Overview
- **Tech Stack**: Java 11, JavaFX (OpenJFX 13), Maven.
- **Package**: `society`.
- **Main Class**: `society.App`.
- **Data Persistence**: Data is stored using JSON format (`.json`) in the `data/` directory.

## Development Workflow
- **Build & Run**: `mvn clean javafx:run`
- **Source Code**: `veterui/src/main/java/`
- **Resources (FXML, CSS)**: `veterui/src/main/resources/`

## Conventions
- **Controller Pattern**:
  - Location: `society.controller`
  - Sub-packages: `principales/` (main views) and `reutilizables/` (reusable components).
  - Mapping: One controller per FXML file, linked via `fx:controller`.
- **View Pattern (Basic)**:
  - Location: `veterui/src/main/java/society/view/`
  - Rule: This folder contains a more basic, simplified version of the JavaFX views compared to the ones in `resources/society`. It must maintain the same structure and file names (as Java classes or simple FXMLs) but with simpler implementations.
- **Testing**: Use `tdd` skill for new features.

## Domain Context
- Detailed business rules/entities: `docs/domain/*.md`
