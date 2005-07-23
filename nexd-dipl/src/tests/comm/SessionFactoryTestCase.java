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
 * $Log: SessionFactoryTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.comm;

import de.xplib.nexd.comm.AbstractSessionFactory;
import de.xplib.nexd.comm.SessionConfigurationException;
import junit.framework.TestCase;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class SessionFactoryTestCase extends TestCase {

    
    public void testNewInstanceClassNotFoundFail() throws Exception {
        
        System.setProperty("de.xplib.nexd.comm.AbstractSessionFactory", "WrongSessionFactory");
        
        try {
            AbstractSessionFactory.newInstance();
            assertTrue(false);
        } catch (SessionConfigurationException e) {
            assertTrue(true);
        }
        System.setProperty("de.xplib.nexd.comm.AbstractSessionFactory", "de.xplib.nexd.engine.comm.DefaultSessionFactory");
    }
    
    public void testNewInstanceCastFail() throws Exception  {
        
        System.setProperty("de.xplib.nexd.comm.AbstractSessionFactory", "tests.comm.NoFactoryImpl");
        
        try {
            AbstractSessionFactory.newInstance();
            assertTrue(false);
        } catch (SessionConfigurationException e) {
            assertTrue(true);
        }
        System.setProperty("de.xplib.nexd.comm.AbstractSessionFactory", "de.xplib.nexd.engine.comm.DefaultSessionFactory");
    }
    
    public void testNewInstanceInstantiationFail() throws Exception {
        System.setProperty("de.xplib.nexd.comm.AbstractSessionFactory", "tests.comm.NoConcreteFactoryClass");
        
        try {
            AbstractSessionFactory.newInstance();
            assertTrue(false);
        } catch (SessionConfigurationException e) {
            assertTrue(true);
        }
        System.setProperty("de.xplib.nexd.comm.AbstractSessionFactory", "de.xplib.nexd.engine.comm.DefaultSessionFactory");
    }
}
