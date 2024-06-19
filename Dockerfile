FROM maven as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

COPY .env .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]