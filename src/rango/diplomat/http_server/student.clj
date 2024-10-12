(ns rango.diplomat.http-server.student
  (:require [rango.adapters.student :as adapters.student]
            [rango.controllers.student :as controllers.student]
            [schema.core :as s])
  (:import (java.util UUID)))

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

(s/defn fetch-students-by-reservations-menu
  [{{:keys [menu-id]} :path-params
    {:keys [datomic]} :components}]
  {:status 200
   :body   {:reservations (->> (controllers.student/fetch-students-by-menu-reservations (UUID/fromString menu-id) datomic)
                               (map adapters.student/internal->wire))}})

(s/defn retract-student!
  [{{:keys [student-id]} :path-params
    {:keys [datomic]}    :components}]
  (controllers.student/retract! (UUID/fromString student-id) datomic)
  {:status 200
   :body   {}})
