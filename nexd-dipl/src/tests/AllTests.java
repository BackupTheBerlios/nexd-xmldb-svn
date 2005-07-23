/*
 * Project: nexd 
 * Copyright (C) 2004  Manuel Pichler <manuel.pichler@xplib.de>
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
package tests;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import tests.xapi.XAPIAllTests;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for tests");
        //$JUnit-BEGIN$
        suite.addTest(XAPIAllTests.suite());
        suite.addTest(tests.vcl.AllTests.suite());
        suite.addTest(tests.pcvr.AllTests.suite());
        suite.addTest(tests.comm.AllTests.suite());
        suite.addTest(tests.engine.AllTests.suite());
        suite.addTest(tests.xml.AllTests.suite());
        suite.addTest(tests.storage.AllTests.suite());
        suite.addTestSuite(IntStackTestCase.class);
        //$JUnit-END$
        return suite;
    }
    
    public static void main(String[] args) {
        Test t = suite();
        t.run(new TestResult());
    }
}
