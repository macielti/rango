(ns rango.db.datomic.student
  (:require [common-clj.integrant-components.datomic :as component.datomic]
            [datomic.api :as d]
            [rango.adapters.student :as adapters.student]
            [rango.models.student :as models.student]
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
          (dissoc :db/id)
          adapters.student/database->internal))

(s/defn all :- [models.student/Student]
  [database]
  (some-> (d/q '[:find (pull ?student [*])
                 :in $
                 :where [?student :student/id _]] database)
          (->> (mapv first))
          (->> (mapv #(dissoc % :db/id)))
          (->> (map adapters.student/database->internal))))

(s/defn by-menu-reservation :- [models.student/Student]
  [menu-id :- s/Uuid
   database]
  (some-> (d/q '[:find (pull ?student [*])
                 :in $ ?menu-id
                 :where [?reservation :reservation/menu-id ?menu-id]
                 [?reservation :reservation/student-id ?student-id]
                 [?student :student/id ?student-id]] database menu-id)
          (->> (mapv first))
          (->> (mapv #(dissoc % :db/id)))
          (->> (map adapters.student/database->internal))))

(s/defn retract!
  [student-id :- s/Uuid
   datomic]
  (let [student-entid (d/entid (d/db datomic) [:student/id student-id])]
    @(d/transact datomic [[:db.fn/retractEntity student-entid]])))
