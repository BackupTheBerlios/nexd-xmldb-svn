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
 * $Log: VirtualResource.java,v $
 * Revision 1.5  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.4  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.3  2005/03/26 12:14:20  nexd
 * UML documentation.
 *
 * Revision 1.2  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api;

import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.api.pcvr.PCVResource;


/**
 * <p>Provides access to the virtual XML resources stored in the database. A 
 * <code>VirtualResource</code> can be accessed either as text XML or via the 
 * DOM or SAX APIs.</p>
 *
 * <p>The default behavior for {@link org.xmldb.api.base.Resource#getContent()} 
 * is to work with XML data as text so these methods work on <code>String</code>
 * content. This is the same behaviour like XMLResource provides it. But the 
 * method {@link org.xmldb.api.base.Resource#setContent(java.lang.Object)} of 
 * class VirtualResource has a different behaviour than the same method in class
 * XMLResource. This statement also applies to the methods 
 * {@link org.xmldb.api.modules.XMLResource#setContentAsDOM(org.w3c.dom.Node)} 
 * and {@link org.xmldb.api.modules.XMLResource#setContentAsSAX()}.</p>
 * 
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public interface VirtualResource extends XMLResource {

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole preCompiled
     */
    /*#_de.xplib.nexd.api.pcvr.PCVResource lnkResource*/
    
    /**
     * <p>This method returns the pre compiled virtual resource. This method is 
     * not required for the public database access, but it can help a 
     * {@link de.xplib.nexd.api.vcl.VCLSchema} designer to find bugs.</p>
     *  
     * @return <p>The pre compiled virtual Resource.</p>
     * @exception XMLDBException
     *            <p>This <code>Exception</code> is thrown, if any database 
     *            specific error occurs. The returned error code is
     *            {@link org.xmldb.api.base.ErrorCodes#VENDOR_ERROR}.</p>
     */
    PCVResource getPreCompiledResource() throws XMLDBException;

}
