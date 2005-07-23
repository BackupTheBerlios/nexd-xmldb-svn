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
 * Something that crawls over the parse tree. This is a participant in the
 * Visitor Pattern [Gamma et al, #331]. You pass a visitor to the XPath.accept
 * method which then passes it to all the nodes on the parse tree, each one of
 * which calls back one of the visitor's visit methods.
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @link http://sparta.sourceforge.net
 * @stereotype Visitor
 * @version $Rev$
 */
public interface Visitor extends NodeTestVisitor, BooleanExprVisitor {

}

// $Log: Visitor.java,v $
// Revision 1.2  2005/05/30 19:17:08  nexd
// UML documentation update....
//
// Revision 1.1  2005/05/08 11:59:31  nexd
// restructuring
//
// Revision 1.1  2004/12/17 17:13:58  nexd
// Initial checkin
//
// Revision 1.5 2002/12/13 18:06:03 eobrain
// Factor Visitor out into separate visitors for node tests and predicates.
//
// Revision 1.4 2002/12/05 04:34:38 eobrain
// Add support for greater than and less than relational expressions in
// predicates.
//
// Revision 1.3 2002/10/30 16:29:51 eobrain
// Feature request [ 630127 ] Support /a/b[text()='foo']
// http://sourceforge.net/projects/sparta-xml/
//
// Revision 1.2 2002/09/18 05:29:45 eobrain
// Support xpath predicates of the form [1], [2], ...
//
// Revision 1.1.1.1 2002/08/19 05:04:03 eobrain
// import from HP Labs internal CVS
//
// Revision 1.5 2002/08/18 23:39:21 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.4 2002/06/14 19:35:12 eob
// Add handling of "text()" in XPath expressions.
//
// Revision 1.3 2002/06/04 05:27:34 eob
// Simplify use of visitor pattern to make code easier to understand.
//
// Revision 1.2 2002/02/04 22:12:29 eob
// Add handling of nodetest for attribute.
//
// Revision 1.1 2002/02/01 02:49:59 eob
// initial
