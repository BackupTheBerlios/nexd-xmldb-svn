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
 * $Log: AllTests.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
 *
 */
package tests.xapi.vc;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for tests.xapi.vc");
        //$JUnit-BEGIN$
        suite.addTestSuite(VCLSchemaTestCase.class);
        suite.addTestSuite(PCVResourceTestCase.class);
        suite.addTestSuite(VirtualResourceTestCase.class);
        //$JUnit-END$
        return suite;
    }
}