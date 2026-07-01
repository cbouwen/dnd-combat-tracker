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
A **Campaign** (aggregate root) owns a PC roster and a list of encounter references. Each campaign has a name and a list of **CampaignPlayers** — lightweight PC definitions whose stats (maxHP, AC, initiativeModifier) can change as players level up.

A **CombatEncounter** (aggregate root) belongs to exactly one Campaign and holds a list of **Combatants**, moving through three states: `PREPARING → ACTIVE → ENDED`. Every encounter must be created under a campaign; standalone encounters are not supported. When an encounter is created, the campaign's PC roster is automatically instantiated as full Combatants at max HP.

Combatants have three types: `PC`, `NPC`, `ENEMY`. When starting an encounter, PC initiatives are set by the player; NPCs/Enemies roll automatically (d20 + modifier). Combatants are then sorted by initiative descending, with type as a tiebreaker (PC > NPC > ENEMY).

Multiple monsters with the same name can be added but trying to add a PC with an identical name as one already added will fail and throw an exception.

## API (current)

### Campaign endpoints
- `POST /api/campaigns` → 201 + CampaignResponse (create campaign)
- `GET /api/campaigns` → 200 + List<CampaignResponse> (list all campaigns)
- `GET /api/campaigns/{id}` → 200 + CampaignResponse (get campaign with player roster and encounter IDs)
- `POST /api/campaigns/{id}/players` → 201 + CampaignResponse (add PC to campaign roster)
- `PUT /api/campaigns/{campaignId}/players/{playerId}` → 200 + CampaignResponse (update PC stats, e.g. on level up)
- `DELETE /api/campaigns/{campaignId}/players/{playerId}` → 204 No Content (remove PC from roster)
- `POST /api/campaigns/{id}/encounters` → 201 + CombatEncounterResponse (create encounter, auto-loads PCs from roster)

### Encounter endpoints
- `GET /api/encounters/{id}` → 200 + CombatEncounterResponse (view encounter state)
- `GET /api/encounters` → 200 + List<CombatEncounterResponse> (list all encounters)
- `POST /api/encounters/{id}/combatants` → 201 + CombatantResponse (add combatant)
- `DELETE /api/encounters/{encounterId}/combatants/{combatantId}` → 204 No Content (remove combatant)
- `POST /api/encounters/{id}/start` → 200 + CombatEncounterResponse (start encounter, sets initiatives, sorts)
- `POST /api/encounters/{id}/next-turn` → 200 + CombatEncounterResponse (advance turn)
- `POST /api/encounters/{id}/previous-turn` → 200 + CombatEncounterResponse (go back one turn)
- `POST /api/encounters/{encounterId}/combatants/{combatantId}/damage` → 200 + CombatEncounterResponse (deal damage to combatant)
- `POST /api/encounters/{encounterId}/combatants/{combatantId}/heal` → 200 + CombatEncounterResponse (heal combatant)
- `POST /api/encounters/{encounterId}/end` → 200 + CombatEncounterResponse (end encounter)

## Key Design Decisions
- **Encounters always belong to a campaign** — standalone encounter creation is not supported; every encounter is created via `POST /api/campaigns/{id}/encounters`.
- **`CampaignPlayer` is a mutable template** — stores a PC's permanent stats (maxHP, AC, initiativeModifier); stats can change on level up. HP is not tracked at the campaign level — players manage their own HP between sessions.
- **PCs are instantiated at encounter creation** — each `CampaignPlayer` becomes a fresh `Combatant` (type PC, currentHP = maxHP) when an encounter is created. The encounter then owns that combat state independently.
- **Encounter IDs everywhere** — endpoints use `/encounters/{id}/...` to support multiple concurrent encounters and make multi-user support easier later.
- **`templateId` is metadata** — stored on NPCs/Enemies to link to a monster stat card in the UI; no effect on domain logic.
- **In-memory first** — architecture is clean enough to swap in JPA later without touching the domain.
- **Single-user for now** — no authentication yet; multi-user is a known future concern.

## Current State

### TODO — Campaign feature

