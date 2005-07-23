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
 * $Log: AbstractCharacterData.java,v $
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.6  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.5  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.4  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xml.dom;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

/**
 * The <code>CharacterData</code> interface extends Node with a set of 
 * attributes and methods for accessing character data in the DOM. For 
 * clarity this set is defined here rather than on each object that uses 
 * these attributes and methods. No DOM objects correspond directly to 
 * <code>CharacterData</code>, though <code>Text</code> and others do 
 * inherit the interface from it. All <code>offsets</code> in this interface 
 * start from <code>0</code>.
 * <p>As explained in the <code>DOMString</code> interface, text strings in 
 * the DOM are represented in UTF-16, i.e. as a sequence of 16-bit units. In 
 * the following, the term 16-bit units is used whenever necessary to 
 * indicate that indexing on CharacterData is done in 16-bit units.
 * <p>See also the <a 
 * href='http://www.w3.org/TR/2002/WD-DOM-Level-3-Core-20020114'
 * >Document Object Model (DOM) Level 3 Core Specification</a>.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public abstract class AbstractCharacterData 
	extends AbstractNode 
	implements CharacterData {

    /**
     * Comment for <code>nodeValue</code>
     */
    private final StringBuilder nodeValue = new StringBuilder();

    /**
     * @param ownerIn ..
     * @param nodeNameIn ..
     * @param nodeValueIn ..
     * @param nodeTypeIn ..
     */
    public AbstractCharacterData(final DocumentImpl ownerIn, 
                                 final String nodeNameIn,
                                 final String nodeValueIn,
                                 final short nodeTypeIn) {
        super(ownerIn, nodeNameIn, nodeTypeIn);
        
        this.setNodeValue(nodeValueIn);
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
    public final void setNodeValue(final String nodeValueIn) 
            throws DOMException {
        
        this.nodeValue.replace(0, this.nodeValue.length(), nodeValueIn);
        
        if (this.internalId != null && this.ownerDocument != null) {
            this.ownerDocument.cache.change(null, this);
        }
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
        return this.nodeValue.toString();
    }
    
    //
    // Methods from org.w3c.dom.CharacterData
    //

    /**
     * The character data of the node that implements this interface. The DOM 
     * implementation may not put arbitrary limits on the amount of data 
     * that may be stored in a <code>CharacterData</code> node. However, 
     * implementation limits may mean that the entirety of a node's data may 
     * not fit into a single <code>DOMString</code>. In such cases, the user 
     * may call <code>substringData</code> to retrieve the data in 
     * appropriately sized pieces.
     * <br> When the <code>CharacterData</code> is a <code>Text</code>, or a 
     * <code>CDATASection</code>, this attribute contains the property 
     * [character code] defined in . When the <code>CharacterData</code> is 
     * a <code>Comment</code>, this attribute contains the property 
     * [content] defined by the Comment Information Item in .
     * 
     * @return ... 
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.
     * @exception DOMException
     *   DOMSTRING_SIZE_ERR: Raised when it would return more characters than 
     *   fit in a <code>DOMString</code> variable on the implementation 
     *   platform.
     * @see org.w3c.dom.CharacterData#getData()
     */
    public String getData() throws DOMException {
        return this.getNodeValue();
    }

    /**
     * The character data of the node that implements this interface. The DOM 
     * implementation may not put arbitrary limits on the amount of data 
     * that may be stored in a <code>CharacterData</code> node. However, 
     * implementation limits may mean that the entirety of a node's data may 
     * not fit into a single <code>DOMString</code>. In such cases, the user 
     * may call <code>substringData</code> to retrieve the data in 
     * appropriately sized pieces.
     * <br> When the <code>CharacterData</code> is a <code>Text</code>, or a 
     * <code>CDATASection</code>, this attribute contains the property 
     * [character code] defined in . When the <code>CharacterData</code> is 
     * a <code>Comment</code>, this attribute contains the property 
     * [content] defined by the Comment Information Item in .
     * 
     * @param data .. 
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.
     * @exception DOMException
     *   DOMSTRING_SIZE_ERR: Raised when it would return more characters than 
     *   fit in a <code>DOMString</code> variable on the implementation 
     *   platform.
     * @see org.w3c.dom.CharacterData#setData(java.lang.String)
     */
    public void setData(final String data) throws DOMException {
        this.setNodeValue(data);
    }

    /**
     * The number of 16-bit units that are available through <code>data</code> 
     * and the <code>substringData</code> method below. This may have the 
     * value zero, i.e., <code>CharacterData</code> nodes may be empty.
     * 
     * @return The text length
     * @see org.w3c.dom.CharacterData#getLength()
     */
    public int getLength() {
        return this.nodeValue.length();
    }

    /**
     * Extracts a range of data from the node.
     * 
     * @param offset Start offset of substring to extract.
     * @param count The number of 16-bit units to extract.
     * @return The specified substring. If the sum of <code>offset</code> and 
     *   <code>count</code> exceeds the <code>length</code>, then all 16-bit 
     *   units to the end of the data are returned.
     * @exception DOMException
     *   INDEX_SIZE_ERR: Raised if the specified <code>offset</code> is 
     *   negative or greater than the number of 16-bit units in 
     *   <code>data</code>, or if the specified <code>count</code> is 
     *   negative.
     *   <br>DOMSTRING_SIZE_ERR: Raised if the specified range of text does 
     *   not fit into a <code>DOMString</code>.
     * @see org.w3c.dom.CharacterData#substringData(int, int)
     */
    public String substringData(final int offset, 
                                final int count) throws DOMException {
        
        if (count < 0 || offset < 0 || offset > this.getLength()) {
            throw new DOMException(
                    DOMException.INDEX_SIZE_ERR, 
                    "The method substringData only accepts positive values.");
        }
        
        int end = offset + count;
        if (end > this.getLength()) {
            end = this.getLength();
        }
        return this.nodeValue.substring(offset, end);
    }

    /**
     * Append the string to the end of the character data of the node. Upon 
     * success, <code>data</code> provides access to the concatenation of 
     * <code>data</code> and the <code>DOMString</code> specified.
     * 
     * @param arg The <code>DOMString</code> to append.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     * @see org.w3c.dom.CharacterData#appendData(java.lang.String)
     */
    public final void appendData(final String arg) throws DOMException {
        
        this.nodeValue.append(arg);
        
        if (this.internalId != null && this.ownerDocument != null) {
            this.ownerDocument.cache.change(null, this);
        }
    }

    /**
     * Insert a string at the specified 16-bit unit offset.
     * 
     * @param offset The character offset at which to insert.
     * @param arg The <code>DOMString</code> to insert.
     * @exception DOMException
     *   INDEX_SIZE_ERR: Raised if the specified <code>offset</code> is 
     *   negative or greater than the number of 16-bit units in 
     *   <code>data</code>.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     * @see org.w3c.dom.CharacterData#insertData(int, java.lang.String)
     */
    public final void insertData(final int offset, 
                                 final String arg) throws DOMException {
        
        if (offset < 0 || offset > this.getLength()) {
            throw new DOMException(
                    DOMException.INDEX_SIZE_ERR, 
                    "The method insertData only accepts positive offsets.");
        }
        
        this.nodeValue.insert(offset, arg);
        
        if (this.internalId != null && this.ownerDocument != null) {
            this.ownerDocument.cache.change(null, this);
        }
    }

    /**
     * Remove a range of 16-bit units from the node. Upon success, 
     * <code>data</code> and <code>length</code> reflect the change.
     * 
     * @param offset The offset from which to start removing.
     * @param count The number of 16-bit units to delete. If the sum of 
     *   <code>offset</code> and <code>count</code> exceeds 
     *   <code>length</code> then all 16-bit units from <code>offset</code> 
     *   to the end of the data are deleted.
     * @exception DOMException
     *   INDEX_SIZE_ERR: Raised if the specified <code>offset</code> is 
     *   negative or greater than the number of 16-bit units in 
     *   <code>data</code>, or if the specified <code>count</code> is 
     *   negative.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     * @see org.w3c.dom.CharacterData#deleteData(int, int)
     */
    public final void deleteData(final int offset, 
                                 final int count) throws DOMException {
        
        if (count < 0 || offset < 0 || offset > this.getLength()) {
            throw new DOMException(
                    DOMException.INDEX_SIZE_ERR, 
                    "The method deleteData only accepts positive values.");
        }
        
        int end = offset + count;
        if (end > this.getLength()) {
            end = this.getLength();
        }
        
        this.nodeValue.delete(offset, end);
        
        if (this.internalId != null && this.ownerDocument != null) {
            this.ownerDocument.cache.change(null, this);
        }
    }

    /**
     * Replace the characters starting at the specified 16-bit unit offset 
     * with the specified string.
     * 
     * @param offset The offset from which to start replacing.
     * @param count The number of 16-bit units to replace. If the sum of 
     *   <code>offset</code> and <code>count</code> exceeds 
     *   <code>length</code>, then all 16-bit units to the end of the data 
     *   are replaced; (i.e., the effect is the same as a <code>remove</code>
     *    method call with the same range, followed by an <code>append</code>
     *    method invocation).
     * @param arg The <code>DOMString</code> with which the range must be 
     *   replaced.
     * @exception DOMException
     *   INDEX_SIZE_ERR: Raised if the specified <code>offset</code> is 
     *   negative or greater than the number of 16-bit units in 
     *   <code>data</code>, or if the specified <code>count</code> is 
     *   negative.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     * @see org.w3c.dom.CharacterData#replaceData(int, int, java.lang.String)
     */
    public final void replaceData(final int offset, 
                                  final int count, 
                                  final String arg) throws DOMException {
        
        if (count < 0 || offset < 0 || offset > this.getLength()) {
            throw new DOMException(
                    DOMException.INDEX_SIZE_ERR, 
                    "The method replaceData only accepts positive values.");
        }
        
        int end = offset + count;
        if (end > this.getLength()) {
            end = this.getLength();
        }
        this.nodeValue.replace(offset, end, arg);
        
        if (this.internalId != null && this.ownerDocument != null) {
            this.ownerDocument.cache.change(null, this);
        }
    }

}
