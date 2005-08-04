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
 * $Log: ResourceObjectFactory.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.VirtualResource;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ResourceObjectFactory {
    
    

    /**
     * Holds singleton instance
     */
    private static ResourceObjectFactory instance;

    /**
     * Returns the singleton instance.
     * @return	the singleton instance
     */
    public static ResourceObjectFactory getInstance() {
        if (instance == null) {
            instance = new ResourceObjectFactory();
        }
        return instance;
    }

    /**
     * 
     */
    public ResourceObjectFactory() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public ResourceObject createResourceRow(final Collection collIn,
                                         final Resource resIn) {
        
        if (resIn instanceof VirtualResource) {
            return this.createResourceRow((VirtualCollection) collIn,
                                          (VirtualResource) resIn);
        } else if (resIn instanceof SixdmlResource) {
            return this.createResourceRow((SixdmlCollection) collIn,
                                          (SixdmlResource) resIn);
        } else if (resIn instanceof XMLResource) {
            return this.createResourceRow(collIn, (XMLResource) resIn);
        } else if (resIn instanceof BinaryResource) {
            return this.createResourceRow(collIn, (BinaryResource) resIn);
        }
        return null;
    }
    
    public ResourceObject createResourceRow(final VirtualCollection collIn,
                                         final VirtualResource resIn) {
        return new VirtualResourceObject(collIn, resIn);
    }
    
    public ResourceObject createResourceRow(final SixdmlCollection collIn,
                                         final SixdmlResource resIn) {
        return new SixdmlResourceObject(collIn, resIn);
    }
    
    public ResourceObject createResourceRow(final Collection collIn,
                                         final XMLResource resIn) {
        return new XMLResourceObject(collIn, resIn);
    }
    
    public ResourceObject createResourceRow(final Collection collIn,
                                         final BinaryResource resIn) {
        return null;
    }

}
