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
 * $Log: ServicePool.java,v $
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
import java.util.NoSuchElementException;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.config.ServiceConfig;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public final class ServicePool {

    /**
     * @label used for creation
     */
    /*#de.xplib.nexd.engine.http.soap.ServiceFactory Dependency_Link*/

    /**
     * @label returns instances
     */

    /*#de.xplib.nexd.engine.http.soap.Service Dependency_Link1*/

    /**
     * Holds singleton instance
     * @label singleton
     */
    private static ServicePool instance;

    /**
     * The object pool that holds the allowed number of service instances.
     * @associates <{de.xplib.nexd.engine.http.soap.ServiceFactory}>
     * @clientCardinality 1
     * @clientRole serviceObjPool
     * @directed true
     * @supplierCardinality 1
     */
    private final GenericObjectPool serviceObjPool;

    /**
     * prevents instantiation
     *
     * @param engineIn The used NEXD Engine instance.
     * @param configIn The service configuration.
     * @exception XMLDBException If something goes wrong during connection
     *                           phase.
     */
    private ServicePool(final NEXDEngineI engineIn, final ServiceConfig configIn)
            throws XMLDBException {
        super();

        this.serviceObjPool = new GenericObjectPool(new ServiceFactory(
                engineIn, configIn), configIn.getMaxConnection());
    }

    /**
     * Returns the singleton instance.
     *
     * @return The singleton instance.
     * @param engineIn The used NEXD Engine instance.
     * @param configIn The service configuration.
     * @exception XMLDBException If something goes wrong during connection
     *                           phase.
     */
    public static ServicePool getInstance(final NEXDEngineI engineIn,
            final ServiceConfig configIn) throws XMLDBException {

        if (instance == null) {
            instance = new ServicePool(engineIn, configIn);
        }
        return instance;
    }

    /**
     * Returns the singleton instance.
     *
     * @return The singleton instance.
     */
    public static ServicePool getInstance() {
        return instance;
    }

    /**
     * @param clientAddressIn The address of the remote client.
     * @return A new service instance.
     * @exception XMLDBException If something goes wrong during connection
     *                           phase.
     */
    public Service borrowService(final InetAddress clientAddressIn)
            throws XMLDBException {

        try {
            Service service = (Service) this.serviceObjPool.borrowObject();
            service.setClientAddress(clientAddressIn);

            return service;
        } catch (NoSuchElementException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, 1,
                    "The maximum number of clients ["
                            + this.serviceObjPool.getMaxActive()
                            + "] is connected to the database");
        } catch (Exception e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR,
                    "An unknown connection exception occured.");
        }
    }

    /**
     * Returns a <code>Service</code> to the pool.
     *
     * @param serviceIn The <code>Service</code> to return.
     */
    public void returnService(final Service serviceIn) {
        try {
            serviceIn.setClientAddress(null);
            this.serviceObjPool.returnObject(serviceIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}