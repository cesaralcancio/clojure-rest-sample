(ns rest-demo.model.clientModel
  (:require [clojure.pprint :as pp]))

(def client-collection (atom []))

(defn add-client! [firstname surname email birthdate]
  (let [new-client {:uuid       (str (java.util.UUID/randomUUID))
                    :firstname  firstname
                    :surname    surname
                    :email      email
                    :birthday   birthdate
                    :created_at (new java.util.Date)}]
    (swap! client-collection conj new-client)
    new-client))

(defn remove-client [collection uuid]
  (remove #(= (:uuid %) uuid) collection))

(defn remove-client! [uuid]
  (swap! client-collection remove-client uuid)
  )
(remove-client! "f027ea34-f061-44a9-bd5e-c9599323051e")

(add-client! "Jose" "Antonio" "jose@gmail.com" "1991-01-01")
(pp/pprint client-collection)