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
package de.xplib.nexd.engine.xml.dom.xpath;

import java.io.IOException;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.xplib.nexd.engine.xml.xpath.XPathExceptionExt;
import de.xplib.nexd.xml.xpath.AbstractXPathFactory;
import de.xplib.nexd.xml.xpath.XPathException;
import de.xplib.nexd.xml.xpath.XPathI;

/**
 * Facade class providing a convenient API to the XPath parser and
 * evaluator.  For efficiency, parsed XPath expressions are cached.
 * For ease of migration from Xalan XPath this class has the same name
 * and has the same method names as the Xalan XPathAPI class.  See <a
 * href="http://home.earthlink.net/~huston2/dp/facade.html">Facade
 * Pattern</a>
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public final class XPathAPI {
    
    /**
     * Construtor
     */
    private XPathAPI() {
        super();
    }

    /**
     * Evaluates the given xpath query(<code>queryIn</code>) against the 
     * <code>Document</code> referenced by <code>docIn</code> and returns the
     * result as a <code>NodeList</code> instance.
     * 
     * @param docIn The context <code>Document</code> instance.
     * @param queryIn The xpath query to evaluate.
     * @return The result of the xpath query as a <code>NodeList</code> instance
     * @throws XPathException If the xpath query is not valid.
     * @throws IOException If something goes wrong while <code>queryIn</code>
     *                     is parsed.
     */
    public static NodeList eval(final Document docIn, final String queryIn)
            throws XPathException, IOException {
        
        XPathI xpath = AbstractXPathFactory.getInstance().createXPath(
                (queryIn.charAt(0) == '/') ? queryIn : "/" + queryIn);
        
        final XPathVisitor visitor = new XPathVisitor(docIn, xpath);
        
        return new NodeList() {
            public int getLength() {
                return visitor.filteredNodes.size();
            }
            
            public Node item(final int indexIn) {
                return (Node) visitor.filteredNodes.get(indexIn);
            }
        };
    }
    
    /** 
     * Evaluates the given <code>queryIn</code> against the 
     * <code>Document</code> referenced by <code>docIn</code>. 
     * 
     * @param docIn The context <code>Document</code> instance.
     * @param queryIn The xpath query to evaluate.
     * @return All the elements in the document that match the absolute XPath 
     *         expression. 
     * @throws XPathException If the given xpath query is not valid.
     * @throws IOException If something goes wrong while <code>queryIn</code>
     *                     is parsed.
     */
    public static Iterator selectElementIterator(final Document docIn,
                                                    final String queryIn) 
            throws XPathException, IOException {
        
        XPathI xpath = AbstractXPathFactory.getInstance().createXPath(
                (queryIn.charAt(0) == '/') ? queryIn : "/" + queryIn);
        
        if (xpath.isStringValue()) {
            throw new XPathExceptionExt(xpath, "\"" + queryIn
                    + "\" evaluates to string not element");
        }
        XPathVisitor visitor = new XPathVisitor(docIn, xpath);
        return visitor.getResult();
    }
    
    /** 
     * Evaluates the given xpath query(<code>queryIn</code>) against the given
     * <code>Element</code> instance.
     * 
     * @param elemIn The context <code>Element</code> instance.
     * @param queryIn The xpath query to evaluate.
     * @return All the elements that match the relative XPath expression with 
     *         respect to the context element. 
     * @throws XPathException If the given xpath query is not valid.
     * @throws IOException If something goes wrong while <code>queryIn</code> 
     *                     is parsed.
     */
    public static Iterator selectElementIterator(final Element elemIn,
                                                    final String queryIn) 
            throws XPathException, IOException {
        
        XPathI xpath = AbstractXPathFactory.getInstance().createXPath(queryIn);
        
        if (xpath.isStringValue()) {
            throw new XPathExceptionExt(xpath, "\"" + queryIn
                    + "\" evaluates to string not element");
        }
        
        XPathVisitor visitor = new XPathVisitor(elemIn, xpath);
        return visitor.getResult();
    }
    
    /** 
     * Evaluates the given <code>queryIn</code> against the 
     * <code>Document</code> referenced by <code>docIn</code> and returns the
     * first <code>Element</code> or <code>null</code>. 
     * 
     * @param docIn The context <code>Document</code> instance.
     * @param queryIn The xpath query to evaluate.
     * @return The first element in this document that matches the absolute 
     *         XPath expression, or null if there is no match.
     * @throws XPathException If the given xpath query is not valid.
     * @throws IOException If something goes wrong while <code>queryIn</code> 
     *                     is parsed.
     * @todo make more efficient by short-circuiting the search.  
     */
    public static Element selectSingleElement(final Document docIn, 
                                              final String queryIn)
            throws XPathException, IOException {
        
        Iterator iter = selectElementIterator(
                docIn, (queryIn.charAt(0) == '/') ? queryIn : "/" + queryIn);
        
        return (iter.hasNext()) ? (Element) iter.next() : null;
    }
    
    /** 
     * Evaluates the given xpath query(<code>queryIn</code>) against the given
     * <code>Element</code> instance and returns the first <code>Element</code>
     * or <code>null</code>.
     * 
     * @param elemIn The context <code>Element</code> instance.
     * @param queryIn The xpath query to evaluate.
     * @return The first element that matches the relative XPath expression with
     *         respect to the context element, or null if there is no match.
     * @throws XPathException If the given xpath query is not valid.
     * @throws IOException If something goes wrong while <code>queryIn</code> 
     *                     is parsed.
     * @todo make more efficient by short-circuiting the search.  
     */
    public static Element selectSingleElement(final Element elemIn,
                                              final String queryIn) 
            throws XPathException, IOException {
        
        Iterator iter = selectElementIterator(elemIn, queryIn);
        return (iter.hasNext()) ? (Element) iter.next() : null;
    }
    
    /** 
     * Evaluates the given <code>queryIn</code> against the 
     * <code>Document</code> referenced by <code>docIn</code> and returns the
     * first <code>String</code> or <code>null</code>. 
     * 
     * @param docIn The context <code>Document</code> instance.
     * @param queryIn The xpath query to evaluate.
     * @return The first string in this document that matches the absolute XPath
     *         expression, or null if there is no match.
     * @throws XPathException If the given xpath query is not valid.
     * @throws IOException If something goes wrong while <code>queryIn</code> 
     *                     is parsed.
     * @todo make more efficient by short-circuiting the search.  
     */
    public static String selectSingleString(final Document docIn, 
                                            final String queryIn)
            throws XPathException, IOException {
        
        Iterator iter = selectStringIterator(
                docIn, (queryIn.charAt(0) == '/') ? queryIn : "/" + queryIn);
        
        return (iter.hasNext()) ? ((Node) iter.next()).getNodeValue() : null;
    }
    
    /** 
     * Evaluates the given xpath query(<code>queryIn</code>) against the given
     * <code>Element</code> instance and returns the first <code>String</code>
     * or <code>null</code>.
     * 
     * @param elemIn The context <code>Element</code> instance.
     * @param queryIn The xpath query to evaluate.
     * @return the first element that matches the relative XPath expression with
     * respect to the context element, or null if there is no match.
     * @throws XPathException If the given xpath query is not valid.
     * @throws IOException If something goes wrong while <code>queryIn</code> 
     *                     is parsed.
     * @todo make more efficient by short-circuiting the search.  
     */
    public static String selectSingleString(final Element elemIn, 
                                            final String queryIn)
            throws XPathException, IOException {
        
        Iterator iter = selectStringIterator(elemIn, queryIn);
                
        return (iter.hasNext()) ? ((Node) iter.next()).getNodeValue() : null;
    }
    
    /**
     * Evaluates the given <code>queryIn</code> against the 
     * <code>Document</code> referenced by <code>docIn</code>.
     * 
     * @param docIn The context <code>Document</code> instance.
     * @param queryIn The xpath query to evaluate. 
     * @return All the strings in the document that match the absolute XPath 
     *         expression.
     * @throws XPathException If the given xpath query is not valid.
     * @throws IOException If something goes wrong while <code>queryIn</code> 
     *                     is parsed. 
     */
    public static Iterator selectStringIterator(final Document docIn,
                                                final String queryIn) 
            throws XPathException, IOException {
        
        XPathI xpath = AbstractXPathFactory.getInstance().createXPath(
                (queryIn.charAt(0) == '/') ? queryIn : "/" + queryIn);
        if (!xpath.isStringValue()) {
            throw new XPathExceptionExt(xpath, "\"" + queryIn
                    + "\" evaluates to element not string");
        }
        XPathVisitor visitor = new XPathVisitor(docIn, xpath);
        return visitor.getResult();
    }
    
    /** 
     * Evaluates the given xpath query(<code>queryIn</code>) against the given
     * <code>Element</code> instance.
     * 
     * @param elemIn The context <code>Element</code> instance.
     * @param queryIn The xpath query to evaluate.
     * @return All the strings that match the relative XPath expression with 
     *         respect to the context element.
     * @throws XPathException If the given xpath query is not valid.
     * @throws IOException If something goes wrong while <code>queryIn</code> 
     *                     is parsed. 
     */
    public static Iterator selectStringIterator(final Element elemIn,
                                                final String queryIn) 
            throws XPathException, IOException {
        
        XPathI xpath = AbstractXPathFactory.getInstance().createXPath(queryIn);
        if (!xpath.isStringValue()) {
            throw new XPathExceptionExt(xpath, "\"" + queryIn
                    + "\" evaluates to element not string");
        }
        
        XPathVisitor visitor = new XPathVisitor(elemIn, xpath);
        return visitor.getResult();
    }

}

