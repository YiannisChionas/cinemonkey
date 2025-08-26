#!/usr/bin/env bash
set -euo pipefail

CACERTS="/opt/java/openjdk/lib/security/cacerts"

if compgen -G "/certs/*.pem" > /dev/null; then
  echo "Importing custom certs..."
  for f in /certs/*.pem; do
    alias="$(basename "$f" .pem)"
    # αν υπάρχει ήδη, αγνόησέ το
    keytool -list -keystore "$CACERTS" -storepass changeit -alias "$alias" >/dev/null 2>&1 || \
    keytool -importcert -noprompt -trustcacerts -alias "$alias" -file "$f" \
      -keystore "$CACERTS" -storepass changeit || true
  done
fi

exec java $JAVA_OPTS -jar /app/app.jar
