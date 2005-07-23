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
package de.xplib.nexd.engine.xapi;

import org.sixdml.SixdmlDatabase;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.AbstractSessionFactory;
import de.xplib.nexd.comm.ConnectionException;
import de.xplib.nexd.comm.MaxConnectionException;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.comm.SessionConfigurationException;
import de.xplib.nexd.comm.SessionI;
import de.xplib.nexd.engine.xapi.services.QueryServiceImpl;
import de.xplib.nexd.engine.xapi.services.StatementServiceImpl;
import de.xplib.nexd.engine.xapi.services.TransactionServiceImpl;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DatabaseImpl extends AbstractConfigurable implements
        SixdmlDatabase {

    /*#de.xplib.nexd.engine.xapi.DBUri Dependency_Link*/
    /**
     * The prefix for a NEXD XML-Database uri.
     */
    public static final String URI_PREFIX = "xmldb:";

    /**
     * The name of the NEXD XML-Database.
     */
    public static final String NAME = "nexd";

    /**
     * The default conformance level.
     */
    public static final String LEVEL = "1.0";

    /**
     * The <code>NEXDEngineI</code> instance, that is used a driver for the NEXD
     * XML-Database backend.
     */
    private NEXDEngineI engine = null;

    /**
     * Gets the specified resource from the database.
     *
     * @param uri the resource to obtain.
     * @param username The username to use for authentication to the database or
     * null if the database does not support authentication.
     * @param password The password to use for authentication to the database or
     * null if the database does not support authentication.
     * @return the requested resource.
     * @exception XMLDBException ..
     * @see org.sixdml.SixdmlDatabase#getResource(
     * 		java.lang.String, java.lang.String, java.lang.String)
     */
    public Resource getResource(final String uri, final String username,
            final String password) throws XMLDBException {

        DBUri dbUri = DBUri.parseUri(uri);
        Resource res = null;
        if (dbUri != null) {

            this.createEngine(dbUri, username, password);

            String path = dbUri.getPath();
            String coll = path.substring(0, path.lastIndexOf('/'));
            String resId = path.substring(path.lastIndexOf('/') + 1);

            res = this.engine.queryCollection(coll).getResource(resId);
        }
        return res;
    }

    /**
     * Provides a list of all services known to the collection. If no services
     * are known an empty list is returned.
     * @return An array of registered Service implementations.
     * @exception XMLDBException If an error occurs.
     * @see org.sixdml.SixdmlDatabase#getServices()
     */
    public Service[] getServices() throws XMLDBException {
        Service[] services = new Service[] {
                new QueryServiceImpl(this.engine),
                new TransactionServiceImpl(this.engine, (CollectionImpl) this
                        .getCollection("/db", "sa", "")),
                new StatementServiceImpl(this.engine, this) };

        return services;
    }

    /**
     * Returns a Service instance for the requested service name and version.
     * If no Service exists for those parameters a null value is returned. The
     * only valid name and version number for this implementation are "XSLT"
     * and "1.0" respectively.
     *
     * @param nameIn the name of the service to return.
     * @param version the version number of the service to return.
     * @return ..
     * @exception XMLDBException if an error occurs.
     * @see org.sixdml.SixdmlDatabase#getService(
     * 		java.lang.String, java.lang.String)
     */
    public Service getService(final String nameIn, final String version)
            throws XMLDBException {

        Service service = null;
        if (nameIn.equals("SixdmlQueryService") && version.equals(LEVEL)) {
            service = new QueryServiceImpl(this.engine);
        } else if ((nameIn.equals("TransactionService") || nameIn
                .equals("SixdmlTransactionService"))
                && version.equals(LEVEL)) {

            service = new TransactionServiceImpl(this.engine,
                    (CollectionImpl) this.getCollection("/db", "sa", ""));
        } else if (nameIn.equals("SixdmlStatementService")
                && version.equals(LEVEL)) {

            service = new StatementServiceImpl(this.engine, this);
        }
        return service;
    }

    /**
     * Returns the name associated with the Database instance.
     *
     * @return The name of the object.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     * @see org.xmldb.api.base.Database#getName()
     */
    public String getName() throws XMLDBException {
        return NAME;
    }

    /**
     * Retrieves a <code>Collection</code> instance based on the URI provided
     * in the <code>uri</code> parameter. The format of the URI is defined in
     * the documentation for DatabaseManager.getCollection().<p/>
     *
     * Authentication is handled via username and password however it is not
     * required that the database support authentication. Databases that do not
     * support authentication MUST ignore the
     * <code>username</code> and <code>password</code> if those provided are not
     * null.
     *
     * @param uri The URI to use to locate the collection.
     * @param username The username to use for authentication to the database or
     *        null if the database does not support authentication.
     * @param password The password to use for authentication to the database or
     *        null if the database does not support authentication.
     * @return A <code>Collection</code> instance for the requested collection
     *         or null if the collection could not be found.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific
     *            errors that occur.<br /> <code>ErrorCodes.INVALID_URI</code>
     *            If the URI is not in a valid format.<br />
     *            <code>ErrorCodes.PERMISSION_DENIED</code> If the
     *            <code>username</code> and <code>password</code> were not
     *            accepted by the database.
     * @see org.xmldb.api.base.Database#getCollection(
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public Collection getCollection(final String uri, final String username,
            final String password) throws XMLDBException {

        DBUri dbUri = DBUri.parseUri(uri);
        Collection coll = null;
        if (dbUri != null) {
            this.createEngine(dbUri, username, password);
            coll = this.engine.queryCollection(dbUri.getPath());
        }
        return coll;
    }

    /**
     * acceptsURI determines whether this <code>Database</code> implementation
     * can handle the URI. It should return true if the Database instance knows
     * how to handle the URI and false otherwise.
     *
     * @param uri The URI to check for.
     * @return true If the URI can be handled, false otherwise.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific
     *            errors that occur.<br /><code>ErrorCodes.INVALID_URI</code> If
     *            the URI is not in a valid format. <br />
     * @see org.xmldb.api.base.Database#acceptsURI(java.lang.String)
     */
    public boolean acceptsURI(final String uri) throws XMLDBException {
        return (DBUri.parseUri(uri) != null);
    }

    /**
     * Returns the XML:DB API Conformance level for the implementation. This can
     * be used by client programs to determine what functionality is available
     * to them.
     *
     * @return The XML:DB API conformance level for this implementation.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     * @see org.xmldb.api.base.Database#getConformanceLevel()
     */
    public String getConformanceLevel() throws XMLDBException {
        return "1.0";
    }

    /**
     * Getter method for the underlying <code>NEXDEngineI</code> instance.
     *
     * @return The underlying <code>NEXDEngineI</code>.
     */
    public NEXDEngineI getEngine() {
        return this.engine;
    }

    /**
     * @param uriIn ..
     * @param username The username for authentification
     * @param password ..
     * @throws XMLDBException ..
     */
    protected void createEngine(final DBUri uriIn, final String username,
            final String password) throws XMLDBException {

        if (this.engine == null) {
            try {
                // create a session instance
                AbstractSessionFactory factory = AbstractSessionFactory
                        .newInstance();
                SessionI session = null;
                if (uriIn.getPort() == null) {
                    session = factory.newSession();
                } else {
                    //session = factory.newSession(parts[0], parts[1]);
                    session = factory.newSession(uriIn.getHost(), uriIn
                            .getPort());
                }

                // connect session to backend
                this.engine = session.getEngine();
                if (engine != null) {
                    this.engine.open(username, password);
                }
            } catch (SessionConfigurationException e) {
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e
                        .getMessage());
            } catch (ConnectionException e) {
                e.printStackTrace();
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e
                        .getMessage());
            } catch (MaxConnectionException e) {
                throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e
                        .getMessage());
            }
        }
    }

}