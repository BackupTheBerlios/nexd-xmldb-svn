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
package de.xplib.nexd.xapi;

import java.io.IOException;
import java.net.URL;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlIndex;
import org.sixdml.exceptions.DocumentAlreadyExistsException;
import org.sixdml.exceptions.IndexAlreadyExistsException;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.sixdml.exceptions.NonExistentDocumentException;
import org.sixdml.exceptions.NonExistentIndexException;
import org.sixdml.exceptions.NonWellFormedXMLException;
import org.sixdml.exceptions.UnsupportedIndexTypeException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class CollectionImpl 
    extends AbstractCollection
    implements SixdmlCollection {
    
    /**
     * 
     */
    public CollectionImpl() {
        super();
    }

    /**
     * <Some description here>
     * 
     * @param schemaFile
     * @throws org.sixdml.exceptions.InvalidCollectionDocumentException
     * @throws java.io.IOException
     * @throws org.sixdml.exceptions.InvalidSchemaException
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.dbmanagement.SixdmlCollection#setSchema(java.net.URL)
     */
    public void setSchema(final URL schemaFile)
            throws InvalidCollectionDocumentException, IOException,
            InvalidSchemaException, XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.dbmanagement.SixdmlCollection#showSchema()
     */
    public final String showSchema() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <Some description here>
     * 
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.dbmanagement.SixdmlCollection#unsetSchema()
     */
    public void unsetSchema() throws XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @param name
     * @param xmlString
     * @throws org.sixdml.exceptions.DocumentAlreadyExistsException
     * @throws org.sixdml.exceptions.InvalidCollectionDocumentException
     * @throws org.sixdml.exceptions.NonWellFormedXMLException
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.dbmanagement.SixdmlCollection#insertDocument(
     * 		java.lang.String, java.lang.String)
     */
    public void insertDocument(final String name, final String xmlString)
            throws DocumentAlreadyExistsException,
                   InvalidCollectionDocumentException, 
                   NonWellFormedXMLException,
                   XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @param name
     * @param documentSource
     * @throws org.sixdml.exceptions.DocumentAlreadyExistsException
     * @throws java.io.IOException
     * @throws org.sixdml.exceptions.InvalidCollectionDocumentException
     * @throws org.sixdml.exceptions.NonWellFormedXMLException
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.dbmanagement.SixdmlCollection#insertDocument(
     * 		java.lang.String, java.net.URL)
     */
    public void insertDocument(final String name, final URL documentSource)
            throws DocumentAlreadyExistsException, 
                   IOException,
                   InvalidCollectionDocumentException, 
                   NonWellFormedXMLException,
                   XMLDBException {
        
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @param name
     * @throws org.sixdml.exceptions.NonExistentDocumentException
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.dbmanagement.SixdmlCollection#removeDocument(
     * 		java.lang.String)
     */
    public void removeDocument(final String name)
            throws NonExistentDocumentException, XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @param index
     * @throws org.sixdml.exceptions.UnsupportedIndexTypeException
     * @throws org.sixdml.exceptions.IndexAlreadyExistsException
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.dbmanagement.SixdmlCollection#addIndex(
     * 		org.sixdml.dbmanagement.SixdmlIndex)
     */
    public void addIndex(final SixdmlIndex index)
            throws UnsupportedIndexTypeException, 
                   IndexAlreadyExistsException,
                   XMLDBException {
        
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @param name
     * @throws org.sixdml.exceptions.NonExistentIndexException
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.dbmanagement.SixdmlCollection#removeIndex(
     * 		java.lang.String)
     */
    public void removeIndex(final String name) 
    		throws NonExistentIndexException, XMLDBException {
        
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.dbmanagement.SixdmlCollection#getIndices()
     */
    public final SixdmlIndex[] getIndices() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getName()
     */
    public final String getName() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getServices()
     */
    public final Service[] getServices() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param name
     * @param version
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getService(
     * 		java.lang.String, java.lang.String)
     */
    public Service getService(final String name, final String version)
            throws XMLDBException {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
    public Collection getChildCollection(final String name) 
    		throws XMLDBException {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param id
     * @param type
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#createResource(
     *      java.lang.String, java.lang.String)
     */
    public Resource createResource(final String id, final String type)
            throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param res
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#removeResource(
     *      org.xmldb.api.base.Resource)
     */
    public void removeResource(final Resource res) throws XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @param res
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#storeResource(
     *      org.xmldb.api.base.Resource)
     */
    public void storeResource(final Resource res) throws XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @param id
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#getResource(java.lang.String)
     */
    public Resource getResource(final String id) throws XMLDBException {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * <Some description here>
     * 
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.xmldb.api.base.Collection#close()
     */
    public void close() throws XMLDBException {
        // TODO Auto-generated method stub

    }

}
