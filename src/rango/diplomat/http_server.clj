(ns rango.diplomat.http-server
  (:require [common-clj.traceability.core :as common-traceability]
            [rango.diplomat.http-server.student :as diplomat.http-server.student]))

(def routes [["/api/students"
              :post [(common-traceability/http-with-correlation-id diplomat.http-server.student/create-student!)]
              :route-name :create-student]
             ["/api/students"
              :get [(common-traceability/http-with-correlation-id diplomat.http-server.student/fetch-all)]
              :route-name :fetch-all-students]])
