(ns rango.components
  (:require
   [common-clj.integrant-components.config]
   [common-clj.integrant-components.datomic]
   [common-clj.integrant-components.prometheus]
   [common-clj.integrant-components.routes]
   [common-clj.integrant-components.service]
   [integrant.core :as ig]
   [rango.db.datomic.config :as database.config]
   [rango.diplomat.http-server :as diplomat.http-server]
   [taoensso.timbre :as timbre])
  (:gen-class))

(def config
  {:common-clj.integrant-components.config/config         {:path "resources/config.edn"
                                                           :env  :prod}
   :common-clj.integrant-components.datomic/datomic       {:schemas    database.config/schemas
                                                           :components {:config (ig/ref :common-clj.integrant-components.config/config)}}
   :common-clj.integrant-components.routes/routes         {:routes diplomat.http-server/routes}
   :common-clj.integrant-components.prometheus/prometheus {:metrics []}
   :common-clj.integrant-components.service/service       {:components {:prometheus (ig/ref :common-clj.integrant-components.prometheus/prometheus)
                                                                        :config     (ig/ref :common-clj.integrant-components.config/config)
                                                                        :routes     (ig/ref :common-clj.integrant-components.routes/routes)
                                                                        :datomic    (ig/ref :common-clj.integrant-components.datomic/datomic)}}})

(defn start-system! []
  (timbre/set-min-level! :info)
  (ig/init config))

(def -main start-system!)

(def config-test
  (-> config
      (assoc :common-clj.integrant-components.config/config {:path "resources/config.example.edn"
                                                             :env  :test})))
