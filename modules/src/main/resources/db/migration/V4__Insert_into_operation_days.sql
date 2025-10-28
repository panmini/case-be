-- UBER (TAK → IST): Weekend + Friday only
INSERT INTO thy.transportation_operating_days (transportation_id, day_of_week)
SELECT t.id, gs.day
FROM thy.transportations t
JOIN LATERAL (VALUES (5), (6), (7)) AS gs(day) ON true
WHERE t.transportation_type = 'UBER'
  AND t.origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'TAK');

-- BUS (SUL → IST): Weekdays only
INSERT INTO thy.transportation_operating_days (transportation_id, day_of_week)
SELECT t.id, gs.day
FROM thy.transportations t
JOIN LATERAL generate_series(1, 5) AS gs(day) ON true
WHERE t.transportation_type = 'BUS'
  AND t.origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'SUL');

-- SUBWAY (VIC → LHR): Daily except Sunday
INSERT INTO thy.transportation_operating_days (transportation_id, day_of_week)
SELECT t.id, gs.day
FROM thy.transportations t
JOIN LATERAL generate_series(1, 6) AS gs(day) ON true
WHERE t.transportation_type = 'SUBWAY'
  AND t.origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'VIC');

-- BUS (PAD → LGW): Mon, Tue, Thu, Fri only
INSERT INTO thy.transportation_operating_days (transportation_id, day_of_week)
SELECT t.id, gs.day
FROM thy.transportations t
JOIN LATERAL (VALUES (1), (2), (4), (5)) AS gs(day) ON true
WHERE t.transportation_type = 'BUS'
  AND t.origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'PAD');

-- SUBWAY (LHR → VIC): Daily except Sunday
INSERT INTO thy.transportation_operating_days (transportation_id, day_of_week)
SELECT t.id, gs.day
FROM thy.transportations t
JOIN LATERAL generate_series(1, 6) AS gs(day) ON true
WHERE t.transportation_type = 'SUBWAY'
  AND t.origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LHR');

-- UBER (CDG → PAR): Daily
INSERT INTO thy.transportation_operating_days (transportation_id, day_of_week)
SELECT t.id, gs.day
FROM thy.transportations t
JOIN LATERAL generate_series(1, 7) AS gs(day) ON true
WHERE t.transportation_type = 'UBER'
  AND t.origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'CDG');

-- UBER (JFK → MAN): Weekdays + Saturday
INSERT INTO thy.transportation_operating_days (transportation_id, day_of_week)
SELECT t.id, gs.day
FROM thy.transportations t
JOIN LATERAL generate_series(1, 6) AS gs(day) ON true
WHERE t.transportation_type = 'UBER'
  AND t.origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'JFK');

-- BUS (LAX → LAD): Daily
INSERT INTO thy.transportation_operating_days (transportation_id, day_of_week)
SELECT t.id, gs.day
FROM thy.transportations t
JOIN LATERAL generate_series(1, 7) AS gs(day) ON true
WHERE t.transportation_type = 'BUS'
  AND t.origin_location_id = (SELECT id FROM thy.locations WHERE location_code = 'LAX');