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
 * $Log: PCVResourceType.java,v $
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

import de.fmui.spheon.jsoap.Entry;
import de.fmui.spheon.jsoap.SoapConfig;
import de.fmui.spheon.jsoap.SoapException;
import de.fmui.spheon.jsoap.util.ObjectMap;
import de.xplib.nexd.api.pcvr.PCVResource;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class PCVResourceType extends AbstractXMLResourceType {

    
    
    /**
     * Constructor.
     */
    public PCVResourceType() {
        super(PCVResource.class, "PCVResource");
    }
    
    /**
     * Serialize the given value und creates an entry.
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
        
        
        
        if (value != null && value instanceof PCVResource) {
            PCVResource pcvr = (PCVResource) value;
            
            String[] refs = pcvr.getReferenceCollections();
            
            Entry collRefs = new Entry(entry, "CollectionReferences");
            for (int i = 0; i < refs.length; i++) { /*
                Entry colRef = new Entry(collRefs, "CollectionReference");
                colRef.setAttribute(xsiNS + ":type", xsdNS + ":string");
                colRef.setValue(refs[i]); */
                this.addEntry(
                        collRefs, "CollectionReference", refs[i], xsiNS, xsdNS);
            }
            
            refs = pcvr.getReferenceResources();
            
            Entry resRefs = new Entry(entry, "ResourceReferences");
            for (int i = 0; i < refs.length; i++) { /*
                Entry resRef = new Entry(resRefs, "ResourceReference");
                resRef.setAttribute(xsiNS + ":type", xsdNS + ":string");
                resRef.setValue(refs[i]);*/
                this.addEntry(
                        resRefs, "ResourceReference", refs[i], xsiNS, xsdNS);
            }
        }        
        return entry;
    }
    
    /**
     * Adds an entry to the given parent.
     * 
     * @param parentIn The parent entry.
     * @param nameIn The name of the entry.
     * @param valueIn The value of the entry.
     * @param xsiNS The ...
     * @param xsdNS The ...
     */
    private void addEntry(final Entry parentIn, 
                          final String nameIn,
                          final String valueIn,
                          final String xsiNS,
                          final String xsdNS) {
        

        Entry resRef = new Entry(parentIn, nameIn);
        resRef.setAttribute(xsiNS + ":type", xsdNS + ":string");
        resRef.setValue(valueIn);
    }
}
