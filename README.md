# LIMS Sample Status Reporter

[![CodeQL](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/codeql-analysis.yml/badge.svg?branch=main)](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/codeql-analysis.yml)
[![Latest Release ](https://img.shields.io/github/v/release/qbicsoftware/sample-status-reporter.svg)](https://github.com/qbicsoftware/sample-status-reporter/releases)
![Groovy Language](https://img.shields.io/badge/language-groovy-blue.svg)

## System setup

For this application to be run the following environment variables need to be set:

| Name                                | Description                                                            |
|-------------------------------------|------------------------------------------------------------------------|
| `LAST_UPDATE_FILE`                  | A path to a persistent file. The last successful run is stored here.   |
| `LIMS_PASSWORD`                     | The password to access the OpenBiS LIMS                                |
| `LIMS_SERVER_URL`                   | The URL to the OpenBiS LIMS API                                        |
| `LIMS_USER`                         | The user to access the OpenBiS LIMS                                    |
| `SAMPLE_TRACKING_AUTH_PASSWORD`     | The password for the sample tracking user                              |
| `SAMPLE_TRACKING_AUTH_USER`         | The username for the sample tracking service                           |
| `SAMPLE_TRACKING_LOCATION_ENDPOINT` | The endpoint to list all locations. This does not contain the base url |
| `SAMPLE_TRACKING_LOCATION_USER`     | The sample tracking user currently using the application               |
| `SAMPLE_TRACKING_URL`               | The base URL for the sample tracking service                           |
| `USER_DB_DIALECT`                   | The database dialect of the user database                              |
| `USER_DB_DRIVER`                    | The database driver for the user database                              |
| `USER_DB_HOST`                      | The URL to the host of the user database containing the database name  |
| `USER_DB_USER_NAME`                 | The database user name                                                 |
| `USER_DB_USER_PW`                   | The database user password                                             |

## Run the app

Checkout the latest code from `main` and run the Maven goal `spring-boot:run`:

```
mvn spring-boot:run [-Dspring-boot.run.arguments=[-hV],[-t=<timePoint>]]
  -h, --help      Show this help message and exit.
  -t, --time-point=<timePoint>
                  Point in time from where to search for updates e.g. '2022-01-01T00:00:00Z'.
                  Defaults to the last successful run. 
                  If never run successfully defaults to the same time yesterday.
  -V, --version   Print version information and exit.
```



