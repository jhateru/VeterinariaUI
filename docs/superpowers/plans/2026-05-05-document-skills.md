# Document and Add Installed Skills Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Update `opencode.json` and `AGENTS.md` to include documentation and listings of all currently installed skills.

**Architecture:** Direct modification of configuration and documentation files.

**Tech Stack:** JSON, Markdown.

---

### Task 1: Update AGENTS.md

**Files:**
- Modify: `AGENTS.md:14-21`

- [ ] **Step 1: Read AGENTS.md to verify current content**

- [ ] **Step 2: Replace the existing "Agent Skills" section with the full list of skills and their descriptions**

Skills to include:
- **Changelog Generator**: Generate release notes from commits or feature lists.
- **brainstorming**: Explore requirements and design before implementation.
- **dispatching-parallel-agents**: Use when facing 2+ independent tasks that can be worked on without shared state or sequential dependencies.
- **executing-plans**: Use when you have a written implementation plan to execute in a separate session with review checkpoints.
- **finishing-a-development-branch**: Use when implementation is complete, all tests pass, and you need to decide how to integrate the work.
- **receiving-code-review**: Use when receiving code review feedback.
- **requesting-code-review**: Use when completing tasks or major features to verify work.
- **subagent-driven-development**: Use when executing implementation plans with independent tasks in the current session.
- **systematic-debugging**: Use when encountering any bug, test failure, or unexpected behavior.
- **test-driven-development**: Use when implementing any feature or bugfix.
- **using-git-worktrees**: Use when starting feature work that needs isolation.
- **using-superpowers**: Use when starting any conversation.
- **verification-before-completion**: Use when about to claim work is complete.
- **writing-plans**: Use when you have a spec or requirements for a multi-step task.
- **writing-skills**: Use when creating new skills or verifying skills.

- [ ] **Step 3: Commit the changes to AGENTS.md**

```bash
git add AGENTS.md
git commit -m "docs: update AGENTS.md with full list of skills"
```

### Task 2: Update opencode.json

**Files:**
- Modify: `opencode.json`

- [ ] **Step 1: Read opencode.json to understand current structure**

- [ ] **Step 2: Add the skills to the configuration if appropriate**
(Note: Based on current content, it might just be adding them to a list or if it's a plugin-based config, ensuring they are recognized. Since the user asked to "agregar al opencode.json", I will assume they want them listed there.)

- [ ] **Step 3: Commit the changes to opencode.json**

```bash
git add opencode.json
git commit -m "config: add skills to opencode.json"
```
