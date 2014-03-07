(ns cdk-clojure.core
  (:use [clojure.java.javadoc])
  (:require [clojure.string :as string])
  (:import [org.openscience.cdk.smiles SmilesParser]
           [org.openscience.cdk DefaultChemObjectBuilder]
           [org.openscience.cdk.aromaticity CDKHueckelAromaticityDetector]
           [org.openscience.cdk.tools.manipulator AtomContainerManipulator]
           [org.openscience.cdk CDKConstants]
           [org.openscience.cdk.fingerprint MACCSFingerprinter]
           [org.openscience.cdk ]
           [org.openscience.cdk.interfaces ]
           [org.openscience.cdk.layout StructureDiagramGenerator]
           [org.openscience.cdk.renderer AtomContainerRenderer ]
           [org.openscience.cdk.renderer.font AWTFontManager ]
           [org.openscience.cdk.renderer.generators BasicSceneGenerator BasicBondGenerator BasicAtomGenerator]
           [org.openscience.cdk.renderer.visitor ]
           [org.openscience.cdk.templates MoleculeFactory]
           [java.awt Rectangle Graphics2D Color]
           [java.awt.image BufferedImage]
           [java.util ArrayList]
           [java.imageio]))


(javadoc java.awt.Point)

(def width 200)
(def height 200)


;the draw area and the image should be the same size
(def drawArea (new Rectangle width height))
(def image (new BufferedImage width height 1)) ; TYPE_INT+RGB is the last field = 1


;Note that the make triazole is a submethod of Molecule Factory which we can access by
; prexifing with ..
(def triazole (.. MoleculeFactory (make123Triazole)))
; Alternative, maybe better way...
(def triazole1 (MoleculeFactory/make123Triazole))


; Create a Structure Diagrma generator for your Molecule
;; IMolecule triazole = MoleculeFactory.make123Triazole();
;; StructureDiagramGenerator sdg = new StructureDiagramGenerator();
;; sdg.setMolecule(triazole);
;; sdg.generateCoordinates();
;; triazole = sdg.getMolecule();

(defn makemol [mol]
     (let [sdg (doto (new StructureDiagramGenerator)
                     (.setMolecule mol)
                     (.generateCoordinates))]
          (.getMolecule sdg)))

(def triazole2D (makemol triazole))
;generators make the image elements

(defn make_generator []
  (let [gen (doto (new ArrayList)
                  (.add (new BasicSceneGenerator))
                  (.add (new BasicBondGenerator))
                  (.add (new BasicAtomGenerator)))]
      gen))


(def  gen (make_generator))
(type gen)
(type (first gen))


;the renderer needs to have a toolkit-specific font manager
(def renderer (AtomContainerRenderer. gen (new AWTFontManager)))


; Run Setup Once
(-> renderer (. setup triazole2D drawArea))
(type renderer)

;the call to 'setup' only needs to be done on the first paint
;renderer.setup(triazole, drawArea);


;; (defn render-to-graphics [width height ]
;;   (let [^Graphics2D g (image) ]
;;     (doto g
;;       (. getGraphics)
;;       (.setColor (. Color WHITE))
;;       (.fillRect 0 0 width height))))


;; ; paint the background
;; Graphics2D g2 = (Graphics2D)image.getGraphics();
;; g2.setColor(Color.WHITE);
;; g2.fillRect(0, 0, WIDTH, HEIGHT);

;; (defn- drawGraphics [^Graphics2D g w h ]
;;   (doto g
;;     (. getGraphics)
;;     (.setColor (. Color WHITE))
;;     (.fillRect 0 0 w h)))


;; (drawGraphics g width height)

;; ;the paint method also needs a toolkit-specific renderer
;; renderer.paint(triazole, new AWTDrawVisitor(g2));

;; ImageIO.write((RenderedImage)image, "PNG", new File("triazole.png"));



;;        ;the draw area and the image should be the same size
;;        Rectangle drawArea = new Rectangle(WIDTH, HEIGHT);
;;        Image image = new BufferedImage(
;;                WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

;;        ;any molecule will do
;;        IMolecule triazole = MoleculeFactory.make123Triazole();
;;        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
;;        sdg.setMolecule(triazole);
;;        try {
;;            sdg.generateCoordinates();
;;        } catch (Exception e) { }
;;        triazole = sdg.getMolecule();


;;        ; generators make the image elements
;;        List<IGenerator> generators = new ArrayList<IGenerator>();
;;        generators.add(new BasicSceneGenerator());
;;        generators.add(new BasicBondGenerator());
;;        generators.add(new BasicAtomGenerator());

;;        ;the renderer needs to have a toolkit-specific font manager
;;        Renderer renderer = new Renderer(generators, new AWTFontManager());

;;        ;the call to 'setup' only needs to be done on the first paint
;;        renderer.setup(triazole, drawArea);

;;        ;paint the background
;;        Graphics2D g2 = (Graphics2D)image.getGraphics();
;;        g2.setColor(Color.WHITE);
;;        g2.fillRect(0, 0, WIDTH, HEIGHT);

;;        ;;the paint method also needs a toolkit-specific renderer
;;        renderer.paintMolecule(triazole, new AWTDrawVisitor(g2));

;;        ImageIO.write((RenderedImage)image, "PNG", new File("triazole.png"));

