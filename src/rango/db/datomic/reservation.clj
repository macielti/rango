(ns rango.db.datomic.reservation
  (:require [common-clj.integrant-components.datomic :as component.datomic]
            [datomic.api :as d]
            [rango.adapters.reservation :as adapters.reservation]
            [rango.models.reservation :as models.reservation]
            [schema.core :as s]))

(s/defn insert! :- models.reservation/Reservation
  [reservation :- models.reservation/Reservation
   datomic]
  (-> (component.datomic/transact-and-lookup-entity! :reservation/id (adapters.reservation/internal->database reservation) datomic)
      :entity
      adapters.reservation/database->internal))

(s/defn by-menu :- [models.reservation/Reservation]
  [menu-id :- s/Uuid
   database]
  (some-> (d/q '[:find (pull ?reservation [*])
                 :in $ ?menu-id
                 :where [?reservation :reservation/menu-id ?menu-id]] database menu-id)
          (->> (mapv first))
          (->> (mapv #(dissoc % :db/id)))
          (->> (map adapters.reservation/database->internal))))
