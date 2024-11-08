FROM clojure as buildStage

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN lein uberjar

FROM amazoncorretto:22-alpine

WORKDIR /app

COPY --from=buildStage /usr/src/app/target/rango-0.1.0-SNAPSHOT-standalone.jar  /app/rango.jar

CMD ["java", "-jar", "rango.jar"]
