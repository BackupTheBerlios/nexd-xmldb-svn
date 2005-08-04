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
 * $Log: SaveResourceAction.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.dialog.StandardDialogFactory;
import de.xplib.xdbm.ui.model.ResourceObject;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class SaveResourceAction extends I18NAction implements UIObjectObserver {
    
    public static final SaveResourceAction INSTANCE
        = new SaveResourceAction();
    
    private ResourceObject resObj = null;

    /**
     * 
     */
    public SaveResourceAction() {
        super("action.object.resource.save");

        this.setEnabled(false);
        this.putValue(SMALL_ICON, Application.createIcon(
                "icon/action.save.generic.png"));
        
        Application.getInstance().addObserver(this);
    }

    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {
        if (objectIn instanceof ResourceObject) {
            this.resObj = (ResourceObject) objectIn;
            this.setEnabled(true);
        } else {
            this.resObj = null;
            this.setEnabled(false);
        }
    }

    /**
     * <Some description here>
     * 
     * @param aeIn
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent aeIn) {

        if (this.resObj == null) {
            StandardDialogFactory.showNoResourceDialog();
            return;
        }

        JFileChooser jfc = new JFileChooser();
        jfc.setSelectedFile(new File(this.resObj.getId()));
        
        int ret = jfc.showSaveDialog(Application.getInstance());
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            
            try {
                FileWriter fw = new FileWriter(f);
                fw.write(this.resObj.getContent());
                fw.flush();
                fw.close();
            } catch (IOException e) {
                StandardDialogFactory.showErrorMessageDialog(e);
            }
        }
    }

}
