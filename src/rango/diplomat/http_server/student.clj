(ns rango.diplomat.http-server.student
  (:require
   [rango.adapters.student :as adapters.student]
   [rango.controllers.student :as controllers.student]
   [schema.core :as s]))

(s/defn create-student!
  [{{:keys [student]} :json-params
    {:keys [datomic]} :components}]
  {:status 200
   :body   {:student (-> (adapters.student/wire->internal student)
                         (controllers.student/create! datomic)
                         adapters.student/internal->wire)}})

(s/defn fetch-all
  [{{:keys [datomic]} :components}]
  {:status 200
   :body   {:students (->> (controllers.student/fetch-all datomic)
                           (map adapters.student/internal->wire))}})