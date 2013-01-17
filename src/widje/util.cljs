(ns widje.util
  (:require [jayq.core :as jq])
  (:use-macros [widje.macros :only [defwidget]]))

;; Events

(defn evt->key
  "Maps event to readable key names"
  [e]
  (get {
     8 :backspace
     9 :tab
    13 :enter
    16 :shift
    17 :ctrl
    18 :alt
    19 :pause
    20 :capslock
    27 :esc
    32 :space
    33 :page-up
    34 :page-down
    35 :end
    36 :home
    37 :left-arrow
    38 :up-arrow
    39 :right-arrow
    40 :down-arrow
    45 :insert
    46 :delete
  } (.-keyCode e)))

;; Checkbox

;; sidenote: checkbox state querying and manipulation is
;; definitely not well-designed in html/dom

(defn checkbox-checked?
  "Returns boolean state of checkbox"
  [checkbox]
  (jq/is ($ checkbox) ":checked"))

(defn check! [checkbox value]
  "Sets boolean state of checkbox"
  (if value
    (jq/attr ($ checkbox) "checked" true)
    (jq/remove-attr ($ checkbox) "checked")))

; Widget: checkbox bound to atom via val-fn
(defwidget bound-checkbox [id classes atm val-fn]
  [:input.-checkbox {:id id
                     :class (str classes " -checkbox")
                     :type "checkbox"}]
  [checkbox]
  (check! checkbox (val-fn @atm))
  (add-watch atm (gensym "bound-checkbox")
    #(check! checkbox (val-fn %4))))
