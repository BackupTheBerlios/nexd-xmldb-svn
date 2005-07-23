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
 * $Log: PCVRCompilerImpl.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.2  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.1  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 */
package de.xplib.nexd.engine.xapi.pcvr;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualResource;
import de.xplib.nexd.api.pcvr.AbstractPCVRCompiler;
import de.xplib.nexd.engine.xapi.VirtualCollectionImpl;
import de.xplib.nexd.engine.xapi.VirtualResourceImpl;

/**
 * This class implements the GoF Abstract Factory (87) that returns the concrete
 * {@link _de.xplib.nexd.api.VirtualResource} implementation, which is used by 
 * NEXD.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class PCVRCompilerImpl extends AbstractPCVRCompiler {
    
    /**
     * <p>The context <code>VirtualCollectionImpl</code> instance.</p>
     */
    private final VirtualCollectionImpl coll;
    
    /**
     * <p>Constructor</p>
     * 
     * @param collIn The context <code>VirtualCollectionImpl</code> instance.
     * @throws XMLDBException If any database specific error occures.
     */
    protected PCVRCompilerImpl(final VirtualCollectionImpl collIn)
            throws XMLDBException {
        super();
        
        this.coll = collIn;
    }
    
    /**
     * <p>This method creates a <code>{@link VirtualResource}</code> instance
     * from the given {@link Document} and the <code>idIn</code>.</p>
     * <p>All temporary contents of the <code>@value</code>
     * <code>{@link org.w3c.dom.Attr}</code> must be encoded with the base64
     * codec (<code>{@link Base64#encode(byte[])</code>).</p>
     *
     * @param docIn The <code>{@link _de.xplib.nexd.api.pcvr.PCVResource}</code>
     *              object that should be compiled.
     * @param idIn The id for the generated {@link VirtualResource}.
     * @return The resulting <code>{@link VirtualResource}</code>
     *         instance.
     * @throws SAXException If the content of an <code>@value</code> <code>
     *                      {@link org.w3c.dom.Attr}</code> doesn't contains
     *                      valid xml or text.
     * @throws XMLDBException If anything else goes wrong.
     * @see _de.xplib.nexd.api.pcvr.PCVRCompiler#compile(
     *      org.w3c.dom.Document, java.lang.String)
     */
    public VirtualResource compile(final Document docIn, final String idIn)
            throws SAXException, XMLDBException {
        
        return this.compile(new PCVResourceImpl(this.coll, docIn, idIn));
    }

    /**
     * <p>Creates an instance of the underlying {@link VirtualResource}
     * implementation. This method implements the GoF Abstract Factory
     * pattern (87).</p>
     *
     * @param nameIn <p>The name or id of the newly created 
     *               {@link VirtualResource} object.</p>
     * @param docIn <p>The content of the new {@link VirtualResource} object</p>
     * @return <p>A concrete implementation of {@link VirtualResource}.</p>
     * @throws XMLDBException <p>If anything else goes wrong.</p>
     * @see _de.xplib.nexd.api.pcvr.AbstractPCVRCompiler#createVirtualResource()
     */
    protected VirtualResource createVirtualResource(
            final String nameIn, final Document docIn) throws XMLDBException {
        
        VirtualResourceImpl res = new VirtualResourceImpl(
                this.coll, docIn, nameIn);

        return res;
    }

}
