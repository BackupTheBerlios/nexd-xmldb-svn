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
 * $Log: NewVirtualCollectionAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.VirtualCollectionManagementService;
import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.NewVirtualCollectionDialog;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.model.CollectionObject;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NewVirtualCollectionAction 
    extends I18NAction 
    implements UIObjectObserver {
    
    /**
     * GoF Flyweight instance of NewVirtualCollectionAction.
     */
    public static final NewVirtualCollectionAction INSTANCE
        = new NewVirtualCollectionAction();
    
    private boolean provides = false;
    
    private Collection target = null;

    /**
     */
    public NewVirtualCollectionAction() {
        super("action.object.new.vcollection");
        
        this.setEnabled(false);
        this.putValue(SMALL_ICON, Application.createIcon(
                "icon/action.new.virtual.collection.png"));
        
        Application.getInstance().addObserver(this);
    }

    /**
     * <Some description here>
     * 
     * @param aeIn
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent aeIn) {

        if (this.target == null) {
            StandardDialogFactory.showNoCollectionDialog();
            return;
        }
        
        NewVirtualCollectionDialog dialog = new NewVirtualCollectionDialog(
                this.target);
        dialog.setVisible(true);
    }

    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(final UIObject objectIn) {
        // TODO Auto-generated method stub

        this.target = null;
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
                    return;
                }
            }
            
            if (!(c instanceof VirtualCollection)) {
                this.target = c;
                this.setEnabled(true);
            } else {
                this.setEnabled(false);
            }
        }
        
    }
}
