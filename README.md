# TicTacToe 

A sample web app to play Tic Tac Toe against the computer. The application requires minimum JDK 21 to run locally.

## Technologies

This application has several components:

* Running on JDK 21 and built with Maven.
* Setup built with Spring Boot (v3.2.2), persistency layer with JPA and H2 database, UI with Thymeleaf with Bootstrap
* Containerized with Dockerfile and deployed with Docker Compose.

|           UI Component      |                   Link                             |
|-----------------------------|----------------------------------------------------|
| Header                      | https://getbootstrap.com/docs/5.3/examples/headers/|
| Footer                      | https://getbootstrap.com/docs/5.3/examples/footers/|
| Sign In and Register        | https://getbootstrap.com/docs/5.3/examples/sign-in/|

## How to create the keystore and truststore

In order to build an example keystore, you can use the following series of commands:

1. Create a CA certificate, by running `openssl req -x509 -sha256 -days 3650 -newkey rsa:4096 -keyout localCA.key -out localCA.crt`
2. Create a certificate signing request, by running `openssl req -new -newkey rsa:4096 -keyout localhost.key -out localhost.csr`
3. Make an extension file (`local.ext`) containing additional hosts and IPs that your certificate should authorize:

```txt{local.ext}
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
subjectAltName = @alt_names
[alt_names]
DNS.1 = localhost
DNS.2 = springboot
IP.1 = 127.0.0.1
```

4. Sign the request with our rootCA.crt certificate and its private key:
```shell
openssl x509 -req -CA localCA.crt -CAkey localCA.key -in localhost.csr \
    -out localhost.crt -days 365 -CAcreateserial -extfile local.ext
```

5. Create the keystore by importing the certificate created at previous step:
```shell
openssl pkcs12 -export -out keystore.p12 -name "localhost" -inkey localhost.key -in localhost.crt
```

To enable mutual authentication, you need to generate a truststore and generate client certificates:

1. Create a truststore by running:

```shell
keytool -import -trustcacerts -noprompt -alias ca -ext san=dns:localhost,ip:127.0.0.1 -file localCA.crt -keystore truststore.p12
```

2. Generate a client-side certificate signing request with common name (CN) Ana:

```shell
openssl req -new -newkey rsa:4096 -nodes -keyout ana.key -out ana.csr -subj '/CN=ana'
```

3. Sign the request with the existing CA:

```shell
openssl x509 -req -CA localCA.crt -CAkey localCA.key -in ana.csr -out ana.crt -days 365 -CAcreateserial
```

4. Package the signed certificate and the private key into the PKCS file:

```shell
openssl pkcs12 -export -out ana.p12 -name "ana" -inkey ana.key -in ana.crt
```

5. Import the generated certificate (`ana.p12`) in your browser.

## How to play

You can start the application locally
from your IDE or by running the following command in a terminal window:

```
./mvnw spring-boot:run
```

When accessing the application on https://localhost:8443, you will be asked to sign in to play. If you didn't register, 
you can do so by clicking [the register link](https://localhost:8443/register). 
If there is an user already registered with a chosen username, please select a different one.

Once you register, login with your chosen username and password to play. Have fun!

## How to deploy

You can deploy the application locally via Docker Compose. First build the application using:

```
./mvnw clean verify
```

And then invoke:

```
docker-compose up --build
```

This will build a local containerize environment for you to inspect the application.

If you wish to deploy in other environments, you can always build and push a docker image using:

```
docker buildx build --platform=linux/amd64  --tag <registry>/<username>/tictactoe:1.0 . --no-cache
```