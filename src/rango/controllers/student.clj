(ns rango.controllers.student
  (:require [rango.models.student :as models.student]
            [rango.db.datomic.student :as database.student]
            [schema.core :as s]))

(s/defn create! :- models.student/Student
  [student :- models.student/Student
   datomic]
  (database.student/insert! student datomic))
