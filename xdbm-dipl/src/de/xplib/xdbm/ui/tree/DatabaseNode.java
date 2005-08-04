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
 * $Log: DatabaseNode.java,v $
 * Revision 1.1  2005/04/12 08:34:22  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import de.xplib.xdbm.ui.Application;
import de.xplib.xdbm.ui.model.Connection;
import de.xplib.xdbm.ui.model.XapiCollectionObject;
import de.xplib.xdbm.util.XmldbObserver;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class DatabaseNode 
    extends DefaultMutableTreeNode 
    implements XmldbObserver {
    
    private CollectionTree tree;
    
    private XapiCollectionObject cNode;
    
    private Database xmldb = null;
        
    private Connection conn = null;

    public DatabaseNode(final CollectionTree treeIn) {
        super("database");
        
        this.tree = treeIn;
        
        Application.getInstance().addObserver(this);
    }
    
    public String getUri() {
        if (this.isConnected()) {
            return this.conn.getUri();
        }
        return "";
    }
    
    public boolean isConnected() {
        return (this.xmldb != null);
    }
    
    /**
     * <Some description here>
     * 
     * @param xmldbIn
     * @see de.xplib.xdbm.util.XmldbObserver#update(org.xmldb.api.base.Database)
     */
    public void update(final Database xmldbIn) {
        this.xmldb = xmldbIn;
        if (xmldbIn instanceof Database) {
            this.conn = Application.getInstance().getConfig().getConnection();
            
            try {
                
                String uri = this.conn.getUri().replaceFirst("xmldb:", "");
                
                Collection coll = this.xmldb.getCollection(uri, "sa", "");
                this.cNode = new XapiCollectionObject(this.tree, coll);
                this.add(this.cNode);
                                
                this.tree.expandRow(
                        this.tree.getModel().getIndexOfChild(this, this.cNode));
                
                ((DefaultTreeModel) this.tree.getModel()).reload(this);
                
            } catch (XMLDBException e) {
                e.printStackTrace(Application.err);
            }
        } else {
            
            this.remove(this.cNode);
            
            ((DefaultTreeModel) this.tree.getModel()).reload();
            
            this.conn = null;
            this.cNode.close();
            this.cNode = null;
        }
    }
}
