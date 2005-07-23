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
package de.xplib.nexd.engine.xml.dom.xpath;

import java.util.ArrayList;
import java.util.Iterator;

import de.xplib.nexd.engine.xml.xpath.Visitor;
import de.xplib.nexd.xml.xpath.XPathI;

/**
 * Visitor that evaluates an xpath expression relative to a context
 * node by walking over the parse tree of the expression.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 * @see http://sparta-xml.sourceforge.net
 */
public abstract class AbstractXPathVisitor implements Visitor {

    /**
     * Was/Is the last <code>Step</code> a multi level one?
     */
    protected boolean multiLevel;
    
    /**
     * The used <code>XPathI</code> instance.
     */
    protected final XPathI xpath;
    
    /** 
     * The filtered <code>Node</code> instances.
     */
    protected final ArrayList filteredNodes = new ArrayList(1);
    
    /**
     * <code>Iterator</code> containing the currently selected nodes. 
     */
    protected Iterator nodeIterator = null;
        
    /** 
     * same as Boolean.TRUE (which is not supported in J2ME) 
     */
    protected static final Boolean TRUE = Boolean.TRUE;
    
    /** 
     * same as Boolean.FALSE (which is not supported in J2ME) 
     */
    protected static final Boolean FALSE = Boolean.FALSE;
    
    /**
     * Stack holding the eval results.
     */
    protected final BooleanStack exprStack = new BooleanStack();

    /**
     * Constructor.
     * 
     * @param xpathIn The used <code>XPathI</code> instance.
     */
    protected AbstractXPathVisitor(final XPathI xpathIn) {
        super();
        
        xpath = xpathIn;
    }


    /** 
     * Profiling found this to be very heavily used so do not use 
     * java.util.LinkedList 
     */
    protected static class BooleanStack {
        
        /**
         * Top of stack object.
         */
        private Item top;
        
        /**
         * Stack <code>Item</code> class.
         */
        private static class Item {
            
            /**
             * Constructor.
             * 
             * @param boolIn The <code>Boolean</code> value.
             * @param prevIn The previous <code>Item</code> object.
             */
            Item(final Boolean boolIn, final Item prevIn) {
                super();
                
                bool = boolIn;
                prev = prevIn;
            }
            
            /**
             * Value of this.
             */
            final Boolean bool;
            
            /**
             * Previous stack <code>Item</code>.
             */
            final Item prev;
        }
        
        /**
         * Default constructor. 
         */
        protected BooleanStack() {
            super();
        }
        
        /**
         * Pushes a new <code>Boolean</code> value on top of this.
         * 
         * @param boolIn The value.
         */
        public void push(final Boolean boolIn) {
            top = new Item(boolIn, top);
        }
        
        /**
         * Pops a value from the stack.
         * 
         * @return The <code>Boolean</code> value.
         */
        public Boolean pop() {
            Boolean result = top.bool;
            top = top.prev;
            return result;
        }
    }



}
