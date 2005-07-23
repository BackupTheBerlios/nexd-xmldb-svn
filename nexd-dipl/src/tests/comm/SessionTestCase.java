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
 * $Log: SessionTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.comm;

import java.io.File;

import junit.framework.TestCase;
import de.xplib.nexd.comm.AbstractSessionFactory;
import de.xplib.nexd.comm.MaxConnectionException;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.comm.SessionConfigurationException;
import de.xplib.nexd.comm.SessionI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class SessionTestCase extends TestCase {
    
    SessionI sess;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        sess = AbstractSessionFactory.newInstance().newSession();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    

    public final void testConfigException() throws Exception {
        
        File tmp = new File("/home/manuel/.nexd/nexd-engine.xml.bak");
        
        File f = new File("/home/manuel/.nexd/nexd-engine.xml");
        try {
        f.renameTo(tmp);
        
        NEXDEngineI engine = null;
        
        try {
            engine = sess.getEngine();
            //assertTrue(false);
        } catch (SessionConfigurationException e) {
            assertTrue(true);
        } finally {
            if (engine != null) {
                engine.close();
            }
        }
        
        } finally {
            tmp.renameTo(f);
        }
        
    }
    
    public final void testMaxConnection() throws Exception {
        int size = 30;
        
        NEXDEngineI[] engines = new NEXDEngineI[size];
        try {
            for (int i = 0; i < size; i++) {
                engines[i] = sess.getEngine();
                engines[i].open("sa", "");
            }
            assertTrue(false);
        } catch (MaxConnectionException e) {
            assertTrue(true);
        } finally {
            for (int i = 0; i < size; i++) {
                if (engines[i] instanceof NEXDEngineI) {
                    engines[i].close();
                }
            }
        }
    }
    
    

}
