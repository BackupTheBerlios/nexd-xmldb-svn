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
 * A '@attrName < "n"' test.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public class AttrLessExpr extends AttrRelationalExpr {

    /**
     * Constructor for AttrLessThanExpr.
     * 
     * @param attrNameIn --
     * @param attrValueIn --
     */
    public AttrLessExpr(final String attrNameIn, final int attrValueIn) {
        super(attrNameIn, attrValueIn);
    }

    /**
     * <Some description here>
     * 
     * @param visitor --
     * @throws XPathException --
     * @see de.xplib.nexd.xml.xpath.BooleanExpr#accept(
     * 		de.xplib.nexd.xml.xpath.BooleanExprVisitor)
     */
    public final void accept(final BooleanExprVisitor visitor) 
    		throws XPathException {
        visitor.visit(this);
    }

    /**
     * <Some description here>
     * 
     * @return < 
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return toString("<");
    }
}
