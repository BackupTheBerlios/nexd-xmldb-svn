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
package de.xplib.nexd.xapi;

import org.sixdml.dbmanagement.SixdmlResource;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

/**
 * Provides access to XML resources stored in the database. An XMLResource can
 * be accessed either as text XML or via the DOM or SAX APIs.<p />
 *
 * The default behavior for getContent and setContent is to work with XML data
 * as text so these methods work on <code>String</code> content.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class XMLResourceImpl 
    extends AbstractResource
    implements SixdmlResource {

    /**
     * 
     */
    public XMLResourceImpl() {
        super("", RESOURCE_TYPE);
        // TODO Auto-generated constructor stub
    }

    /**
     * Gets the name of the resource.
     *
     * @return The name of the resource
     * @see org.sixdml.dbmanagement.SixdmlResource#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
    * Returns the unique id for the parent document to this 
    * <code>Resource</code> or <code>null</code> if the <code>Resource</code> 
    * does not have a parent document. <code>getDocumentId()</code> is typically
    * used with <code>Resource</code> instances retrieved using a query. It 
    * enables accessing the parent document of the <code>Resource</code> even 
    * if the <code>Resource</code> is a child node of the document. If the 
    * <code>Resource</code> was not obtained through a query then 
    * <code>getId()</code> and <code>getDocumentId()</code> will return the 
    * same id.
    *
    * @return The id for the parent document of this <code>Resource</code> or 
    *         <code>null</code> if there is no parent document for this 
    *         <code>Resource</code>.
    * @exception XMLDBException With expected error codes.<br />
    *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *            specific errors that occur.<br /> 
     * @see org.xmldb.api.modules.XMLResource#getDocumentId()
     */
    public String getDocumentId() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
    * Returns the content of the <code>Resource</code> as a DOM Node.
    *
    * @return The XML content as a DOM <code>Node</code>
    * @exception XMLDBException With expected error codes.<br />
    *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *            specific errors that occur.<br />
     * @see org.xmldb.api.modules.XMLResource#getContentAsDOM()
     */
    public Node getContentAsDOM() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Sets the content of the <code>Resource</code> using a DOM Node as the
     * source.
     *
     * @param content The new content value
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.INVALID_RESOURCE</code> if the content value 
     *            provided is <code>null</code>.<br />
     *            <code>ErrorCodes.WRONG_CONTENT_TYPE</code> if the content 
     *            provided in not a valid DOM <code>Node</code>.
     * @see org.xmldb.api.modules.XMLResource#setContentAsDOM(org.w3c.dom.Node)
     */
    public void setContentAsDOM(final Node content) throws XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Allows you to use a <code>ContentHandler</code> to parse the XML data 
     * from the database for use in an application.
     *
     * @param handler The SAX <code>ContentHandler</code> to use to handle the
     *        <code>Resource</code> content.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.INVALID_RESOURCE</code> if the 
     *            <code>ContentHandler</code> provided is <code>null</code>.
     * @see org.xmldb.api.modules.XMLResource#getContentAsSAX(
     *      org.xml.sax.ContentHandler)
     */
    public void getContentAsSAX(final ContentHandler handler) 
            throws XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Sets the content of the <code>Resource</code> using a SAX 
     * <code>ContentHandler</code>.
     *
     * @return A SAX <code>ContentHandler</code> that can be used to add content
     *         into the <code>Resource</code>.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     * @see org.xmldb.api.modules.XMLResource#setContentAsSAX()
     */
    public ContentHandler setContentAsSAX() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the <code>Collection</code> instance that this resource is 
     * associated with. All resources must exist within the context of a 
     * <code>collection</code>.
     *
     * @return The collection associated with the resource.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#getParentCollection()
     */
    public Collection getParentCollection() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Retrieves the content from the resource. The type of the content varies
     * depending what type of resource is being used.
     *
     * @return The content of the resource.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#getContent()
     */
    public Object getContent() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Sets the content for this resource. The type of content that can be set
     * depends on the type of resource being used.
     *
     * @param value The content value to set for the resource.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#setContent(java.lang.Object)
     */
    public void setContent(final Object value) throws XMLDBException {
        // TODO Auto-generated method stub

    }

}
