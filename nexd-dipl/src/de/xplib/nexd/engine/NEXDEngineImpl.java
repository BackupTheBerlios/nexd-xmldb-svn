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
 * $Log: NEXDEngineImpl.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.12  2005/04/24 15:00:27  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.11  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.10  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
 *
 * Revision 1.9  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.8  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 * Revision 1.7  2005/03/01 10:34:37  nexd
 * Advanced VCL support
 *
 */
package de.xplib.nexd.engine;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sixdml.SixdmlResourceSet;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidQueryException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;

import de.xplib.nexd.api.InvalidParentCollectionException;
import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.pcvr.AbstractPCVRFactory;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.api.vcl.AbstractVCLParser;
import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLParserI;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VariableExistsException;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.config.ConfigurationException;
import de.xplib.nexd.engine.config.EngineConfig;
import de.xplib.nexd.engine.xapi.AbstractCollection;
import de.xplib.nexd.engine.xapi.BinaryResourceImpl;
import de.xplib.nexd.engine.xapi.CollectionImpl;
import de.xplib.nexd.engine.xapi.VirtualCollectionImpl;
import de.xplib.nexd.engine.xapi.VirtualResourceImpl;
import de.xplib.nexd.engine.xapi.XMLResourceImpl;
import de.xplib.nexd.engine.xml.dom.DocumentImpl;
import de.xplib.nexd.store.AbstractStorageFactory;
import de.xplib.nexd.store.EmptyStorageObject;
import de.xplib.nexd.store.StorageCollectionI;
import de.xplib.nexd.store.StorageDocumentObjectI;
import de.xplib.nexd.store.StorageException;
import de.xplib.nexd.store.StorageI;
import de.xplib.nexd.store.StorageObjectI;
import de.xplib.nexd.store.StorageValidationObjectI;


