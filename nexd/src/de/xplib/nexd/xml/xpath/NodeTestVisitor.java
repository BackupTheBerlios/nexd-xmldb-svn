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
 * Add functionality to ubclasses of NodeTest. This is a participant in the
 * Visitor Pattern [Gamma et al, #331]. You pass a visitor to the XPath.accept
 * method which then passes it to all the nodes on the parse tree, each one of
 * which calls back one of the visitor's visit methods.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public interface NodeTestVisitor {

    /**
     * @param a ---
     */
    void visit(AllElementTest a);

    /**
     * @param a ---
     */
    void visit(ThisNodeTest a);

    /**
     * @param a ---
     * @throws XPathException ---
     */
    void visit(ParentNodeTest a) throws XPathException;

    /**
     * @param a ---
     */
    void visit(ElementTest a);

    /**
     * @param a ---
     */
    void visit(AttrTest a);

    /**
     * @param a ---
     */
    void visit(TextTest a);

}