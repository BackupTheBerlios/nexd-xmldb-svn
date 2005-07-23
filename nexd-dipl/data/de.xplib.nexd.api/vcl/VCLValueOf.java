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
 * $Log: VCLValueOf.java,v $
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
 * <p>This class represents the <code>value-of</code> 
 * <code>{@link Element}</code> in the DOM <code>{@link org.w3c.dom.Document}
 * </code> of the parsed <code>{@link VCLSchema}</code>. This object contains a
 * XPath expression or it references a defined variable 
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
 * @version $Revision: 1.3 $
 * @see de.xplib.nexd.api.vcl.VCLNode
 * @see de.xplib.nexd.api.vcl.VCLCollectionReference
 * @see de.xplib.nexd.api.vcl.represents
 * @see de.xplib.nexd.api.vcl.VCLSchema
 * @see de.xplib.nexd.api.vcl.VCLVariable
 */
public interface VCLValueOf extends VCLNode {

    /**
     * <p>The name of the <code>{@link Element}</code> that represents this 
     * in the <code>{@link VCLSchema}</code>.</p>
     */
    String ELEM_NAME = "value-of";
    
    /**
     * <p>The <code>{@link org.w3c.dom.Attr}</code> name for the XPath or 
     * variable select expression.</p>
     */
    String ATTR_SELECT = "select";
    
    /**
     * <p>Returns the value of the <code>@select</code> 
     * <code>{@link org.w3c.dom.Attr}</code>. This can be a XPath or a variable
     * expression.</p>
     * 
     * @return <p>The XPath or variable expression.</p>
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
