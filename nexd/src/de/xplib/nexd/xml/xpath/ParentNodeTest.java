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
 * A '..' node test. This is part of the GoF Flyweight(195) pattern -- Only one
 * object of this class ever exists, shared amongst all clients. You use
 * INSTANCE instead of the constructor to get that object.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public final class ParentNodeTest extends NodeTest {
    
    /**
     * Comment for <code>INSTANCE</code>
     */
    static final ParentNodeTest INSTANCE = new ParentNodeTest();
    
    /**
     * only need one of them => GoF Flyweight Pattern(195)
     */
    private ParentNodeTest() {
    }
    
    /**
     * <Some description here>
     * 
     * @param visitor ---
     * @throws XPathException ---
     * @see de.xplib.nexd.xml.xpath.NodeTest#accept(
     * 		de.xplib.nexd.xml.xpath.Visitor)
     */
    public void accept(final Visitor visitor) throws XPathException {
        visitor.visit(this);
    }
    
    /**
     * <Some description here>
     * 
     * @return false
     * @see de.xplib.nexd.xml.xpath.NodeTest#isStringValue()
     */
    public boolean isStringValue() {
        return false;
    }
    
    /**
     * <Some description here>
     * 
     * @return ..
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "..";
    }
    
}

// $Log: ParentNodeTest.java,v $
// Revision 1.2 2002/12/06 23:38:05 eobrain
// Make objects that are always the same follow the Flyweight Pattern.
//
// Revision 1.1.1.1 2002/08/19 05:04:04 eobrain
// import from HP Labs internal CVS
//
// Revision 1.3 2002/08/18 23:38:54 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.2 2002/06/14 19:40:47 eob
// Make test for isStringValue more object-oriented. Avoid "instanceof".
//
// Revision 1.1 2002/02/01 02:06:17 eob
// initial
