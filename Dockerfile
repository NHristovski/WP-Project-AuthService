FROM maven:3.6.3-jdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package


FROM openjdk:11
COPY --from=build /usr/src/app/target/auth-0.0.1-SNAPSHOT.jar /usr/app/auth-0.0.1-SNAPSHOT.jar
COPY src/main/resources/application.properties /usr/app/application.properties
EXPOSE 9100
ENTRYPOINT ["java","-jar","/usr/app/auth-0.0.1-SNAPSHOT.jar","--spring.config.location=file:/usr/app/application.properties"]

