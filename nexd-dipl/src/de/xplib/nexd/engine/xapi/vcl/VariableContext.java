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
 * $Log: VariableContext.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import java.util.Stack;

import org.apache.commons.collections.FastHashMap;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public final class VariableContext {
    
    /**
     * <code>{@link Stack}</code> that holds all variable 
     * <code>{@link FastHashMap}</code> instances down the 
     * <code>{@link de.xplib.nexd.api.vcl.VCLCollectionReference}</code> tree.
     */
    private final Stack varStack = new Stack();
    
    /**
     * All variables that are visible in the current context.
     */
    private FastHashMap vars;

    /**
     * Constructor.
     */
    protected VariableContext() {
        super();
    }
    
    /**
     * Adds a new level with all previous defined variables.
     */
    protected void push() {
        this.vars = new FastHashMap(this.vars);
        this.vars.setFast(true);
        this.varStack.push(this.vars);
    }
    
    /**
     * Adds an empty level.
     */
    protected void pushEmpty() {
        this.vars = new FastHashMap();
        this.vars.setFast(true);
        this.varStack.push(this.vars);
    }
    
    /**
     * Removes the last level. 
     */
    protected void pop() {
        this.varStack.pop();
    }
    
    /**
     * Removes the last level and sets the internal pointer to the new current
     * variables.
     */
    protected void popReset() {
        this.varStack.pop();
        this.vars = (FastHashMap) this.varStack.peek();
    }
    
    
    /**
     * Returns the value of a variable.
     * 
     * @param nameIn The variable name.
     * @return The value of <code>null</code>.
     */
    protected String get(final String nameIn) {
        return (String) this.vars.get(nameIn);
    }
    
    /**
     * Returns the value of a variable as a byte array,
     * @param nameIn The variable name.
     * @return The variable value as byte array.
     */
    protected byte[] getBytes(final String nameIn) {
        return ((String) this.vars.get(nameIn)).getBytes();
    }
    
    /**
     * Adds a variable to the current level.
     * 
     * @param nameIn The variable name.
     * @param valueIn The variable value.
     */
    protected void put(final String nameIn, final String valueIn) {
        this.vars.put(nameIn, valueIn);
    }
    
    /**
     * Does the current context contain a variable for the given name.
     * @param nameIn The variable name.
     * @return Is there a variable for this name?
     */
    protected boolean contains(final String nameIn) {
        return this.vars.containsKey(nameIn);
    }

}
