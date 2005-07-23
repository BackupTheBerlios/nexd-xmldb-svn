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
 * $Log: EnginePoolTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.engine;

import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.NEXDEnginePool;
import de.xplib.nexd.engine.config.EngineConfig;
import junit.framework.TestCase;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class EnginePoolTestCase extends TestCase {

    
    private int conns;
    
    private NEXDEnginePool pool;
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        conns = EngineConfig.getInstance().getMaxConnections();
        pool  = NEXDEnginePool.getInstance();
    }
    
    public final void testGetEngineSuccess() throws Exception {
        
        NEXDEngineI[] engines = new NEXDEngineI[conns];
        
        for (int i = 0; i < engines.length - 2; i++) {
            engines[i] = pool.getEngine();
        }
        
        for (int i = 0; i < engines.length - 2; i++) {
            engines[i].close();
        }
        
        for (int i = 0; i < engines.length - 2; i++) {
            engines[i] = pool.getEngine();
        }

        for (int i = 0; i < engines.length - 2; i++) {
            engines[i].close();
        }
    }
    
    
    public final void testGetEngineMaxConnFail() throws Exception {
        
        NEXDEngineI[] engines = new NEXDEngineI[10];
                
        try {
            engines[0] = pool.getEngine();
            engines[1] = pool.getEngine();
            engines[2] = pool.getEngine();
            engines[3] = pool.getEngine();
            engines[4] = pool.getEngine();
            engines[5] = pool.getEngine();
            engines[6] = pool.getEngine();
            engines[7] = pool.getEngine();
            engines[8] = pool.getEngine();
            engines[9] = pool.getEngine();
            for (int i = 0; i < engines.length; i++) {
                engines[i].close();
            }
            assertTrue(false);
        } catch (XMLDBException e) {
            assertEquals(ErrorCodes.VENDOR_ERROR, e.errorCode);
            assertEquals(1, e.vendorErrorCode);
        }
        
        engines[0].close();
        
        engines[0] = pool.getEngine();

        for (int i = 0; i < engines.length; i++) {
            if (engines[i] instanceof NEXDEngineI)
                engines[i].close();
        }
    }
}
