(ns rango.db.datomic.config
  (:require [rango.wire.datomic.student :as wire.datomic.student]))

(def schemas (concat []
                     wire.datomic.student/student))
