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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

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
public class AttrImpl extends AbstractParentNode implements Attr {
    

    /**
     * Comment for <code>nodeValue</code>
     */
    private StringBuilder nodeValue = new StringBuilder();
    
    /**
     * Comment for <code>ownerElement</code>
     */
    private ElementImpl ownerElement = null;
    

    /**
     * @param ownerIn ..
     * @param nodeNameIn ..
     */
    protected AttrImpl(final DocumentImpl ownerIn, 
                       final String nodeNameIn) {
        
        super(ownerIn, nodeNameIn, Node.ATTRIBUTE_NODE);
    }
    
    //
    // Methods from org.w3c.dom.Node
    //
    
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
        return this.nodeValue.toString();
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
     * @param nodeValueIn ---
     * @see org.w3c.dom.Node#setNodeValue(java.lang.String)
     */
    public void setNodeValue(final String nodeValueIn) throws DOMException {
        this.nodeValue.replace(0, this.nodeValue.length(), nodeValueIn);
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
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the name of this attribute.
     * 
     * @return The name of this attribute.
     * @see org.w3c.dom.Attr#getName()
     */
    public String getName() {
        return this.getNodeName();
    }

    /**
     * If this attribute was explicitly given a value in the original 
     * document, this is <code>true</code>; otherwise, it is 
     * <code>false</code>. Note that the implementation is in charge of this 
     * attribute, not the user. If the user changes the value of the 
     * attribute (even if it ends up having the same value as the default 
     * value) then the <code>specified</code> flag is automatically flipped 
     * to <code>true</code>. To re-specify the attribute as the default 
     * value from the DTD, the user must delete the attribute. The 
     * implementation will then make a new attribute available with 
     * <code>specified</code> set to <code>false</code> and the default 
     * value (if one exists).
     * <br>In summary: If the attribute has an assigned value in the document 
     * then <code>specified</code> is <code>true</code>, and the value is 
     * the assigned value.If the attribute has no assigned value in the 
     * document and has a default value in the DTD, then 
     * <code>specified</code> is <code>false</code>, and the value is the 
     * default value in the DTD.If the attribute has no assigned value in 
     * the document and has a value of #IMPLIED in the DTD, then the 
     * attribute does not appear in the structure model of the document.If 
     * the <code>ownerElement</code> attribute is <code>null</code> (i.e. 
     * because it was just created or was set to <code>null</code> by the 
     * various removal and cloning operations) <code>specified</code> is 
     * <code>true</code>. 
     * <br> This attribute represents the property [specified] defined .
     *  
     * @return ...
     * @see org.w3c.dom.Attr#getSpecified()
     */
    public boolean getSpecified() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * On retrieval, the value of the attribute is returned as a string. 
     * Character and general entity references are replaced with their 
     * values. See also the method <code>getAttribute</code> on the 
     * <code>Element</code> interface.
     * <br>On setting, this creates a <code>Text</code> node with the unparsed 
     * contents of the string. I.e. any characters that an XML processor 
     * would recognize as markup are instead treated as literal text. See 
     * also the method <code>setAttribute</code> on the <code>Element</code> 
     * interface.
     * <br> If the <code>value</code> does contain the normalized attribute 
     * value, this attribute represents the property [normalized value] 
     * defined in .
     * 
     * @return The value of this attribute.
     * @see org.w3c.dom.Attr#getValue()
     */
    public String getValue() {
        return this.getNodeValue();
    }

    /**
     * On retrieval, the value of the attribute is returned as a string. 
     * Character and general entity references are replaced with their 
     * values. See also the method <code>getAttribute</code> on the 
     * <code>Element</code> interface.
     * <br>On setting, this creates a <code>Text</code> node with the unparsed 
     * contents of the string. I.e. any characters that an XML processor 
     * would recognize as markup are instead treated as literal text. See 
     * also the method <code>setAttribute</code> on the <code>Element</code> 
     * interface.
     * <br> If the <code>value</code> does contain the normalized attribute 
     * value, this attribute represents the property [normalized value] 
     * defined in .
     * 
     * @param value New value for this attribute.
     * @see org.w3c.dom.Attr#setValue(java.lang.String)
     */
    public void setValue(final String value) {
        this.setNodeValue(value);
    }

    /**
     * The <code>Element</code> node this attribute is attached to or 
     * <code>null</code> if this attribute is not in use.
     * <br> This attribute represents the property [owner element] defined in.
     *  
     * @since DOM Level 2
     * @return The owner <code>Element</code> of this attribute. 
     * @see org.w3c.dom.Attr#getOwnerElement()
     */
    public Element getOwnerElement() {
        return this.ownerElement;
    }
    
    /**
     * <Some description here>
     * 
     * @return
     * @see org.w3c.dom.Attr#getSchemaTypeInfo()
     */
    public TypeInfo getSchemaTypeInfo() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * <Some description here>
     * 
     * @return
     * @see org.w3c.dom.Attr#isId()
     */
    public boolean isId() {
        // TODO Auto-generated method stub
        return false;
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
        // TODO Auto-generated method stub
        return 2;
    }
    
    /**
     * @param ownerElementIn ..
     */
    protected void setOwnerElement(final ElementImpl ownerElementIn) {
        this.ownerElement = ownerElementIn;
    }
}
