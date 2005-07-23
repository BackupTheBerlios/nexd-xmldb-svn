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
 * $Log: VCLCollectionReferenceImpl.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.5  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.4  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import org.sixdml.exceptions.InvalidQueryException;
import org.w3c.dom.Element;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLCollectionReference;
import de.xplib.nexd.api.vcl.VCLNodeIterator;
import de.xplib.nexd.api.vcl.VCLSchemaVisitor;
import de.xplib.nexd.api.vcl.VariableExistsException;

/**
 * <p>This class represents the <code>collection</code> 
 * <code>{@link Element}</code> in the DOM <code>{@link org.w3c.dom.Document}
 * </code> of the parsed <code>{@link de.xplib.nexd.api.vcl.VCLSchema}</code>. 
 * This object references a {@link org.xmldb.api.base.Collection} in the xml 
 * database.</p>
 * <ul>
 *   <li><b>match</b>
 *   <p>This Element selects the {@link org.xmldb.api.base.Collection} 
 *   <i>bar</i> in its parent {@link org.xmldb.api.base.Collection} <i>foo</i>.
 *   </p>
 *   <pre>
 *   &lt;vcl:collection match="/foo/bar"&gt;
 *   &lt;/vcl:collection&gt;
 *   </pre></li>
 *   <li><b>match + select</b>
 *   <p>This Element selects the {@link org.xmldb.api.base.Collection} 
 *   <i>bar</i> in its parent {@link org.xmldb.api.base.Collection} <i>foo</i>.
 *   But it just returns that {@link org.xmldb.api.modules.XMLResource} objects
 *   where the XPath expression in the <code>@select</code> matches.
 *   </p>
 *   <pre>
 *   &lt;vcl:collection match="/foo/bar" select="/foo[@bar='manuel']"&gt;
 *   &lt;/vcl:collection&gt;
 *   </pre></li>
 * </ul>
 * <p>This class implements a part of the GoF Composite pattern (163).</p> 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class VCLCollectionReferenceImpl implements VCLCollectionReference {
    
    /**
     * @clientCardinality 1
     * @clientRole collIt
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     */

    private VCLCollectionReferenceImpl lnkVCLCollectionReferenceImpl;

    /**
     * @clientCardinality 1
     * @clientRole attrIt
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     */

    /*#private VCLAttrImpl lnkVCLAttrImpl*/

    /**
     * @clientCardinality 1
     * @clientRole valIt
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     */

    /*#private VCLValueOfImpl lnkVCLValueOfImpl*/

    /**
     * @clientCardinality 1
     * @clientRole varIt
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     */

    /*#private VCLVariableImpl lnkVCLVariableImpl*/

    /**
     * <p>The database path of a {@link org.xmldb.api.base.Collection}.</p>
     */
    private final String match;
    
    /**
     * <p>The reference to the underlying {@link Element}.</p>
     */
    private final Element element;
    
    /**
     * <p>The XPath expression or an empty <code>String</code>.</p>
     */
    private String select = "";
    
    /**
     * <p>Wrapper around the collection {@link #references}.</p>
     */
    private final VCLNodeIteratorImpl collIt;
    
    /**
     * <p>Wrapper around the collection {@link #attributes}.</p>
     */
    private final VCLNodeIteratorImpl attrIt;
    
    /**
     * <p>Wrapper around the collection {@link #variables}.</p>
     */
    private final VCLNodeIteratorImpl varIt;
    
    /**
     * <p>Wrapper around the collection {@link #values}.</p>
     */
    private final VCLNodeIteratorImpl valIt;

    /**
     * <p>Constructor.</p>
     * 
     * @param elementIn <p>The reference to the underlying {@link Element}.</p>
     * @param matchIn <p>The database path of a 
     *                {@link org.xmldb.api.base.Collection}.</p>
     */
    public VCLCollectionReferenceImpl(final Element elementIn, 
                                      final String matchIn) {
        super();

        this.element = elementIn;
        this.match   = matchIn;
        
        this.collIt = new VCLNodeIteratorImpl();
        this.attrIt = new VCLNodeIteratorImpl();
        this.varIt  = new VCLNodeIteratorImpl();
        this.valIt  = new VCLNodeIteratorImpl();
    }
    
    /**
     * <p>Returns the value of the <code>@match</code> attribute. This value is
     * the database path of a {@link org.xmldb.api.base.Collection}.</p>
     * 
     * @return <p>The database path of a {@link org.xmldb.api.base.Collection}.
     *         </p>
     * @see de.xplib.nexd.api.vcl.VCLCollectionReference#getMatch()
     */
    public String getMatch() {
        return this.match;
    }
    
    /**
     * <p>Returns the XPath expression in the <code>@select</code> attribute or 
     * an empty <code>String</code> if this attribute is not defined.</p>
     *  
     * @return <p>The XPath expression or an empty <code>String</code>.</p>
     * @see de.xplib.nexd.api.vcl.VCLCollectionReference#getSelect()
     */
    public String getSelect() {
        return this.select;
    }
    
    /**
     * <p>Sets the select expression, which is an XPath query.</p>
     * 
     * @param selectIn <p>The XPath expression or an empty <code>String</code>.
     *                 </p>
     */
    public void setSelect(final String selectIn) {
        this.select = selectIn;
    }
    
    /**
     * <p>Returns the reference to the underlying <code>{@link Element}</code>
     * in the parsed <code>{@link de.xplib.nexd.api.vcl.VCLSchema}</code>.</p>
     * 
     * @return <p>The reference to the underlying <code>{@link Element}</code>.
     *         </p>
     * @see de.xplib.nexd.api.vcl.VCLCollectionReference#getContentAsDOM()
     */
    public Element getContentAsDOM() {
        return this.element;
    }
    
    /**
     * <p>Adds a {@link VCLCollectionReferenceImpl} to this object.</p>
     * 
     * @param refIn <p>The {@link VCLCollectionReferenceImpl} to add</p>
     */
    public void addCollectionReference(final VCLCollectionReferenceImpl refIn) {
        this.collIt.add(refIn);
    }
    
    /**
     * <p>Returns an iterator with the {@link VCLCollectionReference} instances
     * that are one level under this object. If there are no more 
     * {@link VCLCollectionReference} object under this it returns an empty 
     * {@link VCLCollectionReferenceIterator}.</p>
     * 
     * @return <p>A {@link VCLCollectionReferenceIterator} with all 
     *         {@link VCLCollectionReference} instances that are in the context
     *         of this object.</p>
     * @see de.xplib.nexd.api.vcl.VCLCollectionReference#
     *      getCollectionReferences()
     */
    public VCLNodeIterator getCollectionReferences() {
        this.collIt.reset();
        return this.collIt;
    }
    
    /**
     * <p>Adds a {@link VCLAttrImpl} to this object.</p>
     * 
     * @param attributeIn <p>The {@link VCLAttrImpl} to add</p>
     */
    public void addAttribute(final VCLAttrImpl attributeIn) {
        this.attrIt.add(attributeIn);
    }
    
    /**
     * <p>Returns an iterator with the {@link de.xplib.nexd.api.vcl.VCLAttr} 
     * instances that are in the context of this object. Or if there is no 
     * {@link de.xplib.nexd.api.vcl.VCLAttr} instance, it returns an empty 
     * {@link VCLAttrIterator}.</p>
     * 
     * @return <p>A {@link VCLAttrIterator} with all 
     *         {@link de.xplib.nexd.api.vcl.VCLAttr} instances that are in the 
     *         context of this object.</p>
     * @see de.xplib.nexd.api.vcl.VCLCollectionReference#getAttributes()
     */
    public VCLNodeIterator getAttributes() {
        this.attrIt.reset();
        return this.attrIt;
    }
    
    /**
     * <p>Adds a {@link VCLValueOfImpl} to this object.</p>
     * 
     * @param valueIn <p>The {@link VCLValueOfImpl} to add</p>
     */
    public void addValueOf(final VCLValueOfImpl valueIn) {
        this.valIt.add(valueIn);
    }
    
    /**
     * <p>Returns an iterator with the {@link de.xplib.nexd.api.vcl.VCLValueOf} 
     * instances that are in the context of this object. Or if there is no 
     * {@link de.xplib.nexd.api.vcl.VCLValueOf} instance, it returns an empty 
     * {@link VCLValueOfIterator}.</p>
     * 
     * @return <p>A {@link VCLValueOfIterator} with all 
     *         {@link de.xplib.nexd.api.vcl.VCLValueOf} instances that are in 
     *         the context of this object.</p>
     * @see de.xplib.nexd.api.vcl.VCLCollectionReference#getValueOfs()
     */
    public VCLNodeIterator getValueOfs() {
        this.valIt.reset();
        return this.valIt;
    }
    
    /**
     * <p>Adds a {@link VCLVariableImpl} to this object.</p>
     * 
     * @param varIn <p>The {@link VCLVariableImpl} to add</p>
     */
    public void addVariable(final VCLVariableImpl varIn) {
        this.varIt.add(varIn);
    }
    
    /**
     * <p>Returns an iterator with the {@link de.xplib.nexd.api.vcl.VCLVariable}
     * instances that are in the context of this object. Or if there is no 
     * {@link de.xplib.nexd.api.vcl.VCLVariable} instance, it returns an empty 
     * {@link VCLVariableIterator}.</p>
     * 
     * @return <p>A {@link VCLVariableIterator} with all 
     *         {@link de.xplib.nexd.api.vcl.VCLVariable} instances that are in 
     *         the context of this object.</p>
     * @see de.xplib.nexd.api.vcl.VCLCollectionReference#getVariables()
     */
    public VCLNodeIterator getVariables() {
        this.varIt.reset();
        return this.varIt;
    }
    
    /**
     * <p>Accept method for each <code>VCLNode</code>. This method is called 
     * from the {@link de.xplib.nexd.api.vcl.VCLSchemaVisitor} and then the 
     * visitors <code>visit()</code> method is called from the concrete 
     * <code>VCLNode</code> implementation.</p> 
     *  
     * @param visitorIn <p>The {@link de.xplib.nexd.api.vcl.VCLSchemaVisitor} 
     *                  that wants to visit the concrete <code>VCLNode</code> 
     *                  implementation.</p>
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
}
