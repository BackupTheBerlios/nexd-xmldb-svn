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
 * A node test for an element with a particular tagname.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class AttrTest extends NodeTest {

    /**
     * Comment for <code>attrName</code>
     */
    private final String attrName;

    /**
     * @param attrNameIn ---
     */
    AttrTest(final String attrNameIn) {
        attrName = attrNameIn;
    }

    /**
     * <Some description here>
     * 
     * @param visitor ---
     * @see de.xplib.nexd.xml.xpath.NodeTest#accept(
     * 		de.xplib.nexd.xml.xpath.Visitor)
     */
    public final void accept(final Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * @return attribute name
     */
    public final String getAttrName() {
        return attrName;
    }

    /**
     * <Some description here>
     * 
     * @return true
     * @see de.xplib.nexd.xml.xpath.NodeTest#isStringValue()
     */
    public final boolean isStringValue() {
        return true;
    }

    /**
     * <Some description here>
     * 
     * @return @ + attrName
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return "@" + attrName;
    }
}

// $Log: AttrTest.java,v $
// Revision 1.2 2002/12/06 23:41:49 eobrain
// Add toString() which returns the original XPath.
//
// Revision 1.1.1.1 2002/08/19 05:04:05 eobrain
// import from HP Labs internal CVS
//
// Revision 1.3 2002/08/18 23:38:24 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.2 2002/06/14 19:39:17 eob
// Make test for isStringValue more object-oriented. Avoid "instanceof".
//
// Revision 1.1 2002/02/04 21:35:35 eob
// initial
