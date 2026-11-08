# Nethra Travel AI — Local Development

## Prerequisites

* JDK 21 (the build sandbox keeps one at `~/workspace/tools/jdk`)
* Maven 3.9+ (`~/workspace/tools/maven` in the sandbox)
* Node 20+ and npm for the frontend
* Docker + Docker Compose for the container route (optional)

No database, message broker or API key is required for local development: every service
defaults to embedded H2, all travel inventory is in-repo mock data, and the LLM summary is
optional.

## Option A — Docker Compose (whole platform)

```bash
docker compose up --build
# frontend:  http://localhost:3000
# gateway:   http://localhost:8080
```

## Option B — Run services locally

Build once from the repo root:

```bash
mvn -q -DskipTests package
```

Start services in this order (each is a plain Spring Boot jar; ports in parentheses):
budget (8082), destination (8083), transport (8084), hotel (8085), activity (8086),
partner (8087), itinerary (8088), notification (8090), then ai-planner (8089),
user (8081) and api-gateway (8080).

```bash
java -jar budget-service/target/budget-service-1.0.0.jar
# ... repeat per service ...
java -jar ai-planner-service/target/ai-planner-service-1.0.0.jar
java -jar api-gateway/target/api-gateway-1.0.0.jar
```

The planner finds its peers at `http://localhost:<port>` by default; override with
`BUDGET_SERVICE_URL`, `DESTINATION_SERVICE_URL`, `TRANSPORT_SERVICE_URL`, `HOTEL_SERVICE_URL`,
`ACTIVITY_SERVICE_URL`, `PARTNER_SERVICE_URL`, `ITINERARY_SERVICE_URL`,
`NOTIFICATION_SERVICE_URL`. The gateway uses `USER_SERVICE_URL`, `AI_PLANNER_SERVICE_URL`,
etc. the same way.

> Sandbox note: the development sandbox used for this project intercepts direct TCP sockets
> opened by JVM processes (an egress control), so in that environment services are verified
> individually and the planner graph is verified by its mocked-Feign integration tests. On a
> normal workstation or in Docker Compose, inter-service Feign calls work as configured.

## Frontend

```bash
cd frontend
npm install
npm run dev     # Vite dev server on 5173
npm run build   # production build to dist/ (used by the Docker image)
```

`VITE_API_URL` sets the API base (default: same-origin, expecting the nginx proxy or the
gateway at `http://localhost:8080`).

## Try it

```bash
# the spec example — expect feasible=false, minimumRequiredBudget 15196
curl -s -X POST http://localhost:8080/ai/plan -H 'Content-Type: application/json' \
  -d '{"budgetInr":15000,"sourceCity":"Hyderabad","days":4,"transportMode":"FLIGHT","travelers":1}'

# a feasible trip
curl -s -X POST http://localhost:8080/ai/plan -H 'Content-Type: application/json' \
  -d '{"budgetInr":15000,"sourceCity":"Hyderabad","days":3,"transportMode":"TRAIN","travelers":1}'

# login with the seeded demo user
curl -s -X POST http://localhost:8080/api/users/login -H 'Content-Type: application/json' \
  -d '{"email":"demo@nethra.ai","password":"password123"}'
```

## Tests

```bash
mvn -q test
```

* `budget-service` — `BudgetServiceTest` (3 tests): allocation sums to the budget, buffer is
  exactly 8%, splits are normalized.
* `destination-service` — `DestinationServiceTest` (4 tests): catalog completeness, scoring
  order, minimum estimates.
* `ai-planner-service` — `TravelGraphIntegrationTest` (3 tests, Feign clients mocked):
  the spec rejection (Hyderabad/4d/FLIGHT/₹15,000 → minimum ₹15,196, TRAIN alternative),
  a feasible train trip (Bengaluru, 3 itinerary days, total ≤ budget, buffer ₹1,200),
  and partner-package priority for Goa (hotel client verified never called);
  `RagServiceTest` (1 test): guide retrieval returns relevant excerpts.

## PostgreSQL profile

Each service ships `application-postgres.yml`; run with
`--spring.profiles.active=postgres` and `DB_URL` / `DB_USERNAME` / `DB_PASSWORD` set.
DDL for all tables is in `db/schema.sql`.

## Optional: OpenAI summaries

Set `OPENAI_API_KEY` (and optionally `OPENAI_MODEL`, default `gpt-4o-mini`) for the planner
only. Without a key the planner uses deterministic template summaries — everything else is
identical.
