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
 * The boolean constant "true". 
 * This is part of the GoF Flyweight(195) pattern --
 * Only one object of this class ever exists, shared amongst all clients. You
 * use INSTANCE instead of the constructor to get that object.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public final class TrueExpr extends BooleanExpr {

    /**
     *  
     */
    static final TrueExpr INSTANCE = new TrueExpr();

    /**
     * only need one of them => much memory sharing
     */
    private TrueExpr() {
    }

    /**
     * <Some description here>
     * 
     * @param visitor
     *            ---
     * @see de.xplib.nexd.xml.xpath.BooleanExpr#accept(
     *      de.xplib.nexd.xml.xpath.BooleanExprVisitor)
     */
    public void accept(final BooleanExprVisitor visitor) {
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
