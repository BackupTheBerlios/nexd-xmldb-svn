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
 * $Log: StorageEntityI.java,v $
 * Revision 1.5  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.4  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.3  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.store;

/**
 * Base interface for all storable XML:DB objects. This interface adds support
 * for storage specific informations, like a PRIMARY KEYs or a qualified file
 * system path. 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public interface StorageEntityI {

    /**
     * @clientCardinality 1
     * @clientRole identifier
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#public InternalIdI lnkInternalIdI*/

    /**
     * Returns a vendor specific id <code>String</code>. This could be used for
     * storage specific informations, like a PRIMARY KEYs or a concrete file
     * system path.
     * 
     * @return The internal storage identifier. 
     */
    InternalIdI getInternalId();
    
}
