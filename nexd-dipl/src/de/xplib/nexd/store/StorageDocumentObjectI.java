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
 * $Log: StorageDocumentObjectI.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.store;

import de.xplib.nexd.xml.DOMDocumentI;


/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public interface StorageDocumentObjectI extends StorageObjectI {
    
    /**
     * Getter method for the document id.
     * 
     * @return The document id.
     */
    //String getDocumentId();

    /**
     * Getter method for the used <code>DocumentCacheI</code>, which holds all 
     * changed node for a later storage update.
     * 
     * @return The used <code>DocumentCacheI</code> instance.
     */
    //DocumentCacheI getCache();

    /**
     * Returns <code>true</code> if the <code>Document</code> is in athe initial
     * loading process.
     * 
     * @return Is the document in loading process?
     */
    //boolean isLoading();

    /**
     * Starts the loading process.
     */
    //void startLoading();

    /**
     * Stops the loading process.
     *
     */
    //void stopLoading();
    
    /**
     * Returns the underlying DOM document.
     * 
     * @return The underlying Document.
     */
    DOMDocumentI getContent();
}
