# ---------- build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Προ-κατεβάζουμε deps για καλύτερο caching
COPY cinemonkey-backend/pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Κώδικας
COPY cinemonkey-backend/ ./

# Αν θες να τρέχουν tests με H2, βγάλε το -DskipTests (ή δες το RUN_TESTS πιο κάτω)
ARG RUN_TESTS=false
RUN if [ "$RUN_TESTS" = "true" ]; then \
      mvn -B -P test verify; \
    else \
      mvn -B -DskipTests package; \
    fi

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# JAR από το build stage
COPY --from=build /build/target/cinemonkey-backend-0.0.1-SNAPSHOT.jar app.jar

# (dev μόνο) εισαγωγή self-signed cert για να εμπιστεύεται https://cinemonkey.com
COPY self-signed/cinemonkey.com.pem /usr/local/share/ca-certificates/cinemonkey.crt
RUN keytool -importcert \
    -noprompt -trustcacerts \
    -alias cinemonkey \
    -file /usr/local/share/ca-certificates/cinemonkey.crt \
    -keystore /opt/java/openjdk/lib/security/cacerts \
    -storepass changeit

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
