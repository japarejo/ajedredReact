FROM maven:3.8.6-openjdk-18-slim AS build
RUN mkdir -p workspace
WORKDIR workspace
COPY . .
RUN mvn clean install -DskipTests=true

EXPOSE 8080

CMD mvn spring-boot:run