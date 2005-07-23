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
 * Test position within element list.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */

public class PositionEqualsExpr extends AbstractBooleanExpr {

    /**
     * Comment for <code>position</code>
     */
    private final int position;

    /**
     * @param positionIn ---
     */
    public PositionEqualsExpr(final int positionIn) {
        super();
        
        this.position = positionIn;
    }

    /**
     * <Some description here>
     * 
     * @param visitor ---
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.AbstractBooleanExpr#accept(
     * 		de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor)
     */
    public final void accept(final BooleanExprVisitor visitor) 
    		throws XPathException {
        visitor.visit(this);
    }

    /**
     * @return ---
     */
    public final int getPosition() {
        return position;
    }

    /**
     * <Some description here>
     * 
     * @return ---
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return "[" + position + "]";
    }
}

// $Log: PositionEqualsExpr.java,v $
// Revision 1.3  2005/05/11 18:00:12  nexd
// Minor changes and corrections.
//
// Revision 1.2  2005/05/11 17:31:39  nexd
// Refactoring and extended test cases
//
// Revision 1.1  2005/05/08 11:59:31  nexd
// restructuring
//
// Revision 1.1  2004/12/17 17:13:59  nexd
// Initial checkin
//
// Revision 1.3 2002/12/13 18:08:30 eobrain
// Factor Visitor out into separate visitors for node tests and predicates.
//
// Revision 1.2 2002/12/06 23:41:49 eobrain
// Add toString() which returns the original XPath.
//
// Revision 1.1 2002/09/18 05:22:06 eobrain
// Support xpath predicates of the for [1], [2], ...
//
