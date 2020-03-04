(ns avisi-apps.fmt.example)

(when something body)

(defn f [x] body)

(defn f [x] body)

(defn many-args [a b c d e f] body)

; We would like to see this differently but it is not a big enough problem for now.
; We want it like this:
;(defn multi-arity
;  ([x]
;   body)
;  ([x y]
;   body))

(defn multi-arity ([x] body) ([x y] body))

(let [x 1 y 2] body)

[1 2 3 4 5 6]

{:key-1 v1 :key-2 v2}

#{a b c d e f}

(or (condition-a) (condition-b))

(filter even? (range 1 10))

(clojure.core/filter even? (range 1 10))

;; Wow this a very long comment with a story that is way to long to fit on one line in the editor. I sure hope the
;; formatter wraps this.

(filter even? (range 1 10))

{:short-key "value" :very-long-key "value"}

(dom/div {:foo "Bar"} (dom/span "hoi"))

(defsc name []
  {:query [:foo "bar"]}
  (dom/div
    {:foo "Bar"}
    (dom/span "hoi" (dom/span "hoi" (dom/span "hoi")))
    (dom/span "hoi")
    (dom/span "hoi")
    (dom/span "hoi")))
