/*
 * Project: nexd 
 * Copyright (C) 2004  Manuel Pichler <manuel.pichler@xplib.de>
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
package de.xplib.nexd.xml.dom;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>NodeList</code> interface provides the abstraction of an ordered 
 * collection of nodes, without defining or constraining how this collection 
 * is implemented. <code>NodeList</code> objects in the DOM are live.
 * <p>The items in the <code>NodeList</code> are accessible via an integral 
 * index, starting from 0.
 * <p>See also the <a 
 * href='http://www.w3.org/TR/2002/WD-DOM-Level-3-Core-20020114'
 * >Document Object Model (DOM) Level 3 Core Specification</a>.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class NodeListImpl implements NodeList {
    
    
    /**
     * Comment for <code>parentNode</code>
     */
    private AbstractNode owner = null;
    
    /**
     * Comment for <code>childNodes</code>
     */
    private List nodes = null;

    /**
     * @param ownerIn ..
     */
    public NodeListImpl(final AbstractNode ownerIn) {
        this(ownerIn, new ArrayList());
    }
    
    /**
     * @param ownerIn ..
     * @param nodesIn ..
     */
    public NodeListImpl(final AbstractNode ownerIn, final List nodesIn) {
        super();
        
        this.owner = ownerIn;
        this.nodes = nodesIn;
    }

    /**
     * Returns the <code>index</code>th item in the collection. If 
     * <code>index</code> is greater than or equal to the number of nodes in 
     * the list, this returns <code>null</code>.
     * 
     * @param index Index into the collection.
     * @return The node at the <code>index</code>th position in the 
     *   <code>NodeList</code>, or <code>null</code> if that is not a valid 
     *   index.
     * @see org.w3c.dom.NodeList#item(int)
     */
    public Node item(final int index) {
        if (index < this.getLength()) {
            return (Node) this.nodes.get(index);
        }
        return null;
    }

    /**
     * The number of nodes in the list. The range of valid child node indices 
     * is 0 to <code>length-1</code> inclusive.
     * 
     * @return ---
     * @see org.w3c.dom.NodeList#getLength()
     */
    public int getLength() {
        return this.nodes.size();
    }
    
    
    //
    // Non DOM methods
    //
    
    
    /**
     * @return The internal used <code>ArrayList</code>
     */
    protected List getList() {
        return this.nodes;
    }

}
