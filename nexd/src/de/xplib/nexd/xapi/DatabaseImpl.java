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

import org.apache.commons.collections.map.LRUMap;
import org.sixdml.SixdmlDatabase;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.ConnectionException;
import de.xplib.nexd.comm.SessionConfigurationException;
import de.xplib.nexd.comm.SessionFactory;
import de.xplib.nexd.comm.SessionI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DatabaseImpl 
    extends AbstractConfigurable 
    implements SixdmlDatabase {
    
    /**
     * The prefix for a NEXD XML-Database uri. 
     */
    public static final String URI_PREFIX = "xmldb:";
    
    /**
     * The name of the NEXD XML-Database.
     */
    public static final String NAME = "nexd";
        
    /**
     * The <code>Session</code> instance, that manages the communication
     * to the NEXD XML-Database backend.
     */
    private SessionI session = null;
    
    /**
     * Last recently used cache that contains the last parsed uris. 
     */
    private LRUMap uriCache = new LRUMap(80);

    /**
     * 
     */
    public DatabaseImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Gets the specified resource from the database.
     *  
     * @param name the resource to obtain. 
     * @param username The username to use for authentication to the database or
     * null if the database does not support authentication.
     * @param password The password to use for authentication to the database or
     * null if the database does not support authentication.
     * @return the requested resource. 
     * @exception XMLDBException ..
     * @see org.sixdml.SixdmlDatabase#getResource(
     * 		java.lang.String, java.lang.String, java.lang.String)
     */
    public Resource getResource(final String name, 
                                final String username, 
                                final String password) throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Provides a list of all services known to the collection. If no services 
     * are known an empty list is returned.
     * @return An array of registered Service implementations.
     * @exception XMLDBException If an error occurs. 
     * @see org.sixdml.SixdmlDatabase#getServices()
     */
    public Service[] getServices() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns a Service instance for the requested service name and version. 
     * If no Service exists for those parameters a null value is returned. The 
     * only valid name and version number for this implementation are "XSLT" 
     * and "1.0" respectively.
     * 
     * @param name the name of the service to return. 
     * @param version the version number of the service to return. 
     * @return ..
     * @exception XMLDBException if an error occurs.
     * @see org.sixdml.SixdmlDatabase#getService(
     * 		java.lang.String, java.lang.String)
     */
    public Service getService(final String name, final String version)
            throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
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
    public Collection getCollection(final String uri, 
                                    final String username, 
                                    final String password)
                                            throws XMLDBException {
        
        this.createSession(uri, username, password);
        
        String path = this.getURIPath(uri);
        
        if (this.acceptsURI(uri)) {
            System.out.println(uri + " # # " + path);
        }
        // TODO Auto-generated method stub
        return null;
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
        return (this.parseURI(uri) != null);
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
     * @param uriIn ..
     * @throws XMLDBException ..
     */
    protected void createSession(final String uriIn,
                                 final String username,
                                 final String password) throws XMLDBException {
        
        if (this.session == null) {
            
            String[] parts = this.parseURI(uriIn);
            if (parts == null) {
                throw new XMLDBException(
                        ErrorCodes.INVALID_URI, "The uri [" + uriIn + "] has"
                        + " an invalid format.");
            }

            try {
                // create a session instance
                SessionFactory sf = SessionFactory.newInstance();
                if (parts.length == 3) {
                    this.session = sf.newSession(parts[0], parts[1]);
                } else {
                    this.session = sf.newSession();
                }

                // connect session to backend
                this.session.open(username, password);
                
            } catch (SessionConfigurationException e) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e.getMessage());
            } catch (ConnectionException e) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e.getMessage());
            }
        }
    }
    
    /**
     * @param uriIn ..
     * @return ..
     */
    protected String getURIPath(final String uriIn) {
        String[] parts = this.parseURI(uriIn);
        return parts[parts.length - 1];
    }
    
    /**
     * @param uriIn ..
     * @return ..
     */
    protected String[] parseURI(final String uriIn) {
        
        // lookup for the cached uri
        if (!this.uriCache.containsKey(uriIn)) {
        
            if (!uriIn.subSequence(0, 7).equals(NAME + "://")) {
                return null;
            }
            String stripped = uriIn.substring(NAME.length() + 3);
            int slash       = stripped.indexOf('/');
            if (slash == -1) {
                return null;
            }
            String host = stripped.substring(0, slash);
            String path = stripped.substring(slash);
        
            int col = host.indexOf(':'); 
            if (!host.equals("localhost.") && col != host.lastIndexOf(':')) {
                return null;
            }
            if (!path.startsWith("/")) {
                return null;
            }
        
            String[] parts;
            if (col != -1) {
                parts = new String[] {
                        host.substring(0, col), host.substring(col + 1), path};
            } else {
                parts = new String[] {host, path};
            }
        
            // Store the parts in cache
            this.uriCache.put(uriIn, parts);
    	}
        
        return (String[]) this.uriCache.get(uriIn);
    }

}
