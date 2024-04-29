FROM openjdk:8
COPY target/*.jar /usr/src/myapp/app.jar
WORKDIR /usr/src/myapp
RUN echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-jar", "app.jar"]
