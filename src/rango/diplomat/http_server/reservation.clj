(ns rango.diplomat.http-server.reservation
  (:require [rango.adapters.reservation :as adapters.reservation]
            [rango.controllers.reservation :as controllers.reservation]
            [schema.core :as s])
  (:import (java.util UUID)))

(s/defn create-reservation!
  [{{:keys [reservation]} :json-params
    {:keys [datomic]}     :components}]
  {:status 200
   :body   {:reservation (-> (controllers.reservation/create! (:student-code reservation)
                                                              (UUID/fromString (:menu-id reservation)) datomic)
                             adapters.reservation/internal->wire)}})

(s/defn fetch-reservations-by-menu
  [{{:keys [menu-id]} :path-params
    {:keys [datomic]} :components}]
  {:status 200
   :body   {:reservations (->> (controllers.reservation/fetch-by-menu (UUID/fromString menu-id) datomic)
                               (map adapters.reservation/internal->wire))}})
