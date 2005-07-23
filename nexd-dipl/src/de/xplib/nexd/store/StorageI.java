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
 * $Log: StorageI.java,v $
 * Revision 1.11  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.10  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.9  2005/04/22 14:59:42  nexd
 * SOAP support and performance update.
 *
 * Revision 1.8  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.7  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.6  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.store;


/**
 * <p>Base interface for all NEXD compatible storage drivers.</p>
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.11 $
 */
public interface StorageI {

    /**
     * @clientCardinality 1
     * @directed true
     * @label manages physical
     * @link aggregationByValue
     * @supplierCardinality 1..*
     */
    /*#de.xplib.nexd.store.StorageEntityI lnkStorageEntityI*/
    
    /**
     * The name of the internal collection where the VCLSchema is stored.
     */
    String VCL_SCHEMA_COLLECTION = "$vcl-schema";
    
    /**
     * The resource name of the VCLSchema. 
     */
    String VCL_SCHEMA_RESOURCE = "vcl-schema.vcs";
    
    /**
     * Internal Collection name where all pre compiled virtual resources are
     * stored.
     */
    String PCVR_DATA_COLLECTION = "$pcvr-data";
    
    /**
     * Name of the internal collection where the xsl stylesheet for a 
     * virtual collection is stored.
     */
    String XSL_STYLESHEET_COLLECTION = "$xsl-stylesheet";
    
    /**
     * The resource name for a xsl stylesheet of a virtual collection.
     */
    String XSL_STYLESHEET_RESOURCE = "xsl-stylesheet.xsl";
    
    /**
     * The name of the internal collection where all transformed virtual 
     * resources are stored.
     */
    String XSL_DATA_COLLECTION = "$xsl-data";

    /**
     * Returns the factory that creates the concrete <code>StorageI</code>
     * implementation.
     * 
     * @return The <code>AbstractStorageFactory</code> that creates and returns 
     *         this instance of <code>StorageI</code>.
     */
    AbstractStorageFactory getFactory();

    /**
     * @param username The username used for authentication.
     * @param password The passord used for authentication.
     * @exception StorageException If something goes wrong during connection.
     */
    void open(String username, String password) throws StorageException;

    /**
     * Checks if an id is already used to identify a stored object.
     *
     * @param collIn The context <code>StorageCollectionI</code>.
     * @param oidIn The id to look for.
     * @return <code>true</code> if the id is already used, otherwise
     *         <code>false</code>.
     * @throws StorageException ..
     */
    boolean containsId(StorageCollectionI collIn, String oidIn)
            throws StorageException;

    /**
     * Stores a new <code>StorageCollectionI</code> in the underlying media.
     *
     * @param collPath The path which locates the 
     *                 <code>StorageCollectionI</code> in the system.
     * @param typeIn The collection type.
     * @return A new instance of <code>StorageCollectionI</code>
     * @throws StorageException If the <code>collPath</code> already exists or
     *                          something <code>StorageI</code> specific goes
     *                          wrong.
     */
    StorageCollectionI storeCollection(String collPath, short typeIn)
            throws StorageException;

    /**
     * Removes a <code>StorageCollectionImpl</code> in the underlying media.
     *
     * @param collPath The path which locates the 
     *                 <code>StorageCollectionImpl</code> in the system.
     * @throws StorageException If the <code>collPath</code> doesn't exists or
     *                          something <code>StorageI</code> specific goes
     *                          wrong.
     */
    void dropCollection(String collPath) throws StorageException;

    /**
     * Selects a <code>StorageCollectionI</code> or returns <code>null</code>, 
     * if <code>collPath</code> doesn't locate anything.
     *
     * @param collPath Returns the database collection identified by the
     *                 specified <code>collPath</code>.
     * @return The <code>StorageCollectionI</code> or <code>null</code> if no
     *         collection matches the path.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    StorageCollectionI queryCollection(String collPath) throws StorageException;

    /**
     * Returns the number of child collection that are located under
     * <code>collIn</code>.
     *
     * @param collIn The context <code>StorageCollectionI</code>.
     * @return The number of child collections.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    int queryCollectionCount(StorageCollectionI collIn) throws StorageException;

    /**
     * Returns an array with all names of collections that are located under
     * <code>collIn</code>.
     *
     * @param collIn The context <code>StorageCollectionI</code>.
     * @return An array with all names.
     * @exception StorageException If something <code>StorageI</code> specific
     *                             goes wrong.
     */
    String[] queryCollections(StorageCollectionI collIn) 
            throws StorageException;

    /**
     * Stores a XML-Schema or a DTD for the given 
     * <code>StorageCollectionI</code> object.
     *
     * @param collIn The context <code>StorageCollectionI</code> instance.
     * @param schemaIn The <code>StorageValidationObjectI</code> instance to
     *                 store or update.
     * @exception StorageException If something <code>StorageI</code> specific
     *                             goes wrong.
     */
    void storeValidationObject(StorageCollectionI collIn,
                               StorageValidationObjectI schemaIn) 
            throws StorageException;

    /**
     * Remove a <code>StorageValidationObjectI</code> from the given
     * <code>StorageCollectionI</code> instance, if a schema exists.
     *
     * @param collIn The context <code>StorageCollectionI</code> instance.
     * @exception StorageException A database specific exception.
     */
    void dropValidationObject(StorageCollectionI collIn) 
            throws StorageException;

