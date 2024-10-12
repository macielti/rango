(ns rango.controllers.menu
  (:require [datomic.api :as d]
            [rango.db.datomic.menu :as database.menu]
            [rango.models.menu :as models.menu]
            [schema.core :as s]))

(s/defn create! :- models.menu/Menu
  [menu :- models.menu/Menu
   datomic]
  (database.menu/insert! menu datomic))

(s/defn fetch-all :- [models.menu/Menu]
  [datomic]
  (database.menu/all (d/db datomic)))

(s/defn retract!
  [menu-id :- s/Uuid
   datomic]
  (database.menu/retract! menu-id datomic))
