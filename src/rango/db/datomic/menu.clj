(ns rango.db.datomic.menu
  (:require [common-clj.integrant-components.datomic :as component.datomic]
            [datomic.api :as d]
            [rango.adapters.menu :as adapters.menu]
            [rango.models.menu :as models.menu]
            [schema.core :as s]))

(s/defn insert! :- models.menu/Menu
  [menu :- models.menu/Menu
   datomic]
  (-> (component.datomic/transact-and-lookup-entity! :menu/id (adapters.menu/internal->database menu) datomic)
      :entity
      adapters.menu/database->internal))

(s/defn lookup :- (s/maybe models.menu/Menu)
  [menu-id :- s/Uuid
   database]
  (some-> (d/q '[:find (pull ?menu [*])
                 :in $ ?menu-id
                 :where [?menu :menu/id ?menu-id]] database menu-id)
          ffirst
          adapters.menu/database->internal))

(s/defn all :- [models.menu/Menu]
  [database]
  (some-> (d/q '[:find (pull ?menu [*])
                 :in $
                 :where [?menu :menu/id _]] database)
          (->> (mapv first))
          (->> (mapv #(dissoc % :db/id)))
          (->> (map adapters.menu/database->internal))))

(s/defn retract!
  [menu-id :- s/Uuid
   datomic]
  (let [menu-entid (d/entid (d/db datomic) [:menu/id menu-id])]
    @(d/transact datomic [[:db.fn/retractEntity menu-entid]])))
