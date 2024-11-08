(ns fixtures.reservation
  (:require [common-test-clj.helpers.schema :as test.helper.schema]
            [fixtures.menu]
            [fixtures.student]
            [rango.models.reservation :as models.reservation]))

(defonce reservation-id (random-uuid))

(def reservation
  (test.helper.schema/generate models.reservation/Reservation
                               {:reservation/id         reservation-id
                                :reservation/student-id fixtures.student/student-id
                                :reservation/menu-id    fixtures.menu/menu-id}))
