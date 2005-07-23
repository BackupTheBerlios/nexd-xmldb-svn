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
 * $Log: ResourceSetType.java,v $
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

import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;

import de.fmui.spheon.jsoap.AbstractEncoding;
import de.fmui.spheon.jsoap.EncodingWrapperInterface;
import de.fmui.spheon.jsoap.Entry;
import de.fmui.spheon.jsoap.SoapConfig;
import de.fmui.spheon.jsoap.SoapException;
import de.fmui.spheon.jsoap.util.ObjectMap;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class ResourceSetType 
    extends AbstractEncoding 
    implements EncodingWrapperInterface {
    
    /**
     * The Java type.
     */
    private Class type;
    
    /**
     * The SOAP type.
     */
    private String soapType;

    /**
     * <Some description here>
     * 
     * @return
     * @see de.fmui.spheon.jsoap.AbstractEncoding#getType()
     */
    public Class getType() {
        return this.type;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.fmui.spheon.jsoap.AbstractEncoding#getSoapType()
     */
    public String getSoapType() {
        return this.soapType;
    }

    /**
     * Constructor.
     * 
     * @param typeIn The Java type.
     * @param soapTypeIn The SOAP type.
     * @see de.fmui.spheon.jsoap.EncodingWrapperInterface#set(
     *      java.lang.Class, java.lang.String)
     */
    public void set(final Class typeIn, final String soapTypeIn) {
        this.setType(typeIn);
        this.setSoapType(soapTypeIn);
    }

    /**
     * Sets the Java type.
     * 
     * @param typeIn The Java type.
     * @see de.fmui.spheon.jsoap.EncodingWrapperInterface#setType(
     *      java.lang.Class)
     */
    public void setType(final Class typeIn) {
        this.type = typeIn;
    }

    /**
     * Sets the SOAP type.
     * 
     * @param soapTypeIn The SOAP type.
     * @see de.fmui.spheon.jsoap.EncodingWrapperInterface#setSoapType(
     *      java.lang.String)
     */
    public void setSoapType(final String soapTypeIn) {
        this.soapType = soapTypeIn;
    }
    
    

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
        
        Entry entry = super.getEntry(
                configIn, parent, name, value, vclass, objMap, xsdNS, xsiNS);
        
        if (value != null && value instanceof ResourceSet) {
            try {
                
                String itemName = this.soapType + "Item";
                
                ResourceSet set = (ResourceSet) value;
                for (int i = 0, s = (int) set.getSize(); i < s; i++) {
                    
                    configIn.getEncEntry(
                            entry, itemName, set.getResource(i), 
                            Resource.class, objMap, xsdNS, xsiNS);
                }
                
            } catch (Exception e) {
                throw new SoapException(e.getMessage());
            }
        }
        return entry;
    }
}
