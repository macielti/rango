(ns rango.logic.reservation-test
  (:require [clojure.test :refer :all]
            [java-time.api :as jt]
            [rango.logic.reservation :as logic.reservation]
            [matcher-combinators.test :refer [match?]]
            [schema.test :as s]
            [fixtures.student]
            [fixtures.menu]))

(s/deftest ->reservation-test
  (testing "that we are able to create a reservation"
    (is (match? {:reservation/id         uuid?
                 :reservation/student-id fixtures.student/student-id
                 :reservation/menu-id    fixtures.menu/menu-id
                 :reservation/created-at jt/local-date-time?}
                (logic.reservation/->reservation fixtures.student/student
                                                 fixtures.menu/menu)))))
