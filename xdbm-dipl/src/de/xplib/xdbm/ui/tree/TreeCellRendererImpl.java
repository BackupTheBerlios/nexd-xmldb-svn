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
 * $Log: TreeCellRendererImpl.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.VCLSchemaObject;
import de.xplib.xdbm.ui.model.VirtualCollectionObject;
import de.xplib.xdbm.ui.model.XapiCollectionObject;
import de.xplib.xdbm.util.I18N;
import de.xplib.xdbm.util.I18NObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class TreeCellRendererImpl 
    extends DefaultTreeCellRenderer
    implements I18NObserver {
    
    public static final Icon DB_CONN_ICON = Application.createIcon(
            "icon/tree.db.connected.png");
    
    public static final Icon DB_DISCONN_ICON = Application.createIcon(
    		"icon/tree.db.disconnected.png");
    
    public static final Icon COLL_ICON = Application.createIcon(
            "icon/tree.collection.png");
    
    public static final Icon VCOLL_ICON = Application.createIcon(
            "icon/tree.virtual.collection.png");
    
    public static final Icon VCOLL_SCHEMA_ICON = Application.createIcon(
            "icon/tree.vcs2.png");
    
    /**
     * Comment for <code>i18n</code>
     */
    private I18N i18n = I18N.getInstance();
    
    /**
     * 
     */
    public TreeCellRendererImpl() {
        
    }
    
    /**
     * <Some description here>
     * 
     * @param tree
     * @param value
     * @param sel
     * @param expanded
     * @param leaf
     * @param row
     * @param hasFocus
     * @return
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(
     *      javax.swing.JTree, java.lang.Object, boolean, boolean, 
     *      boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent(final JTree tree, 
                                                  final Object value,
                                                  final boolean sel, 
                                                  final boolean expanded, 
                                                  final boolean leaf, 
                                                  final int row,
                                                  final boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded,
                                           leaf, row, hasFocus);
        
        if (value instanceof DatabaseNode) {
            this.render((DatabaseNode) value);
        } else if (value instanceof XapiCollectionObject) {
            this.render((XapiCollectionObject) value);
        } else if (value instanceof VirtualCollectionObject) {
            this.render((VirtualCollectionObject) value);
        } else if (value instanceof VCLSchemaObject) {
            this.render((VCLSchemaObject) value);
        }
        
        return this;
    }
    
    
    
    /**
     * <Some description here>
     * 
     * @param i18nIn
     * @see de.xplib.xdbm.util.I18NObserver#updateI18N(de.xplib.xdbm.util.I18N)
     */
    public void updateI18N(final I18N i18nIn) {
        // TODO Auto-generated method stub

    }
    
    /**
     * @param collNodeIn The collection node to render.
     */
    private void render(final XapiCollectionObject collNodeIn) {
        this.setIcon(COLL_ICON);
        this.setText(collNodeIn.getName());
    }
    
    private void render(final VirtualCollectionObject vcollNodeIn) {
        this.setIcon(VCOLL_ICON);
        this.setText(vcollNodeIn.getName());
    }
    
    private void render(final VCLSchemaObject schemaIn) {
        this.setIcon(VCOLL_SCHEMA_ICON);
        this.setText("VCLSchema");
    }
    
    /**
     * @param dbNodeIn The database node.
     */
    private void render(final DatabaseNode dbNodeIn) {
        if (dbNodeIn.isConnected()) {
            this.setIcon(DB_CONN_ICON);
            this.setText(i18n.getTitle(
                    "tree.db.connected", new String[] {dbNodeIn.getUri()}));
        } else {
            this.setIcon(DB_DISCONN_ICON);
            this.setText(i18n.getTitle("tree.db.disconnected"));
        }
    }
}
