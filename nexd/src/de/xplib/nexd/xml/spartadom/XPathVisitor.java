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
package de.xplib.nexd.xml.spartadom;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import de.xplib.nexd.xml.xpath.AllElementTest;
import de.xplib.nexd.xml.xpath.AttrEqualsExpr;
import de.xplib.nexd.xml.xpath.AttrExistsExpr;
import de.xplib.nexd.xml.xpath.AttrGreaterExpr;
import de.xplib.nexd.xml.xpath.AttrLessExpr;
import de.xplib.nexd.xml.xpath.AttrNotEqualsExpr;
import de.xplib.nexd.xml.xpath.AttrTest;
import de.xplib.nexd.xml.xpath.BooleanExpr;
import de.xplib.nexd.xml.xpath.ElementTest;
import de.xplib.nexd.xml.xpath.ParentNodeTest;
import de.xplib.nexd.xml.xpath.PositionEqualsExpr;
import de.xplib.nexd.xml.xpath.Step;
import de.xplib.nexd.xml.xpath.TextEqualsExpr;
import de.xplib.nexd.xml.xpath.TextExistsExpr;
import de.xplib.nexd.xml.xpath.TextNotEqualsExpr;
import de.xplib.nexd.xml.xpath.TextTest;
import de.xplib.nexd.xml.xpath.ThisNodeTest;
import de.xplib.nexd.xml.xpath.TrueExpr;
import de.xplib.nexd.xml.xpath.Visitor;
import de.xplib.nexd.xml.xpath.XPath;
import de.xplib.nexd.xml.xpath.XPathException;

/**
 * A list of nodes, together with the positions in their context of each node.
 */
class NodeListWithPosition {

    /**
     * Comment for <code>EIGHT</code>
     */
    private static final Integer EIGHT = new Integer(8);

    /**
     * Comment for <code>FIVE</code>
     */
    private static final Integer FIVE = new Integer(5);

    /**
     * Comment for <code>FOUR</code>
     */
    private static final Integer FOUR = new Integer(4);

    /**
     * Comment for <code>NINE</code>
     */
    private static final Integer NINE = new Integer(9);

    /**
     * Comment for <code>ONE</code>
     */
    private static final Integer ONE = new Integer(1);

    /**
     * Comment for <code>SEVEN</code>
     */
    private static final Integer SEVEN = new Integer(7);

    /**
     * Comment for <code>SIX</code>
     */
    private static final Integer SIX = new Integer(6);

    /**
     * Comment for <code>TEN</code>
     */
    private static final Integer TEN = new Integer(10);

    /**
     * Comment for <code>THREE</code>
     */
    private static final Integer THREE = new Integer(3);

    /**
     * Comment for <code>TWO</code>
     */
    private static final Integer TWO = new Integer(2);

    /**
     * @param node ..
     * @return ..
     */
    private static Integer identity(final BaseNode node) {
        return new Integer(System.identityHashCode(node));
    }

    /**
     * Comment for <code>positions</code>
     */
    private Hashtable positions = new Hashtable();

    /**
     * Comment for <code>vector</code>
     */
    private final Vector vector = new Vector();

    /**
     * @param node ..
     * @param position ..
     */
    void add(final BaseNode node, final int position) {
        //Profiling shows thisto be the most heavily used method in Sparta so
        //optimize the crap out of it.
        vector.addElement(node);
        //Avoid creating new integer objects for common cases
        Integer posn;
        switch (position) {
        case 1:
            posn = ONE;
            break;
        case 2:
            posn = TWO;
            break;
        case 3:
            posn = THREE;
            break;
        case 4:
            posn = FOUR;
            break;
        case 5:
            posn = FIVE;
            break;
        case 6:
            posn = SIX;
            break;
        case 7:
            posn = SEVEN;
            break;
        case 8:
            posn = EIGHT;
            break;
        case 9:
            posn = NINE;
            break;
        case 10:
            posn = TEN;
            break;
        default:
            posn = new Integer(position);
            break;
        }
        positions.put(identity(node), posn);
    }

    /**
     * @param string ..
     */
    void add(final String string) {
        vector.addElement(string);
    }
    
    /**
     * @return ..
     */
    Enumeration iterator() {
        return vector.elements();
    }

    /**
     * @param node ..
     * @return ..
     */
    int position(final BaseNode node) {
        return ((Integer) positions.get(identity(node))).intValue();
    }

    /**
     * 
     */
    void removeAllElements() {
        vector.removeAllElements();
        positions.clear();
    }

