(ns cdk-clojure.core
  (:use [clojure.java.javadoc]
        [seesaw.core])
  (:require [clojure.string :as string])
  (:import [org.openscience.cdk.smiles SmilesParser]
           [org.openscience.cdk DefaultChemObjectBuilder]
           [org.openscience.cdk.tools.manipulator AtomContainerManipulator]
           [org.openscience.cdk CDKConstants]
           [org.openscience.cdk.fingerprint MACCSFingerprinter]
           [org.openscience.cdk.interfaces IAtomContainer]
           [org.openscience.cdk.layout StructureDiagramGenerator]
           [org.openscience.cdk.renderer AtomContainerRenderer ]
           [org.openscience.cdk.renderer.font AWTFontManager ]
           [org.openscience.cdk.renderer.generators BasicSceneGenerator BasicBondGenerator BasicAtomGenerator]
           [org.openscience.cdk.renderer.visitor  AWTDrawVisitor]
           [org.openscience.cdk.templates MoleculeFactory]

           [java.awt Rectangle Graphics2D Color Graphics]
           [java.awt.image BufferedImage]
           [java.util ArrayList]
           [javax.imageio ImageIO]))

;minimal functions desired:
; parsing smarts,
; making an image
; calculating 2D coordinates of a molecule
; outputing a simple clojure map of the molecule
;    for this one we should define types
; output for chemdoodleweb JSON

(defn parse-smiles [smiles]
  "return an IAtomContainer from smiles input"
  (let [builder (SilentChemObjectBuilder/getInstance)
        sp      (SmilesParser. builder)]
    (.parseSmiles sp smiles)))

(defn draw-molecule
  "take an IAomContainer and make a drawing"
  [ ^IAtomContainer molecule, width, height, filename ]
  (let [ ;; Generate Coordinates and Structure Information for the Molecule
         sdg   (doto (new StructureDiagramGenerator)
                 (.setMolecule molecule)
                 (.generateCoordinates))
         mol2d (.getMolecule sdg)
         gen   (doto (new ArrayList)
                 (.add (new BasicSceneGenerator))
                 (.add (new BasicBondGenerator))
                 (.add (new BasicAtomGenerator)))
         ;;Setup the Drawing
         image     (new BufferedImage width height 1)
         drawArea  (new Rectangle width height)
         g         (doto (. image getGraphics)
                     (.fillRect 0 0 width height))
         renderer (AtomContainerRenderer. gen (new AWTFontManager))]

    (-> renderer (. setup mol2d drawArea))
    (-> renderer (. paint mol2d (new AWTDrawVisitor g) ))
    (ImageIO/write image "PNG" (io/file filename))))


(defn IAtomContainer->clj
  "convert IAtom Container to Chemdoodle JSON Format"
  [^IAtomContainer mol]
    (let [x ]))

(defn IAtomContainer->JSON
  "convert IAtom Container to Chemdoodle JSON Format"
  [mol])