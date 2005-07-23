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
 * $Log: VCLSchemaImpl.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.7  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.6  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.5  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
 *
 * Revision 1.4  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.sixdml.exceptions.InvalidQueryException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLCollectionReference;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VCLSchemaVisitor;
import de.xplib.nexd.api.vcl.VariableExistsException;

/**
 * <p>This class represents the <code>schema</code> 
 * <code>{@link org.w3c.dom.Element}</code> in the DOM 
 * <code>{@link org.w3c.dom.Document}</code> of the parsed 
 * <code>{@link VCLSchema}</code>. This is the first 
 * <code>{@link org.w3c.dom.Element}</code> in each Virtual Collection Language 
 * Schema.</p> 
 * <p>This class implements a part of the GoF Composite pattern (163).</p>
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public class VCLSchemaImpl implements VCLSchema {
        
    /**
     * The prefix that will be placed before each auto generated xml document. 
     */
    private String prefix = null;
    
    /**
     * The file extension for each auto generated xml document. 
     */
    private String postfix = null;
    
    /**
     * If this attribute is set to <code>true<code>, the generated xml documents
     * will get an incremented document number.
     */
    private boolean enumerate = false;
    
    /**
     * This attribute defines a constant elemName for all generated xml 
     * documents. If this attribute is not <code>null</code> the 
     * <code>enumerate</code> attribute must have the value <code>true</code>.
     */
    private String name = null;
    
    /**
     * This attribute holds the <code>{@link VCLCollectionReference}</code>,
     * which is used to start the automatic generation of xml documents.
     * @clientCardinality 1
     * @clientRole collRef
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    private VCLCollectionReferenceImpl collRef = null;
    
    /**
     * Comment for <code>content</code>
     */
    private Document content;
    
    /**
     * Comment for <code>elemName</code>
     */
    private final String elemName;
    
    /**
     * Constructor.
     * 
     * @see VCLSchemaImpl#VCLSchemaImpl(String)
     */
    protected VCLSchemaImpl() throws SAXException {
        this(NAMESPACE_PREFIX);
    }

    /**
     * Constructor.
     * 
     * @param prefixIn The xml namespace prefix for Virtual Collection Language
     *                 elements.
     * @throws SAXException ...
     */
    protected VCLSchemaImpl(final String prefixIn) throws SAXException {
        super();
        
        this.elemName = prefixIn + ":" + ELEM_NAME;
        
        try {
            this.content = DocumentBuilderFactory
                                   .newInstance()
                                   .newDocumentBuilder()
                                   .getDOMImplementation().createDocument(
                                           NAMESPACE_URI, this.elemName, null);
            /*
            this.content = new DOMImplementationImpl().createDocument(
                    NAMESPACE_URI, this.elemName, null);*/
        } catch (ParserConfigurationException e) {
            throw new SAXException(e.getMessage(), e);
        } catch (FactoryConfigurationError e) {
            throw new SAXException(e.getMessage());
        }
    }

    /**
     * Returns the prefix for all generated xml documents or it returns
     * <code>null</code>. The names of the documents will begin with this 
     * prefix. Example: 
     * <p>
     *   The prefix <b>virtual</b> and the document name <b>foobar.xml</b> will
     *   become <b>virtual_foobar.xml<b>.
     * </p>
     * 
     * @return The <code>prefix</code> for auto generated xml documents or 
     *         <code>null</code>.
     * @see de.xplib.nexd.api.vcl.VCLSchema#getPrefix()
     */
    public String getPrefix() {
        return this.prefix;
    }
    
    /**
     * Setter method for the prefix that will stand at the beginning of each 
     * auto generated xml document.
     *  
     * @param prefixIn The prefix for auto generated xml documents.
     */
    public void setPrefix(final String prefixIn) {
        if (!prefixIn.trim().equals("")) {
            this.prefix = prefixIn.trim();
        }
    }
    
    /**
     * Returns the <code>postfix</code> or file extension for the auto generated
     * xml documents or <code>null</code> if not set. The postfix will replace
     * an existing file extension. Examples:
     * <p>
     *   The postfix <b>vxml</b> and the document name <b>foobar.xml</b> will 
     *   get <b>foobar.vxml</b>.
     * </p>
     * <p>
     *   The postfix <b>vxml</b> and the document name <b>foobar</b> will also 
     *   get <b>foobar.vxml</b>.
     * </p>
     * <p>
     *   The postfix <b>vxml</b> and the document name <b>foo.bar.xml</b> will 
     *   get <b>foo.bar.vxml</b>.
     * </p>
     * This shows that only the part after the last dot will be replaced or if
     * no dot is present the <code>postfix</code> will be appended.
     *  
     * @return The <code>postfix</code> for the auto generated xml documents or
     *         <code>null</code>.
     * @see de.xplib.nexd.api.vcl.VCLSchema#getPostfix()
     */
    public String getPostfix() {
        return this.postfix;
    }
    
    /**
     * Setter method for the <code>postfix</code> or file extension of the auto 
     * generated xml documents.
     *  
     * @param postfixIn The file extension for auto generated xml documents. 
     */
    public void setPostfix(final String postfixIn) {
        if (!postfixIn.trim().equals("")) {
            this.postfix = postfixIn.trim();
        }
    }
    
    /**
     * Returns <code>true</code> if the auto generated documents get an 
     * sequential number. If the <code>name</code> attribute has a value 
     * different to <code>null</code> this method will allways return 
     * <code>true</code>. Examples:
     * <p>
     *   Enumerate is set to <code>true</code> and we have the xml document 
     *   names <i>'foo.xml', 'bar.xml'</i> and <i>'foobar.xml'</code>. Then the
     *   documents will get the names <i>'foo1.xml', 'bar2.xml'<i> and 
     *   <i>'foobar3.xml'</i>.
     * </p>
     * <p>
     *   Enumerate is set to <code>true</code> and the <code>name</code> 
     *   attribute is set to <i>'document'</i>. Then the documents will get
     *   the names <i>document1, document2, ...</i>.
     * </p>
     * <p>
     *   Enumerate is set to <code>false</code> but the <code>name</code> 
     *   attribute is set to a value other than <code>null</code>. Then we will
     *   get the same result as in the previous paragraph.
     * </p>
     * 
     * @return <code>true</code> if the generated document get a sequential 
     *         number, otherwise <code>false</code>.
     * @see de.xplib.nexd.api.vcl.VCLSchema#isEnumerate()
     */
    public boolean isEnumerate() {
        return ((this.name == null || this.name.equals(""))
                ? this.enumerate : true);
    }
    
    /**
     * Setter method for the <code>enumerate</code> attribute.
     *  
     * @param enumerateIn <code>true</code> if the documents will get a 
     *                    sequential number.
     */
    public void setEnumerate(final boolean enumerateIn) {
        this.enumerate = enumerateIn;
    }
    
    /**
     * Returns the <code>name</code> for the auto generated xml documents or
     * <code>null</code>. If this attribute has a value different to 
     * <code>null</code>, then the return value of {@link #isEnumerate()} is
     * allways <code>true</code>. Examples:
     * <p>
     *   <code>name</code> is set to <i>'vdoc.vxml'</i> and 
     *   <code>enumerate</code> is set to <i>true</i>, then the generated 
     *   documents will get the names <i>'vdoc1.vxml', 'vdoc2.vxml', ...</i>
     * </p>
     * <p>
     *   <code>name</code> is set to <i>'vdoc'</i>, <code>prefix</code> is set 
     *   to <i>'vxml'</i> and <code>enumerate</code> is <i>false</i>, then the 
     *   document will nevertheless get a sequential number and the names will 
     *   look like <i>'vdoc1.vxml', 'vdoc2.vxml', ...</i>.
     * </p>
     * 
     * @return The <code>name</code> for all documents or <code>null</code>.
     * @see de.xplib.nexd.api.vcl.VCLSchema#getName()
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Setter method for the <code>name</code> attribute.
     * 
     * @param nameIn The <code>name</code> for the auto generated xml documents.
     */
    public void setName(final String nameIn) {
        if (nameIn == null) {
            this.name = "";
        } else if (!nameIn.trim().equals("")) {
            this.name = nameIn.trim();
        }
    }
    
    /**
     * Returns the root <code>{@link VCLCollectionReference}</code> object. This
     * reference is used as starting point for the automatic generation of xml
     * documents.
     * 
     * @return The root {@link VCLCollectionReference} of the Virtual Collection
     *         Language Schema.
     * @see de.xplib.nexd.api.vcl.VCLSchema#getCollectionReference()
     */
    public VCLCollectionReference getCollectionReference() {
        return this.collRef;
    }
    
    /**
     * Setter method for the root <code>{@link VCLCollectionReference}</code>
     * instance.
     * 
     * @param refIn The root {@link VCLCollectionReference} instance.
     */
    public void setCollectionReference(final VCLCollectionReferenceImpl refIn) {
        this.collRef = refIn;
    }
    
    /**
     * <p>Accept method for each <code>VCLNode</code>. This method is called 
     * from the {@link VCLSchemaVisitor} and then the visitors 
     * <code>visit()</code> method is called from the concrete 
     * <code>VCLNode</code> implementation.</p> 
     *  
     * @param visitorIn <p>The {@link VCLSchemaVisitor} that wants to visit the
     *                  concrete <code>VCLNode</code> implementation.</p>
     * @throws InvalidCollectionReferenceException <p>This is thrown, if a 
     *                       referenced {@link org.xmldb.api.base.Collection} 
     *                       doesn't exist or it is an instance of 
     *                       {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an 
     *                               invalid syntax.</p> 
     * @throws InvalidVCLSchemaException <p>This is thrown, if the xml Document
     *                                   doesn't match the required structure or
     *                                   it doesn't define all necessary 
     *                                   attributes.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is 
     *                                     thrown, if a variable is accessed 
     *                                     that was not declared before.</p>
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p> 
     * @throws XMLDBException <p>If any database specific error occures.</p>
     * @see de.xplib.nexd.api.vcl.VCLNode#accept(
     *      de.xplib.nexd.api.vcl.VCLSchemaVisitor)
     */
    public void accept(final VCLSchemaVisitor visitorIn) 
            throws InvalidCollectionReferenceException, 
                   InvalidQueryException, 
                   InvalidVCLSchemaException, 
                   UndeclaredVariableException, 
                   VariableExistsException, 
                   XMLDBException {
        
        visitorIn.visit(this);
    }
    
    
    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.modules.XMLResource#getContentAsDOM()
     */
    public Node getContentAsDOM() throws XMLDBException {
        return this.content;
    }
    /**
     * <Some description here>
     * 
     * @param handler
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.modules.XMLResource#getContentAsSAX(
     *      org.xml.sax.ContentHandler)
     */
    public void getContentAsSAX(final ContentHandler handler) 
            throws XMLDBException {
        // TODO Auto-generated method stub

    }
    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.modules.XMLResource#getDocumentId()
     */
    public String getDocumentId() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * <Some description here>
     * 
     * @param contentIn
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.modules.XMLResource#setContentAsDOM(org.w3c.dom.Node)
     */
    public void setContentAsDOM(final Node contentIn) throws XMLDBException {
        // TODO Auto-generated method stub

    }
    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.modules.XMLResource#setContentAsSAX()
     */
    public ContentHandler setContentAsSAX() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Resource#getContent()
     */
    public Object getContent() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Resource#getId()
     */
    public String getId() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Resource#getParentCollection()
     */
    public Collection getParentCollection() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Resource#getResourceType()
     */
    public String getResourceType() throws XMLDBException {
        return XMLResource.RESOURCE_TYPE;
    }
    /**
     * <Some description here>
     * 
     * @param value
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Resource#setContent(java.lang.Object)
     */
    public void setContent(final Object value) throws XMLDBException {
        // TODO Auto-generated method stub

    }
}
