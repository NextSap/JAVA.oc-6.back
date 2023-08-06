FROM maven:3.8.3-openjdk-17 AS build
COPY src /src
COPY pom.xml pom.xml
RUN mvn clean package -DskipTests
COPY target/paymybuddy-0.0.1.jar paymybuddy-0.0.1.jar
COPY database.sql /docker-entrypoint-initdb.d
ENTRYPOINT ["java","-jar","paymybuddy-0.0.1.jar"]