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
 * $Log: SchemaValidator.java,v $
 * Revision 1.2  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.5  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.4  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.w3c.dom.Document;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.engine.xml.util.AbstractValidator;
import de.xplib.nexd.engine.xml.util.DTDValidator;
import de.xplib.nexd.engine.xml.util.NonValidDocumentException;
import de.xplib.nexd.store.StorageValidationObjectI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class SchemaValidator {
    
    /**
     * The concrete <code>AbstractValidator</code> instance.
     */
    private final AbstractValidator validator;
    
    /**
     * The used <code>StorageValidationObjectImpl</code> instance.
     */
    private final StorageValidationObjectI schema;

    /**
     * Construtor.
     * 
     * @param schemaIn The schema used for validation.
     * @throws IOException If a temorary file cannot be written.
     * @throws XMLDBException If a database specific error occures.
     */
    public SchemaValidator(final StorageValidationObjectI schemaIn)
            throws IOException, XMLDBException {
        
        super();

        this.schema = schemaIn;
        
        File file = File.createTempFile("nexdTempSchema", "schema");
        file.deleteOnExit();
        
        FileWriter writer = new FileWriter(file);
        writer.write(schemaIn.getContent());
        writer.flush();
        writer.close();
        
        if (this.schema.getType() == StorageValidationObjectI.DTD) {
            this.validator = new DTDValidator(file.toURL());
        } else {
            // FIXME Implement a xsd validator.
            throw new XMLDBException(
                    ErrorCodes.VENDOR_ERROR, 
                    "XSDValidator is not implemented.");
        }
    }
    
    /**
     * Getter method for the used <code>StorageValidationObjectImpl</code> 
     * object.
     * 
     * @return The instance.
     */
    public final StorageValidationObjectI getSchema() {
        return this.schema;
    }
    
    /**
     * Validates a single DOM document <code>docIn</code> against a schema.
     * 
     * @param docIn The document to validate.
     * @throws IOException If the <code>docIn</code> cannot be written to a 
     *                     temporary file.
     * @throws InvalidCollectionDocumentException If a document in the 
     *                                            collection doesn't fit to the
     *                                            schema. 
     */
    public final void validate(final Document docIn)
            throws IOException, InvalidCollectionDocumentException {
       
        try {
            this.validator.validate(docIn);
        } catch (NonValidDocumentException e) {
            throw new InvalidCollectionDocumentException("", e);
        }
    }

}
