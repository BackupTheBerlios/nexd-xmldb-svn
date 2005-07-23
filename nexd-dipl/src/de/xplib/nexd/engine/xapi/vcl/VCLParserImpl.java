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
 * $Log: VCLParserImpl.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.5  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.4  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.3  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.collections.FastHashMap;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.vcl.AbstractVCLParser;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.VCLCollectionReference;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VCLValueOf;
import de.xplib.nexd.api.vcl.VCLVariable;
import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;
//import de.xplib.nexd.engine.xml.dom.DOMImplementationImpl;

/**
 * Base parser for a Virtual-Collection-Language Schema file or resource. This 
 * parser tries to transform the input VCL-Schema in an object tree.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 * @see org.xml.sax.helpers.DefaultHandler
 */
public class VCLParserImpl extends AbstractVCLParser {
    
    /**
     * Comment for <code>parser</code>
     */
    private SAXParser parser = null;
    
    /**
     * Comment for <code>domImpl</code>
     */
    private DOMImplementation domImpl = null;
    
    /**
     * Comment for <code>schema</code>
     * @clientCardinality 1
     * @clientRole schema
     * @directed true
     * @label creates
     * @link aggregation
     * @supplierCardinality 0..*
     */
    private VCLSchemaImpl schema = null;
    
    /**
     * Comment for <code>doc</code>
     */
    private Document doc = null;
    
    /**
     * Comment for <code>elem</code>
     */
    private Element elem = null;
    
    /**
     * The used prefix for the Virtual Collection Language Schema markup.
     */
    private String vclPrefix = "";
    
    /**
     * This stack holds all open <code>{@link VCLCollectionReference}</code>
     * instances.
     */
    private final Stack collections = new Stack();
    
    /**
     * <p>This HashMap holds all namespace uris and the corresponding 
     * prefixes.</p>
     */
    private final Map prefixes;
    
    /**
     * Last thrown InvalidVCLSchemaException, this is required because the sax 
     * parser just allowes sax exceptions.
     */
    private InvalidVCLSchemaException exception = null;
    
    /**
     * 
     *
     */
    public VCLParserImpl() {
        super();
        
        this.prefixes = new FastHashMap();
        ((FastHashMap) this.prefixes).setFast(true);
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
     * @see de.xplib.nexd.api.vcl.AbstractVCLParser#parse(
     *      org.xml.sax.InputSource)
     */
    public VCLSchema parse(final InputSource isIn) 
            throws IOException, SAXException, InvalidVCLSchemaException {
        
        if (isIn == null) {
            throw new IllegalArgumentException("InputSource cannot be null.");
        }
        
        this.reset();
        
        try {
            this.parser.parse(isIn, this);
        } catch (SAXException e) {
            Throwable throwable = e.getCause();
            if (e.toString().indexOf("InvalidVCLSchemaException") != -1
                    || throwable instanceof InvalidVCLSchemaException) {
                
                throw new InvalidVCLSchemaException();
            } else if (this.exception != null) {
                InvalidVCLSchemaException res = null;
                InvalidVCLSchemaException ret = this.exception;
                this.exception = res;
                throw ret;
            }
            throw e;
        }
        
        if (this.schema.getCollectionReference() == null) {
            throw new InvalidVCLSchemaException();
        }
        
        return this.schema;
    }
    
