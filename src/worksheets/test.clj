;; gorilla-repl.fileformat = 1

;; **
;;; # Test Repl
;;; 
;;; Welcome to gorilla :-) Shift + enter evaluates code. Poke the question mark (top right) to learn more ...
;; **

;; @@
(use '[gorilla-repl table latex html])
(use 'gorilla-renderable.core)
(use 'gorilla-plot.vega :reload)
(use 'gorilla-plot.core :reload)
(use 'clojure.pprint)
(use 'hiccup.core)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(defrecord Clock [hours mins])
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>user.Clock</span>","value":"user.Clock"}
;; <=

;; @@
(defn clock-svg
  [hour minute]
  (let [hour-prime (- 6 hour)
        hourX (+ 50 (* 30 (Math/sin (* hour-prime 0.53))))
        hourY (+ 50 (* 30 (Math/cos (* hour-prime 0.53))))
        min-prime (- 30 minute)
        minX (+ 50 (* 35 (Math/sin (* hour-prime 0.10472))))
        minY (+ 50 (* 35 (Math/sin (* hour-prime 0.10472))))]
    (html [:svg {:height 100 :width 100}
           [:circle {:cx 50 :cy 50 :r 40 :stroke "black" :stroke-width 4 :fill "white"}]
           [:line {:x1 50 :y1 50 :x2 hourX :y2 hourY :stroke "red" :stroke-width 4}]
           [:line {:x1 50 :y1 50 :x2 minX :y2 minY :stroke "black" :stroke-width 4}]])))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;user/clock-svg</span>","value":"#'user/clock-svg"}
;; <=

;; @@
(html-view (clock-svg 12 45))
;; @@
;; =>
;;; {"type":"html","content":"<svg height=\"100\" width=\"100\"><circle cx=\"50\" cy=\"50\" fill=\"white\" r=\"40\" stroke-width=\"4\" stroke=\"black\"></circle><line stroke-width=\"4\" stroke=\"red\" x1=\"50\" x2=\"51.15193713515706\" y1=\"50\" y2=\"20.022124144018374\"></line><line stroke-width=\"4\" stroke=\"black\" x1=\"50\" x2=\"29.42747456619071\" y1=\"50\" y2=\"29.42747456619071\"></line></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<svg height=\\\"100\\\" width=\\\"100\\\"><circle cx=\\\"50\\\" cy=\\\"50\\\" fill=\\\"white\\\" r=\\\"40\\\" stroke-width=\\\"4\\\" stroke=\\\"black\\\"></circle><line stroke-width=\\\"4\\\" stroke=\\\"red\\\" x1=\\\"50\\\" x2=\\\"51.15193713515706\\\" y1=\\\"50\\\" y2=\\\"20.022124144018374\\\"></line><line stroke-width=\\\"4\\\" stroke=\\\"black\\\" x1=\\\"50\\\" x2=\\\"29.42747456619071\\\" y1=\\\"50\\\" y2=\\\"29.42747456619071\\\"></line></svg>\"}"}
;; <=

;; @@
(extend-type Clock
  Renderable
  (render [self]
          (:type :html
           :content (clock-svg (:hours self) (:mins self))
           :value (pr-str self))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(Clock. 12 45)
;; @@

;; @@

;; @@

;; @@

;; @@
