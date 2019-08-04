alter table equity_momentum drop column score;
alter table equity_momentum add column score JSONB not null;