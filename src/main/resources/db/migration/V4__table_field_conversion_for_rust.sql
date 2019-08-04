ALTER TABLE equity_data ALTER COLUMN data_source TYPE text USING data_source::text;
ALTER TABLE equity_data ALTER COLUMN symbol TYPE text USING symbol::text;
ALTER TABLE equity_data ALTER COLUMN data_source_version TYPE text USING data_source_version::text;
ALTER TABLE equity_data ALTER COLUMN url TYPE text USING url::text;
