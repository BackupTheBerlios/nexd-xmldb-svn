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
package de.xplib.nexd.store;

import de.xplib.nexd.ConfigurationException;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 */
public abstract class StorageFactory {
    
    
    /**
     * Constant with the identifier for the used StorageFactory class.
     */
    public static final String STORAGE_FACTORY_KEY = 
        "de.xplib.nexd.store.storage.factory";

    
    /**
     * This factory method creates an instance of StorageFactory. It tries to 
     * find the class name in the system properties and the environment
     * variables. The used variable key is 
     * <code>de.xplib.nexd.store.storage.factory</code>.
     * 
     * <code>
     *   System.getProperty('de.xplib.nexd.store.storage.factory');
     * 	 System.getenv().get('de.xplib.nexd.store.storage.factory');
     * </code>
     * 
     * @return Returns a concrete instance of StorageFactory.
     * @throws ClassNotFoundException If the configured class cannot be found.
     * @throws InstantiationException If something goes wrong.
     * @throws IllegalAccessException If something goes wrong.
     * @throws ConfigurationException If there is no configuration for the 
     *                                used factory, this Exception is thrown.  
     */
	public static final StorageFactory newInstance()
			throws ClassNotFoundException,
				   InstantiationException,
				   IllegalAccessException,
				   ConfigurationException {
	    
	    String name = "";
	    if (System.getProperties().containsKey(STORAGE_FACTORY_KEY)) {
	        name = System.getProperty(STORAGE_FACTORY_KEY);
	    } else if (System.getenv().containsKey(STORAGE_FACTORY_KEY)) {
	        name = (String) System.getenv().get(STORAGE_FACTORY_KEY);
	    } else {
	        throw new ConfigurationException();
	    }
	    
	    Class clazz = Class.forName(name);
	    return (StorageFactory) clazz.newInstance();
	} // end public static final StorageFactory newInstance()
	
	
	/**
	 * This method creates the StorageI instance, that is associated with 
	 * the factory. 
	 * 
	 * @return The concrete StorageI instance.
	 * @throws ConfigurationException This is thrown if something is 
	 *                                missconfigured for the used storage.
	 */
	public abstract StorageI newStorage() throws ConfigurationException;
	
	
} // end public abstract class StorageFactory
