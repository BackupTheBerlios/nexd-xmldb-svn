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

import java.io.IOException;
import java.net.URL;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Class that validates a {@link org.w3c.dom.Document} against a XML-Schema.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class DTDValidator extends AbstractValidator {
    
    /**
     * 
     */
    private final DOMParser parser;

    /**
     * Constructor.
     * 
     * @param dtdIn The Document Type Definition for validation.
     * @throws IOException If the schema cannot be located.
     */
    public DTDValidator(final URL dtdIn) throws IOException {
        super(dtdIn);
        
        this.parser = new DOMParser();
        this.parser.setErrorHandler(new ValidatorErrorHandler());
        
        try {
            this.parser.setFeature(
                    "http://xml.org/sax/features/validation", true);
        } catch (Exception e) {
            e.getCause();
        }
        try {
            this.parser.setFeature(
                    "http://xml.org/sax/features/namespaces", true);
        } catch (Exception e) {
            e.getCause();
        }
    }

    /**
     * Validates the <code>{@link Document} against a XML-Schema or a DTD.
     * 
     * @param docIn The <code>Document</code> to be valid.
     * @throws IOException If the temporary file cannot be written.
     * @throws NonValidDocumentException If the <code>Document</code> is not 
     *                                   valid.
     * @see de.xplib.nexd.engine.xml.util.ValidatorI#validate(
     *      org.w3c.dom.Document)
     */
    public void validate(final Document docIn) 
            throws NonValidDocumentException, IOException {
        
        Element elem = docIn.getDocumentElement();
        if (elem == null) {
            throw new NonValidDocumentException(
                    "The document doesn't has a documentElement.");
        }
        
        DocumentType doctype = docIn.getImplementation().createDocumentType(
                elem.getTagName(), null, this.tmpSchema.getAbsolutePath());
        
        if (docIn.getDoctype() == null) {
            docIn.appendChild(doctype);
        } else {
            docIn.replaceChild(doctype, docIn.getDoctype());
        }
        
        this.writeToFile(docIn);
        
        try {
            this.parser.parse(this.tmpDocument.getAbsolutePath());
        } catch (SAXException e) {
            throw new NonValidDocumentException(e.getMessage());
        }
    }

}
