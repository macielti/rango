(ns rango.controllers.student
  (:require
   [datomic.api :as d]
   [rango.db.datomic.student :as database.student]
   [rango.models.student :as models.student]
   [schema.core :as s]))

(s/defn create! :- models.student/Student
  [student :- models.student/Student
   datomic]
  (if-let [student' (database.student/lookup-by-code (:student/code student) (d/db datomic))]
    student'
    (database.student/insert! student datomic)))

(s/defn fetch-all :- [models.student/Student]
  [datomic]
  (database.student/all (d/db datomic)))
