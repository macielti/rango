(ns rango.components
  (:require [common-clj.integrant-components.config]
            [common-clj.integrant-components.routes]
            [common-clj.integrant-components.service]
            [integrant.core :as ig]
            [porteiro-component.admin-component :as porteiro.admin]
            [porteiro-component.diplomat.http-server :as porteiro.diplomat.http-server]
            [postgresql-component.core :as component.postgresql]
            [rango.diplomat.http-server :as diplomat.http-server])
  (:gen-class))

(def config
  {:common-clj.integrant-components.config/config   {:path "resources/config.edn"
                                                     :env  :prod}
   ::porteiro.admin/admin                           {:components {:config     (ig/ref :common-clj.integrant-components.config/config)
                                                                  :postgresql (ig/ref ::component.postgresql/postgresql)}}
   ::component.postgresql/postgresql                {:components {:config (ig/ref :common-clj.integrant-components.config/config)}}
   :common-clj.integrant-components.routes/routes   {:routes (concat diplomat.http-server/routes porteiro.diplomat.http-server/routes)}
   :common-clj.integrant-components.service/service {:components {:config     (ig/ref :common-clj.integrant-components.config/config)
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
