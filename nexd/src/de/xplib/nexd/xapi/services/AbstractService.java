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
package de.xplib.nexd.xapi.services;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.xapi.AbstractConfigurable;

/**
 * The <code>Service</code> interface provides an extension mechanism for 
 * <code>Collection</code> implementations. It is to be implented by Service
 * instances that define their own set of methods to perform the necessary
 * action. For an example of what a functional <code>Service</code> interface
 * should look like look at XPathQueryService.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @see de.xplib.nexd.xapi.AbstractConfigurable
 * @see org.xmldb.api.base.Service
 */
public abstract class AbstractService 
    extends AbstractConfigurable 
    implements Service {
    
    /**
     * The default version for the conformance level. 
     */
    public static final String DEFAULT_VERSION = "1.0";
    
    /**
     * The name of the concrete <code>Service</code> instance.
     */
    private final String name;
    
    /**
     * The conformance level of the service. The default value is level '1.0'.
     */
    private final String version;

    /**
     * Constructor.
     * 
     * @param nameIn The unique identifier of the <code>Service</code>
     */
    protected AbstractService(final String nameIn) {
        this(nameIn, DEFAULT_VERSION);
    }
    
    /**
     * Constructor.
     * 
     * @param nameIn The unique identifier of the <code>Service</code>
     * @param versionIn The conformance level of the <code>Service</code>.
     */
    protected AbstractService(final String nameIn, final String versionIn) {
        super();
        
        this.name    = nameIn;
        this.version = versionIn;
    }
    
    

    /**
     * Returns the name associated with the Service instance.
     *
     * @return The name of the object.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     * @see org.xmldb.api.base.Service#getName()
     */
    public final String getName() throws XMLDBException {
        return this.name;
    }

    /**
     * Gets the Version attribute of the Service object
     *
     * @return The Version value
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br /> 
     * @see org.xmldb.api.base.Service#getVersion()
     */
    public final String getVersion() throws XMLDBException {
        return this.version;
    }

    /**
     * Sets the Collection attribute of the Service object
     *
     * @param col The new Collection value
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     * @see org.xmldb.api.base.Service#setCollection(
     *      org.xmldb.api.base.Collection)
     */
    public final void setCollection(final Collection col) 
            throws XMLDBException {
        // TODO Auto-generated method stub

    }

}
