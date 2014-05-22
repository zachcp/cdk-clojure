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
(use 'hiccup.core)

(defn circle 
  [x y]
  [:circle {:cx x :cy y  :r 4 :stroke "black" :stroke-width 2 :fill "white"}])



(defn basic_points
  [points]
   ( html [:svg {:height 100 :width 100}
         (map #(circle (+ 50 (* 10 (.x %))) (+ 50 (* 10 (.y %)))) points)]))


(basic_points points)
(html-view (basic_points points))
;; @@
;; =>
;;; {"type":"html","content":"<svg height=\"100\" width=\"100\"><circle cx=\"50.0\" cy=\"50.0\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle><circle cx=\"50.0\" cy=\"65.0\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle><circle cx=\"35.734152255572695\" cy=\"69.63525491562422\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle><circle cx=\"26.917373471185606\" cy=\"57.5\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle><circle cx=\"35.734152255572695\" cy=\"45.36474508437579\" fill=\"white\" r=\"4\" stroke-width=\"2\" stroke=\"black\"></circle></svg>","value":"#gorilla_repl.html.HtmlView{:content \"<svg height=\\\"100\\\" width=\\\"100\\\"><circle cx=\\\"50.0\\\" cy=\\\"50.0\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle><circle cx=\\\"50.0\\\" cy=\\\"65.0\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle><circle cx=\\\"35.734152255572695\\\" cy=\\\"69.63525491562422\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle><circle cx=\\\"26.917373471185606\\\" cy=\\\"57.5\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle><circle cx=\\\"35.734152255572695\\\" cy=\\\"45.36474508437579\\\" fill=\\\"white\\\" r=\\\"4\\\" stroke-width=\\\"2\\\" stroke=\\\"black\\\"></circle></svg>\"}"}
;; <=
