(ns rango.db.datomic.reservation
  (:require [common-clj.integrant-components.datomic :as component.datomic]
            [rango.adapters.reservation :as adapters.reservation]
            [rango.models.reservation :as models.reservation]
            [schema.core :as s]))

(s/defn insert! :- models.reservation/Reservation
  [reservation :- models.reservation/Reservation
   datomic]
  (-> (component.datomic/transact-and-lookup-entity! :reservation/id (adapters.reservation/internal->database reservation) datomic)
      :entity
      adapters.reservation/database->internal))
