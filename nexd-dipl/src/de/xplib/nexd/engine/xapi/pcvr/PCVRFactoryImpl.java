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
 * $Log: PCVRFactoryImpl.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package de.xplib.nexd.engine.xapi.pcvr;

import org.w3c.dom.Document;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.pcvr.AbstractPCVRFactory;
import de.xplib.nexd.api.pcvr.PCVRCompiler;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.engine.xapi.VirtualCollectionImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class PCVRFactoryImpl extends AbstractPCVRFactory {

    /**
     * Returns an instance <code>PCVRCompiler</code> that is provided by this
     * fatcory implementation.
     * 
     * @param collIn The <code>VirtualCollection</code> instance, which will be
     *               the context for the <code>PCVRCompile</code> implementation
     * @return A concrete implementation of <code>PCVRCompile</code> that is 
     *         associated with the given <code>collIn</code> 
     * @throws XMLDBException If anything goes wrong during the instantiation or
     *                        a database specific error occures.
     * @see _de.xplib.nexd.api.pcvr.AbstractPCVRFactory#newPCVRCompiler(
     *      _de.xplib.nexd.api.VirtualCollection)
     */
    public PCVRCompiler newPCVRCompiler(final VirtualCollection collIn) 
            throws XMLDBException {
        
        return new PCVRCompilerImpl((VirtualCollectionImpl) collIn);
    }

    
    /**
     * Factory method that creates a concrete instance of the 
     * <code>PCVResource</code> interface for the given <code>nameIn</code>, the
     * DOM <code>Document</code> and the <code>VirtualCollection</code>.
     * 
     * @param collIn The context <code>VirtualCollection</code> instance for the
     *               <code>PCVResource</code> that will be created
     * @param nameIn The name of this new <code>PCVResource</code>.
     * @param pcvDocIn The raw XML data for this new <code>PCVResource</code>
     * @return A concrete implementation of <code>PCVResource</code> that is 
     *         associated with the given <code>collIn</code>.
     * @throws XMLDBException If anything goes wrong during the instantiation or
     *                        a database specific error occures.
     * @see _de.xplib.nexd.api.pcvr.AbstractPCVRFactory#newPCVResource(
     *      _de.xplib.nexd.api.VirtualCollection, java.lang.String, 
     *      org.w3c.dom.Document)
     */
    public PCVResource newPCVResource(final VirtualCollection collIn,
                                      final String nameIn, 
                                      final Document pcvDocIn)
            throws XMLDBException {
        
        pcvDocIn.getDocumentElement().setAttribute(
                PCVResource.ATTR_SCHEMA_RESNAME, nameIn);

        Document doc = (Document) pcvDocIn.cloneNode(true);

        return new PCVResourceImpl(collIn, doc, nameIn);
    }
}
