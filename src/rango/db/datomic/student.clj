(ns rango.db.datomic.student
  (:require [rango.adapters.student :as adapters.student]
            [rango.models.student :as models.student]
            [common-clj.integrant-components.datomic :as component.datomic]
            [datomic.api :as d]
            [schema.core :as s]))

(s/defn insert! :- models.student/Student
  [student :- models.student/Student
   datomic]
  (-> (component.datomic/transact-and-lookup-entity! :student/id (adapters.student/internal->database student) datomic)
      :entity
      adapters.student/database->internal))

(s/defn lookup-by-code :- (s/maybe models.student/Student)
  [code :- s/Str
   database]
  (some-> (d/q '[:find (pull ?student [*])
                 :in $ ?code
                 :where [?student :student/code ?code]] database code)
          ffirst
          adapters.student/database->internal))
