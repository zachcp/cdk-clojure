;https://github.com/JChemPaint/jchempaint/blob/b0b34cb965c80eaecc75d5ba5f18f1f7cb81d42f/src/main/org/openscience/jchempaint/renderer/visitor/SVGGenerator.java

(ns cdk-clojure.svg
  (:use [analemma.xml])
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
           [java.awt.geom  AffineTransform NoninvertibleTransformException Rectangle2D]
           [java.awt.image BufferedImage]
           [java.util ArrayList HashMap List]
           [javax.vecmath Point2d Vector2d Vector4d]
           [javax.imageio ImageIO]
           [hiccup.core]
           [analemma.svg]
           [org.openscience.jchempaint.renderer.visitor SVGGenerator]))

;(use 'analemma.xml)

; convert to allow conversion directl
;import java.awt.geom.Rectangle2D.Double;

;; /**
;;  * We can only guarantee the same quality of SVG output everywhere
;;  * by drawing paths and not using fonts. This is an indirect
;;  * consequence of font commercialisation which has successfully
;;  * prevented SVG fonts from becoming usable on all browsers. See
;;  * https://github.com/JChemPaint/jchempaint/wiki/The-svg-font-problem-and-its-solution
;;  *
;;  * So, we convert an open font to SVG paths and use these.
;;  * To resolve the problem of placement, we use bbox, advance and
;;  * (maybe later) kerning values from the same font.
;;  * To make sure bonds don't cross text, we use two passes where
;;  * text is drawn first and the bonds second.
;;  *
;;  * Two-pass implementation (c) 2012 by
;;  * @author Ralf Stephan <ralf@ark.in-berlin.de>
;;  * @jcp.issue #2
;;  *
;;  * First code layer (c) 2007 by
;;  * @author maclean
;;  * @cdk.module rendersvg
;;  * @cdk.bug 2403250
;;  */


; SVG Helper Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; basic funcitons from https://github.com/liebke/analemma/blob/master/src/analemma/svg.clj

(defn svg [& content]
  (let [xmlns {"xmlns:svg" "http://www.w3.org/2000/svg"
	       "xmlns" "http://www.w3.org/2000/svg"
	       "xmlns:xlink" "http://www.w3.org/1999/xlink"
	       "version" "1.0"}
	  attrs (if (map? (first content)) (first content) {})
	  content (if (map? (first content)) (rest content) content)]
    (concat [:svg (merge xmlns attrs)] content)))

(defn line [x1 y1 x2 y2 & options]
  (let [attrs (apply hash-map options)]
    [:line (apply merge {:x1 x1, :y1 y1, :x2 x2, :y2 y2} attrs)]))

(defn rect [x y height width & options]
  (let [attrs (apply hash-map options)]
    [:rect (apply merge {:x x, :y y, :height height, :width width} attrs)]))

(defn circle [cx cy r & options]
  (let [attrs (apply hash-map options)]
    [:circle (apply merge {:cx cx, :cy cy, :r r} attrs)]))

(defn ellipse [cx cy rx ry & options]
  (let [attrs (apply hash-map options)]
    [:ellipse (apply merge {:cx cx, :cy cy, :rx rx, :ry ry} attrs)]))

(defn polygon [[& points] & options]
  (let [attrs (apply hash-map options)
	points (reduce (fn [s [x y]] (str s " " x "," y))
		       "" (partition 2 points))]
    [:polygon (apply merge {:points points}
		     attrs)]))

(defn tref [id]
  [:tref {"xlink:href" (str "#" (name id))}])

(defn text [ & content]
  (concat [:text]  content))

(defn group [& content]
  (cons :g content))


(defn makepolygon [x1 y1 fs & options]
  ;take and x and y value as well as the fontsize and return a polygon to
  ; be used as a backstop behing the letters
  (let [ dlarge (/ fs 2)
         dsmall (/ (/ fs 2) 1.5)
         ;topleft, topright, left top......
         tlx (- x1 dsmall)  tly (- y1 dlarge)
         trx (+ x1 dsmall)  try (- y1 dlarge)
         rtx (+ x1 dlarge)  rty (- y1 dsmall)
         rbx (+ x1 dlarge)  rby (+ y1 dsmall)
         brx (+ x1 dsmall)  bry (+ y1 dlarge)
         blx (- x1 dsmall)  bly (+ y1 dlarge)
         lbx (- x1 dlarge)  lby (+ y1 dsmall)
         ltx (- x1 dlarge)  lty (- y1 dsmall) ]
    (polygon [tlx,tly trx,try rtx,rty rbx,rby brx,bry blx,bly lbx,lby ltx,lty ] :stroke 0 :fill "white")))

(defn drawbond [bond]
  (let [x1 (:x1 bond)  y1 (:y1 bond)
        x2 (:x2 bond)  y2 (:y2 bond) ]
    (line x1 y1 x2 y2 :stroke "#006600" :stroke-width 3 )))

(defn drawatom [at]
  (let [element (:element at)
        x       (:x at)
        y       (:y at)]
  (group
    (makepolygon x y 10)
    (text {:x x :y y} (str element)))))

(defn drawmol [mol height width]
  (let [bonds (:bonds mol)
        atoms (:atoms mol)
        lines (map drawbond bonds)
        drawnatoms (map drawatom atoms)]
     (svg {:height height :width width}
          (apply group lines)
          (apply group drawnatoms))))


;Convert IAtomContainers to Cljure DataStrutures
(defrecord Atom [element x y] )
(defrecord Bond [order x1 y1 x2 y2])
(defrecord Molecule [ atoms bonds])

(defn ->Atom [iatom]
   ;Iatom to Atom
   (let [element (.getSymbol iatom)
         coords   (.getPoint2d iatom)
         x (.x coords)
         y (.y coords) ]
    (Atom. element x y)))

(defn ->Bond [ibond]
   ;Iatom to Atom
   (let [order (keyword (str (.getOrder ibond)))
         a1 (.getPoint2d (first  (.atoms ibond)))
         a2 (.getPoint2d (second (.atoms ibond)))
         x1 (.x a1)
         y1 (.y a1)
         x2 (.x a2)
         y2 (.y a2) ]
    (Bond. order x1 y1 x2 y2)))

(defn ->Mol [iatomcontainer]
  (let [ atoms (map ->Atom (.atoms iatomcontainer))
         bonds (map ->Bond (.bonds iatomcontainer))]
         (Molecule. atoms bonds)))



;; Make and Draw a Molecule
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn parsesmiles [smiles]
  ; return an IAtomContainer from smiles input
  (let [builder (SilentChemObjectBuilder/getInstance)
        sp      (SmilesParser. builder)]
       (.parseSmiles sp smiles)))


