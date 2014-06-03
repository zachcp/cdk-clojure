;; gorilla-repl.fileformat = 1

;; **
;;; # Test Repl
;;; 
;;; Welcome to gorilla :-) Shift + enter evaluates code. Poke the question mark (top right) to learn more ...
;; **

;; @@
(ns cdk-clojure.core
  (:require [clojure.java.io :as io]
            [gorilla-repl table latex html]
            [gorilla-renderable.core]
            [gorilla-plot.core]
            [hiccup.core])
  (:import [org.openscience.cdk.layout StructureDiagramGenerator]
           [org.openscience.cdk.renderer AtomContainerRenderer ]
           [org.openscience.cdk.renderer.font AWTFontManager ]
           [org.openscience.cdk.renderer.generators BasicSceneGenerator BasicBondGenerator BasicAtomGenerator]
           [org.openscience.cdk.renderer.visitor  AWTDrawVisitor]
           [org.openscience.cdk.templates MoleculeFactory]
           [java.awt Rectangle Graphics2D Color]
           [java.awt.image BufferedImage]
           [java.util ArrayList]
           [javax.imageio ImageIO]
           [clojure.pprint]))

;(use '[gorilla-repl table latex html])
(use 'hiccup.core)
(use '[gorilla-repl table latex html])

(use 'analemma.svg)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Load Molecule Using CDK
;; **

;; @@
(def w 200)
(def h 200)
(def drawArea (new Rectangle w h))
(def image (new BufferedImage w h 1)) ; TYPE_INT+RGB is the last field = 1
(def ^Graphics2D g (doto (. image getGraphics)
                         (.fillRect 0 0 w h)))



; get a molecule
(def triazole1 (MoleculeFactory/make123Triazole))

; The Structure Diagram generator will create the Coordinates for your molecule
(defn makemol [mol]
     (let [sdg (doto (new StructureDiagramGenerator)
                     (.setMolecule mol)
                     (.generateCoordinates))]
          (.getMolecule sdg)))

; generate the coordinates
(def triazole2D (makemol triazole1))

;generators make the image elements
(defn make_generator []
  (let [gen (doto (new ArrayList)
                  (.add (new BasicSceneGenerator))
                  (.add (new BasicBondGenerator))
                  (.add (new BasicAtomGenerator)))]
      gen))


(def  gen (make_generator))

;the renderer needs to have a toolkit-specific font manager
(def renderer (AtomContainerRenderer. gen (new AWTFontManager)))


; Run Setup and then paint the molecule
(-> renderer (. setup triazole2D drawArea))
(-> renderer (. paint triazole2D (new AWTDrawVisitor g) ))

;Output to a file
;(ImageIO/write image "PNG" (io/file "/Users/zachpowers/Desktop/triazole.png") )
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#&lt;Rectangle java.awt.Rectangle[x=60,y=58,width=81,height=84]&gt;</span>","value":"#<Rectangle java.awt.Rectangle[x=60,y=58,width=81,height=84]>"}
;; <=

;; @@
;get atoms
(def atoms (seq (.atoms triazole2D)))
(def bonds (seq (.bonds triazole2D)))

;get symboy of atoms
(.getSymbol (first atoms))

(def points (map #(.getPoint2d %)  atoms))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;cdk-clojure.core/points</span>","value":"#'cdk-clojure.core/points"}
;; <=

;; @@
(defn circle 
  [x y]
  [:circle {:cx x :cy y  :r 4 :stroke "black" :stroke-width 2 :fill "white"}])



(defn basic_points
  [points]
   ( html [:svg {:height 100 :width 100}
         (map #(circle (+ 50 (* 10 (.x %))) (+ 50 (* 10 (.y %)))) points)]))


(basic_points points)
(html-view(basic_points points))
;; @@
;; =>
;;; {"type":"html","content":"<svg height=\"100\" width=\"100\"><circle cx=\"50.0\" cy=\"50.0\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle><circle cx=\"50.0\" cy=\"65.0\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle><circle cx=\"35.734152255572695\" cy=\"69.63525491562422\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle><circle cx=\"26.917373471185606\" cy=\"57.5\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle><circle cx=\"35.734152255572695\" cy=\"45.36474508437579\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<svg height=\\\"100\\\" width=\\\"100\\\"><circle cx=\\\"50.0\\\" cy=\\\"50.0\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle><circle cx=\\\"50.0\\\" cy=\\\"65.0\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle><circle cx=\\\"35.734152255572695\\\" cy=\\\"69.63525491562422\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle><circle cx=\\\"26.917373471185606\\\" cy=\\\"57.5\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle><circle cx=\\\"35.734152255572695\\\" cy=\\\"45.36474508437579\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle></svg>\"}"}
;; <=

;; @@
(def b (first bonds))
(print (seq (.atoms b)))
;(print (.getOrder b))
;(first (.atoms b))
(second (.atoms b))

;; @@
;; ->
;;; (#&lt;Atom Atom(9879141, S:C, 2D:[(0.0, 0.0)], AtomType(9879141, FC:0, Isotope(9879141, Element(9879141, S:C, AN:6))))&gt; #&lt;Atom Atom(1042854178, S:N, 2D:[(0.0, 1.5)], AtomType(1042854178, FC:0, Isotope(1042854178, Element(1042854178, S:N, AN:7))))&gt;)
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#&lt;Atom Atom(1042854178, S:N, 2D:[(0.0, 1.5)], AtomType(1042854178, FC:0, Isotope(1042854178, Element(1042854178, S:N, AN:7))))&gt;</span>","value":"#<Atom Atom(1042854178, S:N, 2D:[(0.0, 1.5)], AtomType(1042854178, FC:0, Isotope(1042854178, Element(1042854178, S:N, AN:7))))>"}
;; <=

;; @@
(defn line [x1 y1 x2 y2 & options]
  (let [attrs (apply hash-map options)]
    [:line (apply merge {:x1 x1, :y1 y1, :x2 x2, :y2 y2} attrs)]))

(defn basic_lines 
  [bonds]
  ( let [ x1 (fn [b] (.x (.getPoint2d (first  (.atoms b)))))
          y1 (fn [b] (.y (.getPoint2d (first  (.atoms b)))))
          x2 (fn [b] (.x (.getPoint2d (second (.atoms b)))))
          y2 (fn [b] (.y (.getPoint2d (second (.atoms b)))))
          positions (juxt x1 y1 x2 y2)]
   (html [:svg {:height 100 :width 100}
         (map #(line (+ 50 (* 10 (x1 %)))
                     (+ 50 (* 10 (y1 %)))
                     (+ 50 (* 10 (x2 %)))
                     (+ 50 (* 10 (y2 %))))
                     bonds)])))



(html-view (basic_lines bonds))
(basic_lines bonds)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;&lt;svg height=\\&quot;100\\&quot; width=\\&quot;100\\&quot;&gt;&lt;line x1=\\&quot;50.0\\&quot; x2=\\&quot;50.0\\&quot; y1=\\&quot;50.0\\&quot; y2=\\&quot;65.0\\&quot;&gt;&lt;/line&gt;&lt;line x1=\\&quot;50.0\\&quot; x2=\\&quot;35.734152255572695\\&quot; y1=\\&quot;65.0\\&quot; y2=\\&quot;69.63525491562422\\&quot;&gt;&lt;/line&gt;&lt;line x1=\\&quot;35.734152255572695\\&quot; x2=\\&quot;26.917373471185606\\&quot; y1=\\&quot;69.63525491562422\\&quot; y2=\\&quot;57.5\\&quot;&gt;&lt;/line&gt;&lt;line x1=\\&quot;26.917373471185606\\&quot; x2=\\&quot;35.734152255572695\\&quot; y1=\\&quot;57.5\\&quot; y2=\\&quot;45.36474508437579\\&quot;&gt;&lt;/line&gt;&lt;line x1=\\&quot;35.734152255572695\\&quot; x2=\\&quot;50.0\\&quot; y1=\\&quot;45.36474508437579\\&quot; y2=\\&quot;50.0\\&quot;&gt;&lt;/line&gt;&lt;/svg&gt;&quot;</span>","value":"\"<svg height=\\\"100\\\" width=\\\"100\\\"><line x1=\\\"50.0\\\" x2=\\\"50.0\\\" y1=\\\"50.0\\\" y2=\\\"65.0\\\"></line><line x1=\\\"50.0\\\" x2=\\\"35.734152255572695\\\" y1=\\\"65.0\\\" y2=\\\"69.63525491562422\\\"></line><line x1=\\\"35.734152255572695\\\" x2=\\\"26.917373471185606\\\" y1=\\\"69.63525491562422\\\" y2=\\\"57.5\\\"></line><line x1=\\\"26.917373471185606\\\" x2=\\\"35.734152255572695\\\" y1=\\\"57.5\\\" y2=\\\"45.36474508437579\\\"></line><line x1=\\\"35.734152255572695\\\" x2=\\\"50.0\\\" y1=\\\"45.36474508437579\\\" y2=\\\"50.0\\\"></line></svg>\""}
;; <=

;; @@
(basic_lines bonds)
;; @@

;; @@
(first bonds)
(def a [1 2 3 4])
(apply list a)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-list'>(<span>","close":"<span class='clj-list'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>1</span>","value":"1"},{"type":"html","content":"<span class='clj-long'>2</span>","value":"2"},{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"},{"type":"html","content":"<span class='clj-long'>4</span>","value":"4"}],"value":"(1 2 3 4)"}
;; <=

;; @@
(.getPoint2d (first (.atoms (first bonds))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#&lt;Point2d (0.0, 0.0)&gt;</span>","value":"#<Point2d (0.0, 0.0)>"}
;; <=

;; @@

;; @@
