FROM ghcr.io/graalvm/graalvm-ce:22 as build

RUN gu install native-image

RUN curl -O https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein && \
    chmod +x lein && \
    mv lein /usr/bin/lein && \
    lein upgrade

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN lein uberjar

RUN native-image \
      --no-fallback \
      --trace-object-instantiation=java.lang.Thread \
      --report-unsupported-elements-at-runtime \
      --initialize-at-build-time=ch.qos.logback.core.util.StatusPrinter,ch.qos.logback.classic.Logger,ch.qos.logback.core.CoreConstants,ch.qos.logback.core.util.StatusPrinter2,ch.qos.logback.core.util.Loader,ch.qos.logback.classic.Level,ch.qos.logback.core.status.StatusBase,ch.qos.logback.core.status.InfoStatus \
      --initialize-at-build-time=io.opentracing.util.GlobalTracer \
      --initialize-at-build-time=org.slf4j.helpers.Reporter,org.slf4j.LoggerFactory \
      --initialize-at-build-time=org.pg.Pool \
      --initialize-at-build-time=io.opentelemetry.api.common.ArrayBackedAttributes,io.opentelemetry.api.DefaultOpenTelemetry,io.opentelemetry.api.GlobalOpenTelemetry,io.opentelemetry.context.propagation.DefaultContextPropagators \
      --initialize-at-build-time=org.eclipse.jetty.http.HttpVersion,org.eclipse.jetty.http2.hpack.HpackContext,org.eclipse.jetty.util.BufferUtil,org.eclipse.jetty.http.HttpMethod,org.eclipse.jetty.server.Response,org.eclipse.jetty.http.DateGenerator,org.eclipse.jetty.http.HttpHeader,org.eclipse.jetty.http2.hpack.HpackEncoder,org.eclipse.jetty.util.TypeUtil,org.eclipse.jetty.http.compression.Huffman,org.eclipse.jetty.util.StringUtil,org.eclipse.jetty.http.PreEncodedHttpField,org.eclipse.jetty.http.HttpScheme,org.eclipse.jetty.http.HttpTokens \
      --initialize-at-build-time=org.joda.time.chrono.ISOChronology,org.joda.time.Period,org.joda.time.DateTimeFieldType,org.joda.time.DateTimeZone,org.joda.time.chrono.ISOYearOfEraDateTimeField,org.joda.time.chrono.BasicGJChronology,org.joda.time.chrono.GregorianChronology \
      --initialize-at-build-time=org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers,org.bouncycastle.asn1.nist.NISTObjectIdentifiers,org.bouncycastle.util.Strings,org.bouncycastle.crypto.CryptoServicesRegistrar,org.bouncycastle.asn1.x9.X9ObjectIdentifiers,org.bouncycastle.util.Properties,org.bouncycastle.crypto.engines.Salsa20Engine,org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers,org.bouncycastle.asn1.x509.X509ObjectIdentifiers \
      --features=clj_easy.graal_build_time.InitClojureClasses \
      --enable-url-protocols=http,https \
      -Dio.pedestal.log.defaultMetricsRecorder=nil \
      -jar ./target/rango-0.1.0-SNAPSHOT-standalone.jar \
      -H:Name=rango-native \
      -H:+ReportExceptionStackTraces \
      -H:+StaticExecutableWithDynamicLibC \
      -H:CCompilerOption=-pipe

FROM ghcr.io/graalvm/jdk-community:23.0.1

WORKDIR /app

COPY --from=build /usr/src/app/rango-native  /app/rango

CMD ["./rango"]
