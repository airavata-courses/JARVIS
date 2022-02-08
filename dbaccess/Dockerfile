FROM openjdk:11 as build
VOLUME /main-app
ADD target/dbaccess-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar","/app.jar"]
