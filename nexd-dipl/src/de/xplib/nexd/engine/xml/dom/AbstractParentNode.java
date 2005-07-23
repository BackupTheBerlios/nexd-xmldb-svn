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
package de.xplib.nexd.engine.xml.dom;

import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
public abstract class AbstractParentNode extends AbstractNode {
    
    /**
     * @clientCardinality 1
     * @clientRole childNodes
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#NodeListImpl lnkNodeListImpl*/

    /**
     * Comment for <code>ADD_TOP</code>
     */
    public static final short ADD_TOP = 0;
    
    /**
     * Comment for <code>ADD_BOTTOM</code>
     */
    public static final short ADD_BOTTOM = 1;
    
    /**
     * Comment for <code>childNodes</code>
     */
    private final NodeListImpl childNodes = new NodeListImpl(this);

    /**
     * @param ownerIn ..
     * @param nodeNameIn ..
     * @param nodeTypeIn ..
     */
    public AbstractParentNode(final DocumentImpl ownerIn,
                              final String nodeNameIn, 
                              final short nodeTypeIn) {
        
        super(ownerIn, nodeNameIn, nodeTypeIn);
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
        return this.childNodes;
    }
    
    /**
     * The first child of this node. If there is no such node, this returns 
     * <code>null</code>.
     * 
     * @return ---
     * @see org.w3c.dom.Node#getFirstChild()
     */
    public Node getFirstChild() {
        Node first = null;
        if (this.hasChildNodes()) {
            first = (Node) this.childNodes.item(0);
        }
        return first;
    }

    /**
     * The last child of this node. If there is no such node, this returns 
     * <code>null</code>.
     * @return ---
     * @see org.w3c.dom.Node#getLastChild()
     */
    public Node getLastChild() {
        Node last = null;
        if (this.hasChildNodes()) {
            last = (Node) this.childNodes.item(this.childNodes.getLength() - 1);
        }
        return last;
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
        
        int where = this.acceptNode(newChild);
        
        if (newChild.getParentNode() != null 
                && newChild.getParentNode() != this) {

            newChild.getParentNode().removeChild(newChild);
        }
        
        List nList = this.childNodes.getList();
        
        int index = nList.indexOf(refChild);
        if (refChild != null && index >= 0) {
            nList.add(index, newChild);
        } else if (where == ADD_TOP) {
            nList.add(0, newChild);
        } else {
            nList.add(newChild);
        }
        
        ((AbstractNode) newChild).setParentNode(this);
        
        if (this.ownerDocument != null && !this.ownerDocument.isLoading()
                && this.internalId != null) {
            this.ownerDocument.cache.insert((AbstractNode) newChild);
        }
        
        return newChild;
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
        
        if (oldChild == null || !this.childNodes.getList().contains(oldChild)) {
            throw new DOMException(
                    DOMException.NOT_FOUND_ERR, 
                    "Cannot find the old child node.");
        }
        
        Node ref = oldChild.getNextSibling();
        this.removeChild(oldChild);
        this.insertBefore(newChild, ref);
        
        if (this.internalId != null) {
            
            DocumentImpl doc = this.ownerDocument;
            
            // If this is the owner document it self, I have to use
            // this as document.
            if (doc == null) {
                if (this instanceof DocumentImpl) {
                    doc = (DocumentImpl) this;
                } else {
                    throw new DOMException(
                        DOMException.INVALID_STATE_ERR,
                    	"Unknown error in AbstractParentNode.replaceChild()");
                }
            }
            doc.cache.delete((AbstractNode) oldChild);
            doc.cache.insert((AbstractNode) newChild);
        }
        
        return oldChild;
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
        
        this.childNodes.getList().remove(oldChild);
        ((AbstractNode) oldChild).setParentNode(null);
        
        if (this.internalId != null) {
            DocumentImpl doc = this.ownerDocument;
            
            // If this is the owner document it self, I have to use
            // this as document.
            if (doc == null) {
                if (this instanceof DocumentImpl) {
                    doc = (DocumentImpl) this;
                } else {
                    throw new DOMException(
                        DOMException.INVALID_STATE_ERR,
                        "Unknown error in AbstractParentNode.replaceChild()");
                }
            }
            doc.cache.delete((AbstractNode) oldChild);
        }
        
        return oldChild;
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
        
        this.insertBefore(newChild, null);
        
        return newChild;
    }
    
    /**
     * Returns whether this node has any children.
     * 
     * @return <code>true</code> if this node has any children, 
     *   <code>false</code> otherwise.
     * @see org.w3c.dom.Node#hasChildNodes()
     */
    public boolean hasChildNodes() {
        return (this.childNodes.getLength() > 0);
    }
    
    
    /**
     * @param nodeIn ..
     * @return ..
     * @throws DOMException ..
     */
    protected abstract int acceptNode(Node nodeIn) throws DOMException;
}
