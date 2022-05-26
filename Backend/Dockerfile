FROM openjdk:11-jdk
ARG OUTPUT=target/backend-*.jar
COPY ${OUTPUT} backend.jar
ENTRYPOINT ["java","-jar","/backend.jar"]