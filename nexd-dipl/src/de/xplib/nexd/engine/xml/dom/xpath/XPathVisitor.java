/*
 * Project: nexd 
 * Copyright (C) 2005  Manuel Pichler <manuel.pichler@xplib.de>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * $Log: XPathVisitor.java,v $
 * Revision 1.3  2005/05/11 18:00:12  nexd
 * Minor changes and corrections.
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.4  2005/04/24 15:00:27  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.3  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xml.dom.xpath;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import de.xplib.nexd.engine.xml.xpath.AllElementTest;
import de.xplib.nexd.engine.xml.xpath.AndExpr;
import de.xplib.nexd.engine.xml.xpath.AttrEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrExistsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrGreaterEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrGreaterExpr;
import de.xplib.nexd.engine.xml.xpath.AttrLessEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrLessExpr;
import de.xplib.nexd.engine.xml.xpath.AttrNotEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrTest;
import de.xplib.nexd.engine.xml.xpath.ElementTest;
import de.xplib.nexd.engine.xml.xpath.NotExpr;
import de.xplib.nexd.engine.xml.xpath.OrExpr;
import de.xplib.nexd.engine.xml.xpath.ParentNodeTest;
import de.xplib.nexd.engine.xml.xpath.PositionEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.Step;
import de.xplib.nexd.engine.xml.xpath.TextEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.TextExistsExpr;
import de.xplib.nexd.engine.xml.xpath.TextNotEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.TextTest;
import de.xplib.nexd.engine.xml.xpath.ThisNodeTest;
import de.xplib.nexd.engine.xml.xpath.TrueExpr;
import de.xplib.nexd.engine.xml.xpath.XPathExceptionExt;
import de.xplib.nexd.xml.xpath.XPathException;
import de.xplib.nexd.xml.xpath.XPathI;

/**
 * Visitor that evaluates an xpath expression relative to a context
 * node by walking over the parse tree of the expression.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 * @see http://sparta-xml.sourceforge.net
 */
class XPathVisitor extends AbstractXPathVisitor {

    /**
     * Comment for <code>ONE</code>
     */
    private static final Integer ONE = new Integer(1);

    /**
     * @label context
     */
    private final Node contextNode;

    /**
     * Currently selected <code>Node</code> instance.
     */
    private Node currentNode = null;

    /**
     * Contains the selected <code>Node</code> instances and theis positions.
     *  
     * @associates Node. 
     */
    private final NodeListWithPosition rawNodeList = new NodeListWithPosition();

    /** 
     * Evaluate an absolute xpath expression in a document by walking
     * over the parse tree of th expression.
     * 
     * @param contextIn The context <code>Document</code> instance.
     * @param xpathIn The used <code>XPathI</code> instance.
     * @throws XPathException If the given <code>XPath</code> is not valid. 
     */
    public XPathVisitor(final Document contextIn, final XPathI xpathIn) 
            throws XPathException {
        
        this(xpathIn, contextIn);
    }

    /** 
     * Evaluate a relative xpath expression relative to a context
     * element by walking over the parse tree of th expression.
     * 
     * @param contextIn The context <code>Element</code> instance.
     * @param xpathIn The used <code>XPathI</code> instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid. 
     */
    public XPathVisitor(final Element contextIn, 
                        final XPathI xpathIn) throws XPathException {
        this(xpathIn, contextIn);
        if (xpathIn.isAbsolute()) {
            throw new XPathExceptionExt(xpathIn,
                    "Cannot use element as context node for absolute xpath");
        }
    }

    /** 
     * Evaluate a relative xpath expression relative to a context
     * element by walking over the parse tree of th expression.
     * 
     * @param xpathIn The used <code>XPathI</code> instance.
     * @param contextIn The context <code>Node</code> instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     */
    private XPathVisitor(final XPathI xpathIn, final Node contextIn) 
            throws XPathException {
        
        super(xpathIn);
        contextNode = contextIn;
        filteredNodes.add(contextNode);

        for (Iterator i = xpathIn.getSteps(); i.hasNext();) {
            Step step = (Step) i.next();
            multiLevel = step.isMultiLevel();
            //nodeIterator = null;
            step.getNodeTest().accept(this);
            nodeIterator = rawNodeList.elements();
            filteredNodes.clear();
            while (nodeIterator.hasNext()) {
                currentNode = (Node) nodeIterator.next();
                step.getPredicate().accept(this);
                Boolean expr = (Boolean) exprStack.pop();
                if (expr.booleanValue()) {
                    filteredNodes.add(currentNode);
                }
            }
        }

        //convert result from list of nodes to list of strings
        /*
        if (xpathIn.isStringValue()) {
            int size = filteredNodes.size();
            for (int i = 0; i < size; ++i) {
                Node node = (Node) filteredNodes.elementAt(i);
                String string = (node instanceof Attr) ? ((Attr) node)
                        .getValue() : ((Text) node).getData();
                filteredNodes.setElementAt(string, i);
            }
        }*/

    }

