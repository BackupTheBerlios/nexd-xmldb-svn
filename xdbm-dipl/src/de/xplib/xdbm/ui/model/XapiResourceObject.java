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
 * $Log: XapiResourceObject.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public abstract class XapiResourceObject implements ResourceObject {
    
    protected String id = "";
    
    protected String type = "";
    
    protected Collection coll = null;
    
    protected Resource userObject = null;

    /**
     * 
     */
    public XapiResourceObject(final Collection collIn,
                              final Resource resIn,
                              final String typeIn) {
        super();
        
        
        this.coll       = collIn;
        this.userObject = resIn;
        
        this.type = typeIn;
        try {
            this.id = resIn.getId();
        } catch (XMLDBException e) {
            e.printStackTrace(Application.err);
        }
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.ResourceObject#getId()
     */
    public String getId() {
        return this.id;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.ResourceObject#getType()
     */
    public String getType() {
        return this.type;
    }
    
    public Collection getCollection() {
        return this.coll;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#getUserObject()
     */
    public Object getUserObject() {
        return this.userObject;
    }
    
    private boolean deleted = false;
    private boolean changed = false;
    private boolean isNew = false;
    
    public boolean isChanged() {
        return this.changed;
    }
    
    public void setChanged(final boolean changedIn) {
        this.changed = changedIn;
    }

    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted() {
        this.deleted = true;
    }
    
    public boolean isNew() {
        return this.isNew;
    }
    
    public void setNew(final boolean newIn) {
        this.isNew = newIn;
    }

}
