;; gorilla-repl.fileformat = 1

;; **
;;; # Test Repl
;;; 
;;; Welcome to gorilla :-) Shift + enter evaluates code. Poke the question mark (top right) to learn more ...
;; **

;; @@
(ns cdk-clojure.core
  (:use [gorilla-repl html])
  (:require [clojure.java.io :as io])
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
           [gorilla-renderable.core]
           [clojure.pprint]
           [hiccup.core]))

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
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;C&quot;</span>","value":"\"C\""}
;; <=

;; @@
(def points (map #(.getPoint2d %)  atoms))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;cdk-clojure.core/points</span>","value":"#'cdk-clojure.core/points"}
;; <=

;; @@
(use '[gorilla-repl table latex html])
(defn basic_points
  [points]
  (html [:svg {:height 100 :width 100}
         (map #([circle {:cx (.x %)
                           :cy (.y %)
                           :r 40  :stroke "black" :stroke-width 4 :fill "white"}])
                points
                )]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(html)
;; @@

;; @@

;; @@
