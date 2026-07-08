# Cricket-Team-Lineup-Editor

[![Java CI with Maven](https://github.com/junaid0000/Cricket-Team-Lineup-Editor/actions/workflows/maven.yml/badge.svg)](https://github.com/junaid0000/Cricket-Team-Lineup-Editor/actions/workflows/maven.yml)
[![Coverage Status](https://coveralls.io/repos/github/junaid0000/Cricket-Team-Lineup-Editor/badge.svg?branch=master)](https://coveralls.io/github/junaid0000/Cricket-Team-Lineup-Editor?branch=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=junaid0000_Cricket-Team-Lineup-Editor&metric=alert_status)](https://sonarcloud.io/project/overview?id=junaid0000_Cricket-Team-Lineup-Editor)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=junaid0000_Cricket-Team-Lineup-Editor&metric=vulnerabilities)](https://sonarcloud.io/component_measures?metric=vulnerabilities&id=junaid0000_Cricket-Team-Lineup-Editor)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=junaid0000_Cricket-Team-Lineup-Editor&metric=bugs)](https://sonarcloud.io/component_measures?metric=bugs&id=junaid0000_Cricket-Team-Lineup-Editor)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=junaid0000_Cricket-Team-Lineup-Editor&metric=code_smells)](https://sonarcloud.io/component_measures?metric=code_smells&id=junaid0000_Cricket-Team-Lineup-Editor)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=junaid0000_Cricket-Team-Lineup-Editor&metric=coverage)](https://sonarcloud.io/component_measures?metric=coverage&id=junaid0000_Cricket-Team-Lineup-Editor)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=junaid0000_Cricket-Team-Lineup-Editor&metric=duplicated_lines_density)](https://sonarcloud.io/component_measures?metric=duplicated_lines_density&id=junaid0000_Cricket-Team-Lineup-Editor)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=junaid0000_Cricket-Team-Lineup-Editor&metric=sqale_index)](https://sonarcloud.io/component_measures?metric=sqale_index&id=junaid0000_Cricket-Team-Lineup-Editor)

Project Overview: The application will be a simple  Cricket Team Lineup 
Editor  . It will manage a single main entity:
**Player**: playerId, name, jerseyNumber, role

**Main Features:**
Add a new player
Edit an existing player's details or role
Update an existing player's details or role 
Delete a player
 
**Tools & Technologies:**
Java (version 17 ]).
MongoDB (managed via Docker/Testcontainers).
Maven for build automation.
JUnit 4 for unit testing.
Mockito for mocking (used appropriately without mocking 3rd party types).
PIT for mutation testing.
JaCoCo and Coveralls for 100% code coverage.
SonarCloud for code quality (no technical debt).
GitHub Actions for continuous integration.
Swing-based user interface.

## Build And Test

```bash
mvn verify
```

This runs unit tests, integration tests, and end-to-end tests.

## Coverage

```bash
mvn verify -Pjacoco
```
