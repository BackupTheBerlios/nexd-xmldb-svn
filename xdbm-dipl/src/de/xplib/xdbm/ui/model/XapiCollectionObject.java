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
 * $Log: XapiCollectionObject.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import javax.swing.tree.DefaultMutableTreeNode;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.tree.CollectionTree;
import de.xplib.xdbm.ui.tree.LoadableNode;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class XapiCollectionObject 
    extends DefaultMutableTreeNode
    implements CollectionObject, TreeObject, LoadableNode, UIObject {
    
    private Collection coll;

    private CollectionTree tree;
    
    public XapiCollectionObject(final CollectionTree treeIn,
                          final Collection collIn) {
        super(collIn);
        
        this.tree = treeIn;
        this.coll = collIn;
        
        this.allowsChildren = true;
    }
    
    public boolean isLeaf() {
        
        try {
            String names[] = this.coll.listChildCollections();
            
            return (names.length == 0);
        } catch (XMLDBException e) {
            e.printStackTrace(Application.err);
        }
        
        return false;
    }
    
    public Collection getCollection() {
        return this.coll;
    }
    
    public String getName() {
        try {
            return this.coll.getName();
        } catch (XMLDBException e) {
            e.printStackTrace(Application.err);
        }
        return "";
    }
    
    public void close() {
        try {
            this.coll.close();
        } catch (XMLDBException e) {
            e.printStackTrace(Application.err);
        }
    }
    
    /**
     * <Some description here>
     * 
     * 
     * @see de.xplib.xdbm.ui.tree.LoadableNode#loadChildNodes()
     */
    public void loadChildNodes() {
        
        if (this.children != null) {
            this.children.clear();
        }
                
        try {
            String[] names = this.coll.listChildCollections();
            
            for (int i = 0; i < names.length; i++) {
                Collection child = this.coll.getChildCollection(names[i]);
                
                if (child instanceof VirtualCollection) {
                    this.add(new VirtualCollectionObject(
                            this.tree, (VirtualCollection) child));
                } else {
                    this.add(new XapiCollectionObject(this.tree, child));
                }
            }
        } catch (XMLDBException e) {
            e.printStackTrace(Application.err);
        }
    }
    
    private boolean deleted = false;
    private boolean changed = false;
    private boolean isNew = false;
    
    public boolean isChanged() {
        return this.changed;
    }
    
    public void setChanged(final boolean changedIn) {
        this.changed = changedIn;
    }

    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted() {
        this.deleted = true;
    }
    
    public boolean isNew() {
        return this.isNew;
    }
    
    public void setNew(final boolean newIn) {
        this.isNew = newIn;
    }
}
