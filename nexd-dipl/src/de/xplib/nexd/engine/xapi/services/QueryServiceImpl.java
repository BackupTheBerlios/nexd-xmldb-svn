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
import org.sixdml.exceptions.InvalidQueryException;
import org.sixdml.query.SixdmlQueryResultsMap;
import org.sixdml.query.SixdmlQueryService;
import org.sixdml.query.SixdmlXpathObject;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.xapi.XPathObjectImpl;

/**
 * QueryService.java
 * 
 * This is a Service that enables the execution of XPath queries within the 
 * context of a Collection or against the documents stored in the Collection.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 * @see de.xplib.nexd.engine.xapi.services.AbstractNamespaceService
 * @see org.sixdml.query.SixdmlQueryService
 */
public class QueryServiceImpl 
    extends AbstractNamespaceService 
    implements SixdmlQueryService {
    
    /**
     * Constructor.
     * 
     * @param engineIn The associated <code>NEXDEngineI</code> instance.
     */
    public QueryServiceImpl(final NEXDEngineI engineIn) {
        this(engineIn, null);
    }

    /**
     * Constructor.
     * 
     * @param engineIn The associated <code>NEXDEngineI</code> instance.
     * @param ctxCollIn The context <code>SixdmlCollection</code> instance.
     */
    public QueryServiceImpl(final NEXDEngineI engineIn, 
                            final SixdmlCollection ctxCollIn) {
        
        super(engineIn, ctxCollIn, "SixdmlQueryService", DEFAULT_VERSION);
    }

    /**
     * Executes an XPath query against the specified collection. 
     * 
     * @param queryIn The XPath query. 
     * @param collIn The collection to execute the query against. 
     * @return The results of the query as a pairing of 
     *         <code>SixdmlResources</code> and <code>SixdmlXpathObjects</code>.
     * @see org.sixdml.query.SixdmlXpathObject
     * @exception InvalidQueryException If the query is not valid XPath. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.query.SixdmlQueryService#executeQuery(
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlCollection)
     */
    public SixdmlQueryResultsMap executeQuery(final String queryIn,
                                              final SixdmlCollection collIn) 
            throws InvalidQueryException, XMLDBException {
        
        SixdmlQueryResultsMap map = new SixdmlQueryResultsMap(
                collIn.getName(), queryIn);
        
        String[] names = this.engine.queryResources(collIn);
        for (int i = 0; i < names.length; i++) {
            Resource res = this.engine.queryResource(collIn, names[i]);
            if (!res.getResourceType().equals(SixdmlResource.RESOURCE_TYPE)) {
                continue;
            }
            SixdmlXpathObject xobj = this.executeQuery(
                    queryIn, (SixdmlResource) res);
            
            String result;
            if (xobj.getType() == SixdmlXpathObject.NODESET) {
                result = xobj.getNodeSetAsXML(); 
            } else {
                result = xobj.getObjectAsString();
            }
			if (result != null) {
			    map.put(names[i], result);
			}
        }
        return map;
    }

    /**
     * Executes an XPath query against the specified collection only returning 
     * results from documents that satisfy the given predicate.
     *   
     * @param queryIn The XPath query. 
     * @param collIn The collection to execute the query against. 
     * @param predicateIn A XPath query used to filter which documents the main 
     *                    query is run against. 
     * @return The results of the query as a pairing of 
     *         <code>SixdmlResources</code> and <code>SixdmlXpathObjects</code>.
     * @see org.sixdml.query.SixdmlQueryResultsMap
     * @exception InvalidQueryException If the query is not valid XPath. 
     * @exception XMLDBException If a database error occurs
     * @see org.sixdml.query.SixdmlQueryService#executeQuery(
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlCollection, 
     *      java.lang.String)
     */
    public SixdmlQueryResultsMap executeQuery(final String queryIn,
                                              final SixdmlCollection collIn, 
                                              final String predicateIn)
            throws InvalidQueryException, XMLDBException {
        
        return this.executeQuery(queryIn, collIn);
    }

    /**
     * Executes an XPath query against the specified collection. 
     * 
     * @param queryIn The XPath query. 
     * @param resIn The resource to execute the query against. 
     * @return The results of the query as a <code>SixdmlXpathObject</code>.
     * @see org.sixdml.query.SixdmlXpathObject
     * @exception InvalidQueryException if the query is not valid XPath. 
     * @exception XMLDBException if a database error occurs 
     * @see org.sixdml.query.SixdmlQueryService#executeQuery(
     *      java.lang.String, org.sixdml.dbmanagement.SixdmlResource)
     */
    public SixdmlXpathObject executeQuery(final String queryIn, 
                                          final SixdmlResource resIn)
            throws InvalidQueryException, XMLDBException {
                
        return new XPathObjectImpl(this.engine.queryResourceByXPath(
                (SixdmlCollection) resIn.getParentCollection(), 
                resIn.getId(), queryIn));
    }

}
