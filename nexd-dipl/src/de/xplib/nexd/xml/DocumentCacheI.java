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
 * $Log: DocumentCacheI.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.xml;

import org.w3c.dom.NodeList;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public interface DocumentCacheI {
    /**
     * @clientCardinality 0..1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     */
    /*#private DOMNodeI lnkDOMNodeI;*/

    /**
     * Getter method for the <code>NodeList</code> with the deleted 
     * <code>Node</code>s.
     * 
     * @return The deleted <code>Node</code>s.
     */
    NodeList getDeleted();

    /**
     * Getter method for the <code>NodeList</code> with the inserted 
     * <code>Node</code>s.
     * 
     * @return The inserted <code>Node</code>s.
     */
    NodeList getInserted();

    /**
     * Getter method for the <code>NodeList</code> with the changed
     * <code>Node</code>s.
     * 
     * @return The changed <code>Node</code>s.
     */
    NodeList getChanged();

    /**
     * Clears all <code>NodeList</code> instances. 
     */
    void clear();
}