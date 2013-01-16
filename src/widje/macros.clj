(ns widje.macros
  "Widget is a function that return DOM node.
  It combines crate (hiccup) template with supporting logic (event handling etc).
  Also widget keep track of each instance it creates and provide easy way
  to search them.
  
  Example:
  (defwidget todo [item]      ; widget function name and arguments
    [:div.todo                ; crate template
      :p.-text (:name item)]  ;
    [text]                    ; bind elements to vars by role (see widje.role)
    (listen text :click       ; handle events
      (mark-done item))       ;
  
  (def mytodo {:name \"Do stuff\"})  ; model
  (todo {:name \"Do stuff\"})        ; render
  (find-instance mytodo)             ; find todo DOM node by first widget param
  ")

;; Widget

(defmacro widget
  "Basic form, returns anonymous widget function. Similar to 'fn'"
  ([params tags]
    `(widje.macros/widget ~params ~tags []))
  ([params tags bindings & body]
    `(let [keyed?# (seq '~params)
           instances# (atom (if keyed?# {} []))
           widget-fn# (fn ~params
        (let [node# (crate.core/html ~tags)
              {:keys ~bindings} node#]
          (if keyed?#
            (swap! instances# assoc (first ~params) node#)
            (swap! instances# conj node#))
          ~@body
          node#))]
       (set! (.-_instances widget-fn#) instances#)
       widget-fn#)))

(defmacro defwidget
  "Defines named widget function. Similar to 'defn'"
  ([name params tags]
    `(widje.macros/defwidget ~name ~params ~tags []))
  ([name params tags bindings & body]
    `(def ~name (widje.macros/widget ~params ~tags ~bindings ~@body))))

(defmacro letwidget [widgetspecs & body]
  "Similar to 'letfn'"
  `(let ~(vec (interleave
                (map first widgetspecs)
                (map #(cons 'widje.macros/widget (rest %)) widgetspecs)))
     ~@body))

;; Util

(defmacro listen
  "(listen a :click
     (js/alert \"Hey!\"))"
  [elem evt & body]
  `(jayq.core/bind ~elem ~evt
     (fn [~'event]
       (jq/prevent ~'event)
       (cljs.core/this-as ~'this
         ~@body))))
