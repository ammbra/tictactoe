# Java runtime built with jlink in a multi-stage container build
FROM openjdk:21-ea-oraclelinux8 as jre-build

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
	--add-modules java.base,java.compiler,java.desktop,java.instrument,java.management,java.net.http,java.prefs,java.rmi,java.scripting,java.security.jgss,java.sql.rowset,jdk.jfr,jdk.net,jdk.unsupported \
	--strip-debug \
	--no-man-pages \
	--no-header-files \
	--compress=2 \
	--output /javaruntime

# Define your base image
FROM oraclelinux:8-slim

ENV JAVA_HOME /usr/java/openjdk-21
ENV PATH $JAVA_HOME/bin:$PATH

COPY --from=jre-build /javaruntime $JAVA_HOME

# Continue with your application deployment
COPY ./target/tictactoe.jar /app.jar

RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

ENV JDK_JAVA_OPTIONS "--enable-preview"

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]