    /**
     * <Some description here>
     * 
     * @return ..
     * @see java.lang.Object#toString()
     */
    public String toString() {
        try {

            StringBuffer y = new StringBuffer("{ ");
            for (Enumeration i = vector.elements(); i.hasMoreElements();) {
                Object e = i.nextElement();
                if (e instanceof String) {
                    y.append("String(" + e + ") ");
                } else {
                    BaseNode n = (BaseNode) e;
                    y.append("Node(" + n.toXml() + ")["
                            + positions.get(identity(n)) + "] ");
                }
            }
            y.append("}");
            return y.toString();

        } catch (IOException e) {
            return e.toString();
        }
    }
}

/**
 * Visitor that evaluates an xpath expression relative to a context node by
 * walking over the parse tree of the expression.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 * @stereotype visitor
 */
/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class XPathVisitor implements Visitor {

    /*
     * static private class BooleanStack extends LinkedList{ void push(Boolean
     * b){ addLast(b); } Boolean pop(){ return (Boolean)removeLast(); } }
     */
    /**
     * Profiling found this to be very heavily used so do not use
     * java.util.LinkedList
     */
    private static class BooleanStack {

        /**
         */
        private static class Item {

            /**
             * Comment for <code>bool</code>
             */
            private final Boolean bool;

            /**
             * Comment for <code>prev</code>
             */
            private final Item prev;
            
            /**
             * @param b ..
             * @param p ..
             */
            Item(final Boolean b, final Item p) {
                bool = b;
                prev = p;
            }
        }
        
        /**
         * Comment for <code>top</code>
         */
        private Item top = null;

        /**
         * @return ..
         */
        Boolean pop() {
            Boolean result = top.bool;
            top = top.prev;
            return result;
        }

        /**
         * @param b ..
         */
        void push(final Boolean b) {
            top = new Item(b, top);
        }
    }

    /**
     * Comment for <code>FALSE</code>
     */
    private static final Boolean FALSE = new Boolean(false);

    /**
     * Comment for <code>TRUE</code>
     */
    private static final Boolean TRUE = new Boolean(true);

    /**
     * Comment for <code>contextNode</code>
     */
    private BaseNode contextNode;

    /**
     * Comment for <code>exprStack</code>
     */
    private final BooleanStack exprStack = new BooleanStack();

    /**
     * Comment for <code>multiLevel</code>
     */
    private boolean multiLevel;

    /**
     * Comment for <code>node</code>
     */
    private Object node = null; //String or Element

    /**
     * Comment for <code>nodelistFiltered</code>
     */
    private Vector nodelistFiltered = new Vector();

    /**
     * Comment for <code>nodelistRaw</code>
     */
    private final NodeListWithPosition nodelistRaw =
        	new NodeListWithPosition();

    /**
     * Comment for <code>nodesetIterator</code>
     */
    private Enumeration nodesetIterator = null;

    /**
     * Comment for <code>xpath</code>
     */
    private final XPath xpath;

    /**
     * Evaluate an absolute xpath expression in a document by walking over the
     * parse tree of th expression.
     *
     * @param context ..
     * @param xpathIn ..
     * @throws XPathException ..
     */
    public XPathVisitor(final BaseDocument context, final XPath xpathIn) 
            throws XPathException {
        
        this(xpathIn, context);
    }

    /**
     * Evaluate a relative xpath expression relative to a context element by
     * walking over the parse tree of th expression.
     *
     * @param context ..
     * @param xpathIn ..
     * @throws XPathException ..
     */
    public XPathVisitor(final BaseElement context, 
                        final XPath xpathIn) 
                                throws XPathException {
        this(xpathIn, context);
        if (xpathIn.isAbsolute()) {
            throw new XPathException(xpathIn,
                    "Cannot use element as context node for absolute xpath");
        }
    }

    /**
     * Evaluate a relative xpath expression relative to a context element by
     * walking over the parse tree of th expression.
     *
     * @param xpathIn ..
     * @param context ..
     * @throws XPathException ..
     */
    private XPathVisitor(final XPath xpathIn, final BaseNode context) 
    		throws XPathException {
        
        xpath = xpathIn;
        contextNode = context;
        nodelistFiltered = new Vector(1);
        nodelistFiltered.addElement(contextNode);

        for (Enumeration i = xpath.getSteps(); i.hasMoreElements();) {
            Step step = (Step) i.nextElement();
            multiLevel = step.isMultiLevel();
            nodesetIterator = null;
            step.getNodeTest().accept(this);
            nodesetIterator = nodelistRaw.iterator();
            nodelistFiltered.removeAllElements();
            BooleanExpr predicate = step.getPredicate();
            while (nodesetIterator.hasMoreElements()) {
                node = nodesetIterator.nextElement();
                predicate.accept(this);
                Boolean expr = exprStack.pop();
                if (expr.booleanValue()) {
                    nodelistFiltered.addElement(node);
                }
            }
        }
    }

    /**
     * @param doc ..
     */
    private void accumulateElements(final BaseDocument doc) {
        BaseElement child = doc.getDocumentElement();
        nodelistRaw.add(child, 1);
        if (multiLevel) {
            accumulateElements(child); //recursive call
        }
    }

    /**
     * @param element ..
     */
    private void accumulateElements(final BaseElement element) {
        int position = 0;
        for (BaseNode n = element.getFirstChild(); n != null; n = n
                .getNextSibling()) {
            if (n instanceof BaseElement) {
                nodelistRaw.add(n, ++position);
                if (multiLevel) {
                    accumulateElements((BaseElement) n); //recursive call
                }
            }
        }
    }

    /**
     * @param document ..
     * @param tagName ...
     */
    private void accumulateMatchingElements(final BaseDocument document, 
            		                        final String tagName) {
        BaseElement child = document.getDocumentElement();
        if (child == null) {
            return; //no document element
        }
        if (child.getTagName() == tagName) { //both strings interned
            nodelistRaw.add(child, 1);
        }
        if (multiLevel) {
            accumulateMatchingElements(child, tagName); //recursive call
        }
    }

    /**
     * @param element ..
     * @param tagName ..
     */
    private void accumulateMatchingElements(final BaseElement element, 
                                            final String tagName) {
        int position = 0;
        for (BaseNode n = element.getFirstChild(); n != null; n = n
                .getNextSibling()) {
            if (n instanceof BaseElement) {
                BaseElement child = (BaseElement) n;
                if (child.getTagName() == tagName) { //both strings interned
                    nodelistRaw.add(child, ++position);
                }
                if (multiLevel) {
                    accumulateMatchingElements(child, tagName); //recursion
                }
            }
        }
    }

    /**
     * Get the first element that match the xpath expression, or null.
     * 
     * @return ..
     */
    public BaseElement getFirstResultElement() {
        return nodelistFiltered.size() == 0 ? null
                : (BaseElement) nodelistFiltered.elementAt(0);
    }

    /**
     * Get the first string that match the xpath expression, or null.
     *  
     * @return ..
     */
    public String getFirstResultString() {
        return nodelistFiltered.size() == 0 ? null
                : (String) nodelistFiltered.elementAt(0).toString();
    }

    /** 
     * Get all the elements or strings that match the xpath expression.
     * 
     * @return ..
     */
    public Enumeration getResultEnumeration() {
        return nodelistFiltered.elements();
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @see de.xplib.nexd.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.xml.xpath.AllElementTest)
     */
    public void visit(final AllElementTest a) {
        Vector oldNodeList = nodelistFiltered;
        nodelistRaw.removeAllElements();
        for (Enumeration i = oldNodeList.elements(); i.hasMoreElements();) {
            Object localNode = i.nextElement();
            if (localNode instanceof BaseElement) {
                accumulateElements((BaseElement) localNode);
            } else if (localNode instanceof BaseDocument) {
                accumulateElements((BaseDocument) localNode);
            }
        }
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @throws XPathException ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     * 		de.xplib.nexd.xml.xpath.AttrEqualsExpr)
     */
    public void visit(final AttrEqualsExpr a) throws XPathException {
        if (!(node instanceof BaseElement)) {
            throw new XPathException(xpath,
                    "Cannot test attribute of document");
        }
        BaseElement element = (BaseElement) node;
        String attrValue = element.getAttribute(a.getAttrName());
        boolean result = a.getAttrValue().equals(attrValue);
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @throws XPathException ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.xml.xpath.AttrExistsExpr)
     */
    public void visit(final AttrExistsExpr a) throws XPathException {
        if (!(node instanceof BaseElement)) {
            throw new XPathException(xpath,
                    "Cannot test attribute of document");
        }
        BaseElement element = (BaseElement) node;
        String attrValue = element.getAttribute(a.getAttrName());
        boolean result = attrValue != null && attrValue.length() > 0;
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @throws XPathException ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.xml.xpath.AttrGreaterExpr)
     */
    public void visit(final AttrGreaterExpr a) throws XPathException {
        if (!(node instanceof BaseElement)) {
            throw new XPathException(xpath,
                    "Cannot test attribute of document");
        }
        BaseElement element = (BaseElement) node;
        // Use jdk1.1 API to make the code work with PersonalJava.
        // double attrValue = Double.parseDouble( element.getAttribute(
        // a.getAttrName() ) );
        long attrValue = Long.parseLong(element.getAttribute(a.getAttrName()));
        boolean result = attrValue > a.getAttrValue();
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @throws XPathException ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.xml.xpath.AttrLessExpr)
     */
    public void visit(final AttrLessExpr a) throws XPathException {
        if (!(node instanceof BaseElement)) {
            throw new XPathException(xpath,
                    "Cannot test attribute of document");
        }
        BaseElement element = (BaseElement) node;
        // Use jdk1.1 API to make the code work with PersonalJava.
        // double attrValue = Double.parseDouble( element.getAttribute(
        // a.getAttrName() ) );
        long attrValue = Long.parseLong(element.getAttribute(a.getAttrName()));
        boolean result = attrValue < a.getAttrValue();
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @throws XPathException ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.xml.xpath.AttrNotEqualsExpr)
     */
    public void visit(final AttrNotEqualsExpr a) throws XPathException {
        if (!(node instanceof BaseElement)) {
            throw new XPathException(xpath,
                    "Cannot test attribute of document");
        }
        BaseElement element = (BaseElement) node;
        String attrValue = element.getAttribute(a.getAttrName());
        boolean result = !a.getAttrValue().equals(attrValue);
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * <Some description here>
     * 
     * @param test ..
     * @see de.xplib.nexd.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.xml.xpath.AttrTest)
     */
    public void visit(final AttrTest test) {
        Vector oldNodeList = nodelistFiltered;
        nodelistRaw.removeAllElements();
        for (Enumeration i = oldNodeList.elements(); i.hasMoreElements();) {
            BaseNode localNode = (BaseNode) i.nextElement();
            if (localNode instanceof BaseElement) {
                BaseElement element = (BaseElement) localNode;
                String attr = element.getAttribute(test.getAttrName());
                if (attr != null) {
                    nodelistRaw.add(attr);
                }
            }
        }
    }

    /**
     * <Some description here>
     * 
     * @param test ..
     * @see de.xplib.nexd.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.xml.xpath.ElementTest)
     */
    public void visit(final ElementTest test) {
        String tagName = test.getTagName();
        Vector oldNodeList = nodelistFiltered;
        int n = oldNodeList.size();
        nodelistRaw.removeAllElements();
        for (int i = 0; i < n; ++i) {
            Object localNode = oldNodeList.elementAt(i);
            if (localNode instanceof BaseElement) {
                accumulateMatchingElements((BaseElement) localNode, tagName);
            } else if (localNode instanceof BaseDocument) {
                accumulateMatchingElements((BaseDocument) localNode, tagName);
            }
        }
    }

    /**
     * @param a ..
     * @throws XPathException ..
     *             if ".." applied to node with no parent.
     * @see de.xplib.nexd.xml.xpath.NodeTestVisitor#visit(ParentNodeTest)
     */
    public void visit(final ParentNodeTest a) throws XPathException {
        nodelistRaw.removeAllElements();
        BaseNode parent = contextNode.getParentNode();
        if (parent == null) {
            throw new XPathException(xpath,
                    "Illegal attempt to apply \"..\" to node with no parent.");
        }
        nodelistRaw.add(parent, 1);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @throws XPathException ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.xml.xpath.PositionEqualsExpr)
     */
    public void visit(final PositionEqualsExpr a) throws XPathException {
        if (!(node instanceof BaseElement)) {
            throw new XPathException(xpath, "Cannot test position of document");
        }
        BaseElement element = (BaseElement) node;
        boolean result = (nodelistRaw.position(element) == a.getPosition());
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @throws XPathException ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.xml.xpath.TextEqualsExpr)
     */
    public void visit(final TextEqualsExpr a) throws XPathException {
        if (!(node instanceof BaseElement)) {
            throw new XPathException(xpath,
                    "Cannot test attribute of document");
        }
        BaseElement element = (BaseElement) node;
        for (BaseNode i = element.getFirstChild(); i != null; i = i
                .getNextSibling()) {
            if (i instanceof BaseText) {
                BaseText text = (BaseText) i;
                if (text.getData().equals(a.getValue())) {
                    exprStack.push(TRUE);
                    return;
                }
            }
        }
        exprStack.push(FALSE);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @throws XPathException ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.xml.xpath.TextExistsExpr)
     */
    public void visit(final TextExistsExpr a) throws XPathException {
        if (!(node instanceof BaseElement)) {
            throw new XPathException(xpath,
                    "Cannot test attribute of document");
        }
        BaseElement element = (BaseElement) node;
        for (BaseNode i = element.getFirstChild(); i != null; i = i
                .getNextSibling()) {
            if (i instanceof BaseText) {
                exprStack.push(TRUE);
                return;
            }
        }
        exprStack.push(FALSE);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @throws XPathException ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.xml.xpath.TextNotEqualsExpr)
     */
    public void visit(final TextNotEqualsExpr a) throws XPathException {
        if (!(node instanceof BaseElement)) {
            throw new XPathException(xpath,
                    "Cannot test attribute of document");
        }
        BaseElement element = (BaseElement) node;
        for (BaseNode i = element.getFirstChild(); i != null; i = i
                .getNextSibling()) {
            if (i instanceof BaseText) {
                BaseText text = (BaseText) i;
                if (!text.getData().equals(a.getValue())) {
                    exprStack.push(TRUE);
                    return;
                }
            }
        }
        exprStack.push(FALSE);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @see de.xplib.nexd.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.xml.xpath.TextTest)
     */
    public void visit(final TextTest a) {
        Vector oldNodeList = nodelistFiltered;
        nodelistRaw.removeAllElements();
        for (Enumeration i = oldNodeList.elements(); i.hasMoreElements();) {
            Object localNode = i.nextElement();
            if (localNode instanceof BaseElement) {
                BaseElement element = (BaseElement) localNode;
                for (BaseNode n = element.getFirstChild(); n != null; n = n
                        .getNextSibling()) {
                    if (n instanceof BaseText) {
                        nodelistRaw.add(((BaseText) n).getData());
                    }
                }
            }
        }
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @see de.xplib.nexd.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.xml.xpath.ThisNodeTest)
     */
    public void visit(final ThisNodeTest a) {
        nodelistRaw.removeAllElements();
        nodelistRaw.add(contextNode, 1);
    }

    /**
     * <Some description here>
     * 
     * @param a ..
     * @see de.xplib.nexd.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.xml.xpath.TrueExpr)
     */
    public void visit(final TrueExpr a) {
        exprStack.push(TRUE);
    }

}

