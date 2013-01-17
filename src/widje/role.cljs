(ns widje.role
  "Provides an ability to query DOM nodes by 'role'.
  Role in this case is just a CSS class prefixed by '-'.
  (:myrole ($ \"<div><p class=\"-myrole\">test</p></div>\")) => HTMLElement<p \"test\">"
  (:require [jayq.core :as jq]))

(defn $-with-context
  "Same as $, but matches against context root node too."
  [selector context]
  (let [found-nodes (jq/$ selector context)]
    (if (.is (jq/$ context) selector)
      (.add found-nodes context)
      found-nodes)))

(extend-type js/Node
  ILookup
  (-lookup
    ([this k]
      ($-with-context (str ".-" (name k)) this))
    ([this k _]
      (-lookup this k))))
