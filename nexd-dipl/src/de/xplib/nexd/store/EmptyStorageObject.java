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
package de.xplib.nexd.store;


/**
 * An empty implementation of the <code>StorageObjectI</code>. This class is 
 * used if only the <code>oid</code> or/and <code>type</code> is required. The 
 * simplifies some tasks and it reduces possible network traffic.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class EmptyStorageObject implements StorageObjectI {
    
    /**
     * The object id.
     */
    private final String oid;
    
    /**
     * The object type.
     */
    private final byte type;
    
    /**
     * The internal storage identifier.
     */
    private final InternalIdI internalId;

    /**
     * Constructor.
     * 
     * @param oidIn The object id.
     * @param typeIn The resource type.
     * @param internalIdIn The internal storage identifier.
     */
    public EmptyStorageObject(final String oidIn, 
                              final byte typeIn,
                              final InternalIdI internalIdIn) {
        
        super();
        
        this.oid        = oidIn;
        this.type       = typeIn;
        this.internalId = internalIdIn;
    }

    /**
     * Getter method for the resource oid.
     * 
     * @return The resource oid.
     * @see de.xplib.nexd.store.StorageObjectI#getOID()
     */
    public String getOID() {
        return this.oid;
    }

    /**
     * Getter method for the resouce type.
     * 
     * @return The resource type.
     * @see de.xplib.nexd.store.StorageObjectI#getType()
     * @see de.xplib.nexd.store.StorageObjectI#BINARY
     * @see de.xplib.nexd.store.StorageObjectI#XML
     */
    public byte getType() {
        return this.type;
    }
    
    /**
     * Returns the internal storage oid.
     * 
     * @return The internal oid.
     * @see de.xplib.nexd.store.StorageEntityI#getInternalId()
     */
    public InternalIdI getInternalId() {
        return this.internalId;
    }
}
