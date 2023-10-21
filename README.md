# TicTacToe 

A sample web app to play Tic Tac Toe against the computer.

| Component        | Technology               |
|------------------|--------------------------|
| Java Runtime     | JDK 21                   |
| Build Tool       | Maven                    |
| Framework        | Spring Boot (v3.1.4)     |
| Persistency      | H2 Database, JPA         |
| UI               | Thymeleaf with Bootstrap |
| Containerization | Dockerfile               |
| Deployment       | Docker Compose           |

|           UI Component      |                   Link                             |
|-----------------------------|----------------------------------------------------|
| Header                      | https://getbootstrap.com/docs/5.3/examples/headers/|
| Footer                      | https://getbootstrap.com/docs/5.3/examples/footers/|
| Sign In and Register        | https://getbootstrap.com/docs/5.3/examples/sign-in/|

## How to play

You can start the application locally
from your IDE or by running the following command in a terminal window:

```
./mvnw spring-boot:run
```

When accessing the application on http://localhost:8080, you will be asked to sign in to play. If you didn't register, 
you can do so by clicking [the register link](http://localhost:8080/register). If there is an user already registered with a chosen username, please choose a different one.

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
