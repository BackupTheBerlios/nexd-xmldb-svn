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
 * $Log: Response.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.http;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class Response {
    
    /**
     * Comment for <code>pStream</code>
     */
    private final PrintStream pStream;

    /**
     * Constructor.
     * 
     * @param outIn The output stream for this response.
     */
    public Response(final OutputStream outIn) {
        super();
        
        this.pStream = new PrintStream(outIn);
    }
    
    /**
     * Sends a single header.
     * 
     * @param headerIn The header.
     */
    public void sendHeader(final String headerIn) {
        this.pStream.print(headerIn + "\r\n");
    }
    
    /**
     * Sends the body of a response.
     * 
     * @param bodyIn The body of a response.
     */
    public void sendBody(final String bodyIn) {
        this.pStream.print("Content-Length: " + bodyIn.length());
        this.pStream.print("\r\n\r\n" + bodyIn);
        this.pStream.flush();
    }

}
