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
 * $Log: VCLDocumentParser.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.VCLSchema;

/**
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @stereotype Helper
 * @version $Revision: 1.2 $
 */
public class VCLDocumentParser {
    
    /**
     * The XMLSerializer used to make a String from an input Document.
     */
    private final XMLSerializer serializer;
    
    /**
     * Constructor.
     */
    public VCLDocumentParser() {
        super();
        
        this.serializer = new XMLSerializer();
    }

    /**
     * <p>This helper method creates an {@link VCLSchema} instance from the 
     * given DOM {@link Document}.</p>
     * 
     * @param docIn <p>The {@link VCLSchema} as a DOM <code>Document</code>.</p>
     * @return <p>The {@link VCLSchema} instance.</p> 
     * @throws XMLDBException <p>If the given <code>docIn</code> is not a valid
     *                        {@link VCLSchema} or something goes wrong with the
     *                        third party xml serializer.</p>
     */
    public VCLSchema parse(final Document docIn)
            throws XMLDBException {
        
        try {
            VCLParserImpl parser = new VCLParserImpl();
            
            StringWriter writer = new StringWriter();
            
            this.serializer.setOutputCharStream(writer);
            this.serializer.serialize(docIn);
            
            writer.flush();
            
            VCLSchema schema = parser.parse(new ByteArrayInputStream(
                    writer.getBuffer().toString().getBytes()));
            
            writer.close();
            
            return schema;
        } catch (IOException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (SAXException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (InvalidVCLSchemaException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
}
