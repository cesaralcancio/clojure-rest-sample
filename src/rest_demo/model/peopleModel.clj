(ns rest-demo.model.peopleModel
  (:require [clojure.pprint :as pp]
            [clojure.string :as str]))

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
