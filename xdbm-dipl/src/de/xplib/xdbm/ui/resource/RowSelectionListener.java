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
 * $Log: RowSelectionListener.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.resource;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.ResourceObject;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class RowSelectionListener implements ListSelectionListener {
    
    private Application app = Application.getInstance();
    
    private ResourceObject obj = null;
    
    /**
     * Comment for <code>model</code>
     */
    private ResourceTableModel model = null;

    /**
     * 
     */
    public RowSelectionListener(final ResourceTableModel modelIn) {
        super();
        
        this.model = modelIn;
    }

    /**
     * <Some description here>
     * 
     * @param e
     * @see javax.swing.event.ListSelectionListener#valueChanged(
     *      javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e) {
        
        if (e.getValueIsAdjusting()) {
            return;
        }
        
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        int row = lsm.getMinSelectionIndex();
        if (row == -1) {
            return;
        }

		ResourceObject ro = this.model.getRow(row);

        if (ro != this.app.getUIObject()) {
            if (!(this.app.getUIObject() instanceof ResourceObject) && ro == null) {
                return;
            }
            this.app.setUIObject(ro);
        }
        
    }

}
