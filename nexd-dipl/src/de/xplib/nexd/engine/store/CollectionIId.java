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
 * $Log: CollectionIId.java,v $
 * Revision 1.1  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.store;

import de.xplib.nexd.store.InternalIdI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class CollectionIId implements InternalIdI {
    
    /**
     * The internal id
     */
    private final int id;

    /**
     * Construtor.
     * 
     * @param idIn The internal id
     */
    public CollectionIId(final int idIn) {
        super();
        
        this.id = idIn;
    }
    
    /**
     * Returns the internal id.
     * @return The internal id.
     */
    public int getID() {
        return this.id;
    }
    
    /**
     * Exports the internal id as a string.
     * 
     * @return The String representation of the internal id.
     * @see de.xplib.nexd.store.InternalIdI#export()
     */
    public String export() {
        return String.valueOf(id);
    }

}
