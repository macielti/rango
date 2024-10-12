(ns fixtures.reservation
  (:require [clojure.test :refer :all]
            [common-clj.test.helper.schema :as test.helper.schema]
            [rango.wire.datomic.reservation :as wire.datomic.reservation]
            [fixtures.menu]))

(defonce reservation-id (random-uuid))

(def reservation
  (test.helper.schema/generate rango.models.reservation/Reservation
                               {:reservation/id      reservation-id
                                :reservation/menu-id fixtures.menu/menu-id}))

(def database-reservation
  (test.helper.schema/generate wire.datomic.reservation/Reservation
                               {:reservation/id      reservation-id
                                :reservation/menu-id fixtures.menu/menu-id}))
