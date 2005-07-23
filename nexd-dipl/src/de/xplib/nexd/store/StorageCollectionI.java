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

/*
 * $Log: StorageCollectionI.java,v $
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.store;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public interface StorageCollectionI extends StorageEntityI {
    /**
     * @clientCardinality 0..1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     */
    /*#de.xplib.nexd.store.StorageEntityI lnkStorageEntityI*/

    /**
     * Comment for <code>TYPE_COLLECTION</code>
     */
    short TYPE_COLLECTION = 0;

    /**
     * Comment for <code>TYPE_VIRTUAL_COLLECTION</code>
     */
    short TYPE_VIRTUAL_COLLECTION = 1;

    /**
     * Getter method for the collection <code>type</code>.
     *
     * @return The collection type.
     */
    short getType();

    /**
     * Getter method for the collection <code>path</code>.
     *
     * @return The collection path.
     */
    String getPath();
}