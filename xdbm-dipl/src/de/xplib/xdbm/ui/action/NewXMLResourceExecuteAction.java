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
 * $Log: NewXMLResourceExecuteAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.ExecuteProcessDialog;
import de.xplib.xdbm.ui.model.ResourceObject;
import de.xplib.xdbm.ui.model.ResourceObjectFactory;
import de.xplib.xdbm.util.ExecuteProgressThread;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NewXMLResourceExecuteAction extends I18NErrorAction {
    
    /**
     * GoF Flyweight instance of NewXMLResourceExecuteAction.
     */
    public static final NewXMLResourceExecuteAction INSTANCE
        = new NewXMLResourceExecuteAction();
    
    
    private Collection coll = null;
    
    private String file = "";
    
    private String id = "";

    /**
     * 
     */
    public NewXMLResourceExecuteAction() {
        super("action.object.new.xml.resource.exec");
        // TODO Auto-generated constructor stub
    }
    
    

    /**
     * @param coll The coll to set.
     */
    public final void setColl(Collection coll) {
        this.coll = coll;
    }
    
    public final void setId(String id) {
        this.id = id.trim();
    }
    
    /**
     * @param file The file to set.
     */
    public final void setFile(String file) {
        this.file = file.trim();
    }
    
    /**
     * <Some description here>
     * 
     * @param aeIn
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent aeIn) {

        if (this.coll == null) {
            JOptionPane.showMessageDialog(
                    Application.getInstance(),
                    i18n.getText("dialog.resource.xml.new.err.nocoll"),
                    i18n.getTitle("app.err.title"), 
                    JOptionPane.ERROR_MESSAGE);
            this.error = true;
            return;
        }
        
        File f = new File(this.file);
        if (!f.exists() || !f.canRead()) {
            JOptionPane.showMessageDialog(
                    Application.getInstance(),
                    i18n.getText("dialog.resource.xml.new.err.nofile",
                            new String[] {this.file}),
                    i18n.getTitle("app.err.title"), 
                    JOptionPane.ERROR_MESSAGE);
            this.error = true;
            return;
        }
        
        Document doc = null;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance()
                                                       .newDocumentBuilder();
            
            doc = db.parse(new File(this.file));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    Application.getInstance(),
                    i18n.getText("dialog.resource.xml.new.err.noxml",
                            new String[] {this.file}),
                    i18n.getTitle("app.err.title"), 
                    JOptionPane.ERROR_MESSAGE);
            this.error = true;
            return;
        }
        
        try {
            if (this.id.equals("")) {
                this.id = this.coll.createId();
            }
            
            new NewXMLResourceThread(
                    new ExecuteProcessDialog("dialog.resource.xml.new.exec"),
                    this.coll, this.id, doc).start();
            /*
            XMLResource xres = (XMLResource) this.coll.createResource(
                    this.id, XMLResource.RESOURCE_TYPE);
            xres.setContentAsDOM(doc);

            this.coll.storeResource(xres);
            
            ResourceObject ro = ResourceObjectFactory.getInstance().createResourceRow(
                    this.coll, this.coll.getResource(this.id));
            
            ro.setNew(true);
            Application.getInstance().setUIObject(ro);
            */
        } catch (XMLDBException e) {
            JOptionPane.showMessageDialog(
                    Application.getInstance(), 
                    this.i18n.getText(
                            "app.err.msg.dump", new String[] {e.getMessage()}),
                    this.i18n.getTitle("app.err.title"), 
                    JOptionPane.ERROR_MESSAGE);
            this.error = true;
            return;
        }
        
        this.error = false;
        this.coll = null;
        this.id = "";
        this.file = "";
    }

    
    
    class NewXMLResourceThread extends ExecuteProgressThread {
        
        private Collection ctxColl;
        
        private String name;
        
        private Document document;
        
        public NewXMLResourceThread(final ExecuteProcessDialog dialogIn,
                                    final Collection ctxCollIn,
                                    final String nameIn,
                                    final Document documentIn) {
            super(dialogIn);
            
            this.ctxColl  = ctxCollIn;
            this.name     = nameIn;
            this.document = documentIn;
        }
        
        
        /**
         * <Some description here>
         * 
         * @throws Exception
         * @see de.xplib.xdbm.util.ExecuteProgressThread#doPostRun()
         */
        protected void doPostRun() throws Exception {
            // TODO Auto-generated method stub
            ResourceObject ro = ResourceObjectFactory.getInstance().createResourceRow(
                    this.ctxColl, this.ctxColl.getResource(this.name));
            
            ro.setNew(true);
            Application.getInstance().setUIObject(ro);
        }
        
        /**
         * <Some description here>
         * 
         * @throws Exception
         * @see de.xplib.xdbm.util.ExecuteProgressThread#doRun()
         */
        protected void doRun() throws Exception {
            XMLResource xres = (XMLResource) this.ctxColl.createResource(
                    this.name, XMLResource.RESOURCE_TYPE);
            xres.setContentAsDOM(this.document);

            this.ctxColl.storeResource(xres);
        }
    }
}
