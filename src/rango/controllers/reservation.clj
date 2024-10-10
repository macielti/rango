(ns rango.controllers.reservation
  (:require [datomic.api :as d]
            [rango.db.datomic.menu :as database.menu]
            [rango.db.datomic.reservation :as database.reservation]
            [rango.db.datomic.student :as database.student]
            [rango.logic.reservation :as logic.reservation]
            [rango.models.reservation :as models.reservation]
            [schema.core :as s]))

(s/defn create! :- models.reservation/Reservation
  [student-code :- s/Str
   menu-id :- s/Uuid
   datomic]
  (let [student (database.student/lookup-by-code student-code (d/db datomic))
        menu (database.menu/lookup menu-id (d/db datomic))]
    (database.reservation/insert! (logic.reservation/->reservation student menu) datomic)))
