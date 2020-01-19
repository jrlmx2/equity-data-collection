create table iex_symbols (
    symbol text primary key,
    company_name text,
    exchange text,
    industry text,
    website text,
    description text,
    CEO text,
    security_name text,
    issue_type text,
    sector text,
    primary_sic_code integer,
    employees integer,
    tags text[],
    address text,
    address2 text,
    state text,
    city text,
    zip text,
    country text,
    phone text,
    updated_at timestamp
);

CREATE TRIGGER update_iex_symbol_change_timestamp BEFORE UPDATE
    ON iex_symbols FOR EACH ROW EXECUTE PROCEDURE
    update_changetimestamp_column();