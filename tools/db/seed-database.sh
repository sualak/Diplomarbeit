#!/bin/bash

# bash tools/db/seed-database.sh shopping-portal src/main/resources/fixtures 10
# bash tools/db/seed-database.sh shopping-portal-test src/test/resources/fixtures 10

DB_NAME=$1
FIXTURES_PATH=$2
AMOUNT=$3

./gradlew dropDatabase -PdbName="$DB_NAME"
./gradlew dropFixtures -PfixturesPath="$FIXTURES_PATH"
./gradlew createCollections -PdbName="$DB_NAME"
./gradlew generateFixtures -PfixturesPath="$FIXTURES_PATH" -Pamount="$AMOUNT"
./gradlew importFixtures -PdbName="$DB_NAME" -PfixturesPath="$FIXTURES_PATH"
