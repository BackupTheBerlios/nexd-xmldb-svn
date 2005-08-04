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
 * $Log: EmptyResourceObject.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.resource;

import org.xmldb.api.base.Collection;

import de.xplib.xdbm.ui.model.ResourceObject;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class EmptyResourceObject implements ResourceObject {

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.ResourceObject#getId()
     */
    public String getId() {
        return "";
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.ResourceObject#getType()
     */
    public String getType() {
        return "";
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.ResourceObject#getContent()
     */
    public String getContent() {
        return "";
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.ResourceObject#getCollection()
     */
    public Collection getCollection() {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#getUserObject()
     */
    public Object getUserObject() {
        return null;
    }
    
    public boolean isChanged() {
        return false;
    }
    
    public void setChanged(final boolean b) {
    }

    public boolean isDeleted() {
        return false;
    }
    
    public void setDeleted() {        
    }
    
    public boolean isNew() {
        return false;
    }
    
    public void setNew(final boolean b) {
    }

}
