FROM ghcr.io/graalvm/graalvm-ce:22 as build

RUN gu install native-image

RUN curl -O https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein && \
    chmod +x lein && \
    mv lein /usr/bin/lein && \
    lein upgrade

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN lein uberjar

RUN lein native

FROM gcr.io/distroless/base:latest

WORKDIR /app

COPY --from=build /usr/src/app/target/rango  /app/rango

CMD ["./rango"]
