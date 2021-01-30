(ns rest-demo.core
  (:require [org.httpkit.server :as server]
            [compojure.core :as compojure]
            [ring.middleware.defaults :as ring.middleware]
            [compojure.route :as route]
            [rest-demo.handler.peopleHandler :as people.handler]
            [rest-demo.handler.exampleHandler :as example.handler]
            [rest-demo.handler.clientHandler :as client.handler])
  (:gen-class))

(compojure/defroutes
  app-routes
  ; Examples
  (compojure/GET "/" [] example.handler/simple-body-page)
  (compojure/GET "/request" [] example.handler/request-example)
  (compojure/POST "/json" [] example.handler/json-example)
  (compojure/GET "/hello" [] example.handler/hello-name)
  ; People
  (compojure/GET "/people" [] people.handler/people-handler)
  (compojure/GET "/people/add" [] people.handler/addperson-handler)
  ; Client
  (compojure/POST "/client" [] client.handler/addclient-handler)
  ; Not found services
  (route/not-found "Error, page not found!"))

(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware
    ; Obs: Changed site-defaults to api-defaults because of the CSRF restriction
    ; https://stackoverflow.com/questions/33132131/unable-to-complete-post-request-in-clojure
    ; according to "Invalid anti-forgery token" error
    (server/run-server (ring.middleware/wrap-defaults #'app-routes ring.middleware/api-defaults) {:port port})

    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})

    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))