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
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/**
 * The <code>DOMImplementation</code> interface provides a number of methods 
 * for performing operations that are independent of any particular instance 
 * of the document object model.
 * <p>See also the <a 
 * href='http://www.w3.org/TR/2002/WD-DOM-Level-3-Core-20020114'
 * >Document Object Model (DOM) Level 3 Core Specification</a>.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DOMImplementationImpl implements DOMImplementation {
    
    /**
     *  This method returns a specialized object which implements the 
     * specialized APIs of the specified feature and version, as specified 
     * in . The specialized object may also be obtained by using 
     * binding-specific casting methods but is not necessarily expected to, 
     * as discussed in . This method also allow the implementation to 
     * provide specialized objects which do not support the 
     * <code>DOMImplementation</code> interface.
     *  
     * @param feature  The name of the feature requested. Note that any plus 
     *   sign "+" prepended to the name of the feature will be ignored since 
     *   it is not significant in the context of this method. 
     * @param version  This is the version number of the feature to test. 
     * @return  Returns an object which implements the specialized APIs of 
     *   the specified feature and version, if any, or <code>null</code> if 
     *   there is no object which implements interfaces associated with that 
     *   feature. If the <code>DOMObject</code> returned by this method 
     *   implements the <code>DOMImplementation</code> interface, it must 
     *   delegate to the primary core <code>DOMImplementation</code> and not 
     *   return results inconsistent with the primary core 
     *   <code>DOMImplementation</code> such as <code>hasFeature</code>, 
     *   <code>getFeature</code>, etc. 
     * @since DOM Level 3
     * @see org.w3c.dom.DOMImplementation#getFeature(
     * 		java.lang.String, java.lang.String)
     */
    public Object getFeature(final String feature, final String version) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Test if the DOM implementation implements a specific feature.
     * 
     * @param feature The name of the feature to test (case-insensitive). The 
     *   values used by DOM features are defined throughout the DOM Level 3 
     *   specifications and listed in the  section. The name must be an XML 
     *   name. To avoid possible conflicts, as a convention, names referring 
     *   to features defined outside the DOM specification should be made 
     *   unique.
     * @param version This is the version number of the feature to test. In 
     *   Level 3, the string can be either "3.0", "2.0" or "1.0". If the 
     *   version is <code>null</code> or empty string, supporting any 
     *   version of the feature causes the method to return <code>true</code>.
     * @return <code>true</code> if the feature is implemented in the 
     *   specified version, <code>false</code> otherwise.
     * @see org.w3c.dom.DOMImplementation#hasFeature(
     * 		java.lang.String, java.lang.String)
     */
    public boolean hasFeature(final String feature, final String version) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Creates an empty <code>DocumentType</code> node. Entity declarations 
     * and notations are not made available. Entity reference expansions and 
     * default attribute additions do not occur. It is expected that a 
     * future version of the DOM will provide a way for populating a 
     * <code>DocumentType</code>.
     * 
     * @param qualifiedName The qualified name of the document type to be 
     *   created.
     * @param publicId The external subset identifier.
     * @param systemId The external subset system identifier.
     * @return A new <code>DocumentType</code> node with 
     *   <code>Node.ownerDocument</code> set to <code>null</code>.
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified qualified name 
     *   contains an illegal character.
     *   <br>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is 
     *   malformed.
     *   <br>NOT_SUPPORTED_ERR: May be raised by DOM implementations which do 
     *   not support the <code>"XML"</code> feature, if they choose not to 
     *   support this method. Other features introduced in the future, by 
     *   the DOM WG or in extensions defined by other groups, may also 
     *   demand support for this method; please consult the definition of 
     *   the feature to see if it requires this method. 
     * @see org.w3c.dom.DOMImplementation#createDocumentType(
     * 		java.lang.String, java.lang.String, java.lang.String)
     */
    public DocumentType createDocumentType(final String qualifiedName,
                                           final String publicId, 
                                           final String systemId) 
                                                   throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates a DOM Document object of the specified type with its document 
     * element.
     * @param namespaceURI The namespace URI of the document element to 
     *   create or <code>null</code>.
     * @param qualifiedName The qualified name of the document element to be 
     *   created or <code>null</code>.
     * @param doctype The type of document to be created or <code>null</code>.
     *   When <code>doctype</code> is not <code>null</code>, its 
     *   <code>Node.ownerDocument</code> attribute is set to the document 
     *   being created.
     * @return A new <code>Document</code> object with its document element. 
     *   If the <code>NamespaceURI</code>, <code>qualifiedName</code>, and 
     *   <code>doctype</code> are <code>null</code>, the returned 
     *   <code>Document</code> is empty with no document element.
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified qualified name 
     *   contains an illegal character.
     *   <br>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is 
     *   malformed, if the <code>qualifiedName</code> has a prefix and the 
     *   <code>namespaceURI</code> is <code>null</code>, or if the 
     *   <code>qualifiedName</code> is <code>null</code> and the 
     *   <code>namespaceURI</code> is different from <code>null</code>, or 
     *   if the <code>qualifiedName</code> has a prefix that is "xml" and 
     *   the <code>namespaceURI</code> is different from "
     *   http://www.w3.org/XML/1998/namespace" , or if the DOM 
     *   implementation does not support the <code>"XML"</code> feature but 
     *   a non-null namespace URI was provided, since namespaces were 
     *   defined by XML.
     *   <br>WRONG_DOCUMENT_ERR: Raised if <code>doctype</code> has already 
     *   been used with a different document or was created from a different 
     *   implementation.
     *   <br>NOT_SUPPORTED_ERR: May be raised by DOM implementations which do 
     *   not support the "XML" feature, if they choose not to support this 
     *   method. Other features introduced in the future, by the DOM WG or 
     *   in extensions defined by other groups, may also demand support for 
     *   this method; please consult the definition of the feature to see if 
     *   it requires this method. 
     * @since DOM Level 2
     * @see org.w3c.dom.DOMImplementation#createDocument(
     *      java.lang.String, java.lang.String, org.w3c.dom.DocumentType)
     */
    public Document createDocument(final String namespaceURI, 
                                   final String qualifiedName,
                                   final DocumentType doctype) 
                                           throws DOMException {
        
        DocumentImpl document = new DocumentImpl(this);
        
        if (doctype != null) {
            if (doctype.getOwnerDocument() != null) {
                throw new DOMException(
                        DOMException.WRONG_DOCUMENT_ERR, 
                        "The doctype is allready in use.");
            } else if (!(doctype instanceof DocumentTypeImpl)) {
                throw new DOMException(
                        DOMException.WRONG_DOCUMENT_ERR, 
                        "The doctype is from a different implementaion.");
            }
            document.appendChild((DocumentTypeImpl) doctype);
        }
        
        if (qualifiedName != null) {
            
            Element elem = null;
            if (namespaceURI != null) {
                elem = document.createElementNS(
                        namespaceURI, qualifiedName);
            } else {
                elem = document.createElement(qualifiedName);
            }
            document.appendChild(elem);
        }
        
        //doctype.getOwnerDocument() 
        // TODO Auto-generated method stub
        return document;
    }

    /**
     * This method makes available a <code>DOMImplementation</code>'s 
     * specialized interface (see ).
     * @param feature The name of the feature requested (case-insensitive).
     * @return Returns an alternate <code>DOMImplementation</code> which 
     *   implements the specialized APIs of the specified feature, if any, 
     *   or <code>null</code> if there is no alternate 
     *   <code>DOMImplementation</code> object which implements interfaces 
     *   associated with that feature. Any alternate 
     *   <code>DOMImplementation</code> returned by this method must 
     *   delegate to the primary core <code>DOMImplementation</code> and not 
     *   return results inconsistent with the primary 
     *   <code>DOMImplementation</code>
     * @see org.w3c.dom.DOMImplementation#getInterface(java.lang.String)
     */
    public DOMImplementation getInterface(final String feature) {
        // Allways null, I will only support DOM Level 2
        return null;
    }

}
