# sb_test (Spring-Boot Test)

## 1. SpringBootBatchDemo

## Environment configuration

Sensitive credentials are no longer stored in the repository.  Copy the
provided `.env.example` files to `.env` and supply real values or export the
variables in your shell before running Docker or the application:

```
cp .env.example .env
cp SpringBootBatchDemo/.env.example SpringBootBatchDemo/.env
```

The following variables must be provided:

- `DOCKER_MYSQL_ROOT_PASSWORD`
- `DOCKER_MYSQL_DATABASE`
- `DOCKER_MYSQL_USER`
- `DOCKER_MYSQL_PASSWORD`
- `INFLUXDB_USERNAME`
- `INFLUXDB_PASSWORD`
- `INFLUXDB_ORG`
- `INFLUXDB_BUCKET`
- `INFLUXDB_ADMIN_TOKEN`

The Spring Boot configuration reads passwords from these environment variables.