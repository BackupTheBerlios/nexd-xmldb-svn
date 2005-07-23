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
 * $Log: VCLCollectionReference.java,v $
 * Revision 1.7  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.6  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.5  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.4  2005/03/26 12:14:20  nexd
 * UML documentation.
 *
 * Revision 1.3  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api.vcl;

import org.w3c.dom.Element;

/**
 * <p>This class represents the <code>collection</code>
 * <code>{@link Element}</code> in the DOM <code>{@link org.w3c.dom.Document}
 * </code> of the parsed <code>{@link VCLSchema}</code>. This object references
 * a {@link org.xmldb.api.base.Collection} in the xml database</p>
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
 * @version $Revision: 1.7 $
 * @see de.xplib.nexd.api.vcl.VCLAttr
 * @see de.xplib.nexd.api.vcl.VCLNode
 * @see de.xplib.nexd.api.vcl.VCLSchema
 * @see de.xplib.nexd.api.vcl.VCLValueOf
 * @see de.xplib.nexd.api.vcl.VCLVariable
 */
public interface VCLCollectionReference extends VCLNode {

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole vars
     */
    /*#de.xplib.nexd.api.vcl.VCLVariable lnkVCLVariable*/

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole attrs
     */
    /*#de.xplib.nexd.api.vcl.VCLAttr lnkVCLAttr*/

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole valueOfs
     */
    /*#de.xplib.nexd.api.vcl.VCLValueOf lnkVCLValueOf*/

    /**
     * @clientCardinality 0..1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @supplierRole collRefs
     */
    /*#de.xplib.nexd.api.vcl.VCLCollectionReference lnkCollRef*/

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#de.xplib.nexd.api.vcl.VCLCollectionReferenceIterator lnkVCLCollRefIt*/

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#de.xplib.nexd.api.vcl.VCLVariableIterator lnkVCLVariableIt*/

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#de.xplib.nexd.api.vcl.VCLAttrIterator lnkVCLAttrIt*/

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#de.xplib.nexd.api.vcl.VCLValueOfIterator lnkVCLValueOfIt*/

    /**
     * The name of the collection <code>{@link org.w3c.dom.Element}</code>.
     */
    String ELEM_NAME = "collection";

    /**
     * The name of the required <code>{@link org.w3c.dom.Attr}</code>
     * <i>match</i>, that points on a referenced
     * <code>{@link org.xmldb.api.base.Collection}</code>.
     */
    String ATTR_MATCH = "match";

    /**
     * The name of the optional <code>{@link org.w3c.dom.Attr}</code> that holds
     * <i>select</i> condintion as a XPath expression.
     */
    String ATTR_SELECT = "select";

    /**
     * <p>Returns the value of the <code>@match</code> attribute. This value is
     * the database path of a {@link org.xmldb.api.base.Collection}.</p>
     *
     * @return <p>The database path of a {@link org.xmldb.api.base.Collection}.
     *         </p>
     */
    String getMatch();

    /**
     * <p>Returns the XPath expression in the <code>@select</code> attribute or
     * an empty <code>String</code> if this attribute is not defined.</p>
     *
     * @return <p>The XPath expression or an empty <code>String</code>.</p>
     */
    String getSelect();

    /**
     * <p>Returns an iterator with the {@link VCLCollectionReference} instances
     * that are one level under this object. If there are no more
     * {@link VCLCollectionReference} object under this it returns an empty
     * {@link VCLNodeIterator}.</p>
     *
     * @return <p>A {@link VCLNodeIterator} with all
     *         {@link VCLCollectionReference} instances that are in the context
     *         of this object.</p>
     */
    VCLNodeIterator getCollectionReferences();

    /**
     * <p>Returns an iterator with the {@link VCLAttr} instances that are in the
     * context of this object. Or if there is no {@link VCLAttr} instance, it
     * returns an empty {@link VCLNodeIterator}.</p>
     *
     * @return <p>A {@link VCLNodeIterator} with all {@link VCLAttr} instances
     *         that are in the context of this object.</p>
     */
    VCLNodeIterator getAttributes();

    /**
     * <p>Returns an iterator with the {@link VCLValueOf} instances that are in
     * the context of this object.Or if there is no {@link VCLValueOf} instance,
     * it returns an empty {@link VCLNodeIterator}.</p>
     *
     * @return <p>A {@link VCLNodeIterator} with all {@link VCLValueOf}
     *         instances that are in the context of this object.</p>
     */
    VCLNodeIterator getValueOfs();

    /**
     * <p>Returns an iterator with the {@link VCLVariable} instances that are in
     * the context of this object. Or if there is no {@link VCLVariable}
     * instance, it returns an empty {@link VCLNodeIterator}.</p>
     *
     * @return <p>A {@link VCLNodeIterator} with all {@link VCLVariable}
     *         instances that are in the context of this object.</p>
     */
    VCLNodeIterator getVariables();

    /**
     * <p>Returns the reference to the underlying <code>{@link Element}</code>
     * in the parsed <code>{@link VCLSchema}</code>.</p>
     *
     * @return <p>The reference to the underlying <code>{@link Element}</code>.
     *         </p>
     */
    Element getContentAsDOM();

}