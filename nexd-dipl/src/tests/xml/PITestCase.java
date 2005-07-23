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
 * $Log: PITestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 */
package tests.xml;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

import de.xplib.nexd.engine.xml.dom.DOMImplementationImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class PITestCase extends TestCase {

    private Document doc;
    
    private Element elem;
    
    public void setUp() throws Exception {
        super.setUp();
        
        doc = new DOMImplementationImpl().createDocument(null, null, null);
        Node n = doc.appendChild(doc.createElement("root"));
        n.appendChild(doc.createProcessingInstruction("php", "print $var;"));
        n.appendChild(doc.createTextNode("Manuel "));
        n.appendChild(doc.createTextNode("idea "));
        n.appendChild(doc.createTextNode("of a new "));
        n.appendChild(doc.createTextNode("XML Database "));
        n.appendChild(doc.createTextNode("is great."));
        
        elem = doc.getDocumentElement();
    }

    public final void testGetNodeValue() {
        assertEquals("print $var;", elem.getFirstChild().getNodeValue());
    }

    public final void testSetNodeValue() {
        elem.getFirstChild().setNodeValue("$var = 1;");
        assertEquals("$var = 1;", elem.getFirstChild().getNodeValue());
    }

    public final void testCloneNode() {
        ProcessingInstruction pi = (ProcessingInstruction) elem.getFirstChild().cloneNode(true);
        assertEquals("php", pi.getTarget());
        assertEquals("print $var;", pi.getData());
    }

    public final void testGetTarget() {
        ProcessingInstruction pi = (ProcessingInstruction) elem.getFirstChild();
        assertEquals("php", pi.getTarget());
    }

    public final void testGetData() {
        ProcessingInstruction pi = (ProcessingInstruction) elem.getFirstChild();
        assertEquals("print $var;", pi.getData());
    }

    public final void testSetData() {
        ProcessingInstruction pi = (ProcessingInstruction) elem.getFirstChild();
        pi.setData("$var = 1;");
        assertEquals("$var = 1;", pi.getData());
    }

}
