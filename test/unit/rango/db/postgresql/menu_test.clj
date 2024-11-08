(ns rango.db.postgresql.menu-test
  (:require [clojure.test :refer [is testing]]
            [common-test-clj.component.postgresql-mock :as component.postgresql-mock]
            [common-test-clj.helpers.schema :as test.helper.schema]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango.db.postgresql.menu :as database.menu]
            [rango.models.menu :as models.menu]
            [schema.test :as s]))

(def menu-id (random-uuid))
(def menu
  (test.helper.schema/generate models.menu/Menu {:menu/id menu-id}))

(s/deftest insert-test
  (testing "Should insert a menu"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (is (match? {:menu/description    string?
                   :menu/reference-date jt/local-date?
                   :menu/id             uuid?
                   :menu/created-at     jt/local-date-time?}
                  (database.menu/insert! menu conn))))))

(s/deftest all-test
  (testing "Should return all menus"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)

      (is (match? [{:menu/id uuid?}
                   {:menu/id uuid?}
                   {:menu/id uuid?}]
                  (database.menu/all conn))))))

(s/deftest retract!-test
  (testing "Should return all menus"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (database.menu/insert! menu conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)

      (is (= {:deleted 1}
             (database.menu/retract! menu-id conn)))

      (is (match? [{:menu/id uuid?}
                   {:menu/id uuid?}]
                  (database.menu/all conn)))

      (is (= {:deleted 0}
             (database.menu/retract! (random-uuid) conn))))))
