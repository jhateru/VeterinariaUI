# Agent Instructions

## Core Rules
- **Agent Directory**: All agent-related configs, skills, or memory MUST reside in `.agents/`.

## Constraints
- **Databases**: NEVER delete, purge, or overwrite `*.sqlite`, `*.sqlite3`, `*.db`, `*.mdf`.
- **Secrets**: FORBIDDEN to read/modify/print `.env`, `secrets.json`, or PKI files (`*.pem`, `*.key`, `*.crt`, `*.p12`).

## Project Overview
- **Tech Stack**: Java 11, JavaFX (OpenJFX 13), Maven.
- **Package**: `society`.
- **Main Class**: `society.App`.

## Development Workflow
- **Build & Run**: `mvn clean javafx:run`
- **Source Code**: `veterui/src/main/java/`
- **Resources (FXML, CSS)**: `veterui/src/main/resources/`

## Conventions
- **Controller Pattern**:
  - Location: `society.controller`
  - Sub-packages: `principales/` (main views) and `reutilizables/` (reusable components).
  - Mapping: One controller per FXML file, linked via `fx:controller`.
- **Testing**: Use `tdd` skill for new features.

## Domain Context
- Detailed business rules/entities: `docs/domain/*.md`
