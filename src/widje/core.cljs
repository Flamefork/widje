(ns widje.core
  "See widje.macros for widget-related stuff."
  (:require [jayq.core :as jq]
            [crate.binding :as binding]
            [widje.role :as role]))

;; General

(defn render
  "Renders widget, inserts it into container and notifies listeners."
  [container widget & params]
  (let [node (apply widget params)]
    (jq/inner (jq/$ container) node)
    (jq/trigger (:notify node) :render)))

;; Instances

(defn instances
  "Returns all instances (root DOM elements) created by widget.
  Result may be map (for parametrized widgets) or vector."
  [widget]
  @(.-_instances widget))

(defn find-instance
  "Returns instance (root DOM element) created by widget with
  param passed as first argument. Works only for widgets with
  at least one argument."
  [widget param]
  ((instances widget) param))

;; Binding

(declare atoms-binding)

(def bound
  ^{:doc "Returns value of func (or just value) bound to atm.
     func is a function of one argument: current value of atm."
    :arglists '([atm func?])}
  binding/bound)

(defn bound*
  ^{:doc "Returns value of func (or just value) bound to all provided atoms.
     func is a function of (count atm*) arguments: current values of atm(s)."
    :arglists '([[atm*] func?])}
  [atoms & [func]]
  (let [func (or func identity)]
    (atoms-binding. atoms func)))

(def bound-coll
  ^{:doc "Returns value of func (:as opts) bound to each element of collection atom."
    :arglists '(atm path? opts?)}
  binding/bound-coll)

(def map-bound
  ^{:doc "Returns value of as func bound to each element of map atom."
    :arglists '(as atm opts?)}
  binding/map-bound)

;; Internals

(deftype atoms-binding [atoms value-func]
  crate.binding/bindable
  (-value [this] (apply value-func (map deref atoms)))
  (-on-change [this func]
    (doseq [atm atoms]
      (add-watch atm (gensym "atom-binding") #(func (crate.binding/-value this))))))

