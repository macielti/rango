(ns fixtures.student
  (:require [clojure.test :refer :all]
            [common-clj.test.helper.schema :as test.helper.schema]
            [rango.models.student :as models.student]))

(defonce student-id (random-uuid))
(def student-name "Manuel Gomes")

(def student
  (test.helper.schema/generate models.student/Student
                               {:student/id   student-id
                                :student/name student-name}))
