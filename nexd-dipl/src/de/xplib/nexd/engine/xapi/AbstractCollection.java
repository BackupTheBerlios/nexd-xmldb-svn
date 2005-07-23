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
 * $Log: AbstractCollection.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.9  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.8  2005/04/22 14:59:42  nexd
 * SOAP support and performance update.
 *
 * Revision 1.7  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
 *
 * Revision 1.6  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.5  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi;

import java.io.IOException;
import java.net.URL;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlIndex;
import org.sixdml.exceptions.IndexAlreadyExistsException;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.sixdml.exceptions.NonExistentIndexException;
import org.sixdml.exceptions.UnsupportedIndexTypeException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.store.StorageCollectionI;

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
public abstract class AbstractCollection extends AbstractConfigurable implements
        SixdmlCollection {

    /**
     * @backDirected true
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole resources
     */
    /*#AbstractResource lnkAbstractResource*/

    /**
     * The underlying <code>StorageCollectionI</code>.
     */
    protected final StorageCollectionI dbColl;

    /**
     * Comment for <code>engine</code>
     */
    protected final NEXDEngineI engine;

    /**
     * Comment for <code>name</code>
     */
    private String name = null;

    /**
     * Comment for <code>open</code>
     */
    private boolean open = false;

    /**
     * @param engineIn The used <code>NEXDEngineI</code> instance.
     * @param dbCollIn The underlying <code>StorageCollectionI</code>.
     */
    public AbstractCollection(final NEXDEngineI engineIn,
            final StorageCollectionI dbCollIn) {
        super();

        this.engine = engineIn;
        this.dbColl = dbCollIn;
        this.open = true;
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
            throws InvalidCollectionDocumentException, IOException,
            InvalidSchemaException, XMLDBException {

        // Empty, is not needed by all Collection types.
    }

    /**
     * Returns the contents of the schema for the collection as a string.
     *
     * @return The schema for the collection or NULL if none exists.
     * @exception XMLDBException If a database error occurs.
     * @see org.sixdml.dbmanagement.SixdmlCollection#showSchema()
     */
    public String showSchema() throws XMLDBException {
        // Empty, is not needed by all Collection types.
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
        // Empty, is not needed by all Collection types.
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
            throws UnsupportedIndexTypeException, IndexAlreadyExistsException,
            XMLDBException {

    }

    /**
     * Removes a particular index from the collection.
     *
     * @param nameIn The index to remove
     * @exception NonExistentIndexException If there is no index for the name.
     * @exception XMLDBException If a database error occurs.
     * @see org.sixdml.dbmanagement.SixdmlCollection#removeIndex(
     *      java.lang.String)
     */
    public void removeIndex(final String nameIn)
            throws NonExistentIndexException, XMLDBException {
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
        return new SixdmlIndex[0];
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
    public final String getName() throws XMLDBException {

        if (this.name == null) {
            if (this.getProperty("name") == null) {
                String[] path = this.dbColl.getPath().split("/");
                if (path.length != 0) {
                    this.name = path[path.length - 1];
                }
            } else {
                this.name = this.getProperty("name");
            }
        }
        return this.name;
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
    public final Collection getParentCollection() throws XMLDBException {

        if (!this.isOpen()) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        return this.engine.queryParentCollection(this);
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
        if (!this.open) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
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
        if (!this.open) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        return new String[0];
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

        if (!this.open) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }

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
        if (!this.open) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        return this.engine.queryResourceCount(this);
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
        if (!this.open) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        String[] names = this.engine.queryResources(this);

        String[] copy = new String[names.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = names[i];
        }
        return copy;
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
    public final boolean isOpen() throws XMLDBException {
        return this.open;
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
    public final void close() throws XMLDBException {
        this.open = false;

        // If this is the root collection close the connection. 
        if (this.dbColl.getPath().equals("/db")) {
            this.engine.close();
        }
    }

    /**
     * @return The underlying <code>StorageCollectionI</code>.
     */
    public final StorageCollectionI getStorageCollection() {
        return this.dbColl;
    }

    /**
     * Retrieves a <code>Resource</code> from the database. If the
     * <code>Resource</code> could not be located a null value will be returned.
     *
     * @param idIn The unique id for the requested resource.
     * @return The retrieved <code>Resource</code> instance.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the
     *            <code>close</code> method has been called on the
     *            <code>Collection</code><br />
     * @see org.xmldb.api.base.Collection#getResource(java.lang.String)
     */
    public Resource getResource(final String idIn) throws XMLDBException {
        if (!this.open) {
            throw new XMLDBException(ErrorCodes.COLLECTION_CLOSED);
        }
        return this.engine.queryResource(this, idIn);
    }

}