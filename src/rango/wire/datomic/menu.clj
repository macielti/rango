(ns rango.wire.datomic.menu
  (:require [rango.models.menu :as models.menu]
            [schema.core :as s])
  (:import (java.util Date)))

(def menu
  [{:db/ident       :menu/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Menu Id"}
   {:db/ident       :menu/description
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Menu description"}
   {:db/ident       :menu/reference-date
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "Date which the menu will be served"}
   {:db/ident       :menu/created-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "When the menu was created"}])

(s/defschema Menu
  (assoc models.menu/menu
         :menu/created-at Date
         :menu/reference-date Date))
