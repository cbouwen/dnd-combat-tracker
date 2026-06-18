# D&D Combat Tracker — Project Context

## Goal
Learning project to practice DDD, hexagonal (ports & adapters) architecture, and Spring Boot. The app tracks D&D combat encounters: initiative order, turn management, HP tracking.

## Tech Stack
Java 17, Spring Boot 4.0.6, Maven, in-memory storage (no database yet).

## Architecture
Three layers, dependencies point inward only:
- **Domain** — pure business logic, no framework dependencies
- **Application** — use cases, ports (interfaces), commands, exceptions
- **Infrastructure** — Spring adapters: REST controllers, in-memory repository, request/response objects

## Domain Model
A **CombatEncounter** (aggregate root) holds a list of **Combatants** and moves through three states: `PREPARING → ACTIVE → ENDED`.

Combatants have three types: `PC`, `NPC`, `ENEMY`. When starting an encounter, PC initiatives are set by the player; NPCs/Enemies roll automatically (d20 + modifier). Combatants are then sorted by initiative descending, with type as a tiebreaker (PC > NPC > ENEMY).

Multiple monsters with the same name can be added but trying to add a PC with an identical name as one already added will fail and throw an exception.

## API (current)
- `POST /api/encounters` — create a new encounter
- `POST /api/encounters/{id}/combatants` — add a combatant to an encounter

## Key Design Decisions
- **Encounter IDs everywhere** — endpoints use `/encounters/{id}/...` to support multiple concurrent encounters and make multi-user support easier later.
- **`templateId` is metadata** — stored on NPCs/Enemies to link to a monster stat card in the UI; no effect on domain logic.
- **In-memory first** — architecture is clean enough to swap in JPA later without touching the domain.
- **Single-user for now** — no authentication yet; multi-user is a known future concern.

## Current State

### Next endpoints to build
- [x] `GET /api/encounters/{id}` — view encounter state
- [x] `POST /api/encounters/{id}/start` — start encounter
- [x] `POST /api/encounters/{id}/next-turn` — advance turn
- [x] `POST /api/encounters/{id}/previous-turn` — previous turn
- [x] `DELETE /api/encounters/{id}/combatants/{combatantId}` — remove combatant

### Future / deferred
- [ ] Spring Security + authentication
- [ ] Add `dmId` to encounters, filter by authenticated user
- [ ] JPA + PostgreSQL persistence
- [ ] Monster template database
- [ ] ElasticSearch
- [ ] Logging + grafana