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
 * $Log: ServerConfig.java,v $
 * Revision 1.1  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 */
package de.xplib.nexd.engine.config;


/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ServerConfig {
    
    /**
     * The Server port.
     */
    private final int port;
    
    /**
     * The SOAP service configuration.
     */
    private final ServiceConfig serviceCfg;

    /**
     * Constructor.
     * 
     * @param maxConnIn The maximum number of open connections.
     * @param portIn The server port.
     * @param remoteIn Are remote operations allowed?
     * @param remoteChangesIn Are remote change operations allowed?
     */
    public ServerConfig(final int maxConnIn,
                        final int portIn,
                        final boolean remoteIn,
                        final boolean remoteChangesIn) {
        super();
        
        this.port       = portIn;
        this.serviceCfg = new ServiceConfig(
                maxConnIn, remoteIn, remoteChangesIn);
    }
    
    /**
     * Returns the server port.
     * 
     * @return The server port.
     */
    public final int getPort() {
        return port;
    }
    
    /**
     * Returns the SOAP service configuration.
     * 
     * @return The service configuration.
     */
    public final ServiceConfig getServiceConfig() {
        return serviceCfg;
    }
}
