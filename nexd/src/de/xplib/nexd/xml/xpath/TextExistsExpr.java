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
 * [text()] expression This is part of the GoF Flyweight(195) pattern -- Only
 * one object of this class ever exists, shared amongst all clients. You use
 * INSTANCE instead of the constructor to get that object.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public final class TextExistsExpr extends BooleanExpr {

    /**
     * Comment for <code>INSTANCE</code>
     */
    static final TextExistsExpr INSTANCE = new TextExistsExpr();

    /**
     * only need one of them => much memory sharing
     */
    private TextExistsExpr() {
    }

    /**
     * <Some description here>
     * 
     * @param visitor
     * @throws XPathException
     * @see de.xplib.nexd.xml.xpath.BooleanExpr#accept(
     * 		de.xplib.nexd.xml.xpath.BooleanExprVisitor)
     */
    public void accept(final BooleanExprVisitor visitor) throws XPathException {
        visitor.visit(this);
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "[text()]";
    }

}

//$Log: TextExistsExpr.java,v $
//Revision 1.4 2002/12/13 22:42:22 eobrain
//Fix javadoc.
//
//Revision 1.3 2002/12/13 18:08:44 eobrain
//Factor Visitor out into separate visitors for node tests and predicates.
//
//Revision 1.2 2002/12/06 23:41:49 eobrain
//Add toString() which returns the original XPath.
//
//Revision 1.1 2002/10/30 16:25:20 eobrain
//Feature request [ 630127 ] Support /a/b[text()='foo']
//http://sourceforge.net/projects/sparta-xml/
//
