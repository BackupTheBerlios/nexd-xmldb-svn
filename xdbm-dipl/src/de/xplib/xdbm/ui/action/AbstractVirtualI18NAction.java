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
 * $Log: AbstractVirtualI18NAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.VirtualCollectionManagementService;
import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.CollectionObject;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractVirtualI18NAction extends I18NAction implements UIObjectObserver {
    
    private boolean provides = false;
    
    private boolean done = false;

    /**
     * @param keyIn
     */
    public AbstractVirtualI18NAction(String keyIn) {
        super(keyIn);
    }
    
    protected final boolean checkVirtualCollection(final UIObject objectIn) {
        
        if (this.done) {
            return this.provides;
        }
        
        if (objectIn instanceof CollectionObject) {
            Collection c = ((CollectionObject) objectIn).getCollection();
            if (!this.provides) {
                if (c instanceof VirtualCollection) {
                    this.provides = true;
                } else {
                    try {
                        Service s = c.getService(
                                "VirtualCollectionManagementService", "1.0");
                        if (s != null && s instanceof VirtualCollectionManagementService) {
                            this.provides = true;
                        }
                    } catch (XMLDBException e) {
                    }
                }
                if (!this.provides) {
                    Application.getInstance().removeObserver(this);
                }
                this.done = true;
            }
        } else {
            return true;
        }
        
        return this.provides;
    }

}
