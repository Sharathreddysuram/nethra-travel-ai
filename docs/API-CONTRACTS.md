# Nethra Travel AI — API Contracts

Base URL (via gateway): `http://localhost:8080`. The frontend's nginx proxies `/api/` and
`/ai/` to the gateway, so the same paths work on `http://localhost:3000`.
All money values are INR with 2 decimal places. Errors use the shared `ApiError` shape:
`{ "timestamp": ..., "status": ..., "error": ..., "path": ... }` (Spring Boot default error
body for unhandled errors; validation errors return 400).

## ai-planner-service

### POST /ai/plan
Runs the full agentic workflow. Always HTTP 200 for a well-formed request — an infeasible
trip is a **successful** response with `feasible=false`, not an error.

Request:
```json
{ "budgetInr": 15000, "sourceCity": "Hyderabad", "days": 4, "transportMode": "FLIGHT", "travelers": 1 }
```
`transportMode` ∈ `FLIGHT | TRAIN | BUS | CAR`. Validation: budget ≥ 1000, days 1–30,
travelers 1–10, source city required (400 otherwise).

Feasible response (abridged — Hyderabad, 3 days, TRAIN, ₹15,000, verified by
`TravelGraphIntegrationTest.feasibleTrainTripStaysWithinBudget`):
```json
{
  "feasible": true,
  "destination": { "id": "bengaluru", "name": "Bengaluru", "...": "..." },
  "transportOption": { "mode": "TRAIN", "providerName": "Bharat Rail Mock", "price": 0.00, "durationHours": 0.0 },
  "hotel": { "name": "...", "pricePerNight": 0.00 },
  "partnerPackage": null,
  "activities": [ { "name": "...", "price": 0.00 } ],
  "itinerary": { "days": [ { "dayNumber": 1, "slots": [ { "slot": "MORNING", "activityName": "..." } ] } ] },
  "costBreakdown": { "TRANSPORT": 0.00, "HOTEL": 0.00, "FOOD": 0.00, "ACTIVITIES": 0.00, "EMERGENCY_BUFFER": 1200.00 },
  "totalCost": 0.00,
  "remainingBudget": 0.00,
  "summary": "deterministic template summary (or OpenAI-enhanced when OPENAI_API_KEY is set)",
  "guideHighlights": ["guide excerpt retrieved by the RAG component"],
  "agentTrace": [ { "agent": "BudgetAnalysisAgent", "decision": "ALLOCATED", "detail": "..." } ],
  "minimumRequiredBudget": null, "reason": null,
  "alternativeTransportModes": [], "alternativeDestinations": [], "suggestions": []
}
```
Guarantees asserted by tests for the feasible case: destination **Bengaluru**, itinerary has
exactly **3 days**, `totalCost ≤ 15000`, `EMERGENCY_BUFFER = 1200.00` (8% of budget),
`remainingBudget = budget − totalCost`.

Rejection response — the spec example (Hyderabad, 4 days, FLIGHT, ₹15,000), verified by
`TravelGraphIntegrationTest.specExampleHyderabadFlightIsRejected`:
```json
{
  "feasible": false,
  "destination": null, "transportOption": null, "hotel": null, "partnerPackage": null,
  "activities": [], "itinerary": null, "costBreakdown": {}, "totalCost": 0.00,
  "minimumRequiredBudget": 15196.00,
  "reason": "No destination is reachable from Hyderabad by FLIGHT for 4 day(s) within ₹15,000. The cheapest option (Mysuru) needs at least ₹15,196 including the 8% emergency buffer.",
  "alternativeTransportModes": [
    { "transportMode": "TRAIN", "minimumRequiredBudget": 0.00, "cheapestDestinationName": "..." },
    { "transportMode": "BUS",   "minimumRequiredBudget": 0.00, "cheapestDestinationName": "..." },
    { "transportMode": "CAR",   "minimumRequiredBudget": 0.00, "cheapestDestinationName": "..." }
  ],
  "alternativeDestinations": [
    { "destinationName": "Mysuru", "minimumRequiredBudget": 15196.00 }
  ],
  "suggestions": ["Try a cheaper transport mode", "Shorten the trip", "Increase the budget to at least the minimum shown"],
  "agentTrace": [ { "agent": "FeasibilityAgent", "decision": "REJECTED", "detail": "..." } ]
}
```
The minimum is computed, not hard-coded: cheapest destination Mysuru — transport ₹5,280 +
lodging/activities ₹5,100 + food ₹3,600 = ₹13,980, grossed up by the 8% buffer
(÷ 0.92) = **₹15,196**. Tests assert: `feasible=false`, `itinerary=null`,
`minimumRequiredBudget > 15000`, a TRAIN alternative is present, and alternatives and
suggestions are non-empty.

