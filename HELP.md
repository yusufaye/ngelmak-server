# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.2/maven-plugin/build-image.html)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#web.security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#howto.data-access.exposing-spring-data-repositories-as-rest)

### Guides
The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
* [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.


# DATABASE (POSGRESQL)

## Install PostgreSQL Linux (Ubuntu)

PostgreSLQ server can be configured to listen on some addresses for remote connections. This is done by editing the `postgresql.conf` file.

```
sudo nano /etc/postgresql/14/main/postgresql.conf

listen_addresses = '*'
```

We then configure `pg_hba.conf` file to use md5 password auth for remote connection.

```
sudo sed -i '/^host/s/ident/md5/' /etc/postgresql/14/main/pg_hba.conf
sudo sed -i '/^local/s/peer/trust/' /etc/postgresql/14/main/pg_hba.conf
echo "host all all 0.0.0.0/0 md5" | sudo tee -a /etc/postgresql/14/main/pg_hba.conf
```

Resart PostgreSQL
```
sudo systemctl restart postgresql
```

### Connect to the PostgreSQL database server

Connect to PostgreSQL through `postgres` user:
```
sudo -u postgres psql
```

We have to alter the password for postgres user:
```
ALTER USER postgres PASSWORD '<password>';
```

Create a new user:
```
postgres=# CREATE USER <username> WITH PASSWORD '<password>';
CREATE ROLE
```

We can also create a database for our created user:
```
postgres=# CREATE DATABASE <databas_name> OWNER <username>;
CREATE DATABASE
```

## Getting started with PostgreSQL (linux teminal only)

### Connect to a PostgreSQL Database Server (via psql)

The `psql` is a terminal-based utility to connect to the PostgreSQL server. It is used for interacting with PostgreSQL server such as executing SQL statements and managing database objects.

First, you can connect as postgress user
```
sudo -i -u postgres
```

```
psql -U postgres or simply qsql
```
where:
-`psql`: Invoke the psql program
-`-U` postgres: Specify the user that connects to the PostgreSQL server. The -U option means user. Note that you need to use -U in uppercase, not lowercase.


### Create and manage a database

Create a new database with `CREATE DATABASE <database-name>`
```
postgres=# CREATE DATABASE ngelmak_project;
```

We can list all databases
```
postgres=# \l
```

Connect to the created database
```
postgres=# \c ngelmak_project 
You are now connected to database "ngelmak_project" as user "postgres".
ngelmak_project=#
```

Display all tables (relations) from the connect databases
```
ngelmak_project=# \dt
```

Please refers to [PostgreSQL tutorial for more details](https://www.postgresqltutorial.com/postgresql-getting-started/connect-to-postgresql-database/).