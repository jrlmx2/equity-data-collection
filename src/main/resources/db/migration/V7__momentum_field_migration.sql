alter table equity_momentum drop column score;
alter table equity_momentum add column score float8 not null;
alter table equity_momentum add column fip float8 not null;