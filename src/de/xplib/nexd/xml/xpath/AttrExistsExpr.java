/* <blockquote><small> Copyright (C) 2002 Hewlett-Packard Company.
 * This file is part of Sparta, an XML Parser, DOM, and XPath library.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the <a href="doc-files/LGPL.txt">GNU
 * Lesser General Public License</a> as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option)
 * any later version.  This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. </small></blockquote>
 * 
 * @author Eamonn O'Brien-Strain
 */
package de.xplib.nexd.xml.xpath;

/**
 * A '@attrName' test to see if attribute exists.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class AttrExistsExpr extends AttrExpr {

    /**
     * @param attrNameIn ---
     */
    AttrExistsExpr(final String attrNameIn) {
        super(attrNameIn);
    }

    /**
     * <Some description here>
     * 
     * @param visitor --
     * @throws XPathException --
     * @see de.xplib.nexd.xml.xpath.BooleanExpr#accept(
     * 		de.xplib.nexd.xml.xpath.BooleanExprVisitor)
     */
    public final void accept(final BooleanExprVisitor visitor) 
    		throws XPathException {
        visitor.visit(this);
    }

    /**
     * <Some description here>
     * 
     * @return --
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return "[@" + this.getAttrName() + "]";
    }
}

// $Log: AttrExistsExpr.java,v $
// Revision 1.3 2002/12/13 18:08:32 eobrain
// Factor Visitor out into separate visitors for node tests and predicates.
//
// Revision 1.2 2002/12/06 23:41:49 eobrain
// Add toString() which returns the original XPath.
//
// Revision 1.1.1.1 2002/08/19 05:04:05 eobrain
// import from HP Labs internal CVS
//
// Revision 1.2 2002/08/18 23:38:08 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.1 2002/02/01 02:07:46 eob
// initial
