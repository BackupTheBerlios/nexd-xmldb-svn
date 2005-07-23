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
 * $Log: Server.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 */
package de.xplib.nexd.engine.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xmldb.api.base.XMLDBException;

import de.fmui.spheon.jsoap.SoapConfig;
import de.xplib.nexd.engine.NEXDEngineImpl;
import de.xplib.nexd.engine.NEXDEnginePool;
import de.xplib.nexd.engine.config.ConfigurationException;
import de.xplib.nexd.engine.config.EngineConfig;
import de.xplib.nexd.engine.config.ServerConfig;
import de.xplib.nexd.engine.http.soap.ServicePool;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public final class Server {
    
    /**
     * @clientCardinality 1
     * @directed true
     * @label handles request
     * @supplierCardinality 0..*
     */

    /*#private ServerThread lnkServerThread*/

    /**
     * The current server version.
     */
    public static final String SERVER_VERSION = "0.1";
    
    /**
     * Log instance used for server logging.
     */
    private static final Log LOG = LogFactory.getLog(Server.class);
    
    /**
     * Comment for <code>pool</code>
     */
    private final ServicePool pool;
    
    /**
     * Default Server port.
     */
    private final int port;
    
    /**
     * The used <code>ServerSocket</code> instance. 
     */
    private final ServerSocket socket;
    
    /**
     * Global <code>SoapConfig</code> instance.
     */
    private final SoapConfig sconfig = new SoapConfig();
    
    /**
     * 
     * @exception ConfigurationException If the server or engine config is not 
     *                                   correct..
     * @throws IOException If something goes wrong with the io communication.
     * @throws XMLDBException If any database specific error occures.
     */
    private Server() throws ConfigurationException, 
                                IOException,
                                XMLDBException {
        super();
        
        try {
            sconfig.setActor("/de/xplib/nexd/comm");
            
            String nullStr = null;
            sconfig.setClassLoader(this.getClass().getClassLoader());
            sconfig.addToConfig(nullStr);
            sconfig.addToConfig(getClass().getResourceAsStream(
                    "/de/xplib/nexd/engine/http/soap/config.xml"));

            sconfig.addToConfig(getClass().getResourceAsStream(
            		"/de/xplib/nexd/engine/http/soap/server.xml"));
                    //"/de/xplib/nexd/soap/server.xml"));
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        
        EngineConfig cfg = EngineConfig.getInstance();
        ServerConfig config = cfg.getServerConfig();
                
        LOG.info("Reading server port");
        this.port = config.getPort();
        LOG.info("Server port is " + this.port);
                       
        LOG.info("Create ServerSocket instance.");
        this.socket = new ServerSocket(this.port);
        LOG.info("ServerSocket instance is ok. " 
                + this.socket.getInetAddress().getHostAddress());

        LOG.info("Fetching ServicePool instance.");
        
        NEXDEngineImpl engine = NEXDEnginePool.getInstance().getEngine();
        
        this.pool = ServicePool.getInstance(engine, config.getServiceConfig());
        LOG.info("ServicePool instance is ok.");
                
        LOG.info("Starting main server loop.");
        while (true) {
            Socket client = this.socket.accept();
            //new ServerThread(client).start();
            ServerThread.runServerThread(client, sconfig, pool);
            
            LOG.info("New connection to: " 
                    + client.getInetAddress().getHostAddress());
        }
    }
    
    

    /**
     * 
     * @param args ...
     */
    public static void main(final String[] args) {
        
        try {
            new Server();
        } catch (Exception e) {
            LogFactory.getLog(Server.class).error(e);
            System.exit(1);
        }
        
    }
}
