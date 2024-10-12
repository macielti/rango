(ns rango.interceptors.menu
  (:require [common-clj.io.interceptors.datomic :as io.interceptors.datomic])
  (:import (java.util UUID)))

(defn menu-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :menu-id UUID/fromString))

(def menu-resource-existence-interceptor-check
  (io.interceptors.datomic/resource-existence-check-interceptor menu-resource-identifier-fn
                                                                '[:find (pull ?resource [*])
                                                                  :in $ ?resource-identifier
                                                                  :where [?resource :menu/id ?resource-identifier]]))
