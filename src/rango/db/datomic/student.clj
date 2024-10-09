(ns rango.db.datomic.student
  (:require [rango.adapters.student :as adapters.student]
            [rango.models.student :as models.student]
            [common-clj.integrant-components.datomic :as component.datomic]
            [schema.core :as s]))

(s/defn insert! :- models.student/Student
  [student :- models.student/Student
   datomic]
  (-> (component.datomic/transact-and-lookup-entity! :student/id student datomic)
      :entity
      adapters.student/database->internal))