#### Domain layer
- [x] Create `CampaignPlayer` value object: `id`, `name`, `maxHP`, `ac`, `initiativeModifier`
- [x] Create `Campaign` aggregate root: `id`, `name`, `List<CampaignPlayer>`, `List<String> encounterIds`
  - [x] `Campaign.create(String id, String name)` factory method
  - [x] `addPlayer(CampaignPlayer)` — adds PC to roster
  - [x] `removePlayer(String playerId)` — removes PC from roster
  - [x] `updatePlayer(CampaignPlayer)` — replaces existing player by ID (for level-up stat changes)
  - [x] `addEncounterId(String encounterId)` — called when a new encounter is created
- [x] Add `campaignId: String` field to `CombatEncounter`
- [x] Update `CombatEncounter.create()` and `createWithCombatants()` to require `campaignId`

#### Application layer
- [ ] Create `CampaignRepositoryPort` interface: `save`, `findById`, `findAll`, `clear`
- [ ] Create `CampaignNotFoundException` (application exception)
- [ ] Create `CreateCampaignCommand` record: `name`
- [ ] Create `AddCampaignPlayerCommand` record: `campaignId`, `name`, `maxHP`, `ac`, `initiativeModifier`
- [ ] Create `UpdateCampaignPlayerCommand` record: `campaignId`, `playerId`, `maxHP`, `ac`, `initiativeModifier`
- [ ] Create `CreateCampaignUseCase` — generates UUID, creates Campaign, persists
- [ ] Create `GetCampaignUseCase` — fetch by ID, throw `CampaignNotFoundException` if missing
- [ ] Create `GetAllCampaignsUseCase` — return all campaigns
- [ ] Create `AddPlayerToCampaignUseCase` — fetch campaign, add CampaignPlayer, persist
- [ ] Create `UpdateCampaignPlayerUseCase` — fetch campaign, replace player stats, persist
- [ ] Create `RemovePlayerFromCampaignUseCase` — fetch campaign, remove player, persist
- [ ] Create `CreateEncounterInCampaignUseCase` — fetch campaign, instantiate each `CampaignPlayer` as a `Combatant` (PC, currentHP = maxHP), create encounter with campaignId, register encounter ID on campaign, persist both
- [ ] Delete `CreateEncounterUseCase` (standalone encounters no longer supported)

#### Infrastructure layer
- [ ] Create `CampaignRepositoryAdapter` (in-memory HashMap, implements `CampaignRepositoryPort`)
- [ ] Create `CampaignPlayerResponse` DTO record: `id`, `name`, `maxHP`, `ac`, `initiativeModifier`
- [ ] Create `CampaignResponse` DTO record: `id`, `name`, `List<CampaignPlayerResponse>`, `List<String> encounterIds`
- [ ] Create `CreateCampaignRequest` DTO record: `name` — with `toCommand()` method
- [ ] Create `AddCampaignPlayerRequest` DTO record: `name`, `maxHP`, `ac`, `initiativeModifier` — with `toCommand(campaignId)` method
- [ ] Create `UpdateCampaignPlayerRequest` DTO record: `maxHP`, `ac`, `initiativeModifier` — with `toCommand(campaignId, playerId)` method
- [ ] Create `CampaignController` at `/api/campaigns` with all campaign endpoints
- [ ] Add `campaignId` to `CombatEncounterResponse`
- [ ] Add `CampaignNotFoundException` → 404 mapping in `RestExceptionHandler`
- [ ] Remove `POST /api/encounters` endpoint from `CombatEncounterController`

#### Tests
- [ ] `CampaignTest` — domain unit tests (create, add/remove/update player, add encounter ID)
- [ ] `CreateCampaignUseCaseTest`
- [ ] `AddPlayerToCampaignUseCaseTest`
- [ ] `UpdateCampaignPlayerUseCaseTest`
- [ ] `CreateEncounterInCampaignUseCaseTest` — verify PCs are loaded from roster at maxHP with correct stats

### TODO — Infrastructure
- [ ] JPA + PostgreSQL persistence

### Future / deferred
- [ ] Spring Security + authentication
- [ ] Add `dmId` to campaigns, filter by authenticated user
- [ ] Monster template database
- [ ] ElasticSearch
- [ ] Logging + grafana