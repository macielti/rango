(ns rango.adapters.menu
  (:require [java-time.api :as jt]
            [rango.models.menu :as models.menu]
            [rango.wire.datomic.menu :as wire.datomic.menu]
            [rango.wire.in.menu :as wire.in.menu]
            [rango.wire.out.menu :as wire.out.menu]
            [schema.core :as s]))

(s/defn wire->internal :- models.menu/Menu
  [{:keys [reference-date description]} :- wire.in.menu/Menu]
  {:menu/id             (random-uuid)
   :menu/description    description
   :menu/reference-date (jt/local-date reference-date)
   :menu/created-at     (jt/local-date-time)})

(s/defn internal->database :- wire.datomic.menu/Menu
  [{:menu/keys [created-at reference-date] :as menu} :- models.menu/Menu]
  (assoc menu :menu/created-at (-> (jt/zoned-date-time created-at (jt/zone-id "UTC"))
                                   jt/java-date)
              :menu/reference-date (-> (jt/zoned-date-time reference-date (jt/zone-id "UTC"))
                                       jt/java-date)))

(s/defn database->internal :- models.menu/Menu
  [{:menu/keys [created-at reference-date] :as menu} :- wire.datomic.menu/Menu]
  (assoc menu :menu/created-at (-> (jt/zoned-date-time created-at (jt/zone-id "UTC"))
                                   jt/local-date-time)
              :menu/reference-date (-> (jt/zoned-date-time reference-date (jt/zone-id "UTC"))
                                       jt/local-date)))

(s/defn internal->wire :- wire.out.menu/Menu
  [{:menu/keys [id description reference-date created-at]} :- models.menu/Menu]
  {:id             (str id)
   :reference-date (str reference-date)
   :description    description
   :created-at     (str created-at)})

(s/defn postgresql->internal :- models.menu/Menu
  [{:keys [] :as menu}]
  {:menu/id             (:id menu)
   :menu/reference-date (:reference_date menu)
   :menu/description    (:description menu)
   :menu/created-at     (:created_at menu)})

