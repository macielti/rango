(defproject rango "0.1.0-SNAPSHOT"

  :description "Rango is a REST API for school canteen management"

  :url "https://github.com/macielti/rango"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.12.0"]
                 [net.clojars.macielti/porteiro-component "0.1.1"]
                 [net.clojars.macielti/common-clj "34.70.70"]
                 [net.clojars.macielti/postgresql-component "2.1.2"]]

  :profiles {:dev {:plugins        [[com.github.clojure-lsp/lein-clojure-lsp "1.4.2"]
                                    [lein-shell "0.5.0"]]

                   :resource-paths ["resources" "test/resources/"]

                   :test-paths     ["test/unit" "test/integration" "test/helpers"]

                   :dependencies   [[net.clojars.macielti/common-test-clj "1.0.0"]
                                    [danlentz/clj-uuid "0.1.9"]
                                    [hashp "0.2.2"]
                                    [nubank/matcher-combinators "3.9.1"]
                                    [com.github.igrishaev/pg2-migration "0.1.18"]]

                   :injections     [(require 'hashp.core)]

                   :repl-options   {:init-ns rango.components}

                   :aliases        {"clean-ns"     ["clojure-lsp" "clean-ns" "--dry"] ;; check if namespaces are clean
                                    "format"       ["clojure-lsp" "format" "--dry"] ;; check if namespaces are formatted
                                    "diagnostics"  ["clojure-lsp" "diagnostics"] ;; check if project has any diagnostics (clj-kondo findings)
                                    "lint"         ["do" ["clean-ns"] ["format"] ["diagnostics"]] ;; check all above
                                    "clean-ns-fix" ["clojure-lsp" "clean-ns"] ;; Fix namespaces not clean
                                    "format-fix"   ["clojure-lsp" "format"] ;; Fix namespaces not formatted
                                    "lint-fix"     ["do" ["clean-ns-fix"] ["format-fix"]] ;; Fix both

                                    "native"
                                    ["shell"
                                     "native-image"
                                     "--report-unsupported-elements-at-runtime"
                                     "--no-server"
                                     "--allow-incomplete-classpath"
                                     "--initialize-at-build-time"
                                     "--enable-url-protocols=http,https"
                                     "-Dio.pedestal.log.defaultMetricsRecorder=nil"
                                     "-jar" "./target/${:uberjar-name:-${:name}-${:version}-standalone.jar}"
                                     "-H:+StaticExecutableWithDynamicLibC"
                                     "-H:CCompilerOption=-pipe"
                                     "-H:ReflectionConfigurationFiles=reflect-config.json"
                                     "-H:Name=./target/rango"]}}}

  :src-dirs ["src"]

  :resource-paths ["resources"]

  :aot :all

  :main rango.components)