// $Log: XPathVisitor.java,v $
// Revision 1.9 2003/08/11 18:48:39 eobrain
// Fix bug in xpath tag[n] when identical elements.
//
// Revision 1.8 2003/07/17 23:58:40 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.7 2003/05/12 20:06:14 eobrain
// Performance improvements: optimize code in areas that profiling revealed to
// be bottlenecks.
//
// Revision 1.6 2003/01/27 23:30:58 yuhongx
// Replaced Hashtable with HashMap.
//
// Revision 1.5 2003/01/09 01:10:50 yuhongx
// Use JDK1.1 API to make the code work with PersonalJava.
//
// Revision 1.4 2002/12/05 04:35:39 eobrain
// Add support for greater than and less than in predicates.
//
// Revision 1.3 2002/10/30 16:28:46 eobrain
// Feature request [ 630127 ] Support /a/b[text()='foo']
// http://sourceforge.net/projects/sparta-xml/
//
// Revision 1.2 2002/09/18 05:29:04 eobrain
// Support xpath predicates of the form [1], [2], ...
//
// Revision 1.1.1.1 2002/08/19 05:03:53 eobrain
// import from HP Labs internal CVS
//
// Revision 1.11 2002/08/18 04:47:47 eob
// Make class package-private so as not to clutter up the javadoc.
//
// Revision 1.10 2002/06/21 00:28:33 eob
// Make work with old JDK 1.1.*
//
// Revision 1.9 2002/06/14 19:34:50 eob
// Add handling of "text()" in XPath expressions.
//
// Revision 1.8 2002/06/04 05:29:28 eob
// Simplify use of visitor pattern to make code easier to understand.
// Fix bug when predicate in middle of XPath.
//
// Revision 1.7 2002/05/23 21:18:52 eob
// Better error reporting.
//
// Revision 1.6 2002/05/10 21:39:28 eob
// Allow documents to take relative xpaths.
//
// Revision 1.5 2002/03/26 01:49:53 eob
// Return different results depending on type. Return first result.
//
// Revision 1.4 2002/03/21 23:54:36 eob
// Fix handling of .. when no parent.
//
// Revision 1.3 2002/02/14 02:21:45 eob
// Handle attribute XPaths.
//
// Revision 1.2 2002/02/04 22:09:44 eob
// add visit(AttrTest)
//
// Revision 1.1 2002/02/01 22:46:12 eob
// initial
