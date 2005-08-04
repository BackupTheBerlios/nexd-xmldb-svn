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
 * $Log: ExecuteNewVirtualCollectionAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.VirtualCollectionManagementService;
import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.ExecuteProcessDialog;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.model.TreeObjectFactory;
import de.xplib.xdbm.ui.model.VirtualCollectionObject;
import de.xplib.xdbm.util.ExecuteProgressThread;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ExecuteNewVirtualCollectionAction extends I18NErrorAction {
    
    private Collection parent = null;
    
    private String name = "";
    
    private String vclSchema = "";
    
    private String xslStylesheet = "";
    
    /**
     * Comment for <code>INSTANCE</code>
     */
    public static final ExecuteNewVirtualCollectionAction INSTANCE
        = new ExecuteNewVirtualCollectionAction();

    /**
     * 
     */
    public ExecuteNewVirtualCollectionAction() {
        super("action.object.new.vcollection.exec");
        
        this.setEnabled(false);
    }
    
    public void setContext(Collection parentIn,
                           String nameIn,
                           String vclSchemaIn,
                           String xslStylesheetIn) {
        
        this.parent        = parentIn;
        this.name          = nameIn;
        this.vclSchema     = vclSchemaIn;
        this.xslStylesheet = xslStylesheetIn;
    }

    /**
     * <Some description here>
     * 
     * @param aeIn
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent aeIn) {
        
        if (this.parent == null) {
            StandardDialogFactory.showNoCollectionDialog();
            this.error = true;
            return;
        }
        if (this.name == null || this.name.equals("")) {
            StandardDialogFactory.showNoNameDialog();
            this.error = true;
            return;
        }
        if (this.vclSchema == null || this.vclSchema.equals("")) {
            StandardDialogFactory.showNoVCLSchemaDialog();
            this.error = true;
            return;
        }
        
        File f = new File(this.vclSchema);
        if (!f.exists() || !f.canRead()) {
            StandardDialogFactory.showNoReadDialog(this.vclSchema);
            this.error = true;
            return;
        }
        
        String path = this.vclSchema;
        if (path.indexOf("://") == -1) {
            path = "file://" + path;
        }
        
        URL schema;
        try {
            schema = new URL(path);
        } catch (MalformedURLException e) {
            StandardDialogFactory.showErrorMessageDialog(e);
            this.error = true;
            return;
        }
        
        URL stylesheet = null;
        if (this.xslStylesheet != null && !this.xslStylesheet.equals("")) {
            f = new File(this.xslStylesheet);
            if (!f.exists() || !f.canRead()) {
                StandardDialogFactory.showNoReadDialog(this.xslStylesheet);
                this.error = true;
                return;
            }
            
            path = this.xslStylesheet;
            if (path.indexOf("://") == -1) {
                path = "file://" + path;
            }
            
            try {
                stylesheet = new URL(path);
            } catch (MalformedURLException e) {
                StandardDialogFactory.showErrorMessageDialog(e);
                this.error = true;
                return;
            }
        }
        
        try {
            if (this.parent.getChildCollection(this.name) != null) {
                StandardDialogFactory.showSameNameDialog(this.name);
                this.error = true;
                return;
            }
            
            Service s = this.parent.getService(
                    "VirtualCollectionManagementService", "1.0");
            if (s == null || !(s instanceof VirtualCollectionManagementService)) {
                StandardDialogFactory.showNoSuchServiceDialog(
                        "VirtualCollectionManagementService");
            }
            
            Thread t = new CreateVirtualCollectionThread(
                    new ExecuteProcessDialog("dialog.vcollection.new.exec"),
                    (VirtualCollectionManagementService) s, 
                    this.name, schema, stylesheet);
            t.start();
            
            this.parent        = null;
            this.name          = null;
            this.vclSchema     = null;
            this.xslStylesheet = null;
        } catch (XMLDBException e) {
            StandardDialogFactory.showErrorMessageDialog(e);
            this.error = true;
            return;
        }
    }
    
    class CreateVirtualCollectionThread extends ExecuteProgressThread {
        
       // private ExecuteProcessDialog dialog = null;
        
        private VirtualCollectionManagementService vcms = null;
        
        private String name;
        
        private URL vclSchema;
        
        private URL xslStylesheet;
        
        private VirtualCollection vcoll;
        
        public CreateVirtualCollectionThread(
                ExecuteProcessDialog dialogIn,
                VirtualCollectionManagementService vcmsIn,
                String nameIn,
                URL vclSchemaIn,
                URL xslStylesheetIn) {
            
            super(dialogIn);
            
            this.vcms   = vcmsIn;
            this.name   = nameIn;
            this.vclSchema = vclSchemaIn;
            this.xslStylesheet = xslStylesheetIn;
        }
        
        protected void doRun() throws Exception {
            if (this.xslStylesheet == null) {
                vcoll = vcms.createVirtualCollection(this.name, this.vclSchema);
            } else {
                vcoll = vcms.createVirtualCollection(this.name, this.vclSchema, this.xslStylesheet);
            }
        }
        
        /**
         * @throws Exception
         */
        protected void doPostRun() throws Exception {
            
            VirtualCollectionObject vco = TreeObjectFactory.getInstance()
                                                           .createTreeObject(vcoll);
            vco.setNew(true);
            Application.getInstance().setUIObject(vco);
        }
    }
}
