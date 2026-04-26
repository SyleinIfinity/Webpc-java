FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /src

COPY FE/pom.xml FE/pom.xml
COPY FE/src FE/src
RUN mvn -q -DskipTests -f FE/pom.xml package

COPY FE_NHANVIEN/pom.xml FE_NHANVIEN/pom.xml
COPY FE_NHANVIEN/src FE_NHANVIEN/src
RUN mvn -q -DskipTests -f FE_NHANVIEN/pom.xml package

FROM eclipse-temurin:21-jre

ARG JETTY_VERSION=12.1.8

ENV JETTY_HOME=/opt/jetty-home
ENV JETTY_BASE=/opt/jetty-base

WORKDIR /opt

RUN apt-get update -y \
  && apt-get install -y --no-install-recommends ca-certificates curl \
  && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-home/${JETTY_VERSION}/jetty-home-${JETTY_VERSION}.tar.gz" \
  | tar -xz \
  && mv "jetty-home-${JETTY_VERSION}" "$JETTY_HOME"

RUN mkdir -p "$JETTY_BASE" \
  && cd "$JETTY_BASE" \
  && java -jar "$JETTY_HOME/start.jar" --create-startd \
  && java -jar "$JETTY_HOME/start.jar" --add-modules=server,http,ee10-webapp,ee10-apache-jsp,ee10-deploy \
  && printf "%s\n" "jetty.http.port=8080" > "$JETTY_BASE/start.d/ports.ini"

# Deploy 2 WAR trong 1 Jetty:
# - FE -> /
# - FE_NHANVIEN -> /nhanvien
COPY --from=build /src/FE/target/*.war /tmp/fe.war
COPY --from=build /src/FE_NHANVIEN/target/*.war /tmp/fe-nhanvien.war

RUN mkdir -p "$JETTY_BASE/webapps" \
  && mv /tmp/fe.war "$JETTY_BASE/webapps/ROOT.war" \
  && mv /tmp/fe-nhanvien.war "$JETTY_BASE/webapps/nhanvien.war"

EXPOSE 8080

WORKDIR $JETTY_BASE

CMD ["bash", "-lc", "java -jar \"$JETTY_HOME/start.jar\""]
