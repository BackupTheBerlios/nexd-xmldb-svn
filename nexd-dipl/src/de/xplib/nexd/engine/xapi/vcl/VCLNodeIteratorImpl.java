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
 * $Log: VCLNodeIteratorImpl.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.2  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.1  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.FastArrayList;

import de.xplib.nexd.api.vcl.VCLNode;
import de.xplib.nexd.api.vcl.VCLNodeIterator;

/**
 * <p>This is the abstract base class for all used iterators. This class doesn't
 * implement any design issue, it just makes coding easier.</p>
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class VCLNodeIteratorImpl implements VCLNodeIterator {
    
    /**
     * The underlying <code>{@link Collection}</code> instance.
     */
    private final Collection coll;
    
    /**
     * The wrapped <code>{@link Iterator}</code> instance.
     */
    protected Iterator nodeIt;
    
    /**
     * Constructor.
     */
    public VCLNodeIteratorImpl() {
        super();
        
        FastArrayList list = new FastArrayList();
        list.setFast(true);
        
        this.coll = list;
        this.reset();
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public final boolean hasNext() {
        return this.nodeIt.hasNext();
    }
    
    /**
     * Returns the current selected <code>VCLNode</code>.
     * 
     * @return The <code>VCLNode</code>.
     * @see de.xplib.nexd.api.vcl.VCLNodeIterator#next()
     */
    public final VCLNode next() {
        return (VCLNode) this.nodeIt.next();
    }
    
    /**
     * Resets the Iterator to the first element.
     */
    public final void reset() {
        this.nodeIt = this.coll.iterator();
    }
    
    /**
     * Adds a <code>VCLNode</code> to the internal collection.
     * 
     * @param nodeIn The <code>VCLNode</code> to add.
     */
    protected final void add(final VCLNode nodeIn) {
        this.coll.add(nodeIn);
    }

}