    /**
     * Returns the <code>StorageValidationObjectI</code> object for the given
     * <code>StorageCollectionI collIn</code> instance or <code>null</code>.
     *
     * @param collIn The context <code>StorageCollectionI</code> instance.
     * @return The <code>StorageValidationObjectI</code> object or
     *         <code>null</code>.
     * @exception StorageException If something <code>StorageI</code> specific
     *                             goes wrong.
     */
    StorageValidationObjectI queryValidationObject(StorageCollectionI collIn)
            throws StorageException;

    /**
     * Stores a <code>StorageObjectI</code> instance as a document.
     * @param collIn ..
     * @param docIn ..
     * @throws StorageException ..
     */
    void storeDocument(StorageCollectionI collIn, StorageDocumentObjectI docIn)
            throws StorageException;

    /**
     * Updates a stored <code>StorageDocumentObjectI</code> instance.
     *
     * @param collIn The context <code>StorageCollectionI</code> instance.
     * @param docIn The <code>StorageDocumentObjectI</code> instance to update.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    void updateDocument(StorageCollectionI collIn, StorageDocumentObjectI docIn)
            throws StorageException;

    /**
     * Removes a <code>StorageObjectI</code> object from the
     * <code>StorageCollectionI collIn</code> instance.
     *
     * @param collIn The context <code>StorageCollectionI</code> object.
     * @param objIn The <code>StorageObjectI</code> to delete.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    void dropObject(StorageCollectionI collIn, StorageObjectI objIn)
            throws StorageException;

    /**
     * Returns a <code>StorageObjectI</code> instance identified by the
     * parameter <code>idIn</code> or if there is no entry for <code>idIn</code>
     * it returns <code>null</code>.
     *
     * @param collIn The associated parent <code>StorageCollectionI</code>.
     * @param oidIn The unique id for the <code>StorageObjectI</code>.
     * @return The <code>StorageObjectI</code> instance of <code> null</code>.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    StorageObjectI queryObject(StorageCollectionI collIn, String oidIn)
            throws StorageException;

    /**
     * Returns the number of <code>StorageObjectI</code> objects that are
     * stored in the <code>StorageCollectionI</code> identified by
     * <code>collIn</code>.
     *
     * @param collIn The context <code>StorageCollectionI</code> object.
     * @return The number of stored <code>StorageObjectI</code> objects.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    int queryObjectCount(StorageCollectionI collIn) throws StorageException;

    /**
     * Returns an array with the names of all <code>StorageObjectI</code>
     * objects stored in the <code>StorageCollectionI</code> 
     * <code>collIn</code>.
     *
     * @param collIn The context <code>StorageCollectionI</code> object.
     * @return An array with all names.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    String[] queryObjects(StorageCollectionI collIn) throws StorageException;

    /**
     * Executes an XPath query against all <code>SixdmlResource</code> instances
     * in the given <code>StorageCollectionI</code> object.
     *
     * @param collIn The context <code>StorageCollectionI</code> instance.
     * @param queryIn The xpath expression to execute.
     * @return All found <code>StorageObjectI</code> object.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    StorageObjectI[] queryObjectsByXPath(StorageCollectionI collIn,
                                         String queryIn) 
            throws StorageException;

    /**
     * Executes an XPath query against a single <code>SixdmlResource</code>
     * instances, identified by <code>idIn</code> in the given
     * <code>StorageCollectionI</code> object.
     *
     * @param collIn The context <code>StorageCollectionI</code> instance.
     * @param oidIn The <code>StorageObjectI</code> identifier.
     * @param queryIn The xpath expression to execute.
     * @return All found <code>StorageObjectI</code> object.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    StorageObjectI[] queryObjectByXPath(StorageCollectionI collIn,
                                        String oidIn, String queryIn) 
            throws StorageException;

    /**
     * <p>Stores the reference between <code>collIn</code> and
     * <code>vcollIn</code>. These refrences are used for the update process of
     * a {@link _de.xplib.nexd.api.VirtualCollection}</code>
     *
     * @param collIn <p>The <code>StorageCollectionI</code> for a
     *               {@link org.sixdml.dbmanagement.SixdmlCollection}.</p>
     * @param vcollIn <p>The <code>StorageCollectionI</code> for a
     *                {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     * @exception StorageException <p>If something <code>StorageI</code>
     *                             specific goes wrong.</p>
     */
    void storeCollectionReference(StorageCollectionI collIn,
                                  StorageCollectionI vcollIn) 
            throws StorageException;

    /**
     * <p>Deletes all reference entries that exist for the given
     * <code>vcollIn</code> which is a 
     * {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     *
     * @param vcollIn <p>The context <code>StorageCollectionI</code>.</p>
     * @exception StorageException <p>If something <code>StorageI</code>
     *                             specific goes wrong.</p>
     */
    void dropCollectionReference(StorageCollectionI vcollIn)
            throws StorageException;

    /**
     * <p>Returns all <code>StorageCollectionI</code> instances that represent a
     * <code>Collection</code> which are refrenced by the given
     * <code>collIn</code>, this is the physical collection for a
     * {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     *
     * @param collIn <p>The context <code>StorageCollectionI</code>.</p>
     * @return <p>An array with all refrenced.</p>
     * @exception StorageException <p>If something <code>StorageI</code>
     *                             specific goes wrong.</p>
     */
    StorageCollectionI[] queryCollectionReferences(StorageCollectionI collIn)
            throws StorageException;
}
