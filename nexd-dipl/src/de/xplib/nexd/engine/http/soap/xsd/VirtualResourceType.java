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
 * $Log: VirtualResourceType.java,v $
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 */
package de.xplib.nexd.engine.http.soap.xsd;

import java.util.HashMap;
import java.util.List;

import de.fmui.spheon.jsoap.Entry;
import de.fmui.spheon.jsoap.Parameter;
import de.fmui.spheon.jsoap.RingException;
import de.fmui.spheon.jsoap.SoapConfig;
import de.fmui.spheon.jsoap.SoapException;
import de.xplib.nexd.api.VirtualResource;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class VirtualResourceType extends SixdmlResourceType {
    
    /**
     * Constructor.
     */
    public VirtualResourceType() {
        super();
        
        this.set(VirtualResource.class, "VirtualResource");
    }
    
    /**
     * Deserialize a value from an entry.
     * 
     * @param configIn
     * @param param
     * @param entry
     * @param idMap
     * @param edgeVec
     * @param arrayTypeStr
     * @return
     * @throws SoapException
     * @throws RingException
     * @see de.fmui.spheon.jsoap.AbstractEncoding#getValue(
     *      de.fmui.spheon.jsoap.SoapConfig, de.fmui.spheon.jsoap.Parameter, 
     *      de.fmui.spheon.jsoap.Entry, java.util.HashMap, java.util.List, 
     *      java.lang.String)
     */
    public Object getValue(final SoapConfig configIn, 
                           final Parameter param, 
                           final Entry entry,
                           final HashMap idMap, 
                           final List edgeVec, 
                           final String arrayTypeStr)
            throws SoapException, RingException {
        
        throw new SoapException(
                "A VirtualResource cannot be transfered to engine.");
    }
}
