FROM clojure as buildStage

LABEL stage="builder"

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN apt-get -y update

RUN lein deps

RUN lein uberjar

FROM amazoncorretto:22-alpine

WORKDIR /app

COPY --from=buildStage /usr/src/app/target/rango-0.1.0-SNAPSHOT-standalone.jar  /app/rango.jar

CMD ["java", "-jar", "-Xmx100m", "rango.jar"]
