CREATE table equity_data(
   url VARCHAR(400) not null,
   chart jsonb not null,
   stats jsonb not null,
   updated TIMESTAMP NOT NULL,
   data_source VARCHAR(20),
   data_source_version VARCHAR(10),
   symbol VARCHAR(15),
   PRIMARY KEY(symbol, data_source, data_source_version)
);

CREATE INDEX symbol_data_source
    ON equity_data (symbol, data_source);