    /**
     * Resets the internal parser and all temporary attribute to the initial
     * state.
     * 
     * @throws SAXException If something sax specific goes wrong.
     */
    public void reset() throws SAXException {
        if (this.parser == null) {
            try {
                this.parser = SAXParserFactory.newInstance().newSAXParser();
                this.parser.getXMLReader().setContentHandler(this);
                this.parser.getXMLReader().setErrorHandler(this);
                
                this.parser.getXMLReader().setFeature(
                        "http://xml.org/sax/features/namespaces", true);
                
                this.domImpl = new DocumentBuilderFactoryImpl()
                                      .newDocumentBuilder()
                                      .getDOMImplementation();
            } catch (ParserConfigurationException e) {
                throw new SAXException(e);
            } catch (FactoryConfigurationError e) {
                throw new SAXException(e.getException());
            }
        }
        /*
        if (this.domImpl == null) {
            this.domImpl = new DOMImplementationImpl();
        }
        */
        Object reset = null;
        
        this.vclPrefix = "";
        this.elem      = (Element) reset;
        this.schema    = (VCLSchemaImpl) reset;
        this.doc       = (Document) reset;
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
     * @see #endDocument
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
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
     * @see #startDocument
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
     * @param prefixIn the Namespace prefix being declared.
     *	An empty string is used for the default element namespace,
     *	which has no prefix.
     * @param uriIn the Namespace URI the prefix is mapped to
     * @throws org.xml.sax.SAXException the client may throw
     *            an exception during processing
     * @see #endPrefixMapping
     * @see #startElement
     * @see org.xml.sax.ContentHandler#startPrefixMapping(
     *      java.lang.String, java.lang.String)
     */
    public void startPrefixMapping(final String prefixIn, final String uriIn)
            throws SAXException {

        if (uriIn.equals(VCLSchema.NAMESPACE_URI)) {
            this.schema    = new VCLSchemaImpl(prefixIn);
            this.vclPrefix = prefixIn;
            try {
                this.doc = (Document) this.schema.getContentAsDOM();
            } catch (XMLDBException e) {
                throw new SAXException(e);
            }
        }
        
        this.prefixes.put(prefixIn, uriIn);
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
     * @param prefixIn the prefix that was being mapped.
     *	This is the empty string when a default mapping scope ends.
     * @throws org.xml.sax.SAXException the client may throw
     *            an exception during processing
     * @see #startPrefixMapping
     * @see #endElement
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(final String prefixIn) throws SAXException {
        if (prefixIn.equals(this.vclPrefix)) {
            this.vclPrefix = "";
        }
        this.prefixes.remove(prefixIn);
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
     * @param uriIn the Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed
     * @param localNameIn the local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed
     * @param qNameIn the qualified name (with prefix), or the
     *        empty string if qualified names are not available
     * @param attrsIn the attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.  The value of this object after
     *        startElement returns is undefined
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see #endElement
     * @see org.xml.sax.Attributes
     * @see org.xml.sax.helpers.AttributesImpl
     * @see org.xml.sax.ContentHandler#startElement(
     *      java.lang.String, java.lang.String, java.lang.String, 
     *      org.xml.sax.Attributes)
     */
    public void startElement(final String uriIn, 
                             final String localNameIn, 
                             final String qNameIn,
                             final Attributes attrsIn) throws SAXException {
        
        if (this.schema == null) {
            if (!localNameIn.equals(VCLSchema.ELEM_NAME)) {
                throw new SAXException(new InvalidVCLSchemaException());
            }
            this.schema = new VCLSchemaImpl();
            try {
                this.doc    = (Document) this.schema.getContentAsDOM();
            } catch (XMLDBException e) {
                throw new SAXException(e);
            }
        }
        
        if (uriIn.equals(VCLSchema.NAMESPACE_URI)) {
            if (localNameIn.equals(VCLSchema.ELEM_NAME)) {
                this.handleSchema(attrsIn);
            } else if (localNameIn.equals(VCLCollectionReference.ELEM_NAME)) {
                this.handleCollectionReference(qNameIn, attrsIn);
            } else if (localNameIn.equals(VCLVariable.ELEM_NAME)) {
                this.handleVariable(qNameIn, attrsIn);
            } else if (localNameIn.equals(VCLValueOf.ELEM_NAME)) {
                this.handleValueOf(qNameIn, attrsIn);
            }
        } else {
            this.createElement(qNameIn, attrsIn);
        }
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
     * @param uriIn the Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed
     * @param localNameIn the local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed
     * @param qNameIn the qualified XML name (with prefix), or the
     *        empty string if qualified names are not available
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see org.xml.sax.ContentHandler#endElement(
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(final String uriIn, 
                           final String localNameIn, 
                           final String qNameIn) throws SAXException {
        
        if (uriIn.equals(VCLSchema.NAMESPACE_URI) 
                && localNameIn.equals(VCLCollectionReference.ELEM_NAME)) {
            this.collections.pop();
        }
        
        Object reset = null;

        Node node = this.elem.getParentNode();
        if (node == this.doc) {
            this.elem = (Element) reset;
        } else {
            this.elem = (Element) node;
        }
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
     * @param chIn the characters from the XML document
     * @param startIn the start position in the array
     * @param lengthIn the number of characters to read from the array
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see #ignorableWhitespace 
     * @see org.xml.sax.Locator
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(final char[] chIn, 
                           final int startIn, 
                           final int lengthIn) throws SAXException {
        
        if (this.elem == null) {
            throw new SAXException("Invalid document structure.");
        }
        
        StringBuilder builder = new StringBuilder();
        for (int i = startIn, l = (lengthIn + startIn); i < l; i++) {
            builder.append(chIn[i]); 
        }

        if (this.elem.getLastChild() instanceof Text) {
            ((Text) this.elem.getLastChild()).appendData(builder.toString());
        } else {
            Text txt = this.doc.createTextNode(builder.toString());
            this.elem.appendChild(txt);
        }
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
     * @param targetIn the processing instruction target
     * @param dataIn the processing instruction data, or null if
     *        none was supplied.  The data does not include any
     *        whitespace separating it from the target
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see org.xml.sax.ContentHandler#processingInstruction(
     *      java.lang.String, java.lang.String)
     */
    public void processingInstruction(final String targetIn, 
                                      final String dataIn) throws SAXException {
        
        if (this.elem == null) {
            throw new SAXException("Invalid document structure.");
        }
        
        this.elem.appendChild(this.doc.createProcessingInstruction(
                targetIn, dataIn));
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
     * @param nameIn the name of the skipped entity.  If it is a 
     *        parameter entity, the name will begin with '%', and if
     *        it is the external DTD subset, it will be the string
     *        "[dtd]"
     * @throws org.xml.sax.SAXException any SAX exception, possibly
     *            wrapping another exception
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(final String nameIn) throws SAXException {
        
        if (this.elem == null) {
            throw new SAXException("Invalid document structure.");
        }
        this.elem.appendChild(this.doc.createEntityReference(nameIn));
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
     * @param exceptionIn The warning information encapsulated in a
     *                    SAX parse exception.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.SAXParseException 
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(final SAXParseException exceptionIn) 
            throws SAXException {
        throw exceptionIn;
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
     * @param exceptionIn The error information encapsulated in a
     *                    SAX parse exception.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.SAXParseException 
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(final SAXParseException exceptionIn) throws SAXException {
        throw exceptionIn;
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
     * @param exceptionIn The error information encapsulated in a
     *                    SAX parse exception.  
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.SAXParseException
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(final SAXParseException exceptionIn) 
            throws SAXException {
        throw exceptionIn;
    }
    
    /**
     * Creates an element.
     * @param qNameIn The qualified name of the element.
     * @param attrsIn The attributes.
     * @throws SAXException I think never.
     */
    private void createElement(final String qNameIn, final Attributes attrsIn) 
            throws SAXException {
        
        Element tmp; 
        
        int index = qNameIn.indexOf(':'); 
        if (index == -1) {
            tmp = this.doc.createElement(qNameIn);
        } else {
            String prefix = qNameIn.substring(0, index);
            
            String uri = "";
            if (this.prefixes.containsKey(prefix)) {
                uri = (String) this.prefixes.get(prefix);
            }
            tmp = this.doc.createElementNS(uri, qNameIn);
        }
        
        if (this.elem == null) {
            throw new SAXException(new InvalidVCLSchemaException());
        }
        
        this.elem.appendChild(tmp);
        this.elem = tmp;
        
        for (int i = 0, l = attrsIn.getLength(); i < l; i++) {
            
            String qName = attrsIn.getQName(i);
            
            index = qName.indexOf(':'); 
            if (index == -1) {
                this.elem.setAttribute(qName, attrsIn.getValue(i));
            } else {
                String prefix = qName.substring(0, index);
                
                String uri = "";
                if (this.prefixes.containsKey(prefix)) {
                    uri = (String) this.prefixes.get(prefix);
                }
                this.elem.setAttributeNS(uri, qName, attrsIn.getValue(i));
            }
            
            if (attrsIn.getURI(i).equals(VCLSchema.NAMESPACE_URI)) {
                
                this.handAttr(attrsIn.getQName(i), 
                              attrsIn.getLocalName(i), 
                              attrsIn.getValue(i));
            }
            
            
        }
    }
    
    /**
     * Handels the vcl:schema element and extracts the name settings.
     * 
     * @param attrsIn The attributes.
     * @throws SAXException I think never.
     */
    private void handleSchema(final Attributes attrsIn) throws SAXException {
        
        this.elem = this.doc.getDocumentElement();
        for (int i = 0, l = attrsIn.getLength(); i < l; i++) {
            
            String name  = attrsIn.getQName(i);
            String value = attrsIn.getValue(i).trim();
            this.elem.setAttribute(name, value);
            
            if (name.equals(VCLSchemaImpl.ATTR_PREFIX)) {
                this.schema.setPrefix(value);
            } else if (name.equals(VCLSchemaImpl.ATTR_POSTFIX)) {
                this.schema.setPostfix(value);
            } else if (name.equals(VCLSchemaImpl.ATTR_ENUMERATE)) {
                value = value.toLowerCase(Locale.GERMANY);
                this.schema.setEnumerate(
                        value.equals("true") || value.equals("1"));
            } else if (name.equals(VCLSchemaImpl.ATTR_NAME)) {
                this.schema.setName(value);
            }
        }
    }
    
    /**
     * Create a VCLCollectionReference instance and adds it to the abstract
     * vcl-schema tree.
     *  
     * @param qNameIn The qualified name.
     * @param attrsIn The attributes.
     * @throws SAXException I think never.
     */
    private void handleCollectionReference(final String qNameIn,
                                           final Attributes attrsIn) 
        throws SAXException {
        
        if (attrsIn.getIndex(VCLCollectionReference.ATTR_MATCH) == -1) {
            this.exception = new InvalidVCLSchemaException(
                    InvalidVCLSchemaException.ATTR_MATCH_REQUIRED);
            throw new SAXException("");
        }
        
        this.createElement(qNameIn, attrsIn);
        
        VCLCollectionReferenceImpl ref = new VCLCollectionReferenceImpl(
                this.elem, 
                attrsIn.getValue(VCLCollectionReference.ATTR_MATCH));
        
        int select = attrsIn.getIndex(VCLCollectionReference.ATTR_SELECT);
        if (select != -1) {
            ref.setSelect(attrsIn.getValue(select));
        }
        
        
        if (this.collections.isEmpty()) {
            this.schema.setCollectionReference(ref);
        } else if (this.elem.getParentNode() 
                == this.doc.getDocumentElement()) { 
            
            this.exception = new InvalidVCLSchemaException(
                    InvalidVCLSchemaException.DOUBLE_ROOT_REFERENCE);
            throw new SAXException("");
        } else {
            ((VCLCollectionReferenceImpl) this.collections.peek())
                    .addCollectionReference(ref);
        }
        
        this.collections.push(ref);
    }
    
    /**
     * Create a VCLVariable object and adds it to the vcl-schema tree.
     * 
     * @param qNameIn The qualified name.
     * @param attrsIn The attributes.
     * @throws SAXException I think never.
     */
    private void handleVariable(final String qNameIn,
                                final Attributes attrsIn) throws SAXException {
        
        if (this.collections.isEmpty()) {
            throw new SAXException(new InvalidVCLSchemaException());
        }
        
        String name   = attrsIn.getValue(VCLVariable.ATTR_NAME);
        String select = attrsIn.getValue(VCLVariable.ATTR_SELECT);
        if (name == null || select == null) {
            throw new SAXException(new InvalidVCLSchemaException());
        }
        
        this.createElement(qNameIn, attrsIn);
        
        ((VCLCollectionReferenceImpl) this.collections.peek())
                .addVariable(new VCLVariableImpl(this.elem, name, select));
    }
    
    /**
     * Create a VCLValueOf object and adds it to the vcl-schema tree.
     * 
     * @param qNameIn The qualified name.
     * @param attrsIn The attributes.
     * @throws SAXException I think never.
     */
    private void handleValueOf(final String qNameIn, final Attributes attrsIn)
            throws SAXException {
        
        if (this.collections.isEmpty()) {
            throw new SAXException(new InvalidVCLSchemaException());
        }
        
        String select = attrsIn.getValue(VCLVariable.ATTR_SELECT);
        if (select == null) {
            throw new SAXException(new InvalidVCLSchemaException());
        }
        
        this.createElement(qNameIn, attrsIn);
        
        ((VCLCollectionReferenceImpl) this.collections.peek())
                .addValueOf(new VCLValueOfImpl(this.elem, select));
    }
    
    /**
     * Create a VCLAttr object and adds it to the vcl-schema tree.
     * 
     * @param qNameIn The qualified name.
     * @param lNameIn The local name.
     * @param valueIn The value.
     * @throws SAXException I think never.
     */
    private void handAttr(final String qNameIn, 
                          final String lNameIn, 
                          final String valueIn) 
            throws SAXException {
        
        if (this.collections.isEmpty()) {
            throw new SAXException(new InvalidVCLSchemaException());
        }
        
        ((VCLCollectionReferenceImpl) this.collections.peek()).addAttribute(
                new VCLAttrImpl(
                        this.elem.getAttributeNode(qNameIn), lNameIn, valueIn));
    }
}
