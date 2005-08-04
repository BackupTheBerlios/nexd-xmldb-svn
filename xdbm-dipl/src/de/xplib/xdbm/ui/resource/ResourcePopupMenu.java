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
 * $Log: ResourcePopupMenu.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.resource;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.xplib.xdbm.ui.action.CopyResourceAction;
import de.xplib.xdbm.ui.action.CutResourceAction;
import de.xplib.xdbm.ui.action.DeleteResourceAction;
import de.xplib.xdbm.ui.action.NewCollectionAction;
import de.xplib.xdbm.ui.action.NewVirtualCollectionAction;
import de.xplib.xdbm.ui.action.NewXMLResourceAction;
import de.xplib.xdbm.ui.action.PasteResourceAction;
import de.xplib.xdbm.ui.action.QueryXMLResourceAction;
import de.xplib.xdbm.ui.action.ShowPCVResourceAction;
import de.xplib.xdbm.ui.action.ShowXMLAction;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ResourcePopupMenu extends JPopupMenu implements I18NObserver {
    
    private JMenu jmNew = new JMenu();

    /**
     * 
     */
    public ResourcePopupMenu() {
        super();
        
        this.initUI();
        
        I18N i18n = I18N.getInstance();
        i18n.addObserver(this);
        this.updateI18N(i18n);
    }
    
    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(de.xplib.xdbm.util.I18N)
     */
    public void updateI18N(I18N i18nIn) {
        this.jmNew.setText(i18nIn.getTitle("menu.file.new"));
    }
    
    /**
     * 
     */
    private void initUI() {
        
        this.add(this.jmNew);
        this.jmNew.add(new JMenuItem(NewCollectionAction.INSTANCE));
        this.jmNew.add(new JMenuItem(NewVirtualCollectionAction.INSTANCE));
        this.jmNew.add(new JMenuItem(NewXMLResourceAction.INSTANCE));
        
        this.addSeparator();
        
        this.add(new JMenuItem(CutResourceAction.INSTANCE));
        this.add(new JMenuItem(CopyResourceAction.INSTANCE));
        this.add(new JMenuItem(PasteResourceAction.INSTANCE));
        this.add(new JMenuItem(DeleteResourceAction.INSTANCE));
        
        this.addSeparator();
        
        this.add(new JMenuItem(QueryXMLResourceAction.INSTANCE));
        
        this.addSeparator();
        
        this.add(new JMenuItem(ShowXMLAction.INSTANCE));
        this.add(new JMenuItem(ShowPCVResourceAction.INSTANCE));
    }
}
