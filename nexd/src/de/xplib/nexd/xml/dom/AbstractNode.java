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
package de.xplib.nexd.xml.dom;

import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

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
public abstract class AbstractNode implements Node {
    
    /**
     * <code>fwChildNodes</code> is a flyweigt empty child nodes list. As 
     * proposed by the GoF.
     */
    private static NodeListImpl fwChildNodes = new NodeListImpl(null);
    
    /**
     * Comment for <code>nodeName</code>
     */
    private String nodeName = "";
    
    /**
     * Comment for <code>nodeType</code>
     */
    private short nodeType = -1;
    
    /**
     * Comment for <code>ownerDocument</code>
     */
    private DocumentImpl ownerDocument = null;
    
    /**
     * Comment for <code>parentNode</code>
     */
    private AbstractParentNode parentNode = null;
    
    
    /**
     * @param ownerIn ..
     * @param nodeNameIn ..
     * @param nodeTypeIn ..
     */
    protected AbstractNode(final DocumentImpl ownerIn,
                           final String nodeNameIn, 
                           final short nodeTypeIn) {
        
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
        List list = ((NodeListImpl) this.parentNode.getChildNodes()).getList();
        int index = list.indexOf(this);
        if (index > 0) {
            return (Node) list.get(index - 1);
        }
        return null;
    }

