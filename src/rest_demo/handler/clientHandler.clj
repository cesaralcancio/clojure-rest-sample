(ns rest-demo.handler.clientHandler
  (:require [clojure.pprint :as pp]
            [ring.util.request :as r.u.request]
            [cheshire.core :as cheshire]
            [rest-demo.model.clientModel :as client.model]))

(defn addclient-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (let [str-json (r.u.request/body-string req)
                  map-json (cheshire/parse-string str-json true)
                  firstname (:firstname map-json)
                  surname (:surname map-json)
                  email (:email map-json)
                  birthday (:birthday map-json)
                  response (client.model/add-client! firstname surname email birthday)]
              (cheshire/generate-string response)
              )})

; testing
;(let [lista (client.model/add-client! "Jose" "Augusto" "jose@gmail.com" "1991-05-01")]
;  (println lista)
;  (println (cheshire/generate-string lista))
;  )
