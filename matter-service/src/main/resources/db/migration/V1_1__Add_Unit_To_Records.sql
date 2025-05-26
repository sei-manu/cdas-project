ALTER TABLE data_record
    RENAME COLUMN int_value TO numeric_value;

ALTER TABLE data_record
    ALTER COLUMN numeric_value TYPE DOUBLE PRECISION USING numeric_value::DOUBLE PRECISION,
    ADD COLUMN unit_of_measurement TEXT;

ALTER TABLE datapoint
    ADD COLUMN unit_of_measurement TEXT;