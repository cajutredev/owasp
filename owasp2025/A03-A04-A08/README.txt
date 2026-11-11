SQL Injection Demonstration Application
========================================

The application runs as a Jakarta EE 11 WAR on Open Liberty and uses PostgreSQL.

Database connection
-------------------

The Liberty server configures a JNDI datasource named jdbc/Usuarios with these
connection settings:

  Host: localhost
  Port: 5432
  Database: usuarios
  Username: postgres

Set the database password in the DB_PASSWORD environment variable before
starting Liberty. Do not put the password in source control.

On Windows PowerShell:

  $env:DB_PASSWORD = "<your-local-database-password>"
  .\mvnw.cmd process-resources liberty:dev

The PostgreSQL JDBC driver is copied into the Liberty configuration resources
during the process-resources phase. This is the directory used by the datasource
library in both Liberty dev mode and the Docker image. Build the project before
creating the Docker image so the driver is available to copy into the image.
