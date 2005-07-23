/*
 * Project: nexd 
 * Copyright (C) 2004  Manuel Pichler <manuel.pichler@xplib.de>
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
 * $Log: AbstractNode.java,v $
 * Revision 1.3  2005/05/30 19:17:07  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.8  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.7  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.6  2005/03/01 10:25:11  nexd
 * Advanced namespace and DOM 3 support.
 *
 */
package de.xplib.nexd.engine.xml.dom;

import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.xplib.nexd.store.InternalIdI;
import de.xplib.nexd.xml.DOMNodeI;

/**
 * The <code>Node</code> interface is the primary datatype for the entire 
 * Document Object Model. It represents a single node in the document tree. 
 * While all objects implementing the <code>Node</code> interface expose 
 * methods for dealing with children, not all objects implementing the 
 * <code>Node</code> interface may have children. For example, 
 * <code>Text</code> nodes may not have children, and adding children to 
 * such nodes results in a <code>DOMException</code> being raised.
 * <p>The attributes <code>nodeName</code>, <code>nodeValue</code> and 
 * <code>attributes</code> are included as a mechanism to get at node 
 * information without casting down to the specific derived interface. In 
 * cases where there is no obvious mapping of these attributes for a 
 * specific <code>nodeType</code> (e.g., <code>nodeValue</code> for an 
 * <code>Element</code> or <code>attributes</code> for a <code>Comment</code>
 * ), this returns <code>null</code>. Note that the specialized interfaces 
 * may contain additional and more convenient mechanisms to get and set the 
 * relevant information.
 * <p>The values of <code>nodeName</code>, 
 * <code>nodeValue</code>, and <code>attributes</code> vary according to the 
 * node type as follows: 
 * <table border='1'>
 * <tr>
 * <th>Interface</th>
 * <th>nodeName</th>
 * <th>nodeValue</th>
 * <th>attributes</th>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>Attr</td>
 * <td valign='top' rowspan='1' colspan='1'>name of 
 * attribute</td>
 * <td valign='top' rowspan='1' colspan='1'>value of attribute</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>CDATASection</td>
 * <td valign='top' rowspan='1' colspan='1'><code>"#cdata-section"</code></td>
 * <td valign='top' rowspan='1' colspan='1'>
 * content of the CDATA Section</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>Comment</td>
 * <td valign='top' rowspan='1' colspan='1'><code>"#comment"</code></td>
 * <td valign='top' rowspan='1' colspan='1'>content of 
 * the comment</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>Document</td>
 * <td valign='top' rowspan='1' colspan='1'><code>"#document"</code></td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>DocumentFragment</td>
 * <td valign='top' rowspan='1' colspan='1'>
 * <code>"#document-fragment"</code></td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>DocumentType</td>
 * <td valign='top' rowspan='1' colspan='1'>document type name</td>
 * <td valign='top' rowspan='1' colspan='1'>
 * null</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>Element</td>
 * <td valign='top' rowspan='1' colspan='1'>tag name</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * <td valign='top' rowspan='1' colspan='1'>NamedNodeMap</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>Entity</td>
 * <td valign='top' rowspan='1' colspan='1'>entity name</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>
 * EntityReference</td>
 * <td valign='top' rowspan='1' colspan='1'>name of entity referenced</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>Notation</td>
 * <td valign='top' rowspan='1' colspan='1'>notation name</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * <td valign='top' rowspan='1' colspan='1'>
 * null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>ProcessingInstruction</td>
 * <td valign='top' rowspan='1' colspan='1'>target</td>
 * <td valign='top' rowspan='1' colspan='1'>entire content excluding the target
 * </td><td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>Text</td>
 * <td valign='top' rowspan='1' colspan='1'>
 * <code>"#text"</code></td>
 * <td valign='top' rowspan='1' colspan='1'>content of the text node</td>
 * <td valign='top' rowspan='1' colspan='1'>null</td>
 * </tr>
 * </table> 
 * <p>See also the <a 
 * href='http://www.w3.org/TR/2002/WD-DOM-Level-3-Core-20020114'
 * >Document Object Model (DOM) Level 3 Core Specification</a>.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public abstract class AbstractNode implements DOMNodeI {
    
    /**
     * <code>fwChildNodes</code> is a flyweigt empty child nodes list. As 
     * proposed by the GoF.
     */
    private static NodeListImpl fwChildNodes = new NodeListImpl(null);
    
    /**
     * This message is thrown if the user tries to add the wrong child
     * nodes.
     */
    private static final String NO_CHILD_MSG 
            = "This Node doesn't allow children.";
    
    /**
     * Comment for <code>ownerDocument</code>
     * @clientCardinality 0..*
     * @clientRole ownerDocument
     * @directed true
     * @supplierCardinality 1
     */
    protected DocumentImpl ownerDocument = null;
    
    /**
     * Comment for <code>parentNode</code>
     * @clientRole parentNode
     * @directed true
     */
    protected AbstractParentNode parentNode = null;
    
    /**
     * Comment for <code>nodeName</code>
     */
    protected String nodeName = "";
    
    /**
     * Comment for <code>nodeType</code>
     */
    private final short nodeType;
    
    
    /**
     * @param ownerIn ..
     * @param nodeNameIn ..
     * @param nodeTypeIn ..
     */
    protected AbstractNode(final DocumentImpl ownerIn,
                           final String nodeNameIn, 
                           final short nodeTypeIn) {
        
        super();
        
        this.ownerDocument = ownerIn;
        this.nodeName = nodeNameIn;
        this.nodeType = nodeTypeIn;
    }
    

    /**
     * The name of this node, depending on its type; see the table above.
     * 
     * @return ---
     * @see org.w3c.dom.Node#getNodeName()
     */
    public String getNodeName() {
        return this.nodeName;
    }

    /**
     * The value of this node, depending on its type; see the table above. 
     * When it is defined to be <code>null</code>, setting it has no effect.
     * 
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.
     * @exception DOMException
     *   DOMSTRING_SIZE_ERR: Raised when it would return more characters than 
     *   fit in a <code>DOMString</code> variable on the implementation 
     *   platform.
     * @return ---
     * @see org.w3c.dom.Node#getNodeValue()
     */
    public String getNodeValue() throws DOMException {
        return null;
    }

    /**
     * The value of this node, depending on its type; see the table above. 
     * When it is defined to be <code>null</code>, setting it has no effect.
     * 
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.
     * @exception DOMException
     *   DOMSTRING_SIZE_ERR: Raised when it would return more characters than 
     *   fit in a <code>DOMString</code> variable on the implementation 
     *   platform.
     * @param nodeValue ---
     * @see org.w3c.dom.Node#setNodeValue(java.lang.String)
     */
    public void setNodeValue(final String nodeValue) throws DOMException {
    }

    /**
     * A code representing the type of the underlying object, as defined above.
     * 
     * @return ---
     * @see org.w3c.dom.Node#getNodeType()
     */
    public short getNodeType() {
        return this.nodeType;
    }

    /**
     * The parent of this node. All nodes, except <code>Attr</code>, 
     * <code>Document</code>, <code>DocumentFragment</code>, 
     * <code>Entity</code>, and <code>Notation</code> may have a parent. 
     * However, if a node has just been created and not yet added to the 
     * tree, or if it has been removed from the tree, this is 
     * <code>null</code>. 
     * <br> When the node is an <code>Element</code>, a 
     * <code>ProcessingInstruction</code>, an <code>EntityReference</code>, 
     * a <code>CharacterData</code>, a <code>Comment</code>, or a 
     * <code>DocumentType</code>, this attribute represents the properties 
     * [parent] defined in .
     *  
     * @return ---
     * @see org.w3c.dom.Node#getParentNode()
     */
    public Node getParentNode() {
        return this.parentNode;
    }

    /**
     * A <code>NodeList</code> that contains all children of this node. If 
     * there are no children, this is a <code>NodeList</code> containing no 
     * nodes.
     * <br> When the node is a <code>Document</code>, or an 
     * <code>Element</code>, and if the <code>NodeList</code> does not 
     * contain <code>EntityReference</code> or <code>CDATASection</code> 
     * nodes, this attribute represents the properties [children] defined in.
     * 
     * @return --- 
     * @see org.w3c.dom.Node#getChildNodes()
     */
    public NodeList getChildNodes() {
        return AbstractNode.fwChildNodes;
    }

    /**
     * The first child of this node. If there is no such node, this returns 
     * <code>null</code>.
     * 
     * @return ---
     * @see org.w3c.dom.Node#getFirstChild()
     */
    public Node getFirstChild() {
        return null;
    }

    /**
     * The last child of this node. If there is no such node, this returns 
     * <code>null</code>.
     * @return ---
     * @see org.w3c.dom.Node#getLastChild()
     */
    public Node getLastChild() {
        return null;
    }

    /**
     * The node immediately preceding this node. If there is no such node, 
     * this returns <code>null</code>.
     * 
     * @return ---
     * @see org.w3c.dom.Node#getPreviousSibling()
     */
    public Node getPreviousSibling() {
        Node prev = null;
        if (this.parentNode instanceof AbstractParentNode) {
            List list = ((NodeListImpl) 
                    this.parentNode.getChildNodes()).getList();
            int index = list.indexOf(this);
            if (index > 0) {
                prev = (Node) list.get(index - 1);
            }
        }
        return prev;
    }

    /**
     * The node immediately following this node. If there is no such node, 
     * this returns <code>null</code>.
     * 
     * @return ---
     * @see org.w3c.dom.Node#getNextSibling()
     */
    public Node getNextSibling() {
        Node next = null;
        if (this.parentNode instanceof AbstractParentNode) {
            List list = ((NodeListImpl) 
                    this.parentNode.getChildNodes()).getList();
            int index = list.indexOf(this);
            if (index < list.size() - 1) {
                next = (Node) list.get(index + 1);
            }
        }
        return next;
    }

    /**
     * A <code>NamedNodeMap</code> containing the attributes of this node (if 
     * it is an <code>Element</code>) or <code>null</code> otherwise.
     * <br> If no namespace declaration appear in the attributes, this 
     * attribute represents the property [attributes] defined in .
     * 
     * @return --- 
     * @see org.w3c.dom.Node#getAttributes()
     */
    public NamedNodeMap getAttributes() {
        return null;
    }

    /**
     * The <code>Document</code> object associated with this node. This is 
     * also the <code>Document</code> object used to create new nodes. When 
     * this node is a <code>Document</code> or a <code>DocumentType</code> 
     * which is not used with any <code>Document</code> yet, this is 
     * <code>null</code>.
     * 
     * @return ---
     * @version DOM Level 2
     * @see org.w3c.dom.Node#getOwnerDocument()
     */
    public Document getOwnerDocument() {
        return this.ownerDocument;
    }

    /**
     * Inserts the node <code>newChild</code> before the existing child node 
     * <code>refChild</code>. If <code>refChild</code> is <code>null</code>, 
     * insert <code>newChild</code> at the end of the list of children.
     * <br>If <code>newChild</code> is a <code>DocumentFragment</code> object, 
     * all of its children are inserted, in the same order, before 
     * <code>refChild</code>. If the <code>newChild</code> is already in the 
     * tree, it is first removed.
     * @param newChild The node to insert.
     * @param refChild The reference node, i.e., the node before which the 
     *   new node must be inserted.
     * @return The node being inserted.
     * @exception DOMException
     *   HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not 
     *   allow children of the type of the <code>newChild</code> node, or if 
     *   the node to insert is one of this node's ancestors or this node 
     *   itself, or if this node if of type <code>Document</code> and the 
     *   DOM application attempts to insert a second 
     *   <code>DocumentType</code> or <code>Element</code> node.
     *   <br>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created 
     *   from a different document than the one that created this node.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly or 
     *   if the parent of the node being inserted is readonly.
     *   <br>NOT_FOUND_ERR: Raised if <code>refChild</code> is not a child of 
     *   this node.
     *   <br>NOT_SUPPORTED_ERR: if this node if of type <code>Document</code>, 
     *   this exception might be raised if the DOM implementation doesn't 
     *   support the insertion of a <code>DocumentType</code> or 
     *   <code>Element</code> node.
     * @version DOM Level 3
     * @see org.w3c.dom.Node#insertBefore(org.w3c.dom.Node, org.w3c.dom.Node)
     */
    public Node insertBefore(final Node newChild, 
                             final Node refChild) throws DOMException {
        
        throw new DOMException(
                DOMException.HIERARCHY_REQUEST_ERR, NO_CHILD_MSG);
    }

    /**
     * Replaces the child node <code>oldChild</code> with <code>newChild</code>
     *  in the list of children, and returns the <code>oldChild</code> node.
     * <br>If <code>newChild</code> is a <code>DocumentFragment</code> object, 
     * <code>oldChild</code> is replaced by all of the 
     * <code>DocumentFragment</code> children, which are inserted in the 
     * same order. If the <code>newChild</code> is already in the tree, it 
     * is first removed.
     * @param newChild The new node to put in the child list.
     * @param oldChild The node being replaced in the list.
     * @return The node replaced.
     * @exception DOMException
     *   HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not 
     *   allow children of the type of the <code>newChild</code> node, or if 
     *   the node to put in is one of this node's ancestors or this node 
     *   itself.
     *   <br>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created 
     *   from a different document than the one that created this node.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node or the parent of 
     *   the new node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if <code>oldChild</code> is not a child of 
     *   this node.
     *   <br>NOT_SUPPORTED_ERR: if this node if of type <code>Document</code>, 
     *   this exception might be raised if the DOM implementation doesn't 
     *   support the replacement of the <code>DocumentType</code> child or 
     *   <code>Element</code> child.
     * @version DOM Level 3
     * @see org.w3c.dom.Node#replaceChild(org.w3c.dom.Node, org.w3c.dom.Node)
     */
    public Node replaceChild(final Node newChild, 
                             final Node oldChild) throws DOMException {
        
        throw new DOMException(
                DOMException.HIERARCHY_REQUEST_ERR, NO_CHILD_MSG);
    }

    /**
     * Removes the child node indicated by <code>oldChild</code> from the list 
     * of children, and returns it.
     * 
     * @param oldChild The node being removed.
     * @return The node removed.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if <code>oldChild</code> is not a child of 
     *   this node.
     *   <br>NOT_SUPPORTED_ERR: if this node if of type <code>Document</code>, 
     *   this exception might be raised if the DOM implementation doesn't 
     *   support the removal of the <code>DocumentType</code> child or the 
     *   <code>Element</code> child.
     * @version DOM Level 3
     * @see org.w3c.dom.Node#removeChild(org.w3c.dom.Node)
     */
    public Node removeChild(final Node oldChild) throws DOMException {
        throw new DOMException(
                DOMException.HIERARCHY_REQUEST_ERR, NO_CHILD_MSG);
    }

    /**
     * Adds the node <code>newChild</code> to the end of the list of children 
     * of this node. If the <code>newChild</code> is already in the tree, it 
     * is first removed.
     * 
     * @param newChild The node to add.If it is a 
     *   <code>DocumentFragment</code> object, the entire contents of the 
     *   document fragment are moved into the child list of this node
     * @return The node added.
     * @exception DOMException
     *   HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not 
     *   allow children of the type of the <code>newChild</code> node, or if 
     *   the node to append is one of this node's ancestors or this node 
     *   itself.
     *   <br>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created 
     *   from a different document than the one that created this node.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly or 
     *   if the previous parent of the node being inserted is readonly.
     * @see org.w3c.dom.Node#appendChild(org.w3c.dom.Node)
     */
    public Node appendChild(final Node newChild) throws DOMException {
        throw new DOMException(
                DOMException.HIERARCHY_REQUEST_ERR, NO_CHILD_MSG);
    }

    /**
     * Returns whether this node has any children.
     * 
     * @return <code>true</code> if this node has any children, 
     *   <code>false</code> otherwise.
     * @see org.w3c.dom.Node#hasChildNodes()
     */
    public boolean hasChildNodes() {
        return false;
    }

    /**
     * Puts all <code>Text</code> nodes in the full depth of the sub-tree 
     * underneath this <code>Node</code>, including attribute nodes, into a 
     * "normal" form where only structure (e.g., elements, comments, 
     * processing instructions, CDATA sections, and entity references) 
     * separates <code>Text</code> nodes, i.e., there are neither adjacent 
     * <code>Text</code> nodes nor empty <code>Text</code> nodes. This can 
     * be used to ensure that the DOM view of a document is the same as if 
     * it were saved and re-loaded, and is useful when operations (such as 
     * XPointer  lookups) that depend on a particular document tree 
     * structure are to be used.In cases where the document contains 
     * <code>CDATASections</code>, the normalize operation alone may not be 
     * sufficient, since XPointers do not differentiate between 
     * <code>Text</code> nodes and <code>CDATASection</code> nodes.
     * 
     * @version DOM Level 2
     * @see org.w3c.dom.Node#normalize()
     */
    public void normalize() {
                
        if (this.hasChildNodes()) {
            Node last = null;
            List nodes = ((NodeListImpl) this.getChildNodes()).getList();
            for (int i = 0; i < nodes.size(); i++) {
                Node node = (Node) nodes.get(i);
                node.normalize();
                if ((last instanceof TextImpl) && (node instanceof TextImpl)) {
                    ((TextImpl) last).appendData(node.getNodeValue());
                    nodes.remove(i);
                } else {
                    last = node;
                }
            }
        }
        if (this.hasAttributes()) {
            NamedNodeMap nnm = this.getAttributes();
            for (int i = 0, size = nnm.getLength(); i < size; i++) {
                nnm.item(i).normalize();
            }
        }
    }

    /**
     * Tests whether the DOM implementation implements a specific feature and 
     * that feature is supported by this node.
     * 
     * @param feature The name of the feature to test. This is the same name 
     *   which can be passed to the method <code>hasFeature</code> on 
     *   <code>DOMImplementation</code>.
     * @param version This is the version number of the feature to test. In 
     *   Level 2, version 1, this is the string "2.0". If the version is not 
     *   specified, supporting any version of the feature will cause the 
     *   method to return <code>true</code>.
     * @return Returns <code>true</code> if the specified feature is 
     *   supported on this node, <code>false</code> otherwise.
     * @since DOM Level 2
     * @see org.w3c.dom.Node#isSupported(java.lang.String, java.lang.String)
     */
    public boolean isSupported(final String feature, final String version) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * The namespace URI of this node, or <code>null</code> if it is 
     * unspecified.
     * <br> When the node is <code>Element</code>, or <code>Attr</code>, this 
     * attribute represents the properties [namespace name] defined in . 
     * <br>This is not a computed value that is the result of a namespace 
     * lookup based on an examination of the namespace declarations in 
     * scope. It is merely the namespace URI given at creation time.
     * <br>For nodes of any type other than <code>ELEMENT_NODE</code> and 
     * <code>ATTRIBUTE_NODE</code> and nodes created with a DOM Level 1 
     * method, such as <code>createElement</code> from the 
     * <code>Document</code> interface, this is always <code>null</code>.Per 
     * the Namespaces in XML Specification  an attribute does not inherit 
     * its namespace from the element it is attached to. If an attribute is 
     * not explicitly given a namespace, it simply has no namespace.
     * 
     * @return ---
     * @since DOM Level 2
     * @see org.w3c.dom.Node#getNamespaceURI()
     */
    public String getNamespaceURI() {
        return null;
    }

    /**
     * The namespace prefix of this node, or <code>null</code> if it is 
     * unspecified.
     * <br> When the node is <code>Element</code>, or <code>Attr</code>, this 
     * attribute represents the properties [prefix] defined in . 
     * <br>Note that setting this attribute, when permitted, changes the 
     * <code>nodeName</code> attribute, which holds the qualified name, as 
     * well as the <code>tagName</code> and <code>name</code> attributes of 
     * the <code>Element</code> and <code>Attr</code> interfaces, when 
     * applicable.
     * <br>Note also that changing the prefix of an attribute that is known to 
     * have a default value, does not make a new attribute with the default 
     * value and the original prefix appear, since the 
     * <code>namespaceURI</code> and <code>localName</code> do not change.
     * <br>For nodes of any type other than <code>ELEMENT_NODE</code> and 
     * <code>ATTRIBUTE_NODE</code> and nodes created with a DOM Level 1 
     * method, such as <code>createElement</code> from the 
     * <code>Document</code> interface, this is always <code>null</code>.
     * 
     * @return ---
     * @since DOM Level 2
     */
    public String getPrefix() {
        return null;
    }

    /**
     * The namespace prefix of this node, or <code>null</code> if it is 
     * unspecified.
     * <br> When the node is <code>Element</code>, or <code>Attr</code>, this 
     * attribute represents the properties [prefix] defined in . 
     * <br>Note that setting this attribute, when permitted, changes the 
     * <code>nodeName</code> attribute, which holds the qualified name, as 
     * well as the <code>tagName</code> and <code>name</code> attributes of 
     * the <code>Element</code> and <code>Attr</code> interfaces, when 
     * applicable.
     * <br>Note also that changing the prefix of an attribute that is known to 
     * have a default value, does not make a new attribute with the default 
     * value and the original prefix appear, since the 
     * <code>namespaceURI</code> and <code>localName</code> do not change.
     * <br>For nodes of any type other than <code>ELEMENT_NODE</code> and 
     * <code>ATTRIBUTE_NODE</code> and nodes created with a DOM Level 1 
     * method, such as <code>createElement</code> from the 
     * <code>Document</code> interface, this is always <code>null</code>.
     * 
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified prefix contains an 
     *   illegal character, per the XML 1.0 specification .
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NAMESPACE_ERR: Raised if the specified <code>prefix</code> is 
     *   malformed per the Namespaces in XML specification, if the 
     *   <code>namespaceURI</code> of this node is <code>null</code>, if the 
     *   specified prefix is "xml" and the <code>namespaceURI</code> of this 
     *   node is different from "http://www.w3.org/XML/1998/namespace", if 
     *   this node is an attribute and the specified prefix is "xmlns" and 
     *   the <code>namespaceURI</code> of this node is different from "
     *   http://www.w3.org/2000/xmlns/", or if this node is an attribute and 
     *   the <code>qualifiedName</code> of this node is "xmlns" .
     * @param prefix ---
     * @since DOM Level 2
     * @see org.w3c.dom.Node#setPrefix(java.lang.String)
     */
    public void setPrefix(final String prefix) throws DOMException {
    }

    /**
     * Returns the local part of the qualified name of this node.
     * <br> When the node is <code>Element</code>, or <code>Attr</code>, this 
     * attribute represents the properties [local name] defined in . 
     * <br>For nodes of any type other than <code>ELEMENT_NODE</code> and 
     * <code>ATTRIBUTE_NODE</code> and nodes created with a DOM Level 1 
     * method, such as <code>createElement</code> from the 
     * <code>Document</code> interface, this is always <code>null</code>.
     * 
     * @return ---
     * @since DOM Level 2
     * @see org.w3c.dom.Node#getLocalName()
     */
    public String getLocalName() {
        return null;
    }

    /**
     * Returns whether this node (if it is an element) has any attributes.
     * 
     * @return <code>true</code> if this node has any attributes, 
     *   <code>false</code> otherwise.
     * @since DOM Level 2
     * @see org.w3c.dom.Node#hasAttributes()
     */
    public boolean hasAttributes() {
        return false;
    }

    /**
     * Returns whether this node is the same node as the given one.
     * <br>This method provides a way to determine whether two 
     * <code>Node</code> references returned by the implementation reference 
     * the same object. When two <code>Node</code> references are references 
     * to the same object, even if through a proxy, the references may be 
     * used completely interchangeably, such that all attributes have the 
     * same values and calling the same DOM method on either reference 
     * always has exactly the same effect.Do we really want to make this 
     * different from equals?Yes, change name from isIdentical to 
     * isSameNode. (Telcon 4 Jul 2000).Is this really needed if we provide a 
     * unique key?Yes, because the key is only unique within a document. 
     * (F2F 2 Mar 2001).Definition of 'sameness' is needed.
     * 
     * @param other The node to test against.
     * @return Returns <code>true</code> if the nodes are the same, 
     *   <code>false</code> otherwise.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#isSameNode(org.w3c.dom.Node)
     */
    public boolean isSameNode(final Node other) {
        return (this == other);
    }

    /**
     * Look up the prefix associated to the given namespace URI, starting from 
     * this node.
     * <br>See  for details on the algorithm used by this method.Should this 
     * be optional?No.How does the lookup work? Is it based on the prefix of 
     * the nodes, the namespace declaration attributes, or a combination of 
     * both?See .
     * 
     * @param namespaceURI The namespace URI to look for.
     * @return Returns the associated namespace prefix or <code>null</code> 
     *   if none is found. If more than one prefix are associated to the 
     *   namespace prefix, the returned namespace prefix is implementation 
     *   dependent.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#lookupNamespacePrefix(java.lang.String)
     */
    public String lookupNamespacePrefix(final String namespaceURI) {
        return null;
    }

    /**
     * Look up the namespace URI associated to the given prefix, starting from 
     * this node.
     * <br>See  for details on the algorithm used by this method.Name? May 
     * need to change depending on ending of the relative namespace URI 
     * reference nightmare.No need.Should this be optional?No.How does the 
     * lookup work? Is it based on the namespaceURI of the nodes, the 
     * namespace declaration attributes, or a combination of both?See .
     * 
     * @param prefix The prefix to look for.
     * @return Returns the associated namespace URI or <code>null</code> if 
     *   none is found.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#lookupNamespaceURI(java.lang.String)
     */
    public String lookupNamespaceURI(final String prefix) {
        return null;
    }
    
    /**
     * The internal identifier.
     */
    protected InternalIdI internalId = null;
    
    /**
     * Getter method for the internal used identifier.
     * 
     * @return The internal used storage identifier.
     */
    public final InternalIdI getInternalId() {
        return this.internalId;
    }
    
    /**
     * Setter method for the internal used identifier.
     * 
     * @param internalIdIn The internal used storage identifier.
     */
    public final void setInternalId(final InternalIdI internalIdIn) {
        this.internalId = internalIdIn;
    }
    
    //////////////////////////////////////////////////////////////////////
    // Non DOM methods
    //////////////////////////////////////////////////////////////////////
    
    
    /**
     * @param parentNodeIn ..
     */
    protected void setParentNode(final AbstractParentNode parentNodeIn) {
        this.parentNode = parentNodeIn;
    }

}
