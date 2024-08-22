FROM amazoncorretto:21-alpine
WORKDIR auth-service
COPY rest/target/rest-0.0.1-SNAPSHOT.jar /auth-service/auth.jar
ENTRYPOINT ["java","-jar","/auth-service/auth.jar"]