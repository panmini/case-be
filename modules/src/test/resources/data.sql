CREATE TABLE test_vars
(
    v_transaction_id UUID
);

-- Insert one row of UUIDs
INSERT INTO test_vars (v_transaction_id)
VALUES (gen_random_uuid());

