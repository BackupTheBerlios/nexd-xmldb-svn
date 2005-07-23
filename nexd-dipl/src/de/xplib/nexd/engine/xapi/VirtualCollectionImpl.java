/*
 * Project: nexd 
 * Copyright (C) 2005  Manuel Pichler <manuel.pichler@xplib.de>
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
 * $Log: VirtualCollectionImpl.java,v $
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
 * Revision 1.2  2005/03/26 12:14:20  nexd
 * UML documentation.
 *
 * Revision 1.1  2005/03/01 10:23:04  nexd
 * Initial import of the virtual collection api.
 *
 */
package de.xplib.nexd.engine.xapi;

import java.io.IOException;
import java.net.URL;

import org.sixdml.exceptions.DocumentAlreadyExistsException;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.NonExistentDocumentException;
import org.sixdml.exceptions.NonWellFormedXMLException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.xapi.services.QueryServiceImpl;
import de.xplib.nexd.engine.xapi.services.TransactionServiceImpl;
import de.xplib.nexd.engine.xapi.services.XPathQueryServiceImpl;
import de.xplib.nexd.store.StorageCollectionI;
/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class VirtualCollectionImpl 
    extends AbstractCollection 
    implements VirtualCollection {
    
    /**
     * Constant Exception message.
     */
    private static final String MESSAGE = 
        "VirtualCollection doesn't allow user input.";
    
    /**
     * Constructor.
     * 
     * @param engineIn The used <code>NEXDEngineI</code> instance.
     * @param dbCollIn The underlying <code>StorageCollectionI</code>.
     */
    public VirtualCollectionImpl(final NEXDEngineI engineIn,
                                 final StorageCollectionI dbCollIn) {
        
        super(engineIn, dbCollIn);
    }
    
    /**
     * <p>This method returns the {@link VCLSchema} that describes how the 
     * {@link _de.xplib.nexd.api.VirtualResource} objects in this 
     * <code>VirtualCollection</code> are build.</p>
     * 
     * @return <p>The {@link VCLSchema} that describes how the resources are
     *         build.</p> 
     * @exception XMLDBException
     *            <p>This <code>Exception</code> is thrown, if any database 
     *            specific error occurs. The returned error code is
     *            {@link org.xmldb.api.base.ErrorCodes#VENDOR_ERROR}.</p>
     * @see _de.xplib.nexd.api.VirtualCollection#getVCLSchema()
     */
    public VCLSchema getVCLSchema() throws XMLDBException {
        return this.engine.queryVCLSchema(this);
    }
    
    /**
     * <p>Sets the xsl-stylesheet that will be used to transform the documents
     * in this <code>VirtualCollection</code>. If a xsl-stylesshet already 
     * exists for this <code>VirtualCollection</code> then it is replaced by the
     * new one if it is a valid xsl-sytlesheet.</p>
     * 
     * @param xslIn <p>The <code>URL</code> where the xsl-stylesheet is located
     *              </p>
     * @throws IOException <p>If the given <code>URL</code> cannot be opend.</p>
     * @throws SAXException <p>If it is not a valid xsl-stylesheet.</p>
     * @exception XMLDBException <p>with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br /></p>
     * @see _de.xplib.nexd.api.VirtualCollection#setStylesheet(java.net.URL)
     */
    public void setStylesheet(final URL xslIn) throws IOException,
                                                      SAXException,
                                                      XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        this.engine.storeCollectionStylesheet(this, xslIn);
    }
    
    /**
     * <p>Unsets the xsl-stylesheet for this <code>VirtualCollection</code>. 
     * Does nothing if no xsl-stylesheet exists for the <code>VirtualCollection
     * </code>.</p> 
     * 
     * @exception XMLDBException <p>with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br /></p>
     * @see _de.xplib.nexd.api.VirtualCollection#unsetStylesheet()
     */
    public void unsetStylesheet() throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        this.engine.dropCollectionStylesheet(this);
    }
    
    /**
     * <p>Returns the contents of the xsl-stylesheet for the 
     * <code>VirtualCollection</code> as a DOM {@link Node}.</p>
     *  
     * @return <p>The xsl-stylesheet for the <code>VirtualCollection</code> or 
     *         <code>null</code> if none exists.</p> 
     * @exception XMLDBException <p>with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br /></p>
     * @see _de.xplib.nexd.api.VirtualCollection#showStylesheet()
     */
    public Node showStylesheet() throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        
        return this.engine.queryCollectionStylesheet(this);
    }
    
    /**
     * Creates a new unique ID within the context of the <code>Collection</code>
     *
     * @return The created id as a string.
     * @exception XMLDBException with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#createId()
     */
    public String createId() throws XMLDBException {
        throw new XMLDBException(ErrorCodes.INVALID_COLLECTION, MESSAGE);
    }
    
    /**
     * Creates a new empty <code>Resource</code> with the provided id. 
     * The type of <code>Resource</code>
     * returned is determined by the <code>type</code> parameter. The XML:DB API
     * currently defines "XMLResource" and "BinaryResource" as valid resource 
     * types. The <code>id</code> provided must be unique within the scope of 
     * the collection. If <code>id</code> is null or its value is empty then an 
     * id is generated by calling <code>createId()</code>. The
     * <code>Resource</code> created is not stored to the database until 
     * <code>storeResource()</code> is called.
     *
     * @param idIn The unique id to associate with the created <
     *           code>Resource</code>.
     * @param typeIn The <code>Resource</code> type to create.
     * @return An empty <code>Resource</code> instance.    
     * @exception XMLDBException with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.UNKNOWN_RESOURCE_TYPE</code> if the 
     *            <code>type</code> parameter is not a known 
     *            <code>Resource</code> type. 
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#createResource(
     *      java.lang.String, java.lang.String)
     */
    public Resource createResource(final String idIn, final String typeIn)
            throws XMLDBException {
        
        throw new XMLDBException(ErrorCodes.INVALID_COLLECTION, MESSAGE);
    }
    
    /**
     * Returns a <code>Service</code> instance for the requested service name 
     * and version. If no <code>Service</code> exists for those parameters a 
     * <code>null value is returned.</code>
     *
     * @param nameIn Description of Parameter
     * @param versionIn Description of Parameter
     * @return The Service instance or null if no Service could be found.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#getService(
     *      java.lang.String, java.lang.String)
     */
    public Service getService(final String nameIn, final String versionIn)
            throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        
        Service service = null;
        if (nameIn.equals("XPathQueryService") 
                && Float.parseFloat(versionIn) >= 1.0f) {
            
            service = new XPathQueryServiceImpl(this.engine, this);
        } else if ((nameIn.equals("TransactionService")
                || nameIn.equals("SixdmlTransactionService")) 
                && Float.parseFloat(versionIn) >= 1.0f) {
            
            service = new TransactionServiceImpl(this.engine, this);
        } else if (nameIn.equals("SixdmlQueryService") 
                && Float.parseFloat(versionIn) >= 1.0f) {
            
            service = new QueryServiceImpl(this.engine, this);
        }
        return service;
    }

    /**
     * Provides a list of all services known to the collection. If no services
     * are known an empty list is returned.
     *
     * @return An array of registered <code>Service</code> implementations.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#getServices()
     */
    public Service[] getServices() throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        
        return new Service[] {
                new XPathQueryServiceImpl(this.engine, this),
                new TransactionServiceImpl(this.engine, this),
                new QueryServiceImpl(this.engine, this)};
    }
    
    /**
     * Removes the <code>Resource</code> from the database.
     *
     * @param resIn The resource to remove.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     *            <code>ErrorCodes.INVALID_RESOURCE</code> if the 
     *            <code>Resource</code> is not valid.<br />
     *            <code>ErrorCodes.NO_SUCH_RESOURCE</code> if the 
     *            <code>Resource</code> is not known to this 
     *            <code>Collection</code>.
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#removeResource(
     *      org.xmldb.api.base.Resource)
     */
    public void removeResource(final Resource resIn) throws XMLDBException {
        throw new XMLDBException(ErrorCodes.INVALID_COLLECTION, MESSAGE);
    }
    
    /**
     * Stores the provided resource into the database. If the resource does not
     * already exist it will be created. If it does already exist it will be
     * updated.
     *
     * @param resIn The resource to store in the database.
     * @exception XMLDBException With expected error codes.<br />
     *                           <code>ErrorCodes.VENDOR_ERROR</code> for any 
     *                           vendor specific errors that occur.<br />
     *                           <code>ErrorCodes.INVALID_RESOURCE</code> if the
     *                           <code>Resource</code> is not valid.
     *                           <code>ErrorCodes.COLLECTION_CLOSED</code> if 
     *                           the <code>close</code> method has been called 
     *                           on the <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#storeResource(
     *      org.xmldb.api.base.Resource)
     */
    public void storeResource(final Resource resIn) throws XMLDBException {
        throw new XMLDBException(ErrorCodes.INVALID_COLLECTION, MESSAGE);
    }
    
    /**
     * Adds an XML document created from the string argument to the collection. 
     * 
     * @param nameIn The name of the document.
     * @param xmlStringIn The string representation of a well-formed XML 
     *                    document. 
     * @exception DocumentAlreadyExistsException If there already exists a 
     *            document with the specified name in the database. 
     * @exception InvalidCollectionDocumentException If the document fails to 
     *            validate against the collection's schema. 
     * @exception NonWellFormedXMLException If the XML is not well formed. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#insertDocument(
     *      java.lang.String, java.lang.String)
     * @see org.sixdml.dbmanagement.SixdmlCollection#insertDocument(
     *      java.lang.String, java.lang.String)
     */
    public void insertDocument(final String nameIn, final String xmlStringIn)
            throws DocumentAlreadyExistsException,
                   InvalidCollectionDocumentException, 
                   NonWellFormedXMLException,
                   XMLDBException {

        throw new XMLDBException(ErrorCodes.INVALID_COLLECTION, MESSAGE);
    }
    
    /**
     * Adds an XML document loaded from the specified URL to the collection. 
     * 
     * @param nameIn The name of the document.
     * @param documentSourceIn The url of a well-formed XML document.
     * @exception DocumentAlreadyExistsException If there already exists a 
     *            document with the specified name in the database. 
     * @exception IOException If an error occurs while trying to retrieve the 
     *            file. 
     * @exception InvalidCollectionDocumentException If the document fails to 
     *            validate against the collection's schema. 
     * @exception NonWellFormedXMLException If the XML is not well formed. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#insertDocument(
     *      java.lang.String, java.net.URL)
     */
    public void insertDocument(final String nameIn, final URL documentSourceIn)
            throws DocumentAlreadyExistsException, 
                   IOException,
                   InvalidCollectionDocumentException, 
                   NonWellFormedXMLException,
                   XMLDBException {
        
        throw new XMLDBException(ErrorCodes.INVALID_COLLECTION, MESSAGE);
    }
    
    /**
     * Removes an XML document from the collection. 
     * 
     * @param nameIn The name of the document. 
     * @exception NonExistentDocumentException If the document does not exist 
     *            in the collection. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#removeDocument(
     *      java.lang.String)
     */
    public void removeDocument(final String nameIn)
            throws NonExistentDocumentException, XMLDBException {
        
        throw new XMLDBException(ErrorCodes.INVALID_COLLECTION, MESSAGE);
    }
}
