(ns rest-demo.core
  (:require [org.httpkit.server :as server]
            [compojure.core :as compojure]
            [ring.middleware.defaults :as ring.middleware]
            [compojure.route :as route]
            [rest-demo.handler.peopleHandler :as people.handler]
            [rest-demo.handler.exampleHandler :as example.handler]
            [rest-demo.handler.clientHandler :as client.handler]
            [ring.swagger.swagger2 :as rs]
            [schema.core :as s])
  (:gen-class))

(s/defschema User {:id      s/Str,
                   :name    s/Str
                   :address {:street s/Str
                             :city   (s/enum :tre :hki)}})

(defn swagger [req]
  (let [alguma
        (rs/swagger-json
          {:info  {:version        "1.0.0"
                   :title          "Sausages"
                   :description    "Sausage description"
                   :termsOfService "http://helloreverb.com/terms/"
                   :contact        {:name  "My API Team"
                                    :email "foo@example.com"
                                    :url   "http://www.metosin.fi"}
                   :license        {:name "Eclipse Public License"
                                    :url  "http://www.eclipse.org/legal/epl-v10.html"}}
           :tags  [{:name        "user"
                    :description "User stuff"}]
           :paths {"/api/ping" {:get {}}
                   "/user/:id" {:post {:summary     "User Api"
                                       :description "User Api description"
                                       :tags        ["user"]
                                       :parameters  {:path {:id s/Str}
                                                     :body User}
                                       :responses   {200 {:schema      User
                                                          :description "Found it!"}
                                                     404 {:description "Ohnoes."}}}}}})
        response {:status  200
                  :headers {"Content-type" "text/json"}
                  :body    alguma}]
    (println alguma)
    (println response)
    response
    ))

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
  (compojure/GET "/clients" [] client.handler/clients-handler)
  (compojure/GET "/client/:uuid" [] client.handler/client-handler)
  (compojure/POST "/client" [] client.handler/addclient-handler)
  (compojure/PATCH "/client" [] client.handler/updateclient-handler)
  (compojure/DELETE "/client/:uuid" [] client.handler/remove-client-handler)
  (compojure/GET "/swagger.json" [] swagger)
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