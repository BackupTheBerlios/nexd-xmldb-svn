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
 * The test for a nodeset that appears before the optional [predicate] in an
 * xpath step.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public abstract class NodeTest {

    /**
     * @param visitor ---
     * @throws XPathException ---
     */
    public abstract void accept(Visitor visitor) throws XPathException;

    /**
     * Does this nodetest evaluate to a string values (attribute values or
     * text() nodes)
     * 
     * @return ---
     */
    public abstract boolean isStringValue();

}

// $Log: NodeTest.java,v $
// Revision 1.1.1.1 2002/08/19 05:04:04 eobrain
// import from HP Labs internal CVS
//
// Revision 1.3 2002/08/18 23:38:46 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.2 2002/06/14 19:40:19 eob
// Make test for isStringValue more object-oriented. Avoid "instanceof".
//
// Revision 1.1 2002/02/01 01:25:00 eob
// initial
