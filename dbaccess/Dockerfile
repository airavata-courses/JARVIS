### BUILD a maven builder. This will contain all mvn dependencies and act as an abstraction for all mvn goals
FROM maven:3.8.4-jdk-11

# create app folder for sources
RUN mkdir -p /build/src
RUN mkdir -p /build/logs
COPY src /build/src

# The WORKDIR instruction sets the working directory for any RUN, CMD, ENTRYPOINT, COPY and ADD instructions that follow it in the Dockerfile.
WORKDIR /build
COPY pom.xml /build
#Download all required dependencies into one layer
RUN mvn dependency:resolve && mvn clean && mvn compile && mvn install
EXPOSE 8088
CMD ["java", "-jar","./target/dbaccess-0.0.1-SNAPSHOT.jar"]
