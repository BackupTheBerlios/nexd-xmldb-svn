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

import org.w3c.dom.DOMException;

import de.xplib.nexd.xml.util.QName;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class ElementNSImpl extends ElementImpl {
    
    /**
     * Comment for <code>qName</code>
     */
    private QName qName = null;    

    /**
     * @param owner ..
     * @param namespaceURIIn ..
     * @param qualifiedNameIn ..
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
    public ElementNSImpl(final DocumentImpl owner, 
                         final String namespaceURIIn, 
                         final String qualifiedNameIn) throws DOMException {
        
        super(owner, qualifiedNameIn);
        
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
