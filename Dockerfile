FROM openjdk:21-jdk-slim
WORKDIR /app
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon
COPY src/ src/
RUN ./gradlew build --no-daemon -x test
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=academic_management_service
ENV DB_USER=postgres
ENV DB_PASSWORD=password
CMD ["java", "-jar", "build/libs/academic-management-service-0.0.1-SNAPSHOT.jar"]