    /**
     * @param docIn ...
     */
    private void accumulateElements(final Document docIn) {
        Element child = docIn.getDocumentElement();
        rawNodeList.add(child, ONE);
        if (multiLevel) {
            accumulateElements(child); //recursive call
        }
    }

    /**
     * @param elementIn ...
     */
    private void accumulateElements(final Element elementIn) {
        int position = 0;
        for (Node n = elementIn.getFirstChild(); n != null; n = n
                .getNextSibling()) {
            if (n instanceof Element) {
                rawNodeList.add(n, ++position);
                if (multiLevel) {
                    accumulateElements((Element) n); //recursive call
                }
            }
        }
    }

    /**
     * @param nodeIn ... 
     */
    private void accumulateElements(final Node nodeIn) {
        if (nodeIn instanceof Document) {
            accumulateElements((Document) nodeIn);
        } else {
            accumulateElements((Element) nodeIn);
        }
    }

    /**
     * Finds all <code>childNodes</code> of the given <code>elemIn</code> that
     * have <code>tagName</code> equal to <code>nameIn</code>.
     * 
     * @param elemIn The context <code>Node</code> instance.
     * @param nameIn The name to find.
     */
    private void accumulateMatchingElements(final Node elemIn, 
                                            final String nameIn) {
        int position = 0;
        for (Node n = elemIn.getFirstChild(); n != null; n = n
                .getNextSibling()) {
            if (n instanceof Element) {
                Element child = (Element) n;
                if (child.getTagName().equals(nameIn)) {
                    rawNodeList.add(child, ++position);
                }
                if (multiLevel) {
                    accumulateMatchingElements(child, nameIn); //recursion
                }
            }
        }
    }

    /**
     * Returns the selected <code>Node</code> instance as <code>Element</code>.
     * 
     * @return The current <code>Element</code>.
     * @throws XPathException If the current <code>Node</code> is no 
     *                        <code>Element</code> instance.
     */
    private Element getElement() throws XPathException {
        if (!(currentNode instanceof Element)) {
            throw new XPathExceptionExt(xpath,
                    "Cannot test attribute of document");
        }
        return (Element) currentNode;
    }
    
    /** 
     * Get all the elements or strings that match the xpath expression.
     * 
     * @return All matching nodes. 
     */
    public Iterator getResult() {
        return filteredNodes.iterator();
    }

    /**
     * Visits an <code>AllElementTest</code>(*) instance.
     * 
     * @param testIn The instance.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AllElementTest)
     */
    public void visit(final AllElementTest testIn) {
        ArrayList oldNodeList = filteredNodes;
        rawNodeList.removeAllElements();
        for (Iterator it = oldNodeList.iterator(); it.hasNext();) {
            accumulateElements((Node) it.next());
        }
    }
    
    

    /**
     * Vistis an <code>AndExpr</code>(@foo and @bar) instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AndExpr)
     */
    public void visit(final AndExpr exprIn) throws XPathException {
        // TODO Auto-generated method stub
        exprIn.getExpr().accept(this);
    }

    /**
     * Vistis an <code>AttrEqualsExpr</code>(@foo='bar') instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrEqualsExpr)
     */
    public void visit(final AttrEqualsExpr exprIn) throws XPathException {
        String attrValue = getElement().getAttribute(exprIn.getAttrName());
        boolean result = exprIn.getAttrValue().equals(attrValue);
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * Vistis an <code>AttrExistsExpr</code>(/foo[@bar]) instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrExistsExpr)
     */
    public void visit(final AttrExistsExpr exprIn) throws XPathException {
        String attrValue = getElement().getAttribute(exprIn.getAttrName());
        boolean result = attrValue != null && attrValue.length() > 0;
        exprStack.push(result ? TRUE : FALSE);
    }
    
    /**
     * Visits an <code>AttrGreaterEqualsExpr</code>(@foo>=2.0) instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrGreaterEqualsExpr)
     */
    public void visit(final AttrGreaterEqualsExpr exprIn) 
            throws XPathException {
        
        double attrValue = Double.valueOf(
                getElement().getAttribute(exprIn.getAttrName())).doubleValue();
        boolean result = attrValue >= exprIn.getAttrValue();
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * Visits an <code>AttrGreaterExpr</code>(@foo>2.0) instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrGreaterExpr)
     */
    public void visit(final AttrGreaterExpr exprIn) throws XPathException {
        double attrValue = Double.valueOf(
                getElement().getAttribute(exprIn.getAttrName())).doubleValue();
        boolean result = attrValue > exprIn.getAttrValue();
        exprStack.push(result ? TRUE : FALSE);
    }
    
