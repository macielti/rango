(ns rango.wire.datomic.student
  (:require [rango.models.student :as models.student]
            [schema.core :as s])
  (:import (java.util Date)))

(def student
  [{:db/ident       :student/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Student Id"}
   {:db/ident       :student/code
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Code to identify the student"}
   {:db/ident       :student/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Code to identify the student"}
   {:db/ident       :student/class
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc         "Class with the student is associated"}
   {:db/ident       :student/created-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the student was registered for the first time"}])

(s/defschema Student
  (assoc models.student/Student
    :student/created-at Date))
