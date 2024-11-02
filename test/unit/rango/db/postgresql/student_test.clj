(ns rango.db.postgresql.student-test
  (:require [clojure.test :refer :all]
            [common-clj.integrant-components.postgresql :as postgresql]
            [common-clj.test.helper.schema :as test.helper.schema]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango.db.postgresql.reservation :as database.reservation]
            [rango.db.postgresql.student :as database.student]
            [rango.models.reservation :as models.reservation]
            [rango.models.student :as models.student]
            [schema.test :as s]))

(def student-code "74b4814a")
(def menu-id (random-uuid))
(def student-id (random-uuid))

(def student
  (test.helper.schema/generate models.student/Student {:student/id   student-id
                                                       :student/code student-code}))

(def reservation
  (test.helper.schema/generate models.reservation/Reservation {:reservation/menu-id    menu-id
                                                               :reservation/student-id student-id}))

(s/deftest insert-test
  (testing "Should insert a reservation"
    (let [conn (postgresql/mocked-postgresql-conn)]
      (is (match? {:student/id         uuid?
                   :student/code       string?
                   :student/name       string?
                   :student/class      keyword?
                   :student/created-at jt/local-date-time?}
                  (database.student/insert! student conn))))))

(s/deftest lookup-by-code-test
  (testing "Should be able to query a student by code"
    (let [conn (postgresql/mocked-postgresql-conn)]
      (database.student/insert! student conn)

      (is (match? {:student/code student-code}
                  (database.student/lookup-by-code student-code conn)))

      (is (nil? (database.student/lookup-by-code "8c045cae" conn))))))

(s/deftest all-test
  (testing "Should be able to query all students"
    (let [conn (postgresql/mocked-postgresql-conn)]
      (database.student/insert! student conn)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) conn)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) conn)

      (is (match? [{:student/code student-code}
                   {:student/code string?}
                   {:student/code string?}]
                  (database.student/all conn))))))

(s/deftest by-menu-reservation-test
  (testing "Should be able to query students by menu reservation"
    (let [conn (postgresql/mocked-postgresql-conn)]
      (database.student/insert! student conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation reservation) conn)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) conn)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) conn)

      (is (match? [{:student/code student-code}]
                  (database.student/by-menu-reservation menu-id conn))))))

(s/deftest retract-test
  (testing "Should be able to retract a student"
    (let [conn (postgresql/mocked-postgresql-conn)]
      (database.student/insert! student conn)

      (is (match? {:deleted 1}
                  (database.student/retract! student-id conn)))

      (is (match? []
                  (database.student/all conn))))))
