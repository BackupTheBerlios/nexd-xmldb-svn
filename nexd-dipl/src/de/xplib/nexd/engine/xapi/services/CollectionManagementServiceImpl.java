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

/*
 * $Log: CollectionManagementServiceImpl.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.5  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.4  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.xapi.services;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlCollectionManagementService;
import org.sixdml.dbmanagement.SixdmlIndex;
import org.sixdml.dbmanagement.SixdmlIndexType;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.sixdml.exceptions.UnsupportedIndexTypeException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.NEXDEngineI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class CollectionManagementServiceImpl 
    extends AbstractService 
    implements SixdmlCollectionManagementService {

    /**
     * Constructor.
     * 
     * @param engineIn The associated <code>NEXDEngineI</code> instance.
     * @param parentIn ..
     */
    public CollectionManagementServiceImpl(final NEXDEngineI engineIn,
                                           final SixdmlCollection parentIn) {
        super(engineIn, parentIn, 
                "SixdmlCollectionManagementService", DEFAULT_VERSION);
    }

    /**
     * Creates a collection that is constrained by a particular schema. 
     * 
     * @param name The name of the collection to create. 
     * @param schemaFile the location of the schema file either on the local 
     *                   file system or over the internet. 
     * @return The new <code>Collection</code> instance.
     * @exception XMLDBException if a database error occurs. 
     * @exception IOException if an error occurs while trying to retrieve the 
     *                        file.
     * @exception InvalidSchemaException if the schema is invalid.    
     * @see org.sixdml.dbmanagement.SixdmlCollectionManagementService
     *      #createCollection(java.lang.String, java.net.URL)
     */
    public Collection createCollection(final String name, final URL schemaFile)
            throws XMLDBException, IOException, InvalidSchemaException {
        
        SixdmlCollection coll = (SixdmlCollection) this.createCollection(name);
        try {
            coll.setSchema(schemaFile);
        } catch (InvalidCollectionDocumentException e) {
            this.removeCollection(name);
        } catch (InvalidSchemaException e) {
            this.removeCollection(name);
            throw e;
        } catch (IOException e) {
            this.removeCollection(name);
            throw e;
        } catch (XMLDBException e) {
            this.removeCollection(name);
            throw e;
        }

        return coll;
    }

    /**
     * Creates an index with a name, an indexFields table and an underlying DXE 
     * database. <BR> <B>NOTE</b>: Runtime error may occur if table not 
     * properly populated.
     *  
     * @param name the name of the index. 
     * @param indexFields the index fields for the class. 
     * @return The created <code>SixdmlIndex</code>.
     * @exception XMLDBException if an error occurs. 
     * @exception UnsupportedIndexTypeException if the type of index requested 
     *                                          is unsupported by the database. 
     * @see org.sixdml.dbmanagement.SixdmlCollectionManagementService
     *      #createIndex(java.lang.String, java.util.HashMap)
     */
    public SixdmlIndex createIndex(final String name, final HashMap indexFields)
            throws XMLDBException, UnsupportedIndexTypeException {

        return null;
    }

    /**
     * Gets the types of index that are supported by the this database.
     *  
     * @return an array of SixdmlIndexTypes which the database supports.
     * @exception XMLDBException if an error occurs.
     * @see org.sixdml.dbmanagement.SixdmlCollectionManagementService
     *      #getSupportedIndexTypes()
     */
    public SixdmlIndexType[] getSupportedIndexTypes() throws XMLDBException {
        return new SixdmlIndexType[0];
    }

    /**
     * Creates a new <code>Collection</code> in the database. The default 
     * configuration of the database is determined by the implementer. The 
     * new <code>Collection</code> will be created relative to the <code>
     * Collection</code> from which the <code>CollectionManagementService</code>
     * was retrieved.
     *
     * @param name The name of the collection to create.
     * @return The created <code>Collection</code> instance.
     * @exception XMLDBException with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *            specific errors that occur.<br />
     * @see org.xmldb.api.modules.CollectionManagementService#createCollection(
     *      java.lang.String)
     */
    public Collection createCollection(final String name) 
            throws XMLDBException {
        
        return this.engine.storeCollection(this.ctxColl, name);
    }

    /**
     * Removes a named <code>Collection</code> from the system. The 
     * name for the <code>Collection</code> to remove is relative to the <code>
     * Collection</code> from which the <code>CollectionManagementService</code>
     * was retrieved.
     *
     * @param name The name of the collection to remove.
     * @exception XMLDBException with expected error codes.<br />
     *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *  specific errors that occur.<br />
     * @see org.xmldb.api.modules.CollectionManagementService#removeCollection(
     *      java.lang.String)
     */
    public void removeCollection(final String name) throws XMLDBException {
        
        // don't delete the root collection
        if (name.equals("/db")) {
            throw new XMLDBException(ErrorCodes.INVALID_COLLECTION);
        }
        this.engine.dropCollection(this.ctxColl, name);
    }

}
