/*
 * Project: nexd 
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
package de.xplib.nexd.engine.xml.dom;

import java.util.List;

import org.w3c.dom.NodeList;

import de.xplib.nexd.xml.DocumentCacheI;

/**
 * This class holds all changed nodes in a single <code>Document</code> object.
 * The cache can be used by the <code>StorageI</code> to update only changed
 * nodes.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class DocumentCacheImpl implements DocumentCacheI {
    
    /**
     * <code>NodeList</code> containing the deleted nodes.
     */
    private final NodeListImpl delCache = new NodeListImpl(null);
    
    /**
     * <code>NodeList</code> containing the inserted nodes.
     */
    private final NodeListImpl newCache = new NodeListImpl(null);
    
    /**
     * <code>NodeList</code> containing the changed nodes.
     */
    private final NodeListImpl chgCache = new NodeListImpl(null);
    
    /**
     * Internal <code>java.util.List</code> of <code>delCache</code>.
     */
    private final List delList;
    
    /**
     * Internal <code>java.util.List</code> of <code>newCache</code>.
     */
    private final List newList;
    
    /**
     * Internal <code>java.util.List</code> of <code>chgCache</code>.
     */
    private final List chgList;
    
    /**
     * Constructor.
     */
    public DocumentCacheImpl() {
        super();
        
        this.delList = this.delCache.getList();
        this.newList = this.newCache.getList();
        this.chgList = this.chgCache.getList();
    }
    
    /**
     * Marks the given <code>nodeIn</code> as deleted.
     * 
     * @param nodeIn The deleted <code>AbstractNode</code>.
     */
    public void delete(final AbstractNode nodeIn) {
        if (nodeIn.internalId == null) {
            this.delList.remove(nodeIn);
            this.newList.remove(nodeIn);
        } else {
            this.delList.add(nodeIn);
            this.newList.remove(nodeIn);
        }
    }
    
    /**
     * Marks the given <code>nodeIn</code> as a new inserted <code>Node</code>.
     * 
     * @param nodeIn The new inserted <code>AbstractNode</code>.
     */
    public void insert(final AbstractNode nodeIn) {
        
        this.delList.remove(nodeIn);
        this.newList.add(nodeIn);
    }
    
    /**
     * Marks the given <code>nodeIn</code> as a changed node or if 
     * <code>oldNodeIn</code> is not null it marks <code>nodeIn</code> as a
     * replacement of <code>oldNodeIn</code>.
     * 
     * @param oldNodeIn The <code>AbstractNode</code> to replace of 
     *                  <code>null</code>.
     * @param nodeIn The new or changed <code>AbstractNode</code>.
     */
    public void change(final AbstractNode oldNodeIn, 
                       final AbstractNode nodeIn) {
        
        if (this.newList.contains(oldNodeIn)) {
            this.newList.remove(oldNodeIn);
            this.newList.add(nodeIn);
        } else if (!this.delList.contains(oldNodeIn)) {
            this.chgList.add(nodeIn);
        }
    }
    
    /**
     * Getter method for the <code>NodeList</code> with the deleted 
     * <code>Node</code>s.
     * 
     * @return The deleted <code>Node</code>s.
     */
    public NodeList getDeleted() {
        return this.delCache;
    }
    
    /**
     * Getter method for the <code>NodeList</code> with the inserted 
     * <code>Node</code>s.
     * 
     * @return The inserted <code>Node</code>s.
     */
    public NodeList getInserted() {
        return this.newCache;
    }
    
    /**
     * Getter method for the <code>NodeList</code> with the changed
     * <code>Node</code>s.
     * 
     * @return The changed <code>Node</code>s.
     */
    public NodeList getChanged() {
        return this.chgCache;
    }
    
    /**
     * Clears all <code>NodeList</code> instances. 
     */
    public void clear() {
        this.delList.clear();
        this.chgList.clear();
        this.newList.clear();
    }
    
}
