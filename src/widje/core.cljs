(ns widje.core
  "See widje.macros for documenation."
  (:require [jayq.core :as jq]))

(defn instances [widget]
  "Returns all instances (root DOM elements) created by widget.
  Result may be map (for parametrized widgets) or vector."
  @(.-_instances widget))

(defn find-instance [widget param]
  "Returns instance (root DOM element) created by widget with
  param passed as first argument. Works only for widgets with
  at least one argument."
  ((instances widget) param))

(defn render [container widget & params]
  "Renders widget, inserts it into container and notifies listeners."
  (let [node (apply widget params)]
    (jq/inner (jq/$ container) node)
    (jq/trigger (:notify node) :render)))
