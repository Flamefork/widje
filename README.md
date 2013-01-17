# Widje

Widje is html templating library for ClojureScript based on the awesome [crate](https://github.com/ibdknox/crate/) and [jayq](https://github.com/ibdknox/jayq/).

## Basics

Widget is a function that return DOM node. It combines crate template with supporting logic (event handling etc) into single logical unit. Also widget keep track of each instance it creates and provide easy way to search them by first argument.

## Widget usage

```clojure
(ns myapp
  (:use-macros [widje.macros :only [defwidget]])
  (:require [widje.core :as widje]))

(defwidget todo [item]      ; widget function name and arguments
  [:div.todo                ; crate template
    :p.-text (:name item)]  ;
  [text]                    ; bind elements to vars by role (see widje.role)
  (listen text :click       ; handle events
    (mark-done item))       ;
  
(def mytodo {:name "Do stuff"})
=> {:name "Do stuff"}

(todo {:name "Do stuff"})
=> HTMLElement <div class="todo"><p class="-text">Do stuff</p></div>

(widje/find-instance mytodo)
=> HTMLElement <div class="todo"><p class="-text">Do stuff</p></div>
```

There are also `widget` (= `fn`) and `letwidget` (= `letfn`) macros.

## Binding usage

This is actually a documentation for the awesome `crate` binding feature.

```clojure
(defwidget item [item-atm]
  [:li
    (bound item-atm :name)])

(defwidget items [items-coll-atm]
  [:ul
    (bound-coll items-coll-atm {:as item})])
```

Widje adds `(bound* [atoms*] func?)` to crate features for binding to multiple atoms.

## Utils

See `widje.util` for documentation for:

- `evt->key`
- `bound-checkbox`
- `checkbox-checked?`
- `check!`

`macros/listen` macro is an easy way to bind to events (see widget usage example).

`core/render` is an easy way to render widget to some container.

## License

Copyright (C) 2013 Ilia Ablamonov

Distributed under the Eclipse Public License, the same as Clojure.
