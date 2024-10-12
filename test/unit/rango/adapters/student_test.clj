(ns rango.adapters.student-test
  (:require [clj-uuid]
            [clojure.test :refer [is testing]]
            [fixtures.student]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango.adapters.student :as adapters.student]
            [schema.test :as s]))

(s/deftest wire->internal-test
  (testing "That we are able to convert a wire student to an internal student"
    (is (match? {:student/id         uuid?
                 :student/code       "random-code"
                 :student/name       "Manuel Gomes"
                 :student/class      :random-class
                 :student/created-at jt/local-date-time?}
                (adapters.student/wire->internal fixtures.student/wire-student)))))

(s/deftest internal->database-test
  (testing "That we are able to convert an internal student to a database student"
    (is (match? {:student/id         fixtures.student/student-id
                 :student/code       fixtures.student/student-code
                 :student/name       "Manuel Gomes"
                 :student/class      :random-class
                 :student/created-at inst?}
                (adapters.student/internal->database fixtures.student/student)))))

(s/deftest database->internal-test
  (testing "That we are able to convert a database student to an internal student"
    (is (match? {:student/id         fixtures.student/student-id
                 :student/code       "random-code"
                 :student/name       "Manuel Gomes"
                 :student/class      :random-class
                 :student/created-at jt/local-date-time?}
                (adapters.student/database->internal fixtures.student/database-student)))))

(s/deftest internal->wire-test
  (testing "That we are able to convert an internal student to a wire student"
    (is (match? {:id         clj-uuid/uuid-string?
                 :code       "random-code"
                 :name       "Manuel Gomes"
                 :class      "random-class"
                 :created-at string?}
                (adapters.student/internal->wire fixtures.student/student)))))
