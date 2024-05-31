FROM openjdk:17
WORKDIR /usr/src/app
COPY ./target/user-service-0.1.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","user-service-0.1.jar"]