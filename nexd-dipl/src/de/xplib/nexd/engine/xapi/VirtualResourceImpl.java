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
 * $Log: VirtualResourceImpl.java,v $
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.3  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.2  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
 *
 * Revision 1.1  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 */
package de.xplib.nexd.engine.xapi;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualResource;
import de.xplib.nexd.api.pcvr.PCVResource;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class VirtualResourceImpl 
    extends AbstractXMLResource 
    implements VirtualResource {
    
    /**
     * Constructor.
     * 
     * @param parentIn The parent collection of this <code>Resource</code>.
     * @param contentIn The content of the virtual resource.
     * @param resourceIdIn <p>The unique <code>Resource</code> id.</p>
     */
    public VirtualResourceImpl(final VirtualCollectionImpl parentIn,
                               final Document contentIn,
                               final String resourceIdIn) {
        
        super(parentIn, resourceIdIn);
        
        this.content = contentIn;
    }
    
    /**
     * <Some description here>
     * 
     * @param contentIn
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.modules.XMLResource#setContentAsDOM(
     *      org.w3c.dom.Node)
     */
    public void setContentAsDOM(final Node contentIn) throws XMLDBException {
        throw new XMLDBException(
                ErrorCodes.NOT_IMPLEMENTED, 
                "VirtualResource doesn't allow content changes.");
    }
    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.modules.XMLResource#setContentAsSAX()
     */
    public ContentHandler setContentAsSAX() throws XMLDBException {
        throw new XMLDBException(
                ErrorCodes.NOT_IMPLEMENTED, 
                "VirtualResource doesn't allow content changes.");
    }
    
    /**
     * <Some description here>
     * 
     * @param value
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Resource#setContent(java.lang.Object)
     */
    public void setContent(final Object value) throws XMLDBException {
        throw new XMLDBException(
                ErrorCodes.NOT_IMPLEMENTED, 
                "VirtualResource doesn't allow content changes.");
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws XMLDBException
     * @see _de.xplib.nexd.api.VirtualResource#getPreCompiledResource()
     */
    public PCVResource getPreCompiledResource() throws XMLDBException {
        
        VirtualCollectionImpl vcoll = (VirtualCollectionImpl) 
                this.getParentCollection(); 
        return vcoll.engine.queryPCVResource(vcoll, this.getId());
    }
}
