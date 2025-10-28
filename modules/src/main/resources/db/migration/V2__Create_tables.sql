CREATE SCHEMA IF NOT EXISTS thy;

CREATE TABLE thy.locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    location_code VARCHAR(5) NOT NULL,
    location_type VARCHAR(20) NOT NULL,
    deleted       BOOLEAN      DEFAULT FALSE NOT NULL
);

CREATE TABLE thy.transportations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    origin_location_id UUID NOT NULL,
    destination_location_id UUID NOT NULL,
    transportation_type VARCHAR(20) NOT NULL,
    deleted       BOOLEAN      DEFAULT FALSE NOT NULL,
    FOREIGN KEY (origin_location_id) REFERENCES thy.locations(id),
    FOREIGN KEY (destination_location_id) REFERENCES thy.locations(id)
);

CREATE TABLE thy.transportation_operating_days (
    transportation_id UUID NOT NULL,
    day_of_week INTEGER NOT NULL,
    deleted       BOOLEAN      DEFAULT FALSE NOT NULL,
    PRIMARY KEY (transportation_id, day_of_week),
    FOREIGN KEY (transportation_id) REFERENCES thy.transportations(id) ON DELETE CASCADE
);

CREATE INDEX idx_locations_code ON thy.locations(location_code);
CREATE INDEX idx_transportations_origin ON thy.transportations(origin_location_id);
CREATE INDEX idx_transportations_destination ON thy.transportations(destination_location_id);
CREATE INDEX idx_transportations_type ON thy.transportations(transportation_type);
CREATE INDEX idx_operating_days_transportation ON thy.transportation_operating_days(transportation_id);

CREATE UNIQUE INDEX unique_active_location_code
ON thy.locations (location_code)
WHERE deleted = false;