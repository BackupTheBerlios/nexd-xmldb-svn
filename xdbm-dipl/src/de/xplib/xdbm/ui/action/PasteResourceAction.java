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
 * $Log: PasteResourceAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.model.CollectionObject;
import de.xplib.xdbm.ui.model.ResourceObject;
import de.xplib.xdbm.ui.model.ResourceObjectFactory;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class PasteResourceAction extends I18NAction implements UIObjectObserver {
    
    /**
     * GoF Flyweight instance of PasteResourceAction.
     */
    public static final PasteResourceAction INSTANCE
        = new PasteResourceAction();
    
    private Collection target = null;
    
    private ResourceObject resObj = null;
    
    private boolean copy = true;

    /**
     * @param keyIn
     */
    private PasteResourceAction() {
        super("action.object.resource.paste");

        Application.getInstance().addObserver(this);
        
        this.putValue(SMALL_ICON, Application.createIcon(
                "icon/action.paste.generic.png"));
        this.setEnabled(false);
    }
    
    public void setCopyObject(ResourceObject objIn) {
        this.resObj = objIn;
        this.copy   = true;
        this.setEnabled(this.target != null);
    }
    
    public void setCutObject(ResourceObject objIn) {
        this.resObj = objIn;
        this.copy   = false;
        this.setEnabled(this.target != null);
    }

    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {
        
        if (objectIn != null && objectIn.isDeleted()) {
            if (objectIn == this.resObj) {
                this.resObj = null;
                this.setEnabled(false);
                return;
            } else if (objectIn == this.target) {
                this.target = null;
                this.setEnabled(false);
                return;
            }
        } else if (objectIn instanceof CollectionObject) {
            Collection c = (Collection) ((CollectionObject) objectIn).getCollection();
            if (!(c instanceof VirtualCollection)) {
                this.target = c;
                this.setEnabled(this.resObj != null);
                return;
            }
        }
        this.target = null;
        this.setEnabled(false);
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
        if (this.resObj == null) {
            StandardDialogFactory.showNoResourceDialog();
            return;
        }

        Resource res = (Resource) this.resObj.getUserObject();
        
        try {
            String rid = res.getId();
            String id = null;
            if (this.target.getResource(rid) == null) {
                id = rid;
            } else if (this.target.getResource("Copy_Of_" + rid) == null) {
                id = "Copy_Of_" + rid;
            }
            rid = "_Copy_Of_" + rid;
            int i = 0;
            while (id == null && i < 100) {
                if (this.target.getResource((++i) + rid) == null) {
                    id = i + rid;
                }
            }
            
            int cnt = this.target.listResources().length;
            
            Resource copy = this.target.createResource(id, res.getResourceType());
            copy.setContent(res.getContent());
            this.target.storeResource(copy);
                        
            if (!this.copy) {
                try {
                    res.getParentCollection().removeResource(res);
                    
                    this.resObj.setDeleted();
                    
                    Application.getInstance().setUIObject(this.resObj);
                } catch (XMLDBException e) {
                    // fail try to remove copy again.
                    this.target.removeResource(copy);
                }
            }
            
            if (this.target.listResources().length == cnt + 1) {
                ResourceObject copyObj = ResourceObjectFactory.getInstance()
                                         .createResourceRow(this.target, copy);
            	copyObj.setNew(true);
            	Application.getInstance().setUIObject(copyObj);
            }
        } catch (XMLDBException e) {
            StandardDialogFactory.showErrorMessageDialog(e);
        }
    }

}
