FROM clojure as builder

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN apt-get -y update

RUN lein deps

RUN lein uberjar

FROM ghcr.io/graalvm/graalvm-ce:22 AS native

WORKDIR /app

COPY --from=builder /usr/src/app/target/rango-0.1.0-SNAPSHOT-standalone.jar  /app/rango.jar

RUN gu install native-image

RUN native-image \
      --no-server \
      --allow-incomplete-classpath \
      --initialize-at-build-time \
      --enable-url-protocols=http,https \
      -Dio.pedestal.log.defaultMetricsRecorder=nil \
      -jar /app/rango.jar \
      -H:Name=./target/rango