    /**
     * Visits an <code>AttrLessEqualsExpr</code>(@foo<=2.0) instance.
     * 
     * @param exprIn The  expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrLessEqualsExpr)
     */
    public void visit(final AttrLessEqualsExpr exprIn) throws XPathException {
        double attrValue = Double.valueOf(
                getElement().getAttribute(exprIn.getAttrName())).doubleValue();
        boolean result = (attrValue <= exprIn.getAttrValue());
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * Visits an <code>AttrLessExpr</code>(@foo<2.0) instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrLessExpr)
     */
    public void visit(final AttrLessExpr exprIn) throws XPathException {
        double attrValue = Double.valueOf(
                getElement().getAttribute(exprIn.getAttrName())).doubleValue();
        boolean result = attrValue < exprIn.getAttrValue();
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * Visits an <code>AttrNotEqualsExpr</code>(@foo!='bar') instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrNotEqualsExpr)
     */
    public void visit(final AttrNotEqualsExpr exprIn) throws XPathException {
        String attrValue = getElement().getAttribute(exprIn.getAttrName());
        boolean result = !exprIn.getAttrValue().equals(attrValue);
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * Visits an <code>AttrTest</code>(/@foo) instance.
     * 
     * @param testIn The instance.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrTest)
     */
    public void visit(final AttrTest testIn) {
        ArrayList oldNodeList = filteredNodes;
        rawNodeList.removeAllElements();
        for (Iterator it = oldNodeList.iterator(); it.hasNext();) {
            Node node = (Node) it.next();
            if (node instanceof Element) {
                Element element = (Element) node;
                Attr attr = element.getAttributeNode(testIn.getAttrName());
                if (attr != null) {
                    rawNodeList.add(attr);
                }
            }
        }
    }

    /**
     * Visits an <code>ElementTest</code>(/foo/) instance.
     * 
     * @param testIn The instance.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.ElementTest)
     */
    public void visit(final ElementTest testIn) {
        String tagName = testIn.getTagName();
        ArrayList oldNodeList = filteredNodes;
        rawNodeList.removeAllElements();
        for (Iterator it = oldNodeList.iterator(); it.hasNext();) {
            Node node = (Node) it.next();
            accumulateMatchingElements(node, tagName);
        }
    }
    
    /**
     * Visits a <code>NotExpr</code>(... and not @foo='bar') instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.NotExpr)
     */
    public void visit(final NotExpr exprIn) throws XPathException {
        // TODO Auto-generated method stub

    }
    
    /**
     * Visits an <code>OrExpr</code>(text() or @foo='bar') instance.
     * 
     * @param exprIn The expression instance
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.OrExpr)
     */
    public void visit(final OrExpr exprIn) throws XPathException {
        exprIn.getExpr().accept(this);
    }

    /**
     * Visits a <code>ParentNodeTest</code>(..) instance.
     * 
     * @param testIn The instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.ParentNodeTest)
     */
    public void visit(final ParentNodeTest testIn) throws XPathException {
        ArrayList oldNodeList = filteredNodes;
        rawNodeList.removeAllElements();
        int position = 0;
        Node lastParent = null;
        for (Iterator it = oldNodeList.iterator(); it.hasNext();) {
            Object node = it.next();
            if (node instanceof Node) {
                Node parent = ((Node) node).getParentNode();
                if (parent != null && parent != lastParent) {
                    rawNodeList.add(parent, ++position);
                    lastParent = parent;
                }
            }
        }
    }

    /**
     * Visits a <code>PositionEqualsExpr</code>(/foo[2]) instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.PositionEqualsExpr)
     */
    public void visit(final PositionEqualsExpr exprIn) throws XPathException {
        boolean result = (rawNodeList.position(getElement()) == exprIn
                .getPosition());
        exprStack.push(result ? TRUE : FALSE);
    }

    /**
     * Visits a <code>TextEqualsExpr</code>(text()='bar') instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TextEqualsExpr)
     */
    public void visit(final TextEqualsExpr exprIn) throws XPathException {
        Element element = getElement();
        for (Node i = element.getFirstChild(); i != null; i = i
                .getNextSibling()) {
            if (i instanceof Text) {
                Text text = (Text) i;
                if (text.getData().equals(exprIn.getValue())) {
                    exprStack.push(TRUE);
                    return;
                }
            }
        }
        exprStack.push(FALSE);
    }

    /**
     * Visits a <code>TextExistsExpr</code>(/foo[text()] instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TextExistsExpr)
     */
    public void visit(final TextExistsExpr exprIn) throws XPathException {
        Element element = getElement();
        for (Node i = element.getFirstChild(); i != null; i = i
                .getNextSibling()) {
            if (i instanceof Text) {
                exprStack.push(TRUE);
                return;
            }
        }
        exprStack.push(FALSE);
    }

