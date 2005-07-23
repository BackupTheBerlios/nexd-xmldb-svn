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
package de.xplib.nexd.util;

/**
 * Simple utility class that implements a stack for integers. The stack is 
 * dynamicly resized.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public class IntStack {
    
    /**
     * The default size of the stack.
     */
    public static final int DEFAULT_SIZE = 20;
    
    /**
     * The factor that resizes the stack.
     */
    public static final float RESIZE_FACTOR = 2.0f;
    
    /**
     * The internal pointer.
     */
    private int offset = 0;
    
    /**
     * The current size of the stack.
     */
    private int csize;
    
    /**
     * The current resize factory for the internal data structure.
     */
    private float resize;
    
    /**
     * The data structure with the integer values. 
     */
    private int[] stack;

    /**
     * Constructor.
     */
    public IntStack() {
        this(DEFAULT_SIZE, RESIZE_FACTOR);
    }
    
    /**
     * Constructor.
     * 
     * @param sizeIn The initial size of the stack.
     */
    public IntStack(final int sizeIn) {
        this(sizeIn, RESIZE_FACTOR);
    }
    
    /**
     * Constructor.
     * 
     * @param sizeIn The initial statck size.
     * @param resizeIn The resize factor.
     */
    public IntStack(final int sizeIn, final float resizeIn) {
        super();
        
        this.csize   = sizeIn;
        this.resize = resizeIn;
        
        this.stack = new int[this.csize];
    }
    
    /**
     * Adds a new value at the top of the stack.
     * 
     * @param valueIn A new value.
     */
    public void push(final int valueIn) {
        if (this.offset + 1 == this.csize) {
            int newSize    = (int) (this.csize * this.resize);
            int[] newStack = new int[newSize];
            
            for (int i = 0; i < this.csize; i++) {
                newStack[i] = this.stack[i];
            }
            
            this.csize  = newSize;
            this.stack = newStack;
        }
        
        this.stack[++this.offset] = valueIn;
    }
    
    /**
     * Returns and removes the first value on the stack.
     * 
     * @return The value at the top of the stack.
     */
    public int pop() {
        if (this.offset == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.stack[this.offset--];
    }
    
    /**
     * Returns and doesn't remove the first value on the stack.
     * 
     * @return The value at the top of the stack.
     */
    public int peek() {
        if (this.offset == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.stack[this.offset];
    }
    
    /**
     * Returns the first element in the stack.
     * 
     * @return The first element.
     */
    public int first() {
        return this.stack[1];
    }
    
    /**
     * Returns the current size of the stack.
     * 
     * @return The current size of the stack.
     */
    public int size() {
        return this.offset;
    }
    
    /**
     * This method clears all stack values and sets the internal pointer to 
     * <code>0</code>. 
     */
    public void reset() {
        this.offset = 0;
        this.stack  = new int[this.csize];
    }

}
