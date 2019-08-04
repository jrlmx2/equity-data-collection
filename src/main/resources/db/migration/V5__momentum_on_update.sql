ALTER TABLE equity_momentum RENAME COLUMN created_at TO updated_at;
commit;

CREATE OR REPLACE FUNCTION update_changetimestamp_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_equity_momentum_changetimestamp BEFORE UPDATE
    ON equity_momentum FOR EACH ROW EXECUTE PROCEDURE
    update_changetimestamp_column();