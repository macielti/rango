(ns rango.db.datomic.config
  (:require [rango.wire.datomic.menu :as wire.datomic.menu]
            [rango.wire.datomic.student :as wire.datomic.student]))

(def schemas (concat []
                     wire.datomic.student/student
                     wire.datomic.menu/menu))
