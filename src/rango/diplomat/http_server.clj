(ns rango.diplomat.http-server
  (:require [common-clj.io.interceptors.customer :as io.interceptors.customer]
            [common-clj.traceability.core :as common-traceability]
            [rango.diplomat.http-server.menu :as diplomat.http-server.menu]
            [rango.diplomat.http-server.reservation :as diplomat.http-server.reservation]
            [rango.diplomat.http-server.student :as diplomat.http-server.student]
            [rango.interceptors.menu :as interceptors.menu]
            [rango.interceptors.student :as interceptors.student]))

(def routes [["/api/students"
              :post [io.interceptors.customer/identity-interceptor
                     (io.interceptors.customer/required-roles-interceptor [:admin])
                     (common-traceability/http-with-correlation-id diplomat.http-server.student/create-student!)]
              :route-name :create-student]

             ["/api/students"
              :get [io.interceptors.customer/identity-interceptor
                    (io.interceptors.customer/required-roles-interceptor [:admin])
                    (common-traceability/http-with-correlation-id diplomat.http-server.student/fetch-all)]
              :route-name :fetch-all-students]

             ["/api/students/:student-id"
              :delete [io.interceptors.customer/identity-interceptor
                       (io.interceptors.customer/required-roles-interceptor [:admin])
                       interceptors.student/student-resource-existence-interceptor-check
                       (common-traceability/http-with-correlation-id diplomat.http-server.student/retract-student!)]
              :route-name :retract-student]

             ["/api/menus"
              :post [io.interceptors.customer/identity-interceptor
                     (io.interceptors.customer/required-roles-interceptor [:admin])
                     (common-traceability/http-with-correlation-id diplomat.http-server.menu/create-menu!)]
              :route-name :create-menu]

             ["/api/menus/:menu-id"
              :delete [io.interceptors.customer/identity-interceptor
                       (io.interceptors.customer/required-roles-interceptor [:admin])
                       interceptors.menu/menu-resource-existence-interceptor-check
                       (common-traceability/http-with-correlation-id diplomat.http-server.menu/retract-menu!)]
              :route-name :retract-menu]

             ["/api/menus"
              :get [(common-traceability/http-with-correlation-id diplomat.http-server.menu/fetch-all)]
              :route-name :fetch-all-menus]

             ["/api/reservations"
              :post [(common-traceability/http-with-correlation-id diplomat.http-server.reservation/create-reservation!)]
              :route-name :create-reservation]

             ["/api/reservations/menus/:menu-id"
              :get [interceptors.menu/menu-resource-existence-interceptor-check
                    (common-traceability/http-with-correlation-id diplomat.http-server.reservation/fetch-reservations-by-menu)]
              :route-name :fetch-reservations-by-menu]

             ["/api/students-by-menu-reservations/:menu-id"
              :get [io.interceptors.customer/identity-interceptor
                    (io.interceptors.customer/required-roles-interceptor [:admin])
                    interceptors.menu/menu-resource-existence-interceptor-check
                    (common-traceability/http-with-correlation-id diplomat.http-server.student/fetch-students-by-reservations-menu)]
              :route-name :fetch-students-by-menu-reservations]])
