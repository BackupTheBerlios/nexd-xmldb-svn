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

import org.apache.commons.collections.FastHashMap;
import org.xmldb.api.base.Configurable;
import org.xmldb.api.base.XMLDBException;

/**
 * Provides the ability to configure properties about an object.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public abstract class AbstractConfigurable implements Configurable {
    
    /**
     * Comment for <code>properties</code>
     */
    private final FastHashMap properties = new FastHashMap();
    
    /**
     * Constructor. 
     * 
     * Initializes the <code>FastHashMap</code> that stores the properties.
     */
    public AbstractConfigurable() {
        super();
        
        this.properties.setFast(true);
    }

    /**
    * Returns the value of the property identified by <code>name</code>.
    *
    * @param name the name of the property to retrieve.
    * @return the property value or null if no property exists.
    * @exception XMLDBException with expected error codes.<br />
    *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *  specific errors that occur.<br />
     * @see org.xmldb.api.base.Configurable#getProperty(java.lang.String)
     */
    public String getProperty(final String name) throws XMLDBException {
        return (String) this.properties.get(name);
    }

    /**
    * Sets the property <code>name</code> to have the value provided in 
    * <code>value</code>.
    *
    * @param name the name of the property to set.
    * @param value the value to set for the property.
    * @exception XMLDBException with expected error codes.<br />
    *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *  specific errors that occur.<br />
     * @see org.xmldb.api.base.Configurable#setProperty(
     *      java.lang.String, java.lang.String)
     */
    public void setProperty(final String name, final String value) 
        throws XMLDBException {

        this.properties.put(name, value);
    }

}
