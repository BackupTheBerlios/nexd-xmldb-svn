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
 * $Log: SelectDriverField.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.widgets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.xplib.xdbm.util.Config;
import de.xplib.xdbm.util.ConfigObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class SelectDriverField extends JPanel implements ConfigObserver {

    /**
     * Comment for <code>config</code>
     */
    private Config config;
    
    /**
     * Comment for <code>comboBox</code>
     */
    private JComboBox comboBox = new JComboBox();
    
    /**
     * Comment for <code>button</code>
     */
    private JButton button = new JButton("*");
    
    
    /**
     * @param configIn ...
     */
    public SelectDriverField(final Config configIn) {
        this.config = configIn;
        
        this.initComponent();
        
        
        this.update(this.config);
    }
    
    /**
     * @return The selected jar file
     */
    public String getSelectedItem() {
        return (String) this.comboBox.getSelectedItem();
    }
    
    /**
     * @param ilIn ...
     */
    public void addItemListener(final ItemListener ilIn) {
        this.comboBox.addItemListener(ilIn);
    }
    
    /**
     * @param ilIn ...
     */
    public void removeItemListener(final ItemListener ilIn) {
        this.comboBox.removeItemListener(ilIn);
    }
    
    /**
     * <Some description here>
     * 
     * @param configIn
     * @see de.xplib.xdbm.util.ConfigObserver#update(de.xplib.xdbm.util.Config)
     */
    public void update(final Config configIn) {

        this.comboBox.removeAllItems();
        Iterator it = configIn.getDrivers().keySet().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            this.comboBox.addItem(o);
        }
    }
    
    /**
     * 
     */
    private void initComponent() {
       
        this.setLayout(new BorderLayout());
        
        this.add(this.comboBox, BorderLayout.CENTER);
        this.add(this.button, BorderLayout.EAST);
        
        Dimension dim = new Dimension(20, 20);
        this.button.setSize(dim);
        this.button.setPreferredSize(dim);
        this.button.setMaximumSize(dim);
        this.button.setMinimumSize(dim);
    }
    
}
