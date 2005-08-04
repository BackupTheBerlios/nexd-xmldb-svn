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
 * $Log: DeleteCollectionAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.sixdml.SixdmlConstants;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.ResourceObject;
import de.xplib.xdbm.ui.model.TreeObject;
import de.xplib.xdbm.ui.model.TreeObjectFactory;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class DeleteCollectionAction 
    extends I18NAction 
    implements UIObjectObserver {
    
    /**
     * GoF Flyweight instance of DeleteCollectionAction
     */
    public static final DeleteCollectionAction INSTANCE 
        = new DeleteCollectionAction();

    /**
     * Comment for <code>coll</code>
     */
    private Collection coll = null;
    
    /**
     * 
     */
    public DeleteCollectionAction() {
        super("action.object.delete.collection");
        
        Application.getInstance().addObserver(this);

        this.setEnabled(false);
        this.putValue(SMALL_ICON, Application.createIcon(
        		"icon/action.delete.object.png"));
    }

    /**
     * <Some description here>
     * 
     * @param aeIn
     * @see java.awt.event.ActionListener#actionPerformed(
     *      java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent aeIn) {

        if (this.coll == null) {
            JOptionPane.showMessageDialog(
                    Application.getInstance(),
                    this.i18n.getText("dialog.collection.delete.err.nocoll"),
                    this.i18n.getTitle("app.err.title"),
                    JOptionPane.ERROR_MESSAGE);
            
            return;
        }
        
        try {
            Service s = this.coll.getParentCollection().getService(
                    "SixdmlCollectionManagementService", 
                    SixdmlConstants.SIXDML_VERSION);
            if (s == null) {
                s = this.coll.getService("CollectionManagementService", "1.0");
            }
            
            if (s == null) {
                JOptionPane.showMessageDialog(
                        Application.getInstance(),
                        this.i18n.getText("dialog.collection.delete.err.noservice"),
                        this.i18n.getTitle("app.err.title"),
                        JOptionPane.ERROR_MESSAGE);
                
                return;
            }
            
            int res = JOptionPane.showConfirmDialog(
                    Application.getInstance(), 
                    this.i18n.getText(
                            "dialog.collection.delete.msg", 
                            new String[] {this.coll.getName()}),
                    this.i18n.getTitle("dialog.collection.delete.title"),
                    JOptionPane.YES_NO_OPTION);
            
            if (res == JOptionPane.YES_OPTION) {
                CollectionManagementService cms = 
                    (CollectionManagementService) s;
            
                cms.removeCollection(this.coll.getName());
                
                TreeObject to = TreeObjectFactory.getInstance()
                                                 .createTreeObject(this.coll, true);
System.out.println(to.isDeleted());
                Application.getInstance().setUIObject(to);
            }
        } catch (XMLDBException e) {
            JOptionPane.showMessageDialog(
                    Application.getInstance(),
                    this.i18n.getText(
                            "app.err.msg.dump", new String[] {e.getMessage()}),
                    this.i18n.getTitle("app.err.title"),
                    JOptionPane.ERROR_MESSAGE);            
        }
    }
    
    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(
     *      de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {
        
        this.coll = null;

        if (objectIn instanceof TreeObject) {
            Object o = objectIn.getUserObject();
            if (o instanceof Collection && !objectIn.isDeleted()) {
                this.coll = (Collection) o;
                this.checkCollection(this.coll);
            } else {
                this.setEnabled(false);
            }
        } else if (objectIn instanceof ResourceObject) {
            this.coll = ((ResourceObject) objectIn).getCollection();
            this.checkCollection(this.coll);
        } else {
            this.setEnabled(false);
        }
    }
    
    
    private void checkCollection(final Collection collIn) {
        try {
            if (collIn.getName().equals("db") 
                    && collIn.getParentCollection() == null) {
                
                this.setEnabled(false);
            } else {
                this.setEnabled(true);
            }
        } catch (XMLDBException e) {
            this.setEnabled(false);
            e.printStackTrace(Application.err);
        }
    }
}
