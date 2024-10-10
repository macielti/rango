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