(defn get2D [mol]
  (let [ ;; Generate Coordinates and Structure Information for the Molecule
         sdg   (doto (new StructureDiagramGenerator)
                     (.setMolecule mol)
                     (.generateCoordinates))
         mol2d (.getMolecule sdg)]
         mol2d))

(def caffeine2D (-> (parsesmiles "CN1C=NC2=C1C(=O)N(C(=O)N2C)C")
                     (get2D)
                     (->Mol)))


(defn update-vals [map vals f]
  (reduce #(update-in % [%2] f) map vals))

(defn scale [scalefactor Mol ]
     (let [mult  (fn [x] (* scalefactor x))
           atoms (.atoms Mol)
           bonds (.bonds Mol)
           updatedatoms (map #(update-vals % [:x :y]  mult) atoms)
           updatedbonds (map #(update-vals % [:x1 :y1 :x2 :y2] mult) bonds)]
       (Molecule. updatedatoms updatedbonds )))

(defn translate-x [amount Mol ]
     (let [move  (fn [x] (+ amount x))
           atoms (.atoms Mol)
           bonds (.bonds Mol)
           updatedatoms (map #(update-in % [:x] move) atoms)
           updatedbonds (map #(update-vals % [:x1 :x2] move) bonds)]
       (Molecule. updatedatoms updatedbonds)))

(defn translate-y [amount Mol ]
     (let [move  (fn [x] (+ amount x))
           atoms (.atoms Mol)
           bonds (.bonds Mol)
           updatedatoms (map #(update-in % [:y] move) atoms)
           updatedbonds (map #(update-vals % [:y1 :y2] move) bonds)]
       (Molecule. updatedatoms updatedbonds)))


(spit "test.html" (str (emit( drawmol
                        (->> caffeine2D
                             (translate-x 8)
                             (translate-y 5)
                             (scale 40))
                         400
                         400))))



;; to do: Defs are text as path elements  -not sure I want to do that yet



;; 		svg.append(SVGGenerator.HEADER);
;; 		newline();
;; 		svg.append("<defs>");
;; 		tgpadding = 4;
;; 		vbpadding = 40;
;; 		trscale = 0.03;
;; 		subscale = trscale*0.7;
;; 		subshift = 0.5;
;; 	}

;; 	private void writeDEFS (TextGroupElement e) {
;; 		if (e.text.length()>1 && !tgMap.containsKey(e.text))
;; 			tgMap.put(e.text, new Point2d(0,0));
;; 		for (char c : e.text.toCharArray()) {
;; 			String idstr = "Atom-" + c;
;; 			GlyphMetrics m = the_fm.map.get((int) c);
;; 			if (!ptMap.containsKey((int) c))
;; 				ptMap.put((int) c, new Point2d(m.xMax, m.yMax - m.yMin));
;; 			if(!tcList.contains(idstr)) {
;; 				tcList.add(idstr);
;; 				newline();
;; 				svg.append (String.format(
;; 						"  <path id=\"%s\" transform=\"scale(%1.3f,%1.3f)\" d=\"%s\" />",
;; 						idstr, trscale, -trscale, m.outline));
;; 			}
;; 		}

;; 		// Set hyd and hPos according to entry
;; 		int hyd=0, hPos=0;
;; 		for (TextGroupElement.Child ch : e.children) {
;; 			if (ch.text.equals ("H")) {
;; 				if (ch.subscript == null) hyd=1;
;; 				else if (ch.subscript.equals("2")) hyd=2;
;; 				else hyd=3;
;; 				if (ch.position==TextGroupElement.Position.E) hPos=1;
;; 				else if (ch.position==TextGroupElement.Position.W) hPos=-1;
;; 			}
;; 		}
;; 		if (hyd>0) {
;; 			if (!tcList.contains("Atom-H")) {
;; 				tcList.add("Atom-H");
;; 				GlyphMetrics m = the_fm.map.get((int) "H".charAt(0));
;; 				svg.append (String.format(
;; 						"  <path id=\"Atom-H\" transform=\"scale(%1.3f,%1.3f)\" d=\"%s\" />",
;; 						trscale, -trscale, m.outline));
;; 			}
;; 			if (hyd>=2) {
;; 				char c = '2';
;; 				if (hyd==3) c='3';
;; 				String idstr = "Atom-" + c;
;; 				GlyphMetrics m = the_fm.map.get((int) c);
;; 				if(!tcList.contains(idstr)) {
;; 					tcList.add(idstr);
;; 					newline();
;; 					svg.append (String.format(
;; 							"  <path id=\"%s\" transform=\"scale(%1.4f,%1.4f)\" d=\"%s\" />",
;; 							idstr, subscale, -subscale, m.outline));
;; 				}
;; 			}
;; 		}
;; 	}



; Ellipse elements to co behind text

	public void draw (OvalElement oval) {
		newline();
		double[] p1 = transformPoint(oval.x - oval.radius, oval.y - oval.radius);
		double[] p2 = transformPoint(oval.x + oval.radius, oval.y + oval.radius);
		double x, y, w, h;
		x = Math.min(p1[0], p2[0]);
		y = Math.min(p1[1], p2[1]);
		w = Math.abs(p2[0] - p1[0]);
		h = Math.abs(p2[1] - p1[1]);
		Rectangle2D rect = new Rectangle2D.Double(x, y, w, h);
		if (bbox==null) bbox=rect;
		else bbox = bbox.createUnion(rect);
		double r = w / 2;
		svg.append(String.format(
				"<ellipse cx=\"%4.2f\" cy=\"%4.2f\" rx=\"%4.2f\" ry=\"%4.2f\" " +
				"style=\"stroke:black; stroke-width:1px; fill:none;\" />",
				x + r, y + r, r, r));
	}

; Atom elements
  public void draw (AtomSymbolElement atomSymbol) {
		newline();
		double[] p = transformPoint(atomSymbol.x, atomSymbol.y);
		svg.append(String.format(
				"<text x=\"%s\" y=\"%s\" style=\"fill:%s\"" +
				">%s</text>",
				p[0],
				p[1],
				toColorString(atomSymbol.color),
				atomSymbol.text
				));
	}




  /**
	 * At the time of this call, all that we need is in place:
	 * the SVG character macros are written and the bboxes computed.
	 * The textgroup text is now placed with its center at the
	 * position of the atom. Implicit hydrogens are added.
	 *
	 * @param e
	 */
	public void draw (TextGroupElement e) {
		newline();
		double[] pos = transformPoint(e.x, e.y);

		// Determine the bbox of the Atom symbol text
		Point2d bb;
		if (e.text.length() == 1)
			bb = ptMap.get((int)e.text.charAt(0));
		else
			bb = tgMap.get(e.text);

		// Set hyd and hPos according to entry
		int hyd=0, hPos=0;
		for (TextGroupElement.Child c : e.children) {
			if (c.text.equals ("H")) {
				if (c.subscript == null) hyd=1;
				else if (c.subscript.equals("2")) hyd=2;
				else hyd=3;
				if (c.position==TextGroupElement.Position.E) hPos=1;
				else if (c.position==TextGroupElement.Position.W) hPos=-1;
			}
		}

		// Set v to the bbox of the whole TextGroup, add it to
		// the list of such bboxes and enlarge the viewport bbox
		double x, y, w, h;
		x = pos[0]-trscale*bb.x/2;
		y = pos[1]-trscale*bb.y/2-tgpadding;
		w = trscale*bb.x+tgpadding;
		h = trscale*bb.y+2*tgpadding;
		Rectangle2D v = new Rectangle2D.Double(x, y, w, h);
		bbList.add (v);
		if (bbox==null) bbox = v;
                else bbox = bbox.createUnion (v);

		// Output use command(s)
		x = pos[0] - trscale*bb.x/2;
		y = pos[1] + trscale*bb.y/2;
		svg.append(String.format(
				"<use xlink:href=\"#Atom-%s\" x=\"%4.2f\" y=\"%4.2f\"/>",
				e.text, x, y));
		if (hyd != 0) {
			GlyphMetrics m = the_fm.map.get(50+hyd);
			if (hPos>0)
				x += trscale*bb.x;
			else {
				x -= trscale* the_fm.map.get((int) "H".charAt(0)).adv;
				if (hyd>=2)
					x -= subscale*m.adv;
			}
			svg.append(String.format(
					"<use xlink:href=\"#Atom-H\" x=\"%4.2f\" y=\"%4.2f\"/>",
					x, y));
			if (hyd>=2) {
				char c = '2';
				if (hyd==3) c='3';
				String idstr = "Atom-" + c;
				x += trscale* the_fm.map.get((int) "H".charAt(0)).adv;
				y += subshift*subscale*(m.yMax-m.yMin);
				svg.append(String.format(
						"<use xlink:href=\"#%s\" x=\"%4.2f\" y=\"%4.2f\"/>",
						idstr, x, y));
			}
		}
	}



	/**
	 * In this second pass, everything except bonds (and arrows)
	 * is placed, the textgroups referring to the DEFS ids. For
	 * intermediate caching, we first add strings to the DEFS block.
	 */
	public void drawNoBonds() {
		newline();
		svg.append("</defs>");
		if (!tgMap.isEmpty()) { newline(); svg.append("<defs>"); }
		for (String s : tgMap.keySet()) {
			newline();
			svg.append(String.format("<g id=\"Atom-%s\">", s));
			boolean first = true;
			int advance = 0;
			int xMin = 9999, xMax = 0, yMin = 9999, yMax = 0;
			for (char c : s.toCharArray()) {
				svg.append(String.format("<use xlink:href=\"#Atom-%c\" ", c));
				if (first) {
					first=false;
				}
				else {
					svg.append(String.format("transform=\"translate(%4.2f,0)\"", advance*trscale));
				}
				GlyphMetrics m = the_fm.map.get((int)c);
				if (m.xMin + advance < xMin) xMin = m.xMin + advance;
				if (m.xMax + advance > xMax) xMax = m.xMax + advance;
				if (m.yMin < yMin) yMin = m.yMin;
				if (m.yMax > yMax) yMax = m.yMax;
				advance += m.adv;
				svg.append("/>");
			}
			svg.append("</g>");
			Point2d p = tgMap.get(s);
			p.x = xMax - xMin;
			p.y = yMax - yMin;
		}
		if (!tgMap.isEmpty()) { newline(); svg.append("</defs>"); }

		for (IRenderingElement element : elList) {
			if (element instanceof OvalElement)
				draw((OvalElement) element);
	        else if (element instanceof TextGroupElement)
	            draw((TextGroupElement) element);
			else if (element instanceof AtomSymbolElement)
				draw((AtomSymbolElement) element);
			else if (element instanceof TextElement)
				draw((TextElement) element);
			else if (element instanceof RectangleElement)
				draw((RectangleElement) element);
			else if (element instanceof PathElement)
				draw((PathElement) element);
		}
	}

	/**
	 * In the third pass, bonds (and arrows) are drawn,
	 * taking care to leave a small distance to atoms with
	 * text.
	 */
	public void drawBonds() {
		for (IRenderingElement element : elList) {
			if (element instanceof WedgeLineElement)
				draw((WedgeLineElement) element);
			else if (element instanceof LineElement)
				draw((LineElement) element);
	        else if (element instanceof ArrowElement)
	            draw((ArrowElement) element);
	        else if (element instanceof WigglyLineElement)
	        	draw((WigglyLineElement) element);
		}
	}

	/**
	 * This is where most of the work is done by calling
	 * the 2nd and 3rd passes, and finally computing
	 * width and height of the document which is set at last.
	 * @return the SVG document as String
	 */
	public String getResult() {
		drawNoBonds();
		drawBonds();
		newline();
		svg.append("</g>\n</svg>\n");

		int i = svg.indexOf ("0 0 1234567890");
        if (bbox == null)
            bbox = new Rectangle2D.Double(0, 0, 0, 0);
		svg.replace(i, i+14, String.format("0 0 %4.0f %4.0f",
				bbox.getWidth()+2*vbpadding,
				bbox.getHeight()+2*vbpadding));
		i = svg.indexOf ("12345,67890");
		svg.replace(i, i+11, String.format ("%4.0f,%4.0f",
				-bbox.getMinX()+vbpadding,
				-bbox.getMinY()+vbpadding));
		return svg.toString();
	}

	/**
	 * Applies all collected bboxes to the two points and, if one is
	 * inside a bbox, places it at the bbox's edge, same direction.
	 * Intended to be cumulative, i.e., both points may be moved more
	 * than once, or never. Returns true if any segment is left.
	 * @param p1
	 * @param p2
	 */
	private boolean shorten_line(double[] p1, double[] p2)
	{
		for (Rectangle2D v : bbList) {   // shorten line acc. to bboxes
			boolean inside1 = v.contains(p1[0], p1[1]);
			boolean inside2 = v.contains(p2[0], p2[1]);
			if (!inside1 && !inside2) continue;
			if (inside1 && inside2) return false;

			double px, py, qx, qy, cx=0.0, cy=0.0;
			if (inside1) {
				px = p1[0]; py = p1[1];
				qx = p2[0]; qy = p2[1];
			} else {
				px = p2[0]; py = p2[1];
				qx = p1[0]; qy = p1[1];
			}
			if (qx<v.getX() && v.getX()<px) cx = v.getX();
			if (px<v.getMaxX() && v.getMaxX()<qx) cx = v.getMaxX();
			if (qy<v.getY() && v.getY()<py) cy = v.getY();
			if (py<v.getMaxY() && v.getMaxY()<qy) cy = v.getMaxY();

                        double rx, ry;
			if (qy==py) { rx=cx; ry=py; }
			else if (cx == 0.0) { ry = cy; rx = px + (cy-py)*(qx-px)/(qy-py); }
			else if (qx==px) { ry=cy; rx=px; }
			else if (cy == 0.0) { rx = cx; ry = py + (cx-px)*(qy-py)/(qx-px); }
			else {  // cx, cy, qx-px, qy-py all nonzero
				if (Math.abs((cx-px)/(cy-py)) > Math.abs((qx-px)/(qy-py)))
				{ ry = cy; rx = px + (cy-py)*(qx-px)/(qy-py); }
				else
				{ rx = cx; ry = py + (cx-px)*(qy-py)/(qx-px); }
			}
			if (inside1) {
				p1[0] = (int)rx; p1[1] = (int)ry;
			} else {
				p2[0] = (int)rx; p2[1] = (int)ry;
			}
		}
		return true;
	}

	public void draw (WedgeLineElement wedge) {
		double[] p1 = transformPoint(wedge.x1, wedge.y1);
		double[] p2 = transformPoint(wedge.x2, wedge.y2);
		if (bbox==null) bbox = new Rectangle2D.Double(
                            Math.min(p1[0], p2[0]), Math.min(p1[1], p2[1]),
                            Math.abs(p2[0] - p1[0]), Math.abs(p2[1] - p1[1]));
                else bbox = bbox.createUnion(new Rectangle2D.Double(
                        Math.min(p1[0], p2[0]), Math.min(p1[1], p2[1]),
                        Math.abs(p2[0] - p1[0]), Math.abs(p2[1] - p1[1])));
		if (!shorten_line (p1, p2)) return;
		double w1[] = invTransformPoint (p1[0], p1[1]);
		double w2[] = invTransformPoint (p2[0], p2[1]);
        // make the vector normal to the wedge axis
        Vector2d normal =
            new Vector2d(w1[1] - w2[1], w2[0] - w1[0]);
        normal.normalize();
        normal.scale(rendererModel.getWedgeWidth() / rendererModel.getScale());

        // make the triangle corners
        Point2d vertexA = new Point2d(w1[0], w1[1]);
        Point2d vertexB = new Point2d(w2[0], w2[1]);
        Point2d vertexC = new Point2d(vertexB);
        vertexB.add(normal);
        vertexC.sub(normal);
        if (wedge.wedgeType==0) {
            this.drawDashedWedge(vertexA, vertexB, vertexC);
        } else if (wedge.wedgeType==1) {
            this.drawFilledWedge(vertexA, vertexB, vertexC);
        } else {
        	this.drawCrissCrossWedge(vertexA, vertexB, vertexC);
        }
	}

	    public void draw (WigglyLineElement wedge) {
		    	//TODO add code. see http://www.w3.org/TR/SVG/paths.html#PathDataCurveCommands
		    }

	    private void drawCrissCrossWedge(Point2d vertexA, Point2d vertexB,
						Point2d vertexC) {

			        // calculate the distances between lines
			        double distance = vertexB.distance(vertexA);
			        double gapFactor = 0.1;
			        double gap = distance * gapFactor;
			        double numberOfDashes = distance / gap;
			        double d = 0;
			        double[] old=null;

			        // draw by interpolating along the edges of the triangle
			        for (int i = 0; i < numberOfDashes; i++) {
			            double d2 = d-gapFactor;
			            Point2d p1 = new Point2d();
			            p1.interpolate(vertexA, vertexB, d);
			            Point2d p2 = new Point2d();
			            p2.interpolate(vertexA, vertexC, d2);
			            double[] p1T = this.transformPoint(p1.x, p1.y);
			            double[] p2T = this.transformPoint(p2.x, p2.y);
			    		svg.append(String.format(
								"<line x1=\"%4.2f\" y1=\"%4.2f\" x2=\"%4.2f\" y2=\"%4.2f\" " +
								"style=\"stroke:black; stroke-width:1px;\" />",
								p1T[0],
								p1T[1],
								p2T[0],
								p2T[1]
								));
			            if(old==null)
			            	old = p2T;
			    		svg.append(String.format(
								"<line x1=\"%4.2f\" y1=\"%4.2f\" x2=\"%4.2f\" y2=\"%4.2f\" " +
								"style=\"stroke:black; stroke-width:1px;\" />",
								old[0],
								old[1],
								p2T[0],
								p2T[1]
								));
			            old = p1T;
			            if (distance * (d + gapFactor) >= distance) {
			                break;
			            } else {
			                d += gapFactor*2;
			            }
			        }
				}


    private void drawFilledWedge(
            Point2d vertexA, Point2d vertexB, Point2d vertexC) {
        double[] pB = this.transformPoint(vertexB.x, vertexB.y);
        double[] pC = this.transformPoint(vertexC.x, vertexC.y);
        double[] pA = this.transformPoint(vertexA.x, vertexA.y);

		svg.append(String.format(
				"<polygon points=\"%4.2f,%4.2f %4.2f,%4.2f %4.2f,%4.2f\" "+
					"style=\"fill:black;"+
					"stroke:black;stroke-width:1\"/>",
				pB[0],pB[1],
				pC[0],pC[1],
				pA[0],pA[1]
				));
    }

	public void draw (PathElement path) {

	}


	public void draw (LineElement line) {
		newline();
		double[] p1 = transformPoint(line.x1, line.y1);
		double[] p2 = transformPoint(line.x2, line.y2);
		if (!shorten_line (p1, p2)) return;
                if (bbox == null) {
                    bbox = new Rectangle2D.Double(
                            Math.min(p1[0], p2[0]), Math.min(p1[1], p2[1]),
                            Math.abs(p2[0] - p1[0]), Math.abs(p2[1] - p1[1]));
                } else {
                    bbox = bbox.createUnion(new Rectangle2D.Double(
                            Math.min(p1[0], p2[0]), Math.min(p1[1], p2[1]),
                            Math.abs(p2[0] - p1[0]), Math.abs(p2[1] - p1[1])));
                }
    		svg.append(String.format(
				"<line x1=\"%4.2f\" y1=\"%4.2f\" x2=\"%4.2f\" y2=\"%4.2f\" " +
				"style=\"stroke:black; stroke-width:3px;\" />",
				p1[0],
				p1[1],
				p2[0],
				p2[1]
				));
	}

    private void drawDashedWedge(
            Point2d vertexA, Point2d vertexB, Point2d vertexC) {

        // calculate the distances between lines
        double distance = vertexB.distance(vertexA);
        double gapFactor = 0.1;
        double gap = distance * gapFactor;
        double numberOfDashes = distance / gap;
        double d = 0;

        // draw by interpolating along the edges of the triangle
        for (int i = 0; i < numberOfDashes; i++) {
            Point2d p1 = new Point2d();
            p1.interpolate(vertexA, vertexB, d);
            Point2d p2 = new Point2d();
            p2.interpolate(vertexA, vertexC, d);
            double[] p1T = this.transformPoint(p1.x, p1.y);
            double[] p2T = this.transformPoint(p2.x, p2.y);
    		svg.append(String.format(
					"<line x1=\"%4.2f\" y1=\"%4.2f\" x2=\"%4.2f\" y2=\"%4.2f\" " +
					"style=\"stroke:black; stroke-width:1px;\" />",
					p1T[0],
					p1T[1],
					p2T[0],
					p2T[1]
					));

            if (distance * (d + gapFactor) >= distance) {
                break;
            } else {
                d += gapFactor;
            }
        }
    }

    public void draw (ArrowElement line) {

        int w = (int) (line.width * this.rendererModel.getScale());
        double[] a = this.transformPoint(line.x1, line.y1);
        double[] b = this.transformPoint(line.x2, line.y2);
        newline();
		svg.append(String.format(
				"<line x1=\"%4.2f\" y1=\"%4.2f\" x2=\"%4.2f\" y2=\"%4.2f\" " +
				"style=\"stroke:black; stroke-width:"+w+"px;\" />",
				a[0],
				a[1],
				b[0],
				b[1]
				));
        double aW = rendererModel.getArrowHeadWidth() / rendererModel.getScale();
        if(line.direction){
	        double[] c = this.transformPoint(line.x1-aW, line.y1-aW);
	        double[] d = this.transformPoint(line.x1-aW, line.y1+aW);
	        newline();
			svg.append(String.format(
					"<line x1=\"%s\" y1=\"%s\" x2=\"%s\" y2=\"%s\" " +
					"style=\"stroke:black; stroke-width:"+w+"px;\" />",
					a[0],
					a[1],
					c[0],
					c[1]
					));
			newline();
			svg.append(String.format(
					"<line x1=\"%s\" y1=\"%s\" x2=\"%s\" y2=\"%s\" " +
					"style=\"stroke:black; stroke-width:"+w+"px;\" />",
					a[0],
					a[1],
					d[0],
					d[1]
					));
        }else{
	        double[] c = this.transformPoint(line.x2+aW, line.y2-aW);
	        double[] d = this.transformPoint(line.x2+aW, line.y2+aW);
	        newline();
			svg.append(String.format(
					"<line x1=\"%s\" y1=\"%s\" x2=\"%s\" y2=\"%s\" " +
					"style=\"stroke:black; stroke-width:"+w+"px;\" />",
					a[0],
					a[1],
					c[0],
					c[1]
					));
			newline();
			svg.append(String.format(
					"<line x1=\"%s\" y1=\"%s\" x2=\"%s\" y2=\"%s\" " +
					"style=\"stroke:black; stroke-width:"+w+"px;\" />",
					a[0],
					a[1],
					d[0],
					d[1]
					));
        }
    }


	public void draw (RectangleElement rectangleElement) {
        double[] pA = this.transformPoint(rectangleElement.x, rectangleElement.y);
        double[] pB = this.transformPoint(rectangleElement.x+rectangleElement.width, rectangleElement.y);
        double[] pC = this.transformPoint(rectangleElement.x, rectangleElement.y+rectangleElement.height);
        double[] pD = this.transformPoint(rectangleElement.x+rectangleElement.width, rectangleElement.y+rectangleElement.height);

        newline();
		svg.append(String.format(
				"<polyline points=\"%4.2f,%4.2f %4.2f,%4.2f %4.2f,%4.2f %4.2f,%4.2f %4.2f,%4.2f\" "+
					"style=\"fill:none;"+
					"stroke:black;stroke-width:1\"/>",
				pA[0],pA[1],
				pB[0],pB[1],
				pD[0],pD[1],
				pC[0],pC[1],
				pA[0],pA[1]
				));

