-- Nethra Travel AI — PostgreSQL schema (reference DDL)
-- The default developer profile runs every service on embedded H2 with
-- Hibernate create-drop. This DDL documents the production PostgreSQL shape
-- that the JPA entities in each service map to (profile: postgres).
-- Money is stored as NUMERIC(12,2) in INR. IDs are application-generated UUIDs.

CREATE SCHEMA IF NOT EXISTS nethra;

-- user-service -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.users (
    id            VARCHAR(64) PRIMARY KEY,
    name          VARCHAR(120) NOT NULL,
    email         VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(120) NOT NULL,          -- BCrypt
    role          VARCHAR(32)  NOT NULL DEFAULT 'USER',
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- budget-service -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.budgetplans (
    id             VARCHAR(64) PRIMARY KEY,
    total_budget   NUMERIC(12,2) NOT NULL,
    days           INTEGER NOT NULL,
    travelers      INTEGER NOT NULL DEFAULT 1,
    transport_mode VARCHAR(16) NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE TABLE IF NOT EXISTS nethra.budget_allocations (
    budget_plan_id VARCHAR(64) NOT NULL REFERENCES nethra.budgetplans(id) ON DELETE CASCADE,
    category       VARCHAR(32) NOT NULL,          -- TRANSPORT|HOTEL|FOOD|ACTIVITIES|EMERGENCY_BUFFER
    amount         NUMERIC(12,2) NOT NULL,
    PRIMARY KEY (budget_plan_id, category)
);

-- destination-service ------------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.destinations (
    id                 VARCHAR(64) PRIMARY KEY,   -- slug, e.g. 'goa'
    name               VARCHAR(120) NOT NULL,
    state              VARCHAR(120) NOT NULL,
    rating             NUMERIC(3,1) NOT NULL,
    hotel_base_per_night NUMERIC(12,2) NOT NULL,
    food_per_day       NUMERIC(12,2) NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_destinations_rating ON nethra.destinations (rating DESC);

-- transport-service --------------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.transportquotes (
    id              VARCHAR(64) PRIMARY KEY,
    source_city     VARCHAR(120) NOT NULL,
    destination_name VARCHAR(120) NOT NULL,
    mode            VARCHAR(16) NOT NULL,          -- FLIGHT|TRAIN|BUS|CAR
    price           NUMERIC(12,2) NOT NULL,
    travel_date     DATE NOT NULL
);

-- hotel-service ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.hotels (
    id             VARCHAR(64) PRIMARY KEY,
    destination_id VARCHAR(64) NOT NULL,
    name           VARCHAR(180) NOT NULL,
    star_tier      INTEGER NOT NULL,
    price_per_night NUMERIC(12,2) NOT NULL,
    rating         NUMERIC(3,1) NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_hotels_destination ON nethra.hotels (destination_id);

-- activity-service ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.activitys (
    id             VARCHAR(64) PRIMARY KEY,
    destination_id VARCHAR(64) NOT NULL,
    name           VARCHAR(200) NOT NULL,
    price          NUMERIC(12,2) NOT NULL,
    duration_hours NUMERIC(5,1) NOT NULL,
    category       VARCHAR(80) NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_activities_destination ON nethra.activitys (destination_id);

-- partner-service ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.partnerpackages (
    id               VARCHAR(64) PRIMARY KEY,
    destination_id   VARCHAR(64) NOT NULL,
    name             VARCHAR(200) NOT NULL,
    partner_name     VARCHAR(160) NOT NULL,
    price_per_night  NUMERIC(12,2) NOT NULL,
    tour_bundle_price NUMERIC(12,2) NOT NULL
);

-- itinerary-service --------------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.itinerarys (
    id                  VARCHAR(64) PRIMARY KEY,
    destination_name    VARCHAR(120) NOT NULL,
    days                INTEGER NOT NULL,
    total_activity_cost NUMERIC(12,2) NOT NULL DEFAULT 0,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ai-planner-service -------------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.travelplans (
    id          VARCHAR(64) PRIMARY KEY,
    source_city VARCHAR(120) NOT NULL,
    budget_inr  NUMERIC(12,2) NOT NULL,
    total_cost  NUMERIC(12,2),
    status      VARCHAR(24) NOT NULL,             -- FEASIBLE|REJECTED
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE TABLE IF NOT EXISTS nethra.agent_traces (
    travel_plan_id VARCHAR(64) NOT NULL REFERENCES nethra.travelplans(id) ON DELETE CASCADE,
    seq            INTEGER NOT NULL,
    agent          VARCHAR(80) NOT NULL,
    decision       VARCHAR(80) NOT NULL,
    detail         TEXT NOT NULL,
    PRIMARY KEY (travel_plan_id, seq)
);

-- notification-service -----------------------------------------------------
CREATE TABLE IF NOT EXISTS nethra.notifications (
    id         VARCHAR(64) PRIMARY KEY,
    recipient  VARCHAR(200) NOT NULL,
    channel    VARCHAR(24) NOT NULL,              -- EMAIL|SMS|ALERT
    subject    VARCHAR(240) NOT NULL,
    status     VARCHAR(40) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
