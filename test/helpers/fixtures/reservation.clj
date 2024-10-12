(ns fixtures.reservation
  (:require [common-clj.test.helper.schema :as test.helper.schema]
            [fixtures.menu]
            [fixtures.student]
            [rango.models.reservation :as models.reservation]
            [rango.wire.datomic.reservation :as wire.datomic.reservation]))

(defonce reservation-id (random-uuid))

(def reservation
  (test.helper.schema/generate models.reservation/Reservation
                               {:reservation/id         reservation-id
                                :reservation/student-id fixtures.student/student-id
                                :reservation/menu-id    fixtures.menu/menu-id}))

(def database-reservation
  (test.helper.schema/generate wire.datomic.reservation/Reservation
                               {:reservation/id      reservation-id
                                :reservation/menu-id fixtures.menu/menu-id}))
