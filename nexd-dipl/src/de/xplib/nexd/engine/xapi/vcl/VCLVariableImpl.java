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
 * $Log: VCLVariableImpl.java,v $
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

import de.xplib.nexd.api.vcl.VCLSchemaVisitor;
import de.xplib.nexd.api.vcl.VCLVariable;
import de.xplib.nexd.api.vcl.VariableExistsException;

/**
 * <p>This class represents the <code>variable</code> 
 * <code>{@link Element}</code> in the DOM <code>{@link org.w3c.dom.Document}
 * </code> of the parsed <code>{@link de.xplib.nexd.api.vcl.VCLSchema}</code>. 
 * This object contains a XPath expression and a name which is used to access 
 * the content later.</p>
 * <pre>
 *   &lt;vcl:variable name="foo" select="/foo[@bar='manuel']/*" /&gt;
 * </pre>
 * <br />
 * <b>Scope</b>
 * <p>The following example shows the scope of a variable in a Virtual 
 * Collection Language Schema</p>
 * <pre>
 *   &lt;vcl:collection match="/db/foo"&gt;                                  (1)
 *     &lt;vcl:variable name="var1" select="//*[@id=1]" /&gt;
 *     ...
 *     &lt;vcl:collection match="/db/bar"&gt;                                (2)
 *       &lt;vcl:variable name="var11" select="//*[@type='int']" /&gt;
 *       ...
 *       &lt;vcl:collection match="/db/foobar"&gt;                           (3)
 *         &lt;vcl:variable name="var111" select="//*[@name='manu']" /&gt;   
 *         ...
 *       &lt;/vcl:collection&gt;
 *     &lt;/vcl:collection&gt;
 *     &lt;vcl:collection match="/db/foobar"&gt;                             (4)
 *       &lt;vcl:variable name="var12" select="//*[@name='manuel']" /&gt;
 *       ...
 *     &lt;/vcl:collection&gt;
 *   &lt;/vcl:collection&gt;
 * </pre>
 * <ul>
 *   <li><b>var1</b> is visible everywhere, this means in the collections
 *     <i>1</i>, <i>2</i>, <i>3</i> and <i>4</i>.
 *   </li>
 *   <li><b>var11</b> is visible in the collections <i>2</i> and <i>3</i>.</li>
 *   <li><b>var111</b> is only visible in <i>3</i>.</li>
 *   <li><b>var12</b> is only visible in <i>4</i>.</li>
 * </ul>.
 * 
 * <p>This class implements a part of the GoF Composite pattern (163).</p>
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class VCLVariableImpl implements VCLVariable {

    /**
     * <p>The reference to the underlying {@link Element}.</p>
     */
    private final Element element;
    
    /**
     * <p>The variable identifier.</p>
     */
    private final String name;
    
    /**
     * <p>The XPath expression.</p>
     */
    private final String select;

    /**
     * <p>Constructor.</p>
     * 
     * @param elementIn <p>The reference to the underlying {@link Element}.</p>
     * @param nameIn <p>The variable identifier.</p>
     * @param selectIn <p>The XPath expression.</p>
     */
    public VCLVariableImpl(final Element elementIn,
                           final String nameIn,
                           final String selectIn) {
        super();
        
        this.element = elementIn;
        this.name    = nameIn;
        this.select  = selectIn;
    }

    /**
     * <p>Returns the value of the <code>@name</code> <code>
     * {@link org.w3c.dom.Attr}</code>. This is the name that identifies the 
     * variable.</p>
     * 
     * @return <p>The variable identifier.</p>
     * @see de.xplib.nexd.api.vcl.VCLVariable#getName()
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p>Returns the value of the <code>@select</code> 
     * <code>{@link org.w3c.dom.Attr}</code>. This is a XPath expression.</p>
     * 
     * @return <p>The XPath expression.</p>
     * @see de.xplib.nexd.api.vcl.VCLVariable#getSelect()
     */
    public String getSelect() {
        return this.select;
    }

    /**
     * <p>Returns the reference to the underlying <code>{@link Element}</code>
     * in the parsed <code>{@link de.xplib.nexd.api.vcl.VCLSchema}</code>.</p>
     * 
     * @return <p>The reference to the underlying {@link Element}.</p>
     * @see de.xplib.nexd.api.vcl.VCLVariable#getContentAsDOM()
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
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p> 
     * @throws XMLDBException <p>If any database specific error occures.</p>
     * @see de.xplib.nexd.api.vcl.VCLNode#accept(
     *      de.xplib.nexd.api.vcl.VCLSchemaVisitor)
     */
    public void accept(final VCLSchemaVisitor visitorIn) 
            throws InvalidQueryException, 
                   VariableExistsException, 
                   XMLDBException {
        
        visitorIn.visit(this);
    }
}
