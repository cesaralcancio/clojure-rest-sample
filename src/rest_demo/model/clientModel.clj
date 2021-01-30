(ns rest-demo.model.clientModel
  (:require [clojure.pprint :as pp]))

(def client-collection (atom []))

(defn add-client! [firstname surname email birthdate]
  (swap! client-collection conj {:uuid       (str (java.util.UUID/randomUUID))
                                 :firstname  firstname
                                 :surname    surname
                                 :email      email
                                 :birthdate  birthdate
                                 :created_at (new java.util.Date)}))
(add-client! "Cesar Augusto" "Alcancio de Souza" "cesar.alcancio@gmail.com" "1991-05-09")
(pp/pprint client-collection)