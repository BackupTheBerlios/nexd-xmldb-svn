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
 * $Log: StorageImpl.java,v $
 * Revision 1.4  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.3  2005/05/11 18:00:12  nexd
 * Minor changes and corrections.
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
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
 * Revision 1.7  2005/03/01 10:26:31  nexd
 * Bugfix for same record in internalQueryDocumentRange()
 *
 */
package de.xplib.nexd.engine.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;
import de.xplib.nexd.store.AbstractStorageFactory;
import de.xplib.nexd.store.InternalIdI;
import de.xplib.nexd.store.StorageCollectionI;
import de.xplib.nexd.store.StorageDocumentObjectI;
import de.xplib.nexd.store.StorageException;
import de.xplib.nexd.store.StorageI;
import de.xplib.nexd.store.StorageObjectI;
import de.xplib.nexd.store.StorageValidationObjectI;
import de.xplib.nexd.util.IntStack;
import de.xplib.nexd.xml.DOMDocumentI;
import de.xplib.nexd.xml.DOMDocumentTypeI;
import de.xplib.nexd.xml.DOMNodeI;
import de.xplib.nexd.xml.DocumentCacheI;

/**
 * The database driver for the small java based database system
 * <a href="hsqldb.sf.net">hsqldb</a>.
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.4 $
 */
public class StorageImpl implements StorageI, ConstantsQueryI {

    /**
     * @label uses
     */

    /*#de.xplib.nexd.engine.store.XPathSQLBuilder Dependency_Link*/

    /**
     * An internal helper class, that blocks all write operations on the used
     * sequence table.
     */
    class NodeId {

        /**
         * The current node nid.
         */
        private int nid;

        /**
         * Empty default Constructor.
         *
         */
        NodeId() {
            super();
        }

        /**
         * Setter method for the initial node nid.
         *
         * @param initId The initial node nid.
         */
        public void init(final int initId) {
            this.nid = initId;
        }

        /**
         * Getter method for the current node nid.
         *
         * @return The current node nid.
         */
        public int current() {
            return this.nid;
        }

        /**
         * Increments the node nid and returns the new value.
         *
         * @return The incremented node nid.
         */
        public int next() {
            return ++this.nid;
        }
    }

    /**
     * The used system logger.
     */
    private static final Log LOG = LogFactory.getLog(StorageImpl.class);

    /**
     * The JDBC database connection.
     */
    private Connection conn = null;

    /**
     * The factory that creates this object.
     * @clientCardinality 1
     * @clientRole factory
     * @directed true
     * @supplierCardinality 1
     */
    private final StorageFactoryImpl factory;

    /**
     * The JDBC database driver, that is used for the storage.
     */
    private final String jdbcDriver;

    /**
     * The JDBC url, that identifies the database.
     */
    private final String jdbcUrl;

    /**
     * Comment for <code>nodeId</code>
     */
    private final NodeId nodeId = new NodeId();

    /**
     * Constructor.
     *
     * @param factoryIn The <code>AbstractStorageFactory</code> that creates
     *                  this object
     * @param jdbcDriverIn The database driver.
     * @param jdbcUrlIn The database jdbc url.
     * @exception StorageException If the jdbc driver cannot be found.
     */
    protected StorageImpl(final StorageFactoryImpl factoryIn,
            final String jdbcDriverIn, final String jdbcUrlIn)
            throws StorageException {

        super();

        try {
            Class.forName(jdbcDriverIn);
        } catch (ClassNotFoundException e) {
            throw new StorageException(e.getMessage());
        }

        this.factory = factoryIn;
        this.jdbcDriver = jdbcDriverIn;
        this.jdbcUrl = jdbcUrlIn;
        
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");
    }

    /**
     * Returns the factory that creates the concrete <code>StorageI</code>
     * implementation.
     *
     * @return The <code>AbstractStorageFactory</code> that creates and returns
     *         this instance of <code>StorageI</code>.
     * @see de.xplib.nexd.store.StorageI#getFactory()
     */
    public AbstractStorageFactory getFactory() {
        return this.factory;
    }

    /**
     * @param username The username used for authentication.
     * @param password The passord used for authentication.
     * @exception StorageException If something goes wrong during connection.
     * @see StorageI#open(String, String)
     */
    public void open(final String username, final String password)
            throws StorageException {

        try {
            this.conn = DriverManager.getConnection(this.jdbcUrl, username,
                    password);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    /**
     * Checks if an id is already used to identify a stored object.
     *
     * @param collIn The context <code>StorageCollectionImpl</code>.
     * @param idIn The id to look foor
     * @return <code>true</code> if the nid is already used, otherwise
     *         <code>false</code>.
     * @throws StorageException ..
     * @see de.xplib.nexd.comm.NEXDEngineI#containsId(java.lang.String)
     */
    public boolean containsId(final StorageCollectionI collIn, 
                              final String idIn)
            throws StorageException {

        PreparedStatement stmt = null;
        ResultSet rsUnique = null;
        try {

            int iid = ((CollectionIId) collIn.getInternalId()).getID();

            stmt = this.conn.prepareStatement(QUERY_CONTAINS_ID);
            stmt.setString(1, idIn);
            stmt.setInt(2, iid);

            stmt.setString(3, idIn);
            stmt.setInt(4, iid);

            rsUnique = stmt.executeQuery();

            return rsUnique.next();
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsUnique);
        }
    }

    /**
     * Stores a new <code>StorageCollectionImpl</code> in the underlying media.
     *
     * @param collPath The path which locates the
     *                 <code>StorageCollectionImpl</code> in the system.
     * @param typeIn The collection type.
     * @return A new instance of <code>StorageCollectionImpl</code>
     * @throws StorageException If the <code>collPath</code> already exists or
     *                          something <code>StorageI</code> specific goes
     *                          wrong.
     * @see StorageI#storeCollection(String)
     */
    public StorageCollectionI storeCollection(final String collPath,
            final short typeIn) throws StorageException {

        String path = this.prepareCollectionPath(collPath);

        if (this.internalQueryCollection(path) != null) {
            throw new StorageException(StorageException.COLLECTION_EXISTS,
                    new String[] {path});
        }

        String pPath = path.substring(0, path.lastIndexOf('/'));
        Map parent = this.internalQueryCollection(pPath);
        if (parent == null) {
            throw new StorageException(StorageException.NO_SUCH_COLLECTION,
                    new String[] {pPath});
        }

        int iid = this.internalQuerySequenceNumber(TABLE_COLLECTION);
        int pid = ((Integer) parent.get(FIELD_ID)).intValue();
        String name = path.substring(path.lastIndexOf('/') + 1);

        PreparedStatement stmt = null;
        try {

            stmt = this.conn.prepareStatement(QUERY_INSERT_COLL);
            stmt.setInt(1, iid);
            stmt.setInt(2, pid);
            stmt.setShort(3, typeIn);
            stmt.setString(4, name);

            if (stmt.executeUpdate() != 1) {
                throw new StorageException(StorageException.UNKNOWN);
            }

            Map map = this.internalQueryCollection(path);

            return new StorageCollectionImpl(path,
                    StorageCollectionImpl.TYPE_COLLECTION, new CollectionIId(
                            ((Integer) map.get(FIELD_ID)).intValue()), map);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new StorageException(e.getMessage());
            }
        }
    }

