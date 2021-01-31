(ns rest-demo.handler.clientHandler
  (:require [clojure.pprint :as pp]
            [ring.util.request :as r.u.request]
            [cheshire.core :as cheshire]
            [clojure.string :as s]
            [rest-demo.model.clientModel :as client.model]))

; (def smile (cheshire/generate-smile {:foo "bar" :baz 5}))
; (cheshire/parse-smile smile)
; (cheshire/generate-string {:foo "bar" :baz 5})

(defn clients-handler [req]
  {:status  200
   :headers {"Content-type" "text/json"}
   :body    (->> @client.model/client-collection
                 cheshire/generate-string)})

(defn apply-response [client]
  (if-let [xpto (empty? client)]
    {:message "There's no client with this uuid"}
    client))

(defn client-handler [req]
  {:status  200
   :headers {"Content-type" "text/json"}
   :body    (let [uuid (:uuid (:route-params req))]
              (->> @client.model/client-collection
                   (filter #(= (:uuid %) uuid))
                   first
                   apply-response
                   cheshire/generate-string))})

(defn addclient-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (let [str-json (r.u.request/body-string req)
                  map-json (cheshire/parse-string str-json true)
                  uuid (:uuid map-json)
                  firstname (:firstname map-json)
                  surname (:surname map-json)
                  email (:email map-json)
                  birthday (:birthday map-json)
                  response (client.model/add-client!
                             uuid
                             firstname
                             surname
                             email
                             birthday)]
              (cheshire/generate-string response))})

(defn updateclient-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (let [str-json (r.u.request/body-string req)
                  map-json (cheshire/parse-string str-json true)
                  uuid (:uuid map-json)
                  firstname (:firstname map-json)
                  surname (:surname map-json)
                  email (:email map-json)
                  birthday (:birthday map-json)
                  response (client.model/update-client!
                             uuid
                             firstname
                             surname
                             email
                             birthday)]
              (cheshire/generate-string response))})

(defn remove-client-handler [req]
  {:status  200
   :headers {"Content-type" "text/json"}
   :body    (let [uuid (:uuid (:route-params req))]
              (client.model/remove-client! uuid)
              (-> {:message "Successfully removed"} cheshire/generate-string)
              )})

; testing
;(let [lista (client.model/add-client! "Jose" "Augusto" "jose@gmail.com" "1991-05-01")]
;  (println lista)
;  (println (cheshire/generate-string lista))
;  )
