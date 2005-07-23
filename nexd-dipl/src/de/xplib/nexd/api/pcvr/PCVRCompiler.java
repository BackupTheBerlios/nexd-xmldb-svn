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
 * $Log: PCVRCompiler.java,v $
 * Revision 1.7  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.6  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package de.xplib.nexd.api.pcvr;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualResource;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.7 $
 */
public interface PCVRCompiler {

    /**
     * @supplierRole input
     */
    /*#_de.xplib.nexd.api.pcvr.PCVResource Dependency_Link*/

    /**
     * @supplierRole result
     */
    /*#_de.xplib.nexd.api.VirtualResource Dependency_Link1*/

    /**
     * <p>This method creates a <code>{@link VirtualResource}</code> instance
     * from the given <code>{@link PCVResource}</code>.</p>
     * <p>All temporary contents of the <code>@value</code>
     * <code>{@link org.w3c.dom.Attr}</code> must be encoded with the base64
     * codec (<code>{@link Base64#encode(byte[])</code>).</p>
     *
     * @param pcvResIn <p>The <code>{@link PCVResource}</code> object that
     *                 should be compiled.</p>
     * @return <p>The resulting <code>{@link VirtualResource}</code>
     *         instance.</p>
     * @throws SAXException <p>If the content of an <code>@value</code> <code>
     *                      {@link org.w3c.dom.Attr}</code> doesn't contains
     *                      valid xml or text.</p>
     * @throws XMLDBException <p>If anything else goes wrong.</p>
     */
    VirtualResource compile(final PCVResource pcvResIn) throws SAXException,
            XMLDBException;

    /**
     * <p>This method creates a <code>{@link VirtualResource}</code> instance
     * from the given {@link Document} and the <code>idIn</code>.</p>
     * <p>All temporary contents of the <code>@value</code>
     * <code>{@link org.w3c.dom.Attr}</code> must be encoded with the base64
     * codec (<code>{@link Base64#encode(byte[])</code>).</p>
     *
     * @param docIn The <code>{@link PCVResource}</code> object that
     *              should be compiled.
     * @param idIn The id for the generated {@link VirtualResource}.
     * @return The resulting <code>{@link VirtualResource}</code>
     *         instance.
     * @throws SAXException If the content of an <code>@value</code> <code>
     *                      {@link org.w3c.dom.Attr}</code> doesn't contains
     *                      valid xml or text.
     * @throws XMLDBException If anything else goes wrong.
     */
    VirtualResource compile(final Document docIn, final String idIn)
            throws SAXException, XMLDBException;
}
