(ns rango.interceptors.menu
  (:require [common-clj.io.interceptors.postgresql :as io.interceptors.postgresql])
  (:import (java.util UUID)))

(defn menu-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :menu-id UUID/fromString))

(def menu-resource-existence-interceptor-check
  (io.interceptors.postgresql/resource-existence-check-interceptor menu-resource-identifier-fn
                                                                   "SELECT * FROM menus WHERE id = $1"))
