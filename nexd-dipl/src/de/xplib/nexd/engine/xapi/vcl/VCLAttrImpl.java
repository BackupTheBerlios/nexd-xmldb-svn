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
 * $Log: VCLAttrImpl.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.4  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.3  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import org.sixdml.exceptions.InvalidQueryException;
import org.w3c.dom.Attr;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLAttr;
import de.xplib.nexd.api.vcl.VCLSchemaVisitor;

/**
 * <p>This class represents the dynamic <code>{@link org.w3c.dom.Attr}</code>
 * instances everywhere in the DOM <code>{@link org.w3c.dom.Document}
 * </code> of the parsed <code>{@link de.xplib.nexd.api.vcl.VCLSchema}</code>.
 * This attribute contains a XPath expression or it references a defined
 * variable ({@link de.xplib.nexd.api.vcl.VCLVariable}).</p>
 * <ul>
 *   <li><b>XPath</b>
 *   <pre>
 *   &lt;foo vcl:bar="/foo[@bar='manuel']/*" /&gt;
 *
 *   will become
 *
 *   &lt;foo bar="[Any value here]" /&gt;
 *   </pre></li>
 *   <li><b>Variable</b>
 *   <pre>
 *   &lt;foo vcl:bar="$foobar" /&gt;
 *
 *   will become
 *
 *   &lt;foo bar="[Any value here]" /&gt;
 *   </pre></li>
 * </ul>
 * <p>This interface implements a part of the GoF Composite pattern (163).</p>
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class VCLAttrImpl implements VCLAttr {

    /**
     * <p>The underlying <code>org.w3c.dom.Attr</code> instance.</p>
     */
    private final Attr attr;

    /**
     * <p>The local name of the qualified name of the attribute.</p>
     */
    private final String name;

    /**
     * <p>The XPath or variable expression.</p>
     */
    private final String select;

    /**
     * <p>Constructor.</p>
     *
     * @param attrIn <p>The underlying <code>org.w3c.dom.Attr</code> instance.
     *               </p>
     * @param nameIn <p>The local name of the qualified name of the attribute.
     *               </p>
     * @param selectIn <p>The XPath or variable expression.</p>
     */
    public VCLAttrImpl(final Attr attrIn, final String nameIn,
            final String selectIn) {
        super();

        this.attr = attrIn;
        this.name = nameIn;
        this.select = selectIn;
    }

    /**
     * <p>Returns the name of the <code>{@link Attr} that will be created. The
     * name is identified by the local part of the attributes qualified name in
     * the DOM <code>{@link org.w3c.dom.Document}</code> of the parsed <code>
     * {@link de.xplib.nexd.api.vcl.VCLSchema}</code>.</p>
     *
     * @return <p>The local name of the qualified name of the attribute.</p>
     * @see de.xplib.nexd.api.vcl.represents#getName()
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p>Returns the content of the <code>{@link org.w3c.dom.Attr}</code>.
     * This can be a XPath or a variable expression.
     *
     * @return <p>The XPath or variable expression.</p>
     * @see de.xplib.nexd.api.vcl.represents#getSelect()
     */
    public String getSelect() {
        return this.select;
    }

    /**
     * <p>Returns the reference to the underlying <code>{@link Attr}</code>
     * in the parsed <code>{@link de.xplib.nexd.api.vcl.VCLSchema}</code>.</p>
     *
     * @return <p>The reference to the underlying <code>{@link Attr}</code>.</p>
     * @see de.xplib.nexd.api.vcl.represents#getContentAsDOM()
     */
    public Attr getContentAsDOM() {
        return this.attr;
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
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is
     *                                     thrown, if a variable is accessed
     *                                     that was not declared before.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     * @see de.xplib.nexd.api.vcl.VCLNode#accept(
     *      de.xplib.nexd.api.vcl.VCLSchemaVisitor)
     */
    public void accept(final VCLSchemaVisitor visitorIn)
            throws InvalidCollectionReferenceException, InvalidQueryException,
            UndeclaredVariableException, XMLDBException {

        visitorIn.visit(this);
    }

}
