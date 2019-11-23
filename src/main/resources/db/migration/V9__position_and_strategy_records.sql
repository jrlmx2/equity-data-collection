CREATE table strategies(
                           id ulid default generate_ulid(),
                           created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at timestamp,
                           name varchar(50),
                           details jsonb,
                           margin bool,
                           PRIMARY KEY(id)
);

CREATE table strategies_audit(
                                 id ulid default generate_ulid(),
                                 created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 strategy ulid REFERENCES strategies(id),
                                 name_before varchar(50),
                                 name_after varchar(50),
                                 details_before jsonb,
                                 details_after jsonb,
                                 margin_before bool,
                                 margin_after bool,
                                 PRIMARY KEY(id)
);

CREATE OR REPLACE FUNCTION public.audit_strategy_changes()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO public.strategies_audit (strategy, name_before, name_after, details_before, details_after, margin_before, margin_after) VALUES (new.id, old.name, new.name, old.details, new.details, old.margin, new.margin);

    NEW.updated_at = now();
    return new;
END;
$$;

create TRIGGER update_strategy BEFORE UPDATE
    ON strategies FOR EACH ROW EXECUTE PROCEDURE
    audit_strategy_changes();


CREATE table positions(
                          id ulid default generate_ulid(),
                          created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at timestamp,
                          opened_at timestamp,
                          exited_at timestamp,
                          position_type VARCHAR(15),
                          equity_id varchar(15),
                          equity_type varchar(15),
                          entry_strategy ulid REFERENCES strategies(id),
                          exit_strategy ulid REFERENCES strategies(id),
                          PRIMARY KEY(id)
);

create TRIGGER update_position_changetimestamp BEFORE UPDATE
    ON positions FOR EACH ROW EXECUTE PROCEDURE
    update_changetimestamp_column();

CREATE table position_lots(
                              id ulid default generate_ulid(),
                              executed_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              price float8,
                              exchange_number int4,
                              position_id ulid REFERENCES positions(id),
                              PRIMARY KEY(id)
);

