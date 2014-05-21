(ns cdk-clojure.core
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
           [javax.imageio ImageIO]))


; set the graphic parameters and initialize an image
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
(def triazole2D (makemol triazole))


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
(ImageIO/write image "PNG" (io/file "/Users/zachpowers/Desktop/triazole.png") )