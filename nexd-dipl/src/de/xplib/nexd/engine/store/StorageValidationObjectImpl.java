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
package de.xplib.nexd.engine.store;

import de.xplib.nexd.store.InternalIdI;
import de.xplib.nexd.store.StorageEntityI;
import de.xplib.nexd.store.StorageException;
import de.xplib.nexd.store.StorageValidationObjectI;

/**
 * This class holds a XML-Schema or a DTD for a <code>StorageCollectionImpl
 * </code>. All XML-Documents stored in this <code>StorageCollectionImpl</code> 
 * must be valid against this <code>StorageValidationObjectImpl</code> object.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public final class StorageValidationObjectImpl 
    implements StorageEntityI, StorageValidationObjectI {
    
    /**
     * The type of the <code>StorageValidationObjectImpl</code> object.
     */
    private byte type = 0;
    
    /**
     * The concrete content of the <code>StorageValidationObjectImpl</code> 
     * object.
     */
    private String schema = null;
    
    /**
     * Construtor that tries to determine the type of the schema.
     * 
     * @param schemaIn The concrete content of the schema.
     * @throws StorageException If the type of the schema does not match to the
     *                          content.
     */
    public StorageValidationObjectImpl(final String schemaIn) 
            throws StorageException {
        
        super();
        
        this.type   = this.determineType(schemaIn);
        this.schema = schemaIn;
    }
    
    /**
     * Construtor.
     * 
     * @param typeIn The type of the schema.
     * @param schemaIn The concrete content of the schema.
     * @throws StorageException If the type of the schema does not match to the
     *                          content.
     */
    public StorageValidationObjectImpl(final byte typeIn, final String schemaIn)
            throws StorageException {
        
        super();
        
        this.type = typeIn;
        this.setSchema(schemaIn);
    }
    
    /**
     * Getter method for the schema content.
     * 
     * @return The schema content.
     */
    public String getContent() {
        return schema;
    }
    
    /**
     * Setter method for the schema content.
     * 
     * @param schemaIn The schema content.
     * @throws StorageException If the schema content is invalid or doesn't 
     *                          match to the type.
     */
    public void setSchema(final String schemaIn) throws StorageException {
        
        if (this.type != 0 && this.type != this.determineType(schemaIn)) {
            throw new StorageException(StorageException.INVALID_SCHEMA_TYPE);
        }
        this.schema = schemaIn;
    }
    
    /**
     * Getter method for the type of the schema.
     * 
     * @return Type of the schema.
     */
    public byte getType() {
        return type;
    }
    
    /**
     * Setter method for the schema type.
     * 
     * @param typeIn The type of the schema.
     * @throws StorageException If the type doesn't match to the schema it self.
     */
    public void setType(final byte typeIn) throws StorageException {
        
        if (this.schema != null && typeIn != this.determineType(this.schema)) {
            throw new StorageException(StorageException.INVALID_SCHEMA_TYPE);
        }
        this.type = typeIn;
    }
    
    /**
     * Tries to determine the type of the schema.
     * 
     * @param schemaIn The schema to test.
     * @return The type of the schema.
     * @throws StorageException If the type cannot be determined.
     */
    protected byte determineType(final String schemaIn)
            throws StorageException {
        
        byte maybe;
        if (schemaIn.indexOf(":schema") != -1 
                && schemaIn.indexOf("http://www.w3.org/2001/XMLSchema") != -1) {
            
            maybe = XML_SCHEMA;
        } else if (schemaIn.indexOf("<!ELEMENT") != -1 
                && schemaIn.indexOf("<!DOCTYPE") == -1) {
            
            maybe = DTD;
        } else {
            throw new StorageException(StorageException.INVALID_SCHEMA_TYPE);
        }
        return maybe;
    }

    /**
     * Returns the internal id.
     * 
     * @return
     * @see de.xplib.nexd.store.StorageEntityI#getInternalId()
     */
    public InternalIdI getInternalId() {
        return null;
    }
}