// $Log: XPathAPI.java,v $
// Revision 1.2  2005/05/11 17:31:40  nexd
// Refactoring and extended test cases
//
// Revision 1.1  2005/05/08 11:59:33  nexd
// restructuring
//
// Revision 1.2  2005/04/24 15:00:27  nexd
// Bugfixes and many performance and coding improvements.
//
// Revision 1.1  2005/02/04 13:07:21  nexd
// Added support for XPath queries against the NEXD DOM model.
//
// Revision 1.3  2003/12/08 21:13:01  eobrain
// Keep PMD happy by not changing value of parameter.
//
// Revision 1.2  2002/12/13 23:49:24  eobrain
// Fix javadoc.
//
// Revision 1.1.1.1  2002/08/19 05:04:23  eobrain
// import from HP Labs internal CVS
//
// Revision 1.6  2002/08/19 00:19:14  eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.5  2002/08/18 04:27:22  eob
// Remove deprecated method.
//
// Revision 1.4  2002/05/23 21:08:23  eob
// Better error reporting.
//
// Revision 1.3  2002/03/25 22:50:49  eob
// Move XPath object caching to XPath class.
//
// Revision 1.2  2002/02/04 22:05:48  eob
// Add handling of attribute xpath expressions that return strings.
//
// Revision 1.1  2002/02/01 19:20:40  eob
// initial
