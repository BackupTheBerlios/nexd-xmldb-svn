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

import de.xplib.nexd.xml.Sparta;

/**
 * A node test for an element with a particular tagname.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class ElementTest extends NodeTest {

    /**
     *  
     */
    private final String tagName;

    /**
     * 
     * @param tagNameIn --
     */
    ElementTest(final String tagNameIn) {
        this.tagName = Sparta.intern(tagNameIn);
    }

    /**
     * <Some description here>
     * 
     * @param visitor
     *            ---
     * @see de.xplib.nexd.xml.xpath.NodeTest#accept(
     *      de.xplib.nexd.xml.xpath.Visitor)
     */
    public final void accept(final Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * 
     * @return tag as interned string.
     */
    public final String getTagName() {
        return this.tagName;
    }

    /**
     * <Some description here>
     * 
     * @return ---
     * @see de.xplib.nexd.xml.xpath.NodeTest#isStringValue()
     */
    public final boolean isStringValue() {
        return false;
    }

    /**
     * <Some description here>
     * 
     * @return ---
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return this.tagName;
    }
}

// $Log: ElementTest.java,v $
// Revision 1.5 2003/07/18 00:01:43 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.4 2003/05/12 20:54:55 eobrain
// Fix javadoc.
//
// Revision 1.3 2003/05/12 20:07:19 eobrain
// Performance improvement: intern strings.
//
// Revision 1.2 2002/12/06 23:41:49 eobrain
// Add toString() which returns the original XPath.
//
// Revision 1.1.1.1 2002/08/19 05:04:04 eobrain
// import from HP Labs internal CVS
//
// Revision 1.3 2002/08/18 23:38:36 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.2 2002/06/14 19:39:52 eob
// Make test for isStringValue more object-oriented. Avoid "instanceof".
//
// Revision 1.1 2002/02/01 01:43:54 eob
// initial
