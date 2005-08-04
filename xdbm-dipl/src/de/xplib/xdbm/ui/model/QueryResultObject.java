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
 * $Log: QueryResultObject.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.XMLResource;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class QueryResultObject 
    extends XMLResourceObject 
    implements CollectionObject {
    
    public static final String RESOURCE_TYPE = "QueryResult";
    
    private QueryResultSetObject qrso;

    /**
     * 
     */
    public QueryResultObject(final Collection collIn,
                             final XMLResource resIn,
                             final QueryResultSetObject qrsoIn) {
        super(collIn, resIn);
       
        this.type = RESOURCE_TYPE;
        
        this.qrso = qrsoIn;
    }
    
    /**
     * @return
     */
    public QueryResultSetObject getQueryResultSet() {
        return this.qrso;
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
     * 
     * @see de.xplib.xdbm.ui.model.UIObject#setDeleted()
     */
    public void setDeleted() {
    }

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
     * @param b
     * @see de.xplib.xdbm.ui.model.UIObject#setChanged(boolean)
     */
    public void setChanged(boolean b) {
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
     * @see de.xplib.xdbm.ui.model.UIObject#setNew(boolean)
     */
    public void setNew(boolean b) {
    }

}
