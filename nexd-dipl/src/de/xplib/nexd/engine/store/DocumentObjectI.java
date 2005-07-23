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
 * $Log: DocumentObjectI.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package de.xplib.nexd.engine.store;

import de.xplib.nexd.store.StorageDocumentObjectI;
import de.xplib.nexd.xml.DOMDocumentI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public interface DocumentObjectI extends StorageDocumentObjectI, DOMDocumentI {

    /**
     * @clientCardinality 1
     * @clientRole internalId
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#NestedSetIId lnkNestedSetIId*/

    /**
     * Starts the loading process.
     */
    void startLoading();

    /**
     * Stops the loading process.
     *
     */
    void stopLoading();

}
