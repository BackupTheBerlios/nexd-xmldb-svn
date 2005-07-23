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
 * $Log: EngineConfig.java,v $
 * Revision 1.1  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.7  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.6  2005/04/19 07:24:50  nexd
 * Refactoring, now config is a singleton.
 *
 * Revision 1.5  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.4  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 * Revision 1.3  2005/03/01 10:34:37  nexd
 * Advanced VCL support
 *
 */
package de.xplib.nexd.engine.config;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.FastHashMap;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public final class EngineConfig {
    
    /**
     * Comment for <code>LOG</code>
     */
    public static final Log LOG = LogFactory.getLog(EngineConfig.class);
    
    /**
     * Default number of clients that can connect to the NEXD XML-Database
     * engine at the same time. This value will only be used if the admin didn't
     * set the value in the <code>nexd-engine.xml</code> configuration file.
     */
    public static final int MAX_CONNECTIONS = 10;

    /**
     * Holds singleton instance
     */
    private static EngineConfig instance;
    
    /**
     * Comment for <code>MAX_CONNECTIONS_KEY</code>
     */
    private static final String MAX_CONNECTIONS_KEY = 
        "engine[@max-connections]";
        
    /**
     * Comment for <code>STORAGE_KEY</code>
     */
    private static final String STORAGE_KEY = "engine.storage";
    
    /**
     * Comment for <code>maxConnections</code>
     */
    private final int maxConnections;
    
    /**
     * The number of cachable xpath queries.
     */
    private final int queryCacheSize; 
    
    /**
     * Comment for <code>storage</code>
     */
    private final String storage;
    
    /**
     * Comment for <code>storageParams</code>
     */
    private final Map storageParams;
    
    /**
     * The nexd server configuration.
     */
    private final ServerConfig serverCfg;
    
    /**
     * Returns the singleton instance.
     * @return	the singleton instance
     * @exception ConfigurationException If something is wrong configured, or 
     *            the <code>nexd-engine.xml</code> file couldn't be found.
     */
    public static EngineConfig getInstance() 
            throws ConfigurationException {
        
        if (instance == null) {
            instance = new EngineConfig();
        }
        return instance;
    }

    /**
     * Constructor.
     * 
     * @exception de.xplib.nexd.engine.config.ConfigurationException If 
     *            something is wrong configured, or the 
     *            <code>nexd-engine.xml</code> file couldn't be found. 
     */
    private EngineConfig() throws ConfigurationException {
        super();

        Configuration conf;
        try {
            
            conf = new XMLConfiguration(
                    System.getenv("HOME") + "/.nexd/nexd-engine.xml");
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            
            LOG.info("No user config found, trying current location.");
            
            // not found try current filesystem location
            try {
                conf = new XMLConfiguration("conf/nexd-engine.xml");
            } catch (org.apache.commons.configuration.ConfigurationException 
                    e1) {
                
                throw new ConfigurationException(e.getMessage());
            }
        }
        
        this.maxConnections = conf.getInt(MAX_CONNECTIONS_KEY, MAX_CONNECTIONS);
        this.queryCacheSize = conf.getInt("engine[@query-cache]", -1);
        
        this.storage        = conf.getString(STORAGE_KEY + "[@factory]");
        this.storageParams  = this.readParams(conf, STORAGE_KEY);
                
        this.serverCfg = new ServerConfig(
                conf.getInt(MAX_CONNECTIONS_KEY, MAX_CONNECTIONS),
                conf.getInt("engine.server[@port]", -1),
                conf.getBoolean("engine.server[@allow-remote]", false),
                conf.getBoolean("engine.server[@allow-remote-changes]", false));
        
    }
    
    /**
     * Returns the nexd server configuration.
     * 
     * @return The server configuration.
     */
    public ServerConfig getServerConfig() {
        return this.serverCfg;
    }
    
    /**
     * @param conf The <code>Configuration</code> instance.
     * @param key The key used to find the configuration
     * @return A map with all defined attributes in this path.
     */
    private Map readParams(final Configuration conf, final String key) {
        
        FastHashMap map = new FastHashMap();
        map.setFast(true);
        
        int index = (key + ".params[@").length();
        
        Iterator keys = conf.getKeys(STORAGE_KEY);
        while (keys.hasNext()) {
            String path  = (String) keys.next();
            String value = conf.getString(path);
            map.put(path.substring(index, path.length() - 1), value);
        }
        return map;
    }

    /**
     * @return ..
     */
    public int getMaxConnections() {
        return maxConnections;
    }
    
    /**
     * 
     * @return The number of cacheable xpath queries.
     */
    public int getQueryCacheSize() {
        return this.queryCacheSize;
    }
    
    /**
     * @return ..
     */
    public String getStorage() {
        return storage;
    }
    
    /**
     * @return ..
     */
    public Map getStorageParams() {
        return storageParams;
    }
}
