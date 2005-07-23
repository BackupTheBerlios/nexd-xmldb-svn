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
 * $Log: StorageExceptionCodes.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.store;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public interface StorageExceptionCodes {

    /**
     * A not closer specified exception.
     */
    short UNKNOWN = 0;
    
    /**
     * Code which will be returned if the requested <code>DBCollection</code>
     * doesn't exist.
     */
    short NO_SUCH_COLLECTION = 1;
    
    /**
     * Code which will be returned if the user tries to create a collection that
     * already exists. 
     */
    short COLLECTION_EXISTS = 2;
    
    /**
     * Code which is returned if a recieved <code>DocumentImpl</code> contains
     * unexected nodes.
     */
    short INVALID_STORED_DOCUMENT = 3;
    
    /**
     * Code which is returned if the <code>nodeType</code> is not known by the 
     * storage. 
     */
    short UNKNOWN_NODE_TYPE = 4;
    
    /**
     * Code which is returned if the <code>nodeType</code> is known by the 
     * storage but not supported. 
     */
    short UNSUPPORTED_NODE_TYPE = 5;
    
    /**
     * Code which is returns if a node is not stored in the database, but the 
     * user tries to update it.
     */
    short NOT_EXISTING_NODE = 6;
    
    /**
     * Code which is returns if a <code>StorageValidationObjectImpl</code> 
     * cannot determine or handle a schema type.
     */
    short INVALID_SCHEMA_TYPE = 7;
    
    /**
     * Code which is returns if a given xpath expression is not correct or it
     * uses unsupported features.
     */
    short BAD_XPATH_EXPRESSION = 8;
}
