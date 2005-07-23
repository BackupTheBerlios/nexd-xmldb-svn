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
 * $Log: VCLParserI.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package de.xplib.nexd.api.vcl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public interface VCLParserI {
    
    /**
     * String that is used to find the concrete <code>VCLParserI</code> 
     * implementation, that will be used by the NEXD Engine.
     */
    String VCL_PARSER_KEY = "de.xplib.nexd.api.vcl.VCLParserI";

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
    VCLSchema parse(final String uriIn) throws IOException, SAXException,
            InvalidVCLSchemaException;

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
    VCLSchema parse(final URL urlIn) throws IOException, SAXException,
            InvalidVCLSchemaException;

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
    VCLSchema parse(final File fileIn) throws IOException, SAXException,
            InvalidVCLSchemaException;

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
    VCLSchema parse(final InputStream isIn) throws IOException, SAXException,
            InvalidVCLSchemaException;

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
    VCLSchema parse(final InputSource isIn) throws IOException, SAXException,
            InvalidVCLSchemaException;
}