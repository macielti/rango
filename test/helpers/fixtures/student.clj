(ns fixtures.student
  (:require [clojure.test :refer :all]
            [common-clj.test.helper.schema :as test.helper.schema]
            [rango.models.student :as models.student]
            [rango.wire.datomic.student :as wire.datomic.student]
            [rango.wire.in.student :as wire.in.student]))

(defonce student-id (random-uuid))
(def student-name "Manuel Gomes")
(def student-code "random-code")

(def student
  (test.helper.schema/generate models.student/Student
                               {:student/id    student-id
                                :student/name  student-name
                                :student/code  student-code
                                :student/class :random-class}))

(def wire-student
  (test.helper.schema/generate wire.in.student/Student
                               {:code  student-code
                                :name  student-name
                                :class "random-class"}))

(def database-student
  (test.helper.schema/generate wire.datomic.student/Student
                               {:student/id    student-id
                                :student/name  student-name
                                :student/code  student-code
                                :student/class :random-class}))