/*
 * Project: xmldb-manager 
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
 * $Log: VirtualResourceObject.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.VirtualResource;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class VirtualResourceObject extends XMLResourceObject {
    
    /**
     * Comment for <code>LOG</code>
     */
    private static final Log LOG = LogFactory.getLog(
            VirtualResourceObject.class);
    
    
    public static final String RESOURCE_TYPE = "VirtualResource";
    
    private PCVirtualResourceObject pcvrObj = null;

    /**
     * @param collIn
     * @param resIn
     */
    public VirtualResourceObject(final VirtualCollection collIn, 
                                 final VirtualResource resIn) {
        super(collIn, resIn);

        this.type = RESOURCE_TYPE;
    }
    
    public PCVirtualResourceObject getPCVResource() {
        if (this.pcvrObj == null) {
            try {
                this.pcvrObj = new PCVirtualResourceObject(
                        this, ((VirtualResource) this.userObject)
                                .getPreCompiledResource());
            } catch (XMLDBException e) {
                e.printStackTrace();
                LOG.error(e);
            }
        }
        return this.pcvrObj;
    }

}
