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
package de.xplib.nexd.engine.xml.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

/**
 * Abstract base class for a DOM-Document validator.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractValidator {
    
    /**
     * Comment for <code>tmpDocument</code>
     */
    protected File tmpDocument = null;
    
    /**
     * Comment for <code>tmpSchema</code>
     */
    protected File tmpSchema = null;

    /**
     * @param schemaIn The <code>URL</code> where the Schema or DTD is. 
     * @throws IOException If the temporary files cannot be created.
     */
    public AbstractValidator(final URL schemaIn) throws IOException {
        super();
        
        // create temporary documents
        tmpSchema = File.createTempFile("validate_schema", ".tmp");
        tmpSchema.deleteOnExit();
        
        this.tmpDocument = File.createTempFile("validate_document", ".xml");
        this.tmpDocument.deleteOnExit();
        
        // read schema from else where
        InputStream stream = schemaIn.openStream();
        byte[] bytes       = new byte[stream.available()];
        
        stream.read(bytes, 0, bytes.length);
        stream.close();
        
        // write local schema file
        FileWriter writer = new FileWriter(tmpSchema);
        writer.write(new String(bytes));
        writer.flush();
        writer.close();
    }
    
    /**
     * Writes the given <code>{@link Document} to a temporary file.
     * 
     * @param docIn The <code>Document</code> to write to disk.
     * @throws IOException If the temporary file cannot be written.
     */
    protected void writeToFile(final Document docIn) throws IOException {
        FileWriter writer = new FileWriter(this.tmpDocument);
        //fw.write(new XMLSerializer())
        
        XMLSerializer ser = new XMLSerializer();
        ser.setOutputCharStream(writer);
        ser.serialize(docIn);
        
        writer.flush();
        writer.close();
    }
    
    /**
     * Validates the <code>{@link Document} against a XML-Schema or a DTD.
     * 
     * @param docIn The <code>Document</code> to be valid.
     * @throws IOException If the temporary file cannot be written.
     * @throws NonValidDocumentException If the <code>Document</code> is not 
     *                                   valid.
     */
    public abstract void validate(Document docIn) 
            throws IOException, NonValidDocumentException;

}
