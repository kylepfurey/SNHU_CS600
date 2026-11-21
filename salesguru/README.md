# Sales Guru+ Setup
by Kyle Furey

## Database Initialization

```sh
psql -U postgres -f SalesGuru-Init-Database.sql
psql -U postgres -f SalesGuru-Init-Auth-Database.sql
psql -U postgres -f SalesGuru-Migrate-Database.sql
```

Migration is required. The original database creates `invoice.customer_number` as `bigint`, and `cost`, `retail` and `discount` as `real`. The entities now use `integer` and `numeric`, and `ddl-auto=validate` fails otherwise.

These changes were explicitly added. Relational keys should have matching types, and currency shouldn't be stored as a `float`. More on this in the paper.

To reset, drop both databases (`DROP DATABASE salesguru; DROP DATABASE salesguru_auth;`) and rerun all three scripts in order.

## Configuration
All settings come from `SALESGURU_*` environment variables (see `init/RunSalesGuru.sh`). The values in that script are only for development. In production, they are stored securely on the server.

## Test & Run

```sh
cd init
./RunSalesGuru.sh
```

https://localhost:8443/ (login with cs600:cs600)
