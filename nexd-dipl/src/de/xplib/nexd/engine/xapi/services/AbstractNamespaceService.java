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
package de.xplib.nexd.engine.xapi.services;

import org.sixdml.SixdmlNamespaceMap;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.NEXDEngineI;

/**
 * AbstractNamespaceService.java
 *
 * This is a Service that enables the execution of updates within the context of
 * a Collection or against the documents stored in the Collection.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractNamespaceService extends AbstractService {
    
    /**
     * The used namespace map. This feature is currently not used.
     */
    private SixdmlNamespaceMap nsMap = new SixdmlNamespaceMap();

    /**
     * Constructor.
     * 
     * @param engineIn The associated <code>NEXDEngineI</code> instance.
     * @param ctxCollIn The context <code>SixdmlCollection</code> instance. 
     * @param nameIn The name of the <code>Service</code>.
     * @param versionIn The support version of the <code>Service</code>
     */
    public AbstractNamespaceService(final NEXDEngineI engineIn,
                                    final SixdmlCollection ctxCollIn,
                                    final String nameIn, 
                                    final String versionIn) {
        
        super(engineIn, ctxCollIn, nameIn, versionIn);
    }
    
    
    /**
     * Set the SixdmlNamespaceMap used by this class to map prefixes to 
     * namespace URIs used in XPath queries.
     *  
     * @param nsMapIn the new namespace map to use. 
     * @exception XMLDBException if anything goes wrong.
     * @see org.sixdml.update.SixdmlUpdateService#setNamespaceMap(
     *      org.sixdml.SixdmlNamespaceMap)
     */
    public final void setNamespaceMap(final SixdmlNamespaceMap nsMapIn) 
            throws XMLDBException {
        
        this.nsMap = nsMapIn;
    }

    /**
     * Add a mapping between a given prefix and a namespace to the service's 
     * internal namespace map.
     *  
     * @param prefixIn key with which the specified namespace URI is associated.
     * @param namespaceUriIn value to be associated with the specified prefix. 
     * @exception XMLDBException if anything goes wrong. 
     * @see org.sixdml.update.SixdmlUpdateService#addNamespaceMapping(
     *      java.lang.String, java.lang.String)
     */
    public final void addNamespaceMapping(final String prefixIn, 
                                          final String namespaceUriIn)
            throws XMLDBException {
        
        this.nsMap.put(prefixIn, namespaceUriIn);
    }

    /**
     * Remove a mapping between a given prefix and a namespace to the service's
     * internal namespace map.
     *  
     * @param prefixIn key with which the specified namespace URI is associated.
     * @exception XMLDBException if anything goes wrong. 
     * @see org.sixdml.update.SixdmlUpdateService#removeNamespaceMapping(
     *      java.lang.String)
     */
    public final void removeNamespaceMapping(final String prefixIn) 
            throws XMLDBException {
        
        this.nsMap.remove(prefixIn);
    }

    /**
     * Clear all namespace mappings in the service's internal namespace map.
     *  
     * @exception XMLDBException if anything goes wrong. 
     * @see org.sixdml.update.SixdmlUpdateService#clearNamespaceMappings()
     */
    public final void clearNamespaceMappings() throws XMLDBException {
        this.nsMap.clear();
    }

    /**
     * Obtain the namespace URI mapped to a particular prefix.
     *  
     * @param prefixIn the prefix whose namespace is being sought. 
     * @return the namespace URI mapped to the prefix or <code>null</code> if 
     *         the map contains no mapping for this prefix.
     * @exception XMLDBException if anything goes wrong.
     * @see org.sixdml.update.SixdmlUpdateService#getNamespaceMapping(
     *      java.lang.String)
     */
    public final String getNamespaceMapping(final String prefixIn) 
            throws XMLDBException {

        return this.nsMap.get(prefixIn);
    }

}
