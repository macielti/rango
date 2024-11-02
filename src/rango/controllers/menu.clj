(ns rango.controllers.menu
  (:require [pg.pool :as pool]
            [rango.db.postgresql.menu :as database.menu]
            [rango.models.menu :as models.menu]
            [schema.core :as s]))

(s/defn create! :- models.menu/Menu
  [menu :- models.menu/Menu
   postgresql]
  (pool/with-connection [database-conn postgresql]
    (database.menu/insert! menu database-conn)))

(s/defn fetch-all :- [models.menu/Menu]
  [postgresql]
  (pool/with-connection [database-conn postgresql]
    (database.menu/all database-conn)))

(s/defn retract!
  [menu-id :- s/Uuid
   postgresql]
  (pool/with-connection [database-conn postgresql]
    (database.menu/retract! menu-id database-conn)))
