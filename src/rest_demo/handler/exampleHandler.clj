(ns rest-demo.handler.exampleHandler
  (:require [clojure.pprint :as pp]
            [ring.util.request :as r.u.request]
            [cheshire.core :as cheshire]
            ))

; Simple Body Page
(defn simple-body-page [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})

; Request-example
(defn request-example [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->>
              (pp/pprint req)
              (str "Request Object: " req))})

; Json-example
(defn json-example [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (let [; tipo (type req)
                  ; corpo (:body req)
                  ; corpo-str1 (str corpo)
                  corpo-str (r.u.request/body-string req)
                  mapa (cheshire/parse-string corpo-str true)
                  nome (:firstname mapa)]
              ;(println tipo)
              ;(println corpo)
              ;(println corpo-str1)
              ;(println corpo-str)
              ;(println mapa)
              ;(println (:firstname mapa))
              nome
              )})

; Hello Example
(defn hello-name [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->
              (pp/pprint req)
              (str "Hello " (:name (:params req))))})