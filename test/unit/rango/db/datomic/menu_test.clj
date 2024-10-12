(ns rango.db.datomic.menu-test
  (:require [clojure.test :refer [is testing]]
            [common-clj.integrant-components.datomic :as component.datomic]
            [common-clj.test.helper.schema :as test.helper.schema]
            [datomic.api :as d]
            [fixtures.menu]
            [matcher-combinators.test :refer [match?]]
            [rango.db.datomic.config :as database.config]
            [rango.db.datomic.menu :as database.menu]
            [rango.models.menu :as models.menu]
            [schema.test :as s]))

(s/deftest insert-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]
    (testing "Should be able to insert a menu"
      (is (match? fixtures.menu/menu
                  (database.menu/insert! fixtures.menu/menu datomic))))))

(s/deftest lookup-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]

    (testing "Should be able to insert a menu"
      (is (match? fixtures.menu/menu
                  (database.menu/insert! fixtures.menu/menu datomic))))

    (testing "Should be able to lookup a menu"
      (is (match? fixtures.menu/menu
                  (database.menu/lookup fixtures.menu/menu-id (d/db datomic)))))

    (testing "Should be able to lookup a menu"
      (is (nil? (database.menu/lookup (random-uuid) (d/db datomic)))))))

(s/deftest all-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]
    (database.menu/insert! fixtures.menu/menu datomic)
    (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) datomic)
    (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) datomic)

    (testing "Should be able to query all menus entities"
      (is (match? [fixtures.menu/menu
                   {:menu/id uuid?}
                   {:menu/id uuid?}]
                  (database.menu/all (d/db datomic)))))))

(s/deftest retract-test
  (let [datomic (component.datomic/mocked-datomic database.config/schemas)]
    (testing "Should be able to insert a menu"
      (is (match? fixtures.menu/menu
                  (database.menu/insert! fixtures.menu/menu datomic))))

    (testing "Should be able to retract a menu"
      (is (match? {:tempids {}}
                  (database.menu/retract! fixtures.menu/menu-id datomic))))

    (testing "Should not be able to query a menu after retraction"
      (is (nil? (database.menu/lookup fixtures.menu/menu-id (d/db datomic)))))))
