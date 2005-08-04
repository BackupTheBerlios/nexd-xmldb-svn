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
 * $Log: JToolBarButton.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.widgets;

import javax.swing.Action;
import javax.swing.JButton;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class JToolBarButton extends JButton {

    /**
     * @param a
     */
    public JToolBarButton(Action a) {
        super(a);
    }

    
    /**
     * <Some description here>
     * 
     * @return
     * @see javax.swing.AbstractButton#getText()
     */
    public String getText() {
        return null;
    }
    /**
     * <Some description here>
     * 
     * @param text
     * @see javax.swing.AbstractButton#setText(java.lang.String)
     */
    public void setText(String text) {
    }
}
