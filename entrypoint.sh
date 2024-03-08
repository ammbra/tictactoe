#!/bin/bash
set -e

remote_access_file="/etc/jmxremote/jmxremote.access"
remote_password_file="/etc/jmxremote/jmxremote.password"

# Set debug options if required
if [ x"${REMOTE_ACCESS_FILE}" != x ]; then
  remote_access_file= ${REMOTE_ACCESS_FILE}
fi

if [ x"${JMX_PASS_FILE}" != x ]; then
  remote_password_file= ${JMX_PASS_FILE}
fi

exec java -Djava.security.egd=file:/dev/./urandom -Dcom.sun.management.jmxremote.access.file=$remote_access_file \
                                                  -Dcom.sun.management.jmxremote.password.file=$remote_password_file \
                                                  -jar app.jar
