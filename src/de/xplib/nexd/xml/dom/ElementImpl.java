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

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class ElementImpl extends AbstractParentNode implements Element {
    
    
    /**
     * Comment for <code>attributes</code>
     */
    private NamedNodeMapImpl attributes = new NamedNodeMapImpl(this);
    
    
    /**
     * @param owner ..
     * @param tagNameIn ..
     */ 
    protected ElementImpl(final DocumentImpl owner, final String tagNameIn) {
        super(owner, tagNameIn, Node.ELEMENT_NODE);
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
        return this.attributes;
    }

    /**
     * Returns a duplicate of this node, i.e., serves as a generic copy 
     * constructor for nodes. The duplicate node has no parent; (
     * <code>parentNode</code> is <code>null</code>.) and no user data. User 
     * data associated to the imported node is not carried over. However, if 
     * any <code>UserDataHandlers</code> has been specified along with the 
     * associated data these handlers will be called with the appropriate 
     * parameters before this method returns.
     * <br>Cloning an <code>Element</code> copies all attributes and their 
     * values, including those generated by the XML processor to represent 
     * defaulted attributes, but this method does not copy any children it 
     * contains unless it is a deep clone. This includes text contained in 
     * an the <code>Element</code> since the text is contained in a child 
     * <code>Text</code> node. Cloning an <code>Attribute</code> directly, 
     * as opposed to be cloned as part of an <code>Element</code> cloning 
     * operation, returns a specified attribute (<code>specified</code> is 
     * <code>true</code>). Cloning an <code>Attribute</code> always clones 
     * its children, since they represent its value, no matter whether this 
     * is a deep clone or not. Cloning an <code>EntityReference</code> 
     * automatically constructs its subtree if a corresponding 
     * <code>Entity</code> is available, no matter whether this is a deep 
     * clone or not. Cloning any other type of node simply returns a copy of 
     * this node.
     * <br>Note that cloning an immutable subtree results in a mutable copy, 
     * but the children of an <code>EntityReference</code> clone are readonly
     * . In addition, clones of unspecified <code>Attr</code> nodes are 
     * specified. And, cloning <code>Document</code>, 
     * <code>DocumentType</code>, <code>Entity</code>, and 
     * <code>Notation</code> nodes is implementation dependent.
     * 
     * @param deep If <code>true</code>, recursively clone the subtree under 
     *   the specified node; if <code>false</code>, clone only the node 
     *   itself (and its attributes, if it is an <code>Element</code>).
     * @return The duplicate node.
     * @see org.w3c.dom.Node#cloneNode(boolean)
     */
    public Node cloneNode(final boolean deep) {
        // TODO : Auto-generated method stub
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
        return (this.attributes.getLength() > 0);
    }
    
    /**
     * The name of the element. For example, in: 
     * <pre> &lt;elementExample 
     * id="demo"&gt; ... &lt;/elementExample&gt; , </pre>
     *  <code>tagName</code> has 
     * the value <code>"elementExample"</code>. Note that this is 
     * case-preserving in XML, as are all of the operations of the DOM. The 
     * HTML DOM returns the <code>tagName</code> of an HTML element in the 
     * canonical uppercase form, regardless of the case in the source HTML 
     * document.
     * 
     * @return ---
     * @see org.w3c.dom.Element#getTagName()
     */
    public String getTagName() {
        return this.getNodeName();
    }

    /**
     * Retrieves an attribute value by name.
     * 
     * @param name The name of the attribute to retrieve.
     * @return The <code>Attr</code> value as a string, or the empty string 
     *   if that attribute does not have a specified or default value.
     * @see org.w3c.dom.Element#getAttribute(java.lang.String)
     */
    public String getAttribute(final String name) {
        return this.attributes.getNamedItem(name).getNodeValue();
    }

    /**
     * Adds a new attribute. If an attribute with that name is already present 
     * in the element, its value is changed to be that of the value 
     * parameter. This value is a simple string; it is not parsed as it is 
     * being set. So any markup (such as syntax to be recognized as an 
     * entity reference) is treated as literal text, and needs to be 
     * appropriately escaped by the implementation when it is written out. 
     * In order to assign an attribute value that contains entity 
     * references, the user must create an <code>Attr</code> node plus any 
     * <code>Text</code> and <code>EntityReference</code> nodes, build the 
     * appropriate subtree, and use <code>setAttributeNode</code> to assign 
     * it as the value of an attribute.
     * <br>To set an attribute with a qualified name and namespace URI, use 
     * the <code>setAttributeNS</code> method.
     * 
     * @param name The name of the attribute to create or alter.
     * @param value Value to set in string form.
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified name contains an 
     *   illegal character.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     * @see org.w3c.dom.Element#setAttribute(java.lang.String, java.lang.String)
     */
    public void setAttribute(final String name, 
                             final String value) throws DOMException {

        AttrImpl attr = 
            	(AttrImpl) this.getOwnerDocument().createAttribute(name);
        attr.setValue(value);
        
        this.setAttributeNode(attr);
    }

    /**
     * Removes an attribute by name. If the removed attribute is known to have 
     * a default value, an attribute immediately appears containing the 
     * default value as well as the corresponding namespace URI, local name, 
     * and prefix when applicable. If the attribute does not have a 
     * specified or default value, calling this method has no effect.
     * <br>To remove an attribute by local name and namespace URI, use the 
     * <code>removeAttributeNS</code> method.
     * 
     * @param name The name of the attribute to remove.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     * @see org.w3c.dom.Element#removeAttribute(java.lang.String)
     */
    public void removeAttribute(final String name) throws DOMException {
        try {
            AttrImpl attr = (AttrImpl) this.attributes.removeNamedItem(name);
            attr.setOwnerElement(null);
        } catch (DOMException e) {
            if (e.code != DOMException.NOT_FOUND_ERR) {
                throw e;
            }
        }
    }

    /**
     * Retrieves an attribute node by name.
     * <br>To retrieve an attribute node by qualified name and namespace URI, 
     * use the <code>getAttributeNodeNS</code> method.
     * 
     * @param name The name (<code>nodeName</code>) of the attribute to 
     *   retrieve.
     * @return The <code>Attr</code> node with the specified name (
     *   <code>nodeName</code>) or <code>null</code> if there is no such 
     *   attribute.
     * @see org.w3c.dom.Element#getAttributeNode(java.lang.String)
     */
    public Attr getAttributeNode(final String name) {
        return (Attr) this.attributes.getNamedItem(name);
    }

    /**
     * Adds a new attribute node. If an attribute with that name (
     * <code>nodeName</code>) is already present in the element, it is 
     * replaced by the new one.
     * <br>To add a new attribute node with a qualified name and namespace 
     * URI, use the <code>setAttributeNodeNS</code> method.
     * 
     * @param newAttr The <code>Attr</code> node to add to the attribute list.
     * @return If the <code>newAttr</code> attribute replaces an existing 
     *   attribute, the replaced <code>Attr</code> node is returned, 
     *   otherwise <code>null</code> is returned.
     * @exception DOMException
     *   WRONG_DOCUMENT_ERR: Raised if <code>newAttr</code> was created from a 
     *   different document than the one that created the element.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>INUSE_ATTRIBUTE_ERR: Raised if <code>newAttr</code> is already an 
     *   attribute of another <code>Element</code> object. The DOM user must 
     *   explicitly clone <code>Attr</code> nodes to re-use them in other 
     *   elements.
     * @see org.w3c.dom.Element#setAttributeNode(org.w3c.dom.Attr)
     */
    public Attr setAttributeNode(final Attr newAttr) throws DOMException {
        
        Node oldAttr = this.attributes.setNamedItem(newAttr);
        ((AttrImpl) newAttr).setOwnerElement(this);

        return (Attr) oldAttr;
    }

    /**
     * Removes the specified attribute node. If the removed <code>Attr</code> 
     * has a default value it is immediately replaced. The replacing 
     * attribute has the same namespace URI and local name, as well as the 
     * original prefix, when applicable.
     * 
     * @param oldAttr The <code>Attr</code> node to remove from the attribute 
     *   list.
     * @return The <code>Attr</code> node that was removed.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if <code>oldAttr</code> is not an attribute 
     *   of the element.
     * @see org.w3c.dom.Element#removeAttributeNode(org.w3c.dom.Attr)
     */
    public Attr removeAttributeNode(final Attr oldAttr) throws DOMException {
        return (Attr) this.attributes.removeNamedItem(oldAttr.getNodeName());
    }

    /**
     * Returns a <code>NodeList</code> of all descendant <code>Elements</code> 
     * with a given tag name, in document order.
     * 
     * @param name The name of the tag to match on. The special value "*" 
     *   matches all tags.
     * @return A list of matching <code>Element</code> nodes.
     * @see org.w3c.dom.Element#getElementsByTagName(java.lang.String)
     */
    public NodeList getElementsByTagName(final String name) {
        
        NodeListImpl nodeList = new NodeListImpl(this);
        List list = nodeList.getList();
        
        Iterator it = ((NodeListImpl) this.getChildNodes())
        		.getList().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (!(o instanceof ElementImpl)) {
                continue;
            }

            ElementImpl elem = (ElementImpl) o;
            if (elem.getTagName().equals(name) || name.equals("*")) {
                list.add(elem);
            }
            list.addAll(
                    ((NodeListImpl) elem.getElementsByTagName(name)).getList());
        }
        return nodeList;
    }

    /**
     * Retrieves an attribute value by local name and namespace URI.
     * <br>Documents which do not support the "XML" feature will permit only 
     * the DOM Level 1 calls for creating/setting elements and attributes. 
     * Hence, if you specify a non-null namespace URI, these DOMs will never 
     * find a matching node.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param namespaceURI The namespace URI of the attribute to retrieve.
     * @param localName The local name of the attribute to retrieve.
     * @return The <code>Attr</code> value as a string, or the empty string 
     *   if that attribute does not have a specified or default value.
     * @since DOM Level 2
     * @see org.w3c.dom.Element#getAttributeNS(
     * 		java.lang.String, java.lang.String)
     */
    public String getAttributeNS(final String namespaceURI, 
                                 final String localName) {

        Node n = this.attributes.getNamedItemNS(namespaceURI, localName);
        if (n != null) {
            return n.getNodeValue();
        }
        return "";
    }

    /**
     * Adds a new attribute. If an attribute with the same local name and 
     * namespace URI is already present on the element, its prefix is 
     * changed to be the prefix part of the <code>qualifiedName</code>, and 
     * its value is changed to be the <code>value</code> parameter. This 
     * value is a simple string; it is not parsed as it is being set. So any 
     * markup (such as syntax to be recognized as an entity reference) is 
     * treated as literal text, and needs to be appropriately escaped by the 
     * implementation when it is written out. In order to assign an 
     * attribute value that contains entity references, the user must create 
     * an <code>Attr</code> node plus any <code>Text</code> and 
     * <code>EntityReference</code> nodes, build the appropriate subtree, 
     * and use <code>setAttributeNodeNS</code> or 
     * <code>setAttributeNode</code> to assign it as the value of an 
     * attribute.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param namespaceURI The namespace URI of the attribute to create or 
     *   alter.
     * @param qualifiedName The qualified name of the attribute to create or 
     *   alter.
     * @param value The value to set in string form.
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified qualified name 
     *   contains an illegal character, per the XML 1.0 specification .
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is 
     *   malformed per the Namespaces in XML specification, if the 
     *   <code>qualifiedName</code> has a prefix and the 
     *   <code>namespaceURI</code> is <code>null</code>, if the 
     *   <code>qualifiedName</code> has a prefix that is "xml" and the 
     *   <code>namespaceURI</code> is different from "
     *   http://www.w3.org/XML/1998/namespace", or if the 
     *   <code>qualifiedName</code>, or its prefix, is "xmlns" and the 
     *   <code>namespaceURI</code> is different from "
     *   http://www.w3.org/2000/xmlns/".
     *   <br>NOT_SUPPORTED_ERR: Always thrown if the current document does not 
     *   support the <code>"XML"</code> feature, since namespaces were 
     *   defined by XML.
     * @since DOM Level 2
     * @see org.w3c.dom.Element#setAttributeNS(
     * 		java.lang.String, java.lang.String, java.lang.String)
     */
    public void setAttributeNS(final String namespaceURI, 
                               final String qualifiedName,
                               final String value) throws DOMException {
        
        Attr attr = this.getOwnerDocument()
        		.createAttributeNS(namespaceURI, qualifiedName);
        attr.setValue(value);
        
        this.attributes.setNamedItemNS(attr);
    }

    /**
     * Removes an attribute by local name and namespace URI. If the removed 
     * attribute has a default value it is immediately replaced. The 
     * replacing attribute has the same namespace URI and local name, as 
     * well as the original prefix. If the attribute does not have a 
     * specified or default value, calling this method has no effect.
     * <br>Documents which do not support the "XML" feature will permit only 
     * the DOM Level 1 calls for creating/setting elements and attributes. 
     * Hence, if you specify a non-null namespace URI, these DOMs will never 
     * find a matching node.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param namespaceURI The namespace URI of the attribute to remove.
     * @param localName The local name of the attribute to remove.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     * @since DOM Level 2
     * @see org.w3c.dom.Element#removeAttributeNS(
     * 		java.lang.String, java.lang.String)
     */
    public void removeAttributeNS(final String namespaceURI, 
                                  final String localName) throws DOMException {
        
        try {
            this.attributes.removeNamedItemNS(namespaceURI, localName);
        } catch (DOMException e) {
            if (e.code != DOMException.NOT_FOUND_ERR) {
                throw e;
            }
        }
    }

    /**
     * Retrieves an <code>Attr</code> node by local name and namespace URI.
     * <br>Documents which do not support the "XML" feature will permit only 
     * the DOM Level 1 calls for creating/setting elements and attributes. 
     * Hence, if you specify a non-null namespace URI, these DOMs will never 
     * find a matching node.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param namespaceURI The namespace URI of the attribute to retrieve.
     * @param localName The local name of the attribute to retrieve.
     * @return The <code>Attr</code> node with the specified attribute local 
     *   name and namespace URI or <code>null</code> if there is no such 
     *   attribute.
     * @since DOM Level 2
     * @see org.w3c.dom.Element#getAttributeNodeNS(
     * 		java.lang.String, java.lang.String)
     */
    public Attr getAttributeNodeNS(final String namespaceURI, 
                                   final String localName) {

        return (Attr) this.attributes.getNamedItemNS(namespaceURI, localName);
    }

    /**
     * Adds a new attribute. If an attribute with that local name and that 
     * namespace URI is already present in the element, it is replaced by 
     * the new one.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param newAttr The <code>Attr</code> node to add to the attribute list.
     * @return If the <code>newAttr</code> attribute replaces an existing 
     *   attribute with the same local name and namespace URI, the replaced 
     *   <code>Attr</code> node is returned, otherwise <code>null</code> is 
     *   returned.
     * @exception DOMException
     *   WRONG_DOCUMENT_ERR: Raised if <code>newAttr</code> was created from a 
     *   different document than the one that created the element.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>INUSE_ATTRIBUTE_ERR: Raised if <code>newAttr</code> is already an 
     *   attribute of another <code>Element</code> object. The DOM user must 
     *   explicitly clone <code>Attr</code> nodes to re-use them in other 
     *   elements.
     *   <br>NOT_SUPPORTED_ERR: Always thrown if the current document does not 
     *   support the <code>"XML"</code> feature, since namespaces were 
     *   defined by XML.
     * @since DOM Level 2
     * @see org.w3c.dom.Element#setAttributeNodeNS(org.w3c.dom.Attr)
     */
    public Attr setAttributeNodeNS(final Attr newAttr) throws DOMException {
        return (Attr) this.attributes.setNamedItemNS(newAttr);
    }

    /**
     * Returns a <code>NodeList</code> of all the descendant 
     * <code>Elements</code> with a given local name and namespace URI in 
     * document order.
     * <br>Documents which do not support the "XML" feature will permit only 
     * the DOM Level 1 calls for creating/setting elements and attributes. 
     * Hence, if you specify a non-null namespace URI, these DOMs will never 
     * find a matching node.
     * 
     * @param namespaceURI The namespace URI of the elements to match on. The 
     *   special value "*" matches all namespaces.
     * @param localName The local name of the elements to match on. The 
     *   special value "*" matches all local names.
     * @return A new <code>NodeList</code> object containing all the matched 
     *   <code>Elements</code>.
     * @since DOM Level 2
     * @see org.w3c.dom.Element#getElementsByTagNameNS(
     * 		java.lang.String, java.lang.String)
     */
    public NodeList getElementsByTagNameNS(final String namespaceURI, 
                                           final String localName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns <code>true</code> when an attribute with a given name is 
     * specified on this element or has a default value, <code>false</code> 
     * otherwise.
     * 
     * @param name The name of the attribute to look for.
     * @return <code>true</code> if an attribute with the given name is 
     *   specified on this element or has a default value, <code>false</code>
     *    otherwise.
     * @since DOM Level 2
     * @see org.w3c.dom.Element#hasAttribute(java.lang.String)
     */
    public boolean hasAttribute(final String name) {
        return (this.attributes.getNamedItem(name) != null);
    }

    /**
     * Returns <code>true</code> when an attribute with a given local name and 
     * namespace URI is specified on this element or has a default value, 
     * <code>false</code> otherwise.
     * <br>Documents which do not support the "XML" feature will permit only 
     * the DOM Level 1 calls for creating/setting elements and attributes. 
     * Hence, if you specify a non-null namespace URI, these DOMs will never 
     * find a matching node.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param namespaceURI The namespace URI of the attribute to look for.
     * @param localName The local name of the attribute to look for.
     * @return <code>true</code> if an attribute with the given local name 
     *   and namespace URI is specified or has a default value on this 
     *   element, <code>false</code> otherwise.
     * @since DOM Level 2
     * @see org.w3c.dom.Element#hasAttributeNS(
     * 		java.lang.String, java.lang.String)
     */
    public boolean hasAttributeNS(final String namespaceURI, 
                                  final String localName) {
        return this.attributes.getNamedItemNS(namespaceURI, localName) != null;
    }
    
    /**
     *  The type information associated with this element.
     * @return .. 
     * @since DOM Level 3
     * @see org.w3c.dom.Element#getSchemaTypeInfo()
     */
    public TypeInfo getSchemaTypeInfo() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     *  If the parameter <code>isId</code> is <code>true</code>, this method 
     * declares the specified attribute to be a user-determined ID attribute
     * . This affects the value of <code>Attr.isId</code> and the behavior 
     * of <code>Document.getElementById</code>, but does not change any 
     * schema that may be in use, in particular this does not affect the 
     * <code>Attr.schemaTypeInfo</code> of the specified <code>Attr</code> 
     * node. Use the value <code>false</code> for the parameter 
     * <code>isId</code> to undeclare an attribute for being a 
     * user-determined ID attribute. 
     * <br> To specify an attribute by local name and namespace URI, use the 
     * <code>setIdAttributeNS</code> method. 
     * 
     * @param name The name of the attribute.
     * @param isId Whether the attribute is a of type ID.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if the specified node is not an attribute 
     *   of this element.
     * @since DOM Level 3
     * @see org.w3c.dom.Element#setIdAttribute(java.lang.String, boolean)
     */
    public void setIdAttribute(final String name, 
                               final boolean isId) throws DOMException {
        // TODO Auto-generated method stub

    }
    
    /**
     *  If the parameter <code>isId</code> is <code>true</code>, this method 
     * declares the specified attribute to be a user-determined ID attribute
     * . This affects the value of <code>Attr.isId</code> and the behavior 
     * of <code>Document.getElementById</code>, but does not change any 
     * schema that may be in use, in particular this does not affect the 
     * <code>Attr.schemaTypeInfo</code> of the specified <code>Attr</code> 
     * node. Use the value <code>false</code> for the parameter 
     * <code>isId</code> to undeclare an attribute for being a 
     * user-determined ID attribute.
     *  
     * @param idAttr The attribute node.
     * @param isId Whether the attribute is a of type ID.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if the specified node is not an attribute 
     *   of this element.
     * @since DOM Level 3
     * @see org.w3c.dom.Element#setIdAttributeNode(org.w3c.dom.Attr, boolean)
     */
    public void setIdAttributeNode(final Attr idAttr, final boolean isId)
            throws DOMException {
        // TODO Auto-generated method stub

    }
    
    /**
     *  If the parameter <code>isId</code> is <code>true</code>, this method 
     * declares the specified attribute to be a user-determined ID attribute
     * . This affects the value of <code>Attr.isId</code> and the behavior 
     * of <code>Document.getElementById</code>, but does not change any 
     * schema that may be in use, in particular this does not affect the 
     * <code>Attr.schemaTypeInfo</code> of the specified <code>Attr</code> 
     * node. Use the value <code>false</code> for the parameter 
     * <code>isId</code> to undeclare an attribute for being a 
     * user-determined ID attribute.
     *  
     * @param namespaceURI The namespace URI of the attribute.
     * @param localName The local name of the attribute.
     * @param isId Whether the attribute is a of type ID.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if the specified node is not an attribute 
     *   of this element.
     * @since DOM Level 3
     * @see org.w3c.dom.Element#setIdAttributeNS(
     *      java.lang.String, java.lang.String, boolean)
     */
    public void setIdAttributeNS(final String namespaceURI, 
                                 final String localName,
                                 final boolean isId) throws DOMException {
        // TODO Auto-generated method stub

    }
    //
    // Methods from de.xplib.nexd.xml.dom.AbstractParentNode
    //
    
    /**
     * <Some description here>
     * 
     * @param nodeIn
     * @return
     * @throws DOMException
     * @see de.xplib.nexd.xml.dom.AbstractParentNode#acceptNode(
     * 		org.w3c.dom.Node)
     */
    protected int acceptNode(final Node nodeIn) throws DOMException {
        
        if (nodeIn.getOwnerDocument() != this.getOwnerDocument()) {
            throw new DOMException(
                    DOMException.WRONG_DOCUMENT_ERR,
                    "The node is from an other implementation.");
        }
        if (!(nodeIn instanceof CDATASectionImpl) 
            && !(nodeIn instanceof CommentImpl)
            && !(nodeIn instanceof ElementImpl)
            && !(nodeIn instanceof EntityReferenceImpl)
            && !(nodeIn instanceof ProcessingInstructionImpl)
            && !(nodeIn instanceof TextImpl)) {
            
            throw new DOMException(
                    DOMException.HIERARCHY_REQUEST_ERR,
                    "Nodes of this type[" + nodeIn.getNodeType() 
                    + ", " + nodeIn.getClass() + "] are not allowed.");
        }
        return ADD_BOTTOM;
    }

}
