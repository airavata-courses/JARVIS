FROM openjdk:11 as build
VOLUME /main-app
ADD target/UserData-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar"]