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

import java.io.IOException;
import java.net.URL;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlIndex;
import org.sixdml.exceptions.DocumentAlreadyExistsException;
import org.sixdml.exceptions.IndexAlreadyExistsException;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.sixdml.exceptions.NonExistentDocumentException;
import org.sixdml.exceptions.NonExistentIndexException;
import org.sixdml.exceptions.NonWellFormedXMLException;
import org.sixdml.exceptions.UnsupportedIndexTypeException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

/**
 * This is an abstract base class for all collection types that can be stored in
 * the <b>NEXD XML-Database</b>.
 *  
 * A <code>Collection</code> represents a collection of <code>Resource</code>s 
 * stored within an XML database. An XML database MAY expose collections as a 
 * hierarchical set of parent and child collections.<p />
 *
 * A <code>Collection</code> provides access to the <code>Resource</code>s
 * stored by the <code>Collection</code> and to <code>Service</code> instances
 * that can operate against the <code>Collection</code> and the 
 * <code>Resource</code>s stored within it. The <code>Service</code> mechanism
 * provides the ability to extend the functionality of a <code>Collection</code>
 * in ways that allows optional functionality to be enabled for the 
 * <code>Collection</code>. 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public abstract class AbstractCollection 
    extends AbstractConfigurable
    implements SixdmlCollection {

    /**
     * 
     */
    public AbstractCollection() {
        super();
        // TODO Auto-generated constructor stub
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
     *            collection fails to be validated by the schema.
     * @exception IOException If an error occurs while trying to retrieve the 
     *            file.
     * @exception InvalidSchemaException If the schema is invalid. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#setSchema(java.net.URL)
     */
    public void setSchema(final URL schemaFile)
            throws InvalidCollectionDocumentException, 
                   IOException,
                   InvalidSchemaException, 
                   XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Returns the contents of the schema for the collection as a string.
     *  
     * @return The schema for the collection or NULL if none exists. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#showSchema()
     */
    public String showSchema() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Unsets the schema for this collection. Does nothing if no schema exists 
     * for the collection. 
     * 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#unsetSchema()
     */
    public void unsetSchema() throws XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Adds an XML document created from the string argument to the collection. 
     * 
     * @param name The name of the document.
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
    public void insertDocument(final String name, final String xmlString)
            throws DocumentAlreadyExistsException,
                   InvalidCollectionDocumentException, 
                   NonWellFormedXMLException,
                   XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Adds an XML document loaded from the specified URL to the collection. 
     * 
     * @param name The name of the document.
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
    public void insertDocument(final String name, final URL documentSource)
            throws DocumentAlreadyExistsException, 
                   IOException,
                   InvalidCollectionDocumentException, 
                   NonWellFormedXMLException,
                   XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Removes an XML document from the collection. 
     * 
     * @param name The name of the document. 
     * @exception NonExistentDocumentException If the document does not exist 
     *            in the collection. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#removeDocument(
     *      java.lang.String)
     */
    public void removeDocument(final String name)
            throws NonExistentDocumentException, XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Adds an index to apply to the collection. 
     * 
     * @param index The index to add
     * @exception UnsupportedIndexTypeException If the index is of a type 
     *            unsupported by the database.
     * @exception IndexAlreadyExistsException If an index with the same name 
     *            already exists for the collection. 
     * @exception XMLDBException if a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#addIndex(
     *      org.sixdml.dbmanagement.SixdmlIndex)
     */
    public void addIndex(final SixdmlIndex index)
            throws UnsupportedIndexTypeException, 
                   IndexAlreadyExistsException,
                   XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Removes a particular index from the collection.
     * 
     * @param name The index to remove
     * @exception NonExistentIndexException If there is no index for the name.
     * @exception XMLDBException If a database error occurs.
     * @see org.sixdml.dbmanagement.SixdmlCollection#removeIndex(
     *      java.lang.String)
     */
    public void removeIndex(final String name) 
    		throws NonExistentIndexException, XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Returns an array of SixdmlIndex objects that apply to this collection. 
     * If no indexes exist in the collection then an empty array is returned.
     * 
     * @return An array of SixdmlIndex objects.
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.dbmanagement.SixdmlCollection#getIndices()
     */
    public SixdmlIndex[] getIndices() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the name associated with the Collection instance.
     *
     * @return The name of the object.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     * @see org.xmldb.api.base.Collection#getName()
     */
    public String getName() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns a <code>Service</code> instance for the requested service name 
     * and version. If no <code>Service</code> exists for those parameters a 
     * <code>null value is returned.</code>
     *
     * @param name Description of Parameter
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
    public Service getService(final String name, final String version)
            throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the parent collection for this collection or null if no parent
     * collection exists.
     *
     * @return The parent <code>Collection</code> instance.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#getParentCollection()
     */
    public Collection getParentCollection() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Returns a list of collection names naming all child collections of the 
     * current collection. If no child collections exist an empty list is
     * returned.
     *
     * @return An array containing collection names for all child collections.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#listChildCollections()
     */
    public String[] listChildCollections() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns a <code>Collection</code> instance for the requested child 
     * collection if it exists.
     *
     * @param name The name of the child collection to retrieve.
     * @return The requested child collection or null if it couldn't be found.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#getChildCollection(java.lang.String)
     */
    public Collection getChildCollection(final String name) 
            throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the number of resources currently stored in this collection or 0
     * if the collection is empty.
     *
     * @return The number of resource in the collection.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#getResourceCount()
     */
    public int getResourceCount() throws XMLDBException {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Returns a list of the ids for all resources stored in the collection.
     *
     * @return A string array containing the names for all 
     *         <code>Resource</code>s in the collection.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#listResources()
     */
    public String[] listResources() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates a new empty <code>Resource</code> with the provided id. The type 
     * of <code>Resource</code> returned is determined by the <code>type</code>
     * parameter. The XML:DB API currently defines "XMLResource" and 
     * "BinaryResource" as valid resource types. The <code>id</code> provided 
     * must be unique within the scope of the collection. If <code>id</code> is 
     * null or its value is empty then an id is generated by calling 
     * <code>createId()</code>. The <code>Resource</code> created is not stored 
     * to the database until <code>storeResource()</code> is called.
     *
     * @param id The unique id to associate with the created 
     *           <code>Resource</code>.
     * @param type The <code>Resource</code> type to create.
     * @return An empty <code>Resource</code> instance.    
     * @exception XMLDBException With expected error codes.<br />
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
    public Resource createResource(final String id, final String type)
            throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub

    }

    /**
     * Stores the provided resource into the database. If the resource does not
     * already exist it will be created. If it does already exist it will be
     * updated.
     *
     * @param res The resource to store in the database.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.INVALID_RESOURCE</code> if the 
     *            <code>Resource</code> is not valid.
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#storeResource(
     *      org.xmldb.api.base.Resource)
     */
    public void storeResource(final Resource res) throws XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * Retrieves a <code>Resource</code> from the database. If the 
     * <code>Resource</code> could not be located a null value will be returned.
     *
     * @param id The unique id for the requested resource.
     * @return The retrieved <code>Resource</code> instance.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />    
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#getResource(java.lang.String)
     */
    public Resource getResource(final String id) throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates a new unique ID within the context of the <code>Collection</code>
     *
     * @return The created id as a string.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#createId()
     */
    public String createId() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns true if the  <code>Collection</code> is open false otherwise.
     * Calling the <code>close</code> method on <code>Collection</code> will 
     * result in <code>isOpen</code> returning false. It is not safe to use 
     * <code>Collection</code> instances that have been closed.
     *
     * @return true If the <code>Collection</code> is open, false otherwise.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     * @see org.xmldb.api.base.Collection#isOpen()
     */
    public boolean isOpen() throws XMLDBException {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Releases all resources consumed by the <code>Collection</code>. The 
     * <code>close</code> method must always be called when use of a 
     * <code>Collection</code> is complete. It is not safe to use a  
     * <code>Collection</code> after the <code>close</code> method has been 
     * called.
     *
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     * @see org.xmldb.api.base.Collection#close()
     */
    public void close() throws XMLDBException {
        // TODO Auto-generated method stub

    }

}
