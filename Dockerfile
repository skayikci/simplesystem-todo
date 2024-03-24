# layer - 1 : create the jar package
FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build
# layer - 2 : copy the package for runtime
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/todo-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "todo-0.0.1-SNAPSHOT.jar"]