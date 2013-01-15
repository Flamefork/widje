# Widje

Widje is html templating library for ClojureScript based on the awesome [crate](https://github.com/ibdknox/crate/) and [jayq](https://github.com/ibdknox/jayq/).

## Basics

Widget is a function that return DOM node. It combines crate template with supporting logic (event handling etc) into single logical unit. Also widget keep track of each instance it creates and provide easy way to search them by first argument.

## Usage

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
=> <div class="todo"><p class="-text">Do stuff</p></div>

(widje/find-instance mytodo)
```

## License

Copyright (C) 2013 Ilia Ablamonov

Distributed under the Eclipse Public License, the same as Clojure.
