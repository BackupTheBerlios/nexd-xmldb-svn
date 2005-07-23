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
 * $Log: VCLSchema.java,v $
 * Revision 1.6  2005/04/22 14:59:42  nexd
 * SOAP support and performance update.
 *
 * Revision 1.5  2005/04/18 17:42:18  nexd
 * Minor API changes, switched from org.w3c.domDocument to org.w3c.dom.Node. 
 * Now the interfaces are compatible with the XMLResource interface.
 *
 * Revision 1.4  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.3  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api.vcl;

import org.xmldb.api.modules.XMLResource;
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
 * @version $Revision: 1.6 $
 * @see de.xplib.nexd.api.vcl.VCLAttr
 * @see de.xplib.nexd.api.vcl.VCLCollectionReference
 * @see de.xplib.nexd.api.vcl.VCLNode
 * @see de.xplib.nexd.api.vcl.VCLValueOf
 * @see de.xplib.nexd.api.vcl.VCLVariable
 */
public interface VCLSchema extends VCLNode, XMLResource {

    /**
     * @clientCardinality 0..1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#de.xplib.nexd.api.vcl.VCLCollectionReference lnkColl*/

    /**
     * The default xml namespace prefix for Virtual Collection Language
     * elements in a <code>VCLSchema</code>.
     */
    String NAMESPACE_PREFIX = "vcl";

    /**
     * The xml namespace uri for all Virtual Collection Language uris.
     */
    String NAMESPACE_URI = "http://nexd.xplib.de/vcl/version/1.0";

    /**
     * The name of the schema element.
     */
    String ELEM_NAME = "schema";

    /**
     * The attribute name for the prefix setting.
     */
    String ATTR_PREFIX = "prefix";

    /**
     * The attribute name for the postfix setting.
     */
    String ATTR_POSTFIX = "postfix";

    /**
     * The attribute name for the enumerate setting.
     */
    String ATTR_ENUMERATE = "enumerate";

    /**
     * The attribute name for the name setting.
     */
    String ATTR_NAME = "name";

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
     */
    String getPrefix();

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
     */
    String getPostfix();

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
     */
    boolean isEnumerate();

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
     */
    String getName();

    /**
     * Returns the root <code>{@link VCLCollectionReference}</code> object. This
     * reference is used as starting point for the automatic generation of xml
     * documents.
     *
     * @return The root {@link VCLCollectionReference} of the Virtual Collection
     *         Language Schema.
     */
    VCLCollectionReference getCollectionReference();
}