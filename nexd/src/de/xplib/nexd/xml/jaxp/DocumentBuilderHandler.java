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
package de.xplib.nexd.xml.jaxp;

import java.io.IOException;

import javax.xml.parsers.SAXParser;

import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import de.xplib.nexd.xml.dom.DOMImplementationImpl;
import de.xplib.nexd.xml.dom.DocumentImpl;
import de.xplib.nexd.xml.dom.ElementImpl;
import de.xplib.nexd.xml.dom.TextImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DocumentBuilderHandler implements ContentHandler,
        DTDHandler, EntityResolver, ErrorHandler, DeclHandler, LexicalHandler {
    
    /**
     * Comment for <code>document</code>
     */
    private DocumentImpl document = null;
    
    /**
     * Comment for <code>element</code>
     */
    private ElementImpl element = null;
    
    /**
     * Comment for <code>text</code>
     */
    private TextImpl text = null;

    /**
     * @param saxParserIn ..
     * @throws SAXException ..
     */
    protected DocumentBuilderHandler(final SAXParser saxParserIn) 
    		throws SAXException {
        super();
        
        XMLReader reader = saxParserIn.getXMLReader();
        
        reader.setContentHandler(this);
        reader.setDTDHandler(this);
        reader.setEntityResolver(this);
        reader.setErrorHandler(this);
        reader.setProperty(
                "http://xml.org/sax/properties/declaration-handler", this);
        reader.setProperty(
                "http://xml.org/sax/properties/lexical-handler", this);
        reader.setFeature("http://xml.org/sax/features/namespaces", true);
    }

    /**
     * Receive an object for locating the origin of SAX document events.
     *
     * <p>SAX parsers are strongly encouraged (though not absolutely
     * required) to supply a locator: if it does so, it must supply
     * the locator to the application by invoking this method before
     * invoking any of the other methods in the ContentHandler
     * interface.</p>
     *
     * <p>The locator allows the application to determine the end
     * position of any document-related event, even if the parser is
     * not reporting an error.  Typically, the application will
     * use this information for reporting its own errors (such as
     * character content that does not match an application's
     * business rules).  The information returned by the locator
     * is probably not sufficient for use with a search engine.</p>
     *
     * <p>Note that the locator will return correct information only
     * during the invocation SAX event callbacks after
     * {@link #startDocument startDocument} returns and before
     * {@link #endDocument endDocument} is called.  The
     * application should not attempt to use it at any other time.</p>
     *
     * @param locator an object that can return the location of
     *                any SAX document event
     * @see org.xml.sax.Locator
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(final Locator locator) {
        // TODO Auto-generated method stub

    }

    /**
     * Receive notification of the beginning of a document.
     *
     * <p>The SAX parser will invoke this method only once, before any
     * other event callbacks (except for {@link #setDocumentLocator 
     * setDocumentLocator}).</p>
     *
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
        
        // create a new Document
        DOMImplementationImpl impl = new DOMImplementationImpl();
        this.document = (DocumentImpl) impl.createDocument(null, null, null);
        
        // reset all temporary properties
        this.element = null;
        this.text    = null;
    }

    /**
     * Receive notification of the end of a document.
     *
     * <p><strong>There is an apparent contradiction between the
     * documentation for this method and the documentation for {@link
     * org.xml.sax.ErrorHandler#fatalError}.  Until this ambiguity is
     * resolved in a future major release, clients should make no
     * assumptions about whether endDocument() will or will not be
     * invoked when the parser has reported a fatalError() or thrown
     * an exception.</strong></p>
     *
     * <p>The SAX parser will invoke this method only once, and it will
     * be the last method invoked during the parse.  The parser shall
     * not invoke this method until it has either abandoned parsing
     * (because of an unrecoverable error) or reached the end of
     * input.</p>
     *
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
    }

    /**
     * Begin the scope of a prefix-URI Namespace mapping.
     *
     * <p>The information from this event is not necessary for
     * normal Namespace processing: the SAX XML reader will 
     * automatically replace prefixes for element and attribute
     * names when the <code>http://xml.org/sax/features/namespaces</code>
     * feature is <var>true</var> (the default).</p>
     *
     * <p>There are cases, however, when applications need to
     * use prefixes in character data or in attribute values,
     * where they cannot safely be expanded automatically; the
     * start/endPrefixMapping event supplies the information
     * to the application to expand prefixes in those contexts
     * itself, if necessary.</p>
     *
     * <p>Note that start/endPrefixMapping events are not
     * guaranteed to be properly nested relative to each other:
     * all startPrefixMapping events will occur immediately before the
     * corresponding {@link #startElement startElement} event, 
     * and all {@link #endPrefixMapping endPrefixMapping}
     * events will occur immediately after the corresponding
     * {@link #endElement endElement} event,
     * but their order is not otherwise 
     * guaranteed.</p>
     *
     * <p>There should never be start/endPrefixMapping events for the
     * "xml" prefix, since it is predeclared and immutable.</p>
     *
     * @param prefix the Namespace prefix being declared.
     *	An empty string is used for the default element namespace,
     *	which has no prefix.
     * @param uri the Namespace URI the prefix is mapped to
     * @throws org.xml.sax.SAXException the client may throw
     *            an exception during processing
     * @see #endPrefixMapping
     * @see #startElement
     * @see org.xml.sax.ContentHandler#startPrefixMapping(
     * 		java.lang.String, java.lang.String)
     */
    public void startPrefixMapping(final String prefix, final String uri)
            throws SAXException {

        System.out.println("startPrefixMapping(" + prefix + ", " + uri + ")");
    }

    /**
     * End the scope of a prefix-URI mapping.
     *
     * <p>See {@link #startPrefixMapping startPrefixMapping} for 
     * details.  These events will always occur immediately after the
     * corresponding {@link #endElement endElement} event, but the order of 
     * {@link #endPrefixMapping endPrefixMapping} events is not otherwise
     * guaranteed.</p>
     *
     * @param prefix the prefix that was being mapped.
     *	This is the empty string when a default mapping scope ends.
     * @throws org.xml.sax.SAXException the client may throw
     *            an exception during processing
     * @see #startPrefixMapping
     * @see #endElement
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(final String prefix) throws SAXException {
        System.out.println("endPrefixMapping(" + prefix + ")");
    }

    /**
     * Receive notification of the beginning of an element.
     *
     * <p>The Parser will invoke this method at the beginning of every
     * element in the XML document; there will be a corresponding
     * {@link #endElement endElement} event for every startElement event
     * (even when the element is empty). All of the element's content will be
     * reported, in order, before the corresponding endElement
     * event.</p>
     *
     * <p>This event allows up to three name components for each
     * element:</p>
     *
     * <ol>
     * <li>the Namespace URI;</li>
     * <li>the local name; and</li>
     * <li>the qualified (prefixed) name.</li>
     * </ol>
     *
     * <p>Any or all of these may be provided, depending on the
     * values of the <var>http://xml.org/sax/features/namespaces</var>
     * and the <var>http://xml.org/sax/features/namespace-prefixes</var>
     * properties:</p>
     *
     * <ul>
     * <li>the Namespace URI and local name are required when 
     * the namespaces property is <var>true</var> (the default), and are
     * optional when the namespaces property is <var>false</var> (if one is
     * specified, both must be);</li>
     * <li>the qualified name is required when the namespace-prefixes property
     * is <var>true</var>, and is optional when the namespace-prefixes property
     * is <var>false</var> (the default).</li>
     * </ul>
     *
     * <p>Note that the attribute list provided will contain only
     * attributes with explicit values (specified or defaulted):
     * #IMPLIED attributes will be omitted.  The attribute list
     * will contain attributes used for Namespace declarations
     * (xmlns* attributes) only if the
     * <code>http://xml.org/sax/features/namespace-prefixes</code>
     * property is true (it is false by default, and support for a 
     * true value is optional).</p>
     *
     * <p>Like {@link #characters characters()}, attribute values may have
     * characters that need more than one <code>char</code> value.  </p>
     *
     * @param uri the Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed
     * @param localName the local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed
     * @param qName the qualified name (with prefix), or the
     *        empty string if qualified names are not available
     * @param atts the attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.  The value of this object after
     *        startElement returns is undefined
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see #endElement
     * @see org.xml.sax.Attributes
     * @see org.xml.sax.helpers.AttributesImpl
     * @see org.xml.sax.ContentHandler#startElement(
     * 		java.lang.String, java.lang.String, 
     * 		java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(final String uri, 
                             final String localName, 
                             final String qName,
                             final Attributes atts) throws SAXException {
        
        ElementImpl elem = null;
        if (uri == null || uri.equals("")) {
            elem = (ElementImpl) this.document.createElement(localName);
        } else {
            elem = (ElementImpl) this.document.createElementNS(uri, qName);
        }
        
        for (int i = 0, size = atts.getLength(); i < size; i++) {
            
            String nsURI = atts.getURI(i);
            if (nsURI.equals("")) {
                elem.setAttribute(atts.getLocalName(i), atts.getValue(i));
            } else {
                elem.setAttributeNS(nsURI, atts.getQName(i), atts.getValue(i));
            }
        }
        
        if (this.element == null) {
            this.document.appendChild(elem);
        } else {
            this.element.appendChild(elem);
        }
        
        this.element = elem;
        this.text    = null;
    }

    /**
     * Receive notification of the end of an element.
     *
     * <p>The SAX parser will invoke this method at the end of every
     * element in the XML document; there will be a corresponding
     * {@link #startElement startElement} event for every endElement 
     * event (even when the element is empty).</p>
     *
     * <p>For information on the names, see startElement.</p>
     *
     * @param uri the Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed
     * @param localName the local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed
     * @param qName the qualified XML name (with prefix), or the
     *        empty string if qualified names are not available
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see org.xml.sax.ContentHandler#endElement(
     * 		java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(final String uri, 
                           final String localName, 
                           final String qName) throws SAXException {
        
        Node parent = this.element.getParentNode(); 
        if (parent.getNodeType() == Node.ELEMENT_NODE) {
            this.element = (ElementImpl) parent;
        } else {
            this.element = null;
        }

        this.text = null;
    }

    /**
     * Receive notification of character data.
     *
     * <p>The Parser will call this method to report each chunk of
     * character data.  SAX parsers may return all contiguous character
     * data in a single chunk, or they may split it into several
     * chunks; however, all of the characters in any single event
     * must come from the same external entity so that the Locator
     * provides useful information.</p>
     *
     * <p>The application must not attempt to read from the array
     * outside of the specified range.</p>
     *
     * <p>Individual characters may consist of more than one Java
     * <code>char</code> value.  There are two important cases where this
     * happens, because characters can't be represented in just sixteen bits.
     * In one case, characters are represented in a <em>Surrogate Pair</em>,
     * using two special Unicode values. Such characters are in the so-called
     * "Astral Planes", with a code point above U+FFFF.  A second case involves
     * composite characters, such as a base character combining with one or
     * more accent characters. </p>
     *
     * <p> Your code should not assume that algorithms using
     * <code>char</code>-at-a-time idioms will be working in character
     * units; in some cases they will split characters.  This is relevant
     * wherever XML permits arbitrary characters, such as attribute values,
     * processing instruction data, and comments as well as in data reported
     * from this method.  It's also generally relevant whenever Java code
     * manipulates internationalized text; the issue isn't unique to XML.</p>
     *
     * <p>Note that some parsers will report whitespace in element
     * content using the {@link #ignorableWhitespace ignorableWhitespace}
     * method rather than this one (validating parsers <em>must</em> 
     * do so).</p>
     *
     * @param ch the characters from the XML document
     * @param start the start position in the array
     * @param length the number of characters to read from the array
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see #ignorableWhitespace 
     * @see org.xml.sax.Locator
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(final char[] ch, final int start, final int length)
            throws SAXException {

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < length + start; i++) {
            sb.append(ch[i]); 
        }
        
        if (this.text != null) {
            this.text.appendData(sb.toString());
        } else {
            this.text = (TextImpl) this.document.createTextNode(sb.toString());
            this.element.appendChild(this.text); 
        }
    }

    /**
     * Receive notification of ignorable whitespace in element content.
     *
     * <p>Validating Parsers must use this method to report each chunk
     * of whitespace in element content (see the W3C XML 1.0
     * recommendation, section 2.10): non-validating parsers may also
     * use this method if they are capable of parsing and using
     * content models.</p>
     *
     * <p>SAX parsers may return all contiguous whitespace in a single
     * chunk, or they may split it into several chunks; however, all of
     * the characters in any single event must come from the same
     * external entity, so that the Locator provides useful
     * information.</p>
     *
     * <p>The application must not attempt to read from the array
     * outside of the specified range.</p>
     *
     * @param ch the characters from the XML document
     * @param start the start position in the array
     * @param length the number of characters to read from the array
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see #characters
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(final char[] ch, 
                                    final int start, 
                                    final int length) throws SAXException {

        StringBuilder sb = new StringBuilder();
        for (int i = start; i < length + start; i++) {
            sb.append(ch[i]); 
        }
        
        if (this.text != null) {
            this.text.appendData(sb.toString());
        } else {
            this.text = (TextImpl) this.document.createTextNode(sb.toString());
            this.element.appendChild(this.text); 
        }
        System.out.println("whitespaces(" + sb.toString() + ", " + start + ","
                + length + ")");
    }

    /**
     * Receive notification of a processing instruction.
     *
     * <p>The Parser will invoke this method once for each processing
     * instruction found: note that processing instructions may occur
     * before or after the main document element.</p>
     *
     * <p>A SAX parser must never report an XML declaration (XML 1.0,
     * section 2.8) or a text declaration (XML 1.0, section 4.3.1)
     * using this method.</p>
     *
     * <p>Like {@link #characters characters()}, processing instruction
     * data may have characters that need more than one <code>char</code>
     * value. </p>
     *
     * @param target the processing instruction target
     * @param data the processing instruction data, or null if
     *        none was supplied.  The data does not include any
     *        whitespace separating it from the target
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see org.xml.sax.ContentHandler#processingInstruction(
     * 		java.lang.String, java.lang.String)
     */
    public void processingInstruction(final String target, final String data)
            throws SAXException {
        
        Node pi = this.document.createProcessingInstruction(target, data);
        this.element.appendChild(pi);

        this.text = null;
    }

    /**
     * Receive notification of a skipped entity.
     * This is not called for entity references within markup constructs
     * such as element start tags or markup declarations.  (The XML
     * recommendation requires reporting skipped external entities.
     * SAX also reports internal entity expansion/non-expansion, except
     * within markup constructs.)
     *
     * <p>The Parser will invoke this method each time the entity is
     * skipped.  Non-validating processors may skip entities if they
     * have not seen the declarations (because, for example, the
     * entity was declared in an external DTD subset).  All processors
     * may skip external entities, depending on the values of the
     * <code>http://xml.org/sax/features/external-general-entities</code>
     * and the
     * <code>http://xml.org/sax/features/external-parameter-entities</code>
     * properties.</p>
     *
     * @param name the name of the skipped entity.  If it is a 
     *        parameter entity, the name will begin with '%', and if
     *        it is the external DTD subset, it will be the string
     *        "[dtd]"
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(final String name) throws SAXException {
        System.out.println("skippedEntity(" + name + ")");
    }

    /**
     * Receive notification of a notation declaration event.
     *
     * <p>It is up to the application to record the notation for later
     * reference, if necessary;
     * notations may appear as attribute values and in unparsed entity
     * declarations, and are sometime used with processing instruction
     * target names.</p>
     *
     * <p>At least one of publicId and systemId must be non-null.
     * If a system identifier is present, and it is a URL, the SAX
     * parser must resolve it fully before passing it to the
     * application through this event.</p>
     *
     * <p>There is no guarantee that the notation declaration will be
     * reported before any unparsed entities that use it.</p>
     *
     * @param name The notation name.
     * @param publicId The notation's public identifier, or null if
     *        none was given.
     * @param systemId The notation's system identifier, or null if
     *        none was given.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #unparsedEntityDecl
     * @see org.xml.sax.Attributes
     * @see org.xml.sax.DTDHandler#notationDecl(
     * 		java.lang.String, java.lang.String, java.lang.String)
     */
    public void notationDecl(final String name, 
                             final String publicId, 
                             final String systemId)
            throws SAXException {
        System.out.println(
                "notationDecl(" + name + ", "
                + publicId + ", "
                + systemId + ")");
    }

    /**
     * Receive notification of an unparsed entity declaration event.
     *
     * <p>Note that the notation name corresponds to a notation
     * reported by the {@link #notationDecl notationDecl} event.  
     * It is up to the application to record the entity for later 
     * reference, if necessary;
     * unparsed entities may appear as attribute values. 
     * </p>
     *
     * <p>If the system identifier is a URL, the parser must resolve it
     * fully before passing it to the application.</p>
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @param name The unparsed entity's name.
     * @param publicId The entity's public identifier, or null if none
     *        was given.
     * @param systemId The entity's system identifier.
     * @param notationName The name of the associated notation.
     * @see #notationDecl
     * @see org.xml.sax.Attributes
     * @see org.xml.sax.DTDHandler#unparsedEntityDecl(
     * 		java.lang.String, java.lang.String, 
     * 		java.lang.String, java.lang.String)
     */
    public void unparsedEntityDecl(final String name, 
                                   final String publicId,
                                   final String systemId, 
                                   final String notationName) 
                                           throws SAXException {

        System.out.println(
                "unparsedEntityDecl(" + name + ", "
                + publicId + ", "
                + systemId + ", "
                + notationName + ")");
    }

    /**
     * Allow the application to resolve external entities.
     *
     * <p>The parser will call this method before opening any external
     * entity except the top-level document entity.  Such entities include
     * the external DTD subset and external parameter entities referenced
     * within the DTD (in either case, only if the parser reads external
     * parameter entities), and external general entities referenced
     * within the document element (if the parser reads external general
     * entities).  The application may request that the parser locate
     * the entity itself, that it use an alternative URI, or that it
     * use data provided by the application (as a character or byte
     * input stream).</p>
     *
     * <p>Application writers can use this method to redirect external
     * system identifiers to secure and/or local URIs, to look up
     * public identifiers in a catalogue, or to read an entity from a
     * database or other input source (including, for example, a dialog
     * box).  Neither XML nor SAX specifies a preferred policy for using
     * public or system IDs to resolve resources.  However, SAX specifies
     * how to interpret any InputSource returned by this method, and that
     * if none is returned, then the system ID will be dereferenced as
     * a URL.  </p>
     *
     * <p>If the system identifier is a URL, the SAX parser must
     * resolve it fully before reporting it to the application.</p>
     *
     * @param publicId The public identifier of the external entity
     *        being referenced, or null if none was supplied.
     * @param systemId The system identifier of the external entity
     *        being referenced.
     * @return An InputSource object describing the new input source,
     *         or null to request that the parser open a regular
     *         URI connection to the system identifier.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @exception java.io.IOException A Java-specific IO exception,
     *            possibly the result of creating a new InputStream
     *            or Reader for the InputSource.
     * @see org.xml.sax.InputSource
     * @see org.xml.sax.EntityResolver#resolveEntity(
     * 		java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity(final String publicId, 
                                     final String systemId)
                                             throws SAXException, 
                                                    IOException {
        System.out.println(
                "resolveEntity(" + publicId + ", "
                + systemId + ")");
        
        return null;
    }

    /**
     * Receive notification of a warning.
     *
     * <p>SAX parsers will use this method to report conditions that
     * are not errors or fatal errors as defined by the XML
     * recommendation.  The default behaviour is to take no
     * action.</p>
     *
     * <p>The SAX parser must continue to provide normal parsing events
     * after invoking this method: it should still be possible for the
     * application to process the document through to the end.</p>
     *
     * <p>Filters may use this method to report other, non-XML warnings
     * as well.</p>
     *
     * @param exception The warning information encapsulated in a
     *                  SAX parse exception.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.SAXParseException 
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(final SAXParseException exception) throws SAXException {
        System.err.println("warning");
    }

    /**
     * Receive notification of a recoverable error.
     *
     * <p>This corresponds to the definition of "error" in section 1.2
     * of the W3C XML 1.0 Recommendation.  For example, a validating
     * parser would use this callback to report the violation of a
     * validity constraint.  The default behaviour is to take no
     * action.</p>
     *
     * <p>The SAX parser must continue to provide normal parsing
     * events after invoking this method: it should still be possible
     * for the application to process the document through to the end.
     * If the application cannot do so, then the parser should report
     * a fatal error even if the XML recommendation does not require
     * it to do so.</p>
     *
     * <p>Filters may use this method to report other, non-XML errors
     * as well.</p>
     *
     * @param exception The error information encapsulated in a
     *                  SAX parse exception.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.SAXParseException 
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(final SAXParseException exception) throws SAXException {
        System.err.println("error");
    }

    /**
     * Receive notification of a non-recoverable error.
     *
     * <p><strong>There is an apparent contradiction between the
     * documentation for this method and the documentation for {@link
     * org.xml.sax.ContentHandler#endDocument}.  Until this ambiguity
     * is resolved in a future major release, clients should make no
     * assumptions about whether endDocument() will or will not be
     * invoked when the parser has reported a fatalError() or thrown
     * an exception.</strong></p>
     *
     * <p>This corresponds to the definition of "fatal error" in
     * section 1.2 of the W3C XML 1.0 Recommendation.  For example, a
     * parser would use this callback to report the violation of a
     * well-formedness constraint.</p>
     *
     * <p>The application must assume that the document is unusable
     * after the parser has invoked this method, and should continue
     * (if at all) only for the sake of collecting additional error
     * messages: in fact, SAX parsers are free to stop reporting any
     * other events once this method has been invoked.</p>
     *
     * @param exception The error information encapsulated in a
     *                  SAX parse exception.  
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.SAXParseException
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(final SAXParseException exception) 
            throws SAXException {
        System.err.println("fatalError");
    }

    /**
     * Report an element type declaration.
     *
     * <p>The content model will consist of the string "EMPTY", the
     * string "ANY", or a parenthesised group, optionally followed
     * by an occurrence indicator.  The model will be normalized so
     * that all parameter entities are fully resolved and all whitespace 
     * is removed,and will include the enclosing parentheses.  Other
     * normalization (such as removing redundant parentheses or 
     * simplifying occurrence indicators) is at the discretion of the
     * parser.</p>
     *
     * @param name The element type name.
     * @param model The content model as a normalized string.
     * @exception SAXException The application may raise an exception.
     * @see org.xml.sax.ext.DeclHandler#elementDecl(
     *      java.lang.String, java.lang.String)
     */
    public void elementDecl(final String name, final String model) 
            throws SAXException {
        
        System.out.println(
                "elementDecl(" + name + ", "
                + model + ")");
    }

    /**
     * Report an attribute type declaration.
     *
     * <p>Only the effective (first) declaration for an attribute will
     * be reported.  The type will be one of the strings "CDATA",
     * "ID", "IDREF", "IDREFS", "NMTOKEN", "NMTOKENS", "ENTITY",
     * "ENTITIES", a parenthesized token group with 
     * the separator "|" and all whitespace removed, or the word
     * "NOTATION" followed by a space followed by a parenthesized
     * token group with all whitespace removed.</p>
     *
     * <p>The value will be the value as reported to applications,
     * appropriately normalized and with entity and character
     * references expanded.  </p>
     *
     * @param eName The name of the associated element.
     * @param aName The name of the attribute.
     * @param type A string representing the attribute type.
     * @param mode A string representing the attribute defaulting mode
     *        ("#IMPLIED", "#REQUIRED", or "#FIXED") or null if
     *        none of these applies.
     * @param value A string representing the attribute's default value,
     *        or null if there is none.
     * @exception SAXException The application may raise an exception.
     * @see org.xml.sax.ext.DeclHandler#attributeDecl(
     *      java.lang.String, java.lang.String, java.lang.String, 
     *      java.lang.String, java.lang.String)
     */
    public void attributeDecl(final String eName, 
                              final String aName, 
                              final String type,
                              final String mode, 
                              final String value) throws SAXException {
        
        System.out.println(
                "attributeDecl(" + eName + ", "
                + aName + ", "
                + type + ", " 
                + mode + ", "
                + value + ")");
    }

    /**
     * Report an internal entity declaration.
     *
     * <p>Only the effective (first) declaration for each entity
     * will be reported.  All parameter entities in the value
     * will be expanded, but general entities will not.</p>
     *
     * @param name The name of the entity.  If it is a parameter
     *        entity, the name will begin with '%'.
     * @param value The replacement text of the entity.
     * @exception SAXException The application may raise an exception.
     * @see #externalEntityDecl
     * @see org.xml.sax.DTDHandler#unparsedEntityDecl
     * @see org.xml.sax.ext.DeclHandler#internalEntityDecl(
     *      java.lang.String, java.lang.String)
     */
    public void internalEntityDecl(final String name, final String value)
            throws SAXException {

        System.out.println(
                "internalEntityDecl(" + name + ", " + value + ")");
    }

    /**
     * Report a parsed external entity declaration.
     *
     * <p>Only the effective (first) declaration for each entity
     * will be reported.</p>
     *
     * <p>If the system identifier is a URL, the parser must resolve it
     * fully before passing it to the application.</p>
     *
     * @param name The name of the entity.  If it is a parameter
     *        entity, the name will begin with '%'.
     * @param publicId The entity's public identifier, or null if none
     *        was given.
     * @param systemId The entity's system identifier.
     * @exception SAXException The application may raise an exception.
     * @see #internalEntityDecl
     * @see org.xml.sax.DTDHandler#unparsedEntityDecl
     * @see org.xml.sax.ext.DeclHandler#externalEntityDecl(
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public void externalEntityDecl(final String name, 
                                   final String publicId, 
                                   final String systemId) throws SAXException {

        System.out.println(
                "externalEntityDecl(" + name + ", "
                + publicId + ", " + systemId + ")");
    }

    /**
     * Report the start of DTD declarations, if any.
     *
     * <p>This method is intended to report the beginning of the
     * DOCTYPE declaration; if the document has no DOCTYPE declaration,
     * this method will not be invoked.</p>
     *
     * <p>All declarations reported through 
     * {@link org.xml.sax.DTDHandler DTDHandler} or
     * {@link org.xml.sax.ext.DeclHandler DeclHandler} events must appear
     * between the startDTD and {@link #endDTD endDTD} events.
     * Declarations are assumed to belong to the internal DTD subset
     * unless they appear between {@link #startEntity startEntity}
     * and {@link #endEntity endEntity} events.  Comments and
     * processing instructions from the DTD should also be reported
     * between the startDTD and endDTD events, in their original 
     * order of (logical) occurrence; they are not required to
     * appear in their correct locations relative to DTDHandler
     * or DeclHandler events, however.</p>
     *
     * <p>Note that the start/endDTD events will appear within
     * the start/endDocument events from ContentHandler and
     * before the first 
     * {@link org.xml.sax.ContentHandler#startElement startElement}
     * event.</p>
     *
     * @param name The document type name.
     * @param publicId The declared public identifier for the
     *        external DTD subset, or null if none was declared.
     * @param systemId The declared system identifier for the
     *        external DTD subset, or null if none was declared.
     *        (Note that this is not resolved against the document
     *        base URI.)
     * @exception SAXException The application may raise an
     *            exception.
     * @see #endDTD
     * @see #startEntity
     * @see org.xml.sax.ext.LexicalHandler#startDTD(
     * 		java.lang.String, java.lang.String, java.lang.String)
     */
    public void startDTD(final String name, 
                         final String publicId, 
                         final String systemId) throws SAXException {

        System.out.println(
                "startDTD(" + name + ","
                + publicId + ", "
                + systemId + ")");
    }

    /**
     * Report the end of DTD declarations.
     *
     * <p>This method is intended to report the end of the
     * DOCTYPE declaration; if the document has no DOCTYPE declaration,
     * this method will not be invoked.</p>
     *
     * @exception SAXException The application may raise an exception.
     * @see #startDTD
     * @see org.xml.sax.ext.LexicalHandler#endDTD()
     */
    public void endDTD() throws SAXException {
        System.out.println("endDTD()");
    }

    /**
     * Report the beginning of some internal and external XML entities.
     *
     * <p>The reporting of parameter entities (including
     * the external DTD subset) is optional, and SAX2 drivers that
     * report LexicalHandler events may not implement it; you can use the
     * <code
     * >http://xml.org/sax/features/lexical-handler/parameter-entities</code>
     * feature to query or control the reporting of parameter entities.</p>
     *
     * <p>General entities are reported with their regular names,
     * parameter entities have '%' prepended to their names, and 
     * the external DTD subset has the pseudo-entity name "[dtd]".</p>
     *
     * <p>When a SAX2 driver is providing these events, all other 
     * events must be properly nested within start/end entity 
     * events.  There is no additional requirement that events from 
     * {@link org.xml.sax.ext.DeclHandler DeclHandler} or
     * {@link org.xml.sax.DTDHandler DTDHandler} be properly ordered.</p>
     *
     * <p>Note that skipped entities will be reported through the
     * {@link org.xml.sax.ContentHandler#skippedEntity skippedEntity}
     * event, which is part of the ContentHandler interface.</p>
     *
     * <p>Because of the streaming event model that SAX uses, some
     * entity boundaries cannot be reported under any 
     * circumstances:</p>
     *
     * <ul>
     * <li>general entities within attribute values</li>
     * <li>parameter entities within declarations</li>
     * </ul>
     *
     * <p>These will be silently expanded, with no indication of where
     * the original entity boundaries were.</p>
     *
     * <p>Note also that the boundaries of character references (which
     * are not really entities anyway) are not reported.</p>
     *
     * <p>All start/endEntity events must be properly nested.
     *
     * @param name The name of the entity.  If it is a parameter
     *        entity, the name will begin with '%', and if it is the
     *        external DTD subset, it will be "[dtd]".
     * @exception SAXException The application may raise an exception.
     * @see #endEntity
     * @see org.xml.sax.ext.DeclHandler#internalEntityDecl
     * @see org.xml.sax.ext.DeclHandler#externalEntityDecl 
     * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
     */
    public void startEntity(final String name) throws SAXException {
        System.out.println("startEntity(" + name + ")");
    }

    /**
     * Report the end of an entity.
     *
     * @param name The name of the entity that is ending.
     * @exception SAXException The application may raise an exception.
     * @see #startEntity
     * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
     */
    public void endEntity(final String name) throws SAXException {
        System.out.println("endEntity(" + name + ")");
    }

    /**
     * Report the start of a CDATA section.
     *
     * <p>The contents of the CDATA section will be reported through
     * the regular {@link org.xml.sax.ContentHandler#characters
     * characters} event; this event is intended only to report
     * the boundary.</p>
     *
     * @exception SAXException The application may raise an exception.
     * @see #endCDATA
     * @see org.xml.sax.ext.LexicalHandler#startCDATA()
     */
    public void startCDATA() throws SAXException {
        System.out.println("startCDATA()");
    }

    /**
     * Report the end of a CDATA section.
     *
     * @exception SAXException The application may raise an exception.
     * @see #startCDATA
     * @see org.xml.sax.ext.LexicalHandler#endCDATA()
     */
    public void endCDATA() throws SAXException {
        System.out.println("endCDATA()");
    }

    /**
     * Report an XML comment anywhere in the document.
     *
     * <p>This callback will be used for comments inside or outside the
     * document element, including comments in the external DTD
     * subset (if read).  Comments in the DTD must be properly
     * nested inside start/endDTD and start/endEntity events (if
     * used).</p>
     *
     * @param ch An array holding the characters in the comment.
     * @param start The starting position in the array.
     * @param length The number of characters to use from the array.
     * @exception SAXException The application may raise an exception.
     * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
     */
    public void comment(final char[] ch, final int start, final int length) 
    		throws SAXException {
        
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < length + start; i++) {
            sb.append(ch[i]); 
        }
        
        Comment comm = this.document.createComment(sb.toString());
        if (this.element == null) {
            this.document.appendChild(comm);
        } else {
            this.element.appendChild(comm);
        }
        
        this.text = null;
    }
    
    /**
     * @return ..
     */
    public DocumentImpl getDocument() {
        return this.document;
    }

}
