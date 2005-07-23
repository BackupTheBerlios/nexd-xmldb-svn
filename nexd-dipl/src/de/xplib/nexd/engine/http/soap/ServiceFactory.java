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
 * $Log: ServiceFactory.java,v $
 * Revision 1.2  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:42  nexd
 * SOAP support and performance update.
 *
 */
package de.xplib.nexd.engine.http.soap;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.pool.PoolableObjectFactory;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.config.ServiceConfig;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class ServiceFactory implements PoolableObjectFactory {
    
    /**
     * @clientCardinality 1
     * @clientRole product
     * @directed true
     * @label creates instances
     * @link aggregation
     * @supplierCardinality 0..*
     */

    /*#private Service lnkService;*/

    /**
     * Comment for <code>engine</code>
     */
    private final NEXDEngineI engine;
    
    /**
     * The local address of the Server.
     */
    private final InetAddress address;
    
    /**
     * Are remote queries allowed?
     */
    private final boolean allowRemote;
    
    /**
     * Are remote changes allowed?
     */
    private final boolean allowRemoteChanges;
    
    /**
     * Constructor.
     * 
     * @param engineIn The used NEXD Engine.
     * @param configIn The service configuration.
     * @exception XMLDBException If something goes wrong during connection
     *                           phase.
     */
    public ServiceFactory(final NEXDEngineI engineIn, 
                          final ServiceConfig configIn) 
            throws XMLDBException {
        super();
        
        this.engine = engineIn;
        
        this.allowRemote        = configIn.isAllowRemote();
        this.allowRemoteChanges = configIn.isAllowRemoteChanges();
        
        this.engine.open("sa", "");
        
        try {
            this.address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new XMLDBException(
                    ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws java.lang.Exception
     * @see org.apache.commons.pool.PoolableObjectFactory#makeObject()
     */
    public Object makeObject() {
        return new Service(this.engine, 
                               this.address,
                               this.allowRemote, 
                               this.allowRemoteChanges);
    }

    /**
     * <Some description here>
     * 
     * @param obj
     * @throws java.lang.Exception
     * @see org.apache.commons.pool.PoolableObjectFactory#destroyObject(
     *      java.lang.Object)
     */
    public void destroyObject(final Object obj) {

    }

    /**
     * <Some description here>
     * 
     * @param obj
     * @return
     * @see org.apache.commons.pool.PoolableObjectFactory#validateObject(
     *      java.lang.Object)
     */
    public boolean validateObject(final Object obj) {
        return false;
    }

    /**
     * <Some description here>
     * 
     * @param obj
     * @throws java.lang.Exception
     * @see org.apache.commons.pool.PoolableObjectFactory#activateObject(
     *      java.lang.Object)
     */
    public void activateObject(final Object obj) {
    }

    /**
     * <Some description here>
     * 
     * @param obj
     * @throws java.lang.Exception
     * @see org.apache.commons.pool.PoolableObjectFactory#passivateObject(
     *      java.lang.Object)
     */
    public void passivateObject(final Object obj) {

    }

}
