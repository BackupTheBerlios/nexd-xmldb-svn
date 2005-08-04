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
 * $Log: ShowPCVResourceAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.editor.EditorPanel;
import de.xplib.xdbm.ui.model.PCVirtualResourceObject;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.model.VirtualResourceObject;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ShowPCVResourceAction extends AbstractVirtualI18NAction {
    
    public static final ShowPCVResourceAction INSTANCE 
        = new ShowPCVResourceAction();
        
    private VirtualResourceObject vresObj = null;

    /**
     */
    public ShowPCVResourceAction() {
        super("action.vresource.show.pcvr");

        this.setEnabled(false);
        
        Application.getInstance().addObserver(this);
    }

    /**
     * <Some description here>
     * 
     * @param aeIn
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent aeIn) {

        if (this.vresObj == null) {
            StandardDialogFactory.showNoResourceDialog();
            return;
        }

        EditorPanel ip  = (EditorPanel) Application.openPanel("editor");
        if (ip != null) {
            PCVirtualResourceObject pcvro = this.vresObj.getPCVResource();
            if (pcvro == null) {
                StandardDialogFactory.showErrorMessageDialog(new Exception("is null"));
            } else {
                ip.setText(pcvro.getContent());
            }
        }
    }

    
    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(final UIObject objectIn) {
        
        this.checkVirtualCollection(objectIn);
        
        if (objectIn instanceof VirtualResourceObject) {
            this.vresObj = (VirtualResourceObject) objectIn;
            this.setEnabled(true);
        } else {
            this.vresObj = null;
            this.setEnabled(false);
        }
    }
}
