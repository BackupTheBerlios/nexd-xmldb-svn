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
 * $Log: AbstractXMLResource.java,v $
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 */
package de.xplib.nexd.engine.xapi;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.XMLSerializer;
import org.sixdml.dbmanagement.SixdmlResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;
import de.xplib.nexd.xml.DOMDocumentI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractXMLResource 
    extends AbstractResource 
    implements SixdmlResource {
    
    /**
     * Logging instance for xml resource instances.
     */
    protected static final Log LOG = LogFactory.getLog(
            AbstractXMLResource.class);
    
    /**
     * The content of this <code>VirtualResource</code>.
     */
    protected Node content;
    
    /**
     * The id of the parent <code>Document</code>, this is typically for subsets
     * of a <code>Document</code> returned by a query.
     */
    private String documentId = null;

    /**
     * Constructor.
     * 
     * @param parentIn The parent collection
     * @param resourceIdIn The id of this resource.
     */
    public AbstractXMLResource(final AbstractCollection parentIn,
                               final String resourceIdIn) {
        super(parentIn, resourceIdIn, XMLResource.RESOURCE_TYPE);
    }

    /**
    * Returns the unique id for the parent document to this 
    * <code>Resource</code> or <code>null</code> if the <code>Resource</code> 
    * does not have a parent document. <code>getDocumentId()</code> is typically
    * used with <code>Resource</code> instances retrieved using a query. It 
    * enables accessing the parent document of the <code>Resource</code> even 
    * if the <code>Resource</code> is a child node of the document. If the 
    * <code>Resource</code> was not obtained through a query then 
    * <code>getId()</code> and <code>getDocumentId()</code> will return the 
    * same id.
    *
    * @return The id for the parent document of this <code>Resource</code> or 
    *         <code>null</code> if there is no parent document for this 
    *         <code>Resource</code>.
    * @exception XMLDBException With expected error codes.<br />
    *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *            specific errors that occur.<br /> 
     * @see org.xmldb.api.modules.XMLResource#getDocumentId()
     */
    public final String getDocumentId() throws XMLDBException {
        if (this.documentId == null) {
            Node node = this.getContentAsDOM();
            if (node instanceof DOMDocumentI) {
                this.documentId = ((DOMDocumentI) node).getDocumentId();
            }
        }
        return this.documentId;
    }

    /**
    * Returns the content of the <code>Resource</code> as a DOM Node.
    *
    * @return The XML content as a DOM <code>Node</code>
    * @exception XMLDBException With expected error codes.<br />
    *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *            specific errors that occur.<br />
     * @see org.xmldb.api.modules.XMLResource#getContentAsDOM()
     */
    public final Node getContentAsDOM() throws XMLDBException {
        return this.content;
    }

    /**
     * Sets the content of the <code>Resource</code> using a DOM Node as the
     * source.
     *
     * @param contentIn The new content value
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.INVALID_RESOURCE</code> if the content value 
     *            provided is <code>null</code>.<br />
     *            <code>ErrorCodes.WRONG_CONTENT_TYPE</code> if the content 
     *            provided in not a valid DOM <code>Node</code>.
     * @see org.xmldb.api.modules.XMLResource#setContentAsDOM(org.w3c.dom.Node)
     */
    public void setContentAsDOM(final Node contentIn) 
            throws XMLDBException {
        
        this.content = contentIn;
    }

    /**
     * Allows you to use a <code>ContentHandler</code> to parse the XML data 
     * from the database for use in an application.
     *
     * @param handler The SAX <code>ContentHandler</code> to use to handle the
     *        <code>Resource</code> content.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     *            <code>ErrorCodes.INVALID_RESOURCE</code> if the 
     *            <code>ContentHandler</code> provided is <code>null</code>.
     * @see org.xmldb.api.modules.XMLResource#getContentAsSAX(
     *      org.xml.sax.ContentHandler)
     */
    public final void getContentAsSAX(final ContentHandler handler) 
            throws XMLDBException {
        
        try {
            
            StringWriter writer = new StringWriter();
            
            XMLSerializer ser = new XMLSerializer();
            ser.setOutputCharStream(writer);
            
            ser.serialize((Document) this.content);
            
            writer.flush();
            
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.getXMLReader().setContentHandler(handler);
            parser.getXMLReader().parse(new InputSource(new StringReader(
                    writer.getBuffer().toString())));
            
            writer.close();
        } catch (IOException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (SAXException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (FactoryConfigurationError e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }

    }

    /**
     * Sets the content of the <code>Resource</code> using a SAX 
     * <code>ContentHandler</code>.
     *
     * @return A SAX <code>ContentHandler</code> that can be used to add content
     *         into the <code>Resource</code>.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     * @see org.xmldb.api.modules.XMLResource#setContentAsSAX()
     */
    public ContentHandler setContentAsSAX() throws XMLDBException {
        return new XMLResourceContentHandler(this);
    }

    /**
     * Retrieves the content from the resource. The type of the content varies
     * depending what type of resource is being used.
     *
     * @return The content of the resource.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#getContent()
     */
    public final Object getContent() throws XMLDBException {
        
        Object result = null;
        if (this.content instanceof Document) {
            
            StringWriter writer = new StringWriter();
            
            XMLSerializer xser = new XMLSerializer();
            xser.setOutputCharStream(writer);
            
            String xml = "";
            try {
                xser.serialize((Document) this.content);
                xml = writer.toString();
                writer.close();
            } catch (IOException e) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e.getMessage());
            }
            result = xml;
        }
        
        return result;
    }

    /**
     * Sets the content for this resource. The type of content that can be set
     * depends on the type of resource being used.
     *
     * @param valueIn The content value to set for the resource.
     * @exception XMLDBException With expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br /> 
     * @see org.xmldb.api.base.Resource#setContent(java.lang.Object)
     */
    public void setContent(final Object valueIn) throws XMLDBException {
        
        Node node;
        
        if (valueIn instanceof Node) {
            node = (Node) valueIn;
        } else if (valueIn instanceof String) {
            String value    = (String) valueIn;
            File sourceFile = new File(value);
            
            
            InputSource stream;
            if (sourceFile.exists() && sourceFile.isFile()) {
                stream = new InputSource(sourceFile.getAbsolutePath());
            } else if (value.indexOf('<') >= 0) {
                stream = new InputSource(new StringReader(value));
            } else {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, "Invalid content type.");
            }
            
            try {
                DocumentBuilder builder = DocumentBuilderFactoryImpl
                                                .newInstance()
                                                .newDocumentBuilder();
                
                node = builder.parse(stream);
            } catch (ParserConfigurationException e) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e.getMessage());
            } catch (FactoryConfigurationError e) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e.getMessage());
            } catch (SAXException e) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e.getMessage());
            } catch (IOException e) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e.getMessage());
            }
        } else {
            throw new XMLDBException(
                    ErrorCodes.VENDOR_ERROR, "Invalid content type.");
        }
        this.setContentAsDOM(node);
    }
    
    /**
     * Gets the name of the resource.
     *
     * @return The name of the resource
     * @see org.sixdml.dbmanagement.SixdmlResource#getName()
     */
    public String getName() {
        String name = null;
        try {
            name = this.getDocumentId();
        } catch (XMLDBException e) {
            LOG.error(e.getMessage(), e);
        }
        return name;
    }
}
