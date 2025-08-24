# Base image με JDK 17
FROM eclipse-temurin:17-jdk-jammy

# Δημιουργία φακέλου για την εφαρμογή
WORKDIR /app

# Αντιγραφή του JAR αρχείου στο image
COPY cinemonkey-backend/target/cinemonkey-backend-0.0.1-SNAPSHOT.jar cineproject.jar

# Αντιγραφή self-signed certificate (π.χ. από ./self-signed/)
COPY self-signed/cinemonkey.com.pem /usr/local/share/ca-certificates/cinemonkey.crt

# Εισαγωγή του cert στο Java truststore
RUN keytool -importcert \
    -noprompt \
    -trustcacerts \
    -alias cinemonkey \
    -file /usr/local/share/ca-certificates/cinemonkey.crt \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit

# Άνοιγμα port της εφαρμογής
EXPOSE 8080

# Εκκίνηση της εφαρμογής
ENTRYPOINT ["java", "-jar", "cineproject.jar"]
