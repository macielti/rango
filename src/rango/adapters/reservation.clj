(ns rango.adapters.reservation
  (:require [rango.models.reservation :as models.reservation]
            [rango.wire.out.reservation :as wire.out.reservation]
            [schema.core :as s]))

(s/defn internal->wire :- wire.out.reservation/Reservation
  [{:reservation/keys [id student-id menu-id created-at]} :- models.reservation/Reservation]
  {:id         (str id)
   :student-id (str student-id)
   :menu-id    (str menu-id)
   :created-at (str created-at)})

(s/defn postgresql->internal :- models.reservation/Reservation
  [{:keys [id student_id menu_id created_at]}]
  {:reservation/id         id
   :reservation/student-id student_id
   :reservation/menu-id    menu_id
   :reservation/created-at created_at})
