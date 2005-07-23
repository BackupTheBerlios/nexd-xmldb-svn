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
 * $Log: XPathQueryServiceImpl.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.7  2005/04/24 15:00:27  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.6  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.5  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.4  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi.services;

import org.apache.commons.collections.FastHashMap;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.exceptions.InvalidQueryException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import de.xplib.nexd.comm.NEXDEngineI;

/**
 * XPathQueryService is a <code>Service</code> that enables the execution of
 * XPath queries within the context of a <code>Collection</code> or against a 
 * single XML <code>Resource</code> stored in the <code>Collection</code>.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class XPathQueryServiceImpl 
    extends AbstractService 
    implements XPathQueryService {
    
    /**
     * <p>HashMap holding namespaces and their prefixes.</p>
     */
    private final FastHashMap nsMapping = new FastHashMap();

    /**
     * Constructor.
     * 
     * @param engineIn The used <code>NEXDEngineI</code> object.
     * @param collIn The associated <code>SixdmlCollection</code> object.
     */
    public XPathQueryServiceImpl(final NEXDEngineI engineIn, 
                                 final SixdmlCollection collIn) {
        
        super(engineIn, collIn, "XPathQueryService", DEFAULT_VERSION);
        
        this.nsMapping.setFast(true);
    }

    /**
     * Sets a namespace mapping in the internal namespace map used to evaluate
     * queries. If <code>prefix</code> is null or empty the default namespace is
     * associated with the provided URI. A null or empty <code>uri</code> 
     * results in an exception being thrown.
     *
     * @param prefix The prefix to set in the map. If 
     *  <code>prefix</code> is empty or null the
     *  default namespace will be associated with the provided URI.
     * @param uri The URI for the namespace to be associated with prefix.
     * @exception XMLDBException with expected error codes.<br />
     *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *  specific errors that occur.<br />
     *  TODO : probably need some special error here.
     * @see org.xmldb.api.modules.XPathQueryService#setNamespace(
     *      java.lang.String, java.lang.String)
     */
    public void setNamespace(final String prefix, final String uri) 
            throws XMLDBException {

        this.nsMapping.put(prefix, uri);
    }

    /**   
     * Returns the URI string associated with <code>prefix</code> from
     * the internal namespace map. If <code>prefix</code> is null or empty the
     * URI for the default namespace will be returned. If a mapping for the 
     * <code>prefix</code> can not be found null is returned.
     *
     * @param prefix The prefix to retrieve from the namespace map. 
     * @return The URI associated with <code>prefix</code>
     * @exception XMLDBException with expected error codes.<br />
     *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *  specific errors that occur.<br />
     * @see org.xmldb.api.modules.XPathQueryService#getNamespace(
     *      java.lang.String)
     */
    public String getNamespace(final String prefix) throws XMLDBException {
        return (String) this.nsMapping.get(prefix);
    }

    /**
     * Removes the namespace mapping associated with <code>prefix</code> from
     * the internal namespace map. If <code>prefix</code> is null or empty the
     * mapping for the default namespace will be removed.
     *
     * @param prefix The prefix to remove from the namespace map. If 
     *  <code>prefix</code> is null or empty the mapping for the default
     *  namespace will be removed.
     * @exception XMLDBException with expected error codes.<br />
     *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *  specific errors that occur.<br />
     * @see org.xmldb.api.modules.XPathQueryService#removeNamespace(
     *      java.lang.String)
     */
    public void removeNamespace(final String prefix) throws XMLDBException {
        this.nsMapping.remove(prefix);
    }

    /**
     * Removes all namespace mappings stored in the internal namespace map.
     *
     * @exception XMLDBException with expected error codes.<br />
     *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *  specific errors that occur.<br />
     * @see org.xmldb.api.modules.XPathQueryService#clearNamespaces()
     */
    public void clearNamespaces() throws XMLDBException {
        this.nsMapping.clear();
    }

    /**
     * Run an XPath query against the <code>Collection</code>. The XPath will be
     * applied to all XML resources stored in the <code>Collection</code>. 
     * The result is a 
     * <code>ResourceSet</code> containing the results of the query. Any
     * namespaces used in the <code>query</code> string will be evaluated using
     * the mappings setup using <code>setNamespace</code>.
     *
     * @param query The XPath query string to use.
     * @return A <code>ResourceSet</code> containing the results of the query.
     * @exception XMLDBException with expected error codes.<br />
     *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *  specific errors that occur.<br />
     * @see org.xmldb.api.modules.XPathQueryService#query(java.lang.String)
     */
    public ResourceSet query(final String query) throws XMLDBException {
        try {
            return this.engine.queryResourcesByXPath(this.ctxColl, query);
        } catch (InvalidQueryException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Run an XPath query against an XML resource stored in the 
     * <code>Collection</code> associated with this service. The result is a 
     * <code>ResourceSet</code> containing the results of the query. Any
     * namespaces used in the <code>query</code> string will be evaluated using
     * the mappings setup using <code>setNamespace</code>.
     *
     * @param query The XPath query string to use.
     * @param idIn The id of the document to run the query against.
     * @return A <code>ResourceSet</code> containing the results of the query.
     * @exception XMLDBException with expected error codes.<br />
     *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *  specific errors that occur.<br />
     * @see org.xmldb.api.modules.XPathQueryService#queryResource(
     *      java.lang.String, java.lang.String)
     */
    public ResourceSet queryResource(final String idIn, final String query)
            throws XMLDBException {
        
        try {
            return this.engine.queryResourceByXPath(this.ctxColl, idIn, query);
        } catch (InvalidQueryException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

}
