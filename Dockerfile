# Define your base image
FROM container-registry.oracle.com/java/openjdk:23-oraclelinux9 as jre-build

RUN $JAVA_HOME/bin/jlink \
       --add-modules java.base,java.compiler,java.desktop,java.instrument,java.management,java.net.http,java.prefs,java.rmi,java.scripting,java.security.jgss,java.sql.rowset,jdk.jfr,jdk.net,jdk.unsupported,jdk.management.agent,jdk.crypto.ec,jdk.management.jfr \
       --no-man-pages \
       --no-header-files \
       --compress=zip-9 \
       --output javaruntime

# Define your base image
FROM oraclelinux:9-slim

ENV JAVA_HOME /usr/java/openjdk-23
ENV PATH $JAVA_HOME/bin:$PATH
COPY --from=jre-build /javaruntime $JAVA_HOME

# Continue with your application deployment
COPY ./target/tictactoe.jar /app.jar
COPY entrypoint.sh /entrypoint.sh

RUN groupadd -r appuser && useradd -r -g appuser appuser && chmod +x /entrypoint.sh
USER appuser
EXPOSE 1099

ENV JDK_JAVA_OPTIONS "--enable-preview"

CMD ["/entrypoint.sh"]


