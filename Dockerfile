FROM eclipse-temurin:21
LABEL maintainer="vinicius"
WORKDIR /app
COPY target/ValidaBoleto-0.0.1-SNAPSHOT.jar /app/valida-boleto.jar
ENTRYPOINT ["java", "-jar", "valida-boleto.jar"]

