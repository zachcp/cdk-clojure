(ns drawmolecule.core
  (:require [clojure.java.io :as io])
  (:import [org.openscience.cdk.layout StructureDiagramGenerator]
           [org.openscience.cdk.smiles SmilesParser]
           [org.openscience.cdk.interfaces IChemObjectBuilder IChemObject]
           [org.openscience.cdk.silent SilentChemObjectBuilder]
           [org.openscience.cdk.renderer AtomContainerRenderer ]
           [org.openscience.cdk.renderer.font AWTFontManager ]
           [org.openscience.cdk.renderer.generators BasicSceneGenerator BasicBondGenerator BasicAtomGenerator]
           [org.openscience.cdk.renderer.visitor  AWTDrawVisitor]
           [org.openscience.cdk.templates MoleculeFactory]
           [java.awt Rectangle Graphics2D Color]
           [java.awt.image BufferedImage]
           [java.util ArrayList]
           [javax.imageio ImageIO]))


(defn generate_molecule_image
  [ molecule, width, height, filename ]
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


(defn parsesmiles [smiles]
  ; return an IAtomContainer from smiles input
  (let [builder (SilentChemObjectBuilder/getInstance)
        sp      (SmilesParser. builder)]
       (.parseSmiles sp smiles)))



(def caffeine (parsesmiles "CN1C=NC2=C1C(=O)N(C(=O)N2C)C"))
; save a representation of it
(generate_molecule_image caffeine 250 250 "caffeine.png")