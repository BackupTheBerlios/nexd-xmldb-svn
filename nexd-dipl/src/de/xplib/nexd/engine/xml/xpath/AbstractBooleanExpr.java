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
 * an expression that returns a boolean value.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public abstract class AbstractBooleanExpr {
    
    /**
     * @param visitor ---
     * @throws XPathException TODO
     */
    public abstract void accept(BooleanExprVisitor visitor)
    throws XPathException;
    
}

// $Log: AbstractBooleanExpr.java,v $
// Revision 1.3  2005/05/11 18:00:11  nexd
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
// Revision 1.2 2002/12/13 18:08:33 eobrain
// Factor Visitor out into separate visitors for node tests and predicates.
//
// Revision 1.1.1.1 2002/08/19 05:04:05 eobrain
// import from HP Labs internal CVS
//
// Revision 1.3 2002/08/19 00:41:46 eob
// Tweak javadoc comment -- add period (full stop) so that Javadoc knows
// where is end of summary.
//
// Revision 1.2 2002/08/18 23:38:30 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.1 2002/02/01 01:17:38 eob
// initial
