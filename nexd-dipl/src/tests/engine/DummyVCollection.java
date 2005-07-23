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
 * $Log: DummyVCollection.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.engine;

import java.io.IOException;
import java.net.URL;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.vcl.VCLSchema;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class DummyVCollection implements VirtualCollection {

    /**
     * 
     */
    public DummyVCollection() {
        super();
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws XMLDBException
     * @see _de.xplib.nexd.api.VirtualCollection#getVCLSchema()
     */
    public VCLSchema getVCLSchema() throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param xslIn
     * @throws IOException
     * @throws SAXException
     * @throws XMLDBException
     * @see _de.xplib.nexd.api.VirtualCollection#setStylesheet(java.net.URL)
     */
    public void setStylesheet(URL xslIn) throws IOException, SAXException,
            XMLDBException {
    }

    /**
     * <Some description here>
     * 
     * @throws XMLDBException
     * @see _de.xplib.nexd.api.VirtualCollection#unsetStylesheet()
     */
    public void unsetStylesheet() throws XMLDBException {
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws XMLDBException
     * @see _de.xplib.nexd.api.VirtualCollection#showStylesheet()
     */
    public Node showStylesheet() throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getName()
     */
    public String getName() throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getServices()
     */
    public Service[] getServices() throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param name
     * @param version
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getService(java.lang.String, java.lang.String)
     */
    public Service getService(String name, String version)
            throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getParentCollection()
     */
    public Collection getParentCollection() throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getChildCollectionCount()
     */
    public int getChildCollectionCount() throws XMLDBException {
        return 0;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#listChildCollections()
     */
    public String[] listChildCollections() throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param name
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getChildCollection(java.lang.String)
     */
    public Collection getChildCollection(String name) throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getResourceCount()
     */
    public int getResourceCount() throws XMLDBException {
        return 0;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#listResources()
     */
    public String[] listResources() throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param id
     * @param type
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#createResource(java.lang.String, java.lang.String)
     */
    public Resource createResource(String id, String type)
            throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param res
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#removeResource(org.xmldb.api.base.Resource)
     */
    public void removeResource(Resource res) throws XMLDBException {
    }

    /**
     * <Some description here>
     * 
     * @param res
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#storeResource(org.xmldb.api.base.Resource)
     */
    public void storeResource(Resource res) throws XMLDBException {
    }

    /**
     * <Some description here>
     * 
     * @param id
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getResource(java.lang.String)
     */
    public Resource getResource(String id) throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#createId()
     */
    public String createId() throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#isOpen()
     */
    public boolean isOpen() throws XMLDBException {
        return false;
    }

    /**
     * <Some description here>
     * 
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#close()
     */
    public void close() throws XMLDBException {
    }

    /**
     * <Some description here>
     * 
     * @param name
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Configurable#getProperty(java.lang.String)
     */
    public String getProperty(String name) throws XMLDBException {
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param name
     * @param value
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Configurable#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty(String name, String value) throws XMLDBException {
    }

}
