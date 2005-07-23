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
 * $Log: ResourceType.java,v $
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
import java.util.Map;

import org.sixdml.dbmanagement.SixdmlResource;

import de.fmui.spheon.jsoap.Entry;
import de.fmui.spheon.jsoap.Parameter;
import de.fmui.spheon.jsoap.RingException;
import de.fmui.spheon.jsoap.SoapConfig;
import de.fmui.spheon.jsoap.SoapException;
import de.fmui.spheon.jsoap.util.ObjectMap;
import de.xplib.nexd.api.VirtualResource;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class ResourceType extends AbstractEncoder {
        
    /**
     * Server mapping of remote resources.
     */
    protected static Map resources = new HashMap();

    /**
     * Serialize the given value und creates an entry.
     * 
     * @param configIn ..
     * @param parent ..
     * @param name ..
     * @param value ..
     * @param vclass ..
     * @param objMap ..
     * @param xsdNS ..
     * @param xsiNS ..
     * @return ..
     * @throws SoapException ..
     * @see de.fmui.spheon.jsoap.AbstractEncoding#getEntry(
     *      de.fmui.spheon.jsoap.SoapConfig, de.fmui.spheon.jsoap.Entry, 
     *      java.lang.String, java.lang.Object, java.lang.Class, 
     *      de.fmui.spheon.jsoap.util.ObjectMap, 
     *      java.lang.String, java.lang.String)
     */
    public Entry getEntry(final SoapConfig configIn, 
                          final Entry parent, 
                          final String name,
                          final Object value, 
                          final Class vclass, 
                          final ObjectMap objMap, 
                          final String xsdNS,
                          final String xsiNS) throws SoapException {
        
        Entry entry;
        if (value == null) {
            entry = super.getEntry(
                configIn, parent, name, value, vclass, objMap, xsdNS, xsiNS);
        } else if (value instanceof VirtualResource) {
            entry = new VirtualResourceType().getEntry(
                    configIn, parent, name, value, 
                    SixdmlResource.class, 
                    objMap, xsdNS, xsiNS);
        } else if (value instanceof SixdmlResource) {
            entry = new SixdmlResourceType().getEntry(
                    configIn, parent, name, value, 
                    SixdmlResource.class, 
                    objMap, xsdNS, xsiNS);
        } else {
            entry = new Entry(parent, name);
            this.setNull(entry, xsiNS);
        }
        return entry;
    }
    
    /**
     * Deserialize a value from an entry.
     * 
     * @param configIn ..
     * @param param ..
     * @param entry ..
     * @param idMap ..
     * @param edgeVec ..
     * @param arrayTypeStr ..
     * @return ..
     * @throws SoapException ..
     * @throws RingException ..
     */
    public Object getValue(final SoapConfig configIn, 
                           final Parameter param, 
                           final Entry entry,
                           final Map idMap, 
                           final List edgeVec, 
                           final String arrayTypeStr) throws SoapException, 
                                                             RingException {
        
        String type = entry.getChild("type").getValue();
        
        Object result;
        if (type.equals("SixdmlResource")) {
            result = new SixdmlResourceType().getValue(
                    configIn, param, entry, 
                    (HashMap) idMap, edgeVec, arrayTypeStr);
        } else if (type.equals("VirtualResource")) {
            result = new VirtualResourceType().getValue(
                    configIn, param, entry, 
                    (HashMap) idMap, edgeVec, arrayTypeStr);
        } else {
            throw new SoapException("Invalid Resource Type transfered");
        }
        return result;
    }
}
