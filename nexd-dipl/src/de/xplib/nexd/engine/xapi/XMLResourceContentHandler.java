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
 * $Log: XMLResourceContentHandler.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.5  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.4  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi;

import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class XMLResourceContentHandler extends DefaultHandler {
    
    /**
     * The <code>XMLResource</code> that will get the new content.
     * @associates <{de.xplib.nexd.engine.xapi.XMLResourceImpl}>
     * @clientCardinality 0..1
     * @directed true
     * @label fills with content
     * @supplierCardinality 0..*
     */
    protected XMLResource resource = null;
    
    /**
     * <code>StringBuilder</code> that holds the content temporary.
     */
    protected StringBuilder newContent = null;
    
    /**
     * <code>HashMap</code> for namespace to prefix mapping.
     */
    protected Hashtable namespaces = null;
    
    /**
     * <p>Constructor</p>.
     * 
     * @param resourceIn <p>The <code>XMLResource</code> instance.</p>
     */
    public XMLResourceContentHandler(final XMLResource resourceIn) {
        super();
        
        this.resource = resourceIn;
        namespaces = new Hashtable();
    }
    
    /**
     * Receive notification of the beginning of the document.
     *
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#startDocument
     */
    public void startDocument() throws SAXException {
        
        newContent = new StringBuilder();
        newContent.append("<?xml version=\"1.0\"?>");
    }
    
    
    /**
     * Receive notification of the end of the document.
     *
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#endDocument
     */
    public void endDocument() throws SAXException {
        try {
            DocumentBuilder builder = DocumentBuilderFactoryImpl
                                         .newInstance().newDocumentBuilder();
            
            resource.setContentAsDOM(builder.parse(new InputSource(
                    new StringReader(newContent.toString()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Receive notification of the start of a Namespace mapping.
     *
     * @param prefixIn The Namespace prefix being declared.
     * @param uriIn The Namespace URI mapped to the prefix.
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#startPrefixMapping
     */
    public void startPrefixMapping(final String prefixIn, final String uriIn)
            throws SAXException {
        
        namespaces.put(prefixIn, uriIn);
    }
    
    
    /**
     * Receive notification of the end of a Namespace mapping.
     *
     * @param prefixIn The Namespace prefix being declared.
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#endPrefixMapping
     */
    public void endPrefixMapping(final String prefixIn) throws SAXException {
        namespaces.remove(prefixIn);
    }
    
    
    /**
     * Receive notification of the start of an element.
     *
     * @param attributesIn The specified or defaulted attributes.
     * @param uriIn Description of Parameter
     * @param localNameIn Description of Parameter
     * @param qNameIn Description of Parameter
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement(final String uriIn, 
                             final String localNameIn,
                             final String qNameIn, 
                             final Attributes attributesIn)
            throws SAXException {
        
        newContent.append("<");
        newContent.append(qNameIn);
        
        for (int i = 0; i < attributesIn.getLength(); i++) {
            String qName = attributesIn.getQName(i);
            newContent.append(" ");
            newContent.append(qName);
            newContent.append("=");
            newContent.append("\"");
            newContent.append(attributesIn.getValue(i));
            newContent.append("\"");
            
            //Avoid duplicate namespace declarations
            if (qName.startsWith("xmlns")) {
                String lName = attributesIn.getLocalName(i);
                if (lName.equals("xmlns")) {
                    namespaces.remove("");
                } else {
                    namespaces.remove(lName);
                }
            }
        }
        
        Enumeration enu = namespaces.keys();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            newContent.append(" xmlns");
            if (key.length() > 0) {
                newContent.append(":");
                newContent.append(key);
            }
            newContent.append("=");
            newContent.append("\"");
            newContent.append(namespaces.get(key));
            newContent.append("\"");
            namespaces.remove(key);
        }
        
        newContent.append(">");
    }
    
    
    /**
     * Receive notification of the end of an element.
     *
     * @param uriIn Description of Parameter
     * @param localNameIn Description of Parameter
     * @param qNameIn Description of Parameter
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement(final String uriIn, 
                           final String localNameIn, 
                           final String qNameIn) throws SAXException {
        
        newContent.append("</");
        newContent.append(qNameIn);
        newContent.append(">");
    }
    
    
    /**
     * Receive notification of character data inside an element.
     *
     * @param chIn The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the
     * character array.
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(final char[] chIn, final int start, final int length)
            throws SAXException {
        int idx = 0;
        while (idx < length) {
            char chr = chIn[start + idx];
            switch (chr) {
            case '&':
                newContent.append("&amp;");
                break;
            case '<':
                newContent.append("&lt;");
                break;
            case '>':
                newContent.append("&gt;");
                break;
            case '"':
                newContent.append("&quot;");
                break;
            case '\'':
                newContent.append("&apos;");
                break;
            default:
                // If we're outside 7 bit ascii encode as a character ref.
                // Not sure what the proper behavior here should be.
                if ((int) chr > 127) {
                    newContent.append("&#");
                    newContent.append((int) chr);
                    newContent.append(";");
                } else {
                    newContent.append(chr);
                }
            }
            
            idx++;
        }
    }
    
    
    /**
     * Receive notification of ignorable whitespace in element content.
     *
     * @param chIn The whitespace characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the
     * character array.
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(final char[] chIn, 
                                    final int start, 
                                    final int length) throws SAXException {
        
        int idx = 0;
        while (idx < length) {
            newContent.append(chIn[start + idx]);
            idx++;
        }
    }
    
    
    /**
     * Receive notification of a processing instruction.
     *
     * @param targetIn The processing instruction target.
     * @param dataIn The processing instruction data, or null if
     * none is supplied.
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#processingInstruction
     */
    public void processingInstruction(final String targetIn, 
                                      final String dataIn)
            throws SAXException {
        
        newContent.append("<?");
        newContent.append(targetIn);
        newContent.append(" ");
        
        if (dataIn != null) {
            newContent.append(dataIn);
        }
        
        newContent.append("?>");
    }
    
    
    /**
     * Receive notification of a skipped entity.
     *
     * @param nameIn The name of the skipped entity.
     * @exception SAXException Description of Exception
     * @see org.xml.sax.ContentHandler#processingInstruction
     */
    public void skippedEntity(final String nameIn) throws SAXException {
        // no op
    }
    
    
    
}
