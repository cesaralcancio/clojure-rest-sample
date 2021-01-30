(ns rest-demo.handler.peopleHandler
  (:require [clojure.data.json :as json]
            [rest-demo.model.peopleModel :as people.model]))

; Return List of People
(defn people-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @people.model/people-collection))})

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
                  (str (json/write-str (people.model/addperson (p :firstname) (p :surname))))))})