(ns rango.db.postgresql.reservation-test
  (:require [clojure.test :refer :all]
            [common-clj.integrant-components.postgresql :as postgresql]
            [common-clj.test.helper.schema :as test.helper.schema]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango.db.postgresql.reservation :as database.reservation]
            [rango.models.reservation :as models.reservation]
            [schema.test :as s]))

(def menu-id (random-uuid))
(def student-id (random-uuid))
(def reservation
  (test.helper.schema/generate models.reservation/Reservation {:reservation/menu-id    menu-id
                                                               :reservation/student-id student-id}))

(s/deftest insert-test
  (testing "Should insert a reservation"
    (let [conn (postgresql/mocked-postgresql-conn)]
      (is (match? {:reservation/id         uuid?
                   :reservation/student-id uuid?
                   :reservation/menu-id    uuid?
                   :reservation/created-at jt/local-date-time?}
                  (database.reservation/insert! reservation conn))))))

(s/deftest by-menu-test
  (testing "Should insert a reservation"
    (let [conn (postgresql/mocked-postgresql-conn)]
      (database.reservation/insert! reservation conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) conn)

      (is (match? [{:reservation/menu-id menu-id}]
                  (database.reservation/by-menu menu-id conn)))

      (is (= []
             (database.reservation/by-menu (random-uuid) conn))))))

(s/deftest lookup-by-student-and-menu-test
  (testing "Should insert a reservation"
    (let [conn (postgresql/mocked-postgresql-conn)]
      (database.reservation/insert! reservation conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) conn)

      (is (match? {:reservation/menu-id    menu-id
                   :reservation/student-id student-id}
                  (database.reservation/lookup-by-student-and-menu student-id menu-id conn)))

      (is (nil? (database.reservation/lookup-by-student-and-menu student-id (random-uuid) conn)))

      (is (nil? (database.reservation/lookup-by-student-and-menu (random-uuid) menu-id conn)))

      (is (nil? (database.reservation/lookup-by-student-and-menu (random-uuid) (random-uuid) conn))))))
