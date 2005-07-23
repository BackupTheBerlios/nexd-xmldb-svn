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

import de.xplib.nexd.xml.xpath.XPathException;


/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public class OrExpr extends AbstractBinExpr {

    /**
     * Construtor.
     * 
     * @param exprIn The context <code>AbstractBooleanExpr</code> object.
     */
    public OrExpr(final AbstractBooleanExpr exprIn) {
        super(exprIn);
    }
    
    /**
     * <Some description here>
     * 
     * @param visitor
     * @see de.xplib.nexd.engine.xml.xpath.AbstractBooleanExpr#accept(
     *      de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor)
     */
    public void accept(final BooleanExprVisitor visitor) throws XPathException {
        visitor.visit(this);
    }

}
