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
 * $Log: LeftFrame.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui;

import javax.swing.JTabbedPane;

import org.xmldb.api.base.Database;

import com.jgoodies.looks.Options;

import de.xplib.xdbm.ui.tree.TreePanel;
import de.xplib.xdbm.ui.widgets.InternalFrame;
import de.xplib.xdbm.ui.widgets.JTabbedPaneWithCloseIcons;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;
import de.xplib.xdbm.util.XmldbObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class LeftFrame 
    extends InternalFrame 
    implements I18NObserver, XmldbObserver {

    /**
     * Comment for <code>jtPane</code>
     */
    private JTabbedPaneWithCloseIcons jtPane = new JTabbedPaneWithCloseIcons();
    
    private final Application app;
    
    public LeftFrame(final Application appIn) {
        super("Collections");
        
        this.position = LEFT;
        
        this.app = appIn;
        
        this.setContent(this.jtPane);
        
        this.jtPane.putClientProperty(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
        this.jtPane.setTabPlacement(JTabbedPane.BOTTOM);
        
        this.jtPane.addTab("FOO", new TreePanel());
    }

    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(de.xplib.xdbm.util.I18N)
     */
    public void updateI18N(I18N i18nIn) {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @param xmldbIn
     * @see de.xplib.xdbm.util.XmldbObserver#update(org.xmldb.api.base.Database)
     */
    public void update(Database xmldbIn) {
        // TODO Auto-generated method stub

    }

}
