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

/**
 * Base interface for all object types, handled by the XML-Database NEXD.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public interface StorageObjectI extends StorageEntityI {

    /**
     * Identifies a concrete <code>StorageObjectI</code> implementation as
     * a XML resource object.
     */
    byte XML = 0;
    
    /**
     * Identifies a concrete <code>StorageObjectI</code> implementation as
     * a XML binary object.
     */
    byte BINARY = 1;
    
    /**
     * Returns the id of a concrete <code>StorageObjectI</code> implementation
     * this id is unique in the parent collection.
     * 
     * @return The id of the object.
     */
    String getOID();
    
    /**
     * Returns the type of a concrete <code>StorageObjectI</code>
     * implementation.
     * 
     * @return The type of the object.
     */
    byte getType();
}
