/*
 * Project: nexd 
 * Copyright (C) 2004  Manuel Pichler <manuel.pichler@xplib.de>
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
 * $Log: LocalSession.java,v $
 * Revision 1.2  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.4  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.comm;

import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.ConnectionException;
import de.xplib.nexd.comm.MaxConnectionException;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.comm.SessionConfigurationException;
import de.xplib.nexd.comm.SessionI;
import de.xplib.nexd.engine.NEXDEnginePool;
import de.xplib.nexd.engine.config.ConfigurationException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class LocalSession implements SessionI {
    
    /**
     * <Some description here>
     * 
     * @return An instance of the underlying <code>NEXDEngineI</code> instance. 
     *         In this case it is a concrete instance of a 
     *         <code>NEXDEngineI</code> implementation.
     * @exception ConnectionException If something goes wrong during the 
     *            connection process.
     * @exception MaxConnectionException If the maximum number of clients is
     *            connected to the database.
     * @exception SessionConfigurationException If the <code>SessionI</code>
     *            itself or the used <code>NEXDEngineI</code> is bad configured.
     * @see de.xplib.nexd.comm.SessionI#getEngine()
     */
    public NEXDEngineI getEngine()
            throws ConnectionException, 
                   MaxConnectionException, 
                   SessionConfigurationException {

        try {
            return NEXDEnginePool.getInstance().getEngine();
        } catch (ConfigurationException e) {
            throw new SessionConfigurationException(e.getMessage());
        } catch (XMLDBException e) {
            if (e.vendorErrorCode == 1) {
                throw new MaxConnectionException(e.getMessage());
            }
            throw new ConnectionException(e.getMessage());
        }
    }

}
