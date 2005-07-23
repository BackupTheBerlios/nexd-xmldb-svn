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

/**
 * Compare attribute relative to integer.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public abstract class AbstractAttrRelationalExpr extends AbstractAttrExpr {

    /**
     * Comment for <code>attrValue</code>
     */
    private final double attrValue;

    /**
     * @param attrNameIn --
     * @param attrValueIn --
     */
    AbstractAttrRelationalExpr(final String attrNameIn, 
                               final double attrValueIn) {
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
     * @param expr --
     * @return --
     */
    public final String toXPath(final String expr) {
        return "[@" + this.getAttrName() + expr + "\'" + attrValue + "\']";
    }

}

// $Log: AbstractAttrRelationalExpr.java,v $
// Revision 1.2  2005/05/11 17:31:39  nexd
// Refactoring and extended test cases
//
// Revision 1.1  2005/05/08 11:59:31  nexd
// restructuring
//
// Revision 1.4  2005/04/24 15:00:26  nexd
// Bugfixes and many performance and coding improvements.
//
// Revision 1.3  2005/04/22 14:59:42  nexd
// SOAP support and performance update.
//
// Revision 1.2  2005/01/31 07:54:22  nexd
// Extended XPath support for nexd.
//
// Revision 1.1  2004/12/17 17:13:58  nexd
// Initial checkin
//
// Revision 1.1 2002/10/30 16:16:52 eobrain
// initial
//
