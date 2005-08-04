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
 * $Log: TreeObjectFactory.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import org.xmldb.api.base.Collection;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.xdbm.ui.tree.CollectionTree;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public final class TreeObjectFactory {

    /**
     * Holds singleton instance
     */
    private static TreeObjectFactory instance;
    
    private CollectionTree tree;

    /**
     * 
     */
    private TreeObjectFactory(final CollectionTree treeIn) {
        super();

        this.tree = treeIn;
    }
    
    public static TreeObjectFactory getInstance(final CollectionTree treeIn) {
        if (instance == null) {
            instance = new TreeObjectFactory(treeIn);
        }
        return instance;
    }

    /**
     * Returns the singleton instance.
     * @return	the singleton instance
     */
    public static TreeObjectFactory getInstance() {
        return getInstance(null);
    }
    
    
    public TreeObject createTreeObject(final Collection collIn) {
        
        if (collIn instanceof VirtualCollection) {
            return this.createTreeObject((VirtualCollection) collIn);
        } else if (collIn != null) {
            return new XapiCollectionObject(this.tree, collIn);
        }
        
        return null;
    }
    
    public TreeObject createTreeObject(final Collection collIn, boolean delete) {
        
        if (collIn instanceof VirtualCollection) {
            return this.createTreeObject((VirtualCollection) collIn, delete);
        } else if (collIn != null) {
            XapiCollectionObject xco = new XapiCollectionObject(this.tree, collIn);
            xco.setDeleted();
            return xco;
        }
        
        return null;
    }
    
    public VirtualCollectionObject createTreeObject(
            final VirtualCollection vcollIn) {
        
        return this.createTreeObject(vcollIn, false);
    }
    
    public VirtualCollectionObject createTreeObject(
            final VirtualCollection vcollIn, boolean delete) {
        
        return new VirtualCollectionObject(this.tree, vcollIn, delete);
    }

}
