(ns rest-demo.model.clientModel
  (:require [clojure.pprint :as pp]
            [clojure.string :as s]))

(def client-collection (atom []))

(defn uuid-gen [] (java.util.UUID/randomUUID))

(defn date-gen [] (new java.util.Date))

(defn valida-uuid [uuid]
  (if-let [validated-uuid (or (nil? uuid) (s/blank? uuid))]
    (uuid-gen) uuid))
(valida-uuid nil)
(valida-uuid "")
(valida-uuid "123")

(defn add-client!
  ([firstname surname email birthday]
   (add-client! (str (uuid-gen))
                firstname
                surname
                email
                birthday))
  ([uuid firstname surname email birthday]
   (let [new-client {:uuid       (valida-uuid uuid)
                     :firstname  firstname
                     :surname    surname
                     :email      email
                     :birthday   birthday
                     :created_at (date-gen)}]
     (swap! client-collection conj new-client)
     new-client)))

(defn remove-client [collection uuid]
  (remove #(= (:uuid %) uuid) collection))

(defn remove-client! [uuid]
  (swap! client-collection remove-client uuid))
(remove-client! "123")

(add-client! "123" "Jose" "Antonio" "jose@gmail.com" "1991-01-01")
(pp/pprint client-collection)