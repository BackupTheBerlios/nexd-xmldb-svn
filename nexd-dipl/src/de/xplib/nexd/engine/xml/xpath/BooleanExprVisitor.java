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
 * Add functionality to subclasses of AbstractBooleanExpr. This is a participant
 * in the Visitor Pattern [Gamma et al, #331]. You pass a visitor to the
 * XPath.accept method which then passes it to all the nodes on the parse tree,
 * each one of which calls back one of the visitor's visit methods.
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @link http://sparta.sourceforge.net
 * @stereotype Visitor
 * @version $Rev$
 */
public interface BooleanExprVisitor {

    /**
     * @param trueExprIn ---
     */
    void visit(TrueExpr trueExprIn);

    /**
     * @param existExprIn --
     * @throws XPathException ...
     */
    void visit(AttrExistsExpr existExprIn) throws XPathException;

    /**
     * @param equalsExprIn ---
     * @throws XPathException ...
     */
    void visit(AttrEqualsExpr equalsExprIn) throws XPathException;

    /**
     * @param notEqualsExprIn ---
     * @throws XPathException ...
     */
    void visit(AttrNotEqualsExpr notEqualsExprIn) throws XPathException;

    /**
     * @param lessExprIn ---
     * @throws XPathException ...
     */
    void visit(AttrLessExpr lessExprIn) throws XPathException;
    
    /**
     * Visits a <code>AttrLessEqualsExpr</code> (<i>&lt;=</i>).
     * 
     * @param exprIn The expression instance.
     * @throws XPathException ...
     */
    void visit(AttrLessEqualsExpr exprIn) throws XPathException;

    /**
     * @param greaterExprIn ---
     * @throws XPathException ...
     */
    void visit(AttrGreaterExpr greaterExprIn) throws XPathException;
    
    /**
     * Visits a <code>AttrGreaterEqualsExpr</code> (<i>&gt;=</i>).
     * 
     * @param exprIn The expression instance.
     * @throws XPathException ...
     */
    void visit(AttrGreaterEqualsExpr exprIn) throws XPathException;

    /**
     * @param existExprIn ---
     * @throws XPathException ...
     */
    void visit(TextExistsExpr existExprIn) throws XPathException;

    /**
     * @param equalsExprIn ---
     * @throws XPathException ...
     */
    void visit(TextEqualsExpr equalsExprIn) throws XPathException;

    /**
     * @param notEqualsExprIn ---
     * @throws XPathException ...
     */
    void visit(TextNotEqualsExpr notEqualsExprIn) throws XPathException;

    /**
     * @param equalsExprIn ---
     * @throws XPathException ...
     */
    void visit(PositionEqualsExpr equalsExprIn) throws XPathException;
    
    /**
     * Visits an <code>AndExpr</code> expression and evaluates it.
     * 
     * @param exprIn The <code>AndExpr</code> instance.
     * @throws XPathException ...
     */
    void visit(AndExpr exprIn) throws XPathException;
    
    /**
     * Visits an <code>OrExpr</code> expression and evaluates it.
     * 
     * @param exprIn The <code>OrExpr</code> instance.
     * @throws XPathException ...
     */
    void visit(OrExpr exprIn) throws XPathException;
    
    /**
     * Visits an <code>NotExpr</code> expression and evaluates it.
     * 
     * @param exprIn The <code>NotExpr</code> instance.
     * @throws XPathException ...
     */
    void visit(NotExpr exprIn) throws XPathException;
}
