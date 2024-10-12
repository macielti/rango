(ns rango.logic.reservation-test
  (:require [clojure.test :refer [is testing]]
            [fixtures.menu]
            [fixtures.student]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango.logic.reservation :as logic.reservation]
            [schema.test :as s]))

(s/deftest ->reservation-test
  (testing "that we are able to create a reservation"
    (is (match? {:reservation/id         uuid?
                 :reservation/student-id fixtures.student/student-id
                 :reservation/menu-id    fixtures.menu/menu-id
                 :reservation/created-at jt/local-date-time?}
                (logic.reservation/->reservation fixtures.student/student
                                                 fixtures.menu/menu)))))
