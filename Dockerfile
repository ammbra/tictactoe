# Define your base image
FROM container-registry.oracle.com/java/openjdk:21-oraclelinux8

# Continue with your application deployment
COPY ./target/tictactoe.jar /app.jar
COPY entrypoint.sh /entrypoint.sh


RUN groupadd -r appuser && useradd -r -g appuser appuser && chmod +x /entrypoint.sh
USER appuser
EXPOSE 1099

ENV JDK_JAVA_OPTIONS "--enable-preview"

CMD ["/entrypoint.sh"]


