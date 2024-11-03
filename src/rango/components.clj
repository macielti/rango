(ns rango.components
  (:require [common-clj.integrant-components.config]
            [common-clj.integrant-components.postgresql]
            [common-clj.integrant-components.prometheus]
            [common-clj.integrant-components.routes]
            [common-clj.integrant-components.service]
            [common-clj.porteiro.admin]
            [common-clj.porteiro.diplomat.http-server :as porteiro.diplomat.http-server]
            [integrant.core :as ig]
            [rango.diplomat.http-server :as diplomat.http-server]
            [taoensso.timbre :as timbre])
  (:gen-class))

(def config
  {:common-clj.integrant-components.config/config         {:path "resources/config.edn"
                                                           :env  :prod}
   :common-clj.porteiro.admin/admin                       {:components {:config     (ig/ref :common-clj.integrant-components.config/config)
                                                                        :postgresql (ig/ref :common-clj.integrant-components.postgresql/postgresql)}}
   :common-clj.integrant-components.postgresql/postgresql {:components {:config (ig/ref :common-clj.integrant-components.config/config)}}
   :common-clj.integrant-components.routes/routes         {:routes (concat diplomat.http-server/routes porteiro.diplomat.http-server/routes)}
   :common-clj.integrant-components.prometheus/prometheus {:metrics []}
   :common-clj.integrant-components.service/service       {:components {:prometheus (ig/ref :common-clj.integrant-components.prometheus/prometheus)
                                                                        :config     (ig/ref :common-clj.integrant-components.config/config)
                                                                        :routes     (ig/ref :common-clj.integrant-components.routes/routes)
                                                                        :postgresql (ig/ref :common-clj.integrant-components.postgresql/postgresql)}}})

(defn start-system! []
  (timbre/set-min-level! :info)
  (ig/init config))

(def -main start-system!)

(def config-test
  (-> config
      (assoc :common-clj.integrant-components.config/config {:path "resources/config.example.edn"
                                                             :env  :test})))
