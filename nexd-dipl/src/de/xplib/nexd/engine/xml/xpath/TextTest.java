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
 * A "text()" node test in a Xpath step. This is part of the GoF Flyweight(195)
 * pattern -- Only one object of this class ever exists, shared amongst all
 * clients. You use INSTANCE instead of the constructor to get that object.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public final class TextTest extends AbstractNodeTest {

    /**
     * Comment for <code>INSTANCE</code>
     * @clientCardinality 1
     * @clientRole INSTANCE
     * @directed true
     * @link aggregation
     * @stereotype Flyweight
     * @supplierCardinality 1
     */
    static final TextTest INSTANCE = new TextTest();

    /**
     * only need one of them => GoF Flyweight Pattern(195)
     */
    protected TextTest() {
        super();
    }

    /**
     * <Some description here>
     * 
     * @param visitor
     * @throws XPathExceptionExt
     * @see de.xplib.nexd.engine.xml.xpath.AbstractNodeTest#accept(
     *      de.xplib.nexd.engine.xml.xpath.Visitor)
     */
    public void accept(final Visitor visitor) throws XPathExceptionExt {
        visitor.visit(this);
    }

    /**
     * <Some description here>
     * 
     * @return true
     * @see de.xplib.nexd.engine.xml.xpath.AbstractNodeTest#isStringValue()
     */
    public boolean isStringValue() {
        return true;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "text()";
    }
}

// $Log: TextTest.java,v $
// Revision 1.3  2005/05/30 19:17:07  nexd
// UML documentation update....
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
// Revision 1.2 2002/12/06 23:39:35 eobrain
// Make objects that are always the same follow the Flyweight Pattern.
//
// Revision 1.1.1.1 2002/08/19 05:04:03 eobrain
// import from HP Labs internal CVS
//
// Revision 1.2 2002/08/18 23:39:05 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.1 2002/06/14 19:33:21 eob
// initial
