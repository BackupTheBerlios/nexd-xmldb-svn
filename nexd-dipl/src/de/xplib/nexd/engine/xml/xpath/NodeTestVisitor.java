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
 * Add functionality to ubclasses of AbstractNodeTest. This is a participant in
 * the Visitor Pattern [Gamma et al, #331]. You pass a visitor to the
 * XPath.accept method which then passes it to all the nodes on the parse tree,
 * each one of which calls back one of the visitor's visit methods.
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @link http://sparta.sourceforge.net
 * @stereotype Visitor
 * @version $Revision: 1.4 $
 */
public interface NodeTestVisitor {

    /**
     * @param testIn ---
     */
    void visit(AllElementTest testIn);

    /**
     * @param testIn ---
     */
    void visit(ThisNodeTest testIn);

    /**
     * @param testIn ---
     * @throws XPathException ...
     */
    void visit(ParentNodeTest testIn) throws XPathException;

    /**
     * @param testIn ---
     */
    void visit(ElementTest testIn);

    /**
     * @param testIn ---
     */
    void visit(AttrTest testIn);

    /**
     * @param testIn ---
     */
    void visit(TextTest testIn);

}
