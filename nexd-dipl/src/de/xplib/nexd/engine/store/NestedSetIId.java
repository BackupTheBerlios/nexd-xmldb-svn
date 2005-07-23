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
 * $Log: NestedSetIId.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
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
 * @version $Revision: 1.3 $
 */
public class NestedSetIId implements InternalIdI {
    
    /**
     * The nestet set left value.
     */
    private final int lft;
    
    /**
     * The nestet set right value.
     */
    private final int rgt;
    
    /**
     * The internal object id.
     */
    private final int id;

    /**
     * Constructor
     * 
     * @param lftIn The nestet set left value.
     * @param rgtIn The nestet set right value.
     * @param oidIn The internal object id.
     */
    public NestedSetIId(final int lftIn, final int rgtIn, final int oidIn) {
        super();
        
        this.lft = lftIn;
        this.rgt = rgtIn;
        this.id = oidIn;
    }
    
    /**
     * Returns the nestet set left value.
     * @return The nestet set left value.
     */
    public int getLft() {
        return this.lft;
    }
    
    /**
     * Returns the nestet set right value.
     * 
     * @return The nestet set right value.
     */
    public int getRgt() {
        return this.rgt;
    }
    
    /**
     * Returns the internal object id.
     * 
     * @return The internal object id.
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Exports the internal id as a string.
     * 
     * @return String representation of the internal id. 
     * @see de.xplib.nexd.store.InternalIdI#export()
     */
    public String export() {
        return this.id + ":" + this.lft + ":" + this.rgt;
    }

}
