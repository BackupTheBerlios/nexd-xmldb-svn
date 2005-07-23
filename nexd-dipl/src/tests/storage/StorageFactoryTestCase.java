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
 * $Log: StorageFactoryTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.storage;

import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.xerces.dom.DOMImplementationImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.xplib.nexd.engine.store.StorageFactoryImpl;
import de.xplib.nexd.engine.xml.dom.DocumentImpl;
import de.xplib.nexd.store.AbstractStorageFactory;
import de.xplib.nexd.store.StorageDocumentObjectI;
import de.xplib.nexd.store.StorageException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class StorageFactoryTestCase extends TestCase {
    
    protected void setUp() throws Exception {
        super.setUp();
        
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
        "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");
    }
    
    public final void testNewInstanceNoFail() throws Exception {
        String clazz = System.getProperty(AbstractStorageFactory.STORAGE_FACTORY_KEY);
        System.getProperties().remove(AbstractStorageFactory.STORAGE_FACTORY_KEY);
                
        try {
            AbstractStorageFactory factory = AbstractStorageFactory.newInstance();
            assertTrue(false);
        } catch (StorageException e) {
            assertTrue(true);
        }
        if (clazz != null)
            System.setProperty(AbstractStorageFactory.STORAGE_FACTORY_KEY, clazz);
    }

    /*
     * Class under test for StorageValidationObjectI createValidationObject(String)
     */
    public final void testCreateValidationObjectString() {
        //TODO Implement createValidationObject().
    }

    /*
     * Class under test for StorageValidationObjectI createValidationObject(byte, String)
     */
    public final void testCreateValidationObjectbyteString() {
        //TODO Implement createValidationObject().
    }

    /*
     * Class under test for StorageDocumentObjectI createDocumentObject(InternalIdI, String, Document)
     */
    public final void testCreateDocumentObjectInternalIdIStringDocument() {
        //TODO Implement createDocumentObject().
    }

    /*
     * Class under test for StorageDocumentObjectI createDocumentObject(String, Document)
     */
    public final void testCreateDocumentObjectStringDocument() throws Exception {
        
        System.setProperty(AbstractStorageFactory.STORAGE_FACTORY_KEY, "de.xplib.nexd.engine.store.StorageFactoryImpl");
        
        Document doc = new DOMImplementationImpl().createDocument(null, null, null);
        Node root = doc.appendChild(doc.createElement("root"));
        root.appendChild(doc.createElement("child"));
        root.appendChild(doc.createCDATASection("CDATA"));
        root.appendChild(doc.createComment("Comment"));
        root.appendChild(doc.createEntityReference("EntityRef"));
        root.appendChild(doc.createProcessingInstruction("target", "data"));
        
        AbstractStorageFactory factory = AbstractStorageFactory.newInstance();
        StorageDocumentObjectI sdObj = factory.createDocumentObject("myoid", doc);
        
        assertTrue(sdObj.getContent() instanceof DocumentImpl);
    }

    public final void testGetUniqueStorageNoDriverFail() throws Exception {
        AbstractStorageFactory factory = new StorageFactoryImpl();
        try {
            factory.getUniqueStorage(new HashMap());
            assertTrue(false);
        } catch (StorageException e) {
        }
    }
    
    public final void testGetUniqueStorageNoJDBCUriFail() throws Exception {
        
        HashMap map = new HashMap();
        map.put("jdbc-driver", "");
        
        AbstractStorageFactory factory = new StorageFactoryImpl();
        try {
            factory.getUniqueStorage(new HashMap());
            assertTrue(false);
        } catch (StorageException e) {
        }
    }
    
    public final void testGetUniqueStorageSuccess() throws Exception {
        
        HashMap map = new HashMap();
        map.put("jdbc-driver", "org.hsqldb.jdbcDriver");
        map.put("jdbc-url", "jdbc:hsqldb:/home/manuel/tmp/nexd/nexd2.db");
        
        AbstractStorageFactory factory = new StorageFactoryImpl();
        factory.getUniqueStorage(map);
         
    }

}
