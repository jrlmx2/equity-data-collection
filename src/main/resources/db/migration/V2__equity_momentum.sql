-- Drop table

-- DROP TABLE public.equity_momentum;

CREATE TABLE public.equity_momentum (
    id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    symbol varchar(15) NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    strategy varchar(20) NOT NULL,
    score float8 NULL,
    CONSTRAINT position_action_pk PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE public.equity_momentum OWNER TO postgres;
GRANT ALL ON TABLE public.equity_momentum TO postgres;
