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
 * $Log: VirtualCollectionObject.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import javax.swing.tree.DefaultMutableTreeNode;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.tree.CollectionTree;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class VirtualCollectionObject 
    extends DefaultMutableTreeNode
    implements CollectionObject, TreeObject, UIObject {
    
    private CollectionTree tree;
    
    private VirtualCollection vcoll;
    
    private String name = "";
    
    /**
     * 
     */
    public VirtualCollectionObject(final CollectionTree treeIn,
                                   final VirtualCollection collIn) {
        this(treeIn, collIn, false);
    }
    
    public VirtualCollectionObject(final CollectionTree treeIn,
                                   final VirtualCollection collIn,
                                   final boolean delete) {

        super(collIn);

        
        this.tree  = treeIn;
        this.vcoll = collIn;
        
        if (!delete) {
            try {
                
                this.name = collIn.getName();
                
            	VCLSchema schema = collIn.getVCLSchema();
            	this.add(new VCLSchemaObject(treeIn, this, collIn.getVCLSchema()));
        	} catch (XMLDBException e) {
            	e.printStackTrace(Application.err);
        	}
        } else {
            this.setDeleted();
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public Collection getCollection() {
        return this.vcoll;
    }
    
    public boolean isLeaf() {
        return false;
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
