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
 * Compare attribute relative to integer.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public abstract class AttrRelationalExpr extends AttrExpr {

    /**
     * Comment for <code>attrValue</code>
     */
    private final int attrValue;

    /**
     * @param attrNameIn --
     * @param attrValueIn --
     */
    AttrRelationalExpr(final String attrNameIn, final int attrValueIn) {
        super(attrNameIn);
        attrValue = attrValueIn;
    }

    /**
     * @return --
     */
    public final double getAttrValue() {
        return attrValue;
    }

    /**
     * @param op --
     * @return --
     */
    protected final String toString(final String op) {
        return "[@" + this.getAttrName() + op + "\'" + attrValue + "\']";
    }

}

// $Log: AttrCompareExpr.java,v $
// Revision 1.1 2002/10/30 16:16:52 eobrain
// initial
//
