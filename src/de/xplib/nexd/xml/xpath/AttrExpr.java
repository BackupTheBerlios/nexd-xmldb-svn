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
 * A boolean test on an attribute.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public abstract class AttrExpr extends BooleanExpr {

    /**
     * Comment for <code>attrName</code>
     */
    private final String attrName;
    
    /**
     * @param attrNameIn --
     */
    AttrExpr(final String attrNameIn) {
        attrName = attrNameIn;
    }

    /**
     * @return --
     */
    public final String getAttrName() {
        return attrName;
    }

    /**
     * <Some description here>
     * 
     * @return @ + attrName
     * @see java.lang.Object#toString()
     */
    public abstract String toString();
}

// $Log: AttrExpr.java,v $
// Revision 1.2 2002/12/06 23:41:49 eobrain
// Add toString() which returns the original XPath.
//
// Revision 1.1.1.1 2002/08/19 05:04:05 eobrain
// import from HP Labs internal CVS
//
// Revision 1.2 2002/08/18 23:38:13 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.1 2002/02/01 02:50:47 eob
// initial
