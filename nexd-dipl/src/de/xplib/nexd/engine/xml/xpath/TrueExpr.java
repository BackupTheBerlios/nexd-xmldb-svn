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
 * The boolean constant "true". 
 * This is part of the GoF Flyweight(195) pattern --
 * Only one object of this class ever exists, shared amongst all clients. You
 * use INSTANCE instead of the constructor to get that object.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public final class TrueExpr extends AbstractBooleanExpr {

    /**
     * @clientCardinality 1
     * @clientRole INSTANCE
     * @directed true
     * @link aggregation
     * @stereotype Flyweight
     * @supplierCardinality 1
     */
    static final TrueExpr INSTANCE = new TrueExpr();

    /**
     * only need one of them => much memory sharing
     */
    protected TrueExpr() {
        super();
    }

    /**
     * <Some description here>
     * 
     * @param visitor
     *            ---
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.AbstractBooleanExpr#accept(
     *      de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor)
     */
    public void accept(final BooleanExprVisitor visitor) throws XPathException {
        visitor.visit(this);
    }

    /**
     * <Some description here>
     * 
     * @return ---
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "";
    }

}

// $Log: TrueExpr.java,v $
// Revision 1.3  2005/05/30 19:17:08  nexd
// UML documentation update....
//
// Revision 1.2  2005/05/11 18:00:12  nexd
// Minor changes and corrections.
//
// Revision 1.1  2005/05/08 11:59:31  nexd
// restructuring
//
// Revision 1.1  2004/12/17 17:13:58  nexd
// Initial checkin
//
// Revision 1.3 2002/12/13 18:08:33 eobrain
// Factor Visitor out into separate visitors for node tests and predicates.
//
// Revision 1.2 2002/12/06 23:39:52 eobrain
// Make objects that are always the same follow the Flyweight Pattern.
//
// Revision 1.1.1.1 2002/08/19 05:04:03 eobrain
// import from HP Labs internal CVS
//
// Revision 1.3 2002/08/19 00:42:23 eob
// Tweak javadoc comment -- add period (full stop) so that Javadoc knows
// where is end of summary.
//
// Revision 1.2 2002/08/18 23:39:16 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.1 2002/02/01 01:43:31 eob
// initial
