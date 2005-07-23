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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;


/**
 * The <code>Attr</code> interface represents an attribute in an 
 * <code>Element</code> object. Typically the allowable values for the 
 * attribute are defined in a document type definition.
 * <p><code>Attr</code> objects inherit the <code>Node</code> interface, but 
 * since they are not actually child nodes of the element they describe, the 
 * DOM does not consider them part of the document tree. Thus, the 
 * <code>Node</code> attributes <code>parentNode</code>, 
 * <code>previousSibling</code>, and <code>nextSibling</code> have a 
 * <code>null</code> value for <code>Attr</code> objects. The DOM takes the 
 * view that attributes are properties of elements rather than having a 
 * separate identity from the elements they are associated with; this should 
 * make it more efficient to implement such features as default attributes 
 * associated with all elements of a given type. Furthermore, 
 * <code>Attr</code> nodes may not be immediate children of a 
 * <code>DocumentFragment</code>. However, they can be associated with 
 * <code>Element</code> nodes contained within a 
 * <code>DocumentFragment</code>. In short, users and implementors of the 
 * DOM need to be aware that <code>Attr</code> nodes have some things in 
 * common with other objects inheriting the <code>Node</code> interface, but 
 * they also are quite distinct.
 * <p>The attribute's effective value is determined as follows: if this 
 * attribute has been explicitly assigned any value, that value is the 
 * attribute's effective value; otherwise, if there is a declaration for 
 * this attribute, and that declaration includes a default value, then that 
 * default value is the attribute's effective value; otherwise, the 
 * attribute does not exist on this element in the structure model until it 
 * has been explicitly added. Note that the <code>nodeValue</code> attribute 
 * on the <code>Attr</code> instance can also be used to retrieve the string 
 * version of the attribute's value(s).
 * <p>In XML, where the value of an attribute can contain entity references, 
 * the child nodes of the <code>Attr</code> node may be either 
 * <code>Text</code> or <code>EntityReference</code> nodes (when these are 
 * in use; see the description of <code>EntityReference</code> for 
 * discussion). Because the DOM Core is not aware of attribute types, it 
 * treats all attribute values as simple strings, even if the DTD or schema 
 * declares them as having tokenized types.
 * <p>The DOM implementation does not perform any attribute value normalization
 * . While it is expected that the <code>value</code> and 
 * <code>nodeValue</code> attributes of an <code>Attr</code> node initially 
 * return the normalized value, this may not be the case after mutation. 
 * This is true, independently of whether the mutation is performed by 
 * setting the string value directly or by changing the <code>Attr</code> 
 * child nodes. In particular, this is true when character entity references 
 * are involved, given that they are not represented in the DOM and they 
 * impact attribute value normalization. The properties [attribute type] and 
 * [references] defined in  are not accessible from DOM Level 3 Core. 
 * However,  does provide a way to access the property [attribute type]. 
 * <p>See also the <a
 * href='http://www.w3.org/TR/2002/WD-DOM-Level-3-Core-20020114'
 * >Document Object Model (DOM) Level 3 Core Specification</a>.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class AttrNSImpl extends AttrImpl {

    /**
     * Comment for <code>qName</code>
     * @clientCardinality 1
     * @clientRole qName
     * @directed true
     * @supplierCardinality 1
     */
    private final QName qName;    
    
    /**
     * @param ownerIn ..
     * @param namespaceURIIn The namespace URI of the attribute to create.
     * @param qualifiedNameIn The qualified name of the attribute to 
     *   instantiate.
     * @throws DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified qualified name 
     *   contains an illegal character, per the XML 1.0 specification .
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
     */
    protected AttrNSImpl(final DocumentImpl ownerIn, 
                         final String namespaceURIIn, 
                         final String qualifiedNameIn) throws DOMException {
        
        super(ownerIn, qualifiedNameIn);
        
        this.qName = new QName(qualifiedNameIn, namespaceURIIn);
        if (!this.qName.isValidQName()) {
            throw new DOMException(
                    DOMException.INVALID_CHARACTER_ERR,
                    "The qualifiedName[" + qualifiedNameIn + "] is invalid.");
        }
        if (!this.qName.isValidNamespace()) {
            throw new DOMException(
                    DOMException.NAMESPACE_ERR,
                    "The combination of name[" + qualifiedNameIn + "]"
                    + " and namespaceURI[" + namespaceURIIn + "] is invalid.");
        }
    }

    /**
     * The name of this node, depending on its type; see the table above.
     * 
     * @return ---
     * @see org.w3c.dom.Node#getNodeName()
     */
    public final String getNodeName() {
        return this.qName.getQualifiedName();
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
        Attr attr = this.ownerDocument.createAttributeNS(
                this.qName.getNamespaceURI(), this.qName.getQualifiedName());
        attr.setValue(this.getValue());
        
        return attr;
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
        return this.qName.getNamespaceURI();
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
        return this.qName.getPrefix();
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
        this.qName.setPrefix(prefix);
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
        return this.qName.getLocalName();
    }

}
