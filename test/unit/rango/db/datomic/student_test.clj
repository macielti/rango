(ns rango.db.datomic.student-test
  (:require [clojure.test :refer [is testing]]
            [common-clj.integrant-components.datomic :as component.datomic]
            [common-clj.test.helper.schema :as test.helper.schema]
            [datomic.api :as d]
            [fixtures.student]
            [matcher-combinators.test :refer [match?]]
            [rango.db.datomic.config :as database.config]
            [rango.db.datomic.student :as database.student]
            [rango.models.student :as models.student]
            [schema.test :as s]))

(s/deftest insert-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]
    (testing "Should be able to insert a student"
      (is (match? {:student/id fixtures.student/student-id}
                  (database.student/insert! fixtures.student/student datomic))))))

(s/deftest all-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]
    (database.student/insert! fixtures.student/student datomic)
    (database.student/insert! (test.helper.schema/generate models.student/Student {}) datomic)
    (database.student/insert! (test.helper.schema/generate models.student/Student {}) datomic)

    (testing "Should be able to query all students"
      (is (match? [{:student/id uuid?}
                   {:student/id uuid?}
                   {:student/id uuid?}]
                  (database.student/all (d/db datomic)))))))

(s/deftest retract-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]
    (testing "Should be able to insert a student"
      (is (match? {:student/id fixtures.student/student-id}
                  (database.student/insert! fixtures.student/student datomic))))

    (testing "Should be able to query all students after insertion"
      (is (match? [{:student/id fixtures.student/student-id}]
                  (database.student/all (d/db datomic)))))

    (testing "Should be able to retract a student"
      (is (match? {:tempids {}}
                  (database.student/retract! fixtures.student/student-id datomic))))

    (testing "Student is not present after retraction"
      (is (match? []
                  (database.student/all (d/db datomic)))))))