/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class NEXDEngineImpl implements NEXDEngineI {

    /**
     * @label uses
     */

    /*#de.xplib.nexd.engine.CollectionLockVisitor Dependency_Link*/

    /**
     * The used system logger.
     */
    private final Log log = LogFactory.getLog(NEXDEngineImpl.class);

    /**
     *
     */
    private static final String ROOT_COLLECTION_PATH = "/db";

    /**
     * Shared instance of <code>Transaction</code> used for dirty read
     * transactions.
     * @clientCardinality 1
     * @clientRole transactions
     * @directed true
     * @link aggregation
     * @supplierCardinality 1
     */
    private final TransactionManager transactions = TransactionManager
            .getInstance();

    /**
     * Comment for <code>config</code>
     */
    private final EngineConfig config;

    /**
     * Comment for <code>storage</code>
     */
    private StorageI storage = null;

    /**
     * Comment for <code>validator</code>
     */
    private SchemaChecker validator = null;

    /**
     * Comment for <code>vcollManager</code>
     * @clientCardinality 1
     * @clientRole vCollManager
     * @directed true
     * @link aggregation
     * @supplierCardinality 1
     */
    private VirtualCollectionManager vcollManager = null;

    /**
     * Comment for <code>cache</code>
     * @clientCardinality 1
     * @clientRole cache
     * @directed true
     * @supplierCardinality 1
     */
    private final XPathQueryCache cache;

    /**
     * @param configIn The main engine configuration.
     */
    public NEXDEngineImpl(final EngineConfig configIn) {
        super();

        this.config = configIn;

        int queries = this.config.getQueryCacheSize();
        if (queries <= 0) {
            this.cache = XPathQueryCache.getInstance();
        } else {
            this.cache = XPathQueryCache.getInstance(queries);
        }

        log.info("Setting system properties");

        // setup the vcl parser, may be layed out to config
        System.setProperty(VCLParserI.VCL_PARSER_KEY,
                "de.xplib.nexd.engine.xapi.vcl.VCLParserImpl");

        System.setProperty(AbstractPCVRFactory.FACTORY_KEY,
                "de.xplib.nexd.engine.xapi.pcvr.PCVRFactoryImpl");
/*
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");
        */
    }

    /**
     * Opens a connection to NEXD XML-Database engine.
     *
     * @param username The username used for authentication.
     * @param password The password used for authentication.
     * @exception XMLDBException If the authentication fails or an internal
     *         error occures.
     * @see de.xplib.nexd.comm.NEXDEngineI#open(
     *      java.lang.String, java.lang.String)
     */
    public void open(final String username, final String password)
            throws XMLDBException {

        if (this.storage != null) {
            return;
        }

        try {
            AbstractStorageFactory factory = AbstractStorageFactory
                    .newInstance();
            this.storage = factory.getUniqueStorage(this.config
                    .getStorageParams());
            this.storage.open(username, password);

            this.validator = new SchemaChecker(this.storage);

            this.vcollManager = new VirtualCollectionManager(this, this.storage);

        } catch (ClassNotFoundException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (InstantiationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (IllegalAccessException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * @throws XMLDBException ..
     * @see de.xplib.nexd.comm.NEXDEngineI#close()
     */
    public void close() throws XMLDBException {
        try {
            NEXDEnginePool.getInstance().releaseEngine(this);
        } catch (ConfigurationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Starts a new transaction if the given <code>SixdmlCollection</code>
     * instance isn't already in another transaction.
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @return A unique transaction identifier.
     * @throws XMLDBException If the given <code>SixdmlCollection</code>
     *                        instance is already in another transaction.
     * @see de.xplib.nexd.comm.NEXDEngineI#beginTransaction(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public String beginTransaction(final SixdmlCollection collIn)
            throws XMLDBException {

        String transId = this.transactions.begin(this.getStorageCollection(
                collIn).getPath());

        collIn.setProperty(TRANSACTION_ID_KEY, transId);

        return transId;
    }

    /**
     * Commits an open transaction.
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @throws XMLDBException If the given <code>SixdmlCollection</code>
     *                        instance isn't in an open transaction.
     * @see de.xplib.nexd.comm.NEXDEngineI#commitTransaction(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public void commitTransaction(final SixdmlCollection collIn)
            throws XMLDBException {

        this.transactions.commit(collIn.getProperty(TRANSACTION_ID_KEY));
        collIn.setProperty(TRANSACTION_ID_KEY, null);
    }

    /**
     * Checks if an id is already used to identify a stored object.
     *
     * @param collIn The context <code>SixdmlCollection</code>.
     * @param idIn The id to look for.
     * @return <code>true</code> if the id is already used, otherwise
     *         <code>false</code>.
     * @throws XMLDBException ..
     * @see de.xplib.nexd.comm.NEXDEngineI#containsId(java.lang.String)
     */
    public boolean containsId(final SixdmlCollection collIn, final String idIn)
            throws XMLDBException {

        StorageCollectionI coll = this.getStorageCollection(collIn);

        try {
            return this.storage.containsId(coll, idIn);
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Stores a new <code>SixdmlCollection</code> in the backend.
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param nameIn The name of the new <code>SixdmlCollection</code>.
     * @return The instance of the new <code>SixdmlCollection</code>.
     * @throws XMLDBException If the <code>SixdmlCollection</code> already
     *                        exists or something goes wrong in the
     *                        <code>StorageI</code>.
     * @see NEXDEngineI#storeCollection(String)
     */
    public SixdmlCollection storeCollection(final SixdmlCollection collIn,
            final String nameIn) throws XMLDBException {

        if (collIn instanceof VirtualCollection) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR,
                    "A VirtualCollection has no child collections.");
        }

        this.checkMagicChar(nameIn);

        try {

            String path = this.getStorageCollection(collIn).getPath() + "/"
                    + nameIn;

            return new CollectionImpl(this, this.storage.storeCollection(path,
                    StorageCollectionI.TYPE_COLLECTION));
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * <Some description here>
     *
     * @param collIn
     * @param nameIn
     * @param schemaIn
     * @return
     * @throws InvalidCollectionReferenceException
     * @throws InvalidParentCollectionException
     * @throws InvalidQueryException
     * @throws InvalidVCLSchemaException
     * @throws IOException
     * @throws SAXException
     * @throws UndeclaredVariableException
     * @throws VariableExistsException
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#storeCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection,
     *      java.lang.String, java.net.URL)
     */
    public VirtualCollection storeCollection(final SixdmlCollection collIn,
            final String nameIn, final URL schemaIn)
            throws InvalidCollectionReferenceException,
            InvalidParentCollectionException, InvalidQueryException,
            InvalidVCLSchemaException, IOException, SAXException,
            UndeclaredVariableException, VariableExistsException,
            XMLDBException {

        return this.storeCollection(collIn, nameIn, schemaIn, null);
    }

    /**
     * <Some description here>
     *
     * @param collIn
     * @param nameIn
     * @param schemaIn
     * @param xslIn
     * @return
     * @throws InvalidCollectionReferenceException
     * @throws InvalidParentCollectionException
     * @throws InvalidQueryException
     * @throws InvalidVCLSchemaException
     * @throws IOException
     * @throws SAXException
     * @throws UndeclaredVariableException
     * @throws VariableExistsException
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#storeCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection,
     *      java.lang.String, java.net.URL)
     */
    public VirtualCollection storeCollection(final SixdmlCollection collIn,
            final String nameIn, final URL schemaIn, final URL xslIn)
            throws InvalidCollectionReferenceException,
            InvalidParentCollectionException, InvalidQueryException,
            InvalidVCLSchemaException, IOException, SAXException,
            UndeclaredVariableException, VariableExistsException,
            XMLDBException {

        this.checkMagicChar(nameIn);

        if (!(collIn instanceof CollectionImpl)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        String path = this.getStorageCollection(collIn).getPath() + "/"
                + nameIn;

        if (this.queryCollection(path) != null) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, "A collection ['"
                    + path + "'] allready exists.");
        }

        VCLSchema schema = AbstractVCLParser.getInstance().parse(schemaIn);

        VirtualCollection vcoll = this.vcollManager.create(path, schema);

        if (xslIn != null) {
            this.storeCollectionStylesheet(vcoll, xslIn);
        }

        return vcoll;
    }

    /**
     * <p>Removes a <code>StorageCollectionI</code> from the backend.</p>
     * <p>A <code>Collection</code> that isn't an instance of
     * {@link VirtualCollection} can only be deleted if it doesn't have any
     * reference from a {@link VirtualCollection}. If there is any reference an
     * <code>XMLDBException</code> is thrown until all referencing objects are
     * also deleted.</p>
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param nameIn The name of the <code>SixdmlCollection</code> to delete.
     * @throws XMLDBException If the <code>StorageCollectionI</code> doesn't
     *                        exist or something goes wrong in the
     *                        <code>StorageI</code>.
     * @see NEXDEngineI#dropCollection(String)
     */
    public void dropCollection(final SixdmlCollection collIn,
            final String nameIn) throws XMLDBException {

        if (collIn == null || nameIn == null) {
            throw new IllegalArgumentException(
                    "The parameters collIn and nameIn cannot be null.");
        }

        if (nameIn.equals(ROOT_COLLECTION_PATH)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        try {
            // If the input collection is not an instance of VirtualCollection
            // it could have VirtualCollection references, which means that it
            // cannot be deleted until the referencing collections are deleted.
            if (!(collIn instanceof VirtualCollection)) {
                StorageCollectionI scoll = this.storage.queryCollection(this
                        .getStorageCollection(collIn).getPath()
                        + "/" + nameIn);

                if (scoll == null) {
                    throw new XMLDBException(ErrorCodes.NO_SUCH_COLLECTION);
                }

                StorageCollectionI[] refs = this.storage
                        .queryCollectionReferences(scoll);
                if (refs.length != 0) {

                    String refList = "";
                    for (int i = 0; i < refs.length; i++) {
                        refList += refs[i].getPath() + ", ";
                    }
                    refList = refList.substring(0, refList.length() - 2);

                    throw new XMLDBException(ErrorCodes.VENDOR_ERROR, -1,
                            "Cannot delete Collection[" + scoll.getPath() + "]"
                                    + " until the VirtualCollection(s)["
                                    + refList + "]" + " are also delete.");
                }
            }

            String path = this.getStorageCollection(collIn).getPath() + "/"
                    + nameIn;

            this.storage.dropCollection(path);

            // if everything is done, remove the collection from the
            // query cache.
            this.cache.remove(path);

        } catch (StorageException e) {
            if (e.getCode() == StorageException.NO_SUCH_COLLECTION) {
                throw new XMLDBException(ErrorCodes.NO_SUCH_COLLECTION, e
                        .getMessage());
            }
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Selects a <code>StorageCollectionI</code> in the <code>StorageI</code>.
     * If no <code>StorageCollectionI</code> for <code>collPath</code> exists
     * this method returns <code>null</code>.
     *
     * @param collPath The path which locates the
     *        <code>StorageCollectionI</code>
     * @return The <code>SixdmlCollection</code> or <code>null</code>.
     * @throws XMLDBException If something <code>StorageI</code> specific
     *                        goes wrong.
     * @see NEXDEngineI#queryCollection(String)
     */
    public SixdmlCollection queryCollection(final String collPath)
            throws XMLDBException {

        this.checkMagicChar(collPath);

        StorageCollectionI coll;
        try {
            coll = this.storage.queryCollection(collPath);
        } catch (StorageException e) {
            if (e.getCode() == StorageException.NO_SUCH_COLLECTION) {
                throw new XMLDBException(ErrorCodes.NO_SUCH_COLLECTION, e
                        .getMessage());
            }
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        SixdmlCollection sixdml = null;
        if (coll != null) {
            if (coll.getType() == StorageCollectionI.TYPE_COLLECTION) {
                sixdml = new CollectionImpl(this, coll);
            } else {
                sixdml = new VirtualCollectionImpl(this, coll);
            }
        }
        return sixdml;
    }

    /**
     * Selects a <code>SixdmlCollection</code> that is a child collection of the
     * given <code>SixdmlCollection</code> instance and has the name that is
     * stored in the parameter <code>nameIn</code>.<br />
     * This method is used to keep existing transaction states in the collection
     * hierarchy.
     *
     * @param collIn The reference parent <code>SixdmlCollection</code> object
     * @param nameIn The name of the child <code>SixdmlCollection</code>.
     * @return The child <code>SixdmlCollection</code> instance.
     * @throws XMLDBException If something <code>StorageI</code> specific
     *                        goes wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryChildCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection, java.lang.String)
     */
    public SixdmlCollection queryChildCollection(final SixdmlCollection collIn,
            final String nameIn) throws XMLDBException {

        String path = this.getStorageCollection(collIn).getPath() + "/"
                + nameIn;

        SixdmlCollection child = this.queryCollection(path);

        if (child != null) {
            String transId = collIn.getProperty(TRANSACTION_ID_KEY);
            if (transId instanceof String) {
                child.setProperty(TRANSACTION_ID_KEY, transId);
            } else if (child.getProperty(TRANSACTION_ID_KEY) != null) {
                child.setProperty(TRANSACTION_ID_KEY, null);
            }
        }

        return child;
    }

    /**
     * Selects a <code>SixdmlCollection</code> that is the parent collection of
     * the given <code>SixdmlCollection</code> instance.<br />
     * This method is used to keep existing transaction states in the collection
     * hierarchy.
     *
     * @param collIn The reference child <code>SixdmlCollection</code> object.
     * @return The parent <code>SixdmlCollection</code> instance.
     * @throws XMLDBException If something <code>StorageI</code> specific
     *                        goes wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryParentCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public SixdmlCollection queryParentCollection(final SixdmlCollection collIn)
            throws XMLDBException {

        String path = this.getStorageCollection(collIn).getPath();

        SixdmlCollection parent = null;
        if (!path.equals(ROOT_COLLECTION_PATH)) {

            path = path.substring(0, path.lastIndexOf('/'));

            parent = this.queryCollection(path);
            String transId = collIn.getProperty(TRANSACTION_ID_KEY);
            if (transId != null) {
                parent.setProperty(TRANSACTION_ID_KEY, transId);
            }
        }
        return parent;
    }

    /**
     * Returns the number of child collection that are located under
     * <code>collIn</code>.
     *
     * @param collIn The context <code>SixdmlCollection</code>.
     * @return The number of child collections.
     * @throws XMLDBException If something <code>StorageI</code> specific goes
     *                        wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollectionCount(
     *      StorageCollectionI)
     */
    public int queryCollectionCount(final SixdmlCollection collIn)
            throws XMLDBException {

        int count = 0;
        if (!(collIn instanceof VirtualCollection)) {

            try {
                count = this.storage.queryCollectionCount(this
                        .getStorageCollection(collIn));
            } catch (StorageException e) {
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e
                        .getMessage());
            }
        }
        return count;
    }

    /**
     * Returns an array with all names of collections that are located under
     * <code>collIn</code>.
     *
     * @param collIn The context <code>SixdmlCollection</code>.
     * @return An array with all names.
     * @exception XMLDBException If something <code>StorageI</code> specific
     *                           goes wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollections(
     *      StorageCollectionI)
     */
    public String[] queryCollections(final SixdmlCollection collIn)
            throws XMLDBException {

        String[] names;
        if (collIn instanceof VirtualCollection) {
            names = new String[0];
        } else {

            try {
                names = this.storage.queryCollections(this
                        .getStorageCollection(collIn));
            } catch (StorageException e) {
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e
                        .getMessage());
            }
        }
        return names;
    }

    /**
     * Stores a XML-Schema or DTD for <code>SixdmlCollection</code> object.
     * This task can only closed successfull, if all <code>SixdmlResource</code>
     * objects match on the given schema.
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param schemaIn The <code>URL</code> where the schema is.
     * @throws InvalidCollectionDocumentException If any XML-Document in the
     *                                            collection is not valid
     *                                            against the schema.
     * @throws InvalidSchemaException If the schema type if unkown.
     * @throws IOException If the schema can't be found.
     * @throws XMLDBException An database specific exception.
     */
    public void storeCollectionSchema(final SixdmlCollection collIn,
            final URL schemaIn) throws InvalidCollectionDocumentException,
            InvalidSchemaException, IOException, XMLDBException {

        if (!(collIn instanceof CollectionImpl)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        StorageCollectionI coll = ((CollectionImpl) collIn)
                .getStorageCollection();

        StorageValidationObjectI schema = this.validator.validate(coll,
                schemaIn);

        try {

            this.storage.storeValidationObject(coll, schema);
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Remove a schema, if it exists, from the given
     * <code>SixdmlCollection</code>.
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @throws XMLDBException An database specific exception.
     * @see de.xplib.nexd.comm.NEXDEngineI#dropCollectionSchema(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public void dropCollectionSchema(final SixdmlCollection collIn)
            throws XMLDBException {

        try {
            this.storage
                    .dropValidationObject(this.getStorageCollection(collIn));
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Returns the content of a <code>StorageValidationObjectImpl</code> object
     * for the give <code>SixdmlCollection</code> or <code>null</code>, if no
     * schema exists.
     *
     * @param collIn The context <code>SixdmlCollection</code> object.
     * @return The content of the schema or <code>null</code>.
     * @throws XMLDBException An database specific exception.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollectionSchema(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public String queryCollectionSchema(final SixdmlCollection collIn)
            throws XMLDBException {

        final StorageCollectionI coll = this.getStorageCollection(collIn);

        try {
            StorageValidationObjectI scs = this.storage
                    .queryValidationObject(coll);

            return (scs == null) ? null : scs.getContent();
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Stores the xsl-stylesheet for a <code>VirtualCollection</code>. The
     * stylesheed will be retrieved from the given <code>URL</code>. If there is
     * allready a xsl-stylesheet it will be replaced by the new one.</p>
     *
     * @param collIn <p>The context <code>VirtualCollection</code> instance.</p>
     * @param xslIn <p>The <code>URL</code> where the xsl-stylesheet is located.
     *              </p>
     * @exception IOException <p>If the given <code>URL</code> cannot be opend.
     *                         </p>
     * @exception SAXException <p>If it is not a valid xsl-stylesheet.</p>
     * @exception XMLDBException <p>with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific
     *            errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the
     *            <code>close</code> method has been called on the
     *            <code>Collection</code><br /></p>
     * @see de.xplib.nexd.comm.NEXDEngineI#storeCollectionStylesheet(
     *      _de.xplib.nexd.api.VirtualCollection, java.net.URL)
     */
    public void storeCollectionStylesheet(final VirtualCollection collIn,
            final URL xslIn) throws IOException, SAXException, XMLDBException {

        if (collIn == null) {
            throw new IllegalArgumentException(
                    "The VirtualCollection cannot be null.");
        }
        if (xslIn == null) {
            throw new IllegalArgumentException("The URL cannot be null.");
        }

        StorageCollectionI coll = this
                .getStorageCollection((VirtualCollectionImpl) collIn);

        StorageCollectionI xslColl = null;
        StorageCollectionI xslData = null;

        try {
            // 1. Try to fetch the xsl collection
            xslColl = this.storage.queryCollection(coll.getPath() + "/"
                    + StorageI.XSL_STYLESHEET_COLLECTION);

            // 2.a. It doesn't exist, so create it
            if (xslColl == null) {
                xslColl = this.storage.storeCollection(coll.getPath() + "/"
                        + StorageI.XSL_STYLESHEET_COLLECTION,
                        StorageCollectionI.TYPE_COLLECTION);
                // 2.b. It exists, we must drop the transformed resources.
            } else {
                this.storage.dropCollection(coll.getPath() + "/"
                        + StorageI.XSL_DATA_COLLECTION);

                StorageObjectI xslRes = this.storage.queryObject(xslColl,
                        StorageI.XSL_STYLESHEET_RESOURCE);
                this.storage.dropObject(xslColl, xslRes);
            }

            // 3. Store the new stylesheet
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();

            Document xslDoc = docBuilder.parse(xslIn.openStream());

            ((DocumentImpl) xslDoc)
                    .setResourceId(StorageI.XSL_STYLESHEET_RESOURCE);
            this.storage
                    .storeDocument(xslColl, (StorageDocumentObjectI) xslDoc);

            // 4. Create data collection, transform objects and store them
            xslData = this.storage.storeCollection(coll.getPath() + "/"
                    + StorageI.XSL_DATA_COLLECTION,
                    StorageCollectionI.TYPE_COLLECTION);

            Transformer trans = TransformerFactory.newInstance()
                    .newTransformer(new StreamSource(xslIn.openStream()));

            String[] names = this.storage.queryObjects(coll);
            for (int i = 0; i < names.length; i++) {
                StorageObjectI obj = this.storage.queryObject(coll, names[i]);
                if (obj.getType() == StorageObjectI.XML) {
                    DOMResult result = new DOMResult(docBuilder.newDocument());
                    DOMSource source = new DOMSource((Document) obj);

                    trans.transform(source, result);

                    DocumentImpl transDoc = (DocumentImpl) result.getNode();
                    transDoc.setResourceId(names[i]);

                    this.storage.storeDocument(xslData, transDoc);
                }
            }

            this.cache.remove(coll.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            // Something goes wrong drop created collections
            try {
                if (xslColl != null) {
                    this.storage.dropCollection(xslColl.getPath());
                }
                if (xslData != null) {
                    this.storage.dropCollection(xslData.getPath());
                }
            } catch (StorageException e1) {
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e1
                        .getMessage());
            }

            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Removes the xsl-stylesheet from the given <code>VirtualCollection
     * </code>. If there is no stylesheet this method does nothing.</p>
     *
     * @param collIn <p>The context <code>VirtualCollection</code> instance.</p>
     * @exception XMLDBException <p>If something database specific goes wrong.
     *                           </p>
     * @see de.xplib.nexd.comm.NEXDEngineI#dropCollectionStylesheet(
     *      _de.xplib.nexd.api.VirtualCollection)
     */
    public void dropCollectionStylesheet(final VirtualCollection collIn)
            throws XMLDBException {

        String cpath = this.getStorageCollection((SixdmlCollection) collIn)
                .getPath();

        try {
            String path = cpath + "/" + StorageI.XSL_STYLESHEET_COLLECTION;
            if (this.storage.queryCollection(path) != null) {
                this.storage.dropCollection(path);
            }
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }

        try {
            String path = cpath + "/" + StorageI.XSL_DATA_COLLECTION;
            if (this.storage.queryCollection(path) != null) {
                this.storage.dropCollection(path);
            }
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }

        this.cache.remove(cpath);
    }

    /**
     * <p><code>Returns the xsl-stylesheet for the given <code>collIn</code>. If
     * there is no stylesheet this method returns <code>null</code>.</p>
     *
     * @param collIn <p>The context <code>VirtualCollection</code> instance.</p>
     * @return <p>The xsl-stylesheet as a DOM <code>Node</code> or
     *         <code>null</code>.</p>
     * @exception XMLDBException <p>If something database specific goes wrong.
     *                           </p>
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollectionStylesheet()
     */
    public Node queryCollectionStylesheet(final VirtualCollection collIn)
            throws XMLDBException {

        String cpath = this.getStorageCollection((SixdmlCollection) collIn)
                .getPath()
                + "/" + StorageI.XSL_STYLESHEET_COLLECTION;

        try {
            StorageCollectionI xslColl = this.storage.queryCollection(cpath);

            Node xsl = null;
            if (xslColl != null) {
                StorageObjectI stObj = this.storage.queryObject(xslColl,
                        StorageI.XSL_STYLESHEET_RESOURCE);

                StorageDocumentObjectI sdObj = this.storage.getFactory()
                        .createDocumentObject(StorageI.XSL_STYLESHEET_RESOURCE,
                                ((StorageDocumentObjectI) stObj).getContent());

                xsl = sdObj.getContent();
            }
            return xsl;
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.NO_SUCH_RESOURCE, e
                    .getMessage());
        }
    }

    /**
     * Stores a <code>StorageObjectI</code> instance in the collection that is
     * identified by <code>collIn</code>.
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param resIn The new <code>SixdmlResource</code> to store.
     * @exception XMLDBException If something <code>StorageI</code> specific
     *                           goes wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#storeResource(
     *      de.xplib.nexd.store.StorageCollectionI,
     *      de.xplib.nexd.store.StorageObjectI)
     */
    public void storeResource(final SixdmlCollection collIn,
            final SixdmlResource resIn) throws XMLDBException {

        if (collIn == null || resIn == null) {
            throw new IllegalArgumentException(
                    "The parameters collIn and resIn cannot be null.");
        }

        if (!(collIn instanceof CollectionImpl)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        StorageCollectionI coll = ((CollectionImpl) collIn)
                .getStorageCollection();

        this.checkMagicChar(resIn.getId());
        this.checkMagicChar(coll.getPath());

        StorageDocumentObjectI sdObj = null;

        Node node = resIn.getContentAsDOM();

        log.debug("ResourceId: " + resIn.getId());
        log.debug("DocumentId: " + resIn.getDocumentId());

        try {
            if (node instanceof Document) {

                sdObj = this.storage.getFactory().createDocumentObject(
                        resIn.getId(), (Document) node);

                //validate the new document if a schema exists.
                this.validator.validate(coll, sdObj.getContent());

                // remove existing queries for this resource.
                this.cache.remove(coll.getPath());
                this.cache.remove(coll.getPath() + "/" + sdObj.getOID());

                this.storage.storeDocument(coll, sdObj);

                this.vcollManager.update((CollectionImpl) collIn,
                        (XMLResourceImpl) resIn);

            } else {
                log.trace("this.storage.storeNode()");
            }
        } catch (Exception e) {
            // If anything fails we have to remove the resource 
            // otherwise there will be a damaged virtual collection
            try {
                if (sdObj != null) {
                    this.storage.dropObject(coll, sdObj);
                }
            } catch (StorageException e1) {
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e1
                        .getMessage());
            }
            e.printStackTrace();
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, "Cause: "
                    + e.getClass() + "[" + e.getMessage() + "]");
        }
    }

    /**
     * Updates an existing <code>Resource</code> instance.
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param resIn The <code>Resource</code> to update.
     * @exception XMLDBException If something <code>StorageI</code> specific
     *                           goes wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#updateResource(
     *      org.sixdml.dbmanagement.SixdmlCollection,
     *      org.xmldb.api.base.Resource)
     */
    public void updateResource(final SixdmlCollection collIn,
            final Resource resIn) throws XMLDBException {

        if (collIn == null || resIn == null) {
            throw new IllegalArgumentException(
                    "The parameters collIn and resIn cannot be null.");
        }

        if (!(collIn instanceof CollectionImpl)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        StorageCollectionI coll = ((CollectionImpl) collIn)
                .getStorageCollection();

        if (resIn.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
            SixdmlResource res = (SixdmlResource) resIn;
            try {

                StorageDocumentObjectI sdObj = this.storage.getFactory()
                        .createDocumentObject(res.getId(),
                                (Document) res.getContentAsDOM());

                // remove existing queries for this resource.
                this.cache.remove(coll.getPath());
                this.cache.remove(coll.getPath() + "/" + sdObj.getOID());

                this.storage.updateDocument(coll, sdObj);
            } catch (StorageException e) {
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e
                        .getMessage());
            }
        } else if (resIn.getResourceType().equals(BinaryResource.RESOURCE_TYPE)) {

            log.error("Implement binary resources.");
        } else {
            throw new XMLDBException(ErrorCodes.UNKNOWN_RESOURCE_TYPE);
        }
    }

    /**
     * Removes a <code>Resource</code> instance from the
     * <code>SixdmlCollection collIn</code> object.
     *
     * @param collIn The context <code>SixdmlCollection</code> object.
     * @param resIn The <code>Resource</code> instance to remove.
     * @exception XMLDBException If something <code>StorageI</code> specific
     *                           goes wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#dropResource(
     *      org.sixdml.dbmanagement.SixdmlCollection,
     *      org.xmldb.api.base.Resource)
     */
    public void dropResource(final SixdmlCollection collIn, final Resource resIn)
            throws XMLDBException {

        if (resIn == null || collIn == null) {
            throw new IllegalArgumentException("The paramteres collIn ("
                    + collIn + ") " + "and resIn (" + resIn
                    + ") cannot be null");
        }

        // We can only delete from a normal collection
        if (!(collIn instanceof CollectionImpl)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }
        StorageCollectionI coll = ((CollectionImpl) collIn)
                .getStorageCollection();

        // We cannot delete from a system collection with $ in its name
        this.checkMagicChar(coll.getPath());

        EmptyStorageObject res;
        if (resIn.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
            res = new EmptyStorageObject(resIn.getId(), StorageObjectI.XML,
                    null);
        } else if (resIn.getResourceType().equals(BinaryResource.RESOURCE_TYPE)) {

            res = new EmptyStorageObject(resIn.getId(), StorageObjectI.BINARY,
                    null);
        } else {
            throw new XMLDBException(ErrorCodes.INVALID_RESOURCE);
        }

        try {

            this.storage.dropObject(coll, res);

            log.debug("Drop single resource");

            if (resIn.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                this.vcollManager.update((CollectionImpl) collIn,
                        (XMLResourceImpl) resIn);
            }

            // if everything is done, remove all cached query results for this
            // resource
            this.cache.remove(coll.getPath() + "/" + res.getOID());

        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Returns a <code>Resource</code> for the parameter <code>idIn</code> in
     * the <code>SixdmlCollection</code> <code>collIn</code> or
     * <code>null</code> if no resource for <code>idIn</code> exists.
     *
     * @param collIn The parent <code>SixdmlCollection</code>.
     * @param idIn The <code>Resource</code> id.
     * @return The <code>Resource</code> instance or <code>null</code>.
     * @throws XMLDBException If something <code>StorageI</code> specific goes
     *                        wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResource(
     *      org.sixdml.dbmanagement.SixdmlCollection, java.lang.String)
     */
    public Resource queryResource(final SixdmlCollection collIn,
            final String idIn) throws XMLDBException {

        if (idIn == null || collIn == null) {
            throw new IllegalArgumentException(
                    "The paramteres collIn and idIn cannot be null");
        }

        if (!(collIn instanceof AbstractCollection)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        StorageCollectionI coll = ((AbstractCollection) collIn)
                .getStorageCollection();

        Resource res = null;
        try {

            if (collIn instanceof VirtualCollectionImpl) {

                StorageCollectionI xslColl = this.storage.queryCollection(coll
                        .getPath()
                        + "/" + StorageI.XSL_DATA_COLLECTION);
                if (xslColl != null) {
                    coll = xslColl;
                }
            }

            StorageObjectI stRes = this.storage.queryObject(coll, idIn);
            if (stRes == null) {
                log.debug("There is no resource for id " + idIn);
            } else if (stRes.getType() == StorageObjectI.XML) {
                StorageDocumentObjectI sdObj = this.storage.getFactory()
                        .createDocumentObject(idIn,
                                ((StorageDocumentObjectI) stRes).getContent());

                if (collIn instanceof VirtualCollectionImpl) {
                    res = new VirtualResourceImpl(
                            (VirtualCollectionImpl) collIn, sdObj.getContent(),
                            idIn);
                } else {
                    res = new XMLResourceImpl((CollectionImpl) collIn, idIn);
                    res.setContent(sdObj.getContent());
                }
            } else {
                res = new BinaryResourceImpl((CollectionImpl) collIn, idIn);
                //res.setContent(stRes.getBytes());
            }
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }

        return res;
    }

    /**
     * Returns the number of <code>Resource</code> objects stored in
     * <code>collIn</code>.
     *
     * @param collIn The context <code>SixdmlCollection</code> object.
     * @return The number of stored <code>Resource</code> objects.
     * @throws XMLDBException If something <code>StorageI</code> specific goes
     *                        wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResourceCount(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int queryResourceCount(final SixdmlCollection collIn)
            throws XMLDBException {

        if (collIn == null) {
            throw new IllegalArgumentException(
                    "The paramtere collIn cannot be null");
        }
        if (!(collIn instanceof AbstractCollection)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        StorageCollectionI coll = ((AbstractCollection) collIn)
                .getStorageCollection();

        try {
            return this.storage.queryObjectCount(coll);
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Returns the names of all <code>Resource</code> objects stored in the
     * <code>SixdmlCollection</code> identfied by <code>collIn</code>.
     *
     * @param collIn The context <code>SixdmlCollection</code> object.
     * @return An array with the <code>Resource</code> names.
     * @throws XMLDBException If something <code>StorageI</code> specific goes
     *                        wrong.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResources(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public String[] queryResources(final SixdmlCollection collIn)
            throws XMLDBException {

        if (collIn == null) {
            throw new IllegalArgumentException(
                    "The paramtere collIn cannot be null");
        }
        if (!(collIn instanceof AbstractCollection)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        StorageCollectionI coll = ((AbstractCollection) collIn)
                .getStorageCollection();

        try {
            return this.storage.queryObjects(coll);
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Executes an XPath query against the database engine. This method returns
     * the found <code>Resource</code> objects in a <code>ResourceSet</code>
     * instance.
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param queryIn The query that will be executed against the engine.
     * @return The <code>ResourceSet</code> instance containing all matching
     *         objects.
     * @throws XMLDBException If a storage specific exception occures.
     * @throws InvalidQueryException if the query is not valid XPath.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResourceByXPath(
     *      java.lang.String)
     */
    public ResourceSet queryResourcesByXPath(final SixdmlCollection collIn,
            final String queryIn) throws XMLDBException, InvalidQueryException {

        if (collIn != null) {
            log.debug("Coll: '" + collIn.getName() + "'; Query: '" + queryIn
                    + "';");
        }

        StorageCollectionI coll = this.getStorageCollection(collIn);

        SixdmlResourceSet resset;

        // we have no matching storage collection so return an 
        // empty resource set.
        if (coll == null || queryIn == null) {
            resset = new SixdmlResourceSet();

        } else {

            String path = coll.getPath();

            // search for a cached result
            resset = this.cache.get(path, queryIn);
            if (resset == null) {

                try {
                    //We have a virtual collection test for xsl
                    if (collIn instanceof VirtualCollectionImpl) {
                        StorageCollectionI xslColl = this.storage
                                .queryCollection(coll.getPath() + "/"
                                        + StorageI.XSL_DATA_COLLECTION);
                        if (xslColl != null) {
                            coll = xslColl;
                        }
                    }
                    resset = this.buildQueryResourceSet(collIn, this.storage
                            .queryObjectsByXPath(coll, queryIn));
                } catch (StorageException e) {
                    if (e.getCode() == StorageException.BAD_XPATH_EXPRESSION) {
                        throw new InvalidQueryException(e.getMessage(), e);
                    }
                    throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e
                            .getMessage());
                }
                this.cache.add(path, queryIn, resset);
            }
        }

        return resset;
    }

    /**
     * Executes an XPath query against a single <code>Resource</code> in a
     * <code>SixdmlCollection</code>. This method returns the found
     * <code>Resource</code> objects in a <code>ResourceSet</code> instance.
     *
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param idIn The id of the context <code>SixdmlResource</code>.
     * @param queryIn The query that will be executed against the engine.
     * @return The <code>ResourceSet</code> instance containing all matching
     *         objects.
     * @throws XMLDBException If a storage specific exception occures.
     * @throws InvalidQueryException if the query is not valid XPath.
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResourceByXPath(
     *      java.lang.String, java.lang.String)
     */
    public ResourceSet queryResourceByXPath(final SixdmlCollection collIn,
            final String idIn, final String queryIn) throws XMLDBException,
            InvalidQueryException {

        if (collIn == null || idIn == null || queryIn == null) {
            throw new IllegalArgumentException(
                    "The parameters collIn, idIn or queryIn cannot be null.");
        }

        log.debug("Coll: '" + collIn.getName() + "'; ID: '" + idIn
                + "'; Query: '" + queryIn + "';");

        StorageCollectionI coll = this.getStorageCollection(collIn);

        String key = coll.getPath() + "/" + idIn;

        // lookup for cached query result.
        SixdmlResourceSet resset = this.cache.get(key, queryIn);

        // we have no cache query result, so start processing.
        if (resset == null) {

            try {

                // We have a virtual collection test for xsl
                if (collIn instanceof VirtualCollectionImpl) {
                    StorageCollectionI xslColl = this.storage
                            .queryCollection(coll.getPath() + "/"
                                    + StorageI.XSL_DATA_COLLECTION);
                    if (xslColl != null) {
                        coll = xslColl;
                    }
                }

                resset = this.buildQueryResourceSet(collIn, this.storage
                        .queryObjectByXPath(coll, idIn, queryIn));
            } catch (StorageException e) {
                if (e.getCode() == StorageException.BAD_XPATH_EXPRESSION) {
                    throw new InvalidQueryException(e.getMessage(), e);
                }
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e
                        .getMessage());
            }
            // cache this query result
            this.cache.add(key, queryIn, resset);
        } else {
            log.debug("Returns cached query result for [" + idIn + ", "
                    + queryIn + "].");
        }

        return resset;
    }

    /**
     * <p>Returns the {@link VCLSchema} for the given <code>collIn</code>.</p>
     *
     * @param collIn <p>The <code>VirtualCollection</code> that wants to
     *               retrieve its {@link VCLSchema}.</p>
     * @return <p>The {@link VCLSchema} instance.</p>
     * @exception XMLDBException <p>If a storage specific exception occures.</p>
     * @see de.xplib.nexd.comm.NEXDEngineI#queryVCLSchema(
     *      _de.xplib.nexd.api.VirtualCollection)
     */
    public VCLSchema queryVCLSchema(final VirtualCollection collIn)
            throws XMLDBException {

        if (collIn == null) {
            throw new IllegalArgumentException(
                    "The paramtere collIn cannot be null");
        }
        if (!(collIn instanceof VirtualCollectionImpl)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        StorageCollectionI scoll = this
                .getStorageCollection((VirtualCollectionImpl) collIn);

        String path = scoll.getPath() + "/" + StorageI.VCL_SCHEMA_COLLECTION;

        return VCLHelper.getVCLSchema(this.queryDocument(path,
                StorageI.VCL_SCHEMA_RESOURCE));
    }

    /**
     * <p>Returns the {@link PCVResource} for the given <code>idIn</code> in
     * the {@link VirtualCollection} <code>collIn</code>.</p>
     *
     * @param collIn <p>The context <code>VirtualCollection</code>.</p>
     * @param idIn <p>The id of the {@link _de.xplib.nexd.api.VirtualResource}
     *             that wants to retrieve the {@link PCVResource}.</p>
     * @return <p>The {@link PCVResource} instance.</p>
     * @exception XMLDBException <p>If a storage specific exception occures.</p>
     * @see de.xplib.nexd.comm.NEXDEngineI#queryPCVResource(
     *      _de.xplib.nexd.api.VirtualCollection, java.lang.String)
     */
    public PCVResource queryPCVResource(final VirtualCollection collIn,
            final String idIn) throws XMLDBException {

        if (collIn == null) {
            throw new IllegalArgumentException(
                    "The parameters collIn and idIn cannot be null");
        }
        if (!(collIn instanceof VirtualCollectionImpl)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }

        String path = this.getStorageCollection((SixdmlCollection) collIn)
                .getPath()
                + "/" + StorageI.PCVR_DATA_COLLECTION;

        log.debug("getPCVResource");

        return AbstractPCVRFactory.newInstance().newPCVResource(collIn, idIn,
                this.queryDocument(path, idIn));
    }

    /**
     * Helper method to extract a <code>StorageCollectionI</code> instance from
     * the given <code>SixdmlCollection</code>.
     *
     * @param collIn The <code>SixdmlCollection</code> object.
     * @return The matching <code>StorageCollectionI</code> object.
     * @throws XMLDBException If a database specific exception occures.
     */
    protected StorageCollectionI getStorageCollection(
            final SixdmlCollection collIn) throws XMLDBException {

        if (!(collIn instanceof AbstractCollection)) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION, "" + collIn);
        }

        return ((AbstractCollection) collIn).getStorageCollection();
    }

    /**
     * Returns a <code>Document</code> from the database.
     *
     * @param path The collection path.
     * @param idIn The resource id.
     * @return The xml document.
     * @throws XMLDBException If any database specific error occures.
     */
    protected Document queryDocument(final String path, final String idIn)
            throws XMLDBException {

        try {
            StorageCollectionI sColl = this.storage.queryCollection(path);

            Document doc = ((StorageDocumentObjectI) this.storage.queryObject(
                    sColl, idIn)).getContent();

            return doc;
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Creates a <code>SixdmlResourceSet</code> from an arry with
     * <code>StorageObjectI</code> objects.
     *
     * @param collIn The associated <code>SixdmlCollection</code> object.
     * @param resources An array with <code>StorageObjectI</code> objects.
     * @return A resource set with all given objects as resources.
     * @throws XMLDBException If any database specific error occures.
     */
    protected SixdmlResourceSet buildQueryResourceSet(
            final SixdmlCollection collIn, final StorageObjectI[] resources)
            throws XMLDBException {

        SixdmlResourceSet resset = new SixdmlResourceSet();
        for (int i = 0; i < resources.length; i++) {

            SixdmlResource res = this
                    .createSixdmlResource(collIn, resources[i]);

            resset.addResource(res);
        }

        return resset;
    }

    /**
     * Factory method that creates a <code>SixdmlResource</code> from a
     * <code>StorageObjectI</code> instance.
     *
     * @param collIn The associated <code>SixdmlCollection</code> object.
     * @param resIn The storage resource object.
     * @return The xmldb resource instance.
     * @throws XMLDBException If any database specific error occures.
     */
    protected SixdmlResource createSixdmlResource(
            final SixdmlCollection collIn, final StorageObjectI resIn)
            throws XMLDBException {

        SixdmlResource res;
        if (collIn instanceof VirtualCollectionImpl) {
            res = new VirtualResourceImpl((VirtualCollectionImpl) collIn,
                    ((StorageDocumentObjectI) resIn).getContent(), resIn
                            .getOID());
        } else {
            res = new XMLResourceImpl((CollectionImpl) collIn, resIn.getOID());
            res.setContentAsDOM(((StorageDocumentObjectI) resIn).getContent());
        }
        return res;
    }

    /**
     * This method checks if a name contains the magic character <b>$</b>. This
     * symbol is used for internal storages and caches.
     *
     * @param nameIn The name to check.
     * @throws XMLDBException If the given <code>nameIn</code> contains the
     *                        character <b>$</b>.
     */
    protected void checkMagicChar(final String nameIn) throws XMLDBException {
        if (nameIn.indexOf('$') != -1) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR,
                    "A name cannot contain the character '$'.");
        }
    }
}