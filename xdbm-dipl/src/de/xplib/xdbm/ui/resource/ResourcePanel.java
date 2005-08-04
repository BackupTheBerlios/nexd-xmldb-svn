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
 * $Log: ResourcePanel.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.resource;

import java.awt.GridLayout;

import javax.swing.JScrollPane;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.ui.model.VirtualCollectionObject;
import de.xplib.xdbm.ui.model.XapiCollectionObject;
import de.xplib.xdbm.ui.widgets.InternalFramePanel;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;
import de.xplib.xdbm.util.UIObjectObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ResourcePanel 
    extends InternalFramePanel 
    implements I18NObserver, UIObjectObserver {
    
    private ResourceTable table;

    /**
     * 
     */
    public ResourcePanel(Application appIn) {
        super("Test");
        
        I18N i18n = I18N.getInstance();
        i18n.addObserver(this);
        this.updateI18N(i18n);
        
        appIn.addObserver(this);

        this.initUI();
    }
    
    /**
     * 
     */
    private void initUI() {
        
        this.icon = Application.createIcon("icon/icon.resources.png");
        
        this.table = new ResourceTable();
        
        this.setLayout(new GridLayout(1, 1));
        
        JScrollPane jsp = new JScrollPane();
        this.add(jsp);
        
        jsp.getViewport().add(this.table);
    }

    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(
     *      de.xplib.xdbm.util.I18N)
     */
    public void updateI18N(final I18N i18nIn) {

        this.title = i18nIn.getTitle("resource.title");
    }
    
    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(
     *      de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {
        if (objectIn instanceof XapiCollectionObject 
                || objectIn instanceof VirtualCollectionObject) {
            
            this.focus();
        }

    }
}
