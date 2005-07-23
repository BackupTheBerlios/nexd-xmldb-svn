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

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XUpdateQueryService;
import org.xmldb.common.xml.queries.XUpdateQuery;
import org.xmldb.common.xml.queries.XUpdateQueryConfigurationException;
import org.xmldb.xupdate.lexus.XUpdateQueryFactoryImpl;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.xml.DOMDocumentI;
import de.xplib.nexd.xml.DocumentCacheI;

/**
 * XUpdateQueryService is a <code>Service</code> that enables the execution of
 * XUpdate queries within the context of a <code>Collection</code> or against a 
 * single document stored in a collection.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class XUpdateQueryServiceImpl 
    extends AbstractService 
    implements XUpdateQueryService {
    
    /**
     * The used <code>XUpdateQuery</code> instance.
     */
    private final XUpdateQuery xQuery;

    /**
     * Constructor.
     * 
     * @param engineIn The used <code>NEXDEngineI</code> object.
     * @param collIn The associated <code>SixdmlCollection</code> object.
     * @throws XMLDBException If any database specific error occures.
     */
    public XUpdateQueryServiceImpl(final NEXDEngineI engineIn, 
                                   final SixdmlCollection collIn) 
            throws XMLDBException {
        
        super(engineIn, collIn, "XUpdateQueryService", DEFAULT_VERSION);
        
        try {
            this.xQuery = new XUpdateQueryFactoryImpl().newXUpdateQuery();
        } catch (XUpdateQueryConfigurationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Runs a set of XUpdate operations against the collection. All selected
     * documents are to be updated and stored back to the repository.
     * 
     * @param commandsIn
     *            The XUpdate commands to use.
     * @return the number of modified nodes.
     * @exception XMLDBException
     *                with expected error codes. <br />
     *                <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *                specific errors that occur. <br />
     * @see org.xmldb.api.modules.XUpdateQueryService#update(java.lang.String)
     */
    public long update(final String commandsIn) throws XMLDBException {
        
        long count = 0;
        try {
            this.xQuery.setQString(commandsIn);
            
            String[] names = this.engine.queryResources(this.ctxColl);
            for (int i = 0; i < names.length; i++) {
                Resource res = this.engine.queryResource(
                        this.ctxColl, names[i]);
                if (res.getResourceType().equals(
                        SixdmlResource.RESOURCE_TYPE)) {
                    
                    DOMDocumentI doc = (DOMDocumentI) 
                            ((SixdmlResource) res).getContentAsDOM();

                    this.xQuery.execute(doc);
                                        
                    DocumentCacheI cache = doc.getCache();
                    count += cache.getChanged().getLength();
                    count += cache.getDeleted().getLength();
                    count += cache.getInserted().getLength();
                    
                    this.engine.updateResource(this.ctxColl, res);
                }
                
            }
        } catch (XMLDBException e) {
            throw (XMLDBException) e;
        } catch (Exception e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        return count;
    }

    /**
     * Runs a set of XUpdate operations against a resource stored in a
     * collection. The resource will be updated in place in the collection.
     * 
     * @param idIn
     *            The id of the resource to update.
     * @param commandsIn
     *            The XUpdate commands to use.
     * @return the number of modified nodes.
     * @exception XMLDBException
     *                with expected error codes. <br />
     *                <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *                specific errors that occur. <br />
     * @see org.xmldb.api.modules.XUpdateQueryService#updateResource(
     *      java.lang.String, java.lang.String)
     */
    public long updateResource(final String idIn, final String commandsIn)
            throws XMLDBException {
        long count = 0;
        
        try {
            this.xQuery.setQString(commandsIn);

            Resource res = this.engine.queryResource(this.ctxColl, idIn);
            if (res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                DOMDocumentI doc = (DOMDocumentI) ((SixdmlResource) res)
                        .getContentAsDOM();

                this.xQuery.execute(doc);

                DocumentCacheI cache = doc.getCache();
                count  = cache.getChanged().getLength(); 
                count += cache.getDeleted().getLength();
                count += cache.getInserted().getLength();

                this.engine.updateResource(this.ctxColl, res);
            }
        } catch (XMLDBException e) {
            throw (XMLDBException) e;
        } catch (Exception e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        return count;
    }

}
