(ns rango.adapters.reservation-test
  (:require [clojure.test :refer :all]
            [java-time.api :as jt]
            [rango.adapters.reservation :as adapters.reservation]
            [matcher-combinators.test :refer [match?]]
            [fixtures.reservation]
            [fixtures.menu]
            [schema.test :as s]
            [clj-uuid]))

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
