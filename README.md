# Surgery Booking Integration Microservice

![workflow status](https://github.com/smartoperatingblock/surgery-booking-integration-microservice/actions/workflows/build-and-deploy.yml/badge.svg)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Version](https://img.shields.io/github/v/release/smartoperatingblock/surgery-booking-integration-microservice?style=plastic)

[![codecov](https://codecov.io/gh/SmartOperatingBlock/surgery-booking-integration-microservice/branch/main/graph/badge.svg?token=7GL0gAUkQp)](https://codecov.io/gh/SmartOperatingBlock/surgery-booking-integration-microservice)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-booking-integration-microservice&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-booking-integration-microservice)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-booking-integration-microservice&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-booking-integration-microservice)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-booking-integration-microservice&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-booking-integration-microservice)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-booking-integration-microservice&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-booking-integration-microservice)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-booking-integration-microservice&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-booking-integration-microservice)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-booking-integration-microservice&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-booking-integration-microservice)

Microservice responsible for communication between our system and a third-party system that collects surgery bookings

## Usage
You need to specify the following environment variable:
- `AZURE_CLIENT_ID`: ID of an Azure AD application
- `AZURE_TENANT_ID`: ID of the application's Azure AD tenant
- `AZURE_CLIENT_SECRET`: the application's client secrets
- `AZURE_DT_ENDPOINT`: the Azure Digital Twins instance endpoint

If you want to run it via docker container:
1. Provide a `.env` file with all the environment variable described above
2. Run the container with the command:
   ```bash
    docker run ghcr.io/smartoperatingblock/surgery-booking-integration-microservice:latest
    ```
   1. If you want to try the REST-API from the external you need to provide a port mapping to port 3000.
   2. If you want to pass an environment file whose name is different from `.env` use the `--env-file <name>` parameter.
