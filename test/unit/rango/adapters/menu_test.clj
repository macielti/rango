(ns rango.adapters.menu-test
  (:require [clj-uuid]
            [clojure.test :refer [is testing]]
            [fixtures.menu]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango.adapters.menu :as adapters.menu]
            [schema.test :as s]))

(s/deftest wire->internal-test
  (testing "That we are able to convert a wire menu to an internal menu"
    (is (match? {:menu/id             uuid?
                 :menu/description    "This is a test menu"
                 :menu/reference-date (jt/local-date "2024-01-01")
                 :menu/created-at     jt/local-date-time?}
                (adapters.menu/wire->internal fixtures.menu/wire-in-menu)))))

(s/deftest internal->wire-test
  (testing "That we are able to convert an internal menu to a wire menu"
    (is (match? {:id             clj-uuid/uuid-string?
                 :reference-date "2024-01-01"
                 :description    "This is a test menu"
                 :created-at     string?}
                (adapters.menu/internal->wire fixtures.menu/menu)))))
