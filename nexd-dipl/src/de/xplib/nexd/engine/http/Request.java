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
 * $Log: Request.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class Request {
    
    /**
     * The inpur stream.
     */
    private final InputStream iStream;
    
    /**
     * The command GET or POST
     */
    private String cmd;
    
    /**
     * The request url with query string.
     */
    private String url;
    
    /**
     * The http version 1.0 or 1.1
     */
    private String httpversion;
    
    /**
     * The request body which is the content of a post request.
     */
    private String body;

    /**
     * Constructor.
     * 
     * @param isIn The input stream.
     * @throws IOException If any error occures.
     */
    public Request(final InputStream isIn) throws IOException {
        super();

        this.iStream = isIn;
        
        this.readRequest();
    }

    /**
     * Returns the request body.
     * 
     * @return The request body.
     */
    public final String getBody() {
        return body;
    }
    
    /**
     * Returns the request command.
     * 
     * @return GET or POST 
     */
    public final String getCmd() {
        return cmd;
    }
    
    /**
     * Returns the http version.
     * 
     * @return 1.0 or 1.1
     */
    public final String getHttpversion() {
        return httpversion;
    }
    
    /**
     * Returns the request url with query string.
     * 
     * @return The request url.
     */
    public final String getUrl() {
        return url;
    }
    
    /**
     * Reads the headers of a request and if available it reads the message
     * body.
     * 
     * @throws IOException ...
     */
    private void readRequest() throws IOException {
        //Request-Zeilen lesen
        ArrayList request = new ArrayList(10);
        StringBuilder builder = new StringBuilder(100);
        int chr = iStream.read();
        while (chr != -1) {
            if (chr == '\r') {
                chr = iStream.read();
                continue;
            } else if (chr == '\n') { //line terminator
                if (builder.length() <= 0) {
                    break;
                } else {
                    request.add(builder.toString());
                    builder.setLength(0);
                }
            } else {
                builder.append((char) chr);
            }
            chr = iStream.read();
        }
        
        //Kommando, URL und HTTP-Version extrahieren
        String line = ((String) request.get(0));
    
        cmd = "";
        url = "";
        httpversion = "";
        int pos = line.indexOf(' ');
        if (pos != -1) {
            cmd = line.substring(0, pos).toUpperCase();
            line = line.substring(pos + 1);
            //URL
            pos = line.indexOf(' ');
            if (pos == -1) {
                url = line;
            } else {
                url = line.substring(0, pos);
                line = line.substring(pos + 1);
                //HTTP-Version
                pos = line.indexOf('\r');
                if (pos == -1) {
                    httpversion = line;
                } else {
                    httpversion = line.substring(0, pos);
                }
            }
        }
        
        this.readBody(request);
        
        System.out.println(body);
    }
    
    /**
     * Reads the body of a request.
     * 
     * @param request List with all request headers.
     * @throws IOException If any error occures.
     */
    private void readBody(final ArrayList request) throws IOException {
        
        String line;
   
        if (this.cmd.toUpperCase().equals("POST")) {
            
            int length  = 0;
            
            Iterator ite = request.iterator();
            while (ite.hasNext()) {
                line = ((String) ite.next()).trim();
                
                if (line.startsWith("Content-Type:")) {
                    line = line.substring(13).trim();
                } else if (line.startsWith("Content-Length:")) {
                    line = line.substring(15).trim();
                    length = Integer.parseInt(line);
                } else {
                    continue;
                }
            }
            
            byte[] data = new byte[length];
            this.iStream.read(data);
            
            this.body = new String(data);
        }
    }
}
