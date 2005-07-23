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
 * $Log: SixdmlCollectionType.java,v $
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

import de.fmui.spheon.jsoap.Entry;
import de.fmui.spheon.jsoap.Parameter;
import de.fmui.spheon.jsoap.RingException;
import de.fmui.spheon.jsoap.SoapConfig;
import de.fmui.spheon.jsoap.SoapException;
import de.fmui.spheon.jsoap.util.ObjectMap;
import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.xapi.AbstractCollection;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class SixdmlCollectionType extends AbstractEncoder {
    
    /**
     * Service mapping of collections.
     */
    public static Map collections = new HashMap();
    
    /**
     * <Some description here>
     * 
     * @param configIn
     * @param parent
     * @param name
     * @param value
     * @param vclass
     * @param objMap
     * @param xsdNS
     * @param xsiNS
     * @return
     * @throws SoapException
     * @see de.fmui.spheon.jsoap.AbstractEncoding#getEntry(
     *      de.fmui.spheon.jsoap.SoapConfig, 
     *      de.fmui.spheon.jsoap.Entry, 
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
        
        Entry entry = super.getEntry(
                configIn, parent, name, value, vclass, objMap, xsdNS, xsiNS);
        
        if (value != null && value instanceof AbstractCollection) {
            if (entry.getAttribute("name") == null) {
                try {
                    AbstractCollection coll = (AbstractCollection) value;
                    
                    Entry ename = new Entry(entry, "name");
                    ename.setValue(coll.getName());
                    
                    ename.setAttribute(xsiNS + ":type", xsdNS + ":string");
                    
                    Entry etype = new Entry(entry, "type");
                    etype.setValue(
                            (coll instanceof VirtualCollection) ? "1" : "0");
                    
                    Entry eid = new Entry(entry, "internalId");
                    eid.setValue(
                            coll.getStorageCollection()
                                .getInternalId()
                                .export());
                    
                    Entry epath = new Entry(entry, "path");
                    epath.setAttribute(xsiNS + ":type", xsdNS + ":string");
                    epath.setValue(coll.getStorageCollection().getPath());
                    
                    Entry etrans = new Entry(entry, "transaction");
                    etrans.setAttribute(xsiNS + ":type", xsdNS + ":string");
                    etrans.setValue(coll.getProperty(
                            NEXDEngineI.TRANSACTION_ID_KEY));
                    
                    collections.put(etrans.getValue(), coll);
                    
                } catch (Exception e) {
                    throw new SoapException(e.getMessage());
                }
            }
        }
        
        return entry;
    }
    
    /**
     * <Some description here>
     * 
     * @param sconfigIn
     * @param param
     * @param entry
     * @param idMap
     * @param edgeVec
     * @param arrayTypeStr
     * @return
     * @throws SoapException
     * @throws RingException
     * @see de.fmui.spheon.jsoap.AbstractEncoding#getValue(
     *      de.fmui.spheon.jsoap.SoapConfig, 
     *      de.fmui.spheon.jsoap.Parameter, 
     *      de.fmui.spheon.jsoap.Entry, 
     *      java.util.HashMap, java.util.List, java.lang.String)
     */
    public Object getValue(final SoapConfig sconfigIn, 
                           final Parameter param, 
                           final Entry entry,
                           final HashMap idMap, 
                           final List edgeVec, 
                           final String arrayTypeStr) throws SoapException, 
                                                             RingException {
                        
        String iid  = entry.getAttribute("path");
        // For later use
        // String path = "";
        // String name = "";
        
        List children = entry.getChildren();
        if (children != null) {
            for (int i = 0, s = children.size(); i < s; i++) {
                Entry child = (Entry) children.get(i);
                
                String local = child.getLocal();
                if (local.equals("path")) {
                    iid = this.getParamValue(sconfigIn, child, idMap, edgeVec);
                }
            }
        }
        
        if (collections.containsKey(iid)) {
            return collections.get(iid);
        }
        throw new SoapException("Invalid internal id.");
    }
    
    /**
     * Returns the value of a request parameter.
     * 
     * @param sconfigIn ..
     * @param childIn ..
     * @param idMap ..
     * @param edgeVec ..
     * @return ..
     * @throws SoapException ..
     * @throws RingException ..
     */
    private String getParamValue(final SoapConfig sconfigIn,
                                 final Entry childIn,
                                 final Map idMap,
                                 final List edgeVec) throws SoapException,
                                                            RingException {
        
        return (String) new Parameter(
                sconfigIn, childIn, null, 
                (HashMap) idMap, edgeVec, null).getValue();
    }
    
    /**
     * Removes the given collection from the internal local - remote mapping.
     * 
     * @param collIn The collection to remove.
     */
    public static void removeCollection(final AbstractCollection collIn) {
        if (collections.containsValue(collIn)) {
            collections.remove(collIn.getStorageCollection().getInternalId());
        }
    }
}
