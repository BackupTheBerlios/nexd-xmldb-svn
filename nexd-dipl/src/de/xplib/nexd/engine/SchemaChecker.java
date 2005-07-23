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
 * $Log: SchemaChecker.java,v $
 * Revision 1.2  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.w3c.dom.Document;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.store.StorageCollectionI;
import de.xplib.nexd.store.StorageException;
import de.xplib.nexd.store.StorageI;
import de.xplib.nexd.store.StorageObjectI;
import de.xplib.nexd.store.StorageValidationObjectI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class SchemaChecker {

    /**
     * The associated {@link StorageI} object.
     */
    private final StorageI storage;
    
    /**
     * Constructor.
     * 
     * @param storageIn The associated {@link StorageI} object. 
     */
    public SchemaChecker(final StorageI storageIn) {
        super();
        
        this.storage = storageIn;
    }
    
    /**
     * Validates the input <code>Document</code> against the DTD or XML-Schema 
     * in the given {@link StorageCollectionI}, if it exists.
     * 
     * @param collIn The context <code>StorageCollectionI</code>.
     * @param docIn The <code>Document</code> to validate.
     * @throws XMLDBException If any database specific error occures or the 
     *                        <code>Document</code> is not not in the schema.
     */
    public void validate(final StorageCollectionI collIn, 
                         final Document docIn) throws XMLDBException {
        

        StorageValidationObjectI schema;
        try {
            schema = this.storage.queryValidationObject(collIn);
        } catch (StorageException e) {
            throw new XMLDBException(
                    ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
        if (schema != null) {
            SchemaValidator validator;
            try {
                validator = new SchemaValidator(schema);
                validator.validate(docIn);
            } catch (IOException e) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e.getMessage());
            } catch (InvalidCollectionDocumentException e) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e.getMessage());
            }
        }
    }
    
    /**
     * Validates all XML-Documents in the given <code>collIn</code>, against 
     * the DTD or XML-Schema that is located by the <code>URL schemaIn</code>.
     * 
     * @param collIn The context <code>StorageCollectionI</code> instance.
     * @param schemaIn Locates the DTD or XML-Schema.
     * @return An instance of the database representation of the DTD or 
     *         XML-Schema.
     * @throws InvalidCollectionDocumentException If any XML-Document in the 
     *         collection doesn't match against the DTD or XML-Schema.
     * @throws InvalidSchemaException If it is not a XML-Schema and no DTD, or 
     *         it uses a dialect that isn't supported.  
     * @throws IOException If something goes wrong while the content from 
     *         <code>schemaIn</code> is read.
     * @throws XMLDBException If any database specific error occures.
     */
    public StorageValidationObjectI validate(final StorageCollectionI collIn,
                                             final URL schemaIn) 
            throws InvalidCollectionDocumentException,
                   InvalidSchemaException,
                   IOException,
                   XMLDBException {
        
        InputStream stream = schemaIn.openStream();
        byte[] bytes   = new byte[stream.available()];
        
        stream.read(bytes, 0, bytes.length);
        stream.close();
        
        SchemaValidator validator;
        
        try {
            
            StorageValidationObjectI validObject = this.storage.getFactory()
                    .createValidationObject(new String(bytes));
    
            validator = new SchemaValidator(validObject);
            
            String[] names = this.storage.queryObjects(collIn);
            for (int i = 0; i < names.length; i++) {
                StorageObjectI res = this.storage.queryObject(
                        collIn, names[i]);
                
                if (res.getType() == StorageObjectI.BINARY) {
                    continue;
                }
                validator.validate((Document) res);
            }
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
        return validator.getSchema();
    }
}
