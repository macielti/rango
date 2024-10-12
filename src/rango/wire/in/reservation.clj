(ns rango.wire.in.reservation
  (:require [schema.core :as s]))

(def reservation
  {:reservation-id s/Str
   :student-code   s/Str})

#_(s/defschema Reservation reservation)
