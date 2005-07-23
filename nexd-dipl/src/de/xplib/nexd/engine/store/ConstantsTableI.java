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
 * $Log: ConstantsTableI.java,v $
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 */
package de.xplib.nexd.engine.store;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public interface ConstantsTableI {

    /**
     * Comment for <code>TABLE_COLLECTION</code>
     */
    String TABLE_COLLECTION = "nexd_collection";
    
    /**
     * Name of the database table with the stored DOM attributes.
     */
    String TABLE_DOM_ATTR = "nexd_dom_attr";
    
    /**
     * Name of the database table with the stored DOM elements.
     */
    String TABLE_DOM_ELEM = "nexd_dom_element";
    
    /**
     * Name of the database table with the stored DOM documents.
     */
    String TABLE_DOM_DOCUMENT = "nexd_dom_document";
    
    /**
     * Name of the database table with the stored DOM processing instructions.
     */
    String TABLE_DOM_PI = "nexd_dom_pi";
    
    /**
     * Name of the database table with the stored DOM text nodes.
     */
    String TABLE_DOM_TEXT = "nexd_dom_text";
    
    /**
     * Name of the database table with the stored DOM entity reference nodes.
     */
    String TABLE_DOM_ENTITYREF = "nexd_dom_entityref";
    
    /**
     * Name of the database table with the stored DOM doctype nodes.
     */
    String TABLE_DOM_DOCTYPE = "nexd_dom_doctype";
    
    /**
     * Name of the database table with the stored DOM nodes.
     */
    String TABLE_DOM_NODE = "nexd_dom_node";
    
    /**
     * Name of the database table with the stored collection schema.
     */
    String TABLE_SCHEMA = "nexd_schema";
    
    /**
     * Name of the database table with the stored resource objects.
     */
    String TABLE_RESOURCE = "nexd_resource";
    
    /**
     * Name of the database table with the incremented sequence numbers.
     */
    String TABLE_SEQUENCE = "nexd_sequence";
    
    /**
     * <p>Name of the database table with the collection 2 virtual collection
     * mapping.</p>
     */
    String TABLE_COLLECTION_REF = "nexd_collection_vcollection";
}
