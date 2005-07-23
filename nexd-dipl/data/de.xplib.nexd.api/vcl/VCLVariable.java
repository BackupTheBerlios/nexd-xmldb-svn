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
 * $Log: VCLVariable.java,v $
 * Revision 1.3  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api.vcl;

import org.w3c.dom.Element;

/**
 * <p>This class represents the <code>variable</code> 
 * <code>{@link Element}</code> in the DOM <code>{@link org.w3c.dom.Document}
 * </code> of the parsed <code>{@link VCLSchema}</code>. This object contains a
 * XPath expression and a name which is used to access the content later.</p>
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
 * @version $Revision: 1.3 $
 * @see de.xplib.nexd.api.vcl.VCLAttr
 * @see de.xplib.nexd.api.vcl.VCLCollectionReference
 * @see de.xplib.nexd.api.vcl.VCLNode
 * @see de.xplib.nexd.api.vcl.VCLSchema
 * @see de.xplib.nexd.api.vcl.VCLValueOf
 */
public interface VCLVariable extends VCLNode {
    
    /**
     * <p>The name of the <code>{@link Element}</code> that represents this 
     * in the <code>{@link VCLSchema}</code>.</p>
     */
    String ELEM_NAME = "variable";
    
    /**
     * <p>The <code>{@link org.w3c.dom.Attr}</code> name for the variable
     * name.</p>
     */
    String ATTR_NAME = "name";
    
    /**
     * <p>The <code>{@link org.w3c.dom.Attr}</code> name for the XPath 
     * expression.</p>
     */
    String ATTR_SELECT = "select";

    /**
     * <p>Returns the value of the <code>@name</code> <code>
     * {@link org.w3c.dom.Attr}</code>. This is the name that identifies the 
     * variable.</p>
     * 
     * @return <p>The variable identifier.</p>
     */
    String getName();
    
    /**
     * <p>Returns the value of the <code>@select</code> 
     * <code>{@link org.w3c.dom.Attr}</code>. This is a XPath expression.</p>
     * 
     * @return <p>The XPath expression.</p>
     */
    String getSelect();
    
    /**
     * <p>Returns the reference to the underlying <code>{@link Element}</code>
     * in the parsed <code>{@link VCLSchema}</code>.</p>
     * 
     * @return <p>The reference to the underlying <code>{@link Element}</code>.
     *         </p>
     */
    Element getContentAsDOM();
}
