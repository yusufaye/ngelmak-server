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


### Install Java 17

Please consider installing Java jdk 17 if not already installed into your system. Use the command `java --version` to check the version. 

To install jdk-17 execute the command:
```
apt install openjdk-17-jdk openjdk-17-jre
```

Verify that `JAVA_HOME` is set by executing `echo $JAVA_HOME`.

Find the jdk installation path with the following command
```
sudo update-alternatives --config java
```

Set `JAVA_HOME` into your `.bashrc`:
```
nano ~/.bashrc
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
source ~/.bashrc
```


### Maven Parent overrides

You can install maven with APT
```
sudo apt install maven -y
```

Check the installation with
```
mvn --version
```


# DATABASE (POSGRESQL)

## Install PostgreSQL Linux (Ubuntu)

For downloading postgreSQL please follow the link [here](https://www.postgresql.org/download/linux/ubuntu/).

Or we can install it simply with apt by executing the following command:
```
sudo apt -y install postgresql-14
```


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

We have to alter the password for `postgres` user:

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

### Sql create sequence with Postgres

When you use spring boot you can choose a strategy to set primary key value by creating sequence generator. This is the advanced version of the default auto-increment.

To create a new sequence in postgres use the following:
```
DROP SEQUENCE IF EXISTS serial;
CREATE SEQUENCE serial START 101;
```
Select the next number from this sequence:
```
SELECT nextval('serial');
```
Update the current value of the sequence:
```
SELECT setval('serial', 201);
```
Some time we might be in case where data are imported from `csv` file. Importing them won't update the current value of the sequence (`SELECT currval('serial')`). Thus, might lead to violation of the primary key during insertion with Spring JPA.

To avoid that we can update the sequence as fellow:
```
SELECT setval('serial', max(id)) FROM nk_comment;
```

## Getting started with PostgreSQL (linux teminal only)

### Connect to a PostgreSQL Database Server (via psql)

The `psql` is a terminal-based utility to connect to the PostgreSQL server. It is used for interacting with PostgreSQL server such as executing SQL statements and managing database objects.

First, you can connect as postgress user
```
sudo -i -u postgres

# or

psql -U postgres

# or simply

psql
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