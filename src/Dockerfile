# ---------- Build stage ----------
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copy Gradle files first (better caching)
COPY ../build.gradle settings.gradle gradlew ./
COPY ../gradle ./gradle
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the Spring Boot JAR
RUN ./gradlew clean build -x test --no-daemon

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Render provides PORT env variable
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
