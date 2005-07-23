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

/*
 * $Log: NodeListWithPosition.java,v $
 * Revision 1.1  2005/05/08 11:59:33  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.xml.dom.xpath;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.collections.FastHashMap;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/** 
 * A list of nodes, together with the positions in their context of
 * each node.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class NodeListWithPosition {

    /**
     * Map that contains both a <code>Node</code> instance and its position.
     */
    private final Map positions;

    /**
     * ArrayList with the <code>Node</code> instances.
     */
    private final ArrayList vector;
    
    

    /**
     * 
     */
    public NodeListWithPosition() {
        super();

        this.positions = new FastHashMap();
        ((FastHashMap) this.positions).setFast(true);
        
        this.vector = new FastArrayList();
        ((FastArrayList) this.vector).setFast(true);
    }
    
    /**
     * Adds an <code>Attr</code> instance.
     * 
     * @param attrIn The <code>Attr</code> instance.
     */
    void add(final Attr attrIn) {
        vector.add(attrIn);
    }

    /**
     * Adds a <code>Node</code> instance and its position.
     * 
     * @param nodeIn The <code>Node</code> instance.
     * @param positionIn The <code>Node</code> position.
     * @see NodeListWithPosition#add(Node, Integer)
     */
    void add(final Node nodeIn, final int positionIn) {
        add(nodeIn, new Integer(positionIn));
    }

    /**
     * Adds a <code>Node</code> instance and its position.
     * 
     * @param nodeIn The <code>Node</code> instance.
     * @param positionIn The <code>Node</code> position.
     */
    void add(final Node nodeIn, final Integer positionIn) {
        vector.add(nodeIn);
        positions.put(nodeIn, positionIn);
    }

    /**
     * Adds a <code>Text</code> instance.
     * 
     * @param textIn The <code>Text</code> instance.
     */
    void add(final Text textIn) {
        vector.add(textIn);
    }
    
    /**
     * Returns all <code>Node</code>, <code>Text</code> and <code>Attr</code>
     * instances in this list.
     * 
     * @return All <code>Node</code> instances in the list.
     */
    Iterator elements() {
        return vector.iterator();
    }

    /**
     * Returns the position for the given <code>nodeIn</code> parameter.
     * 
     * @param nodeIn The <code>Node</code> to look for.
     * @return The position or <code>-1</code>, if the <code>Node</code> can't
     *         be found.
     */
    int position(final Node nodeIn) {
        return ((Integer) positions.get(nodeIn)).intValue();
    }

    /**
     * Removes all stored <code>Node</code> instances from this list. 
     */
    void removeAllElements() {
        vector.clear();
    }
}
