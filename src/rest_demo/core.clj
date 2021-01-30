(ns rest-demo.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [cheshire.core :refer :all]
            [ring.util.request :as ru])
  (:gen-class))

; my people-collection mutable collection vector
(def people-collection (atom []))

;Collection Helper functions to add a new person
(defn addperson [firstname surname]
  (swap! people-collection conj {:firstname (str/capitalize firstname)
                                 :surname   (str/capitalize surname)}))

; Example JSON objects
(addperson "Functional" "Human")
(addperson "Micky" "Mouse")
(addperson "Cesar" "Alcancio")
(addperson "Paula" "Honda")
(println @people-collection)

; Return List of People
(defn people-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @people-collection))})

; Get the parameter specified by pname from :params object in req
(defn getparameter [req pname]
  (let [something (get (:params req) pname)]
    (println "something: " something)
    something
    ))

(defn addperson-handler [req]
  {:status  200
   :headers {"Content-type" "text/json"}
   :body    (-> (let [p (partial getparameter req)]
                  ; to understand partial function https://clojuredocs.org/clojure.core/partial
                  (println "partial: " p)
                  (str (json/write-str (addperson (p :firstname) (p :surname))))))})

; Simple Body Page
(defn simple-body-page [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})

; request-example
(defn request-example [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->>
              (pp/pprint req)
              (str "Request Object: " req))})

; json-example
(defn json-example [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (let [tipo (type req)
                  corpo (:body req)
                  corpo-str1 (str corpo)
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
           (GET "/people" [] people-handler)
           (GET "/people/add" [] addperson-handler)
           ; (POST "/people/add" [] addperson-handler)
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