;; gorilla-repl.fileformat = 1

;; **
;;; # Use Analemma to Crate SVG Shapes and Emit to SVG within Gorilla-Repl
;;; 
;;; Welcome to gorilla :-) Shift + enter evaluates code. Poke the question mark (top right) to learn more ...
;; **

;; @@
;(use 'hiccup.core)
(use '[gorilla-repl table latex html])
(use 'gorilla-renderable.core)
(use 'gorilla-plot.core)
(use 'analemma.svg)
(use 'analemma.xml)
(use 'analemma.charts)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Lets Try a simple water-like Molecule
;; **

;; @@
(def mol {:bonds ({:order :single
                   :x1 50
                   :y1 50
                   :x2 60
                   :y2 70 }
                  {:order :single
                   :x1 60
                   :y1 70
                   :x2 70
                   :y2 50})
          :atoms ({:element "O"
                   :x1 50
                   :y1 50}
                  {:element "C"
                   :x1 60
                   :y1 70}
                  {:element "0"
                   :x1 70
                   :y1 50})})


(defn makepolygon [x1 y1 fs]
  ;take and x and y value as well as the fontsize and return a polygon to 
  ; be used as a backstop behing the letters
  (let [ dlarge (/ fs 2)
         dsmall (/ (/ fs 2) 1.5)
         ;topleft, topright, left top......
         tlx (- x1 dsmall)  tly (- y1 dlarge)
         trx (+ x1 dsmall)  try (- y1 dlarge) 
         rtx (+ x1 dlarge)  rty (- y1 dsmall) 
         rbx (+ x1 dlarge)  rby (+ y1 dsmall) 
         brx (+ x1 dsmall)  bry (+ y1 dlarge) 
         blx (- x1 dsmall)  bly (+ y1 dlarge) 
         lbx (- x1 dlarge)  lby (+ y1 dsmall) 
         ltx (- x1 dlarge)  lty (- y1 dsmall) ]
    (polygon [tlx,tly trx,try rtx,rty rbx,rby brx,bry blx,bly lbx,lby ltx,lty])))



(def mol-svg (svg {:x 100 :y 100}
                  (-> (group
                       ;add lines
                       (-> (line 50 50 70 70)
                           (style :stroke "#006600" :stroke-width 3))
                       (-> (line 70 70 90 50)
                           (style :stroke "#006600" :stroke-width 3))
                       
                       ;add text and polygons
                       (-> (makepolygon 50 50 20)
                           (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                       (-> (text {:x 45 :y 55} "C")
                           (style :font-size 20))
                       
                       (-> (makepolygon 70 70 20)
                           (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                       (-> (text {:x 65 :y 75} "C")
                           (style :font-size 20))
                       
                       (-> (makepolygon 90 50 20)
                           (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                       (-> (text {:x 85 :y 55} "N")
                           (style :font-size 20)) 
                       
                      
))))

(html-view (emit mol-svg))
;(emit mol-svg)
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg x=\"100\" y=\"100\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><g ><line style=\" stroke: #006600;  stroke-width: 3; \" x1=\"50\" y1=\"50\" x2=\"70\" y2=\"70\" /><line style=\" stroke: #006600;  stroke-width: 3; \" x1=\"70\" y1=\"70\" x2=\"90\" y2=\"50\" /><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 43.333333333333336,40 56.666666666666664,40 60,43.333333333333336 60,56.666666666666664 56.666666666666664,60 43.333333333333336,60 40,56.666666666666664 40,43.333333333333336\" /><text y=\"55\" x=\"45\" style=\" font-size: 20; \" >C</text><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 63.333333333333336,60 76.66666666666667,60 80,63.333333333333336 80,76.66666666666667 76.66666666666667,80 63.333333333333336,80 60,76.66666666666667 60,63.333333333333336\" /><text y=\"75\" x=\"65\" style=\" font-size: 20; \" >C</text><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 83.33333333333333,40 96.66666666666667,40 100,43.333333333333336 100,56.666666666666664 96.66666666666667,60 83.33333333333333,60 80,56.666666666666664 80,43.333333333333336\" /><text y=\"55\" x=\"85\" style=\" font-size: 20; \" >N</text></g></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg x=\\\"100\\\" y=\\\"100\\\" xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><g ><line style=\\\" stroke: #006600;  stroke-width: 3; \\\" x1=\\\"50\\\" y1=\\\"50\\\" x2=\\\"70\\\" y2=\\\"70\\\" /><line style=\\\" stroke: #006600;  stroke-width: 3; \\\" x1=\\\"70\\\" y1=\\\"70\\\" x2=\\\"90\\\" y2=\\\"50\\\" /><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 43.333333333333336,40 56.666666666666664,40 60,43.333333333333336 60,56.666666666666664 56.666666666666664,60 43.333333333333336,60 40,56.666666666666664 40,43.333333333333336\\\" /><text y=\\\"55\\\" x=\\\"45\\\" style=\\\" font-size: 20; \\\" >C</text><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 63.333333333333336,60 76.66666666666667,60 80,63.333333333333336 80,76.66666666666667 76.66666666666667,80 63.333333333333336,80 60,76.66666666666667 60,63.333333333333336\\\" /><text y=\\\"75\\\" x=\\\"65\\\" style=\\\" font-size: 20; \\\" >C</text><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 83.33333333333333,40 96.66666666666667,40 100,43.333333333333336 100,56.666666666666664 96.66666666666667,60 83.33333333333333,60 80,56.666666666666664 80,43.333333333333336\\\" /><text y=\\\"55\\\" x=\\\"85\\\" style=\\\" font-size: 20; \\\" >N</text></g></svg>\"}"}
;; <=

;; @@
(def mol-svg-anim1 (svg {:x 100 :y 100}
                  (-> (group
                       ;add lines
                       (-> (line 50 50 70 70)
                           (style :stroke "#006600" :stroke-width 3))
                       (-> (line 70 70 90 50)
                           (style :stroke "#006600" :stroke-width 3))
                       
                       ;add text and polygons
                       (-> (makepolygon 50 50 20)
                           (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                       (-> (text {:x 45 :y 55} "C")
                           (style :font-size 20))
                       
                       (-> (makepolygon 70 70 20)
                           (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                       (-> (text {:x 65 :y 75} "C")
                           (style :font-size 20))
                       
                       (-> (makepolygon 90 50 20)
                           (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                       (-> (text {:x 85 :y 55} "N")
                           (style :font-size 20)))
                       (animate-transform :begin 0
							:dur 20
							:type :rotate
							:from "0 70 70"
							:to   "360 60 60"
							:repeatCount :indefinite))))

(html-view (emit mol-svg-anim1))
;(emit mol-svg)
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg x=\"100\" y=\"100\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><g ><line style=\" stroke: #006600;  stroke-width: 3; \" x1=\"50\" y1=\"50\" x2=\"70\" y2=\"70\" /><line style=\" stroke: #006600;  stroke-width: 3; \" x1=\"70\" y1=\"70\" x2=\"90\" y2=\"50\" /><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 43.333333333333336,40 56.666666666666664,40 60,43.333333333333336 60,56.666666666666664 56.666666666666664,60 43.333333333333336,60 40,56.666666666666664 40,43.333333333333336\" /><text y=\"55\" x=\"45\" style=\" font-size: 20; \" >C</text><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 63.333333333333336,60 76.66666666666667,60 80,63.333333333333336 80,76.66666666666667 76.66666666666667,80 63.333333333333336,80 60,76.66666666666667 60,63.333333333333336\" /><text y=\"75\" x=\"65\" style=\" font-size: 20; \" >C</text><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 83.33333333333333,40 96.66666666666667,40 100,43.333333333333336 100,56.666666666666664 96.66666666666667,60 83.33333333333333,60 80,56.666666666666664 80,43.333333333333336\" /><text y=\"55\" x=\"85\" style=\" font-size: 20; \" >N</text><animateTransform type=\"rotate\" dur=\"20\" repeatCount=\"indefinite\" to=\"360 60 60\" from=\"0 70 70\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></g></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg x=\\\"100\\\" y=\\\"100\\\" xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><g ><line style=\\\" stroke: #006600;  stroke-width: 3; \\\" x1=\\\"50\\\" y1=\\\"50\\\" x2=\\\"70\\\" y2=\\\"70\\\" /><line style=\\\" stroke: #006600;  stroke-width: 3; \\\" x1=\\\"70\\\" y1=\\\"70\\\" x2=\\\"90\\\" y2=\\\"50\\\" /><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 43.333333333333336,40 56.666666666666664,40 60,43.333333333333336 60,56.666666666666664 56.666666666666664,60 43.333333333333336,60 40,56.666666666666664 40,43.333333333333336\\\" /><text y=\\\"55\\\" x=\\\"45\\\" style=\\\" font-size: 20; \\\" >C</text><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 63.333333333333336,60 76.66666666666667,60 80,63.333333333333336 80,76.66666666666667 76.66666666666667,80 63.333333333333336,80 60,76.66666666666667 60,63.333333333333336\\\" /><text y=\\\"75\\\" x=\\\"65\\\" style=\\\" font-size: 20; \\\" >C</text><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 83.33333333333333,40 96.66666666666667,40 100,43.333333333333336 100,56.666666666666664 96.66666666666667,60 83.33333333333333,60 80,56.666666666666664 80,43.333333333333336\\\" /><text y=\\\"55\\\" x=\\\"85\\\" style=\\\" font-size: 20; \\\" >N</text><animateTransform type=\\\"rotate\\\" dur=\\\"20\\\" repeatCount=\\\"indefinite\\\" to=\\\"360 60 60\\\" from=\\\"0 70 70\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></g></svg>\"}"}
;; <=

;; @@
(def mol-svg-anim2 (svg {:x 100 :y 100}
                        ; this animation will kee the middle C Constant
                  (-> (group
                       ;add lines
                       (-> (line 50 50 70 70)
                           (style :stroke "#006600" :stroke-width 3))
                       (-> (line 70 70 90 50)
                           (style :stroke "#006600" :stroke-width 3))
                       
                       ;add text and polygons
                       (-> (makepolygon 50 50 20)
                           (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                       (-> (text {:x 45 :y 55} "C")
                           (style :font-size 20))
                       
                       ;group and add text and polygons
                       (-> (group
                             (-> (makepolygon 70 70 20)
                                 (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                             (-> (text {:x 65 :y 75} "C")
                                 (style :font-size 20)))
                         	 (animate-transform :begin 0 :dur 20 :type :rotate
								:from "0 70 70"
								:to   "-360 70 70"
								:repeatCount :indefinite))
                       
                       (-> (makepolygon 90 50 20)
                           (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                       (-> (text {:x 85 :y 55} "N")
                           (style :font-size 20)))
                      
                      ;animation settings
                      (animate-transform :begin 0
							:dur 20
							:type :rotate
							:from "0 70 70"
							:to   "360 70 70"
							:repeatCount :indefinite))
                        ))

(html-view (emit mol-svg-anim2))
;(emit mol-svg)
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg x=\"100\" y=\"100\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><g ><line style=\" stroke: #006600;  stroke-width: 3; \" x1=\"50\" y1=\"50\" x2=\"70\" y2=\"70\" /><line style=\" stroke: #006600;  stroke-width: 3; \" x1=\"70\" y1=\"70\" x2=\"90\" y2=\"50\" /><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 43.333333333333336,40 56.666666666666664,40 60,43.333333333333336 60,56.666666666666664 56.666666666666664,60 43.333333333333336,60 40,56.666666666666664 40,43.333333333333336\" /><text y=\"55\" x=\"45\" style=\" font-size: 20; \" >C</text><g ><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 63.333333333333336,60 76.66666666666667,60 80,63.333333333333336 80,76.66666666666667 76.66666666666667,80 63.333333333333336,80 60,76.66666666666667 60,63.333333333333336\" /><text y=\"75\" x=\"65\" style=\" font-size: 20; \" >C</text><animateTransform type=\"rotate\" dur=\"20\" repeatCount=\"indefinite\" to=\"-360 70 70\" from=\"0 70 70\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></g><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 83.33333333333333,40 96.66666666666667,40 100,43.333333333333336 100,56.666666666666664 96.66666666666667,60 83.33333333333333,60 80,56.666666666666664 80,43.333333333333336\" /><text y=\"55\" x=\"85\" style=\" font-size: 20; \" >N</text><animateTransform type=\"rotate\" dur=\"20\" repeatCount=\"indefinite\" to=\"360 70 70\" from=\"0 70 70\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></g></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg x=\\\"100\\\" y=\\\"100\\\" xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><g ><line style=\\\" stroke: #006600;  stroke-width: 3; \\\" x1=\\\"50\\\" y1=\\\"50\\\" x2=\\\"70\\\" y2=\\\"70\\\" /><line style=\\\" stroke: #006600;  stroke-width: 3; \\\" x1=\\\"70\\\" y1=\\\"70\\\" x2=\\\"90\\\" y2=\\\"50\\\" /><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 43.333333333333336,40 56.666666666666664,40 60,43.333333333333336 60,56.666666666666664 56.666666666666664,60 43.333333333333336,60 40,56.666666666666664 40,43.333333333333336\\\" /><text y=\\\"55\\\" x=\\\"45\\\" style=\\\" font-size: 20; \\\" >C</text><g ><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 63.333333333333336,60 76.66666666666667,60 80,63.333333333333336 80,76.66666666666667 76.66666666666667,80 63.333333333333336,80 60,76.66666666666667 60,63.333333333333336\\\" /><text y=\\\"75\\\" x=\\\"65\\\" style=\\\" font-size: 20; \\\" >C</text><animateTransform type=\\\"rotate\\\" dur=\\\"20\\\" repeatCount=\\\"indefinite\\\" to=\\\"-360 70 70\\\" from=\\\"0 70 70\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></g><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 83.33333333333333,40 96.66666666666667,40 100,43.333333333333336 100,56.666666666666664 96.66666666666667,60 83.33333333333333,60 80,56.666666666666664 80,43.333333333333336\\\" /><text y=\\\"55\\\" x=\\\"85\\\" style=\\\" font-size: 20; \\\" >N</text><animateTransform type=\\\"rotate\\\" dur=\\\"20\\\" repeatCount=\\\"indefinite\\\" to=\\\"360 70 70\\\" from=\\\"0 70 70\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></g></svg>\"}"}
;; <=

;; @@
(def mol-svg-anim2 (svg {:x 100 :y 100}
                  ; this animation will keep  All of the Letters Correctly Rotating!
                  (-> (group
                       ;add lines
                       (-> (line 50 50 70 70)
                           (style :stroke "#006600" :stroke-width 3))
                       (-> (line 70 70 90 50)
                           (style :stroke "#006600" :stroke-width 3))
                       
                       ;group and add text and polygons
                       (-> (group
                             (-> (makepolygon 50 50 20)
                                 (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                             (-> (text {:x 45 :y 55} "C")
                                 (style :font-size 20)))
                         	 (animate-transform :begin 0 :dur 20 :type :rotate
								:from "0 50 50"
								:to   "-360 50 50"
								:repeatCount :indefinite))
                       
                       ;group and add text and polygons
                       (-> (group
                             (-> (makepolygon 70 70 20)
                                 (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                             (-> (text {:x 65 :y 75} "C")
                                 (style :font-size 20)))
                         	 (animate-transform :begin 0 :dur 20 :type :rotate
								:from "0 70 70"
								:to   "-360 70 70"
								:repeatCount :indefinite))
                       
                       ;group and add text and polygons
                       (-> (group
                             (-> (makepolygon 90 50 20)
                                 (style :fill "#FFFFFF" :stroke "#00000" :stroke-width 3))
                             (-> (text {:x 85 :y 55} "C")
                                 (style :font-size 20)))
                         	 (animate-transform :begin 0 :dur 20 :type :rotate
								:from "0 90 50"
								:to   "-360 90 50"
								:repeatCount :indefinite))); end major group
                      
                      ;animation settings
                      (animate-transform :begin 0
							:dur 20
							:type :rotate
							:from "0 70 70"
							:to   "360 70 70"
							:repeatCount :indefinite))
                        ))

(html-view (emit mol-svg-anim2))
;(emit mol-svg)
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg x=\"100\" y=\"100\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><g ><line style=\" stroke: #006600;  stroke-width: 3; \" x1=\"50\" y1=\"50\" x2=\"70\" y2=\"70\" /><line style=\" stroke: #006600;  stroke-width: 3; \" x1=\"70\" y1=\"70\" x2=\"90\" y2=\"50\" /><g ><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 43.333333333333336,40 56.666666666666664,40 60,43.333333333333336 60,56.666666666666664 56.666666666666664,60 43.333333333333336,60 40,56.666666666666664 40,43.333333333333336\" /><text y=\"55\" x=\"45\" style=\" font-size: 20; \" >C</text><animateTransform type=\"rotate\" dur=\"20\" repeatCount=\"indefinite\" to=\"-360 50 50\" from=\"0 50 50\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></g><g ><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 63.333333333333336,60 76.66666666666667,60 80,63.333333333333336 80,76.66666666666667 76.66666666666667,80 63.333333333333336,80 60,76.66666666666667 60,63.333333333333336\" /><text y=\"75\" x=\"65\" style=\" font-size: 20; \" >C</text><animateTransform type=\"rotate\" dur=\"20\" repeatCount=\"indefinite\" to=\"-360 70 70\" from=\"0 70 70\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></g><g ><polygon style=\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \" points=\" 83.33333333333333,40 96.66666666666667,40 100,43.333333333333336 100,56.666666666666664 96.66666666666667,60 83.33333333333333,60 80,56.666666666666664 80,43.333333333333336\" /><text y=\"55\" x=\"85\" style=\" font-size: 20; \" >C</text><animateTransform type=\"rotate\" dur=\"20\" repeatCount=\"indefinite\" to=\"-360 90 50\" from=\"0 90 50\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></g><animateTransform type=\"rotate\" dur=\"20\" repeatCount=\"indefinite\" to=\"360 70 70\" from=\"0 70 70\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></g></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg x=\\\"100\\\" y=\\\"100\\\" xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><g ><line style=\\\" stroke: #006600;  stroke-width: 3; \\\" x1=\\\"50\\\" y1=\\\"50\\\" x2=\\\"70\\\" y2=\\\"70\\\" /><line style=\\\" stroke: #006600;  stroke-width: 3; \\\" x1=\\\"70\\\" y1=\\\"70\\\" x2=\\\"90\\\" y2=\\\"50\\\" /><g ><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 43.333333333333336,40 56.666666666666664,40 60,43.333333333333336 60,56.666666666666664 56.666666666666664,60 43.333333333333336,60 40,56.666666666666664 40,43.333333333333336\\\" /><text y=\\\"55\\\" x=\\\"45\\\" style=\\\" font-size: 20; \\\" >C</text><animateTransform type=\\\"rotate\\\" dur=\\\"20\\\" repeatCount=\\\"indefinite\\\" to=\\\"-360 50 50\\\" from=\\\"0 50 50\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></g><g ><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 63.333333333333336,60 76.66666666666667,60 80,63.333333333333336 80,76.66666666666667 76.66666666666667,80 63.333333333333336,80 60,76.66666666666667 60,63.333333333333336\\\" /><text y=\\\"75\\\" x=\\\"65\\\" style=\\\" font-size: 20; \\\" >C</text><animateTransform type=\\\"rotate\\\" dur=\\\"20\\\" repeatCount=\\\"indefinite\\\" to=\\\"-360 70 70\\\" from=\\\"0 70 70\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></g><g ><polygon style=\\\" fill: #FFFFFF;  stroke: #00000;  stroke-width: 3; \\\" points=\\\" 83.33333333333333,40 96.66666666666667,40 100,43.333333333333336 100,56.666666666666664 96.66666666666667,60 83.33333333333333,60 80,56.666666666666664 80,43.333333333333336\\\" /><text y=\\\"55\\\" x=\\\"85\\\" style=\\\" font-size: 20; \\\" >C</text><animateTransform type=\\\"rotate\\\" dur=\\\"20\\\" repeatCount=\\\"indefinite\\\" to=\\\"-360 90 50\\\" from=\\\"0 90 50\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></g><animateTransform type=\\\"rotate\\\" dur=\\\"20\\\" repeatCount=\\\"indefinite\\\" to=\\\"360 70 70\\\" from=\\\"0 70 70\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></g></svg>\"}"}
;; <=

;; @@

;; @@

;; @@
;; http://tutorials.jenkov.com/svg/rect-element.html
(def ex-round-rects
     (svg
      (-> (rect 10 10 50 50 :rx 5 :ry 5)
	  (style :stroke "#006600" :fill "#00cc00"))
      (-> (rect 70 10 50 50 :rx 10 :ry 10)
	  (style :stroke "#006600" :fill "#00cc00"))
      (-> (rect 130 10 50 50 :rx 15 :ry 15)
	  (style :stroke "#006600" :fill "#00cc00"))))

(html-view (emit ex-round-rects))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><rect style=\" fill: #00cc00;  stroke: #006600; \" ry=\"5\" rx=\"5\" x=\"10\" y=\"10\" height=\"50\" width=\"50\" /><rect style=\" fill: #00cc00;  stroke: #006600; \" ry=\"10\" rx=\"10\" x=\"70\" y=\"10\" height=\"50\" width=\"50\" /><rect style=\" fill: #00cc00;  stroke: #006600; \" ry=\"15\" rx=\"15\" x=\"130\" y=\"10\" height=\"50\" width=\"50\" /></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><rect style=\\\" fill: #00cc00;  stroke: #006600; \\\" ry=\\\"5\\\" rx=\\\"5\\\" x=\\\"10\\\" y=\\\"10\\\" height=\\\"50\\\" width=\\\"50\\\" /><rect style=\\\" fill: #00cc00;  stroke: #006600; \\\" ry=\\\"10\\\" rx=\\\"10\\\" x=\\\"70\\\" y=\\\"10\\\" height=\\\"50\\\" width=\\\"50\\\" /><rect style=\\\" fill: #00cc00;  stroke: #006600; \\\" ry=\\\"15\\\" rx=\\\"15\\\" x=\\\"130\\\" y=\\\"10\\\" height=\\\"50\\\" width=\\\"50\\\" /></svg>\"}"}
;; <=

;; @@
(def ex-circle (svg
		(-> (circle 40 40 24)
		    (style :stroke "#006600" :fill "#00cc00"))))

(html-view (emit ex-circle))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><circle style=\" fill: #00cc00;  stroke: #006600; \" cx=\"40\" cy=\"40\" r=\"24\" /></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><circle style=\\\" fill: #00cc00;  stroke: #006600; \\\" cx=\\\"40\\\" cy=\\\"40\\\" r=\\\"24\\\" /></svg>\"}"}
;; <=

;; @@
(def ex-ellipse (svg
		 (-> (ellipse 40 40 30 15)
		     (style :stroke "#006600" :fill "#00cc00"))))

(html-view (emit ex-ellipse))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><ellipse style=\" fill: #00cc00;  stroke: #006600; \" cx=\"40\" cy=\"40\" rx=\"30\" ry=\"15\" /></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><ellipse style=\\\" fill: #00cc00;  stroke: #006600; \\\" cx=\\\"40\\\" cy=\\\"40\\\" rx=\\\"30\\\" ry=\\\"15\\\" /></svg>\"}"}
;; <=

;; @@
(def ex-tri (svg
	     (-> (polygon [0,0 30,0 15,30 0,0])
		 (style :stroke "#006600" :fill "#33cc33"))))
(html-view (emit ex-tri))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><polygon style=\" fill: #33cc33;  stroke: #006600; \" points=\" 0,0 30,0 15,30 0,0\" /></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><polygon style=\\\" fill: #33cc33;  stroke: #006600; \\\" points=\\\" 0,0 30,0 15,30 0,0\\\" /></svg>\"}"}
;; <=

;; @@
(def ex-oct (svg
	     (-> (polygon [50,05 100,5 125,30 125,80 100,105 50,105 25,80 25,30])
		 (style :stroke "#660000"
			:fill "#cc3333"
			:stroke-width 3))))

(html-view (emit ex-oct))



;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><polygon style=\" fill: #cc3333;  stroke: #660000;  stroke-width: 3; \" points=\" 50,5 100,5 125,30 125,80 100,105 50,105 25,80 25,30\" /></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><polygon style=\\\" fill: #cc3333;  stroke: #660000;  stroke-width: 3; \\\" points=\\\" 50,5 100,5 125,30 125,80 100,105 50,105 25,80 25,30\\\" /></svg>\"}"}
;; <=

;; @@
;; http://tutorials.jenkov.com/svg/path-element.html
(def ex-path (svg
	      (-> (path [:M [50,50]
			 :A [30,30 0 0,1 35,20]
			 :L [100,100]
			 :M [110,110]
			 :L [100,0]])
		  (style :stroke "#660000" :fill :none))))
(html-view (emit ex-path))

;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><path style=\" fill: none;  stroke: #660000; \" d=\" M50,50 A30,30,0,0,1,35,20 L100,100 M110,110 L100,0\" /></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><path style=\\\" fill: none;  stroke: #660000; \\\" d=\\\" M50,50 A30,30,0,0,1,35,20 L100,100 M110,110 L100,0\\\" /></svg>\"}"}
;; <=

;; @@
(def ex-text (svg
	      (text {:x 20 :y 40} "Example SVG text 1")
	      (-> (line 10 40 150 40)
		  (style :stroke "#000000"))))

(html-view (emit ex-text))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><text y=\"40\" x=\"20\" >Example SVG text 1</text><line style=\" stroke: #000000; \" x1=\"10\" y1=\"40\" x2=\"150\" y2=\"40\" /></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><text y=\\\"40\\\" x=\\\"20\\\" >Example SVG text 1</text><line style=\\\" stroke: #000000; \\\" x1=\\\"10\\\" y1=\\\"40\\\" x2=\\\"150\\\" y2=\\\"40\\\" /></svg>\"}"}
;; <=

;; @@

(def ex-text2 (svg
	       (-> (text {:x 20 :y 40} "Rotated SVG text")
		   (style :stroke :none :fill "#000000")
		   (rotate 30 20 40))))

(html-view (emit ex-text2))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><text y=\"40\" x=\"20\" style=\" fill: #000000;  stroke: none; \" transform=\"rotate(30,20,40)\" >Rotated SVG text</text></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><text y=\\\"40\\\" x=\\\"20\\\" style=\\\" fill: #000000;  stroke: none; \\\" transform=\\\"rotate(30,20,40)\\\" >Rotated SVG text</text></svg>\"}"}
;; <=

;; @@
(def ex-text3 (svg
	       (-> (text {:x 20 :y 40} "Styled SVG text")
		   (style :font-family "Arial"
			  :font-size 34
			  :stroke "#000000"
			  :fill "#00ff00"))))

(html-view (emit ex-text3))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><text y=\"40\" x=\"20\" style=\" font-size: 34;  fill: #00ff00;  stroke: #000000;  font-family: Arial; \" >Styled SVG text</text></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><text y=\\\"40\\\" x=\\\"20\\\" style=\\\" font-size: 34;  fill: #00ff00;  stroke: #000000;  font-family: Arial; \\\" >Styled SVG text</text></svg>\"}"}
;; <=

;; @@
(def ex-text4 (svg
	        (text {:x 20 :y 10}
		      (tspan "tspan line 1")
		      (tspan "tspan line 2")
		      (tspan "tspan line 3"))))

(html-view (emit ex-text4))

;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><text y=\"10\" x=\"20\" ><tspan >tspan line 1</tspan><tspan >tspan line 2</tspan><tspan >tspan line 3</tspan></text></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><text y=\\\"10\\\" x=\\\"20\\\" ><tspan >tspan line 1</tspan><tspan >tspan line 2</tspan><tspan >tspan line 3</tspan></text></svg>\"}"}
;; <=

;; @@
(def ex-text5 (svg
	        (text {:y 10}
		      (tspan {:x 0} "tspan line 1")
		      (tspan {:x 0 :dy 15} "tspan line 2")
		      (tspan {:x 0 :dy 15} "tspan line 3"))))

(html-view (emit ex-text5))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><text y=\"10\" ><tspan x=\"0\" >tspan line 1</tspan><tspan dy=\"15\" x=\"0\" >tspan line 2</tspan><tspan dy=\"15\" x=\"0\" >tspan line 3</tspan></text></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><text y=\\\"10\\\" ><tspan x=\\\"0\\\" >tspan line 1</tspan><tspan dy=\\\"15\\\" x=\\\"0\\\" >tspan line 2</tspan><tspan dy=\\\"15\\\" x=\\\"0\\\" >tspan line 3</tspan></text></svg>\"}"}
;; <=

;; @@
(def ex-tref (svg
	      (defs [:the-text (text "A text that is referenced.")])
	       (text {:x 20 :y 10} (tref :the-text))
	       (text {:x 30 :y 30} (tref :the-text))))

(html-view (emit ex-tref))

;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><defs ><text id=\"the-text\" >A text that is referenced.</text></defs><text y=\"10\" x=\"20\" ><tref xlink:href=\"#the-text\" /></text><text y=\"30\" x=\"30\" ><tref xlink:href=\"#the-text\" /></text></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><defs ><text id=\\\"the-text\\\" >A text that is referenced.</text></defs><text y=\\\"10\\\" x=\\\"20\\\" ><tref xlink:href=\\\"#the-text\\\" /></text><text y=\\\"30\\\" x=\\\"30\\\" ><tref xlink:href=\\\"#the-text\\\" /></text></svg>\"}"}
;; <=

;; @@
(def ex-text-path (svg
		   (defs [:my-path (path [:M [75,20]
					 :a [1,1 0 0,0 100,0]])])
		   (-> (text {:x 10 :y 100}
			     (text-path "Text along a curved path..." :my-path))
		       (style :stroke "#000000"))))

(html-view (emit ex-text-path))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><defs ><path id=\"my-path\" d=\" M75,20 a1,1,0,0,0,100,0\" /></defs><text y=\"100\" x=\"10\" style=\" stroke: #000000; \" ><textPath xlink:href=\"#my-path\" >Text along a curved path...</textPath></text></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><defs ><path id=\\\"my-path\\\" d=\\\" M75,20 a1,1,0,0,0,100,0\\\" /></defs><text y=\\\"100\\\" x=\\\"10\\\" style=\\\" stroke: #000000; \\\" ><textPath xlink:href=\\\"#my-path\\\" >Text along a curved path...</textPath></text></svg>\"}"}
;; <=

;; @@
(def ex-text-path2 (svg
		    (defs [:the-text (text "Text ref along a curved path...")
			   :my-path (path [:M [75,20]
					   :a [1,1 0 0,0 100,0]])])
		   (-> (text {:x 10 :y 100}
			     (text-path (tref :the-text) :my-path))
		       (style :stroke "#000000"))))

(html-view (emit ex-text-path2))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><defs ><text id=\"the-text\" >Text ref along a curved path...</text><path id=\"my-path\" d=\" M75,20 a1,1,0,0,0,100,0\" /></defs><text y=\"100\" x=\"10\" style=\" stroke: #000000; \" ><textPath xlink:href=\"#my-path\" ><tref xlink:href=\"#the-text\" /></textPath></text></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><defs ><text id=\\\"the-text\\\" >Text ref along a curved path...</text><path id=\\\"my-path\\\" d=\\\" M75,20 a1,1,0,0,0,100,0\\\" /></defs><text y=\\\"100\\\" x=\\\"10\\\" style=\\\" stroke: #000000; \\\" ><textPath xlink:href=\\\"#my-path\\\" ><tref xlink:href=\\\"#the-text\\\" /></textPath></text></svg>\"}"}
;; <=

;; @@
(def ex-img (svg
	     (-> (rect 10 10 130 500)
		 (style :fill "#000000"))
	     (image "http://jenkov.com/images/layout/top-bar-logo.png"
		    :x 20 :y 20 :width 300 :height 80)
	     (-> (line 25 80 350 80)
		 (style :stroke "#ffffff" :stroke-width 3))))

(html-view (emit ex-img))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><rect style=\" fill: #000000; \" x=\"10\" y=\"10\" height=\"130\" width=\"500\" /><image height=\"80\" x=\"20\" y=\"20\" width=\"300\" xlink:href=\"http://jenkov.com/images/layout/top-bar-logo.png\" /><line style=\" stroke: #ffffff;  stroke-width: 3; \" x1=\"25\" y1=\"80\" x2=\"350\" y2=\"80\" /></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><rect style=\\\" fill: #000000; \\\" x=\\\"10\\\" y=\\\"10\\\" height=\\\"130\\\" width=\\\"500\\\" /><image height=\\\"80\\\" x=\\\"20\\\" y=\\\"20\\\" width=\\\"300\\\" xlink:href=\\\"http://jenkov.com/images/layout/top-bar-logo.png\\\" /><line style=\\\" stroke: #ffffff;  stroke-width: 3; \\\" x1=\\\"25\\\" y1=\\\"80\\\" x2=\\\"350\\\" y2=\\\"80\\\" /></svg>\"}"}
;; <=

;; @@
(def ex-trans (svg
	       (-> (rect 50 50 110 110)
		   (style :stroke "#ff0000" :fill "#ccccff")
		   (translate 30)
		   (rotate 45 50 50))
	       (-> (text {:x 70 :y 100} "Hello World")
		   (translate 30)
		   (rotate 45 50 50))))

(html-view (emit ex-trans))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><rect transform=\"translate(30) rotate(45,50,50)\" style=\" fill: #ccccff;  stroke: #ff0000; \" x=\"50\" y=\"50\" height=\"110\" width=\"110\" /><text y=\"100\" x=\"70\" transform=\"translate(30) rotate(45,50,50)\" >Hello World</text></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><rect transform=\\\"translate(30) rotate(45,50,50)\\\" style=\\\" fill: #ccccff;  stroke: #ff0000; \\\" x=\\\"50\\\" y=\\\"50\\\" height=\\\"110\\\" width=\\\"110\\\" /><text y=\\\"100\\\" x=\\\"70\\\" transform=\\\"translate(30) rotate(45,50,50)\\\" >Hello World</text></svg>\"}"}
;; <=

;; @@
(def ex-animate (svg
		 (-> (rect 10 10 110 110)
		     (style :stroke "#ff0000" :fill "#0000ff")
		     (animate-transform :begin 0
					:dur 20
					:type :rotate
					:from "0 60 60"
					:to "360 60 60"
					:repeatCount :indefinite))))


(html-view (emit ex-animate))

;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><rect style=\" fill: #0000ff;  stroke: #ff0000; \" x=\"10\" y=\"10\" height=\"110\" width=\"110\" ><animateTransform type=\"rotate\" dur=\"20\" repeatCount=\"indefinite\" to=\"360 60 60\" from=\"0 60 60\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></rect></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><rect style=\\\" fill: #0000ff;  stroke: #ff0000; \\\" x=\\\"10\\\" y=\\\"10\\\" height=\\\"110\\\" width=\\\"110\\\" ><animateTransform type=\\\"rotate\\\" dur=\\\"20\\\" repeatCount=\\\"indefinite\\\" to=\\\"360 60 60\\\" from=\\\"0 60 60\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></rect></svg>\"}"}
;; <=

;; @@
;; http://www.w3.org/TR/SVG/animate.html
(def ex-anim2 (svg
	       ;(-> (rect 1 1 298 798)
		   ;(style :fill "none" :stroke "blue" :stroke-width 2))

	       ;(-> (rect 300 100 300 100)
		   ;(style :fill (rgb 255 255 0))
		   ;(animate :x :begin 0 :dur 9 :from 300 :to 0)
		   ;(animate :y :begin 0 :dur 9 :from 100 :to 0)
		   ;(animate :width :begin 0 :dur 9 :from 300 :to 800)
		   ;(animate :height :begin 0 :dur 9 :from 100 :to 300))

	       (-> (group
		     (-> (text "It's alive")
			 (style :font-family :Verdana
				:font-size 35.27
				:visibility :hidden)
			 (animate :visibility :to :visible :begin 3)
			 (animate-motion :path (draw :M [0 0] :L [100 100])
					 :begin 3 :dur 6)
			 (animate-color :fill :from (rgb 0 0 255) :to (rgb 128 0 0)
					:begin 3 :dur 6)
			 (animate-transform :type :rotate :from -30 :to 0 :begin 3 :dur 6)
			 (animate-transform :type :scale :from 1 :to 3 :additive :sum
					    :begin 3 :dur 6)))
		   (translate 100 100))))

(html-view (emit ex-anim2))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><g transform=\"translate(100,100)\" ><text style=\" visibility: hidden;  font-size: 35.27;  font-family: Verdana; \" >It's alive<animate to=\"visible\" attributeName=\"visibility\" begin=\"3\" fill=\"freeze\" /><animateMotion dur=\"6\" path=\" M0,0 L100,100\" begin=\"3\" fill=\"freeze\" /><animateColor dur=\"6\" to=\"rgb(128,0,0)\" from=\"rgb(0,0,255)\" attributeName=\"fill\" begin=\"3\" fill=\"freeze\" /><animateTransform type=\"rotate\" dur=\"6\" to=\"0\" from=\"-30\" begin=\"3\" attributeName=\"transform\" fill=\"freeze\" /><animateTransform additive=\"sum\" type=\"scale\" dur=\"6\" to=\"3\" from=\"1\" begin=\"3\" attributeName=\"transform\" fill=\"freeze\" /></text></g></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><g transform=\\\"translate(100,100)\\\" ><text style=\\\" visibility: hidden;  font-size: 35.27;  font-family: Verdana; \\\" >It's alive<animate to=\\\"visible\\\" attributeName=\\\"visibility\\\" begin=\\\"3\\\" fill=\\\"freeze\\\" /><animateMotion dur=\\\"6\\\" path=\\\" M0,0 L100,100\\\" begin=\\\"3\\\" fill=\\\"freeze\\\" /><animateColor dur=\\\"6\\\" to=\\\"rgb(128,0,0)\\\" from=\\\"rgb(0,0,255)\\\" attributeName=\\\"fill\\\" begin=\\\"3\\\" fill=\\\"freeze\\\" /><animateTransform type=\\\"rotate\\\" dur=\\\"6\\\" to=\\\"0\\\" from=\\\"-30\\\" begin=\\\"3\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /><animateTransform additive=\\\"sum\\\" type=\\\"scale\\\" dur=\\\"6\\\" to=\\\"3\\\" from=\\\"1\\\" begin=\\\"3\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></text></g></svg>\"}"}
;; <=

;; @@
(def ex-anim-logo (svg
		   (-> (image "http://clojure.org/space/showimage/clojure-icon.gif"
			      :width 100 :height 100)
		       (animate :x :begin 0 :dur 9 :from 0 :to 300)
		       (animate :y :begin 0 :dur 9 :from 0 :to 300))))

(html-view (emit ex-anim-logo))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><image height=\"100\" width=\"100\" xlink:href=\"http://clojure.org/space/showimage/clojure-icon.gif\" ><animate dur=\"9\" to=\"300\" from=\"0\" attributeName=\"x\" begin=\"0\" fill=\"freeze\" /><animate dur=\"9\" to=\"300\" from=\"0\" attributeName=\"y\" begin=\"0\" fill=\"freeze\" /></image></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><image height=\\\"100\\\" width=\\\"100\\\" xlink:href=\\\"http://clojure.org/space/showimage/clojure-icon.gif\\\" ><animate dur=\\\"9\\\" to=\\\"300\\\" from=\\\"0\\\" attributeName=\\\"x\\\" begin=\\\"0\\\" fill=\\\"freeze\\\" /><animate dur=\\\"9\\\" to=\\\"300\\\" from=\\\"0\\\" attributeName=\\\"y\\\" begin=\\\"0\\\" fill=\\\"freeze\\\" /></image></svg>\"}"}
;; <=

;; @@
(def ex-anim-logo2 (svg
		   (-> (image "http://clojure.org/space/showimage/clojure-icon.gif"
			      :width 100 :height 100)
		       (animate-transform :type :rotate :from -30 :to 0 :begin 0 :dur 6))))

(html-view (emit ex-anim-logo2))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><image height=\"100\" width=\"100\" xlink:href=\"http://clojure.org/space/showimage/clojure-icon.gif\" ><animateTransform type=\"rotate\" dur=\"6\" to=\"0\" from=\"-30\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></image></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><image height=\\\"100\\\" width=\\\"100\\\" xlink:href=\\\"http://clojure.org/space/showimage/clojure-icon.gif\\\" ><animateTransform type=\\\"rotate\\\" dur=\\\"6\\\" to=\\\"0\\\" from=\\\"-30\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></image></svg>\"}"}
;; <=

;; @@
(def ex-anim-logo3 (svg
		    (-> (group
			 (-> (image "http://clojure.org/space/showimage/clojure-icon.gif"
				    :width 100 :height 100)
			     (animate-transform :type :rotate
						:begin 0
						:dur 20
						:from "0 50 50"
						:to "360 50 50"
						:repeatCount :indefinite)))

		       (translate 100 100))))

(html-view (emit ex-anim-logo3))
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\" ><g transform=\"translate(100,100)\" ><image height=\"100\" width=\"100\" xlink:href=\"http://clojure.org/space/showimage/clojure-icon.gif\" ><animateTransform type=\"rotate\" dur=\"20\" repeatCount=\"indefinite\" to=\"360 50 50\" from=\"0 50 50\" begin=\"0\" attributeName=\"transform\" fill=\"freeze\" /></image></g></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" version=\\\"1.0\\\" xmlns=\\\"http://www.w3.org/2000/svg\\\" ><g transform=\\\"translate(100,100)\\\" ><image height=\\\"100\\\" width=\\\"100\\\" xlink:href=\\\"http://clojure.org/space/showimage/clojure-icon.gif\\\" ><animateTransform type=\\\"rotate\\\" dur=\\\"20\\\" repeatCount=\\\"indefinite\\\" to=\\\"360 50 50\\\" from=\\\"0 50 50\\\" begin=\\\"0\\\" attributeName=\\\"transform\\\" fill=\\\"freeze\\\" /></image></g></svg>\"}"}
;; <=

;; @@
(defn txt-box [txt x y fill]
  (let [box-width 300
	box-height 50]
    (-> (svg
	  (group
	   (-> (rect 0 0 box-height box-width :rx 5 :ry 5)
	       (style :stroke fill :fill fill))
	   (-> (text txt)
	       (add-attrs :x (/ box-width 2)
			  :y (/ box-height 2))
	       (style :fill "#ffffff"
		      :font-size "25px"
		      :font-family "Verdana"
		      :alignment-baseline :middle
		      :text-anchor :middle))))
	(add-attrs :x x :y y))))

(defn analemma-stack [directory]
  (spit (str directory "/analemma-stack.svg")
	(emit
	 (svg
	  (-> (group
	       (-> (txt-box "analemma.charts" 0 10 "#006600")
		   (add-attrs :visibility :hidden)
		   (animate :visibility :to :visible :begin 5)
		   (animate :y :begin 5 :dur 1 :from 0 :to 10))
	       (-> (txt-box "analemma.svg" 0 65 "#660000")
		   (add-attrs :visibility :hidden)
		   (animate :visibility :to :visible :begin 3)
		   (animate :y :begin 3 :dur 2 :from 0 :to 65))
	       (-> (txt-box "analemma.xml" 0 120 "#000066")
		   (add-attrs :visibility :hidden)
		   (animate :visibility :to :visible :begin 1)
		   (animate :y :begin 1 :dur 4 :from 0 :to 120)))
	      (translate 10 10))))))


;; @@

;; @@

(defn parse-us-map []
  (parse-xml (slurp "http://upload.wikimedia.org/wikipedia/commons/3/32/Blank_US_Map.svg")))

(defn hide-california [filename]
  (spit filename
	(emit
	 (transform-xml (parse-us-map)
			[{:id "CA"}]
			#(add-attrs % :visibility "hidden")))))

(defn color-maryland [filename]
  (spit filename
	(emit
	 (transform-xml (parse-us-map)
			[{:id "MD"}]
			(fn [elem]
			  (-> (add-style elem :fill "#0000ff")
			      (add-attrs :transform "scale(1.10)")))))))


(defn select-maryland [filename]
  (spit filename
	(emit
	 (svg (-> (apply svg {:x -600 :y -200}
			 (filter-xml (parse-us-map)
				     [[:or "sodipodi:namedview" :defs {:id "MD"}]]))
		  (transform-xml [:svg]
				 #(add-attrs %
			            "xmlns:sodipodi" "http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd"
				    "xmlns:inkscape" "http://www.inkscape.org/namespaces/inkscape"))
		  (transform-xml [{:id "MD"}]
				 (fn [elem]
				   (add-attrs elem :transform "scale(1.5)"))))))))

(defn to-hex-string [n] (str "#" (Integer/toHexString n)))

;;(to-hex-string (translate-value 0.5 0 1 0 16777215))

(def us-states
     {"AK"	 "ALASKA"
      "AL"	 "ALABAMA"
      "AR"	 "ARKANSAS"
      "AS"	 "AMERICAN SAMOA"
      "AZ"	 "ARIZONA"
      "CA"	 "CALIFORNIA"
      "CO"	 "COLORADO"
      "CT"	 "CONNECTICUT"
      "DC"	 "WASHINGTON, DC"
      "DE"	 "DELAWARE"
      "FL"	 "FLORIDA"
      "FM"	 "FEDERATED STATES OF MICRONESIA"
      "GA"	 "GEORGIA"
      "GU"	 "GUAM"
      "HI"	 "HAWAII"
      "IA"	 "IOWA"
      "ID"	 "IDAHO"
      "IL"	 "ILLINOIS"
      "IN"	 "INDIANA"
      "KS"	 "KANSAS"
      "KY"	 "KENTUCKY"
      "LA"	 "LOUISIANA"
      "MA"	 "MASSACHUSETTS"
      "MD"	 "MARYLAND"
      "ME"	 "MAINE"
      "MH"	 "MARSHALL ISLANDS"
      "MI"	 "MICHIGAN"
      "MN"	 "MINNESOTA"
      "MO"	 "MISSOURI"
      "MP"	 "NORTHERN MARIANA ISLANDS"
      "MS"	 "MISSISSIPPI"
      "MT"	 "MONTANA"
      "NC"	 "NORTH CAROLINA"
      "ND"	 "NORTH DAKOTA"
      "NE"	 "NEBRASKA"
      "NH"	 "NEW HAMPSHIRE"
      "NJ"	 "NEW JERSEY"
      "NM"	 "NEW MEXICO"
      "NV"	 "NEVADA"
      "NY"	 "NEW YORK"
      "OH"	 "OHIO"
      "OK"	 "OKLAHOMA"
      "OR"	 "OREGON"
      "PA"	 "PENNSYLVANIA"
      "PR"	 "PUERTO RICO"
      "PW"	 "PALAU"
      "RI"	 "RHODE ISLAND"
      "SC"	 "SOUTH CAROLINA"
      "SD"	 "SOUTH DAKOTA"
      "TN"	 "TENNESSEE"
      "TX"	 "TEXAS"
      "UT"	 "UTAH"
      "VA"	 "VIRGINIA"
      "VI"	 "VIRGIN ISLANDS"
      "VT"	 "VERMONT"
      "WA"	 "WASHINGTON"
      "WI"	 "WISCONSIN"
      "WV"	 "WEST VIRGINIA"
      "WY"	 "WYOMING"})

(defn color-states [filename]
  (spit filename
	(emit
	 (transform-xml (parse-us-map)
			[[:and [:not {:id "path57"}] [:or :g :path]]]
			(fn [elem]
			  (add-style elem :fill (to-hex-string (rand 16777215))))))))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.svg/color-states</span>","value":"#'examples.svg/color-states"}
;; <=
