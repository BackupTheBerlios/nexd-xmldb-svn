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
package de.xplib.nexd.engine.xapi.services;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.exceptions.InvalidQueryException;
import org.sixdml.exceptions.NonWellFormedXMLException;
import org.sixdml.exceptions.UpdateTypeMismatchException;
import org.sixdml.update.SixdmlUpdateService;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.xml.dom.xpath.XPathAPI;
import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;
import de.xplib.nexd.xml.DOMDocumentI;
import de.xplib.nexd.xml.DOMNodeI;
import de.xplib.nexd.xml.xpath.XPathException;

/**
 * UpdateServiceImpl.java
 *
 * This is a Service that enables the execution of updates within the context of
 * a Collection or against the documents stored in the Collection.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class UpdateServiceImpl 
    extends AbstractNamespaceService 
    implements SixdmlUpdateService {

    /**
     * Construtor.
     * 
     * @param engineIn The associated <code>NEXDEngineI</code> instance.
     * @param ctxCollIn The context <code>SixdmlCollection</code> instance.
     */
    public UpdateServiceImpl(final NEXDEngineI engineIn, 
                             final SixdmlCollection ctxCollIn) {
        
        super(engineIn, ctxCollIn, "SixdmlUpdateService", DEFAULT_VERSION);
    }

    /**
     * Inserts an XML fragment as a sibling of each of the nodes returned from 
     * the XPath query. A boolean flag indicates whether the new node will be 
     * inserted before or after the selected nodes.
     *      
     * @param queryIn The query which selects the nodes to update.
     * @param fragmentIn The XML fragment to insert.
     * @param resIn The document to perform the update against. 
     * @param beforeIn If this is true then the new node wil be inserted before
     *                 each of the selected node while if it is false the new 
     *                 node will be inserted after the selected nodes in the 
     *                 tree.  
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is the document
     *                                        root and an attempt is made to 
     *                                        insert anything besides comments 
     *                                        or processing instructions. 
     * @exception NonWellFormedXMLException If the XML fragment is not a valid 
     *                                      XML fragment.
     * @see org.sixdml.update.SixdmlUpdateService#insertSibling(
     *      java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlResource, boolean)
     */
    public int insertSibling(final String queryIn, 
                             final String fragmentIn,
                             final SixdmlResource resIn, 
                             final boolean beforeIn) 
            throws XMLDBException,
                   InvalidQueryException,
                   NonWellFormedXMLException,
                   UpdateTypeMismatchException {
        
        return this.insertSibling(
                queryIn, this.parseFragment(fragmentIn), resIn, beforeIn);
    }

    /**
     * Inserts an XML fragment as a child of each of the nodes returned from the
     * XPath query. A boolean flag indicates whether the new node will be 
     * inserted before or after the selected nodes. The new node is appended to 
     * the end of the current children of the selected node.
     *  
     * @param queryIn The query which selects the nodes to update.
     * @param fragmentIn The XML fragment to insert.
     * @param resIn The document to perform the update against. 
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException  If the target node is not an 
     *                                         element node, document node or 
     *                                         set of element nodes. 
     * @exception NonWellFormedXMLException If the XML fragment is not a valid 
     *                                      XML fragment.
     * @see org.sixdml.update.SixdmlUpdateService#insertChild(
     *      java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlResource)
     */
    public int insertChild(final String queryIn, 
                           final String fragmentIn,
                           final SixdmlResource resIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   NonWellFormedXMLException,
                   UpdateTypeMismatchException {

        return this.insertChild(queryIn, this.parseFragment(fragmentIn), resIn);
    }

    /**
     * Inserts a node as a sibling of each of the nodes returned from the XPath 
     * query. A boolean flag indicates whether the new node will be inserted 
     * before or after the selected nodes.
     *      
     * @param queryIn The query which selects the nodes to update.
     * @param nodesIn The DOM node(s) to insert.
     * @param resIn The document to perform the update against. 
     * @param beforeIn If this is true then the new node wil be inserted before 
     *                 each of the selcted node while if it is false the new 
     *                 node will be inserted after the selected nodes in the 
     *                 tree.  
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is the document
     *                                        root and an attempt is made to 
     *                                        insert anything besides comments 
     *                                        or processing instructions.
     * @see org.sixdml.update.SixdmlUpdateService#insertSibling(
     *      java.lang.String, org.w3c.dom.NodeList, 
     *      org.sixdml.dbmanagement.SixdmlResource, boolean)
     */
    public int insertSibling(final String queryIn, 
                             final NodeList nodesIn,
                             final SixdmlResource resIn, 
                             final boolean beforeIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   UpdateTypeMismatchException {
        
        Node node = resIn.getContentAsDOM();
        if (!(node instanceof DOMNodeI)) {
            throw new XMLDBException(
                    ErrorCodes.VENDOR_ERROR, 
                    "Expected a DOM node and not " + node.getClass() + ".");
        }
        
        int count = 0;
        
        try {
            
            NodeList list = de.xplib.nexd.engine.xml.dom.xpath.XPathAPI.eval(
                    (Document) node, queryIn);
            
            count = list.getLength();
            
            // check compatibility.
            for (int i = 0; i < count; i++) {
                
                Node curr = list.item(i);
                if (curr == curr.getOwnerDocument().getDocumentElement()) {
                    // document element allows only comment an pi
                    for (int j = 0, size = nodesIn.getLength(); j < size; j++) {
                        Node ins = nodesIn.item(j);
                        if (!(ins instanceof ProcessingInstruction) 
                                && !(ins instanceof Comment)) {
                            throw new UpdateTypeMismatchException(
                                    "Can only insert pi and comment.");
                        }
                    }
                }
            }
            
            Document doc = (Document) node;
            
            // insert new items
            for (int i = 0; i < count; i++) {
                Node curr = list.item(i);
                for (int j = 0, size = nodesIn.getLength(); j < size; j++) {
                    Node ins = doc.importNode(nodesIn.item(j), true);
                    if (beforeIn) {
                        curr.getParentNode().insertBefore(ins, curr);
                    } else {
                        curr.getParentNode().insertBefore(
                                ins, curr.getNextSibling());
                    }
                }
            }
        }  catch (XPathException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        } catch (IOException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        }
        this.engine.updateResource(this.ctxColl, resIn);

        resIn.setContentAsDOM(
                ((XMLResource) this.engine.queryResource(
                        this.ctxColl, resIn.getId())).getContentAsDOM());

        return count;
    }

    /**
     * Inserts a node as a child of each of the nodes returned from the XPath 
     * query. A boolean flag indicates whether the new node will be inserted 
     * before or after the selected nodes. The new node is appended to the end 
     * of the current children of the selected node.
     *  
     * @param queryIn The query which selects the nodes to update.
     * @param nodesIn The DOM node(s) to insert.
     * @param resIn the document to perform the update against. 
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException  If the target node is not an 
     *                                         element node, document node or 
     *                                         set of element nodes. 
     * @see org.sixdml.update.SixdmlUpdateService#insertChild(
     *      java.lang.String, org.w3c.dom.NodeList, 
     *      org.sixdml.dbmanagement.SixdmlResource)
     */
    public int insertChild(final String queryIn, 
                           final NodeList nodesIn, 
                           final SixdmlResource resIn)
            throws XMLDBException, 
                   InvalidQueryException,
                   UpdateTypeMismatchException {
        
        Node node = resIn.getContentAsDOM();
        if (!(node instanceof DOMNodeI)) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR);
        }
        
        int count = 0;
        
        try {
            
            Document doc = (Document) node;
            
            NodeList list = XPathAPI.eval(doc, queryIn);
            
            count = list.getLength();

            // check nodes to be ok
            for (int i = 0; i < count; i++) {
                if (!(list.item(i) instanceof Element)) {
                    throw new UpdateTypeMismatchException(
                            "Only Element nodes can accept child nodes.");
                }
            }
            
            // insert the nodes in 
            for (int i = 0; i < count; i++) {
                for (int j = 0, size = nodesIn.getLength(); j < size; j++) {
                    list.item(i).appendChild(
                            doc.importNode(nodesIn.item(j), true));
                }
            }
        } catch (XPathException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        } catch (IOException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        }
        
        this.engine.updateResource(this.ctxColl, resIn);

        resIn.setContentAsDOM(
                ((XMLResource) this.engine.queryResource(
                        this.ctxColl, resIn.getId())).getContentAsDOM());

        return count;
    }

    /**
     * Inserts an attribute node as a child of each of the nodes returned from 
     * the XPath query.     
     * 
     * @param queryIn The query which selects the nodes to update.
     * @param resIn the document to perform the update against. 
     * @param nameIn The name of the attribute to insert. 
     * @param valueIn the value of the attribute to insert.  
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is not an 
     *                                        element node or set of element 
     *                                        nodes.
     * @see org.sixdml.update.SixdmlUpdateService#insertAttribute(
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlResource, 
     *      java.lang.String, java.lang.String)
     */
    public int insertAttribute(final String queryIn, 
                               final SixdmlResource resIn,
                               final String nameIn, 
                               final String valueIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   UpdateTypeMismatchException {
        
        Node node = resIn.getContentAsDOM();
        if (!(node instanceof DOMNodeI)) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR);
        }
        
        int count = 0;
        
        try {
            
            NodeList list = XPathAPI.eval((Document) node, queryIn);
            
            count = list.getLength();
            
            // check that all nodes are elements
            for (int i = 0; i < count; i++) {
                if (!(list.item(i) instanceof Element)) {
                    throw new UpdateTypeMismatchException(
                            "Attributes can only be added to Element nodes.");
                }
            }
            
            for (int i = 0; i < count; i++) {
                ((Element) list.item(i)).setAttribute(nameIn, valueIn);
            }
            
        }  catch (XPathException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        } catch (IOException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        }
        
        this.engine.updateResource(this.ctxColl, resIn);
        
        resIn.setContentAsDOM(
                ((XMLResource) this.engine.queryResource(
                        this.ctxColl, resIn.getId())).getContentAsDOM());
        
        return count;
    }

    /**
     * Inserts an attribute node as a child of each of the nodes returned from 
     * the XPath query.
     *      
     * @param queryIn The query which selects the nodes to update.
     * @param resIn The document to perform the update against. 
     * @param nameIn The name of the attribute to insert. It should be an XML 
     *               qualified name. 
     * @param valueIn The value of the attribute to insert.     
     * @param namespaceUriIn The namsepace URI for the attribute node
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException  If the target node is not an 
     *                                         element node or set of element 
     *                                         nodes. 
     * @see org.sixdml.update.SixdmlUpdateService#insertAttribute(
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlResource, 
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public int insertAttribute(final String queryIn, 
                               final SixdmlResource resIn,
                               final String nameIn, 
                               final String valueIn, 
                               final String namespaceUriIn)
            throws XMLDBException, 
                   InvalidQueryException,
                   UpdateTypeMismatchException {
        
        // We don't support namespace at the moment.
        return this.insertAttribute(queryIn, resIn, nameIn, valueIn);
    }

    /**
     * Deletes all nodes that match the expression from the target document.
     *  
     * @param queryIn The query which selects the nodes to delete. 
     * @param resIn The document to perform the update against 
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid.
     * @exception UpdateTypeMismatchException If no nodes matching the query 
     *                                        are found.
     * @see org.sixdml.update.SixdmlUpdateService#delete(
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlResource)
     */
    public int delete(final String queryIn, final SixdmlResource resIn)
            throws XMLDBException, 
                   InvalidQueryException,
                   UpdateTypeMismatchException {
        
        Node node = resIn.getContentAsDOM();
        if (!(node instanceof DOMNodeI)) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR);
        }
        
        int count = 0;
        
        try {
            NodeList list = XPathAPI.eval((Document) node, queryIn);
            
            count = list.getLength();

            for (int i = 0; i < count; i++) {
                Node item = list.item(i);
                if (item instanceof Attr) {
                    Attr attr = (Attr) item;
                    ((Element) attr.getOwnerElement()).removeAttributeNode(
                            attr);
                } else {
                    item.getParentNode().removeChild(item);
                }
            }
        } catch (XPathException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        } catch (IOException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        }
        
        this.engine.updateResource(this.ctxColl, resIn);
        
        resIn.setContentAsDOM(
                ((XMLResource) this.engine.queryResource(
                        this.ctxColl, resIn.getId())).getContentAsDOM());
        
        return count;
    }

    /**
     * Replaces one or more element, processing-instruction, comment, or text 
     * nodes with the new node.
     * 
     * @param queryIn The query which selects the nodes to update.
     * @param nodesIn The DOM node(s) to insert.
     * @param resIn the document to perform the update against. 
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is not one or 
     *                                        more comment, text, 
     *                                        processing-instruction, or element
     *                                        nodes. 
     * @see org.sixdml.update.SixdmlUpdateService#replace(
     *      java.lang.String, org.w3c.dom.NodeList, 
     *      org.sixdml.dbmanagement.SixdmlResource)
     */
    public int replace(final String queryIn, 
                       final NodeList nodesIn, 
                       final SixdmlResource resIn)
            throws XMLDBException, 
                   InvalidQueryException,
                   UpdateTypeMismatchException {
        
        Node node = resIn.getContentAsDOM();
        if (!(node instanceof DOMNodeI)) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR);
        }
        
        int count = 0;
        
        try {
            
            Document doc = (Document) node;
            
            NodeList list = XPathAPI.eval(doc, queryIn);
            
            count = list.getLength();

            // check compatibility.
            for (int i = 0; i < count; i++) {
                
                Node curr = list.item(i);
                
                if (!(curr instanceof Comment) && !(curr instanceof Text)
                        && !(curr instanceof ProcessingInstruction)
                        && !(curr instanceof Element)) {
                    
                    throw new UpdateTypeMismatchException(
                            "Invalid replace type, allowed are Comment, Text, "
                          + "ProcessingInstruction and Element nodes.");
                }
                
                if (curr == curr.getOwnerDocument().getDocumentElement()) {
                    // document element allows only comment an pi
                    for (int j = 0, size = nodesIn.getLength(); j < size; j++) {
                        Node ins = nodesIn.item(j);
                        if (!(ins instanceof ProcessingInstruction) 
                                && !(ins instanceof Comment)
                                && !(ins instanceof Element)) {
                            throw new UpdateTypeMismatchException(
                                    "Can only insert pi and comment.");
                        }
                    }
                }
            }
            
            Node parent = null;

            // replace nodes.
            for (int i = 0; i < count; i++) {
                Node replace = list.item(i);
                
                if (parent == replace.getParentNode()) {
                    continue;
                }
                parent = replace.getParentNode();
                for (int j = 0, size = nodesIn.getLength(); j < size; j++) {
                    parent.insertBefore(
                            doc.importNode(
                                    nodesIn.item(j), true).cloneNode(true), 
                            replace);
                }

                parent.removeChild(replace);
            }
        } catch (XPathException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        } catch (IOException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        }
        this.engine.updateResource(this.ctxColl, resIn);
        
        resIn.setContentAsDOM(
                ((XMLResource) this.engine.queryResource(
                        this.ctxColl, resIn.getId())).getContentAsDOM());

        return count;
    }

    /**
     * Replaces one or more element, processing-instruction, comment, or text 
     * nodes with the new node.
     * 
     * @param queryIn The query which selects the nodes to update.
     * @param fragmentIn the XML fragment to insert. 
     * @param resIn The document to perform the update against. 
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is not one or 
     *                                        more comment, text, 
     *                                        processing-instruction, or element
     *                                        nodes. 
     * @exception NonWellFormedXMLException If the XML fragment is not a valid
     *                                      XML fragment.
     * @see org.sixdml.update.SixdmlUpdateService#replace(
     *      java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlResource)
     */
    public int replace(final String queryIn, 
                       final String fragmentIn, 
                       final SixdmlResource resIn)
            throws XMLDBException, 
                   InvalidQueryException,
                   NonWellFormedXMLException, 
                   UpdateTypeMismatchException {
        
        return this.replace(queryIn, this.parseFragment(fragmentIn), resIn);
    }

    /**
     * Renames one or more attribute or element nodes.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param nameIn
     *            The new name
     * @param namespaceUriIn
     *            the namespace URI for the element being inserted. If it is
     *            null then it is assumed the new item has no namespace URI.
     * @param resIn
     *            the document to perform the update against
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not one or more attribute or element
     *                nodes.
     * @see org.sixdml.update.SixdmlUpdateService#rename(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.sixdml.dbmanagement.SixdmlResource)
     */
    public int rename(final String queryIn, 
                      final String nameIn, 
                      final String namespaceUriIn,
                      final SixdmlResource resIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   UpdateTypeMismatchException {
        
        Node node = resIn.getContentAsDOM();
        if (!(node instanceof DOMNodeI)) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR);
        }
        
        int count = 0;
        
        try {
            
            DOMDocumentI doc = (DOMDocumentI) node; //.getOwnerDocument();
            
            NodeList list = XPathAPI.eval(doc, queryIn);
            
            count = list.getLength();

            // check compatibility.
            for (int i = 0; i < count; i++) {
                Node curr = list.item(i);
                if (!(curr instanceof Attr) && !(curr instanceof Element)) {
                    throw new UpdateTypeMismatchException(
                            "Invalid rename type, allowed are Attr and Element "
                          + "nodes, not [" + curr.getClass() + "].");
                }
            }
            
            for (int i = 0; i < count; i++) {
                doc.renameNode(list.item(i), namespaceUriIn, nameIn);
            }
            
        } catch (XPathException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        } catch (IOException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        }
        
        this.engine.updateResource(this.ctxColl, resIn);
        
        resIn.setContentAsDOM(
                ((XMLResource) this.engine.queryResource(
                        this.ctxColl, resIn.getId())).getContentAsDOM());
        
        return count;
    }

    /**
     * Inserts an XML fragment as a sibling of each of the nodes returned from 
     * the XPath query. A boolean flag indicates whether the new node will be 
     * inserted before or after the selected nodes.
     *      
     * @param queryIn The query which selects the nodes to update.
     * @param fragmentIn The XML fragment to insert.
     * @param collIn The collection whose documents to perform the update 
     *               against. 
     * @param beforeIn If this is true then the new node wil be inserted before 
     *                 each of the selected node while if it is false the new 
     *                 node will be inserted after the selected nodes in the 
     *                 tree.  
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is not one or 
     *                                        more comment, text, 
     *                                        processing-instruction, or element
     *                                        nodes. 
     * @exception NonWellFormedXMLException If the XML fragment is not a valid
     *                                      XML fragment.
     * @see org.sixdml.update.SixdmlUpdateService#insertSibling(
     *      java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlCollection, boolean)
     */
    public int insertSibling(final String queryIn, 
                             final String fragmentIn,
                             final SixdmlCollection collIn, 
                             final boolean beforeIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   NonWellFormedXMLException,
                   UpdateTypeMismatchException {
        
        int count = 0;
        
        NodeList list = this.parseFragment(fragmentIn);
        
        String[] ids = this.engine.queryResources(collIn);
        for (int i = 0; i < ids.length; i++) {
            Resource res = this.engine.queryResource(collIn, ids[i]);
            if (res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                count += this.insertSibling(
                        queryIn, list, (SixdmlResource) res, beforeIn);
            }
        }

        return count;
    }

    /**
     * Inserts an XML fragment as a child of each of the nodes returned from the
     * XPath query. A boolean flag indicates whether the new node will be 
     * inserted before or after the selected nodes. The new node is appended to 
     * the end of the current children of the selected node.
     *  
     * @param queryIn The query which selects the nodes to update.
     * @param fragmentIn The XML fragment to insert.
     * @param collIn The collection whose documents to perform the update 
     *               against. 
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is not an 
     *                                        element node, document node
     *                                        or set of element nodes. 
     * @exception NonWellFormedXMLException If the XML fragment is not a valid 
     *                                      XML fragment.
     * @see org.sixdml.update.SixdmlUpdateService#insertChild(
     *      java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int insertChild(final String queryIn, 
                           final String fragmentIn,
                           final SixdmlCollection collIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   NonWellFormedXMLException,
                   UpdateTypeMismatchException {
                
        NodeList list = this.parseFragment(fragmentIn);
        return this.insertChild(queryIn, list, collIn);
    }

    /**
     * Inserts a node as a sibling of each of the nodes returned from the XPath
     * query. A boolean flag indicates whether the new node will be inserted
     * before or after the selected nodes.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param nodesIn
     *            The DOM node(s) to insert
     * @param collIn
     *            the collection whose documents to perform the update against
     * @param beforeIn
     *            if this is true then the new node wil be inserted before each
     *            of the selcted node while if it is false the new node will be
     *            inserted after the selected nodes in the tree.
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is the document root and an attempt is
     *                made to insert anything besides comments or processing
     *                instructions.
     * @see org.sixdml.update.SixdmlUpdateService#insertSibling(
     *      java.lang.String, org.w3c.dom.NodeList, 
     *      org.sixdml.dbmanagement.SixdmlCollection, boolean)
     */
    public int insertSibling(final String queryIn, 
                             final NodeList nodesIn,
                             final SixdmlCollection collIn, 
                             final boolean beforeIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   UpdateTypeMismatchException {
        
        int count    = 0;
        String[] ids = this.engine.queryResources(collIn);
        for (int i = 0; i < ids.length; i++) {
            Resource res = this.engine.queryResource(collIn, ids[i]);
            if (res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                count += this.insertSibling(
                        queryIn, nodesIn, (SixdmlResource) res, beforeIn);
            }
        }
        return count;
    }

    /**
     * Inserts a node as a child of each of the nodes returned from the XPath
     * query. A boolean flag indicates whether the new node will be inserted
     * before or after the selected nodes. The new node is appended to the end
     * of the current children of the selected node.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param nodesIn
     *            The DOM node(s) to insert
     * @param collIn
     *            the collection whose documents to perform the update against
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not an element node, document node
     *                or set of element nodes.
     * @see org.sixdml.update.SixdmlUpdateService#insertChild(java.lang.String,
     *      org.w3c.dom.NodeList, org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int insertChild(final String queryIn, 
                           final NodeList nodesIn,
                           final SixdmlCollection collIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   UpdateTypeMismatchException {
        
        int count = 0;
        
        String[] ids = this.engine.queryResources(collIn);
        for (int i = 0; i < ids.length; i++) {
            Resource res = this.engine.queryResource(collIn, ids[i]);
            if (res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                count += this.insertChild(
                        queryIn, nodesIn, (SixdmlResource) res);
            }
        }
        return count;
    }

    /**
     * Inserts an attribute node as a child of each of the nodes returned from
     * the XPath query.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param collIn
     *            the collection whose documents to perform the update against
     * @param nameIn
     *            The name of the attribute to insert.
     * @param valueIn
     *            the value of the attribute to insert.
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not an element node or set of
     *                element nodes.
     * @see org.sixdml.update.SixdmlUpdateService#insertAttribute(
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlCollection, 
     *      java.lang.String, java.lang.String)
     */
    public int insertAttribute(final String queryIn, 
                               final SixdmlCollection collIn,
                               final String nameIn, 
                               final String valueIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   UpdateTypeMismatchException {
        
        int count    = 0;
        String[] ids = this.engine.queryResources(collIn);
        for (int i = 0; i < ids.length; i++) {
            Resource res = this.engine.queryResource(collIn, ids[i]);
            if (res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                count += this.insertAttribute(
                        queryIn, (SixdmlResource) res, nameIn, valueIn);
            }
        }
        return count;
    }

    /**
     * Inserts an attribute node as a child of each of the nodes returned from
     * the XPath query.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param collIn
     *            the collection whose documents to perform the update against
     * @param nameIn
     *            The name of the attribute to insert. It should be an XML
     *            qualified name.
     * @param valueIn
     *            the value of the attribute to insert.
     * @param namespaceUriIn
     *            the namsepace URI for the attribute node
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not an element node or set of
     *                element nodes.
     * @see org.sixdml.update.SixdmlUpdateService#insertAttribute(
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlCollection, 
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public int insertAttribute(final String queryIn, 
                               final SixdmlCollection collIn,
                               final String nameIn, 
                               final String valueIn, 
                               final String namespaceUriIn)
            throws XMLDBException, 
                   InvalidQueryException,
                   UpdateTypeMismatchException {

        return this.insertAttribute(queryIn, collIn, nameIn, valueIn);
    }

    /**
     * Deletes all nodes that match the expression from the target document.
     *  
     * @param queryIn The query which selects the nodes to delete. 
     * @param collIn The collection whose documents to perform the update 
     *               against
     * @return The number of nodes affected by the update.
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid.  
     * @see org.sixdml.update.SixdmlUpdateService#delete(
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int delete(final String queryIn, final SixdmlCollection collIn)
            throws XMLDBException, InvalidQueryException {
        
        return this.delete(queryIn, null, collIn);
    }

    /**
     * Replaces one or more element, processing-instruction, comment, or text
     * nodes with the new node.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param nodesIn
     *            The DOM node(s) to insert
     * @param collIn
     *            the collection whose documents to perform the update against
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not one or more comment, text,
     *                processing-instruction, or element nodes.
     * @see org.sixdml.update.SixdmlUpdateService#replace(java.lang.String,
     *      org.w3c.dom.NodeList, org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int replace(final String queryIn, 
                       final NodeList nodesIn, 
                       final SixdmlCollection collIn)
            throws XMLDBException, 
                   InvalidQueryException,
                   UpdateTypeMismatchException {
        
        int count    = 0;
        String[] ids = this.engine.queryResources(collIn);
        for (int i = 0; i < ids.length; i++) {
            Resource res = this.engine.queryResource(collIn, ids[i]);
            if (res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                count += this.replace(queryIn, nodesIn, (SixdmlResource) res);
            }
        }

        return count;
    }

    /**
     * Replaces one or more element, processing-instruction, comment, or text
     * nodes with the new node.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param fragmentIn
     *            the XML fragment to insert.
     * @param collIn
     *            the collection whose documents to perform the update against
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not one or more comment, text,
     *                processing-instruction, or element nodes.
     * @exception NonWellFormedXMLException
     *                if the XML fragment is not a valid XML fragment.
     * @see org.sixdml.update.SixdmlUpdateService#replace(java.lang.String,
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int replace(final String queryIn, 
                       final String fragmentIn,
                       final SixdmlCollection collIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   NonWellFormedXMLException,
                   UpdateTypeMismatchException {

        NodeList nodes = this.parseFragment(fragmentIn);
        return this.replace(queryIn, nodes, collIn);
    }

    /**
     * Renames one or more attribute or element nodes.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param nameIn
     *            The new name
     * @param namespaceUriIn
     *            the namespace URI for the element being inserted. If it is
     *            null then it is assumed the new item has no namespace URI.
     * @param collIn
     *            the collection whose documents to perform the update against
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not one or more comment, text,
     *                processing-instruction, or element nodes.
     * @see org.sixdml.update.SixdmlUpdateService#rename(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int rename(final String queryIn, 
                      final String nameIn, 
                      final String namespaceUriIn,
                      final SixdmlCollection collIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   UpdateTypeMismatchException {
        
        int count = 0;
        String[] ids = this.engine.queryResources(collIn);
        for (int i = 0; i < ids.length; i++) {
            Resource res = this.engine.queryResource(collIn, ids[i]);
            if (res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                count += this.rename(
                        queryIn, nameIn, namespaceUriIn, (SixdmlResource) res);
            }
        }
        return count;
    }

    /**
     * Inserts an XML fragment as a child of each of the nodes returned from the
     * XPath query. The new node is appended to the end of the current children
     * of the selected node.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param fragmentIn
     *            The XML fragment to insert
     * @param collIn
     *            the collection whose documents to perform the update against
     * @param predicateIn
     *            a filter expression used to determine which documents to
     *            update within the collection.
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not an element node, document node
     *                or set of element nodes.
     * @exception NonWellFormedXMLException
     *                if the XML fragment is not a valid XML fragment.
     * @see org.sixdml.update.SixdmlUpdateService#insertChild(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int insertChild(final String queryIn, 
                           final String predicateIn, 
                           final String fragmentIn,
                           final SixdmlCollection collIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   NonWellFormedXMLException,
                   UpdateTypeMismatchException {

        return this.insertChild(queryIn, fragmentIn, collIn);
    }

    /**
     * Inserts a node as a child of each of the nodes returned from the XPath
     * query. The new node is appended to the end of the current children of the
     * selected node.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param nodesIn
     *            The DOM node(s) to insert
     * @param collIn
     *            the collection whose documents to perform the update against
     * @param predicateIn
     *            a filter expression used to determine which documents to
     *            update within the collection.
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not an element node, document node
     *                or set of element nodes.
     * @see org.sixdml.update.SixdmlUpdateService#insertChild(java.lang.String,
     *      java.lang.String, org.w3c.dom.NodeList,
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int insertChild(final String queryIn, 
                           final String predicateIn, 
                           final NodeList nodesIn,
                           final SixdmlCollection collIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   UpdateTypeMismatchException {

        return this.insertChild(queryIn, nodesIn, collIn);
    }

    /**
     * Inserts a node as a sibling of each of the nodes returned from the XPath
     * query. A boolean flag indicates whether the new node will be inserted
     * before or after the selected nodes.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param nodesIn
     *            The DOM node(s) to insert
     * @param collIn
     *            the collection whose documents to perform the update against
     * @param beforeIn
     *            if this is true then the new node wil be inserted before each
     *            of the selcted node while if it is false the new node will be
     *            inserted after the selected nodes in the tree.
     * @param predicateIn
     *            a filter expression used to determine which documents to
     *            update within the collection.
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is the document root and an attempt is
     *                made to insert anything besides comments or processing
     *                instructions.
     * @see org.sixdml.update.SixdmlUpdateService#insertSibling(
     *      java.lang.String, java.lang.String, org.w3c.dom.NodeList, 
     *      org.sixdml.dbmanagement.SixdmlCollection, boolean)
     */
    public int insertSibling(final String queryIn, 
                             final String predicateIn, 
                             final NodeList nodesIn,
                             final SixdmlCollection collIn, 
                             final boolean beforeIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   UpdateTypeMismatchException {

        return this.insertSibling(queryIn, nodesIn, collIn, beforeIn);
    }

    /**
     * Inserts an XML fragment as a sibling of each of the nodes returned from 
     * the XPath query. A boolean flag indicates whether the new node will be 
     * inserted before or after the selected nodes.
     *      
     * @param queryIn The query which selects the nodes to update.
     *   
     * @param predicateIn A filter expression used to determine which documents
     *                    to update within the collection.
     * @param fragmentIn The XML fragment to insert.
     * @param collIn The collection whose documents to perform the update 
     *               against. 
     * @param beforeIn If this is true then the new node wil be inserted before 
     *                 each of the selected node while if it is false the new 
     *                 node will be inserted after the selected nodes in the 
     *                 tree. 
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is the document
     *                                        root and an attempt is made to 
     *                                        insert anything besides comments 
     *                                        or processing instructions. 
     * @exception NonWellFormedXMLException If the XML fragment is not a valid 
     *                                      XML fragment.
     * @see org.sixdml.update.SixdmlUpdateService#insertSibling(
     *      java.lang.String, java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlCollection, boolean)
     */
    public int insertSibling(final String queryIn, 
                             final String predicateIn, 
                             final String fragmentIn,
                             final SixdmlCollection collIn, 
                             final boolean beforeIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   NonWellFormedXMLException,
                   UpdateTypeMismatchException {
        
        NodeList nodes = this.parseFragment(fragmentIn);
        return this.insertSibling(
                queryIn, predicateIn, nodes, collIn, beforeIn);
    }

    /**
     * Inserts an attribute node as a child of each of the nodes returned from 
     * the XPath query.
     *      
     * @param queryIn The query which selects the nodes to update.  
     * @param predicateIn A filter expression used to determine which documents 
     *                    to update within the collection.
     * @param collIn The collection whose documents to perform the update 
     *               against. 
     * @param nameIn The name of the attribute to insert. 
     * @param valueIn the value of the attribute to insert. 
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is not an 
     *                                        element node or set of element 
     *                                        nodes. 
     * @see org.sixdml.update.SixdmlUpdateService#insertAttribute(
     *      java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlCollection, 
     *      java.lang.String, java.lang.String)
     */
    public int insertAttribute(final String queryIn, 
                               final String predicateIn,
                               final SixdmlCollection collIn, 
                               final String nameIn, 
                               final String valueIn)
            throws XMLDBException, 
                   InvalidQueryException,
                   UpdateTypeMismatchException {

        return this.insertAttribute(queryIn, collIn, nameIn, valueIn);
    }

    /**
     * Inserts an attribute node as a child of each of the nodes returned from 
     * the XPath query.
     *      
     * @param queryIn The query which selects the nodes to update
     * @param predicateIn A filter expression used to determine which documents
     *                    to update within the collection.
     * @param collIn the collection whose documents to perform the update 
     *               against. 
     * @param nameIn The name of the attribute to insert. It should be an 
     *               XML qualified name. 
     * @param valueIn The value of the attribute to insert.     
     * @param namespaceUriIn The namsepace URI for the attribute node. 
     * @return The number of nodes affected by the update. 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is not an 
     *                                        element node or set of element
     *                                        nodes. 
     * @see org.sixdml.update.SixdmlUpdateService#insertAttribute(
     *      java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlCollection, 
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public int insertAttribute(final String queryIn, 
                               final String predicateIn,
                               final SixdmlCollection collIn, 
                               final String nameIn, 
                               final String valueIn,
                               final String namespaceUriIn) 
            throws XMLDBException, 
                   InvalidQueryException,
                   UpdateTypeMismatchException {

        return this.insertAttribute(queryIn, collIn, nameIn, valueIn);
    }

    /**
     * Deletes all nodes that match the expression from the target document. 
     * 
     * @param queryIn The query which selects the nodes to delete. 
     * @param collIn The collection whose documents to perform the update
     *               against 
     * @param predicateIn A filter expression used to determine which documents
     *                  to update within the collection. 
     * @return the number of nodes affected by the update.
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid.
     * @see org.sixdml.update.SixdmlUpdateService#delete(
     *      java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int delete(final String queryIn, 
                      final String predicateIn,
                      final SixdmlCollection collIn) 
            throws XMLDBException,
                   InvalidQueryException {
        
        int count = 0;
        String[] ids = this.engine.queryCollections(collIn);
        for (int i = 0; i < ids.length; i++) {
            Resource res = this.engine.queryResource(collIn, ids[i]);
            if (res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                try {
                    count += this.delete(queryIn, (SixdmlResource) res);
                } catch (UpdateTypeMismatchException e) {
                    throw new XMLDBException(
                            ErrorCodes.UNKNOWN_ERROR, e.getMessage());
                }
            }
        }
        return count;
    }

    /**
     * Replaces one or more element, processing-instruction, comment, or text
     * nodes with the new node.
     * 
     * @param queryIn The query which selects the nodes to update
     * @param fragmentIn The XML fragment to insert. 
     * @param predicateIn A filter expression used to determine which documents 
     *                    to update within the collection. 
     * @param collIn The collection whose documents to perform the update 
     *               against 
     * @exception XMLDBException If a database error occurs during the 
     *                           insertion.
     * @exception InvalidQueryException If the XPath  query is invalid. 
     * @exception UpdateTypeMismatchException If the target node is not one or 
     *                                        more comment, text, 
     *                                        processing-instruction, or element
     *                                        nodes. 
     * @exception NonWellFormedXMLException If the XML fragment is not a valid 
     *                                      XML fragment. 
     * @return The number of nodes affected by the update.  
     * @see org.sixdml.update.SixdmlUpdateService#replace(
     *      java.lang.String, java.lang.String, java.lang.String, 
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int replace(final String queryIn, 
                       final String predicateIn, 
                       final String fragmentIn,
                       final SixdmlCollection collIn) 
            throws XMLDBException,
                   InvalidQueryException, 
                   NonWellFormedXMLException,
                   UpdateTypeMismatchException {

        return this.replace(queryIn, fragmentIn, collIn);
    }

    /**
     * Renames one or more attribute or element nodes.
     * 
     * @param queryIn
     *            The query which selects the nodes to update
     * @param nameIn
     *            The new name
     * @param namespaceUriIn
     *            the namespace URI for the element being inserted. If it is
     *            null then it is assumed the new item has no namespace URI.
     * @param collIn
     *            the collection whose documents to perform the update against
     * @param predicateIn
     *            a filter expression used to determine which documents to
     *            update within the collection.
     * @return the number of nodes affected by the update.
     * @exception XMLDBException
     *                if a database error occurs during the insertion.
     * @exception InvalidQueryException
     *                if the XPath query is invalid.
     * @exception UpdateTypeMismatchException
     *                if the target node is not one or more comment, text,
     *                processing-instruction, or element nodes.
     * @see org.sixdml.update.SixdmlUpdateService#rename(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int rename(final String queryIn, 
                      final String predicateIn, 
                      final String nameIn,
                      final String namespaceUriIn, 
                      final SixdmlCollection collIn)
            throws XMLDBException, 
                   InvalidQueryException,
                   UpdateTypeMismatchException {

        return this.rename(queryIn, nameIn, namespaceUriIn, collIn);
    }
    
    /**
     * Parses the given XML fragment into a <code>NodeList</code> instance.
     * @param fragmentIn The fragment to parse.
     * @return The parsed XML fragment as <code>NodeList</code>.
     * @throws NonWellFormedXMLException If the XML fragment isn't well formed.
     */
    private NodeList parseFragment(final String fragmentIn) 
            throws NonWellFormedXMLException {
        
        try {
            DocumentBuilder builder = 
                new DocumentBuilderFactoryImpl().newDocumentBuilder();
            
            return builder.parse(new InputSource(new StringReader(
                    "<r>" + fragmentIn + "</r>")))
                    .getDocumentElement().getChildNodes();
        } catch (ParserConfigurationException e) {
            throw new NonWellFormedXMLException(fragmentIn, e);
        } catch (SAXException e) {
            throw new NonWellFormedXMLException(e.getMessage(), e);
        } catch (IOException e) {
            throw new NonWellFormedXMLException(e.getMessage(), e);
        }
    }

}
