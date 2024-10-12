(ns fixtures.menu
  (:require [common-clj.test.helper.schema :as test.helper.schema]
            [java-time.api :as jt]
            [rango.models.menu :as models.menu]
            [rango.wire.datomic.menu :as wire.datomic.menu]
            [rango.wire.in.menu :as wire.in.menu]))

(defonce menu-id (random-uuid))
(def menu-description "This is a test menu")

(def wire-in-menu
  (test.helper.schema/generate wire.in.menu/Menu
                               {:reference-date "2024-01-01"
                                :description    menu-description}))

(def menu
  (test.helper.schema/generate models.menu/Menu
                               {:menu/id             menu-id
                                :menu/description    menu-description
                                :menu/reference-date (jt/local-date "2024-01-01")}))

(def database-menu
  (test.helper.schema/generate wire.datomic.menu/Menu
                               {:menu/id          menu-id
                                :menu/description menu-description}))