    /**
     * Visits a <code>TextNotEqualsExpr</code>(text()!='foo') instance.
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TextNotEqualsExpr)
     */
    public void visit(final TextNotEqualsExpr exprIn) throws XPathException {
        Element element = getElement();
        for (Node i = element.getFirstChild(); i != null; i = i
                .getNextSibling()) {
            if (i instanceof Text) {
                Text text = (Text) i;
                if (!text.getData().equals(exprIn.getValue())) {
                    exprStack.push(TRUE);
                    return;
                }
            }
        }
        exprStack.push(FALSE);
    }

    /**
     * Visits a <code>TextTest</code>(text()) instance.
     * 
     * @param testIn The instance.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TextTest)
     */
    public void visit(final TextTest testIn) {
        ArrayList oldNodeList = filteredNodes;
        rawNodeList.removeAllElements();
        for (Iterator it = oldNodeList.iterator(); it.hasNext();) {
            Object node = it.next();
            if (node instanceof Element) {
                Element element = (Element) node;
                for (Node n = element.getFirstChild(); n != null; n = n
                        .getNextSibling()) {
                    if (n instanceof Text) {
                        rawNodeList.add((Text) n);
                    }
                }
            }
        }
    }

    /**
     * Visits a <code>ThisNodeTest</code>(.) instance.
     * 
     * @param testIn The instance.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.ThisNodeTest)
     */
    public void visit(final ThisNodeTest testIn) {
        rawNodeList.removeAllElements();
        rawNodeList.add(contextNode, ONE);
    }

    /**
     * Visits a <code>TrueExpr</code> instance.
     * 
     * @param exprIn The expression instance.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TrueExpr)
     */
    public void visit(final TrueExpr exprIn) {
        exprStack.push(TRUE);
    }

}

// $Log: XPathVisitor.java,v $
// Revision 1.3  2005/05/11 18:00:12  nexd
// Minor changes and corrections.
//
// Revision 1.2  2005/05/11 17:31:40  nexd
// Refactoring and extended test cases
//
// Revision 1.1  2005/05/08 11:59:33  nexd
// restructuring
//
// Revision 1.4  2005/04/24 15:00:27  nexd
// Bugfixes and many performance and coding improvements.
//
// Revision 1.3  2005/03/31 12:08:40  nexd
// Advanced VCL support
//
// Revision 1.2  2005/03/14 12:22:49  nexd
// Heavy javadoc, checkstyle and eclipse todo-Task session.
//
// Revision 1.1  2005/02/04 13:07:21  nexd
// Added support for XPath queries against the NEXD DOM model.
//
// Revision 1.9  2003/12/08 22:46:00  eobrain
// Make J2ME compatible.  (Boolean.TRUE etc. not supported in J2ME)
//
// Revision 1.8  2003/12/08 21:11:14  eobrain
// Fix bug with parent ("..") step.
//
// Revision 1.7  2003/05/12 20:19:16  eobrain
// Remove unused private method.
//
// Revision 1.6  2003/01/27 23:30:59  yuhongx
// Replaced Hashtable with HashMap.
//
// Revision 1.5  2003/01/09 01:20:42  yuhongx
// Use JDK1.1 API to make code work with PersonalJava.
//
// Revision 1.4  2002/12/05 04:36:37  eobrain
// Add support for greater than and less than in predicates.
//
// Revision 1.3  2002/10/30 16:29:18  eobrain
// Feature request [ 630127 ] Support /a/b[text()='foo']
// http://sourceforge.net/projects/sparta-xml/
//
// Revision 1.2  2002/09/18 05:26:46  eobrain
// Support xpath predicates of the form [1], [2], ...
//
// Revision 1.1.1.1  2002/08/19 05:04:23  eobrain
// import from HP Labs internal CVS
//
// Revision 1.10  2002/08/19 00:21:30  eob
// Make class package-private so as to remove clutter in Javadoc.
//
// Revision 1.9  2002/06/21 00:20:16  eob
// Make work with old JDK 1.1.*
//
// Revision 1.8  2002/06/14 19:34:11  eob
// Add handling of "text()" in XPath expressions.
//
// Revision 1.7  2002/06/04 05:28:56  eob
// Simplify use of visitor pattern to make code easier to understand.
// Fix bug when predicate in middle of XPath.
//
// Revision 1.6  2002/05/23 21:09:59  eob
// Better error reporting.
//
// Revision 1.5  2002/03/25 22:59:03  eob
// Handle case of attempt to do ".." from root
//
// Revision 1.4  2002/02/14 02:19:58  eob
// Comment change only.
//
// Revision 1.3  2002/02/04 22:06:11  eob
// Add handling of attribute xpath expressions that return strings.
//
// Revision 1.2  2002/02/01 22:03:05  eob
// Make consistent with sparta version of this class.
//
// Revision 1.1  2002/02/01 18:52:14  eob
// initial
