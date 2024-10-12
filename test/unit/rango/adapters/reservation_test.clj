(ns rango.adapters.reservation-test
  (:require [clj-uuid]
            [clojure.test :refer [is testing]]
            [fixtures.menu]
            [fixtures.reservation]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango.adapters.reservation :as adapters.reservation]
            [schema.test :as s]))

(s/deftest internal->database-test
  (testing "That we are able to convert an internal reservation to a database reservation"
    (is (match? {:reservation/id         fixtures.reservation/reservation-id
                 :reservation/student-id uuid?
                 :reservation/menu-id    fixtures.menu/menu-id
                 :reservation/created-at inst?}
                (adapters.reservation/internal->database fixtures.reservation/reservation)))))

(s/deftest database->internal-test
  (testing "That we are able to convert a database reservation to an internal reservation"
    (is (match? {:reservation/id         fixtures.reservation/reservation-id,
                 :reservation/student-id uuid?
                 :reservation/menu-id    fixtures.menu/menu-id
                 :reservation/created-at jt/local-date-time?}
                (adapters.reservation/database->internal fixtures.reservation/database-reservation)))))

(s/deftest internal->wire-test
  (testing "That we are able to convert an internal reservation to a wire reservation"
    (is (match? {:id         clj-uuid/uuid-string?
                 :student-id clj-uuid/uuid-string?
                 :menu-id    clj-uuid/uuid-string?
                 :created-at string?}
                (adapters.reservation/internal->wire fixtures.reservation/reservation)))))
