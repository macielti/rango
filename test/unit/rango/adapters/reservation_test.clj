(ns rango.adapters.reservation-test
  (:require [clj-uuid]
            [clojure.test :refer [is testing]]
            [fixtures.menu]
            [fixtures.reservation]
            [matcher-combinators.test :refer [match?]]
            [rango.adapters.reservation :as adapters.reservation]
            [schema.test :as s]))

(s/deftest internal->wire-test
  (testing "That we are able to convert an internal reservation to a wire reservation"
    (is (match? {:id         clj-uuid/uuid-string?
                 :student-id clj-uuid/uuid-string?
                 :menu-id    clj-uuid/uuid-string?
                 :created-at string?}
                (adapters.reservation/internal->wire fixtures.reservation/reservation)))))
