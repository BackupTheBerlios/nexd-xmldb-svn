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
 * $Log: ResourceTable.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.resource;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import de.xplib.xdbm.ui.action.CopyResourceAction;
import de.xplib.xdbm.ui.action.CutResourceAction;
import de.xplib.xdbm.ui.action.DeleteResourceAction;
import de.xplib.xdbm.ui.action.PasteResourceAction;
import de.xplib.xdbm.ui.action.SaveResourceAction;
import de.xplib.xdbm.util.PopupListener;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ResourceTable extends JTable {
    
    private ResourceTableModel model = new ResourceTableModel();
    
    /**
     * 
     */
    public ResourceTable() {
        super();
                
        this.setModel(this.model);
        
        this.initListener();
        this.initUI();
    }
    
    /**
     * <Some description here>
     * 
     * 
     * @see java.awt.Component#repaint()
     */
    public void repaint() {
        
        Component parent = this.getParent();
        if (parent != null && this.getHeight() < parent.getHeight()) {
            
            int rc = this.getRowCount();
            int rh = this.getRowHeight();
            int ph = this.getParent().getHeight();
        
            int add = ((ph - (rc * rh)) / rh) + 1;
            for (int i = 0; i < add; i++) {
                this.model.rows.add(new EmptyResourceObject());
            }
        }
        super.repaint();
    }
    
    /**
     * 
     */
    private void initListener() {
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.getSelectionModel().addListSelectionListener(
                new RowSelectionListener(this.model));
        
        this.addMouseListener(new PopupListener(this.popup));
        this.addMouseListener(new RightSelectListener(this));
        
        InputMap ip  = this.getInputMap();
        ActionMap ap = this.getActionMap();
        
        Action a = CutResourceAction.INSTANCE;
        ip.put((KeyStroke) a.getValue(Action.ACCELERATOR_KEY), "cut.resource.action");
        ap.put("cut.resource.action", a);
        
        a = CopyResourceAction.INSTANCE;
        ip.put((KeyStroke) a.getValue(Action.ACCELERATOR_KEY), "copy.resource.action");
        ap.put("copy.resource.action", a);
        
        a = PasteResourceAction.INSTANCE;
        ip.put((KeyStroke) a.getValue(Action.ACCELERATOR_KEY), "paste.resource.action");
        ap.put("paste.resource.action", a);
        
        a = DeleteResourceAction.INSTANCE;
        ip.put((KeyStroke) a.getValue(Action.ACCELERATOR_KEY), "delete.resource.action");
        ap.put("delete.resource.action", a);
        
        a = SaveResourceAction.INSTANCE;
        ip.put((KeyStroke) a.getValue(Action.ACCELERATOR_KEY), "save.resource.action");
        ap.put("save.resource.action", a);
        
    }
    
    /**
     * 
     */
    private void initUI() {
        this.setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
        TableColumnModel model = this.getColumnModel();
        TableColumn column;
        for (int i = 0; i < model.getColumnCount(); i++) {
            column = model.getColumn(i);
            if (i == 0) {
                column.setWidth(20);
                column.setMaxWidth(20);
                column.setMinWidth(20);
                column.setPreferredWidth(20);
            } else if (i == 1 || i == 2) {
                column.setWidth(150);
                column.setMaxWidth(200);
                column.setPreferredWidth(150);
            }
        }
        
        this.add(this.popup);
    }
    
    protected ResourcePopupMenu popup = new ResourcePopupMenu();
    
    
}
