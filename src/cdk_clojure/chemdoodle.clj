(ns cdk-clojure.chemdoodle
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json])
  (:import [org.openscience.cdk.layout StructureDiagramGenerator]
           [org.openscience.cdk.interfaces IAtomContainer IBond IAtom]
           [org.openscience.cdk.renderer AtomContainerRenderer ]
           [org.openscience.cdk.renderer.font AWTFontManager ]
           [org.openscience.cdk.renderer.generators BasicSceneGenerator BasicBondGenerator BasicAtomGenerator]
           [org.openscience.cdk.renderer.visitor  AWTDrawVisitor]
           [org.openscience.cdk.templates MoleculeFactory]
           [java.awt Rectangle Graphics2D Color]
           [java.awt.image BufferedImage]
           [java.util ArrayList]
           [javax.imageio ImageIO]))


; chemdoodle JSON format
; molecule and shape arrays
; { "m": [{},{},{}] ,
;   "s": [{},{},{}]}
;
; Molecule
; array of atoms and bonds
; {"a": [{"i": uniqueID
;         "l": label,
;         "x": xcoord,
;         "y": ycoord,
;         "z": zcoord,
;         "c": charge,
;         "m": mass,
;         "r": radicals,
;         "p": lone pairs,
;         "q": boolena any,
;         "rg": rg = rgroup number},{},{}],
;  "b": [{"i": uniqueID
;         "b": index of start atom,
;         "e": inex of end atom,
;         "o": bond order,
;         "s": Sterochemistry 'none', 'protruding', 'recessed' or 'ambiguous'.
;         },{},{}]}
;
;Methane
;{“a”:[ {"y":0,"x":0} ]}
;Ethane
;{“b”:[{"e":1,"b":0}],”a”:[{"y":-5,"x":-8.6603},{"y":5,"x":8.6603}]
;

;Generate Coordinates
(defn generate_molecule
  [ molecule ]
  (let [ sdg   (doto (new StructureDiagramGenerator)
                     (.setMolecule molecule)
                     (.generateCoordinates))
         mol2d (.getMolecule sdg)]
         mol2d ))

(defn IAtomContainer->JSON [mol]
  (let [aindex  (into [] (map #(.hashCode %) (.atoms mol)))
        bindex  (fn [b]  (map #(.hashCode %) (.atoms b)))
        batoms  (fn [x] (map #(.indexOf aindex %) x))

        getatom  (fn [x] (apply hash-map
                           ["l" (.getSymbol x)
                            "x" (.x (.getPoint2d x))
                            "y" (.y (.getPoint2d x))]))
        getbond  (fn [x] (apply hash-map
                            ["e" (first (batoms (bindex x)))
                             "b" (second (batoms (bindex x)))
                             "o" (str (.getOrder x))]))

        atoms (seq (.atoms mol))
        bonds (seq (.bonds mol))]
   (json/write-str
     {"a" (into [] (map getatom atoms))
      "b" (into [] (map getbond bonds))})))


; make a molecule
(def triazole (generate_molecule (MoleculeFactory/make123Triazole)))

;turn the molecule into webomcponente comsumable JSON
(IAtomContainer->JSON triazole)
