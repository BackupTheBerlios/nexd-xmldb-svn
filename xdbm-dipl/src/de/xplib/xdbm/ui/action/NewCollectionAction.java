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
 * $Log: NewCollectionAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.NewCollectionDialog;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.model.VCLSchemaObject;
import de.xplib.xdbm.ui.model.VirtualCollectionObject;
import de.xplib.xdbm.ui.model.VirtualResourceObject;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NewCollectionAction extends I18NAction implements UIObjectObserver {
    
    /**
     * GoF Flyweight instance.
     */
    public static final NewCollectionAction INSTANCE 
        = new NewCollectionAction();

    /**
     * 
     */
    public NewCollectionAction() {
        super("action.object.new.collection");
        
        this.setEnabled(false);
        
        this.putValue(SMALL_ICON, Application.createIcon(
                "icon/action.new.collection.png"));
        
        Application.getInstance().addObserver(this);
    }

    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {

        if (!(objectIn instanceof VirtualResourceObject)
                && !(objectIn instanceof VirtualCollectionObject)
                && !(objectIn instanceof VCLSchemaObject)
                && objectIn != null) {
            
            this.setEnabled(true);
        } else {
            this.setEnabled(false);
        }
    }

    /**
     * <Some description here>
     * 
     * @param e
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        NewCollectionDialog dialog = new NewCollectionDialog();
        dialog.setVisible(true);
    }

}
