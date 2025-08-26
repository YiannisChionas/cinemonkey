# --- Build stage ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY cinemonkey-backend/pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline
COPY cinemonkey-backend/ .
# Αν θες tests στη CI, ορίσε RUN_TESTS=true στο action
ARG RUN_TESTS=false
RUN if [ "$RUN_TESTS" = "true" ]; then mvn -B -P test verify; else mvn -B -DskipTests package; fi

# --- Runtime stage ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /build/target/cinemonkey-backend-0.0.1-SNAPSHOT.jar app.jar

# runtime entrypoint: αν βρει PEMs στο /certs, τα εισάγει στο cacerts
COPY cinemonkey-backend/docker-entrypoint.sh /docker-entrypoint.sh

# Αφαίρεση CRLF και δικαιώματα εκτέλεσης
RUN sed -i 's/\r$//' /docker-entrypoint.sh && chmod +x /docker-entrypoint.sh

ENV JAVA_OPTS=""
EXPOSE 8080

# Χρησιμοποιούμε ρητά bash ως interpreter (αποφεύγουμε kernel shebang issues)
ENTRYPOINT ["/bin/bash", "/docker-entrypoint.sh"]