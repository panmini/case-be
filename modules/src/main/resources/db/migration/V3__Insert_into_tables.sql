-- Airports
INSERT INTO thy.locations (id, name, country, city, location_code, location_type) VALUES
(gen_random_uuid(), 'Istanbul Airport', 'Turkey', 'Istanbul', 'IST', 'AIRPORT'),
(gen_random_uuid(), 'Sabiha Gokcen Airport', 'Turkey', 'Istanbul', 'SAW', 'AIRPORT'),
(gen_random_uuid(), 'Heathrow Airport', 'United Kingdom', 'London', 'LHR', 'AIRPORT'),
(gen_random_uuid(), 'Gatwick Airport', 'United Kingdom', 'London', 'LGW', 'AIRPORT'),
(gen_random_uuid(), 'Charles de Gaulle Airport', 'France', 'Paris', 'CDG', 'AIRPORT'),
(gen_random_uuid(), 'John F. Kennedy Airport', 'USA', 'New York', 'JFK', 'AIRPORT'),
(gen_random_uuid(), 'Los Angeles Airport', 'USA', 'Los Angeles', 'LAX', 'AIRPORT');

-- City centers and other locations
INSERT INTO thy.locations (id, name, country, city, location_code, location_type) VALUES
(gen_random_uuid(), 'Taksim Square', 'Turkey', 'Istanbul', 'TAK', 'OTHER'),
(gen_random_uuid(), 'Sultanahmet Center', 'Turkey', 'Istanbul', 'SUL', 'OTHER'),
(gen_random_uuid(), 'London Victoria Station', 'United Kingdom', 'London', 'VIC', 'OTHER'),
(gen_random_uuid(), 'London Paddington Station', 'United Kingdom', 'London', 'PAD', 'OTHER'),
(gen_random_uuid(), 'Paris City Center', 'France', 'Paris', 'PAR', 'OTHER'),
(gen_random_uuid(), 'Manhattan Downtown', 'USA', 'New York', 'MAN', 'OTHER'),
(gen_random_uuid(), 'LA Downtown', 'USA', 'Los Angeles', 'LAD', 'OTHER');

-- Flights
INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'IST'),
    (SELECT id FROM thy.locations WHERE location_code = 'LHR'),
    'FLIGHT'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'IST') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LHR'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'IST'),
    (SELECT id FROM thy.locations WHERE location_code = 'CDG'),
    'FLIGHT'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'IST') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'CDG'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'LHR'),
    (SELECT id FROM thy.locations WHERE location_code = 'JFK'),
    'FLIGHT'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LHR') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'JFK'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'CDG'),
    (SELECT id FROM thy.locations WHERE location_code = 'JFK'),
    'FLIGHT'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'CDG') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'JFK'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'SAW'),
    (SELECT id FROM thy.locations WHERE location_code = 'LGW'),
    'FLIGHT'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'SAW') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LGW'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'LGW'),
    (SELECT id FROM thy.locations WHERE location_code = 'LAX'),
    'FLIGHT'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LGW') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LAX'));

-- Before-flight transfers (to airports)
INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'TAK'),
    (SELECT id FROM thy.locations WHERE location_code = 'IST'),
    'UBER'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'TAK') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'IST'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'SUL'),
    (SELECT id FROM thy.locations WHERE location_code = 'IST'),
    'BUS'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'SUL') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'IST'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'VIC'),
    (SELECT id FROM thy.locations WHERE location_code = 'LHR'),
    'SUBWAY'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'VIC') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LHR'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'PAD'),
    (SELECT id FROM thy.locations WHERE location_code = 'LGW'),
    'BUS'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'PAD') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LGW'));

-- After-flight transfers (from airports)
INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'LHR'),
    (SELECT id FROM thy.locations WHERE location_code = 'VIC'),
    'SUBWAY'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LHR') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'VIC'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'CDG'),
    (SELECT id FROM thy.locations WHERE location_code = 'PAR'),
    'UBER'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'CDG') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'PAR'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'JFK'),
    (SELECT id FROM thy.locations WHERE location_code = 'MAN'),
    'UBER'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'JFK') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'MAN'));

INSERT INTO thy.transportations (id, origin_location_id, destination_location_id, transportation_type)
SELECT
    gen_random_uuid(),
    (SELECT id FROM thy.locations WHERE location_code = 'LAX'),
    (SELECT id FROM thy.locations WHERE location_code = 'LAD'),
    'BUS'
WHERE NOT EXISTS (SELECT 1 FROM thy.transportations WHERE origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LAX') AND destination_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LAD'));