### GET /ai/tools
MCP-style tool registry: JSON array of `{ name, description, inputSchema }` for `plan_trip`
and the supporting tools (destination ranking, minimum estimation, budget allocation).

### GET /ai/health
`{ "status": "UP", "service": "ai-planner-service" }` style health payload.

### GET /ai/plans
Persisted plans created by this planner instance (JPA/H2), newest first.

## user-service — /api/users

| Endpoint | Body / params | Response |
|---|---|---|
| POST /register | `{name, email, password}` | `AuthResponse { token, user }` |
| POST /login | `{email, password}` | `AuthResponse { token, user }` — demo account: `demo@nethra.ai / password123` |
| GET /me | header `Authorization: Bearer <jwt>` | `UserProfile` |
| POST /validate | `{token}` | validity + subject |

Passwords are BCrypt-hashed; tokens are JWT (jjwt 0.12.x, HS256).

## budget-service — /api/budget

### POST /allocate
```json
{ "budgetInr": 15000, "days": 4, "travelers": 1, "transportMode": "FLIGHT" }
```
→ (verified live against the running service)
```json
{ "totalBudget": 15000.00, "spendableBudget": 13800.00, "emergencyBuffer": 1200.00,
  "allocations": { "TRANSPORT": 5400.00, "HOTEL": 4050.00, "FOOD": 2400.00,
                   "ACTIVITIES": 1950.00, "EMERGENCY_BUFFER": 1200.00 },
  "assumptions": ["..."] }
```

### POST /precheck
Same request → `BudgetPrecheckResponse { feasible, minimumRequiredBudget, cheapestDestinationName, message }`
computed with `TripCostEstimator` for the requested source/mode/days.

### GET /health — service health payload.

## destination-service — /api/destinations

| Endpoint | Description |
|---|---|
| GET / | All 14 destination profiles. |
| GET /{id} | One destination (e.g. `goa`, `kochi`, `mysuru`). |
| GET /{id}/guide | Guide text used by the RAG store. |
| GET /rank?sourceCity&transportMode&budgetInr&days&travelers | Ranked destinations with scores (value-for-money weighted by budget fit, distance cost, rating, days fit) and per-destination minimum estimates; infeasible ones are flagged, not hidden. |
| GET /minimums?sourceCity&transportMode&days&travelers | `DestinationMinimum` list for every destination (used by the planner's FeasibilityAgent). |

## transport-service — /api/transport

| Endpoint | Description |
|---|---|
| GET /options?sourceCity&destinationId&mode&travelers&date | Options from the mode's mock provider (price, duration, departure window). Deterministic per date. |
| GET /cheapest?... | Cheapest option for the mode. |
| GET /providers | Provider names per mode (all labeled Mock*). |

## hotel-service — /api/hotels

GET /?destinationId&maxPricePerNight → deterministic mock hotels for the destination,
priced by destination cost index and budget tier, sorted by value score.

## activity-service — /api/activities

| Endpoint | Description |
|---|---|
| GET /?destinationId | Curated activities (6 per destination) with price, duration, category. |
| GET /categories | Distinct activity categories. |

## partner-service — /api/partners

GET /packages?destinationId → partner packages (7 across Goa, Manali, Jaipur, Kochi, Udaipur,
Darjeeling, Mysuru) with `pricePerNight`, `tourBundlePrice` and a computed multi-day quote.

## itinerary-service — /api/itineraries

POST /plan `{destinationName, days, activities:[...]}` → `ItineraryPlan` with `DayPlan[]`;
each day has MORNING/AFTERNOON/EVENING slots; arrival/departure days are lighter.

## notification-service — /api/notifications

| Endpoint | Description |
|---|---|
| POST / | `{recipient, channel: EMAIL|SMS|ALERT, subject, message}` → stored + mock-sent (logged), returns the record with status. |
| GET /history | All notifications, newest first. The planner posts here after a plan completes; failures are tolerated and never fail the plan. |
