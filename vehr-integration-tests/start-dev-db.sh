#!/bin/bash
# little dev helper script to start pgdb container and migrate db in order to start tests

docker run -d -it --rm --name="pg-ethercis" -e POSTGRES_PASSWORD=postgres -p 5432:5432 serefarikan/ethercis-pg:v1

cd ../../ehrservice
./gradlew db:flywayMigrate