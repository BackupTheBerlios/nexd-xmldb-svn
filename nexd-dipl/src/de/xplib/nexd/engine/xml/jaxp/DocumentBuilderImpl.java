/*
 * Project: nexd 
 * Copyright (C) 2004  Manuel Pichler <manuel.pichler@xplib.de>
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
 * $Log: DocumentBuilderImpl.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.5  2005/04/24 15:00:27  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.4  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xml.jaxp;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.xplib.nexd.engine.xml.dom.DOMImplementationImpl;
import de.xplib.nexd.engine.xml.dom.DocumentImpl;

/**
 * Defines the API to obtain DOM Document instances from an XML
 * document. Using this class, an application programmer can obtain a
 * {@link Document} from XML.<p>
 *
 * An instance of this class can be obtained from the
 * {@link javax.xml.parsers.DocumentBuilderFactory#newDocumentBuilder()} method.
 * Once an instance of this class is obtained, XML can be parsed from a
 * variety of input sources. These input sources are InputStreams,
 * Files, URLs, and SAX InputSources.<p>
 *
 * Note that this class reuses several classes from the SAX API. This
 * does not require that the implementor of the underlying DOM
 * implementation use a SAX parser to parse XML document into a
 * <code>Document</code>. It merely requires that the implementation
 * communicate with the application using these existing APIs.
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DocumentBuilderImpl extends DocumentBuilder {

    /**
     * @label uses
     */

    /*#de.xplib.nexd.engine.xml.jaxp.DocumentBuilderHandler Dependency_Link*/

    /**
     *
     */
    protected DocumentBuilderImpl() {
        super();
    }

    /**
     * Parse the content of the given input source as an XML document
     * and return a new DOM {@link Document} object.
     * An <code>IllegalArgumentException</code> is thrown if the
     * <code>InputSource</code> is <code>null</code> null.
     *
     * @param sourceIn InputSource containing the content to be parsed.
     * @exception IOException If any IO errors occur.
     * @exception SAXException If any parse errors occur.
     * @see org.xml.sax.DocumentHandler
     * @return A new DOM Document object.
     * @see javax.xml.parsers.DocumentBuilder#parse(org.xml.sax.InputSource)
     */
    public Document parse(final InputSource sourceIn) throws SAXException,
            IOException {

        SAXParser parser;
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            parser = spf.newSAXParser();
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }

        DocumentBuilderHandler dbh = new DocumentBuilderHandler(parser);
        parser.getXMLReader().parse(sourceIn);

        DocumentImpl doc = dbh.getDocument();
        doc.setDocumentURI(sourceIn.getSystemId());
        doc.setEncoding(sourceIn.getEncoding());

        return doc;
    }

    /**
     * Indicates whether or not this parser is configured to
     * understand namespaces.
     *
     * @return true if this parser is configured to understand
     *         namespaces; false otherwise.
     * @see javax.xml.parsers.DocumentBuilder#isNamespaceAware()
     */
    public boolean isNamespaceAware() {
        return false;
    }

    /**
     * Indicates whether or not this parser is configured to
     * validate XML documents.
     *
     * @return true if this parser is configured to validate
     *         XML documents; false otherwise.
     * @see javax.xml.parsers.DocumentBuilder#isValidating()
     */
    public boolean isValidating() {
        return false;
    }

    /**
     * Specify the {@link EntityResolver} to be used to resolve
     * entities present in the XML document to be parsed. Setting
     * this to <code>null</code> will result in the underlying
     * implementation using it's own default implementation and
     * behavior.
     *
     * @param resolverIn The <code>EntityResolver</code> to be used to resolve
     *                   entities present in the XML document to be parsed.
     * @see javax.xml.parsers.DocumentBuilder#setEntityResolver(
     * 		org.xml.sax.EntityResolver)
     */
    public void setEntityResolver(final EntityResolver resolverIn) {
        // TODO Auto-generated method stub

    }

    /**
     * Specify the {@link ErrorHandler} to be used by the parser.
     * Setting this to <code>null</code> will result in the underlying
     * implementation using it's own default implementation and
     * behavior.
     *
     * @param handlerIn The <code>ErrorHandler</code> to be used by the parser.
     * @see javax.xml.parsers.DocumentBuilder#setErrorHandler(
     * 		org.xml.sax.ErrorHandler)
     */
    public void setErrorHandler(final ErrorHandler handlerIn) {
        // TODO Auto-generated method stub

    }

    /**
     * Obtain a new instance of a DOM {@link Document} object
     * to build a DOM tree with.
     *
     * @return A new instance of a DOM Document object.
     * @see javax.xml.parsers.DocumentBuilder#newDocument()
     */
    public Document newDocument() {
        return this.getDOMImplementation().createDocument(null, null, null);
    }

    /**
     * Obtain an instance of a {@link DOMImplementation} object.
     *
     * @return A new instance of a <code>DOMImplementation</code>.
     * @see javax.xml.parsers.DocumentBuilder#getDOMImplementation()
     */
    public DOMImplementation getDOMImplementation() {
        return new DOMImplementationImpl();
    }

}