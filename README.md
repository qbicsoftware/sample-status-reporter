<div align="center">

# LIMS Sample Status Reporter

<i>A command line tool to report LIMS changes to the sample-tracking service</i>.

[![Build Maven Package](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/build_package.yml/badge.svg)](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/build_package.yml)
[![Run Maven Tests](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/run_tests.yml/badge.svg)](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/run_tests.yml)
[![CodeQL](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/codeql-analysis.yml)
[![release](https://img.shields.io/github/v/release/qbicsoftware/sample-status-reporter?include_prereleases)](https://github.com/qbicsoftware/sample-status-reporter/releases)

[![license](https://img.shields.io/github/license/qbicsoftware/sample-status-reporter)](https://github.com/qbicsoftware/sample-status-reporter/blob/main/LICENSE)
![language](https://img.shields.io/badge/language-groovy,%20java-blue.svg)
![framework](https://img.shields.io/badge/framework-spring-blue.svg)

</div>

## System Integration

As updating sample statuses in more then one place can lead to errors and frustration, automation of
this process is deemed important. The `sample-status-reporter` automates the migration of updated
sample statuses from an openBis LIMS to the
[sample-tracking-service](https://github.com/qbicsoftware/sample-tracking-service).

**Interaction with the sample-tracking-service**

To propagate information to the sample-tracking service, the app queries the following endpoints:

* `GET /locations`
* `PUT /samples/{sampleCode}/currentLocation/`

Please see
the [sample-tracking-service SwaggerHub](https://app.swaggerhub.com/apis-docs/qbic/sample-tracking/)
entry for further detail.

**Integration with the openBIS LIMS**

The sample information is retrieved automatically from an openBIS instance acting as LIMS. The
configuration of the openBIS needs to make sure each sample provides a QBiC barcode (containing `Q`) and a sample status.
Furthermore, the names of the properties providing this information needs to be provided (see [Environment Variables](#environment-variables)).

* The sample status may contain values
  from `["Sample received", "QC passed", "QC failed", "Library completed"]`.

This tool acts in the role of a user configured by you. Please make sure, that the configured user
only sees projects where you want to propagate the status to the sample-tracking system.

## How to run

To run the application, first make sure you provide a correct [configuration](#configuration).

Checkout the latest code from `main` and run the maven goal `spring-boot:run`:

```bash
mvn spring-boot:run [-Dspring-boot.run.arguments=[-hV],[-t=<timePoint>]]
  -h, --help      Show this help message and exit.
  -t, --time-point=<timePoint>
                  Point in time from where to search for updates e.g. '2022-01-01T00:00:00Z'.
                  Defaults to the last successful run. 
                  If never run successfully defaults to the same time yesterday.
  -V, --version   Print version information and exit.
```

Alternatively you can package the application and run it as `.jar`. First compile the project and
build an executable java archive:

```shell
mvn clean package
```

The JAR file will be created in the ``/target`` folder:

```
|-target
|---sample-status-reporter-1.0.0.jar
|---...
```

Now change into the folder and run the REST service app with:

```shell
java -jar sample-status-reporter-1.0.0.jar [-hV] [-t=<timePoint>]
  -h, --help      Show this help message and exit.
  -t, --time-point=<timePoint>
                  Point in time from where to search for updates e.g. '2022-01-01T00:00:00Z'.
                  Defaults to the last successful run. 
                  If never run successfully defaults to the same time yesterday.
  -V, --version   Print version information and exit.
```

## Configuration

Please note that this project requires `java 17`.

#### Environment Variables

For this application to be run the following environment variables need to be set:

| Environment Variable                | Description                                                                                   | Default Value                          |
|-------------------------------------|-----------------------------------------------------------------------------------------------|----------------------------------------|
| `LAST_UPDATE_FILE`                  | A path to a persistent file. The last successful run is stored here.                          | `last-updated.txt `                    |
| `LIMS_PASSWORD`                     | The password to access the OpenBiS LIMS                                                       |                                        |
| `LIMS_SERVER_URL`                   | The URL to the OpenBiS LIMS API                                                               |                                        |
| `LIMS_USER`                         | The user to access the OpenBiS LIMS                                                           |                                        |
| `LIMS_BARCODE_PROPERTY`             | The name of the property in the OpenBiS LIMS from which to read the QBiC barcode              |                                        |
| `LIMS_STATUS_PROPERTY`              | The name of the property in the OpenBis LIMS from which to read the sample status information |                                        |
| `SAMPLE_TRACKING_AUTH_PASSWORD`     | The password for the sample tracking user                                                     | `astrongpassphrase! `                  |
| `SAMPLE_TRACKING_AUTH_USER`         | The username for the sample tracking service                                                  | `qbic`                                 |
| `SAMPLE_TRACKING_LOCATION_ENDPOINT` | The endpoint to list all locations. This does not contain the base url                        | `/locations`                           |
| `SAMPLE_TRACKING_LOCATION_USER`     | The sample tracking user currently using the application                                      | `my_email@example.com`                 |
| `SAMPLE_TRACKING_URL`               | The base URL for the sample tracking service                                                  | `http://localhost.de`                  |
| `USER_DB_DIALECT`                   | The database dialect of the user database                                                     | `org.hibernate.dialect.MariaDBDialect` |
| `USER_DB_DRIVER`                    | The database driver for the user database                                                     | `com.mysql.cj.jdbc.Driver`             |
| `USER_DB_HOST`                      | The URL to the host of the user database containing the database name                         | `localhost`                            |
| `USER_DB_USER_NAME`                 | The database user name                                                                        | `myusername`                           |
| `USER_DB_USER_PW`                   | The database user password                                                                    | ` astrongpassphrase!`                  |
