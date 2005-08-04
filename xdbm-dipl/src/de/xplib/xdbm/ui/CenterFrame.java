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
 * $Log: CenterFrame.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.FileNotFoundException;

import javax.swing.JTabbedPane;

import com.jgoodies.looks.Options;

import de.xplib.xdbm.ui.model.PluginFile;
import de.xplib.xdbm.ui.widgets.InternalFrame;
import de.xplib.xdbm.ui.widgets.JTabbedPaneWithCloseIcons;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class CenterFrame extends InternalFrame {
    
    /**
     * The main application window.
     */
    private final Application app;
    
    /**
     * Comment for <code>jtPane</code>
     */
    private JTabbedPaneWithCloseIcons jtPane = new JTabbedPaneWithCloseIcons();
    
//    private ResourcePanel resPanel = new ResourcePanel();
    
    /**
     * @param appIn The main application window.
     */
    public CenterFrame(final Application appIn) {
        super("Ressourcen");
        
        this.position = CENTER;
        
        this.app = appIn;
        
        this.jtPane.addContainerListener(new ContainerAdapter() {
            public void componentAdded(ContainerEvent ce) {
//                Application.out.println("Added " + ce.getComponent());
            }
            public void componentRemoved(ContainerEvent ce) {
//                Application.out.println("Removed " + ce.getComponent());
            }
        });
        
        this.initUI();
    }
    
    /**
     * 
     */
    private void initUI() {
                
        this.setContent(this.jtPane);
                
        this.jtPane.putClientProperty(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
        this.jtPane.setTabPlacement(JTabbedPane.BOTTOM);
        
        
        
        try {
            PluginFile p = app.getConfig().getPluginFile("resource");
            this.jtPane.addTab("FOO", p.createPlugin().getPanelInstance(this.app));
            p = app.getConfig().getPluginFile("editor");
            this.jtPane.addTab("Test", p.createPlugin().getPanelInstance(this.app));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
    }

}
