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
 * $Log: VCLValueOfImpl.java,v $
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
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

import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLSchemaVisitor;
import de.xplib.nexd.api.vcl.VCLValueOf;

/**
 * <p>This class represents the <code>value-of</code> 
 * <code>{@link Element}</code> in the DOM <code>{@link org.w3c.dom.Document}
 * </code> of the parsed <code>{@link de.xplib.nexd.api.vcl.VCLSchema}</code>. 
 * This object contains a XPath expression or it references a defined variable 
 * ({@link de.xplib.nexd.api.vcl.VCLVariable}).</p>
 * <ul>
 *   <li><b>XPath</b>
 *   <pre>
 *   &lt;vcl:value-of select="/foo[@bar='manuel']/*" /&gt;
 *   </pre></li>
 *   <li><b>Variable</b>
 *   <pre>
 *   &lt;vcl:value-of select="$foobar" /&gt;
 *   </pre></li>
 * </ul>
 * <p>This class implements a part of the GoF Composite pattern (163).</p> 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class VCLValueOfImpl implements VCLValueOf {
    
    /**
     * <p>The reference to the underlying {@link Element}.</p>
     */
    private final Element element;
    
    /**
     * <p>The XPath or variable expression.</p>
     */
    private final String select;

    /**
     * <p>Constructor.</p>
     * 
     * @param elementIn <p>The reference to the underlying {@link Element}.</p>
     * @param selectIn <p>The XPath or variable expression.</p>
     */
    public VCLValueOfImpl(final Element elementIn, final String selectIn) {
        super();
        
        this.element = elementIn;
        this.select  = selectIn;
    }

    /**
     * <p>Returns the value of the <code>@select</code> 
     * <code>{@link org.w3c.dom.Attr}</code>. This can be a XPath or a variable
     * expression.</p>
     * 
     * @return <p>The XPath or variable expression.</p>
     * @see de.xplib.nexd.api.vcl.VCLValueOf#getSelect()
     */
    public String getSelect() {
        return this.select;
    }

    /**
     * <p>Returns the reference to the underlying <code>{@link Element}</code>
     * in the parsed <code>{@link de.xplib.nexd.api.vcl.VCLSchema}</code>.</p>
     * 
     * @return <p>The reference to the underlying {@link Element}.</p>
     * @see de.xplib.nexd.api.vcl.VCLValueOf#getContentAsDOM()
     */
    public Element getContentAsDOM() {
        return this.element;
    }

    /**
     * <p>Accept method for each <code>VCLNode</code>. This method is called 
     * from the {@link VCLSchemaVisitor} and then the visitors 
     * <code>visit()</code> method is called from the concrete 
     * <code>VCLNode</code> implementation.</p> 
     *  
     * @param visitorIn <p>The {@link VCLSchemaVisitor} that wants to visit the
     *                  concrete <code>VCLNode</code> implementation.</p>
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
            throws InvalidQueryException, 
                   UndeclaredVariableException, 
                   XMLDBException {
        
        visitorIn.visit(this);
    }
}
