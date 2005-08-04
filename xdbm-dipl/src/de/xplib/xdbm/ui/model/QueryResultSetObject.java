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
 * $Log: QueryResultSetObject.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class QueryResultSetObject implements CollectionObject, UIObject {
    
    private Collection coll;
    
    private Object userObject;
    
    private String content = "";
    
    private QueryResultObject[] objects = new QueryResultObject[0];

    /**
     * 
     */
    public QueryResultSetObject(final Collection collIn,
                                final ResourceSet setIn) {
        super();

        this.coll       = collIn;
        this.userObject = setIn;
        
        try {
            
            int size = (int) setIn.getSize();
            
            this.objects = new QueryResultObject[size];
            
            for (int i = 0; i < size; i++) {
                this.objects[i] = new QueryResultObject(
                        this.coll, (XMLResource) setIn.getResource(i), this);
            }
        } catch (XMLDBException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @return
     */
    public QueryResultObject[] getResultObjects() {
        return this.objects;
    }
    
    public String getContent() {
        
        return this.content;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.CollectionObject#getCollection()
     */
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
    
    //public 
    
    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#isChanged()
     */
    public boolean isChanged() {
        return false;
    }
    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#isDeleted()
     */
    public boolean isDeleted() {
        return false;
    }
    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#isNew()
     */
    public boolean isNew() {
        return false;
    }
    
    /**
     * <Some description here>
     * 
     * @param b
     * @see de.xplib.xdbm.ui.model.UIObject#setChanged(boolean)
     */
    public void setChanged(boolean b) {
    }
    
    /**
     * <Some description here>
     * 
     * 
     * @see de.xplib.xdbm.ui.model.UIObject#setDeleted()
     */
    public void setDeleted() {
    }
    
    /**
     * <Some description here>
     * 
     * @param b
     * @see de.xplib.xdbm.ui.model.UIObject#setNew(boolean)
     */
    public void setNew(boolean b) {
    }
}
