(ns cdk-clojure.noel
  (:use [clojure.java.javadoc])
  (:require [clojure.string :as string])
  (:import [java.awt Rectangle Dimension ]

           [org.openscience.cdk.io MDLReader]
           [org.openscience.cdk.exception CDKException]
           [org.openscience.cdk.layout StructureDiagramGenerator]
           ;[org.openscience.cdk.applications.swing MoleculeViewer2D]
           [org.openscience.cdk.smiles SmilesParser]))


;; Clojure Implementation of http://www.redbrick.dcu.ie/~noel/CDKJython.html
;; class JmolPanel(JPanel):
;;     def __init__(self):
;;         self.adapter=CdkJmolAdapter(None)
;;         self.viewer=JmolViewer.allocateViewer(self,self.adapter)
;;         self.currentSize=Dimension()
;;         self.rectClip=Rectangle()
;;     def getViewer(self):
;;         return self.viewer
;;     def paint(self,g):
;;         self.viewer.setScreenDimension(self.getSize(self.currentSize))
;;         g.getClipBounds(self.rectClip)
;;         self.viewer.renderScreenImage(g,self.currentSize,self.rectClip)

;; class ApplicationCloser(WindowAdapter):
;;     def windowClosing(self,e):
;;         if __name__=="__main__":
;;             sys.exit(0)
;; class ViewMol3D:
;;     def __init__(self,mol=None,file=None):
;;         if file and not mol:
;;             try:
;;                 infile=open(file,"r")
;;                 mol=MDLReader(infile).read(Molecule())
;;                 infile.close()
;;             except IOError,CDKException:
;;                 print "Problem reading molecule from %s" % file
;;                 infile.close() # No finally clause in Jython (unlike CPython)
;;         if mol:
;;             self.frame=JFrame("viewmol CDK Jmol")
;;             self.frame.setSize(300,300)
;;             self.frame.addWindowListener(ApplicationCloser())
;;             contentPane=self.frame.getContentPane()
;;             jmolPanel=JmolPanel()
;;             contentPane.add(jmolPanel)
;;             self.viewer=jmolPanel.getViewer()
;;             self.viewer.openClientFile("","",mol)
;; # The previous line causes 15 "indexInt: null"s to be printed
;;             self.frame.setVisible(1)
;;     def script(self,text):
;;         self.viewer.evalString(text)
;;     def close(self):
;;         self.frame.remove()

;; class ViewMol2D:
;;     def __init__(self,mol=None,file=None):
;;         if file and not mol:
;;             try:
;;                 infile=open(file,"r")
;;                 mol=MDLReader(infile).read(Molecule())
;;                 infile.close()
;;             except IOError,CDKException:
;;                 print "Problem reading molecule from %s" % file
;;                 infile.close()

;;         if mol:
;;             sdg=StructureDiagramGenerator(mol)
;;             try:
;;                 sdg.generateCoordinates()
;;             except CDKException:
;;                 print "Strange coordinates..."
;;             else:
;;                 self.mv=MoleculeViewer2D(sdg.getMolecule())
;;                 self.mv.display()

;; if __name__=="__main__":
;;     mol=MDLReader(open("mymol.sd","r")).read(Molecule())
;;     ViewMol2D(mol)
;;     firstview=ViewMol3D(mol)
;;     secondview=ViewMol3D(file="anothermol.sd")
;;     secondview.script("spacefill on; spin")