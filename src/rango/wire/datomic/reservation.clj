(ns rango.wire.datomic.reservation
  (:require [schema.core :as s])
  (:import (java.util Date)))

(def reservation
  [{:db/ident       :reservation/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Reservation Id"}
   {:db/ident       :reservation/student-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "Student Id"}
   {:db/ident       :reservation/menu-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "Date which the menu will be served"}
   {:db/ident       :reservation/created-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the reservation was placed"}])

(s/defschema Reservation
  (assoc reservation :reservation/created-at Date))
