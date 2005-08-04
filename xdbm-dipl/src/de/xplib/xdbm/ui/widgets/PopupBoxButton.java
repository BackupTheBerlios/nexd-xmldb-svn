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
 * $Log: PopupBoxButton.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import org.tigris.swidgets.PopupToolBoxButton;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class PopupBoxButton 
    extends PopupToolBoxButton
    implements PropertyChangeListener {

    /**
     * @param a
     * @param rows
     * @param cols
     */
    public PopupBoxButton(Action a, int rows, int cols) {
        this(a, new Action[0], rows, cols);
    }
    
    /**
     * @param a
     * @param actions
     * @param rows
     * @param cols
     */
    public PopupBoxButton(Action a, Action[] actions, int rows, int cols) {
        super(a, rows, cols);
        
        a.addPropertyChangeListener(this);
        
        this.setEnabled(a.isEnabled());
        
        for (int i = 0; i < actions.length; i++) {
            this.add(actions[i]);
        }
    }

    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals("enabled")) {
            this.setEnabled(((Boolean) pce.getNewValue()).booleanValue());
        }
    }
}
