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
 * $Log: AbstractVCLParser.java,v $
 * Revision 1.7  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.6  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.5  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.4  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/26 12:14:20  nexd
 * UML documentation.
 *
 * Revision 1.2  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api.vcl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;


/**
 * <p>This is the abstract base class for parser that parses a Virtual
 * Collection Language Schema.</p>
 * <p>The interface of this class is inspired by <code>
 * {@link javax.xml.parsers.DocumentBuilder}</code> class.</p>
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @role __Factory
 * @role __Product
 * @version $Revision: 1.7 $
 */
public abstract class AbstractVCLParser 
    extends DefaultHandler 
    implements VCLParserI {
    
    /**
     * GoF Singlton instance of <code>VCLParserI</code>.
     * @clientCardinality 1
     * @directed true
     * @label creates instance
     * @link aggregation
     * @supplierCardinality 0..1
     */
    private static VCLParserI instance;
    
    /**
     * GoF Singleton pattern method that returns an instance of 
     * AbstractVCLParser. This method uses the system property 
     * "de.xplib.nexd.api.vcl.VCLParserI" to find the used class.
     * 
     * @return An unique instance of AbstractVCLParser
     * @throws XMLDBException If any database specific error occures.
     */
    public static VCLParserI getInstance() throws XMLDBException {
        if (instance == null) {
            instance = createVCLParser();
        }
        return instance;
    }

    /**
     * GoF Factory Pattern method that returns an instance of AbstractVCLParser.
     * This method uses the system property 
     * "de.xplib.nexd.api.vcl.VCLParserI" to find the used class.
     * 
     * @return An instance of VCLParserI
     * @throws XMLDBException If any database specific error occures.
     */
    public static VCLParserI createVCLParser() throws XMLDBException {
        
        String className = System.getProperty(VCL_PARSER_KEY);
        
        try {
            Class clazz = Class.forName(className);
            
            return (VCLParserI) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (InstantiationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (IllegalAccessException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * <p>Parse the content of the given URI as a XML document and return a new
     * {@link VCLSchema} object. An <code>IllegalArgumentException</code> is
     * thrown if the URI is <code>null</code>.
     *
     * @param uriIn <p>The location of the content to be parsed.</p>
     * @return <p>A new {@link VCLSchema} object.</p>
     * @throws IOException <p>If any IO errors occur.</p>
     * @throws SAXException <p>If any parse errors occur.</p>
     * @throws InvalidVCLSchemaException <p>If the given Virtual Collection
     *                                   Language Schema is not valid.</p>
     */
    public VCLSchema parse(final String uriIn) throws IOException,
            SAXException, InvalidVCLSchemaException {

        if (uriIn == null) {
            throw new IllegalArgumentException("URI cannot be null");
        }

        return this.parse(new InputSource(uriIn));
    }

    /**
     * <p>Parse the content of the given URI as a XML document and return a new
     * {@link VCLSchema} object. An <code>IllegalArgumentException</code> is
     * thrown if the URI is <code>null</code>.
     *
     * @param urlIn <p>The location of the content to be parsed.</p>
     * @return <p>A new {@link VCLSchema} object.</p>
     * @throws IOException <p>If any IO errors occur.</p>
     * @throws SAXException <p>If any parse errors occur.</p>
     * @throws InvalidVCLSchemaException <p>If the given Virtual Collection
     *                                   Language Schema is not valid.</p>
     */
    public VCLSchema parse(final URL urlIn) throws IOException,
            SAXException, InvalidVCLSchemaException {

        if (urlIn == null) {
            throw new IllegalArgumentException("URI cannot be null");
        }

        return this.parse(new InputSource(urlIn.openStream()));
    }

    /**
     * <p>Parse the content of the given file as a XML document and return a new
     * {@link VCLSchema} object. An <code>IllegalArgumentException</code> is
     * thrown if the <code>File</code> is <code>null</code>.</p>
     *
     * @param fileIn <p>The file containing the XML to parse.</p>
     * @return <p>A new {@link VCLSchema} object.</p>
     * @throws IOException <p>If any IO errors occur.</p>
     * @throws SAXException <p>If any parse errors occur.</p>
     * @throws InvalidVCLSchemaException <p>If the given Virtual Collection
     *                                   Language Schema is not valid.</p>
     */
    public VCLSchema parse(final File fileIn) throws IOException, SAXException,
            InvalidVCLSchemaException {
        if (fileIn == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        String uri = "file:" + fileIn.getAbsolutePath();
        if (File.separatorChar == '\\') {
            uri = uri.replace('\\', '/');
        }
        return this.parse(new InputSource(uri));
    }

    /**
     * <p>Parse the content of the given <code>InputStream</code> as an XML
     * document and return a new {@link VCLSchema} object. An
     * <code>IllegalArgumentException</code> is thrown if the
     * <code>InputStream</code> is <code>null</code>.</p>
     *
     * @param isIn <p>InputStream containing the content to be parsed.</p>
     * @return <p>A new {@link VCLSchema} object.</p>
     * @throws IOException <p>If any IO errors occur.</p>
     * @throws SAXException <p>If any parse errors occur.</p>
     * @throws InvalidVCLSchemaException <p>If the given Virtual Collection
     *                                   Language Schema is not valid.</p>
     */
    public VCLSchema parse(final InputStream isIn) throws IOException,
            SAXException, InvalidVCLSchemaException {

        if (isIn == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }

        return this.parse(new InputSource(isIn));
    }

    /**
     * <p>Parse the content of the given input source as an XML document and
     * return a new {@link VCLSchema} object.
     * An <code>IllegalArgumentException</code> is thrown if the
     * <code>InputSource</code> is <code>null</code>.</p>
     *
     * @param isIn <p>InputSource containing the content to be parsed.</p>
     * @return <p>A new {@link VCLSchema} object.</p>
     * @throws IOException <p>If any IO errors occur.</p>
     * @throws SAXException <p>If any parse errors occur.</p>
     * @throws InvalidVCLSchemaException <p>If the given Virtual Collection
     *                                   Language Schema is not valid.</p>
     */
    public abstract VCLSchema parse(final InputSource isIn) throws IOException,
            SAXException, InvalidVCLSchemaException;

}