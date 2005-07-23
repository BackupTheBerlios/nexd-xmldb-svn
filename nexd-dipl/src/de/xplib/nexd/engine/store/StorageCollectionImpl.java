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

/*
 * $Log: StorageCollectionImpl.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.5  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.4  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.store;

import java.util.Map;

import org.apache.commons.collections.FastHashMap;

import de.xplib.nexd.store.InternalIdI;
import de.xplib.nexd.store.StorageCollectionI;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class StorageCollectionImpl implements StorageCollectionI {

    /**
     * @clientCardinality 0..1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     */
    /*#de.xplib.nexd.store.StorageEntityI lnkStorageEntityI*/

    /**
     * The collection path.
     */
    private final String path;

    /**
     * The collection type.
     *
     * @see StorageCollectionImpl#TYPE_COLLECTION
     * @see StorageCollectionImpl#TYPE_VIRTUAL_COLLECTION
     */
    private final short type;

    /**
     * <code>Map</code> with backend specific properties.
     */
    private final FastHashMap properties;

    /**
     * Internal id <code>String</code> for storage specific informations.
     * @associates <{CollectionIId}>
     * @clientCardinality 1
     * @clientRole internalId
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    private final InternalIdI internalId;

    /**
     * Constructor.
     *
     * @param pathIn The collection path.
     * @param typeIn The collection type.
     * @param internalIdIn The internal storage id.
     * @param propertiesIn Backend specific properties.
     */
    public StorageCollectionImpl(final String pathIn, final short typeIn,
            final InternalIdI internalIdIn, final Map propertiesIn) {
        super();

        this.path = pathIn;
        this.type = typeIn;

        this.internalId = internalIdIn;

        this.properties = new FastHashMap(propertiesIn);
        this.properties.setFast(true);
    }

    /**
     * Getter method for the collection <code>type</code>.
     *
     * @return The collection type.
     */
    public short getType() {
        return this.type;
    }

    /**
     * Getter method for the collection <code>path</code>.
     *
     * @return The collection path.
     */
    public String getPath() {
        return this.path;
    }
    
    /**
     * Returns the internal storage id.
     *
     * @return The internal id.
     * @see de.xplib.nexd.store.StorageEntityI#getInternalId()
     */
    public InternalIdI getInternalId() {
        return this.internalId;
    }

}