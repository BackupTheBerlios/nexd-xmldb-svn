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
 * [text()='value'] expression
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class TextEqualsExpr extends AbstractTextCompareExpr {
    
    /**
     * @param value ---
     */
    TextEqualsExpr(final String value) {
        super(value);
    }
    
    /**
     * <Some description here>
     * 
     * @param visitor
     * @see de.xplib.nexd.engine.xml.xpath.AbstractBooleanExpr#accept(
     * 		de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor)
     */
    public final void accept(final BooleanExprVisitor visitor) 
    		throws XPathException {
        visitor.visit(this);
    }
    
    /**
     * <Some description here>
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return toString("=");
    }
    
}

// $Log: TextEqualsExpr.java,v $
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
// Revision 1.6 2002/12/13 22:42:22 eobrain
// Fix javadoc.
//
// Revision 1.5 2002/12/13 18:08:34 eobrain
// Factor Visitor out into separate visitors for node tests and predicates.
//
// Revision 1.4 2002/12/06 23:41:49 eobrain
// Add toString() which returns the original XPath.
//
// Revision 1.3 2002/10/30 16:47:00 eobrain
// Comment change only
//
// Revision 1.2 2002/10/30 16:46:20 eobrain
// Comment change only
//
// Revision 1.1 2002/10/30 16:23:42 eobrain
// Feature request [ 630127 ] Support /a/b[text()='foo']
// http://sourceforge.net/projects/sparta-xml/
