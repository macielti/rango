(ns rango.controllers.menu
  (:require [datomic.api :as d]
            [rango.models.menu :as models.menu]
            [schema.core :as s]
            [rango.db.datomic.menu :as database.menu]))

(s/defn create! :- models.menu/Menu
  [menu :- models.menu/Menu
   datomic]
  (database.menu/insert! menu datomic))

(s/defn fetch-all :- [models.menu/Menu]
  [datomic]
  (database.menu/all (d/db datomic)))
