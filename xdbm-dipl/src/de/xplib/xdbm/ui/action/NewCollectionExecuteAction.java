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
 * $Log: NewCollectionExecuteAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.text.JTextComponent;

import org.sixdml.dbmanagement.SixdmlCollectionManagementService;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.TreeObject;
import de.xplib.xdbm.ui.model.TreeObjectFactory;
import de.xplib.xdbm.ui.model.XapiCollectionObject;
import de.xplib.xdbm.ui.widgets.JFileField;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NewCollectionExecuteAction extends I18NErrorAction {
    
    /**
     * GoF Flyweight instance of NewCollectionExecuteAction.
     */
    public static final NewCollectionExecuteAction INSTANCE 
        = new NewCollectionExecuteAction();
    
    /**
     * Comment for <code>coll</code>
     */
    private Collection coll = null;
    
    /**
     * Comment for <code>jtcCollName</code>
     */
    private JTextComponent jtcCollName = null;
    
    /**
     * Comment for <code>jffSchema</code>
     */
    private JFileField jffSchema = null;
    
    /**
     * 
     */
    public NewCollectionExecuteAction() {
        super("action.object.new.collection.exec");
        // TODO Auto-generated constructor stub
    }

    /**
     * @param collIn
     */
    public void setCollection(final Collection collIn) {
        this.coll = collIn;
        
        // unset error if we get a new collection reference.
        this.error = false;
    }
    
    /**
     * @param jtCompIn
     */
    public void setCollectionNameComponent(final JTextComponent jtCompIn) {
        this.jtcCollName = jtCompIn;
    }
    
    /**
     * @param jffCompIn
     */
    public void setSchemaComponent(final JFileField jffCompIn) {
        this.jffSchema = jffCompIn;
    }

    /**
     * <Some description here>
     * 
     * @param e
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        
        Application app = Application.getInstance();
        
        if (this.coll == null) {
            this.message = this.i18n.getText("dialog.collection.new.parent");
            this.error   = true;
            return;
        }
        try {
            if (!this.coll.isOpen()) {
                this.message = this.i18n.getText("app.err.msg.closed");
                this.error   = true;
                return;
            }
        } catch (XMLDBException e1) {
            this.message = e1.getMessage();
            this.error = true;
            return;
        }
        if (this.jtcCollName == null) {
            
            this.message = this.i18n.getText("app.err.msg.unknown");
            this.error   = true;
            return;
        }
        
        
        try {
            
            Collection child;
            String name = this.jtcCollName.getText().trim();
            
            if (this.jffSchema != null) {
                SixdmlCollectionManagementService scms = 
                    (SixdmlCollectionManagementService) this.coll.getService(
                            "SixdmlCollectionManagementService", "1.0");
                
                String schema = this.jffSchema.getText().trim();
                
                if (!schema.equals("")) {
                    if (!schema.matches("^[a-zA-Z]://")) {
                        schema = "file://" + schema;
                    }
                    
                    child = scms.createCollection(name, new URL(schema));
                } else {
                    child = scms.createCollection(name);
                }
            } else {
                CollectionManagementService cms = (CollectionManagementService)
                    this.coll.getService("CollectionManagementService", "1.0");
                
                child = cms.createCollection(name);
            }
            
            
            // unset all
            this.coll = null;
            this.error = false;
            this.message = null;
            this.jtcCollName = null;
            this.jffSchema = null;
            
            TreeObject to = TreeObjectFactory.getInstance().createTreeObject(
                    child);
            
            ((XapiCollectionObject) to).setNew(true);
            Application.getInstance().setUIObject(to);
            
            //new XapiCollectionObject()
        } catch (Exception e2) {
            
            e2.printStackTrace(Application.out);
            
            this.message = this.i18n.getText(
                    "app.err.msg.dump", new String[] {e2.getMessage()});
            this.error = true;
            return;
        }
    }

}
