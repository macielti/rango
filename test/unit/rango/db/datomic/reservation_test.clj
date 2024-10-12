(ns rango.db.datomic.reservation-test
  (:require [clojure.test :refer [is testing]]
            [common-clj.integrant-components.datomic :as component.datomic]
            [common-clj.test.helper.schema :as test.helper.schema]
            [datomic.api :as d]
            [fixtures.menu]
            [fixtures.reservation]
            [fixtures.student]
            [matcher-combinators.test :refer [match?]]
            [rango.db.datomic.config :as database.config]
            [rango.db.datomic.reservation :as database.reservation]
            [rango.models.reservation :as models.reservation]
            [schema.test :as s]))

(s/deftest insert-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]
    (testing "Should be able to insert a reservation"
      (is (match? fixtures.reservation/reservation
                  (database.reservation/insert! fixtures.reservation/reservation datomic))))))

(s/deftest by-menu-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]
    (database.reservation/insert! fixtures.reservation/reservation datomic)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) datomic)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) datomic)

    (testing "Should be able to query reservations by menu"
      (is (match? [fixtures.reservation/reservation]
                  (database.reservation/by-menu fixtures.menu/menu-id (d/db datomic)))))))

(s/deftest lookup-by-student-and-menu-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]
    (database.reservation/insert! fixtures.reservation/reservation datomic)
    (testing "Should be able to insert a reservation"
      (is (match? fixtures.reservation/reservation
                  (database.reservation/lookup-by-student-and-menu fixtures.student/student-id fixtures.menu/menu-id (d/db datomic))))

      (is (nil? (database.reservation/lookup-by-student-and-menu (random-uuid) (random-uuid) (d/db datomic)))))))
