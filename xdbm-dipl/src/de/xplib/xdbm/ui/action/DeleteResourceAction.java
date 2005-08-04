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
 * $Log: DeleteResourceAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.ExecuteProcessDialog;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.model.QueryResultObject;
import de.xplib.xdbm.ui.model.ResourceObject;
import de.xplib.xdbm.ui.model.ResourceObjectFactory;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.model.VirtualResourceObject;
import de.xplib.xdbm.util.ExecuteProgressThread;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class DeleteResourceAction 
    extends I18NAction 
    implements UIObjectObserver {
    
    private ResourceObject resObj = null;
    
    /**
     * GoF Flyweight instance of DeleteResourceAction.
     */
    public static final DeleteResourceAction INSTANCE
        = new DeleteResourceAction();

    /**
     * 
     */
    public DeleteResourceAction() {
        super("action.object.delete.resource");
        
        Application.getInstance().addObserver(this);
        
        this.setEnabled(false);
        this.putValue(SMALL_ICON, Application.createIcon(
                "icon/action.delete.object.png"));
    }

    /**
     * <Some description here>
     * 
     * @param aeIn
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent aeIn) {

        if (this.resObj == null || this.resObj.getUserObject() == null) {
            JOptionPane.showMessageDialog(
                    Application.getInstance(),
                    this.i18n.getText("dialog.error.nores"),
                    this.i18n.getText("app.err.title"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }        
        
        Resource res    = (Resource) this.resObj.getUserObject();
        Collection coll = this.resObj.getCollection();
        if (coll == null) {
            JOptionPane.showMessageDialog(
                    Application.getInstance(),
                    this.i18n.getText("dialog.error.nocoll"),
                    this.i18n.getText("app.err.title"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            
            int ret = JOptionPane.showConfirmDialog(
                    Application.getInstance(), 
                    this.i18n.getText(
                            "dialog.resource.delete.msg", 
                            new String[] {res.getId()}),
                    this.i18n.getTitle("dialog.resource.delete.title"),
                    JOptionPane.YES_NO_OPTION);
            
            if (ret == JOptionPane.NO_OPTION) {
                return;
            }
            
            new DeleteResourceProcessThread(
                    new ExecuteProcessDialog("dialog.resource.delete.exec"),
                    coll, res).start();
            /*
            coll.removeResource(res);
            
            ResourceObject ro = ResourceObjectFactory
                                        .getInstance()
                                        .createResourceRow(coll, res);
            ro.setDeleted();
            
            Application.getInstance().setUIObject(ro);
            */
        } catch (XMLDBException e) {
            StandardDialogFactory.showErrorMessageDialog(e);
        }
    }

    
    
    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(final UIObject objectIn) {

        this.resObj = null;
        if (objectIn instanceof ResourceObject && !(objectIn instanceof QueryResultObject)) {
            if (objectIn instanceof VirtualResourceObject) {
                this.setEnabled(false);
            } else {
                
                this.resObj = (ResourceObject) objectIn;
                this.setEnabled(true);
            }
        } else {
            this.setEnabled(false);
        }
    }
    
    class DeleteResourceProcessThread extends ExecuteProgressThread {
        
        private Collection ctxColl;
        
        private Resource delRes;
        
        public DeleteResourceProcessThread(final ExecuteProcessDialog dialogIn,
                                           final Collection ctxCollIn,
                                           final Resource delResIn) {
            
            super(dialogIn);
            
            this.ctxColl = ctxCollIn;
            this.delRes  = delResIn;
        }
        
        /**
         * <Some description here>
         * 
         * @throws Exception
         * @see de.xplib.xdbm.util.ExecuteProgressThread#doPostRun()
         */
        protected void doPostRun() throws Exception {
            ResourceObject ro = ResourceObjectFactory.getInstance()
            										 .createResourceRow(ctxColl, delRes);
            ro.setDeleted();
            Application.getInstance().setUIObject(ro);
        }
        
        /**
         * <Some description here>
         * 
         * @throws Exception
         * @see de.xplib.xdbm.util.ExecuteProgressThread#doRun()
         */
        protected void doRun() throws Exception {
            ctxColl.removeResource(delRes);
        }
    }
}
