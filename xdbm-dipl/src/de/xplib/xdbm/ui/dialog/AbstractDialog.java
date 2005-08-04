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
 * $Log: AbstractDialog.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.dialog;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.ResourceObject;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.model.VirtualCollectionObject;
import de.xplib.xdbm.ui.model.VirtualResourceObject;
import de.xplib.xdbm.ui.model.XapiCollectionObject;
import de.xplib.xdbm.util.I18N;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractDialog extends JDialog {
    
    protected I18N i18n = I18N.getInstance();
    
    protected Collection ctxColl = null;
    
    protected String ctxPath = "";
    
    public AbstractDialog(final String titleIn, final boolean modalIn) {
        super(Application.getInstance(), titleIn, modalIn);
    }

    
    /**
     * <Some description here>
     * 
     * @param b
     * @see java.awt.Component#setVisible(boolean)
     */
    public void setVisible(boolean b) {
        
        Application app = Application.getInstance();
        
        int x = app.getX();
        int y = app.getY();
        int w = app.getWidth();
        int h = app.getHeight();
        
        int tw = this.getWidth();
        int th = this.getHeight();
                
        //this.setL;
        this.setLocation(x + ((w - tw) / 2), y + ((h - th) / 2));
        
        super.setVisible(b);
    }
    
    
    public boolean loadCollection() {
        
        Collection coll = null;
        
        Application app = Application.getInstance(); 
        UIObject o = app.getUIObject();
        if (o == null) {
            JOptionPane.showMessageDialog(
                    app, i18n.getText("dialog.collection.new.err.noparent"), 
                    i18n.getTitle("app.err.title"), JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (o instanceof VirtualCollectionObject 
                || o instanceof VirtualResourceObject) {
            JOptionPane.showMessageDialog(
                    app, i18n.getText("dialog.collection.new.err.wrongparent"),
                    i18n.getTitle("app.err.title"), JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            if (o instanceof XapiCollectionObject) {
                coll = (Collection) o.getUserObject(); 
            } else if (o instanceof ResourceObject) {
                coll = ((ResourceObject) o).getCollection();
            } else {
                JOptionPane.showMessageDialog(
                        app, i18n.getText("app.err.msg.unknown"), 
                        i18n.getTitle("app.err.title"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            try {
                String path = "/" + coll.getName();
                Collection c = coll.getParentCollection();
                while (c != null) {
                    path = "/" + c.getName() + path;
                    c = c.getParentCollection();
                }
                this.ctxPath = path;
                
            } catch (XMLDBException e) {
                e.printStackTrace(Application.err);
                return false;
            }
        }
        this.ctxColl = coll;
        return true;
    }
}
