(ns cdk-clojure.rguha
  (:use [clojure.java.javadoc])
  (:require [clojure.string :as string]
             )
  (:import [org.openscience.cdk.inchi InChIGeneratorFactory]
           [org.openscience.cdk.layout StructureDiagramGenerator]
           [org.openscience.cdk.layout StructureDiagramGenerator]

           [org.openscience.cdk.smiles SmilesParser SmilesGenerator]
           [org.openscience.cdk.interfaces IAtomContainer]
           [org.openscience.cdk.interfaces IChemObjectBuilder]
           [org.openscience.cdk.silent SilentChemObjectBuilder]
           [org.openscience.cdk DefaultChemObjectBuilder]

           ; swing components
           [javax.swing JButton JFrame JLabel JList JPanel JScrollPane]
           [java.util List]
           [java.awt]
           [java.awt.iamge]
           [java.imageio]
           ))

;(javadoc JButton)

;; import org.openscience.cdk.silent.*;
;; import org.openscience.cdk.interfaces.*;
;; import org.openscience.cdk.layout.*;
;; import org.openscience.cdk.renderer.*;
;; import org.openscience.cdk.renderer.font.*;
;; import org.openscience.cdk.renderer.generators.*;
;; import org.openscience.cdk.renderer.visitor.*;
;; import org.openscience.cdk.smiles.SmilesParser;
;; import org.openscience.cdk.templates.*;
;; import org.openscience.cdk.renderer.generators.BasicSceneGenerator.Margin;
;; import org.openscience.cdk.renderer.generators.BasicSceneGenerator.ZoomFactor;

(def smiles "CN1C=NC2=C1C(=O)N(C(=O)N2C)C")
(def width 200)
(def height 250)


Rectangle drawArea = new Rectangle(WIDTH, HEIGHT);
Image image = new BufferedImage(
  WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB
);
smilesParser = new SmilesParser(
  SilentChemObjectBuilder.getInstance()
)
molecule = smilesParser.parseSmiles(smiles)
StructureDiagramGenerator sdg =
  new StructureDiagramGenerator();
sdg.setMolecule(molecule);
sdg.generateCoordinates();
molecule = sdg.getMolecule();
List generators =
  new ArrayList();
generators.add(new BasicSceneGenerator());
generators.add(new BasicBondGenerator());
generators.add(new BasicAtomGenerator());
AtomContainerRenderer renderer =
  new AtomContainerRenderer(
    generators, new AWTFontManager()
  );
renderer.setup(molecule, drawArea);
model = renderer.getRenderer2DModel();
model.set(ZoomFactor.class, (double)0.9);
Graphics2D g2 = (Graphics2D)image.getGraphics();
g2.setColor(Color.WHITE);
g2.fillRect(0, 0, WIDTH, HEIGHT);
renderer.paint(molecule, new AWTDrawVisitor(g2));
ImageIO.write(
  (RenderedImage)image, "PNG",
  new File("CTR2.png")
);



(defn parse-smiles [smi]
    (let [ builder (SilentChemObjectBuilder/getInstance)
           sp (SmilesParser. builder)
           sg (SmilesGenerator.)
           igf (InChIGeneratorFactory/getInstance)
           mol (-> sp (.parseSmiles smi))
           ]
      {:smiles (-> sg (.create mol ))
       :inchi  (-> igf  (.getInChIGenerator mol) (.getInchi))}))



public class SimpleDepiction {

    public static void main(String[] args) throws Exception {
        SmilesParser smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        String smiles = "c1cc(CC=CC#N)ccn1";
        IAtomContainer molecule = smilesParser.parseSmiles(smiles);

        molecule = Misc.get2DCoords(molecule);
        Renderer2DPanel rendererPanel = new Renderer2DPanel(molecule, 200, 200);
        rendererPanel.setName("rendererPanel");
        JFrame frame = new JFrame("2D Structure Viewer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(rendererPanel);
        frame.setSize(200, 200);
        frame.setVisible(true);
    }
}

