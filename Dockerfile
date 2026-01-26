# ===== BUILD STAGE =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# build app
COPY src ./src
RUN mvn clean package -DskipTests

# ===== RUN STAGE =====
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# tối ưu cho free tier (RAM thấp)
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseSerialGC"

EXPOSE 8080

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
