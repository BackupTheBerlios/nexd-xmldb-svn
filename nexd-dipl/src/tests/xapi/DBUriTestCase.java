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
 * $Log: DBUriTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xapi;

import de.xplib.nexd.engine.xapi.DBUri;
import junit.framework.TestCase;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class DBUriTestCase extends TestCase {


    public final void testGetHost() {
        DBUri uri = DBUri.parseUri("nexd://localhost./db/foo");
        assertNotNull(uri.getHost());
        assertEquals("localhost.", uri.getHost());
        
        uri = DBUri.parseUri("nexd://198.162.0.1:8888/db/foo");
        assertNotNull(uri.getHost());
        assertEquals("198.162.0.1", uri.getHost());
    }

    public final void testGetPath() {
        DBUri uri = DBUri.parseUri("nexd://localhost./db/foo");
        assertNotNull(uri.getPath());
        assertEquals("/db/foo", uri.getPath());
        
        uri = DBUri.parseUri("nexd://198.162.0.1:8888/db/foo");
        assertNotNull(uri.getPath());
        assertEquals("/db/foo", uri.getPath());
    }

    public final void testGetPort() {
        DBUri uri = DBUri.parseUri("nexd://localhost./db/foo");
        assertNull(uri.getPort());
        
        uri = DBUri.parseUri("nexd://198.162.0.1:8888/db/foo");
        assertNotNull(uri.getPort());
        assertEquals("8888", uri.getPort());
    }

    public final void testParseUri() {
        DBUri.parseUri("nexd://localhost./db/foo");
        
        DBUri.parseUri("nexd://198.162.0.1:8888/db/foo");
    }

}
