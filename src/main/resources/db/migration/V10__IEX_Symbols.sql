create table iex_symbols (
    symbol primary key text,
    companyName text,
    exchange text,
    industry text,
    website text,
    description text,
    CEO text,
    securityName text,
    issueType text,
    sector text,
    primarySicCode integer,
    employees integer,
    tags text[],
    address text,
    address2 text,
    state text,
    city text,
    zip text,
    country text,
    phone text
)