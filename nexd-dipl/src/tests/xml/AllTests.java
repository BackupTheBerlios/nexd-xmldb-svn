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
package tests.xml;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for tests.xml");
        //$JUnit-BEGIN$
        suite.addTestSuite(StreamTokenizerTester.class);
        suite.addTestSuite(DOMImplementationTestCase.class);
        suite.addTestSuite(CDATASectionTestCase.class);
        suite.addTestSuite(NodeTestCase.class);
        suite.addTestSuite(DocumentBuilderTestCase.class);
        suite.addTestSuite(DocumentTestCase.class);
        suite.addTestSuite(CharacterDataTestCase.class);
        suite.addTestSuite(TextTestCase.class);
        suite.addTestSuite(DOMCommentTestCase.class);
        suite.addTestSuite(DOMBaseTestCase.class);
        suite.addTestSuite(ElementTestCase.class);
        suite.addTestSuite(PITestCase.class);
        suite.addTestSuite(XalanTestCase.class);
        suite.addTestSuite(NewXPathTestCase.class);
        suite.addTestSuite(AttrTestCase.class);
        suite.addTestSuite(XPathAPITestCase.class);
        suite.addTestSuite(DocumentTypeTestCase.class);
        //$JUnit-END$
        return suite;
    }
}