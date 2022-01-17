# LIMS Sample Status Reporter 
[![CodeQL](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/codeql-analysis.yml/badge.svg?branch=main)](https://github.com/qbicsoftware/sample-status-reporter/actions/workflows/codeql-analysis.yml)
[![Latest Release ](https://img.shields.io/github/v/release/qbicsoftware/sample-status-reporter.svg)](https://github.com/qbicsoftware/sample-status-reporter/releases)
![Groovy Language](https://img.shields.io/badge/language-groovy-blue.svg)

The LIMS Sample status reporter application is used to report changes in the status of a sample set in the LIMS environment to Sample Tracking Service. 

Overview:

- [Requirements](#requirements)
- [Run the app](#run-the-app)
- [App structure](#app-structure)

# Requirements

To run this app, you need to have version 17 of a **Java JRE** or **JDK** installed (e.g. **Zulu**).

## Run the app

Checkout the latest code from `main` and run the Maven goal `spring-boot:run`:

```
mvn spring-boot:run
```

To run this app 

## App structure

The preliminary app structure is outlined in this UML diagram:
![Bioinformatics Analysis Result Set ER](./docs/Spring%20Boot%20Starter%20Template%20UML.jpg)




