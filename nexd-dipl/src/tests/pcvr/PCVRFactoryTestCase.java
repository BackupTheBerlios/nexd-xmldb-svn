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
 * $Log: PCVRFactoryTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.pcvr;

import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.pcvr.AbstractPCVRFactory;
import junit.framework.TestCase;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class PCVRFactoryTestCase extends TestCase {

    
    public void testNewInstanceClassNotFoundFail() {
        
        String factory = System.getProperty(AbstractPCVRFactory.FACTORY_KEY);
        
        System.setProperty(AbstractPCVRFactory.FACTORY_KEY, "de.xplib.not.exists.Factory");
        
        try {
            AbstractPCVRFactory.newInstance();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
        //restore
        System.setProperty(AbstractPCVRFactory.FACTORY_KEY, factory);
    }
    
    
    public void testNewInstanceInstantiationFail() {
        String factory = System.getProperty(AbstractPCVRFactory.FACTORY_KEY);
        System.setProperty(AbstractPCVRFactory.FACTORY_KEY, "tests.pcvr.NotImplementingFactory");
        
        try {
            AbstractPCVRFactory.newInstance();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
//      restore
        System.setProperty(AbstractPCVRFactory.FACTORY_KEY, factory);
    }
    
    public void testNewInstanceIllegalAccessFail() {
        String factory = System.getProperty(AbstractPCVRFactory.FACTORY_KEY);
        System.setProperty(AbstractPCVRFactory.FACTORY_KEY, "tests.pcvr.NonDefaultConstructorFactory");
        try {
            AbstractPCVRFactory.newInstance();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
//      restore
        System.setProperty(AbstractPCVRFactory.FACTORY_KEY, factory);
    }
}