    /**
     * Removes a <code>StorageCollectionImpl</code> in the underlying media.
     *
     * @param collPath The path which locates the
     *                 <code>StorageCollectionImpl</code> in the system.
     * @throws StorageException If the <code>collPath</code> doesn't exists or
     *                          something <code>StorageI</code> specific goes
     *                          wrong.
     * @see de.xplib.nexd.store.StorageI#dropCollection(java.lang.String)
     */
    public void dropCollection(final String collPath) throws StorageException {

        String path = this.prepareCollectionPath(collPath);

        Map coll = this.internalQueryCollection(path);
        if (coll == null) {
            throw new StorageException(StorageException.NO_SUCH_COLLECTION,
                    new String[] {path});
        }

        int iid = ((Integer) coll.get(FIELD_ID)).intValue();

        String delete = "DELETE FROM " + TABLE_COLLECTION + " WHERE id=" + iid;

        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            if (stmt.executeUpdate(delete) != 1) {
                throw new StorageException(StorageException.UNKNOWN);
            }
        } catch (SQLException e) {
            throw new StorageException(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new StorageException(e.getMessage());
            }
        }
    }

    /**
     * Selects a <code>StorageCollectionImpl</code> or returns <code>null</code>
     * , if <code>collPath</code> doesn't locate anything.
     *
     * @param collPath Returns the database collection identified by the
     *                 specified <code>collPath</code>.
     * @return The <code>StorageCollectionImpl</code> or <code>null</code> if no
     *         collection matches the path.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     * @see de.xplib.nexd.store.StorageI#queryCollection(java.lang.String)
     */
    public StorageCollectionI queryCollection(final String collPath)
            throws StorageException {

        String path = this.prepareCollectionPath(collPath);

        StorageCollectionI cont = null;

        Map map = this.internalQueryCollection(path);
        if (map != null) {
            cont = new StorageCollectionImpl(
                    path,
                    ((Short) map.get(FIELD_TYPE)).shortValue(),
                    new CollectionIId(((Integer) map.get(FIELD_ID)).intValue()),
                    map);
        }
        return cont;
    }

    /**
     * Returns the number of child collection that are located under
     * <code>collIn</code>.
     *
     * @param collIn The context <code>StorageCollectionImpl</code>.
     * @return The number of child collections.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    public int queryCollectionCount(final StorageCollectionI collIn)
            throws StorageException {

        PreparedStatement stmt = null;
        ResultSet rsCount = null;
        try {
            stmt = this.conn.prepareStatement(QUERY_COLL_COUNT);

            int iid = ((CollectionIId) collIn.getInternalId()).getID();
            stmt.setInt(1, iid);

            rsCount = stmt.executeQuery();

            int count = 0;
            if (rsCount.next()) {
                count = rsCount.getInt(FIELD_COUNT);
            }
            return count;
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsCount);
        }
    }

    /**
     * Returns an array with all names of collections that are located under
     * <code>collIn</code>.
     *
     * @param collIn The context <code>StorageCollectionImpl</code>.
     * @return An array with all names.
     * @exception StorageException If something <code>StorageI</code> specific
     *                             goes wrong.
     * @see de.xplib.nexd.store.StorageI#queryCollections(
     *      StorageCollectionI)
     */
    public String[] queryCollections(final StorageCollectionI collIn)
            throws StorageException {

        PreparedStatement stmt = null;
        ResultSet rsNames = null;
        try {
            stmt = this.conn.prepareStatement(QUERY_CHILD_COLLS);

            int iid = ((CollectionIId) collIn.getInternalId()).getID();
            stmt.setInt(1, iid);

            FastArrayList list = new FastArrayList();
            list.setFast(true);

            rsNames = stmt.executeQuery();
            while (rsNames.next()) {
                list.add(rsNames.getString(FIELD_NAME));
            }
            return (String[]) list.toArray(new String[list.size()]);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsNames);
        }
    }

    /**
     * Stores a XML-Schema or a DTD for the given
     * <code>StorageCollectionImpl</code> object.
     *
     * @param collIn The context <code>StorageCollectionImpl</code> instance.
     * @param schemaIn The <code>StorageValidationObjectImpl</code> instance to
     *                 store or update.
     * @exception StorageException If something <code>StorageI</code> specific
     *                             goes wrong.
     * @see de.xplib.nexd.store.StorageI#storeValidationObject(
     *      StorageCollectionI,
     *      StorageValidationObjectI)
     */
    public void storeValidationObject(final StorageCollectionI collIn,
            final StorageValidationObjectI schemaIn) throws StorageException {

        int cid = ((CollectionIId) collIn.getInternalId()).getID();

        String sql;
        if (this.queryValidationObject(collIn) == null) {
            sql = "INSERT INTO " + TABLE_SCHEMA
                    + "  (cid, type, schema) VALUES (" + cid + ", ?, ?)";
        } else {
            sql = "UPDATE " + TABLE_SCHEMA + " SET "
                    + "type=?, schema=? WHERE cid=" + cid;
        }

        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(sql);
            stmt.setByte(1, schemaIn.getType());
            stmt.setString(2, schemaIn.getContent());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
    }

    /**
     * Remove a <code>StorageValidationObjectImpl</code> from the given
     * <code>StorageCollectionImpl</code> instance, if a schema exists.
     *
     * @param collIn The context <code>StorageCollectionImpl</code> instance.
     * @throws StorageException A database specific exception.
     * @see de.xplib.nexd.store.StorageI#dropValidationObject(
     *      StorageCollectionI)
     */
    public void dropValidationObject(final StorageCollectionI collIn)
            throws StorageException {

        String sql = "DELETE FROM " + TABLE_SCHEMA + " WHERE cid=?";
        PreparedStatement stmt = null;
        try {

            stmt = this.conn.prepareStatement(sql);

            int iid = ((CollectionIId) collIn.getInternalId()).getID();
            stmt.setInt(1, iid);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
    }

    /**
     * Returns the <code>StorageValidationObjectImpl</code> object for the given
     * <code>StorageCollectionImpl collIn</code> instance or <code>null</code>.
     *
     * @param collIn The context <code>StorageCollectionImpl</code> instance.
     * @return The <code>StorageValidationObjectImpl</code> object or
     *         <code>null</code>.
     * @exception StorageException If something <code>StorageI</code> specific
     *                             goes wrong.
     * @see de.xplib.nexd.store.StorageI#queryValidationObject(
     *      StorageCollectionI)
     */
    public StorageValidationObjectI queryValidationObject(
            final StorageCollectionI collIn) throws StorageException {

        String sql = "SELECT type,schema FROM " + TABLE_SCHEMA + " WHERE cid=?";

        PreparedStatement stmt = null;
        ResultSet rsValidObj = null;
        try {

            stmt = this.conn.prepareStatement(sql);

            int iid = ((CollectionIId) collIn.getInternalId()).getID();
            stmt.setInt(1, iid);

            rsValidObj = stmt.executeQuery();

            StorageValidationObjectI validObject = null;
            if (rsValidObj.next()) {
                validObject = this.factory.createValidationObject(rsValidObj
                        .getByte(FIELD_TYPE), rsValidObj.getString("schema"));
            }
            return validObject;
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsValidObj);
        }
    }

    /**
     * Stores a <code>StorageObjectI</code> instance as a document.
     *
     * @param collIn ..
     * @param docIn ..
     * @throws StorageException ..
     */
    public void storeDocument(final StorageCollectionI collIn,
            final StorageDocumentObjectI docIn) throws StorageException {

        int collId = ((CollectionIId) collIn.getInternalId()).getID();
        int resId = this.internalQuerySequenceNumber(TABLE_RESOURCE);

        String sql = "INSERT INTO " + TABLE_RESOURCE + " (id, cid, type, name)"
                + " VALUES (" + resId + ", " + collId + ", " + docIn.getType()
                + ", '" + docIn.getOID() + "')";

        Statement stmt = null;
        try {
            try {
                stmt = this.conn.createStatement();
                //stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
                stmt.execute("SET AUTOCOMMIT FALSE");
                stmt.executeUpdate(sql);

                DOMDocumentI doc = (DOMDocumentI) docIn;

                this.internalStoreDocument(doc, resId);

            } catch (Exception e) {
                conn.rollback();
                // CHANGE stmt.execute("ROLLBACK");
                stmt.execute("SET AUTOCOMMIT TRUE");
                //stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
                throw new StorageException(e.getMessage(), e);
            }
            conn.commit();
            // CHANGE stmt.execute("COMMIT");
            stmt.execute("SET AUTOCOMMIT TRUE");
            //stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
        } catch (SQLException e) {
            try {
                conn.rollback();
                // CHANGE stmt.execute("ROLLBACK");
                stmt.execute("SET AUTOCOMMIT TRUE");
            } catch (SQLException e1) {
                throw new StorageException(e1.getMessage(), e1);
            }
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
    }

    /**
     * Updates a stored <code>DocumentImpl</code> instance.
     *
     * @param collIn The context <code>StorageCollectionImpl</code> instance.
     * @param objIn The <code>DocumentImpl</code> instance to update.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     * @see de.xplib.nexd.store.StorageI#updateDocument(
     *      StorageCollectionI,
     *      StorageDocumentObjectI)
     */
    public void updateDocument(final StorageCollectionI collIn,
            final StorageDocumentObjectI objIn) throws StorageException {

        int cid = ((CollectionIId) collIn.getInternalId()).getID();
        ResultSet rsObjInfo = this.internalQueryObjectInfo(cid, objIn.getOID());

        int rid = 0;
        try {
            rid = rsObjInfo.getInt(FIELD_ID);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage());
        }

        if (objIn.getInternalId() == null) {
            LOG.error("Error: Update full document");
            throw new StorageException("Unknown internal id for " + objIn);
        }

        DocumentCacheI cache = ((DOMDocumentI) objIn.getContent()).getCache();

        Statement stmt = null;
        try {
            try {

                synchronized (this.nodeId) {
                    stmt = this.conn.createStatement();
                    stmt.execute("SET AUTOCOMMIT FALSE");

                    int seq = this.internalQuerySequenceNumber(TABLE_DOM_NODE);
                    this.nodeId.init(seq - 1);

                    NodeList list = cache.getChanged();
                    for (int i = 0, size = list.getLength(); i < size; i++) {
                        this.internalUpdateSingleNode((DOMNodeI) list.item(i));
                    }

                    list = cache.getDeleted();
                    for (int i = 0, size = list.getLength(); i < size; i++) {

                        DOMNodeI node = (DOMNodeI) list.item(i);

                        NestedSetIId iid = (NestedSetIId) node.getInternalId();
                        int lft = iid.getLft();
                        int rgt = iid.getRgt();

                        int udt = (int) Math.round((float) (rgt - lft) / 2) * 2;

                        String delete = "DELETE FROM nexd_dom_node WHERE rid="
                                + rid + " AND lft BETWEEN " + lft + " AND "
                                + rgt + ";";

                        String update1 = "UPDATE nexd_dom_node SET lft=lft-"
                                + udt + " WHERE rid=" + rid + " AND lft>" + rgt
                                + ";";
                        String update2 = "UPDATE nexd_dom_node SET rgt=rgt-"
                                + udt + " WHERE rid=" + rid + " AND rgt>" + rgt
                                + ";";

                        stmt.executeUpdate(delete);
                        stmt.executeUpdate(update1);
                        stmt.executeUpdate(update2);

                    }

                    list = cache.getInserted();
                    for (int i = 0, size = list.getLength(); i < size; i++) {

                        boolean first = true;

                        DOMNodeI node = (DOMNodeI) list.item(i);
                        DOMNodeI parent = null;
                        if (node instanceof Attr) {
                            parent = (DOMNodeI) ((Attr) node).getOwnerElement();
                        } else if (node.getPreviousSibling() == null) {
                            parent = (DOMNodeI) node.getParentNode();
                        } else {
                            parent = (DOMNodeI) node.getPreviousSibling();
                            first = false;
                        }

                        NestedSetIId iid = (NestedSetIId) parent
                                .getInternalId();

                        int lft = iid.getLft();
                        int rgt = iid.getRgt();
                        int cnt = (1 + this.countChildNodes(node)) * 2;

                        int cmpLft;
                        int cmpRgt;
                        int currLft;
                        if (first) {
                            cmpLft = lft;
                            cmpRgt = lft;
                            currLft = lft + 1;
                        } else {
                            cmpLft = rgt;
                            cmpRgt = rgt;
                            currLft = rgt + 1;
                        }

                        // This is the attribute case {{{
                        String update1 = "UPDATE nexd_dom_node SET lft=lft+"
                                + cnt + " WHERE rid=" + rid + " AND lft>"
                                + cmpLft + ";";
                        String update2 = "UPDATE nexd_dom_node SET rgt=rgt+"
                                + cnt + " WHERE rid=" + rid + " AND rgt>"
                                + cmpRgt + ";";

                        stmt.executeUpdate(update1);
                        stmt.executeUpdate(update2);

                        this.internalStoreNode(node, rid, currLft);
                        this.internalUpdateSequenceNumber(TABLE_DOM_NODE,
                                (cnt / 2));
                        // }}}
                    }
                }

                cache.clear();

            } catch (Exception e) {
                stmt.execute("ROLLBACK");
                stmt.execute("SET AUTOCOMMIT TRUE");
                e.printStackTrace();
                throw new StorageException(e.getMessage(), e);
            }

            stmt.execute("COMMIT");
            stmt.execute("SET AUTOCOMMIT TRUE");
        } catch (SQLException e) {
            try {
                stmt.execute("ROLLBACK");
                stmt.execute("SET AUTOCOMMIT TRUE");
            } catch (SQLException e1) {
                throw new StorageException(e1.getMessage(), e1);
            }
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
    }

    /**
     * Removes a <code>StorageObjectI</code> object from the
     * <code>StorageCollectionImpl collIn</code> instance.
     *
     * @param collIn The context <code>StorageCollectionImpl</code> object.
     * @param resIn The <code>StorageObjectI</code> to delete.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     * @see de.xplib.nexd.store.StorageI#dropObject(
     *      StorageCollectionI,
     *      de.xplib.nexd.store.StorageObjectI)
     */
    public void dropObject(final StorageCollectionI collIn,
            final StorageObjectI resIn) throws StorageException {

        String sql = "DELETE FROM " + TABLE_RESOURCE + " as res "
                + "WHERE res.name=? AND res.cid=?";

        PreparedStatement stmt = null;
        try {

            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, resIn.getOID());

            int iid = ((CollectionIId) collIn.getInternalId()).getID();
            stmt.setInt(2, iid);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }

    }

    /**
     * Returns a <code>StorageObjectI</code> instance identified by the
     * parameter <code>idIn</code> or if there is no entry for <code>idIn</code>
     * it returns <code>null</code>.
     *
     * @param collIn The associated parent <code>StorageCollectionImpl</code>.
     * @param idIn The unique id for the <code>StorageObjectI</code>.
     * @return The <code>StorageObjectI</code> instance of <code> null</code>.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     * @see de.xplib.nexd.store.StorageI#queryObject(
     *      StorageCollectionI, java.lang.String)
     */
    public StorageObjectI queryObject(final StorageCollectionI collIn,
            final String idIn) throws StorageException {

        int collId = ((CollectionIId) collIn.getInternalId()).getID();

        String sqlR = "SELECT * FROM " + TABLE_RESOURCE + " WHERE cid="
                + collId + " AND name='" + idIn + "'";

        Statement stmt = null;
        ResultSet rsObj = null;
        try {
            stmt = this.conn.createStatement();
            rsObj = stmt.executeQuery(sqlR);

            StorageDocumentObjectI obj = null;
            if (rsObj.next()) {
                int type = rsObj.getInt(FIELD_TYPE);
                if (type == StorageObjectI.BINARY) {
                    LOG.info("Implement binary objects");
                } else if (type == StorageObjectI.XML) {
                    DocumentObjectI doc = this.internalQueryDocument(rsObj
                            .getInt(FIELD_ID));

                    doc.setResourceId(rsObj.getString(FIELD_NAME));

                    obj = doc;
                }
            }
            return obj;
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsObj);
        }
    }

    /**
     * Returns the number of <code>StorageObjectI</code> objects that are
     * stored in the <code>StorageCollectionImpl</code> identified by
     * <code>collIn</code>.
     *
     * @param collIn The context <code>StorageCollectionImpl</code> object.
     * @return The number of stored <code>StorageObjectI</code> objects.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     * @see de.xplib.nexd.store.StorageI#queryObjectCount(
     *      StorageCollectionI)
     */
    public int queryObjectCount(final StorageCollectionI collIn)
            throws StorageException {

        int iid = ((CollectionIId) collIn.getInternalId()).getID();

        String sql = "SELECT COUNT(*) AS cnt FROM " + TABLE_RESOURCE
                + " AS res " + "WHERE res.cid=" + iid;

        Statement stmt = null;
        ResultSet rsCount = null;
        try {
            stmt = this.conn.createStatement();
            rsCount = stmt.executeQuery(sql);

            int count = 0;
            if (rsCount.next()) {
                count = rsCount.getInt("cnt");
            }
            return count;
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsCount);
        }
    }

    /**
     * Returns an array with the names of all <code>StorageObjectI</code>
     * objects stored in the <code>StorageCollectionImpl</code>
     * <code>collIn</code>.
     *
     * @param collIn The context <code>StorageCollectionImpl</code> object.
     * @return An array with all names.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     * @see de.xplib.nexd.store.StorageI#queryObjects(
     *      StorageCollectionI)
     */
    public String[] queryObjects(final StorageCollectionI collIn)
            throws StorageException {

        int iid = ((CollectionIId) collIn.getInternalId()).getID();

        String sql = "SELECT res.name FROM " + TABLE_RESOURCE + " AS res "
                + "WHERE res.cid=" + iid;

        Statement stmt = null;
        ResultSet rsNames = null;
        try {
            stmt = this.conn.createStatement();
            rsNames = stmt.executeQuery(sql);

            FastArrayList list = new FastArrayList();
            list.setFast(true);

            while (rsNames.next()) {
                list.add(rsNames.getString(FIELD_NAME));
            }

            return (String[]) list.toArray(new String[list.size()]);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsNames);
        }
    }

    /**
     * Executes an XPath query against all <code>SixdmlResource</code> instances
     * in the given <code>StorageCollectionImpl</code> object.
     *
     * @param collIn The context <code>StorageCollectionImpl</code> instance.
     * @param queryIn The xpath expression to execute.
     * @return All found <code>StorageObjectI</code> object.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     * @see de.xplib.nexd.store.StorageI#queryObjectsByXPath(
     *      StorageCollectionI, java.lang.String)
     */
    public StorageObjectI[] queryObjectsByXPath(
            final StorageCollectionI collIn, final String queryIn)
            throws StorageException {

        int iid = ((CollectionIId) collIn.getInternalId()).getID();
        XPathSQLBuilder builder = new XPathSQLBuilder(queryIn, iid, true);

        return this.internalQueryResourcesByXPath(builder);
    }

    /**
     * Executes an XPath query against a single <code>SixdmlResource</code>
     * instances, identified by <code>idIn</code> in the given
     * <code>StorageCollectionImpl</code> object.
     *
     * @param collIn The context <code>StorageCollectionImpl</code> instance.
     * @param idIn The <code>StorageObjectI</code> identifier.
     * @param queryIn The xpath expression to execute.
     * @return All found <code>StorageObjectI</code> object.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     * @see de.xplib.nexd.store.StorageI#queryObjectByXPath(
     *      StorageCollectionI,
     *      java.lang.String, java.lang.String)
     */
    public StorageObjectI[] queryObjectByXPath(final StorageCollectionI collIn,
            final String idIn, final String queryIn) throws StorageException {

        int iid = ((CollectionIId) collIn.getInternalId()).getID();
        ResultSet rsObjInfo = this.internalQueryObjectInfo(iid, idIn);

        try {
            // init an empty result array
            StorageObjectI[] result = new StorageObjectI[0];

            // OK, we have a match now execute xpath query
            if (rsObjInfo != null) {
                XPathSQLBuilder builder = new XPathSQLBuilder(queryIn,
                        rsObjInfo.getInt(FIELD_ID), false);
                result = this.internalQueryResourcesByXPath(builder);
            }
            return result;
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(rsObjInfo);
        }

    }

    /**
     * <p>Stores the reference between <code>collIn</code> and
     * <code>vcollIn</code>. These refrences are used for the update process of
     * a {@link _de.xplib.nexd.api.VirtualCollection}</code>
     *
     * @param collIn <p>The <code>StorageCollectionImpl</code> for a
     *               {@link org.sixdml.dbmanagement.SixdmlCollection}.</p>
     * @param vcollIn <p>The <code>StorageCollectionImpl</code> for a
     *                {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     * @exception StorageException <p>If something <code>StorageI</code>
     *                             specific goes wrong.</p>
     * @see de.xplib.nexd.store.StorageI#storeCollectionReference(
     *      StorageCollectionI,
     *      StorageCollectionI)
     */
    public void storeCollectionReference(final StorageCollectionI collIn,
            final StorageCollectionI vcollIn) throws StorageException {

        Statement stmt = null;
        try {

            stmt = this.conn.createStatement();

            String sql = "INSERT INTO " + TABLE_COLLECTION_REF
                    + " (cid, vcid, path) VALUES ("
                    + collIn.getInternalId().export() + ", "
                    + vcollIn.getInternalId().export() + ", '"
                    + vcollIn.getPath() + "')";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage());
        } finally {
            this.close(stmt);
        }
    }

    /**
     * <p>Deletes all reference entries that exist for the given
     * <code>vcollIn</code> which is a
     * {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     *
     * @param vcollIn <p>The context <code>StorageCollectionImpl</code>.</p>
     * @exception StorageException <p>If something <code>StorageI</code>
     *                             specific goes wrong.</p>
     * @see de.xplib.nexd.store.StorageI#dropCollectionReference(
     *      StorageCollectionI)
     */
    public void dropCollectionReference(final StorageCollectionI vcollIn)
            throws StorageException {

        Statement stmt = null;

        try {
            stmt = this.conn.createStatement();

            String sql = "DELETE FROM " + TABLE_COLLECTION_REF + " WHERE "
                    + "vcid=" + vcollIn.getInternalId();

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage());
        } finally {
            this.close(stmt);
        }
    }

    /**
     * <p>Returns all <code>StorageCollectionImpl</code> instances that
     * represent a <code>Collection</code> which are refrenced by the given
     * <code>collIn</code>, this is the physical collection for a
     * {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     *
     * @param collIn <p>The context <code>StorageCollectionImpl</code>.</p>
     * @return <p>An array with all refrenced.</p>
     * @exception StorageException <p>If something <code>StorageI</code>
     *                             specific goes wrong.</p>
     * @see de.xplib.nexd.store.StorageI#queryCollectionReferences(
     *      StorageCollectionI)
     */
    public StorageCollectionI[] queryCollectionReferences(
            final StorageCollectionI collIn) throws StorageException {

        Statement stmt = null;
        ResultSet rsRef = null;

        try {
            stmt = this.conn.createStatement();

            String sql = "SELECT"
                    + "  coll.id, coll.pid, coll.name, coll.type, cref.path "
                    + "FROM" + "  " + TABLE_COLLECTION + " AS coll," + "  "
                    + TABLE_COLLECTION_REF + " AS cref " + "WHERE"
                    + "  cref.cid=" + collIn.getInternalId().export()
                    + " AND coll.id=cref.vcid";

            FastArrayList list = new FastArrayList();
            list.setFast(true);

            rsRef = stmt.executeQuery(sql);
            while (rsRef.next()) {
                list.add(this.createContainer(rsRef));
            }

            return (StorageCollectionImpl[]) list
                    .toArray(new StorageCollectionImpl[list.size()]);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage());
        } finally {
            this.close(stmt, rsRef);
        }
    }

    /**
     * <Some description here>
     *
     * @throws Throwable
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable {
        this.conn.close();
        super.finalize();
    }

    /**
     * This method removes unnessary and confusing slashes from the collection
     * path.
     *
     * @param collPathIn The collection path to prepare
     * @return The prepared collection path.
     */
    private String prepareCollectionPath(final String collPathIn) {

        String collPath = collPathIn;
        if (!collPath.startsWith("/")) {
            collPath = "/" + collPath;
        }
        if (!collPath.startsWith("/db")) {
            collPath = "/db" + collPath;
        }

        while (collPath.indexOf("//") != -1) {
            collPath = collPath.replaceAll("//", "/");
        }

        if (collPath.endsWith("/")) {
            collPath = collPath.substring(0, collPath.length() - 1);
        }

        return collPath;
    }

    /**
     * @param tableIn The table name which identifies the sequence row.
     * @return An unique number for the given <code>tableIn</code> param.
     * @exception StorageException If an internal <code>SQLException</code>
     *                             occures or the database setup is broken.
     */
    private synchronized int internalQuerySequenceNumber(final String tableIn)
            throws StorageException {

        this.internalUpdateSequenceNumber(tableIn, 1);

        String select = "SELECT id FROM " + TABLE_SEQUENCE + " WHERE name='"
                + tableIn + "'";

        Statement stmt = null;
        ResultSet rsSeqNr = null;
        try {
            stmt = this.conn.createStatement();
            rsSeqNr = stmt.executeQuery(select);
            if (!rsSeqNr.next()) {
                throw new StorageException(
                        "Cannot increment internal counter.");
            }
            return rsSeqNr.getInt(FIELD_ID);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsSeqNr);
        }
    }

    /**
     * Increments the internal sequence table row identified by
     * <code>tableIn</code> with <code>incIn</code>.
     *
     * @param tableIn The sequence identifier
     * @param incIn The number of increments.
     * @throws StorageException If something storage specific happens.
     */
    private synchronized void internalUpdateSequenceNumber(
            final String tableIn, final int incIn) throws StorageException {

        String update = "UPDATE " + TABLE_SEQUENCE + " SET id=id+" + incIn
                + " WHERE name='" + tableIn + "'";

        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            if (stmt.executeUpdate(update) != 1) {
                throw new StorageException(
                        "Cannot increment internal counter.");
            }
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
    }

    /**
     * Executes a query against the database backend and returns all nessary
     * information about the collection.
     *
     * @param collPath The path where the collection is located.
     * @return An <code>Map</code> with the following properties:<ul>
     *         <li>id (Integer): The unique internal id of the collection</li>
     *         <li>pid (Integer): The internal id of the parent collection</li>
     *         <li>type (Short): The type of the collection (virtual or normal)
     *         </li>
     *         <li>name (String): The human readable name of the collection</li>
     *         </ul>
     *         Or this method returns null.
     * @throws StorageException If something goes wrong in the database backend.
     */
    private Map internalQueryCollection(final String collPath)
            throws StorageException {

        String[] elems = collPath.split("/");

        String alias = "";
        String where = "\nWHERE\n";

        String ltbl = "";
        for (int i = 1; i < elems.length; i++) {

            if (elems[i].equals("")) {
                continue;
            }

            String tbl = "coll_" + i;
            alias += "  " + StorageImpl.TABLE_COLLECTION + " AS " + tbl + ",\n";
            where += "  " + tbl + ".name='" + elems[i] + "' AND";
            if (i > 1) {
                where += " " + tbl + ".pid=" + ltbl + ".id AND";
            }
            where += "\n";

            ltbl = tbl;
        }
        String sql = "SELECT " + ltbl + ".* FROM\n";

        alias = alias.substring(0, alias.length() - 2);
        where = where.substring(0, where.length() - 5);

        sql += alias + where;

        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rsCont = stmt.executeQuery(sql);

            try {
                Map map = null;
                if (rsCont.next()) {
                    map = new HashMap();
                    map.put(FIELD_ID, new Integer(rsCont.getInt(FIELD_ID)));
                    map.put(FIELD_PID, new Integer(rsCont.getInt(FIELD_PID)));
                    map.put(FIELD_TYPE, new Short(rsCont.getShort(FIELD_TYPE)));
                    map.put(FIELD_NAME, rsCont.getString(FIELD_NAME));
                }
                return map;
            } finally {
                this.close(stmt, rsCont);
            }
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    /**
     * Updates the values of a single node. This can be the value of a
     * <code>Text</node> or <code>Comment</code> node, the name of an
     * <code>Element</code> node or the value/name or an <code>Attr</code> node.
     *
     * @param nodeIn The <code>DOMNodeI</code> object to update.
     * @throws StorageException If the given <code>DOMNodeI</code> doesn't
     *                          exist in the database, or it is a type that
     *                          isn't supported.
     */
    private void internalUpdateSingleNode(final DOMNodeI nodeIn)
            throws StorageException {

        NestedSetIId nid = (NestedSetIId) nodeIn.getInternalId();

        if (nid == null || nid.getId() <= 0) {
            throw new StorageException(StorageException.NOT_EXISTING_NODE,
                    new String[] { 
                    nodeIn.getNodeName() });
        }
        //String[] ids = iid.split(":");
        String[] fields;

        String sql = "UPDATE ";

        switch (nodeIn.getNodeType()) {

        case Node.ELEMENT_NODE:
            sql += TABLE_DOM_ELEM + " SET name=?";
            fields = new String[] { 
                    nodeIn.getNodeName() };
            break;

        case Node.ATTRIBUTE_NODE:
            sql += TABLE_DOM_ATTR + " SET name=?, value=?";
            fields = new String[] { 
                    nodeIn.getNodeName(), nodeIn.getNodeValue() };
            break;

        case Node.TEXT_NODE:
        case Node.CDATA_SECTION_NODE:
        case Node.COMMENT_NODE:
            sql += TABLE_DOM_TEXT + " SET value=?";
            fields = new String[] { 
                    nodeIn.getNodeValue() };
            break;

        default:
            throw new StorageException(StorageException.UNSUPPORTED_NODE_TYPE);
        }

        sql += " WHERE nid=" + nid.getId();

        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(sql);

            for (int i = 0; i < fields.length; i++) {
                stmt.setString(i + 1, fields[i]);
            }
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
    }

    /**
     * Delegate method that selects the correct method for storing a new DOM
     * node.
     *
     * @param nodeIn The node to store.
     * @param resIdIn The associated resource identifier.
     * @param lftIn The current left value for the nested set model.
     * @return The new left value for the nested set model.
     * @throws StorageException If something <code>StorageI<code> specific
     *                          goes wrong.<br />
     *                          <b>StorageException.UNKNOWN_NODE_TYPE</b>:<br />
     *                          If the node type is not known.
     */
    private int internalStoreNode(final DOMNodeI nodeIn, final int resIdIn,
            final int lftIn) throws StorageException {

        int lft = lftIn;
        switch (nodeIn.getNodeType()) {

        case Node.ELEMENT_NODE:
            lft = this.internalStoreElement((Element) nodeIn, resIdIn, lftIn);
            break;

        case Node.ATTRIBUTE_NODE:
            lft = this.internalStoreAttr((Attr) nodeIn, resIdIn, lftIn);
            break;

        case Node.TEXT_NODE:
        case Node.CDATA_SECTION_NODE:
        case Node.COMMENT_NODE:
            lft = this
                    .internalStoreText((CharacterData) nodeIn, resIdIn, lftIn);
            break;

        case Node.ENTITY_REFERENCE_NODE:
            lft = this.internalStoreEntityRef((EntityReference) nodeIn,
                    resIdIn, lftIn);
            break;

        case Node.ENTITY_NODE:
            break;

        case Node.PROCESSING_INSTRUCTION_NODE:
            lft = this.internalStorePI((ProcessingInstruction) nodeIn, resIdIn,
                    lftIn);
            break;

        case Node.DOCUMENT_NODE:
            break;

        case Node.DOCUMENT_TYPE_NODE:
            lft = this.internalStoreDoctype((DOMDocumentTypeI) nodeIn, resIdIn,
                    lftIn);
            break;

        case Node.NOTATION_NODE:
            break;

        default:
            throw new StorageException(StorageException.UNKNOWN_NODE_TYPE);
        }

        return lft;
    }

    /**
     * Stores the base data for each node in a xml resource.
     *
     * @param lftIn The left value for the nested set model.
     * @param rgtIn The right value for the nested set model.
     * @param typeIn The nodeType.
     * @param resIdIn The associated resource id.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    private void internalStoreNode(final int lftIn, final int rgtIn,
            final short typeIn, final int resIdIn) throws StorageException {

        String sql = "INSERT INTO " + TABLE_DOM_NODE
                + "  (id, lft, rgt, type, rid) " + "VALUES" + "  ("
                + this.nodeId.next() + ", " + lftIn + ", " + rgtIn + ", "
                + typeIn + ", " + resIdIn + ");";

        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
    }

    /**
     * Stores a <code>DOMDocumentI</code> instance in the backend.
     *
     * @param docIn The <code>DOMDocumentI</code> instance.
     * @param resIdIn The associated resource nid.
     * @return The number of the last right value from the nested set model.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    private int internalStoreDocument(final DOMDocumentI docIn,
            final int resIdIn) throws StorageException {

        // synchronize this block, because node ID's must be unique
        synchronized (this.nodeId) {

            int seq = this.internalQuerySequenceNumber(TABLE_DOM_NODE);
            this.nodeId.init(seq - 1);

            int lft = 1;
            int rgt = 2;

            DOMNodeI node = (DOMNodeI) docIn.getFirstChild();
            while (node != null) {
                rgt = this.internalStoreNode(node, resIdIn, rgt);
                node = (DOMNodeI) node.getNextSibling();
            }

            this.internalStoreNode(lft, rgt, docIn.getNodeType(), resIdIn);

            String sql = "INSERT INTO "
                    + TABLE_DOM_DOCUMENT
                    + "  (nid, version, encoding, standalone)"
                    + "  VALUES (?, ?, ?, ?);";

            PreparedStatement stmt = null;
            try {
                stmt = this.conn.prepareStatement(sql);

                stmt.setEscapeProcessing(true);
                stmt.setInt(1, this.nodeId.current());
                stmt.setString(2, docIn.getVersion());
                stmt.setString(3, docIn.getEncoding());
                stmt.setBoolean(4, docIn.getStandalone());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            } finally {
                this.close(stmt);
            }

            this.internalUpdateSequenceNumber(TABLE_DOM_NODE, this.nodeId
                    .current()
                    - seq);
        }

        return 0;
    }

    /**
     * Stores an <code>Element</code> instance in the database backend.
     *
     * @param elemIn The <code>Element</code> instance.
     * @param resIdIn The associated resource nid.
     * @param lftIn The left value for this <code>Element</code> instance
     *              in the nested set model
     * @return The next left value for the next nested set entry.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    private int internalStoreElement(final Element elemIn, final int resIdIn,
            final int lftIn) throws StorageException {

        int rgt = lftIn + 1;

        if (elemIn.hasAttributes()) {
            NamedNodeMap nnm = elemIn.getAttributes();
            for (int i = 0, size = nnm.getLength(); i < size; i++) {
                rgt = this.internalStoreAttr((Attr) nnm.item(i), resIdIn, rgt);
            }
        }

        if (elemIn.hasChildNodes()) {
            NodeList children = elemIn.getChildNodes();
            for (int i = 0, size = children.getLength(); i < size; i++) {
                rgt = this.internalStoreNode((DOMNodeI) children.item(i),
                        resIdIn, rgt);
            }
        }

        this.internalStoreNode(lftIn, rgt, elemIn.getNodeType(), resIdIn);

        String sql = "INSERT INTO " + TABLE_DOM_ELEM
                + "  (nid, name) VALUES (?, ?)";

        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, this.nodeId.current());
            stmt.setString(2, elemIn.getTagName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }

        return rgt + 1;
    }

    /**
     * Stores a <code>Attr</code> instance in the database backend.
     *
     * @param attrIn The <code>Attr</code> instance.
     * @param oidIn The associated object identifier.
     * @param lftIn The left value for this <code>Attr</code> instance
     *              in the nested set model
     * @return The next left value for the next nested set entry.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    private int internalStoreAttr(final Attr attrIn, final int oidIn,
            final int lftIn) throws StorageException {

        this.internalStoreNode(lftIn, lftIn + 1, attrIn.getNodeType(), oidIn);

        String sql = "INSERT INTO " + TABLE_DOM_ATTR
                + "  (nid, name, value) VALUES ( ? , ? , ? )";

        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(sql);

            stmt.setEscapeProcessing(true);
            stmt.setInt(1, this.nodeId.current());
            stmt.setString(2, attrIn.getName());
            stmt.setString(3, attrIn.getValue());

            stmt.execute();
        } catch (SQLException e) {
            LOG.error(stmt);
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
        return lftIn + 2;
    }

    /**
     * This method stores an instance of <code>CharacterData</code> in
     * the database backend. An instance can be </code>Text</code>,
     * <code>Comment</code> or|and <code>CDATASection</code>.
     *
     * @param charIn The <code>AbstractCharacter</code> instance.
     * @param oidIn The associated object identifier.
     * @param lftIn The left value for this <code>CharacterData</code> instance
     *              in the nested set model
     * @return The next left value for the next nested set entry.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    private int internalStoreText(final CharacterData charIn, final int oidIn,
            final int lftIn) throws StorageException {

        this.internalStoreNode(lftIn, lftIn + 1, charIn.getNodeType(), oidIn);

        String sql = "INSERT INTO " + TABLE_DOM_TEXT
                + "  (nid, value) VALUES (?, ?)";

        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, this.nodeId.current());
            stmt.setString(2, charIn.getData());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }

        return lftIn + 2;
    }

    /**
     * Stores a <code>EntityReference</code> instance in the database
     * backend.
     *
     * @param refIn The <code>EntityReference</code> instance.
     * @param oidIn The associated object identifier.
     * @param lftIn The left value for this <code>Element</code> instance
     *              in the nested set model
     * @return The next left value for the next nested set entry.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    private int internalStoreEntityRef(final EntityReference refIn,
            final int oidIn, final int lftIn) throws StorageException {

        this.internalStoreNode(lftIn, lftIn + 1, refIn.getNodeType(), oidIn);

        String sql = "INSERT INTO " + TABLE_DOM_ENTITYREF
                + "  (nid, name) VALUES (?, ?)";

        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, this.nodeId.current());
            stmt.setString(2, refIn.getNodeName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }

        return lftIn + 2;
    }

    /**
     * Stores a <code>ProcessingInstruction</code> instance in the database
     * backend.
     *
     * @param piIn The <code>ProcessingInstruction</code> instance.
     * @param oidIn The associated object identifier.
     * @param lftIn The left value for this <code>ProcessingInstruction</code>
     *              instance in the nested set model
     * @return The next left value for the next nested set entry.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    private int internalStorePI(final ProcessingInstruction piIn,
            final int oidIn, final int lftIn) throws StorageException {

        this.internalStoreNode(lftIn, lftIn + 1, piIn.getNodeType(), oidIn);

        String sql = "INSERT INTO " + TABLE_DOM_PI
                + "  (nid, target, data) VALUES ( ? , ? , ? )";

        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(sql);

            stmt.setEscapeProcessing(true);
            stmt.setInt(1, this.nodeId.current());
            stmt.setString(2, piIn.getTarget());
            stmt.setString(3, piIn.getData());

            stmt.execute();
        } catch (SQLException e) {
            LOG.error(stmt);
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
        return lftIn + 2;
    }

    /**
     * Stores a <code>DOMDocumentTypeI</code> instance in the database
     * backend.
     *
     * @param dtdIn The <code>DOMDocumentTypeI</code> instance.
     * @param oidIn The associated object identifier.
     * @param lftIn The left value for this <code>DocumentTypeI</code>
     *              instance in the nested set model
     * @return The next left value for the next nested set entry.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    private int internalStoreDoctype(final DOMDocumentTypeI dtdIn,
            final int oidIn, final int lftIn) throws StorageException {

        this.internalStoreNode(lftIn, lftIn + 1, dtdIn.getNodeType(), oidIn);

        String sql = "INSERT INTO " + TABLE_DOM_DOCTYPE
                + "  (nid, name, publicId, systemId, subset) " + "VALUES"
                + "(?, ?, ?, ?, ?)";

        PreparedStatement stmt = null;
        try {
            stmt = this.conn.prepareStatement(sql);

            stmt.setEscapeProcessing(true);
            stmt.setInt(1, this.nodeId.current());
            stmt.setString(2, dtdIn.getName());
            stmt.setString(3, dtdIn.getPublicId());
            stmt.setString(4, dtdIn.getSystemId());
            stmt.setString(5, dtdIn.getInternalSubset());

            stmt.execute();
        } catch (SQLException e) {
            LOG.error(stmt);
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
        return lftIn + 2;
    }

    /**
     * Returns the base information of a <code>StorageObjectI</code> instance.
     * This means that <code>nid, cid, name</code> and <code>type</code> are
     * returned.
     *
     * @param collIdIn The collection nid.
     * @param nameIn The name of the resource.
     * @return A <code>ResultSet</code> containing the information.
     * @throws StorageException If something database specific goes wrong.
     */
    private ResultSet internalQueryObjectInfo(final int collIdIn,
            final String nameIn) throws StorageException {

        String sqlR = "SELECT * FROM " + TABLE_RESOURCE + " WHERE cid="
                + collIdIn + " AND name='" + nameIn + "'";

        Statement stmt = null;
        ResultSet rsObjInfo = null;
        try {
            stmt = this.conn.createStatement();
            rsObjInfo = stmt.executeQuery(sqlR);

            ResultSet retObjInfo = null;
            if (rsObjInfo.next()) {
                retObjInfo = rsObjInfo;
            } else {
                this.close(rsObjInfo);
            }
            return retObjInfo;
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt);
        }
    }

    /**
     * @param oidIn The object id of the <code>DocumentObjectI</code> object.
     * @return The <code>DocumentImpl</code> instance of <code>null</code>.
     * @throws StorageException If something <code>StorageI</code> specific goes
     *                          wrong.
     */
    private DocumentObjectI internalQueryDocument(final int oidIn)
            throws StorageException {

        String sql = "SELECT" + "  doc.version AS name, doc.encoding AS val,"
                + "  doc.standalone AS alone,NULL AS pid, NULL AS sid, n0.* "
                + "FROM nexd_dom_document AS doc LEFT JOIN"
                + "  nexd_dom_node AS n0 ON n0.id=doc.nid " + "WHERE n0.rid="
                + oidIn + " UNION SELECT"
                + "  elem.name AS name, NULL AS val, "
                + "  false AS alone, NULL AS pid, NULL AS sid, n1.* "
                + "FROM nexd_dom_element AS elem LEFT JOIN"
                + "  nexd_dom_node AS n1 ON n1.id=elem.nid " + "WHERE n1.rid="
                + oidIn + " UNION SELECT"
                + "  attr.name AS name, attr.value AS val, "
                + "  false AS alone, NULL AS pid, NULL AS sid, n2.* "
                + "FROM nexd_dom_attr AS attr LEFT JOIN"
                + "  nexd_dom_node AS n2 ON n2.id=attr.nid " + "WHERE n2.rid="
                + oidIn + " UNION SELECT"
                + "  NULL AS name, txt.value AS val, "
                + "  false AS alone, NULL AS pid, NULL AS sid, n3.* "
                + "FROM nexd_dom_text AS txt LEFT JOIN"
                + "  nexd_dom_node AS n3 ON n3.id=txt.nid " + "WHERE n3.rid="
                + oidIn + " UNION SELECT"
                + "  ref.name, NULL AS val, false AS alone, "
                + "  NULL AS pid, NULL AS sid, n4.* "
                + "FROM nexd_dom_entityref AS ref LEFT JOIN"
                + "  nexd_dom_node AS n4 ON n4.id=ref.nid " + "WHERE n4.rid="
                + oidIn + " UNION SELECT"
                + "  pi.target AS name, pi.data AS val,"
                + "  false AS alone, NULL AS pid, NULL AS sid, n5.* " + "FROM "
                + TABLE_DOM_PI + " AS pi LEFT JOIN"
                + "  nexd_dom_node AS n5 ON n5.id=pi.nid " + "WHERE n5.rid="
                + oidIn + " UNION SELECT"
                + "  dtd.name, dtd.subset AS val, false AS alone, "
                + "  dtd.publicId AS pid, dtd.systemId AS sid, n6.* " + "FROM "
                + TABLE_DOM_DOCTYPE + " AS dtd LEFT JOIN"
                + "  nexd_dom_node AS n6 ON n6.id=dtd.nid " + "WHERE n6.rid="
                + oidIn + " ORDER BY lft;";

        Statement stmt = null;
        ResultSet rsNodes = null;
        try {
            stmt = this.conn.createStatement();
            rsNodes = stmt.executeQuery(sql);

            DOMImplementation impl = new DocumentBuilderFactoryImpl()
                    .newDocumentBuilder().getDOMImplementation();
            DocumentObjectI doc = null;
            if (!rsNodes.next()
                    || rsNodes.getInt(FIELD_TYPE) != Node.DOCUMENT_NODE) {

                throw new StorageException(
                        StorageException.INVALID_STORED_DOCUMENT);
            }

            doc = (DocumentObjectI) impl.createDocument(null, null, null);
            doc.setVersion(rsNodes.getString(FIELD_NAME));
            doc.setEncoding(rsNodes.getString(FIELD_VALUE));
            doc.setStandalone(rsNodes.getBoolean("alone"));

            NestedSetIId iid = new NestedSetIId(rsNodes.getInt(FIELD_LFT),
                    rsNodes.getInt(FIELD_RGT), rsNodes.getInt(FIELD_ID));
            doc.setInternalId(iid);

            doc.startLoading();

            Node node = doc;

            IntStack stack = new IntStack(2);
            stack.push(rsNodes.getInt(FIELD_RGT));

            while (rsNodes.next()) {

                short type = rsNodes.getShort(FIELD_TYPE);
                Node tmp = null;

                iid = new NestedSetIId(rsNodes.getInt(FIELD_LFT), rsNodes
                        .getInt(FIELD_RGT), rsNodes.getInt(FIELD_ID));

                switch (type) {

                case Node.ELEMENT_NODE:
                    tmp = doc.createElement(rsNodes.getString(FIELD_NAME));
                    break;

                case Node.ATTRIBUTE_NODE:
                    if (!(node instanceof Element)) {
                        throw new StorageException(
                                StorageException.INVALID_STORED_DOCUMENT);
                    }
                    Attr attr = doc.createAttribute(rsNodes
                            .getString(FIELD_NAME));
                    attr.setValue(rsNodes.getString(FIELD_VALUE));
                    ((DOMNodeI) attr).setInternalId(iid);
                    ((Element) node).setAttributeNode(attr);
                    continue;

                case Node.TEXT_NODE:
                    tmp = doc.createTextNode(rsNodes.getString(FIELD_VALUE));
                    break;

                case Node.CDATA_SECTION_NODE:
                    tmp = doc
                            .createCDATASection(rsNodes.getString(FIELD_VALUE));
                    break;

                case Node.COMMENT_NODE:
                    tmp = doc.createComment(rsNodes.getString(FIELD_VALUE));
                    break;

                case Node.PROCESSING_INSTRUCTION_NODE:
                    tmp = doc.createProcessingInstruction(rsNodes
                            .getString(FIELD_NAME), rsNodes
                            .getString(FIELD_VALUE));
                    break;

                case Node.ENTITY_REFERENCE_NODE:
                    tmp = doc.createEntityReference(rsNodes
                            .getString(FIELD_NAME));
                    break;

                case Node.DOCUMENT_TYPE_NODE:
                    tmp = doc.getImplementation().createDocumentType(
                            rsNodes.getString(FIELD_NAME),
                            rsNodes.getString(FIELD_PID),
                            rsNodes.getString("sid"));
                    ((DOMDocumentTypeI) tmp).setInternalSubset(rsNodes
                            .getString(FIELD_VALUE));
                    ((DOMDocumentTypeI) tmp).setInternalId(iid);
                    doc.appendChild(tmp);
                    continue;

                default:
                    throw new StorageException(
                            StorageException.INVALID_STORED_DOCUMENT);
                }
                ((DOMNodeI) tmp).setInternalId(iid);
                int lft = rsNodes.getInt(FIELD_LFT);
                while (lft > stack.peek()) {
                    node = node.getParentNode();
                    stack.pop();
                }
                if (type == Node.ELEMENT_NODE) {
                    node = node.appendChild(tmp);
                    stack.push(rsNodes.getInt(FIELD_RGT));
                } else {
                    node.appendChild(tmp);
                }
            }
            doc.stopLoading();
            return doc;
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsNodes);
        }
    }

    /**
     * Executes a XPath query against the database. The <code>builderIn</code>
     * contains the information if it is a query against a single resource or
     * all resources in a collection.
     *
     * @param builderIn The query builder.
     * @return All found documents.
     * @throws StorageException If something database specific goes wrong.
     */
    private DocumentObjectI[] internalQueryResourcesByXPath(
            final XPathSQLBuilder builderIn) throws StorageException {

        Statement stmt = null;
        ResultSet rsXPath = null;
        try {
            String query = builderIn.buildSQLQuery();

            stmt = this.conn.createStatement();
            rsXPath = stmt.executeQuery(query);

            NodeTupleList ntl = new NodeTupleList();
            while (rsXPath.next()) {
                ntl.add(rsXPath.getInt(FIELD_RID), rsXPath.getInt(FIELD_LFT),
                        rsXPath.getInt(FIELD_RGT));
            }

            // TODO : Implement nested elements, means elements between 
            // previous select queries.

            return this.internalQueryDocumentRange(ntl);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsXPath);
        }
    }

    /**
     * Selects document parts by the given <code>NodeTupleList</code>.
     * The object contains the resource ids and left and right values for the
     * sql query.
     *
     * @param tuple List with values required for the query.
     * @return The found <code>DocumentImpl</code> instances.
     * @throws StorageException If something database specific goes wrong.
     */
    private DocumentObjectI[] internalQueryDocumentRange(
            final NodeTupleList tuple) throws StorageException {

        if (tuple.getLength() == 0) {
            return new DocumentObjectI[0];
        }

        StringBuilder ntBuilder = new StringBuilder();
        for (int i = 0, size = tuple.getLength(); i < size; i++) {
            ntBuilder.append("(n{#}.rid=");
            ntBuilder.append(tuple.getRid(i));
            ntBuilder.append(" AND r{#}.id=");
            ntBuilder.append(tuple.getRid(i));
            ntBuilder.append(" AND n{#}.lft>=");
            ntBuilder.append(tuple.getLft(i));
            ntBuilder.append(" AND n{#}.rgt<=");
            ntBuilder.append(tuple.getRgt(i));
            ntBuilder.append(") OR ");
        }

        // remove the last 4 chars ') OR '
        final String where = ntBuilder.substring(0, ntBuilder.length() - 4)
                .toString();

        String sql = "SELECT"
                + "  elem.name AS name, NULL AS val, r1.name AS rname, "
                + "  false AS alone, NULL AS pid, NULL AS sid, "
                + "  n1.id, n1.lft, n1.rgt, n1.type, n1.rid "
                + "FROM"
                + "  nexd_dom_element AS elem, "
                + "  nexd_resource AS r1 "
                + "LEFT JOIN"
                + "  nexd_dom_node AS n1 ON n1.id=elem.nid "
                + "WHERE ("
                + where.replaceAll("\\{#\\}", "1")
                + ") "
                + "UNION SELECT"
                + "  attr.name AS name, attr.value AS val, r2.name AS rname, "
                + "  false AS alone, NULL AS pid, NULL AS sid, "
                + "  n2.id, n2.lft, n2.rgt, n2.type, n2.rid "
                + "FROM"
                + "  nexd_dom_attr AS attr, "
                + "  nexd_resource AS r2 "
                + "LEFT JOIN"
                + "  nexd_dom_node AS n2 ON n2.id=attr.nid "
                + "WHERE ("
                + where.replaceAll("\\{#\\}", "2")
                + ") "
                + "UNION SELECT"
                + "  NULL AS name, txt.value AS val, r3.name AS rname, "
                + "  false AS alone, NULL AS pid, NULL AS sid, "
                + "  n3.id, n3.lft, n3.rgt, n3.type, n3.rid "
                + "FROM"
                + "  nexd_dom_text AS txt,"
                + "  nexd_resource AS r3 "
                + "LEFT JOIN"
                + "  nexd_dom_node AS n3 ON n3.id=txt.nid "
                + "WHERE ("
                + where.replaceAll("\\{#\\}", "3")
                + ") "
                + "UNION SELECT"
                + "  ref.name, NULL AS val, false AS alone, r4.name AS rname, "
                + "  NULL AS pid, NULL AS sid, "
                + "  n4.id, n4.lft, n4.rgt, n4.type, n4.rid "
                + "FROM"
                + "  nexd_dom_entityref AS ref,"
                + "  nexd_resource AS r4 "
                + "LEFT JOIN"
                + "  nexd_dom_node AS n4 ON n4.id=ref.nid "
                + "WHERE ("
                + where.replaceAll("\\{#\\}", "4")
                + ") "
                + "UNION SELECT"
                + "  pi.target AS name, pi.data AS val, r5.name AS rname, "
                + "  false AS alone, NULL AS pid, NULL AS sid, "
                + "  n5.id, n5.lft, n5.rgt, n5.type, n5.rid "
                + "FROM"
                + "  "
                + TABLE_DOM_PI
                + " AS pi,"
                + "  nexd_resource AS r5 "
                + "LEFT JOIN"
                + "  nexd_dom_node AS n5 ON n5.id=pi.nid "
                + "WHERE ("
                + where.replaceAll("\\{#\\}", "5")
                + ") "
                + "UNION SELECT"
                + "  dtd.name, dtd.subset AS val, false AS alone, "
                + "  r6.name AS rname,dtd.publicId AS pid,dtd.systemId AS sid, "
                + "  n6.id, n6.lft, n6.rgt, n6.type, n6.rid " + "FROM" + "  "
                + TABLE_DOM_DOCTYPE + " AS dtd," + "  nexd_resource AS r6 "
                + "LEFT JOIN" + "  nexd_dom_node AS n6 ON n6.id=dtd.nid "
                + "WHERE (" + where.replaceAll("\\{#\\}", "6") + ") "
                + "ORDER BY" + "  rid, lft;";

        Statement stmt = null;
        ResultSet rsNodes = null;
        try {
            stmt = this.conn.createStatement();
            rsNodes = stmt.executeQuery(sql);

            if (!rsNodes.next()) {
                LOG.error("Error: " + sql);
                throw new StorageException(
                        StorageException.INVALID_STORED_DOCUMENT);
            }

            FastArrayList list = new FastArrayList();
            list.setFast(true);

            DOMImplementation impl = new DocumentBuilderFactoryImpl()
                    .newDocumentBuilder().getDOMImplementation();
            DocumentObjectI doc = null;
            DOMNodeI node = null;
            IntStack stack = null;

            int rid = -1;

            int lastId = -1;

            do {
                int currentId = rsNodes.getInt(FIELD_ID);

                if (lastId == currentId) {
                    rsNodes.next();
                    continue;
                }
                lastId = currentId;

                int lft = rsNodes.getInt(FIELD_LFT);
                int rgt = rsNodes.getInt(FIELD_RGT);

                InternalIdI iid = new NestedSetIId(lft, rgt, currentId);

                // new document starts here
                if (rid != rsNodes.getInt(FIELD_RID)) {

                    if (doc != null) {
                        doc.startLoading();
                    }

                    rid = rsNodes.getInt(FIELD_RID);
                    doc = (DocumentObjectI) impl.createDocument(null, null, null);

                    doc.setDocumentId(rsNodes.getString(FIELD_RNAME));
                    doc.setResourceId(rsNodes.getInt(FIELD_ID) + "" + lft + ""
                            + rgt);
                    doc.stopLoading();

                    node = doc;
                    stack = new IntStack(2);
                    stack.push(rgt);

                    list.add(doc);
                }

                short type = rsNodes.getShort(FIELD_TYPE);
                Node tmp = null;

                switch (type) {

                case Node.ELEMENT_NODE:
                    tmp = doc.createElement(rsNodes.getString(FIELD_NAME));
                    break;

                case Node.ATTRIBUTE_NODE:

                    if (node == doc.getDocumentElement()
                            && !((Element) node).getAttribute(
                                    rsNodes.getString(FIELD_NAME)).equals("")) {

                        if (doc != null) {
                            doc.startLoading();
                        }

                        doc = (DocumentObjectI) impl.createDocument(null, null,
                                null);

                        doc.setDocumentId(rsNodes.getString(FIELD_RNAME));
                        doc.setResourceId(rsNodes.getInt(FIELD_ID) + "" + lft
                                + "" + rgt);
                        doc.stopLoading();

                        node = doc;
                        list.add(doc);
                    }

                    if (!(node instanceof Element)) {

                        if (!(node instanceof DOMDocumentI)) {
                            LOG.error(node.getClass());
                            throw new StorageException(
                                    StorageException.INVALID_STORED_DOCUMENT);
                        }

                        Element elem = doc.createElementNS(
                                NEXDEngineI.QUERY_RESULT_NS,
                                NEXDEngineI.QUERY_RESULT_PREFIX + ":"
                                        + NEXDEngineI.QUERY_RESULT_TAG);

                        elem.setAttribute("xmlns:"
                                + NEXDEngineI.QUERY_RESULT_PREFIX,
                                NEXDEngineI.QUERY_RESULT_NS);

                        elem.setAttributeNS(NEXDEngineI.QUERY_RESULT_NS,
                                NEXDEngineI.QUERY_RESULT_PREFIX + ":"
                                        + NEXDEngineI.QUERY_RESULT_ID, rsNodes
                                        .getInt(FIELD_ID)
                                        + ""
                                        + rsNodes.getInt(FIELD_LFT)
                                        + ""
                                        + rsNodes.getInt(FIELD_RGT));

                        doc.appendChild(elem);
                        node = (DOMNodeI) elem;
                    }

                    Attr attr = doc.createAttribute(rsNodes
                            .getString(FIELD_NAME));
                    attr.setValue(rsNodes.getString(FIELD_VALUE));
                    ((DOMNodeI) attr).setInternalId(iid);

                    ((Element) node).setAttributeNode(attr);
                    continue;

                case Node.TEXT_NODE:
                    if (!(node instanceof Element)) {

                        if (!(node instanceof DOMDocumentI)) {
                            LOG.error(node.getClass());
                            throw new StorageException(
                                    StorageException.INVALID_STORED_DOCUMENT);
                        }

                        Element elem = doc.createElementNS(
                                NEXDEngineI.QUERY_RESULT_NS,
                                NEXDEngineI.QUERY_RESULT_PREFIX + ":"
                                        + NEXDEngineI.QUERY_RESULT_TAG);

                        elem.setAttribute("xmlns:"
                                + NEXDEngineI.QUERY_RESULT_PREFIX,
                                NEXDEngineI.QUERY_RESULT_NS);

                        elem.setAttributeNS(NEXDEngineI.QUERY_RESULT_NS,
                                NEXDEngineI.QUERY_RESULT_PREFIX + ":"
                                        + NEXDEngineI.QUERY_RESULT_ID, rsNodes
                                        .getInt(FIELD_ID)
                                        + ""
                                        + rsNodes.getInt(FIELD_LFT)
                                        + ""
                                        + rsNodes.getInt(FIELD_RGT));

                        doc.appendChild(elem);
                        node = (DOMNodeI) elem;
                    }
                    tmp = doc.createTextNode(rsNodes.getString(FIELD_VALUE));
                    if (node instanceof Element
                            && node.getNodeName().equals(
                                    NEXDEngineI.QUERY_RESULT_PREFIX + ":"
                                            + NEXDEngineI.QUERY_RESULT_TAG)) {

                        ((DOMNodeI) tmp).setInternalId(iid);

                        node.appendChild(tmp);
                        continue;
                    }

                    break;

                case Node.CDATA_SECTION_NODE:
                    tmp = doc
                            .createCDATASection(rsNodes.getString(FIELD_VALUE));
                    break;

                case Node.COMMENT_NODE:
                    tmp = doc.createComment(rsNodes.getString(FIELD_VALUE));
                    break;

                case Node.PROCESSING_INSTRUCTION_NODE:
                    tmp = doc.createProcessingInstruction(rsNodes
                            .getString(FIELD_NAME), rsNodes
                            .getString(FIELD_VALUE));
                    break;

                case Node.ENTITY_REFERENCE_NODE:
                    tmp = doc.createEntityReference(rsNodes
                            .getString(FIELD_NAME));
                    break;

                case Node.DOCUMENT_TYPE_NODE:
                    tmp = doc.getImplementation().createDocumentType(
                            rsNodes.getString(FIELD_NAME),
                            rsNodes.getString(FIELD_PID),
                            rsNodes.getString("sid"));
                    ((DOMDocumentTypeI) tmp).setInternalSubset(rsNodes
                            .getString(FIELD_VALUE));
                    ((DOMDocumentTypeI) tmp).setInternalId(iid);
                    doc.appendChild(tmp);
                    continue;

                default:
                    throw new StorageException(
                            StorageException.INVALID_STORED_DOCUMENT);
                }

                // we have a new document element, means that we have to create
                // a new document.
                if (lft > stack.peek() && node == doc.getDocumentElement()) {

                    if (doc != null) {
                        doc.startLoading();
                    }

                    doc = (DocumentObjectI) impl.createDocument(null, null, null);

                    doc.setDocumentId(rsNodes.getString(FIELD_RNAME));
                    doc.setResourceId(rsNodes.getInt(FIELD_ID) + "" + lft + ""
                            + rgt);
                    doc.stopLoading();

                    list.add(doc);

                    tmp = doc.importNode(tmp, true);

                    stack.reset();
                    stack.push(rgt);

                    node = doc;
                }

                ((DOMNodeI) tmp).setInternalId(iid);

                while (lft > stack.peek()) {
                    node = (DOMNodeI) node.getParentNode();
                    stack.pop();
                }
                if (type == Node.ELEMENT_NODE) {
                    node = (DOMNodeI) node.appendChild(tmp);
                    if (stack.peek() != rgt) {
                        stack.push(rgt);
                    }
                } else {
                    node.appendChild(tmp);
                }
            } while (rsNodes.next());

            return (DocumentObjectI[]) list.toArray(new DocumentObjectI[list
                    .size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            throw new StorageException(e.getMessage(), e);
        } finally {
            this.close(stmt, rsNodes);
        }
    }

    /**
     * @param rsContIn ...
     * @return ...
     * @throws SQLException ..
     */
    private StorageCollectionI createContainer(final ResultSet rsContIn)
            throws SQLException {

        FastHashMap map = new FastHashMap();
        map.setFast(true);

        map.put(FIELD_ID, new Integer(rsContIn.getInt(FIELD_ID)));
        map.put(FIELD_PID, new Integer(rsContIn.getInt(FIELD_PID)));
        map.put(FIELD_TYPE, new Short(rsContIn.getShort(FIELD_TYPE)));
        map.put(FIELD_NAME, rsContIn.getString(FIELD_NAME));

        return new StorageCollectionImpl(rsContIn.getString("path"), rsContIn
                .getShort(FIELD_TYPE), new CollectionIId(rsContIn
                .getInt(FIELD_ID)), map);
    }

    /**
     * Returns the number of child nodes in the given <code>Node</code> object.
     *
     * @param nodeIn The parent <code>Node</code>.
     * @return The number of children.)
     */
    private int countChildNodes(final Node nodeIn) {

        int count = 0;

        if (nodeIn.hasChildNodes()) {
            NodeList children = nodeIn.getChildNodes();
            count = children.getLength();
            for (int i = 0, size = children.getLength(); i < size; i++) {
                if (children.item(i).hasAttributes()
                        || children.item(i).hasChildNodes()) {

                    count += this.countChildNodes(children.item(i));
                }
            }
        }

        if (nodeIn.hasAttributes()) {
            count += nodeIn.getAttributes().getLength();
        }

        return count;
    }

    /**
     * @param stmtIn The <code>Statement</code> to close.
     * @param rsIn The <code>ResultSet</code> to close.
     */
    private void close(final Statement stmtIn, final ResultSet rsIn) {
        this.close(stmtIn);
        this.close(rsIn);
    }

    /**
     * @param stmtIn The <code>Statement</code> to close.
     */
    private void close(final Statement stmtIn) {
        try {
            if (stmtIn != null) {
                stmtIn.close();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * @param rsIn The <code>ResultSet</code> to close.
     */
    private void close(final ResultSet rsIn) {
        try {
            if (rsIn != null) {
                rsIn.close();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
