(ns rango.adapters.reservation
  (:require [java-time.api :as jt]
            [rango.models.reservation :as models.reservation]
            [rango.wire.datomic.reservation :as wire.datomic.reservation]
            [rango.wire.out.reservation :as wire.out.reservation]
            [schema.core :as s]))

(s/defn internal->database :- wire.datomic.reservation/Reservation
  [{:reservation/keys [created-at] :as reservation} :- models.reservation/Reservation]
  (assoc reservation :reservation/created-at (-> (jt/zoned-date-time created-at (jt/zone-id "UTC"))
                                                 jt/java-date)))

(s/defn database->internal :- models.reservation/Reservation
  [{:reservation/keys [created-at] :as reservation} :- wire.datomic.reservation/Reservation]
  (assoc reservation :reservation/created-at (-> (jt/zoned-date-time created-at (jt/zone-id "UTC"))
                                                 jt/local-date-time)))

(s/defn internal->wire :- wire.out.reservation/Reservation
  [{:reservation/keys [id student-id menu-id created-at]} :- models.reservation/Reservation]
  {:id         (str id)
   :student-id (str student-id)
   :menu-id    (str menu-id)
   :created-at (str created-at)})
