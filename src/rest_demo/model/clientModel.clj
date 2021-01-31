(ns rest-demo.model.clientModel
  (:require [clojure.pprint :as pp]
            [clojure.string :as s]))

(def client-collection (atom []))

(defn uuid-gen [] (java.util.UUID/randomUUID))

(defn date-gen [] (new java.util.Date))

(defn generate-uuid-if-blank [uuid]
  (if-let [validated-uuid (or (nil? uuid) (s/blank? uuid))]
    (uuid-gen) uuid))
(generate-uuid-if-blank nil)
(generate-uuid-if-blank "")
(generate-uuid-if-blank "123")

(defn add-client!
  ([firstname surname email birthday]
   (add-client! (str (uuid-gen))
                firstname
                surname
                email
                birthday))
  ([uuid firstname surname email birthday]
   (let [new-client {:uuid       (generate-uuid-if-blank uuid)
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

(defn update-client!
  [uuid firstname surname email birthday]
  (remove-client! uuid)
  (add-client! uuid firstname surname email birthday))

(add-client! "123" "Jose" "Antonio" "jose@gmail.com" "1991-01-01")
(pp/pprint client-collection)
(remove-client! "123")
(pp/pprint client-collection)