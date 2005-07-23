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
 * $Log: NodeTupleList.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.5  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.4  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.store;

/**
 * <p>This class implements a list or so, that holds for multiple nodes the 
 * three identifiers: <b>resource id</b>, <b>nested set left value</b> and
 * <b>nested set right value</b>.</p>  
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class NodeTupleList {
    
    /**
     * The default size of the internal arrays.
     */
    public static final int DEFAULT_SIZE = 20;
    
    /**
     * Array that contains all resource ids. 
     */
    private int[] rids = new int[DEFAULT_SIZE];
    
    /**
     * An array containing the left indexes of the node tree.
     */
    private int[] lfts = new int[DEFAULT_SIZE];
    
    /**
     * An array containing the right indexes of the node tree.
     */
    private int[] rgts = new int[DEFAULT_SIZE];
    
    /**
     * Current fill length of the internal arrays.
     */
    private int length = 0;

    /**
     * <p>Constructor.</p>
     */
    protected NodeTupleList() {
        super();
    }
    
    /**
     * Adds all necessary params to identify a part of a document.
     * 
     * @param ridIn The resource id.
     * @param lftIn The left index value.
     * @param rgtIn The right index value.
     */
    public void add(final int ridIn, final int lftIn, final int rgtIn) {
        
        if (this.length == this.rids.length) {
            
            int newSize   = this.rids.length + DEFAULT_SIZE;
            
            int[] newRids = new int[newSize];
            int[] newLfts = new int[newSize];
            int[] newRgts = new int[newSize];
            
            System.arraycopy(this.rids, 0, newRids, 0, this.rids.length);
            System.arraycopy(this.lfts, 0, newLfts, 0, this.lfts.length);
            System.arraycopy(this.rgts, 0, newRgts, 0, this.rgts.length);
            
            this.rids = newRids;
            this.lfts = newLfts;
            this.rgts = newRgts;
        }
        
        this.rids[this.length] = ridIn;
        this.lfts[this.length] = lftIn;
        this.rgts[this.length] = rgtIn;
        
        ++this.length;
    }
    
    /**
     * Returns the length of the internal arrays.
     * 
     * @return The current length of the internal arrays.
     */
    public int getLength() {
        return this.length;
    }
    
    /**
     * Returns the resource id for the given <code>indexIn</code>.
     * 
     * @param indexIn The index.
     * @return The resource id.
     */
    public int getRid(final int indexIn) {
        return this.rids[indexIn];
    }
    
    /**
     * Returns the left index value for the given <code>indexIn</code>.
     * 
     * @param indexIn The index.
     * @return The left index value.
     */
    public int getLft(final int indexIn) {
        return this.lfts[indexIn];
    }
    
    /**
     * Returns the right index value for the given <code>indexIn</code>.
     * 
     * @param indexIn The index.
     * @return The right index value.
     */
    public int getRgt(final int indexIn) {
        return this.rgts[indexIn];
    }
    
}
