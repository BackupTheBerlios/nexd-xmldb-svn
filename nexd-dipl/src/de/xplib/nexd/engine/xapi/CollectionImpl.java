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
 * $Log: CollectionImpl.java,v $
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.10  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.9  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.8  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.7  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlIndex;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.exceptions.DocumentAlreadyExistsException;
import org.sixdml.exceptions.IndexAlreadyExistsException;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.sixdml.exceptions.NonExistentDocumentException;
import org.sixdml.exceptions.NonExistentIndexException;
import org.sixdml.exceptions.NonWellFormedXMLException;
import org.sixdml.exceptions.UnsupportedIndexTypeException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.xapi.services.CollectionManagementServiceImpl;
import de.xplib.nexd.engine.xapi.services.QueryServiceImpl;
import de.xplib.nexd.engine.xapi.services.TransactionServiceImpl;
import de.xplib.nexd.engine.xapi.services.UpdateServiceImpl;
import de.xplib.nexd.engine.xapi.services.VirtualCollectionManagementServiceImpl;
import de.xplib.nexd.engine.xapi.services.XPathQueryServiceImpl;
import de.xplib.nexd.engine.xapi.services.XUpdateQueryServiceImpl;
import de.xplib.nexd.store.StorageCollectionI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class CollectionImpl 
    extends AbstractCollection
    implements SixdmlCollection {
    
    /**
     * @clientCardinality 0..1
     * @clientRole parentCollection
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole childCollections
     */

    private AbstractCollection lnkAbstractCollection;

    /**
     * The used system logger.
     */
    protected static final Log LOG = LogFactory.getLog(
            CollectionImpl.class);
    
    /**
     * @param engineIn The used <code>NEXDEngineI</code> instance.
     * @param dbCollIn The underlying <code>StorageCollectionI</code>.
     */
    public CollectionImpl(final NEXDEngineI engineIn,
                          final StorageCollectionI dbCollIn) {
        
        super(engineIn, dbCollIn);
    }

    /**
     * Sets the XML schema or DTD that will be used to constrain the documents
     * in this collection. For this operation to succeed, all documents in the 
     * collection <b>must</b> pass schema validation. If a schema already exists
     * for the collection then it is replaced by the new one if all documents 
     * pass validation.
     *  
     * @param schemaFile The location of the schema file either on the local 
     *                   file system or over the internet. 
     * @exception InvalidCollectionDocumentException If an XML document in the 
     *                                               collection fails to be 
     *                                               validated by the schema.
     * @exception IOException If an error occurs while trying to retrieve the 
     *                        file.
     * @exception InvalidSchemaException If the schema is invalid. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#setSchema(java.net.URL)
     */
    public final void setSchema(final URL schemaFile)
            throws InvalidCollectionDocumentException, IOException,
            InvalidSchemaException, XMLDBException {

        this.engine.storeCollectionSchema(this, schemaFile);
    }

    /**
     * Returns the contents of the schema for the collection as a string.
     *  
     * @return The schema for the collection or NULL if none exists. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#showSchema()
     */
    public final String showSchema() throws XMLDBException {
        return this.engine.queryCollectionSchema(this);
    }

    /**
     * Unsets the schema for this collection. Does nothing if no schema exists 
     * for the collection. 
     * 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#unsetSchema()
     */
    public void unsetSchema() throws XMLDBException {
        this.engine.dropCollectionSchema(this);
    }

    /**
     * Adds an XML document created from the string argument to the collection. 
     * 
     * @param nameIn The name of the document.
     * @param xmlString The string representation of a well-formed XML document 
     * @exception DocumentAlreadyExistsException If there already exists a 
     *            document with the specified name in the database. 
     * @exception InvalidCollectionDocumentException If the document fails to 
     *            validate against the collection's schema. 
     * @exception NonWellFormedXMLException If the XML is not well formed. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#insertDocument(
     *      java.lang.String, java.lang.String)
     */
    public void insertDocument(final String nameIn, final String xmlString)
            throws DocumentAlreadyExistsException,
                   InvalidCollectionDocumentException, 
                   NonWellFormedXMLException,
                   XMLDBException {
        
        if (this.engine.queryResource(this, nameIn) != null) {
            throw new DocumentAlreadyExistsException(
                    "An document for the name[" + nameIn + "] already exists.");
        }
        if (xmlString == null || xmlString.indexOf('<') != 0) {
            throw new NonWellFormedXMLException(
                    "The given Document[" + nameIn + "] is not well formed.");
        }
        
        XMLResource res = (XMLResource) this.createResource(
                nameIn, XMLResource.RESOURCE_TYPE);
                
        try {
            res.setContent(xmlString);
        } catch (XMLDBException e) {
            if (e.errorCode == ErrorCodes.WRONG_CONTENT_TYPE) {
                throw new NonWellFormedXMLException(e.getMessage());
            }
            throw e;
        }
        
        this.storeResource(res);
    }    


    /**
     * Adds an XML document loaded from the specified URL to the collection. 
     * 
     * @param nameIn The name of the document.
     * @param documentSource The url of a well-formed XML document.
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
    public void insertDocument(final String nameIn, final URL documentSource)
            throws DocumentAlreadyExistsException, 
                   IOException,
                   InvalidCollectionDocumentException, 
                   NonWellFormedXMLException,
                   XMLDBException {
        
        InputStream stream = documentSource.openStream();
        byte[] bytes   = new byte[stream.available()];
        
        stream.read(bytes, 0, bytes.length);
        stream.close();
        
        this.insertDocument(nameIn, new String(bytes));
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
        
        Resource res = this.engine.queryResource(this, nameIn);
        if (res == null) {
            throw new NonExistentDocumentException(
                    "There is no document for the name [" + nameIn + "].");
        } /*
        if (res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
            throw new XMLDBException(
                    ErrorCodes.INVALID_RESOURCE, 
                    "The resource for name [" + nameIn + "] has no document.");
        } */
        this.engine.dropResource(this, res);
    }

    /**
     * Adds an index to apply to the collection. 
     * 
     * @param index the index to add
     * @exception UnsupportedIndexTypeException If the index is of a type 
     *                                          unsupported by the database.
     * @exception IndexAlreadyExistsException If an index with the same name 
     *                                        already exists for the collection.
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#addIndex(
     * 		org.sixdml.dbmanagement.SixdmlIndex)
     */
    public void addIndex(final SixdmlIndex index)
            throws UnsupportedIndexTypeException, 
                   IndexAlreadyExistsException,
                   XMLDBException {
        
        throw new UnsupportedIndexTypeException(
                "NEXD doesn't support any index, NOW.");
    }

    /**
     * Removes a particular index from the collection.
     * 
     * @param name The index to add
     * @exception NonExistentIndexException If no index for the given name exist
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#removeIndex(
     * 		java.lang.String)
     */
    public void removeIndex(final String name) 
    		throws NonExistentIndexException, XMLDBException {
        
        throw new NonExistentIndexException(
                "NEXD doesn't support any index, NOW.");

    }

    /**
     * Returns an array of SixdmlIndex objects that apply to this collection. If
     * no indexes exist in the collection then an empty array is returned.
     * 
     * @return An array of SixdmlIndex objects.
     * @exception XMLDBException If a database error occurs.
     * @see org.sixdml.dbmanagement.SixdmlCollection#getIndices()
     */
    public final SixdmlIndex[] getIndices() throws XMLDBException {
        // TODO Implement the index feature.
        return new SixdmlIndex[0];
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
    public final Service[] getServices() throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        
        return new Service[] {
                new CollectionManagementServiceImpl(this.engine, this),
                new XPathQueryServiceImpl(this.engine, this),
                new TransactionServiceImpl(this.engine, this),
                new UpdateServiceImpl(this.engine, this),
                new QueryServiceImpl(this.engine, this),
                new VirtualCollectionManagementServiceImpl(this.engine, this),
                new XUpdateQueryServiceImpl(this.engine, this)};
    }

    /**
     * Returns a <code>Service</code> instance for the requested service name 
     * and version. If no <code>Service</code> exists for those parameters a 
     * <code>null value is returned.</code>
     *
     * @param nameIn Description of Parameter
     * @param version Description of Parameter
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
    public Service getService(final String nameIn, final String version)
            throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        
        Service service = null;
        if ((nameIn.equals("CollectionManagementService") 
                && Float.parseFloat(version) >= 0) 
                || (nameIn.equals("SixdmlCollectionManagementService") 
                && version.equals("1.0"))) {
            
            service = new CollectionManagementServiceImpl(this.engine, this);
        } else if (nameIn.equals("XPathQueryService") 
                && Float.parseFloat(version) >= 1.0f) {
            
            service = new XPathQueryServiceImpl(this.engine, this);
        } else if ((nameIn.equals("TransactionService")
                || nameIn.equals("SixdmlTransactionService")) 
                && version.equals("1.0")) {
            
            service = new TransactionServiceImpl(this.engine, this);
        } else if (nameIn.equals("SixdmlUpdateService")
                && Float.parseFloat(version) >= 1.0f) {
            
            service = new UpdateServiceImpl(this.engine, this);
        } else if (nameIn.equals("SixdmlQueryService")
                && Float.parseFloat(version) >= 1.0f) {
            
            service = new QueryServiceImpl(this.engine, this);
        } else if (nameIn.equals("VirtualCollectionManagementService")
                && Float.parseFloat(version) >= 1.0f) {
            
            service = new VirtualCollectionManagementServiceImpl(
                    this.engine, this);
        } else if (nameIn.equals("XUpdateQueryService") 
                && Float.parseFloat(version) >= 1.0f) {
            
            service = new XUpdateQueryServiceImpl(this.engine, this);
        }
        return service;
    }

    /**
     * Returns the number of child collections under this 
     * <code>Collection</code> or 0 if no child collections exist.
     *
     * @return The number of child collections.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#getChildCollectionCount()
     */
    public int getChildCollectionCount() throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        
        return this.engine.queryCollectionCount(this);
    }

    /**
     * Returns a list of collection names naming all child collections
     * of the current collection. If no child collections exist an empty list is
     * returned.
     *
     * @return An array containing collection names for all child
     *         collections.
     * @exception XMLDBException with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#listChildCollections()
     */
    public String[] listChildCollections() throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        return this.engine.queryCollections(this);
    }

    /**
     * Returns a <code>Collection</code> instance for the requested child 
     * collection if it exists.
     *
     * @param nameIn The name of the child collection to retrieve.
     * @return The requested child collection or null if it couldn't be found.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#getChildCollection(java.lang.String)
     */
    public Collection getChildCollection(final String nameIn) 
    		throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(
                    ErrorCodes.COLLECTION_CLOSED);
        }
        return this.engine.queryChildCollection(this, nameIn);
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
     * @param type The <code>Resource</code> type to create.
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
    public Resource createResource(final String idIn, final String type)
            throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        
        Resource res = null;
        if (type.equals(XMLResource.RESOURCE_TYPE)) {
            res = new XMLResourceImpl(this, idIn);
        } else if (type.equals(BinaryResource.RESOURCE_TYPE)) {
            res = new BinaryResourceImpl(this, idIn);
        } else {
            throw new XMLDBException(ErrorCodes.UNKNOWN_RESOURCE_TYPE);
        }
        return res;
    }

    /**
     * Removes the <code>Resource</code> from the database.
     *
     * @param res The resource to remove.
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
    public void removeResource(final Resource res) throws XMLDBException {
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        this.engine.dropResource(this, res);
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

        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        
        if (resIn.getResourceType().equals(XMLResource.RESOURCE_TYPE)) {
            SixdmlResource sixdmlRes;
            if (resIn instanceof SixdmlResource) {
                sixdmlRes = (SixdmlResource) resIn;
            } else {
                throw new XMLDBException(ErrorCodes.INVALID_RESOURCE);
            }
            
            this.engine.storeResource(this, sixdmlRes);
        } else if (resIn.getResourceType().equals(
                BinaryResource.RESOURCE_TYPE)) {
            //BinaryResource binRes = (BinaryResource) resIn;
            LOG.error("BinaryResource is not implemented jet.");
            throw new XMLDBException(
                    ErrorCodes.NOT_IMPLEMENTED,
                    "In this version NEXD doesn't support binary resources.");
        } else {
            throw new XMLDBException(ErrorCodes.INVALID_RESOURCE);
        }
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
        
        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        
        Random random = new Random();
        String uniqueId;
        boolean isUnique;
        
        do {
            isUnique = true;
            uniqueId = Integer.toHexString(random.nextInt()) + ".xml";
                
            isUnique = !this.engine.containsId(this, uniqueId);
        } while (!isUnique);
        return uniqueId;
    }

}
