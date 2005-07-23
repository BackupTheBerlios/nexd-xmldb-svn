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
 * $Log: NamedNodeMapImpl.java,v $
 * Revision 1.3  2005/05/30 19:17:07  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.5  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.4  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.3  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xml.dom;

import java.util.Iterator;

import org.apache.commons.collections.FastHashMap;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Objects implementing the <code>NamedNodeMap</code> interface are used to 
 * represent collections of nodes that can be accessed by name. Note that 
 * <code>NamedNodeMap</code> does not inherit from <code>NodeList</code>; 
 * <code>NamedNodeMaps</code> are not maintained in any particular order. 
 * Objects contained in an object implementing <code>NamedNodeMap</code> may 
 * also be accessed by an ordinal index, but this is simply to allow 
 * convenient enumeration of the contents of a <code>NamedNodeMap</code>, 
 * and does not imply that the DOM specifies an order to these Nodes. 
 * <p><code>NamedNodeMap</code> objects in the DOM are live.
 * <p>See also the <a 
 * href='http://www.w3.org/TR/2002/WD-DOM-Level-3-Core-20020114'
 * >Document Object Model (DOM) Level 3 Core Specification</a>.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class NamedNodeMapImpl implements NamedNodeMap {
    
    /**
     * @clientCardinality 1
     * @clientRole items
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     */

    private AttrImpl lnkAttrImpl;

    /**
     * Comment for <code>items</code>
     */
    private FastHashMap items = null;
    
    /**
     * Comment for <code>owner</code>
     */
    private AbstractParentNode owner = null;
    

    /**
     * @param ownerIn ..
     */
    public NamedNodeMapImpl(final AbstractParentNode ownerIn) {
        this(ownerIn, new FastHashMap());
    }
    
    /**
     * @param ownerIn ..
     * @param itemsIn ..
     */
    public NamedNodeMapImpl(final AbstractParentNode ownerIn, 
                            final FastHashMap itemsIn) {
        super();
        
        this.owner = ownerIn;
        this.items = itemsIn;
        this.items.setFast(true);
    }

    /**
     * Retrieves a node specified by name.
     * @param name The <code>nodeName</code> of a node to retrieve.
     * @return A <code>Node</code> (of any type) with the specified 
     *   <code>nodeName</code>, or <code>null</code> if it does not identify 
     *   any node in this map.
     * @see org.w3c.dom.NamedNodeMap#getNamedItem(java.lang.String)
     */
    public Node getNamedItem(final String name) {
        return (this.items.containsKey(name) 
                ? (Node) this.items.get(name) : null);
    }

    /**
     * Adds a node using its <code>nodeName</code> attribute. If a node with 
     * that name is already present in this map, it is replaced by the new 
     * one.
     * <br>As the <code>nodeName</code> attribute is used to derive the name 
     * which the node must be stored under, multiple nodes of certain types 
     * (those that have a "special" string value) cannot be stored as the 
     * names would clash. This is seen as preferable to allowing nodes to be 
     * aliased.
     * 
     * @param arg A node to store in this map. The node will later be 
     *   accessible using the value of its <code>nodeName</code> attribute.
     * @return If the new <code>Node</code> replaces an existing node the 
     *   replaced <code>Node</code> is returned, otherwise <code>null</code> 
     *   is returned.
     * @exception DOMException
     *   WRONG_DOCUMENT_ERR: Raised if <code>arg</code> was created from a 
     *   different document than the one that created this map.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this map is readonly.
     *   <br>INUSE_ATTRIBUTE_ERR: Raised if <code>arg</code> is an 
     *   <code>Attr</code> that is already an attribute of another 
     *   <code>Element</code> object. The DOM user must explicitly clone 
     *   <code>Attr</code> nodes to re-use them in other elements.
     *   <br>HIERARCHY_REQUEST_ERR: Raised if an attempt is made to add a node 
     *   doesn't belong in this NamedNodeMap. Examples would include trying 
     *   to insert something other than an Attr node into an Element's map 
     *   of attributes, or a non-Entity node into the DocumentType's map of 
     *   Entities.
     * @see org.w3c.dom.NamedNodeMap#setNamedItem(org.w3c.dom.Node)
     */
    public Node setNamedItem(final Node arg) throws DOMException {
        
        if (arg instanceof Attr) {
            if (((Attr) arg).getOwnerElement() != null) {
                throw new DOMException(
                        DOMException.INUSE_ATTRIBUTE_ERR,
                        "The Attr node is allready in use.");
            }
            ((AttrImpl) arg).setOwnerElement((ElementImpl) this.owner);
        } else {
            this.owner.acceptNode(arg);
        }
        
        Node oldNode = null;
        if (this.items.containsKey(arg.getNodeName())) {
            oldNode = (Node) this.items.get(arg.getNodeName());
            if (oldNode instanceof Attr) {
                ((AttrImpl) oldNode).setOwnerElement(null);
            }
        }
        this.items.put(arg.getNodeName(), arg);
        // TODO Exceptions
        return oldNode;
    }

    /**
     * Removes a node specified by name. When this map contains the attributes 
     * attached to an element, if the removed attribute is known to have a 
     * default value, an attribute immediately appears containing the 
     * default value as well as the corresponding namespace URI, local name, 
     * and prefix when applicable.
     * @param name The <code>nodeName</code> of the node to remove.
     * @return The node removed from this map if a node with such a name 
     *   exists.
     * @exception DOMException
     *   NOT_FOUND_ERR: Raised if there is no node named <code>name</code> in 
     *   this map.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this map is readonly.
     * @see org.w3c.dom.NamedNodeMap#removeNamedItem(java.lang.String)
     */
    public Node removeNamedItem(final String name) throws DOMException {
        if (!this.items.containsKey(name)) {
            throw new DOMException(
                    DOMException.NOT_FOUND_ERR,
                    "Cannot find node for name [" + name + "].");
        }
        Node removedNode = (Node) this.items.remove(name);
        if (removedNode instanceof Attr) {
            ((AttrImpl) removedNode).setOwnerElement(null);
        }
        return removedNode;
    }

    /**
     * Returns the <code>index</code>th item in the map. If <code>index</code> 
     * is greater than or equal to the number of nodes in this map, this 
     * returns <code>null</code>.
     * 
     * @param index Index into this map.
     * @return The node at the <code>index</code>th position in the map, or 
     *   <code>null</code> if that is not a valid index.
     * @see org.w3c.dom.NamedNodeMap#item(int)
     */
    public Node item(final int index) {
        Node item = null;
        
        int size = this.items.size();
        if (index < size) {
            Node[] nodes = (Node[]) this.items.values().toArray(new Node[size]);
        	item = nodes[index];
        }
        return item;
    }

    /**
     * The number of nodes in this map. The range of valid child node indices 
     * is <code>0</code> to <code>length-1</code> inclusive.
     * 
     * @return ---
     * @see org.w3c.dom.NamedNodeMap#getLength()
     */
    public int getLength() {
        return this.items.size();
    }

    /**
     * Retrieves a node specified by local name and namespace URI.
     * <br>Documents which do not support the "XML" feature will permit only 
     * the DOM Level 1 calls for creating/setting elements and attributes. 
     * Hence, if you specify a non-null namespace URI, these DOMs will never 
     * find a matching node.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param namespaceURI The namespace URI of the node to retrieve.
     * @param localName The local name of the node to retrieve.
     * @return A <code>Node</code> (of any type) with the specified local 
     *   name and namespace URI, or <code>null</code> if they do not 
     *   identify any node in this map.
     * @since DOM Level 2
     * @see org.w3c.dom.NamedNodeMap#getNamedItemNS(
     * 		java.lang.String, java.lang.String)
     */
    public Node getNamedItemNS(final String namespaceURI, 
                               final String localName) {
        
        Node namedItem = null;
        
        Iterator iter = this.items.values().iterator();
        while (iter.hasNext()) {
            Node node = (Node) iter.next();
             
            if (node.getNamespaceURI() != null
                && node.getNamespaceURI().equals(namespaceURI)
                && node.getLocalName().equals(localName)) {
                
                namedItem = node;
                break;
            } else if (node instanceof Attr && owner instanceof Element) {
                if (node.getNodeName().equals(localName)
                        && owner.getNamespaceURI() != null
                        && owner.getNamespaceURI().equals(namespaceURI)) {
                    
                    namedItem = node;
                    break;
                }
                
            }
        }
        return namedItem;
    }

    /**
     * Adds a node using its <code>namespaceURI</code> and 
     * <code>localName</code>. If a node with that namespace URI and that 
     * local name is already present in this map, it is replaced by the new 
     * one.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param arg A node to store in this map. The node will later be 
     *   accessible using the value of its <code>namespaceURI</code> and 
     *   <code>localName</code> attributes.
     * @return If the new <code>Node</code> replaces an existing node the 
     *   replaced <code>Node</code> is returned, otherwise <code>null</code> 
     *   is returned.
     * @exception DOMException
     *   WRONG_DOCUMENT_ERR: Raised if <code>arg</code> was created from a 
     *   different document than the one that created this map.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this map is readonly.
     *   <br>INUSE_ATTRIBUTE_ERR: Raised if <code>arg</code> is an 
     *   <code>Attr</code> that is already an attribute of another 
     *   <code>Element</code> object. The DOM user must explicitly clone 
     *   <code>Attr</code> nodes to re-use them in other elements.
     *   <br>HIERARCHY_REQUEST_ERR: Raised if an attempt is made to add a node 
     *   doesn't belong in this NamedNodeMap. Examples would include trying 
     *   to insert something other than an Attr node into an Element's map 
     *   of attributes, or a non-Entity node into the DocumentType's map of 
     *   Entities.
     *   <br>NOT_SUPPORTED_ERR: Always thrown if the current document does not 
     *   support the <code>"XML"</code> feature, since namespaces were 
     *   defined by XML.
     * @since DOM Level 2
     * @see org.w3c.dom.NamedNodeMap#setNamedItemNS(org.w3c.dom.Node)
     */
    public Node setNamedItemNS(final Node arg) throws DOMException {
        return this.setNamedItem(arg);
    }

    /**
     * Removes a node specified by local name and namespace URI. A removed 
     * attribute may be known to have a default value when this map contains 
     * the attributes attached to an element, as returned by the attributes 
     * attribute of the <code>Node</code> interface. If so, an attribute 
     * immediately appears containing the default value as well as the 
     * corresponding namespace URI, local name, and prefix when applicable.
     * <br>Documents which do not support the "XML" feature will permit only 
     * the DOM Level 1 calls for creating/setting elements and attributes. 
     * Hence, if you specify a non-null namespace URI, these DOMs will never 
     * find a matching node.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param namespaceURI The namespace URI of the node to remove.
     * @param localName The local name of the node to remove.
     * @return The node removed from this map if a node with such a local 
     *   name and namespace URI exists.
     * @exception DOMException
     *   NOT_FOUND_ERR: Raised if there is no node with the specified 
     *   <code>namespaceURI</code> and <code>localName</code> in this map.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this map is readonly.
     * @since DOM Level 2
     * @see org.w3c.dom.NamedNodeMap#removeNamedItemNS(
     * 		java.lang.String, java.lang.String)
     */
    public Node removeNamedItemNS(final String namespaceURI, 
                                  final String localName)
            throws DOMException {
        
        Node node = this.getNamedItemNS(namespaceURI, localName);
        if (node != null) {
            this.removeNamedItem(node.getNodeName());
        }
        return node;
    }

}
