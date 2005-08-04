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
 * $Log: InternalFramePanel.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.widgets;

import java.awt.Container;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class InternalFramePanel extends JPanel {
    
    protected String title = "";
    
    protected Icon icon = null;
    
    protected JToolBar toolBar = null;
    
    private JTabbedPane tabbedPane = null;
    
    /**
     * 
     */
    public InternalFramePanel(final String titleIn) {
        super();
        
        this.title = titleIn;
    }
    
    public void focus() {
        if (this.tabbedPane == null 
                || tabbedPane.getSelectedComponent() == this) {
            return;
        }
        
        if (this.tabbedPane.indexOfComponent(this) == -1) {
            this.tabbedPane.addTab(this.title, this);
        }
        this.tabbedPane.setSelectedComponent(this);
    }
    
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        
        if (this.tabbedPane != null) {
            try {
                this.tabbedPane.setEnabledAt(
                        this.tabbedPane.indexOfComponent(this), b);
            } catch (RuntimeException e) {
            }
        }
    }
    
    /**
     * <Some description here>
     * 
     * @see java.awt.Component#addNotify()
     */
    public void addNotify() {
        super.addNotify();
        
        Container c = this.getParent();
        if (c instanceof JTabbedPane) {
            
            JTabbedPane jtp = (JTabbedPane) c;
            int count = jtp.getTabCount();
            
            for (int i = 0; i < count; i++) {
                if (jtp.getComponentAt(i) == InternalFramePanel.this) {
                    
                    try {
                        jtp.setTitleAt(i, title);
                    } catch (RuntimeException e) {
                    }
                    break;
                }
            }
            
            if (jtp.getSelectedComponent() == InternalFramePanel.this
                    && jtp.getParent() instanceof InternalFrame) {
                
                InternalFrame iFrame = (InternalFrame) jtp.getParent();
                iFrame.setTitle(title);
                iFrame.setFrameIcon(icon);
                iFrame.setToolBar(toolBar);
            }
            
            tabbedPane = jtp;
            
            tabbedPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    if (tabbedPane.getSelectedComponent() == InternalFramePanel.this) {
                        InternalFrame iFrame = (InternalFrame) tabbedPane.getParent();
                        iFrame.setTitle(title);
                        iFrame.setFrameIcon(icon);
                        iFrame.setToolBar(toolBar);
                    }
                }
            });
            
        } else if (c instanceof InternalFrame) {
            InternalFrame iFrame = (InternalFrame) c;
            iFrame.setTitle(title);
            iFrame.setFrameIcon(icon);
            iFrame.setToolBar(toolBar);
        }
    }

}
