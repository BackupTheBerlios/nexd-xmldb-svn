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
 * $Log: VirtualCollectionManagementService.java,v $
 * Revision 1.7  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.6  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.5  2005/03/26 12:14:20  nexd
 * UML documentation.
 *
 * Revision 1.4  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api;

import java.io.IOException;
import java.net.URL;

import org.sixdml.exceptions.InvalidQueryException;
import org.sixdml.exceptions.NonWellFormedXMLException;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VariableExistsException;

/**
 * VirtualCollectionManagementService is a <code>Service</code> that enables
 * the basic management of <code>VirtualCollection</code> instances within a
 * database. The functionality provided is very basic because collection
 * management varies widely among databases. This service simply provides
 * functionality for those databases that are able to implement this basic
 * functionality.
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.7 $
 */

public interface VirtualCollectionManagementService extends Service {

    /**
     * @label creates/deletes
     */
    /*#_de.xplib.nexd.api.VirtualCollection Dependency_Link*/

    /**
     * Creates a new <code>VirtualCollection</code> in the database. The
     * default configuration of the database is determined by the implementor.
     * The new <code>VirtualCollection</code> will be created relative to the
     * <code>Collection</code> from which the
     * <code>VirtualCollectionManagementService</code> was retrieved.
     *
     *
     * @param nameIn The name of the collection to create.
     * @param schemaIn The URL where the Virtual Collection Language Schema,
     *                 that discribes how <code>VirtualResource</code> objects
     *                 are build, is located.
     * @return The created <code>VirtualCollection</code> instance.
     * @throws IOException If the given <code>URL</code> couldn't be opend.
     * @throws InvalidCollectionReferenceException
     *            If any of the <code>Collection</code> named in the given
     *            <code>VCLSchema</code> doesn't exist or is an instance of
     *            <code>VirtualCollection</code>
     * @exception InvalidParentCollectionException
     *            If the <code>Collection</code> that is selected as the parent
     *            for the new <code>VirtualCollection</code> is a an instance of
     *            <code>VirtualCollection</code>.
     * @exception InvalidQueryException ...
     * @throws NonWellFormedXMLException If the XML of the Virtual Collection
     *                                   Language Schema is not well formed.
     * @exception SAXException If the given schema is not a valid XML document
     *                         and can't be parsed
     * @exception UndeclaredVariableException ...
     * @exception InvalidVCLSchemaException ...
     * @exception VariableExistsException ...
     * @exception XMLDBException With expected error codes.<br />
     *                           <code>ErrorCodes.VENDOR_ERROR</code> for any
     *                           vendor specific errors that occur.<br />
     */
    VirtualCollection createVirtualCollection(String nameIn, URL schemaIn)
            throws InvalidCollectionReferenceException,
            InvalidParentCollectionException, InvalidQueryException,
            InvalidVCLSchemaException, IOException, NonWellFormedXMLException,
            SAXException, VariableExistsException, UndeclaredVariableException,
            XMLDBException;

    /**
     * Creates a new <code>VirtualCollection</code> in the database. The
     * default configuration of the database is determined by the implementor.
     * The new <code>VirtualCollection</code> will be created relative to the
     * <code>Collection</code> from which the
     * <code>VirtualCollectionManagementService</code> was retrieved.
     *
     * @param nameIn The name of the collection to create.
     * @param schemaIn The URL where the Virtual Collection Language Schema,
     *                 that discribes how <code>VirtualResource</code> objects
     *                 are build, is located.
     * @param xslIn The URL whhere a XSL-Template, that is used to transform a
     *              <code>VirtualResource</code> before it will be send to the
     *              client, is located.
     * @return The created <code>VirtualCollection</code> instance.
     * @throws IOException If the given <code>URL</code> couldn't be opend.
     * @throws InvalidCollectionReferenceException
     *            If any of the <code>Collection</code> named in the given
     *            <code>VCLSchema</code> doesn't exist or is an instance of
     *            <code>VirtualCollection</code>
     * @exception InvalidParentCollectionException
     *            If the <code>Collection</code> that is selected as the parent
     *            for the new <code>VirtualCollection</code> is a an instance of
     *            <code>VirtualCollection</code>.
     * @exception InvalidQueryException ...
     * @throws NonWellFormedXMLException If the XML of the Virtual Collection
     *                                   Language Schema is not well formed.
     * @exception SAXException If the given schema is not a valid XML document
     *                         and can't be parsed
     * @exception UndeclaredVariableException ...
     * @exception InvalidVCLSchemaException ...
     * @exception VariableExistsException ...
     * @exception XMLDBException With expected error codes.<br />
     *                           <code>ErrorCodes.VENDOR_ERROR</code> for any
     *                           vendor specific errors that occur.<br />
     */
    VirtualCollection createVirtualCollection(String nameIn, URL schemaIn,
            URL xslIn) throws InvalidCollectionReferenceException,
            InvalidParentCollectionException, InvalidQueryException,
            InvalidVCLSchemaException, IOException, NonWellFormedXMLException,
            SAXException, VariableExistsException, UndeclaredVariableException,
            XMLDBException;

    /**
     * Removes a named <code>VirtualCollection</code> from the system. The
     * name for the <code>VirtualCollection</code> to remove is relative to the
     * <code>Collection</code> from which the
     * <code>VirtualCollectionManagementService</code> was retrieved.
     *
     * @param nameIn The name of the collection to remove.
     * @exception XMLDBException With expected error codes.<br />
     *                           <code>ErrorCodes.VENDOR_ERROR</code> for any
     *                           vendor specific errors that occur.<br />
     */
    void removeVirtualCollection(String nameIn) throws XMLDBException;

    /**
     * Removes a <code>VirtualCollection</code> by its instance from the system.
     *
     * @param collIn The <code>VirtualCollection</code> to remove.
     * @exception XMLDBException With expected error codes.<br />
     *                           <code>ErrorCodes.VENDOR_ERROR</code> for any
     *                           vendor specific errors that occur.<br />
     */
    void removeVirtualCollection(VirtualCollection collIn)
            throws XMLDBException;

}
