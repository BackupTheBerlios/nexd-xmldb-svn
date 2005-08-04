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
 * $Log: LoadableNodeListener.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.tree;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class LoadableNodeListener implements TreeWillExpandListener {

    /**
     * <Some description here>
     * 
     * @param eventIn
     * @throws javax.swing.tree.ExpandVetoException
     * @see javax.swing.event.TreeWillExpandListener#treeWillExpand(
     *      javax.swing.event.TreeExpansionEvent)
     */
    public void treeWillExpand(final TreeExpansionEvent eventIn)
            throws ExpandVetoException {
        
        Object o = eventIn.getPath().getLastPathComponent();
        if (o instanceof LoadableNode) {
            ((LoadableNode) o).loadChildNodes();
        }
        
        //Application.out.println("Will expand " + o.toString());
    }

    /**
     * <Some description here>
     * 
     * @param event
     * @throws javax.swing.tree.ExpandVetoException
     * @see javax.swing.event.TreeWillExpandListener#treeWillCollapse(
     *      javax.swing.event.TreeExpansionEvent)
     */
    public void treeWillCollapse(TreeExpansionEvent event)
            throws ExpandVetoException {
        // TODO Auto-generated method stub

    }

}
