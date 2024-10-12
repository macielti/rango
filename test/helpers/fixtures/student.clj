(ns fixtures.student
  (:require [clojure.test :refer :all]
            [rango.models.student :as models.student]
            [common-clj.test.helper.schema :as test.helper.schema]))

(defonce student-id (random-uuid))
(def student-name "Manuel Gomes")

(def student
  (test.helper.schema/generate models.student/Student
                               {:student/id   student-id
                                :student/name student-name}))
