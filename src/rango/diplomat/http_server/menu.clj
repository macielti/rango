(ns rango.diplomat.http-server.menu
  (:require [schema.core :as s]
            [rango.adapters.menu :as adapters.menu]
            [rango.controllers.menu :as controllers.menu]))

(s/defn create-menu!
  [{{:keys [menu]}    :json-params
    {:keys [datomic]} :components}]
  {:status 200
   :body   {:menu (-> (adapters.menu/wire->internal menu)
                      (controllers.menu/create! datomic)
                      adapters.menu/internal->wire)}})

(s/defn fetch-all
  [{{:keys [datomic]} :components}]
  {:status 200
   :body   {:menus (->> (controllers.menu/fetch-all datomic)
                        (map adapters.menu/internal->wire))}})