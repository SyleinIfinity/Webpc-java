FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn -q -DskipTests package

EXPOSE 8080

# Chay Jetty (embedded) de phuc vu WAR/JSP. Port trong container co dinh 8080,
# port public duoc map o docker-compose (deploy/docker-compose.yml).
CMD ["bash", "-lc", "mvn -q -DskipTests jetty:run -Djetty.http.port=8080"]
