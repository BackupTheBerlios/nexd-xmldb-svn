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
 * $Log: BottomFrame.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import com.jgoodies.looks.Options;

import de.xplib.xdbm.ui.console.OutputPanel;
import de.xplib.xdbm.ui.widgets.InternalFrame;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class BottomFrame extends InternalFrame {

    /**
     * Comment for <code>app</code>
     */
    private final Application app;
    
    /**
     * Comment for <code>jtPane</code>
     */
    private JTabbedPane jtPane = new JTabbedPane();
    
    /**
     * Comment for <code>outputPanel</code>
     */
    private OutputPanel outputPanel;
    
    /**
     * @param appIn ..
     */
    public BottomFrame(final Application appIn) {
        super("FooBar");
        
        this.position = BOTTOM;
        
        this.app = appIn;

        this.initUI();
    }
    
    /**
     * 
     */
    private void initUI() {
                
        this.jtPane.putClientProperty(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
        this.jtPane.setTabPlacement(JTabbedPane.TOP);
        
        this.setContent(this.jtPane);
        
        this.outputPanel = new OutputPanel();
        
        this.jtPane.addTab("Foo", this.outputPanel);
        this.jtPane.addTab("Bar", new JLabel("Label2"));
    }
    
}
