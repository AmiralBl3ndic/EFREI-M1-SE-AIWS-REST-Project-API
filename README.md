# Application Interoperability with Web Services REST Project | REST API

This repository contains the code of the AIWS REST Project for the team composed of:
- Camille **BRIAND** (M1 SE)
- Elodie **DEHACHE** (M1 SE)
- Jérémi **FRIGGIT** (M1 IRV)
- Jules **LAGNY** (M1 SE)
- Zoé **NIDDAM** (M1 IRV)

## Running the project

The project has been developed using **Tomcat 9.0.26** but has also been tested for **WildFly 18** and **Glassfish 5**.

### Install the Maven dependencies

This project relies on a few dependencies (for clearer code, or for features like keywords filtering), so you have to install them
and include the libraries in the produced artifact (otherwise, *ClassNotFoundException* will be thrown by you application server).

The following dependencies were used:

- **Jersey** and **Jackson**: JAX-RS Implementation and JSON POJO converter
- **HikariCP**: Connection pooling library, used for managing database connection pools
- **java-jwt**: [JSON Web Tokens](https://jwt.io/) management
- **lombok**: Clearer POJOs thanks to useful annotations
- **bcrypt**: Encryption (for password encryption and validation)
- **fuzzywuzzy**: Fuzzy search features, used for keywords filtering
- **mysql-connector-java**: JDBC driver for MySQL

**WARNING**: be sure to include the dependencies jars in the artifact deployed to your application server, otherwise the API won't work

### Connect to the database of your choice

The project is already set to work with our online MySQL database, but if you want to connect your own database, follow these steps

**WARNING**: These steps assumes that the database you are willing to connect to has already been created thanks to the `web/WEB-INF/sql/sql_script.sql` SQL script

1. Open the `web/WEB-INF/db.properties` file: this file contains the properties to connect to the database of your choice
2. Replace the `jdbcUrl` value with the JDBC connection string to your database
3. Replace the `dbUser` value with the user you want to access your database with, this user must at least have the rights to select, insert, update and delete rows
4. Replace the `dbPassword` value with the user's password
5. Replace the `jdbcDriver` value with the classname (complete classname) of the JDBC driver to use to connect to your database

**Disclaimer**: These steps are important, if you do not follow them, the application will not be able to access your database and will likely crash at startup

### Deploy the artifact to the application server of your choice

Although the project has been developed to work well within a **Tomcat 9.0** environment, it should work as well withing **WildFly 18** and **Glassfish 5** environment

## Endpoints

The exposed REST api includes the following endpoints

- **/api/auth** For authentication purposes
- **/api/users** For actions related to *user*s
- **/api/users/{userId}** For actions related to a single *user*
- **/api/books** For actions related to *book*s
- **/api/books/{bookId}** For actions related to a single *book*
- **/api/books/{bookId}/comments** For actions related to *comment*s on a *book*
- **/api/books/{bookId}/comments/{commentId}** For actions related to a single *book* *comment*
- **/api/dvds** For actions related to *dvd*s
- **/api/dvds/{dvdId}** For actions related to a single *dvd*
- **/api/dvds/{dvdId}/comments** For actions related to *comment*s on a *dvd*
- **/api/dvds/{dvdId}/comments/{commentId}** For actions related to a single *dvd* *comment*
- **/api/video-games** For actions related to *video-game*s
- **/api/video-games/{videoGameId}** For actions related to a single *video-game*
- **/api/video-games/{videoGameId}/comments** For actions related to *comment*s on a *video-game*
- **/api/video-games/{videoGameId}/comments/{commentId}** For actions related to a single *video-game* *comment* 

## Testing

This project does not include unit testing, but all the endpoints have been tested thanks to Postman and its integrated test runner.

## Frontend

We have developed a frontend application using Facebook's React.
Once ready, we will include a link here.
