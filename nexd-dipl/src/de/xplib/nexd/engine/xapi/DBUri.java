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
 * $Log: DBUri.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.xapi;

import org.apache.commons.collections.map.LRUMap;

/**
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @stereotype Helper
 * @version $Revision: 1.3 $
 */
public final class DBUri {
    
    /**
     * Last recently used cache that contains the last parsed uris.
     * @associates <{DBUri}>
     * @clientCardinality 1
     * @clientRole URI_CACHE
     * @directed true
     * @supplierCardinality 0..*
     */
    private static final LRUMap URI_CACHE = new LRUMap(80);
    
    /**
     * The host path of the uri
     */
    private final String host;
    
    /**
     * The port part of the uri or null
     */
    private final String port;
    
    /**
     * The path path of the uri
     */
    private final String path;
    
    /**
     * Parses a given uri and returns its DBUri instance.
     * 
     * @param uriIn The uri to parse.
     * @return A DBUri instance.
     */
    public static DBUri parseUri(final String uriIn) {
        if (!URI_CACHE.containsKey(uriIn)) {
            DBUri dbUri = null;
            
            // accept uri without <nexd:....> prefix if we have an engine
            if (uriIn.substring(0, 3).equals("/db")) {
                dbUri = parse("localhost." + uriIn, 10);
            } else if (uriIn.substring(0, 7).equals(
                    DatabaseImpl.NAME + "://") 
                    && uriIn.substring(DatabaseImpl.NAME.length() + 3
                            ).indexOf('/') != -1) {
                
            	String stripped = uriIn.substring(
            	        DatabaseImpl.NAME.length() + 3);
              	dbUri = parse(stripped, stripped.indexOf('/'));
        	}
        	
        	if (dbUri != null) {
        	    URI_CACHE.put(uriIn, dbUri);
        	}
        }
        
        return (DBUri) URI_CACHE.get(uriIn);
    }
    
    /**
     * Konstructor.
     * 
     * @param hostIn The host part of the uri.
     * @param pathIn The path part of the uri.
     */
    private DBUri(final String hostIn, final String pathIn) {
        this(hostIn, null, pathIn);
    }
    
    /**
     * Konstructor.
     * 
     * @param hostIn The host part of the uri.
     * @param portIn The port part of the uri.
     * @param pathIn The path part of the uri.
     */
    private DBUri(final String hostIn, 
                  final String portIn, 
                  final String pathIn) {
        super();
        
        this.host = hostIn;
        this.port = portIn;
        this.path = pathIn;
    }
    
    /**
     * Returns the host part of the uri.
     * 
     * @return The host part of the uri.
     */
    public String getHost() {
        return host;
    }
    
    /**
     * Returns the path part of the uri.
     * 
     * @return The path part of the uri.
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Returns the port part of the uri.
     * 
     * @return The port part of the uri.
     */
    public String getPort() {
        return port;
    }
    
    /**
     * @param stripped Stripped uri without (nexd://) prefix.
     * @param slash the index of the next slash(/).
     * @return The DBUri instance.
     */
    static DBUri parse(final String stripped, final int slash) {
        
        DBUri dbUri = null;
        
        String host = stripped.substring(0, slash);
        String path = stripped.substring(slash);
    
        int col = host.indexOf(':'); 
        if (host.equals("localhost.") || col == host.lastIndexOf(':')) {
            if (path.startsWith("/")) {
                while (path.endsWith("/")) {
                    path = path.substring(0, path.length() - 1);
                }
                
                if (col == -1) {
                    dbUri = new DBUri(host, path);
                } else {
                    dbUri = new DBUri(
                            host.substring(0, col), 
                            host.substring(col + 1), path);
                }
            }
        }
        
        return dbUri;
    }

}
