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

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

/**
 * <code>Resource</code> is a container for data stored within the database. 
 * Raw resources are not particulary useful. It is necessary to have a resource 
 * implementation that provides handling for a specific content type before 
 * anything useful can be done.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public abstract class AbstractResource implements Resource {
    
    /**
     * The unique id of the <code>Resource</code>.
     */
    private final String resourceId;
    
    /**
     * The type of the concrete <code>Resource</code> instance.
     */
    private final String resourceType;

    /**
     * @param resourceIdIn The unique <code>Resource</code> id.
     * @param resourceTypeIn The type of the <code>Resource</code>.
     */
    public AbstractResource(final String resourceIdIn, 
                            final String resourceTypeIn) {
        super();
        
        this.resourceId   = resourceIdIn;
        this.resourceType = resourceTypeIn;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Resource#getParentCollection()
     */
    public Collection getParentCollection() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the unique id for this <code>Resource</code> or null if the 
     * <code>Resource</code> is anonymous. The <code>Resource</code> will be 
     * anonymous if it is obtained as the result of a query.
     *
     * @return The id for the Resource or null if no id exists.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#getId()
     */
    public final String getId() throws XMLDBException {
        return this.resourceId;
    }

    /**
     * Returns the resource type for this Resource. 
     * <p />
     * XML:DB defined resource types are: <p />
     * XMLResource - all XML data stored in the database<br />
     * BinaryResource - Binary blob data stored in the database<br />
     * 
     * @return The resource type for the Resource.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#getResourceType()
     */
    public final String getResourceType() throws XMLDBException {
        return this.resourceType;
    }

}