    /**
     * The node immediately following this node. If there is no such node, 
     * this returns <code>null</code>.
     * 
     * @return ---
     * @see org.w3c.dom.Node#getNextSibling()
     */
    public Node getNextSibling() {
        List list = ((NodeListImpl) this.parentNode.getChildNodes()).getList();
        int index = list.indexOf(this);
        if (index < list.size() - 1) {
            return (Node) list.get(index + 1);
        }
        return null;
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
                DOMException.HIERARCHY_REQUEST_ERR,
                "This Node doesn't allow children.");
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
                DOMException.HIERARCHY_REQUEST_ERR,
                "This Node doesn't allow children.");
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
                DOMException.HIERARCHY_REQUEST_ERR,
                "This Node doesn't allow children.");
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
                DOMException.HIERARCHY_REQUEST_ERR,
                "This Node doesn't allow children.");
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
     * The absolute base URI of this node or <code>null</code> if undefined. 
     * This value is computed according to . However, when the 
     * <code>Document</code> supports the feature "HTML" , the base URI is 
     * computed using first the value of the href attribute of the HTML BASE 
     * element if any, and the value of the <code>documentURI</code> 
     * attribute from the <code>Document</code> interface otherwise.
     * <br> When the node is an <code>Element</code>, a <code>Document</code> 
     * or a a <code>ProcessingInstruction</code>, this attribute represents 
     * the properties [base URI] defined in . When the node is a 
     * <code>Notation</code>, an <code>Entity</code>, or an 
     * <code>EntityReference</code>, this attribute represents the 
     * properties [declaration base URI] in the . How will this be affected 
     * by resolution of relative namespace URIs issue?It's not.Should this 
     * only be on Document, Element, ProcessingInstruction, Entity, and 
     * Notation nodes, according to the infoset? If not, what is it equal to 
     * on other nodes? Null? An empty string? I think it should be the 
     * parent's.No.Should this be read-only and computed or and actual 
     * read-write attribute?Read-only and computed (F2F 19 Jun 2000 and 
     * teleconference 30 May 2001).If the base HTML element is not yet 
     * attached to a document, does the insert change the Document.baseURI?
     * Yes. (F2F 26 Sep 2001)
     * @return ---
     * @since DOM Level 3
     * @see org.w3c.dom.Node#getBaseURI()
     */
    public String getBaseURI() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Compares a node with this node with regard to their position in the 
     * tree and according to the document order. This order can be extended 
     * by module that define additional types of nodes.Should this method be 
     * optional?No.Need reference for namespace nodes.No, instead avoid 
     * referencing them directly.
     * 
     * @param other The node to compare against this node.
     * @return Returns how the given node is positioned relatively to this 
     *   node.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#compareTreePosition(org.w3c.dom.Node)
     */
    public short compareTreePosition(final Node other) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * This attribute returns the text content of this node and its 
     * descendants. When it is defined to be null, setting it has no effect. 
     * When set, any possible children this node may have are removed and 
     * replaced by a single <code>Text</code> node containing the string 
     * this attribute is set to. On getting, no serialization is performed, 
     * the returned string does not contain any markup. No whitespace 
     * normalization is performed, the returned string does not contain the 
     * element content whitespaces . Similarly, on setting, no parsing is 
     * performed either, the input string is taken as pure textual content.
     * <br>The string returned is made of the text content of this node 
     * depending on its type, as defined below: 
     * <table border='1'>
     * <tr>
     * <th>Node type</th>
     * <th>Content</th>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>
     * ELEMENT_NODE, ENTITY_NODE, ENTITY_REFERENCE_NODE, 
     * DOCUMENT_FRAGMENT_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'>concatenation of the
     * <code>textContent</code> 
     * attribute value of every child node, excluding COMMENT_NODE and 
     * PROCESSING_INSTRUCTION_NODE nodes</td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>ATTRIBUTE_NODE, TEXT_NODE, 
     * CDATA_SECTION_NODE, COMMENT_NODE, PROCESSING_INSTRUCTION_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'>
     * <code>nodeValue</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>DOCUMENT_NODE, 
     * DOCUMENT_TYPE_NODE, NOTATION_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'>
     * null</td>
     * </tr>
     * </table> Should any whitespace normalization be performed? MS' text 
     * property doesn't but what about "ignorable whitespace"?Does not 
     * perform any whitespace normalization and ignores "ignorable 
     * whitespace".Should this be two methods instead?No. Keep it a read 
     * write attribute.What about the name? MS uses text and innerText. text 
     * conflicts with HTML DOM.Keep the current name, MS has a different 
     * name and different semantic.Should this be optional?No.Setting the 
     * text property on a Document, Document Type, or Notation node is an 
     * error for MS. How do we expose it? Exception? Which one?
     * (teleconference 23 May 2001) consistency with nodeValue. Remove 
     * Document from the list.
     * 
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.
     * @exception DOMException
     *   DOMSTRING_SIZE_ERR: Raised when it would return more characters than 
     *   fit in a <code>DOMString</code> variable on the implementation 
     *   platform.
     * @return ---
     * @since DOM Level 3
     * @see org.w3c.dom.Node#getTextContent()
     */
    public String getTextContent() throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * This attribute returns the text content of this node and its 
     * descendants. When it is defined to be null, setting it has no effect. 
     * When set, any possible children this node may have are removed and 
     * replaced by a single <code>Text</code> node containing the string 
     * this attribute is set to. On getting, no serialization is performed, 
     * the returned string does not contain any markup. No whitespace 
     * normalization is performed, the returned string does not contain the 
     * element content whitespaces . Similarly, on setting, no parsing is 
     * performed either, the input string is taken as pure textual content.
     * <br>The string returned is made of the text content of this node 
     * depending on its type, as defined below: 
     * <table border='1'>
     * <tr>
     * <th>Node type</th>
     * <th>Content</th>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>
     * ELEMENT_NODE, ENTITY_NODE, ENTITY_REFERENCE_NODE, 
     * DOCUMENT_FRAGMENT_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'>concatenation of the 
     * <code>textContent</code> 
     * attribute value of every child node, excluding COMMENT_NODE and 
     * PROCESSING_INSTRUCTION_NODE nodes</td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>ATTRIBUTE_NODE, TEXT_NODE, 
     * CDATA_SECTION_NODE, COMMENT_NODE, PROCESSING_INSTRUCTION_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'>
     * <code>nodeValue</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>DOCUMENT_NODE, 
     * DOCUMENT_TYPE_NODE, NOTATION_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'>
     * null</td>
     * </tr>
     * </table> Should any whitespace normalization be performed? MS' text 
     * property doesn't but what about "ignorable whitespace"?Does not 
     * perform any whitespace normalization and ignores "ignorable 
     * whitespace".Should this be two methods instead?No. Keep it a read 
     * write attribute.What about the name? MS uses text and innerText. text 
     * conflicts with HTML DOM.Keep the current name, MS has a different 
     * name and different semantic.Should this be optional?No.Setting the 
     * text property on a Document, Document Type, or Notation node is an 
     * error for MS. How do we expose it? Exception? Which one?
     * (teleconference 23 May 2001) consistency with nodeValue. Remove 
     * Document from the list.
     * 
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.
     * @exception DOMException
     *   DOMSTRING_SIZE_ERR: Raised when it would return more characters than 
     *   fit in a <code>DOMString</code> variable on the implementation 
     *   platform.
     * @param textContent ---
     * @since DOM Level 3
     * @see org.w3c.dom.Node#setTextContent(java.lang.String)
     */
    public void setTextContent(final String textContent) throws DOMException {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
        return false;
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Tests whether two nodes are equal.
     * <br>This method tests for equality of nodes, not sameness (i.e., 
     * whether the two nodes are references to the same object) which can be 
     * tested with <code>Node.isSameNode</code>. All nodes that are the same 
     * will also be equal, though the reverse may not be true.
     * <br>Two nodes are equal if and only if the following conditions are 
     * satisfied: The two nodes are of the same type.The following string 
     * attributes are equal: <code>nodeName</code>, <code>localName</code>, 
     * <code>namespaceURI</code>, <code>prefix</code>, <code>nodeValue</code>
     * , <code>baseURI</code>. This is: they are both <code>null</code>, or 
     * they have the same length and are character for character identical.
     * The <code>attributes</code> <code>NamedNodeMaps</code> are equal. 
     * This is: they are both <code>null</code>, or they have the same 
     * length and for each node that exists in one map there is a node that 
     * exists in the other map and is equal, although not necessarily at the 
     * same index.The <code>childNodes</code> <code>NodeLists</code> are 
     * equal. This is: they are both <code>null</code>, or they have the 
     * same length and contain equal nodes at the same index. This is true 
     * for <code>Attr</code> nodes as for any other type of node. Note that 
     * normalization can affect equality; to avoid this, nodes should be 
     * normalized before being compared. 
     * <br>For two <code>DocumentType</code> nodes to be equal, the following 
     * conditions must also be satisfied: The following string attributes 
     * are equal: <code>publicId</code>, <code>systemId</code>, 
     * <code>internalSubset</code>.The <code>entities</code> 
     * <code>NamedNodeMaps</code> are equal.The <code>notations</code> 
     * <code>NamedNodeMaps</code> are equal. 
     * <br>On the other hand, the following do not affect equality: the 
     * <code>ownerDocument</code> attribute, the <code>specified</code> 
     * attribute for <code>Attr</code> nodes, the 
     * <code>isWhitespaceInElementContent</code> attribute for 
     * <code>Text</code> nodes, as well as any user data or event listeners 
     * registered on the nodes.Should this be optional?No.
     * @param arg The node to compare equality with.
     * @param deep If <code>true</code>, recursively compare the subtrees; if 
     *   <code>false</code>, compare only the nodes themselves (and its 
     *   attributes, if it is an <code>Element</code>).
     * @return If the nodes, and possibly subtrees are equal, 
     *   <code>true</code> otherwise <code>false</code>.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#isEqualNode(org.w3c.dom.Node, boolean)
     */
    public boolean isEqualNode(final Node arg, final boolean deep) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * This method makes available a <code>Node</code>'s specialized interface 
     * (see ).What are the relations between Node.isSupported and 
     * Node3.getInterface?Should we rename this method (and also 
     * DOMImplementation.getInterface?)?getInterface can return a node that 
     * doesn't actually support the requested interface and will lead to a 
     * cast exception. Other solutions are returning null or throwing an 
     * exception.
     * 
     * @param feature The name of the feature requested (case-insensitive).
     * @return Returns an alternate <code>Node</code> which implements the 
     *   specialized APIs of the specified feature, if any, or 
     *   <code>null</code> if there is no alternate <code>Node</code> which 
     *   implements interfaces associated with that feature. Any alternate 
     *   <code>Node</code> returned by this method must delegate to the 
     *   primary core <code>Node</code> and not return results inconsistent 
     *   with the primary core <code>Node</code> such as <code>key</code>, 
     *   <code>attributes</code>, <code>childNodes</code>, etc.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#getInterface(java.lang.String)
     */
    public Node getInterface(final String feature) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Associate an object to a key on this node. The object can later be 
     * retrieved from this node by calling <code>getUserData</code> with the 
     * same key.
     * 
     * @param key The key to associate the object to.
     * @param data The object to associate to the given key, or 
     *   <code>null</code> to remove any existing association to that key.
     * @param handler The handler to associate to that key, or 
     *   <code>null</code>.
     * @return Returns the <code>DOMKeyObject</code> previously associated to 
     *   the given key on this node, or <code>null</code> if there was none.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#setUserData(
     * 		java.lang.String, java.lang.Object, org.w3c.dom.UserDataHandler)
     */
    public Object setUserData(final String key, 
                              final Object data, 
                              final UserDataHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Retrieves the object associated to a key on a this node. The object 
     * must first have been set to this node by calling 
     * <code>setUserData</code> with the same key.
     * 
     * @param key The key the object is associated to.
     * @return Returns the <code>DOMKeyObject</code> associated to the given 
     *   key on this node, or <code>null</code> if there was none.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#getUserData(java.lang.String)
     */
    public Object getUserData(final String key) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Compares the reference node, i.e. the node on which this method is 
     * being called, with a node, i.e. the one passed as a parameter, with 
     * regard to their position in the document and according to the 
     * document order.
     * @param other The node to compare against the reference node.
     * @return Returns how the node is positioned relatively to the reference 
     *   node.
     * @exception DOMException
     *   NOT_SUPPORTED_ERR: when the compared nodes are from different DOM 
     *   implementations that do not coordinate to return consistent 
     *   implementation-specific results.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#compareDocumentPosition(org.w3c.dom.Node)
     */
    public short compareDocumentPosition(final Node other) throws DOMException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     *  This method returns a specialized object which implements the 
     * specialized APIs of the specified feature and version, as specified 
     * in . The specialized object may also be obtained by using 
     * binding-specific casting methods but is not necessarily expected to, 
     * as discussed in . This method also allow the implementation to 
     * provide specialized objects which do not support the <code>Node</code>
     *  interface. 
     * @param feature  The name of the feature requested. Note that any plus 
     *   sign "+" prepended to the name of the feature will be ignored since 
     *   it is not significant in the context of this method. 
     * @param version  This is the version number of the feature to test. 
     * @return  Returns an object which implements the specialized APIs of 
     *   the specified feature and version, if any, or <code>null</code> if 
     *   there is no object which implements interfaces associated with that 
     *   feature. If the <code>DOMObject</code> returned by this method 
     *   implements the <code>Node</code> interface, it must delegate to the 
     *   primary core <code>Node</code> and not return results inconsistent 
     *   with the primary core <code>Node</code> such as attributes, 
     *   childNodes, etc. 
     * @since DOM Level 3
     * @see org.w3c.dom.Node#getFeature(java.lang.String, java.lang.String)
     */
    public Object getFeature(final String feature, final String version) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * This method checks if the specified <code>namespaceURI</code> is the 
     * default namespace or not. 
     * 
     * @param namespaceURI The namespace URI to look for.
     * @return Returns <code>true</code> if the specified 
     *   <code>namespaceURI</code> is the default namespace, 
     *   <code>false</code> otherwise. 
     * @since DOM Level 3
     * @see org.w3c.dom.Node#isDefaultNamespace(java.lang.String)
     */
    public boolean isDefaultNamespace(final String namespaceURI) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * Tests whether two nodes are equal.
     * <br>This method tests for equality of nodes, not sameness (i.e., 
     * whether the two nodes are references to the same object) which can be 
     * tested with <code>Node.isSameNode()</code>. All nodes that are the 
     * same will also be equal, though the reverse may not be true.
     * <br>Two nodes are equal if and only if the following conditions are 
     * satisfied: 
     * <ul>
     * <li>The two nodes are of the same type.
     * </li>
     * <li>The following string 
     * attributes are equal: <code>nodeName</code>, <code>localName</code>, 
     * <code>namespaceURI</code>, <code>prefix</code>, <code>nodeValue</code>
     * . This is: they are both <code>null</code>, or they have the same 
     * length and are character for character identical.
     * </li>
     * <li>The 
     * <code>attributes</code> <code>NamedNodeMaps</code> are equal. This 
     * is: they are both <code>null</code>, or they have the same length and 
     * for each node that exists in one map there is a node that exists in 
     * the other map and is equal, although not necessarily at the same 
     * index.
     * </li>
     * <li>The <code>childNodes</code> <code>NodeLists</code> are equal. 
     * This is: they are both <code>null</code>, or they have the same 
     * length and contain equal nodes at the same index. Note that 
     * normalization can affect equality; to avoid this, nodes should be 
     * normalized before being compared.
     * </li>
     * </ul> 
     * <br>For two <code>DocumentType</code> nodes to be equal, the following 
     * conditions must also be satisfied: 
     * <ul>
     * <li>The following string attributes 
     * are equal: <code>publicId</code>, <code>systemId</code>, 
     * <code>internalSubset</code>.
     * </li>
     * <li>The <code>entities</code> 
     * <code>NamedNodeMaps</code> are equal.
     * </li>
     * <li>The <code>notations</code> 
     * <code>NamedNodeMaps</code> are equal.
     * </li>
     * </ul> 
     * <br>On the other hand, the following do not affect equality: the 
     * <code>ownerDocument</code>, <code>baseURI</code>, and 
     * <code>parentNode</code> attributes, the <code>specified</code> 
     * attribute for <code>Attr</code> nodes, the <code>schemaTypeInfo</code>
     *  attribute for <code>Attr</code> and <code>Element</code> nodes, the 
     * <code>Text.isElementContentWhitespace</code> attribute for 
     * <code>Text</code> nodes, as well as any user data or event listeners 
     * registered on the nodes. 
     * <p ><b>Note:</b>  As a general rule, anything not mentioned in the 
     * description above is not significant in consideration of equality 
     * checking. Note that future versions of this specification may take 
     * into account more attributes and implementations conform to this 
     * specification are expected to be updated accordingly.
     *  
     * @param arg The node to compare equality with.
     * @return Returns <code>true</code> if the nodes are equal, 
     *   <code>false</code> otherwise.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#isEqualNode(org.w3c.dom.Node)
     */
    public boolean isEqualNode(final Node arg) {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * Look up the prefix associated to the given namespace URI, starting from 
     * this node. The default namespace declarations are ignored by this 
     * method.
     * <br>See  for details on the algorithm used by this method.
     * 
     * @param namespaceURI The namespace URI to look for.
     * @return Returns an associated namespace prefix if found or 
     *   <code>null</code> if none is found. If more than one prefix are 
     *   associated to the namespace prefix, the returned namespace prefix 
     *   is implementation dependent.
     * @since DOM Level 3
     * @see org.w3c.dom.Node#lookupPrefix(java.lang.String)
     */
    public String lookupPrefix(final String namespaceURI) {
        // TODO Auto-generated method stub
        return null;
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