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
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;

/**
 * <code>Resource</code> is a container for data stored within the database. Raw
 * resources are not particulary useful. It is necessary to have a resource 
 * implementation that provides handling for a specific content type before 
 * anything useful can be done.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class BinaryResourceImpl
	extends AbstractResource
	implements BinaryResource {

    /**
     *  
     */
    public BinaryResourceImpl() {
        super("", RESOURCE_TYPE);
        // TODO Auto-generated constructor stub
    }

    /**
     * Returns the <code>Collection</code> instance that this resource is 
     * associated with. All resources must exist within the context of a 
     * <code>collection</code>.
     *
     * @return The collection associated with the resource.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#getParentCollection()
     */
    public Collection getParentCollection() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Retrieves the content from the resource. The type of the content varies
     * depending what type of resource is being used.
     *
     * @return The content of the resource.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#getContent()
     */
    public Object getContent() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Sets the content for this resource. The type of content that can be set
     * depends on the type of resource being used.
     *
     * @param value The content value to set for the resource.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#setContent(java.lang.Object)
     */
    public void setContent(final Object value) throws XMLDBException {
        // TODO Auto-generated method stub

    }

}
