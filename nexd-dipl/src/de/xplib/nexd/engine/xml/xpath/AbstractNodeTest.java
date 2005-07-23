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
package de.xplib.nexd.engine.xml.xpath;

import de.xplib.nexd.xml.xpath.XPathException;


/**
 * The test for a nodeset that appears before the optional [predicate] in an
 * xpath step.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public abstract class AbstractNodeTest {

    /**
     * @param visitor ---
     * @throws XPathException If the given <code>XPathI</code> is not valid.
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

// $Log: AbstractNodeTest.java,v $
// Revision 1.3  2005/05/11 18:00:12  nexd
// Minor changes and corrections.
//
// Revision 1.2  2005/05/11 17:31:39  nexd
// Refactoring and extended test cases
//
// Revision 1.1  2005/05/08 11:59:31  nexd
// restructuring
//
// Revision 1.1  2004/12/17 17:13:58  nexd
// Initial checkin
//
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
