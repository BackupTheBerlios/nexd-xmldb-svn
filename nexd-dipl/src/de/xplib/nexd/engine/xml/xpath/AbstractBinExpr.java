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
package de.xplib.nexd.engine.xml.xpath;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractBinExpr extends AbstractBooleanExpr {
    
    /**
     * The context <code>AbstractBooleanExpr</code> object.
     * @clientCardinality 0..1
     * @clientRole expr
     * @directed true
     * @supplierCardinality 1
     */
    protected AbstractBooleanExpr expr;
    
    /**
     * Construtor.
     * 
     * @param exprIn The context <code>AbstractBooleanExpr</code> object.
     */
    public AbstractBinExpr(final AbstractBooleanExpr exprIn) {
        super();
        
        this.expr = exprIn;
    }

    /**
     * Returns the context <code>AbstractBooleanExpr</code> object.
     * 
     * @return The context <code>AbstractBooleanExpr</code> object.
     */
    public AbstractBooleanExpr getExpr() {
        return this.expr;
    }
}
