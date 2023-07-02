FROM openjdk:17
EXPOSE 8080
ADD  target/proxemconnector.jar proxemconnector.jar
ENTRYPOINT ["java","-jar","/proxemconnector.jar"]

