# Java Starter REST API

This is a seed project that I have used in some personal projects. It includes some additional features that will save you some time:

  * Database initialization (mariadb/mysql/postgresql)
  * JWT-like authentication
  * User API for login and sign up (w/ user script table)
  * Google's recaptcha token integrated
  * SSL-ready (if no keystore provided, just fallback to plain HTTP)
  * Log4j-ready

## API setup

First, you've got to clone this repo:

`git clone https://github.com/felipeleivav/java-starter-rest-api.git`

Then, config properties according to your own needs:

`vi ./config/api.rest.properties`

This is the script you need in order to use the built-in users API:

`apidb.sql`

Build your API using the build script (requires Maven):

`./build.sh`

And finally run your API:

    cd dist
    ./launch.sh

Logs are automatically generated at `logs` folder.

## Coding the API

Basically these are the packages you will need to touch:

    com.api.rest.bean -> the good ol' beans
    com.api.rest.dao -> queries
    com.api.rest.rest -> api logic

All the APIs you create are private by default, this means your users will need to authenticate in order to operate on them.

You may want to create public APIs by declaring them at the Authenticator filter:

`com.api.rest.filter.Authenticator`

You have to specify path (ex: /products) and method (ex: POST). You can create any combination.

## Security

User passwords are hashed on database (using BCrypt).

Authentication tokens are using symmetric encryption provided by JCE.

Unlike traditional JWT, authentication tokens are being validated every time a user make a request, this means, tokens got decrypted, user/password validated on database, and duration checked. Users will never know any content of their token, they just pass it around.

## Contributions

are welcome :)