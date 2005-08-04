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
 * $Log: CollectionTree.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.TreeObjectFactory;
import de.xplib.xdbm.ui.model.UIObject;
import de.xplib.xdbm.util.PopupListener;
import de.xplib.xdbm.util.UIObjectObserver;
import de.xplib.xdbm.util.XmldbObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class CollectionTree 
    extends JTree 
    implements UIObjectObserver, XmldbObserver {
    
    private TreeObjectFactory factory;
    
    private CollectionTreePopup popup = new CollectionTreePopup();

    public CollectionTree() {
        super();
        
        this.factory = TreeObjectFactory.getInstance(this);
        
        Application.getInstance().addObserver((UIObjectObserver)this);
        Application.getInstance().addObserver((XmldbObserver) this);
        
        this.setModel(new DefaultTreeModel(new DatabaseNode(this)));
                                
        this.setCellRenderer(new TreeCellRendererImpl());
                
        this.addTreeWillExpandListener(new LoadableNodeListener());
        this.addTreeSelectionListener(new TreeSelectionListenerImpl());
        
        
        this.add(this.popup);
        this.addMouseListener(new PopupListener(this.popup));
        this.addMouseListener(new RightSelectListener(this));
    }
    
    
    /**
     * <Some description here>
     * 
     * @param objectIn
     * @see de.xplib.xdbm.util.UIObjectObserver#update(
     *      de.xplib.xdbm.ui.model.UIObject)
     */
    public void update(UIObject objectIn) {
        
        TreePath tp = this.getSelectionPath(); 
        
        Object o = (tp == null ? null : tp.getLastPathComponent());
        if (o == objectIn) {
            return;
        } else if (objectIn == null && o != null && o instanceof UIObject) {
            
            Object c = ((UIObject) o).getUserObject();
            if (c instanceof Collection) {
                try {
                    if (((Collection) c).isOpen()) {
                        Application.getInstance().setUIObject((UIObject) o);
                    }
                } catch (XMLDBException e) {
                    e.printStackTrace(Application.err);
                }
            }
        } else if (objectIn instanceof MutableTreeNode) {
            if (objectIn.isNew() && o instanceof DefaultMutableTreeNode) {
                
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) o;
                dmtn.add((MutableTreeNode) objectIn);
                
                ((DefaultTreeModel) this.treeModel).reload(dmtn);
            } else if (objectIn.isDeleted() && o instanceof DefaultMutableTreeNode
                    && ((UIObject) o).getUserObject() == objectIn.getUserObject()) {
                
                DefaultMutableTreeNode node   = (DefaultMutableTreeNode) o;
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                
                if (parent.getIndex(node) != -1) {
                    parent.remove(node);
                }
                
                ((DefaultTreeModel) this.treeModel).reload(parent);
            }
        }
    }
    
    /**
     * <Some description here>
     * 
     * @param xmldbIn
     * @see de.xplib.xdbm.util.XmldbObserver#update(org.xmldb.api.base.Database)
     */
    public void update(final Database xmldbIn) {
        
        if (xmldbIn == null) {
            
        }
        // TODO Auto-generated method stub

    }
}
