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
 * $Log: ServerThread.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.http;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fmui.spheon.jsoap.Envelope;
import de.fmui.spheon.jsoap.SoapConfig;
import de.fmui.spheon.jsoap.SoapException;
import de.fmui.spheon.jsoap.SoapParser;
import de.xplib.nexd.engine.http.soap.Service;
import de.xplib.nexd.engine.http.soap.ServicePool;


/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
class ServerThread extends Thread {

    /**
     * @clientRole 1
     * @label output
     * @supplierRole 1
     */
    /*#de.xplib.nexd.engine.http.Response Dependency_Link*/

    /**
     * @clientRole 1
     * @label input
     * @supplierRole 1
     */
    /*#de.xplib.nexd.engine.http.Request Dependency_Link1*/

    /**
     * Log instance used for server logging.
     */
    private static final Log LOG = LogFactory.getLog(Server.class);

    /**
     * Comment for <code>WSDL_QUERY</code>
     */
    private static final String WSDL_QUERY = "?wsdl";

    /**
     * Comment for <code>HEADER_200_OK</code>
     */
    private static final String HEADER_200_OK = "HTTP/1.0 200 Ok";

    /**
     * Comment for <code>HEADER_SERVER</code>
     */
    private static final String HEADER_SERVER = "Server: Server "
            + Server.SERVER_VERSION;

    /**
     * Comment for <code>HEADER_CT_TEXT_XML</code>
     */
    private static final String HEADER_CT_TEXT_XML = "Content-Type: text/xml";

    /**
     * Comment for <code>HEADER_CT_TEXT_XML</code>
     */
    private static final String HEADER_CT_LENGTH = "Content-Length: ";

    /**
     * Comment for <code>client</code>
     */
    private final Socket client;

    /**
     * Comment for <code>sconfig</code>
     */
    private final SoapConfig sconfig;

    /**
     * Comment for <code>pool</code>
     */
    private final ServicePool pool;

    /**
     * @param clientIn The client socket.
     * @param sconfigIn the global soap config.
     * @param poolIn The serivce pool.
     */
    public static void runServerThread(final Socket clientIn,
            final SoapConfig sconfigIn, final ServicePool poolIn) {
        new ServerThread(clientIn, sconfigIn, poolIn).start();
    }

    /**
     * Constructor.
     *
     * @param clientIn The client socket.
     * @param sconfigIn the global soap config.
     * @param poolIn The serivce pool.
     */
    public ServerThread(final Socket clientIn, final SoapConfig sconfigIn,
            final ServicePool poolIn) {
        super();

        this.client = clientIn;
        this.sconfig = sconfigIn;
        this.pool = poolIn;
    }

    /**
     * <Some description here>
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {

            Request req = new Request(this.client.getInputStream());
            Response res = new Response(this.client.getOutputStream());

            if (req.getUrl().endsWith(WSDL_QUERY)) {

                String wsdl = Service.getWSDL();

                res.sendHeader(HEADER_200_OK);
                res.sendHeader(HEADER_SERVER);
                res.sendHeader(HEADER_CT_TEXT_XML);
                res.sendBody(wsdl);
                return;
            }

            Envelope env = SoapParser.read(sconfig, req.getBody());

            Service service = pool.borrowService(this.client.getInetAddress());

            Envelope renv = SoapParser.execute(sconfig, env, service);
            pool.returnService(service);

            String soap = renv.toString();

            soap = soap.replaceAll("\" =\"", "\" xmlns:xsi=\"");

            if (soap.lastIndexOf("xmlns:xsi") != soap.indexOf("xmlns:xsi")) {

                int start = soap.indexOf("xmlns:xsi");
                int quote = soap.indexOf("\"", start);
                int end = soap.indexOf("\"", quote + 1);

                soap = soap.substring(0, start) + soap.substring(end + 1);
            }

            LOG.info(soap);

            res.sendHeader(HEADER_200_OK);
            res.sendHeader(HEADER_SERVER);
            res.sendHeader(HEADER_CT_TEXT_XML);
            res.sendBody(soap);

        } catch (IOException e) {
            LOG.fatal(e.getMessage(), e);
        } catch (SoapException e) {
            LOG.error(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                LOG.fatal(e.getMessage(), e);
            }
        }
    }
}