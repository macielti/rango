(ns rango.diplomat.http-server.student
  (:require [schema.core :as s]
            [rango.adapters.student :as adapters.student]
            [rango.controllers.student :as controllers.student]))

(s/defn create-student!
  [{{:keys [student]} :json-params
    {:keys [datomic]} :components}]
  {:status 200
   :body   {:student (-> (adapters.student/wire->internal student)
                         (controllers.student/create! datomic)
                         adapters.student/internal->wire)}})
