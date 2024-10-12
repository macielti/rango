(ns rango.interceptors.student
  (:require [common-clj.io.interceptors.datomic :as io.interceptors.datomic])
  (:import (java.util UUID)))

(defn student-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :student-id UUID/fromString))

(def student-resource-existence-interceptor-check
  (io.interceptors.datomic/resource-existence-check-interceptor student-resource-identifier-fn
                                                                '[:find (pull ?resource [*])
                                                                  :in $ ?resource-identifier
                                                                  :where [?resource :student/id ?resource-identifier]]))
