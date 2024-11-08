(ns rango.components
  (:require [common-clj.integrant-components.config]
            [common-clj.integrant-components.http-client]
            [common-clj.integrant-components.prometheus]
            [common-clj.integrant-components.routes]
            [common-clj.integrant-components.service]
            [integrant.core :as ig]
            [new-relic-component.core :as component.new-relic]
            [porteiro-component.admin-component :as porteiro.admin]
            [porteiro-component.diplomat.http-server :as porteiro.diplomat.http-server]
            [postgresql-component.core :as component.postgresql]
            [rango.diplomat.http-server :as diplomat.http-server]
            [taoensso.timbre.tools.logging])
  (:gen-class))

(taoensso.timbre.tools.logging/use-timbre)

(def config
  {:common-clj.integrant-components.config/config           {:path "resources/config.edn"
                                                             :env  :prod}
   :common-clj.integrant-components.prometheus/prometheus   {:metrics []}
   :common-clj.integrant-components.http-client/http-client {:components {:config     (ig/ref :common-clj.integrant-components.config/config)
                                                                          :prometheus (ig/ref :common-clj.integrant-components.prometheus/prometheus)}}
   ::component.new-relic/new-relic                          {:components {:config      (ig/ref :common-clj.integrant-components.config/config)
                                                                          :http-client (ig/ref :common-clj.integrant-components.http-client/http-client)}}
   ::porteiro.admin/admin                                   {:components {:config     (ig/ref :common-clj.integrant-components.config/config)
                                                                          :postgresql (ig/ref ::component.postgresql/postgresql)}}
   ::component.postgresql/postgresql                        {:components {:config (ig/ref :common-clj.integrant-components.config/config)}}
   :common-clj.integrant-components.routes/routes           {:routes (concat diplomat.http-server/routes porteiro.diplomat.http-server/routes)}
   :common-clj.integrant-components.service/service         {:components {:config     (ig/ref :common-clj.integrant-components.config/config)
                                                                          :routes     (ig/ref :common-clj.integrant-components.routes/routes)
                                                                          :postgresql (ig/ref ::component.postgresql/postgresql)}}})

(defn start-system! []
  (ig/init config))

(defn -main [& _args]
  (start-system!))

(def config-test
  (-> config
      (assoc :common-clj.integrant-components.config/config {:path "resources/config.example.edn"
                                                             :env  :test})))
