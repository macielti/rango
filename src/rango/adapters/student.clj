(ns rango.adapters.student
  (:require
   [camel-snake-kebab.core :as csk]
   [java-time.api :as jt]
   [rango.models.student :as models.student]
   [rango.wire.datomic.student :as wire.datomic.student]
   [rango.wire.in.student :as wire.in.student]
   [rango.wire.out.student :as wire.out.student]
   [schema.core :as s]))

(s/defn wire->internal :- models.student/Student
  [{:keys [code name class]} :- wire.in.student/Student]
  {:student/id         (random-uuid)
   :student/code       code
   :student/name       name
   :student/class      (csk/->kebab-case-keyword class)
   :student/created-at (jt/local-date-time)})

(s/defn internal->database :- wire.datomic.student/Student
  [{:student/keys [created-at] :as student} :- models.student/Student]
  (assoc student :student/created-at (-> (jt/zoned-date-time created-at (jt/zone-id "UTC"))
                                         jt/java-date)))

(s/defn database->internal :- models.student/Student
  [{:student/keys [created-at] :as student} :- wire.datomic.student/Student]
  (assoc student :student/created-at (-> (jt/zoned-date-time created-at (jt/zone-id "UTC"))
                                         jt/local-date-time)))

(s/defn internal->wire :- wire.out.student/Student
  [{:student/keys [id code name class created-at]} :- models.student/Student]
  {:id         (str id)
   :code       code
   :name       name
   :class      (clojure.core/name class)
   :created-at (str created-at)})
