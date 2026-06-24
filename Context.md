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
- `POST /api/encounters` → 201 + CombatEncounterResponse (creates empty encounter in SETUP)
- `GET /api/encounters/{id}` → 200 + CombatEncounterResponse (view encounter state)
- `POST /api/encounters/{id}/combatants` → 201 + CombatantResponse (add combatant)
- `DELETE /api/encounters/{encounterId}/combatants/{combatantId}` → 204 No Content (remove combatant)
- `POST /api/encounters/{id}/start` → 200 + CombatEncounterResponse (start encounter, sets initiatives, sorts)
- `POST /api/encounters/{id}/next-turn` → 200 + CombatEncounterResponse (advance turn)
- `POST /api/encounters/{id}/previous-turn` → 200 + CombatEncounterResponse (go back one turn)
- `POST /api/encounters/{encounterId}/combatants/{combatantId}/damage` → 200 + CombatEncounterResponse (deal damage to combatant)
- `POST /api/encounters/{encounterId}/combatants/{combatantId}/heal` → 200 + CombatEncounterResponse (heal combatant)
- `POST /api/encounters/{encounterId}/end` → 200 + CombatEncounterResponse (end encounter)

## Key Design Decisions
- **Encounter IDs everywhere** — endpoints use `/encounters/{id}/...` to support multiple concurrent encounters and make multi-user support easier later.
- **`templateId` is metadata** — stored on NPCs/Enemies to link to a monster stat card in the UI; no effect on domain logic.
- **In-memory first** — architecture is clean enough to swap in JPA later without touching the domain.
- **Single-user for now** — no authentication yet; multi-user is a known future concern.

## Current State

### TODO


### Next endpoints to build
- [ ] `GET /api/encounters` - List all encounters

### Future / deferred
- [ ] Spring Security + authentication
- [ ] Add `dmId` to encounters, filter by authenticated user
- [ ] JPA + PostgreSQL persistence
- [ ] Monster template database
- [ ] ElasticSearch
- [ ] Logging + grafana