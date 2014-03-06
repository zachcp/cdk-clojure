(ns cdk-clojure.efficientbits
  (:use [clojure.java.javadoc])
  (:require [clojure.string :as string]
            ;[plumbing.graph :as graph]
            ;[schema.core :as s]
            )
  (:import [org.openscience.cdk.inchi InChIGeneratorFactory]
           [org.openscience.cdk.layout StructureDiagramGenerator]
           ;[org.openscience.cdk.applications.swing MoleculeViewer2D]
           [org.openscience.cdk.smiles SmilesParser SmilesGenerator]
           [org.openscience.cdk.interfaces IChemObjectBuilder]
           [org.openscience.cdk.silent SilentChemObjectBuilder]))

;This is an attempt to recreate some CDK code from
; http://efficientbits.blogspot.nl/2013/12/new-smiles-behaviour-parsing-cdk-154.html


; Section 1
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; IChemObjectBuilder     builder = SilentChemObjectBuilder.getInstance();
;; SmilesParser           sp      = new SmilesParser(builder);
;; SmilesGenerator        sg      = new SmilesGenerator();
;; InChIGeneratorFactory  igf     = InChIGeneratorFactory.getInstance();

;; IAtomContainer m = sp.parseSmiles("[O]");
;; System.out.println(sg.create(m));                         // [O]
;; System.out.println(igf.getInChIGenerator(m).getInchi());  // InChI=1S/O

;; // configure atom types
;; AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(m);
;; CDKHydrogenAdder.getInstance(builder).addImplicitHydrogens(m);

;; System.out.println(sg.create(m));                         // O ([OH2])
;; System.out.println(igf.getInChIGenerator(m).getInchi());  // InChI=1S/H2O/h1H2

(def builder (SilentChemObjectBuilder/getInstance))
(def sp (SmilesParser. builder))
(def sg (SmilesGenerator.))
(def igf (InChIGeneratorFactory/getInstance))

;returns a SmilesParser not an IAtomContainer
; (def m  (doto sp (.parseSmiles "[O]")))
(def m  (-> sp  (.parseSmiles "[O]")))
(-> sg (.create m))
(-> igf  (.getInChIGenerator m) (.getInchi))


; refactor
;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn parse-smiles [smi]
    (let [ builder (SilentChemObjectBuilder/getInstance)
           sp (SmilesParser. builder)
           sg (SmilesGenerator.)
           igf (InChIGeneratorFactory/getInstance)
           mol (-> sp (.parseSmiles smi))
           ]
      {:smiles (-> sg (.create mol ))
       :inchi  (-> igf  (.getInChIGenerator mol) (.getInchi))}))


(parse-smiles "CCCCC1NNCC1")
(parse-smiles "c1cccc1c1cccc1")
(parse-smiles "oc1ccocc1")
(parse-smiles "oc(cc)co")
;problem with kekulization
(parse-smiles "NC(Cc1c[n]c2ccccc12)C(O)=O")







