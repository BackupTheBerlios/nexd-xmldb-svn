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
 * Compare text value.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public abstract class AbstractTextCompareExpr extends AbstractBooleanExpr {
    
    /**
     * Comment for <code>value</code>
     */
    private final String value;
    
    /**
     * @param valueIn ---
     */
    protected AbstractTextCompareExpr(final String valueIn) {
        super();
        
        value = valueIn;
    }
    
    /**
     * @return ---
     */
    public final String getValue() {
        return value;
    }
    
    /**
     * @param opName ---
     * @return ---
     */
    public final String toString(final String opName) {
        return "[text()" + opName + "\'" + value + "\']";
    }
    
}

// $Log: AbstractTextCompareExpr.java,v $
// Revision 1.1  2005/05/08 11:59:31  nexd
// restructuring
//
// Revision 1.2  2005/04/22 14:59:42  nexd
// SOAP support and performance update.
//
// Revision 1.1  2004/12/17 17:13:58  nexd
// Initial checkin
//
// Revision 1.2 2002/12/06 23:41:49 eobrain
// Add toString() which returns the original XPath.
//
// Revision 1.1 2002/10/30 16:17:59 eobrain
// initial
//
