(ns rest-demo.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [cheshire.core :refer :all]
            [ring.util.request :as ru]
            [rest-demo.handler.peopleHandler :as people.handler])
  (:gen-class))

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
                  corpo-str (ru/body-string req)
                  mapa (parse-string corpo-str true)
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

(defroutes app-routes
           (GET "/" [] simple-body-page)
           (GET "/request" [] request-example)
           (POST "/json" [] json-example)
           (GET "/hello" [] hello-name)
           (GET "/people" [] people.handler/people-handler)
           (GET "/people/add" [] people.handler/addperson-handler)
           (route/not-found "Error, page not found!"))

(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware
    ; Obs: Changed site-defaults to api-defaults because of the CSRF restriction
    ; https://stackoverflow.com/questions/33132131/unable-to-complete-post-request-in-clojure
    ; according to "Invalid anti-forgery token" error
    (server/run-server (wrap-defaults #'app-routes api-defaults) {:port port})

    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})

    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))