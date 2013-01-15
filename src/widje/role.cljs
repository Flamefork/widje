(ns widje.role
  "Provides an ability to query DOM nodes by 'role'.
  Role in this case is just a CSS class prefixed by '-'.
  (:myrole ($ \"<div><p class=\"-myrole\">test</p></div>\")) => HTMLElement<p \"test\">"
  (:require [jayq.core :as jq]))

(defn $zero? [$obj]
  "Returns true if zero elements matched by $obj, else false."
  (zero? (.-length $obj)))

(defn $-with-context [selector context]
  "Same as $, but matches also against context root."
  (let [found-nodes (jq/$ selector context)]
    (if (.is (jq/$ context) selector)
      (.add found-nodes context)
      found-nodes)))

(defn role-selector [role]
  "Returns .-role for :role"
  (str ".-" (name role)))

(extend-type js/Node
  ILookup
  (-lookup
    ([this k]
      ($-with-context (role-selector k) this))
    ([this k _]
      (-lookup this k))))
