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
 * $Log: QueryEditorPlugin.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.query;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.ApplicationMenuBar;
import de.xplib.xdbm.ui.Plugin;
import de.xplib.xdbm.ui.widgets.InternalFrame;
import de.xplib.xdbm.ui.widgets.InternalFramePanel;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class QueryEditorPlugin implements Plugin {
    
    private QueryEditorPanel panel = null;

    /**
     * 
     */
    public QueryEditorPlugin() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.Plugin#getTitle()
     */
    public String getTitle() {
        return "Query Editor";
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.Plugin#hasPanel()
     */
    public boolean hasPanel() {
        return true;
    }

    /**
     * <Some description here>
     * 
     * @param app
     * @return
     * @see de.xplib.xdbm.ui.Plugin#getPanelInstance(de.xplib.xdbm.ui.Application)
     */
    public InternalFramePanel getPanelInstance(Application app) {
        if (this.panel == null) {
            this.panel = new QueryEditorPanel(app);
        }
        return this.panel;
    }

    public String getPosition() {
        return InternalFrame.CENTER;
    }


    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.Plugin#hasDialog()
     */
    public boolean hasDialog() {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     * <Some description here>
     * 
     * @param appIn
     * @see de.xplib.xdbm.ui.Plugin#openDialog(de.xplib.xdbm.ui.Application)
     */
    public void openDialog(Application appIn) {
    }
    
    /**
     * <Some description here>
     * 
     * @param menuBarIn
     * @see de.xplib.xdbm.ui.Plugin#setUpMenu(de.xplib.xdbm.ui.ApplicationMenuBar)
     */
    public void setUpMenu(ApplicationMenuBar menuBarIn) {
        
        JMenu query = menuBarIn.getQueryMenu();

        query.addSeparator();
        query.add(new JMenuItem("Query Editor öffnen."));
        
    }
}
