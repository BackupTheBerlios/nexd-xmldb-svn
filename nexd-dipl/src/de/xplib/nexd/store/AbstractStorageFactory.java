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
 * $Log: AbstractStorageFactory.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.6  2005/04/22 14:59:42  nexd
 * SOAP support and performance update.
 *
 * Revision 1.5  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.4  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.store;

import java.util.Map;

import org.w3c.dom.Document;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractStorageFactory {

    /**
     * @clientCardinality 1
     * @clientRole storage
     * @directed true
     * @link aggregation
     * @supplierCardinality 1
     */
    /*#de.xplib.nexd.store.StorageI storageI*/

    /**
     * Constant with the identifier for the used AbstractStorageFactory class.
     */
    public static final String STORAGE_FACTORY_KEY 
            = "de.xplib.nexd.store.storage.factory";

    /**
     * This factory method creates an instance of AbstractStorageFactory. It 
     * tries to find the class name in the system properties and the environment
     * variables. The used variable key is
     * <code>de.xplib.nexd.store.storage.factory</code>.
     *
     * <code>
     *   System.getProperty('de.xplib.nexd.store.storage.factory');
     * 	 System.getenv().get('de.xplib.nexd.store.storage.factory');
     * </code>
     *
     * @return Returns a concrete instance of AbstractStorageFactory.
     * @throws ClassNotFoundException If the configured class cannot be found.
     * @throws InstantiationException If something goes wrong.
     * @throws IllegalAccessException If something goes wrong.
     * @throws StorageException If there is no configuration for the
     *                          used factory, this Exception is thrown.
     */
    public static final AbstractStorageFactory newInstance()
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, StorageException {

        String name = "";
        if (System.getProperties().containsKey(STORAGE_FACTORY_KEY)) {
            name = System.getProperty(STORAGE_FACTORY_KEY);
        } else if (System.getenv().containsKey(STORAGE_FACTORY_KEY)) {
            name = (String) System.getenv().get(STORAGE_FACTORY_KEY);
        } else {
            throw new StorageException("Cannot find Storage driver.");
        }

        Class clazz = Class.forName(name);
        return (AbstractStorageFactory) clazz.newInstance();
    } // end public static final AbstractStorageFactory newInstance()
    
    /**
     * Factory method that returns an instance of a validation object that
     * can be stored for a collection.
     * 
     * @param cntIn The content of a DTD or a XML-Schema that is used for xml
     *              validation. 
     * @return The concrete <code>StorageValidationObjectI</code> instance.
     * @throws StorageException If the type of the schema does not match to the
     *                          content or the the schema type is not supported.
     */
    public abstract StorageValidationObjectI createValidationObject(
            final String cntIn) throws StorageException;
    
    /**
     * Factory method that returns an instance of a validation object that
     * can be stored for a collection.
     *
     * @param typeIn The type of the <code>StorageValidationObjectI</code>. 
     * @param cntIn The content of a DTD or a XML-Schema that is used for xml
     *              validation. 
     * @return The concrete <code>StorageValidationObjectI</code> instance.
     * @throws StorageException If the type of the schema does not match to the
     *                          content or the the schema type is not supported.
     */
    public abstract StorageValidationObjectI createValidationObject(
            final byte typeIn, final String cntIn) throws StorageException;
    
    /**
     * Factory method that creates a <code>StorageDocumentObjectI</code> for the
     * underlying implementation.
     * 
     * @param iidIn The internal id.
     * @param oidIn The unique object id.
     * @param domIn The content for the storage object.
     * @return A concrete implementation of the storage object.
     * @throws StorageException If any database specific error occures.
     */
    public abstract StorageDocumentObjectI createDocumentObject(
            final InternalIdI iidIn, final String oidIn, final Document domIn)
            throws StorageException;
    
    /**
     * Factory method that creates a <code>StorageDocumentObjectI</code> for the
     * underlying implementation.
     * 
     * @param oidIn The unique object id.
     * @param domIn The content for the storage object.
     * @return A concrete implementation of the storage object.
     * @throws StorageException If any database specific error occures.
     */
    public abstract StorageDocumentObjectI createDocumentObject(
            final String oidIn, final Document domIn) throws StorageException;

    /**
     * This method creates the <code>StorageI</code> instance, that is
     * associated with the factory. There is only one unique instance of the
     * <code>Storage</code> in the system, which means that this method also
     * implements the <i>GoF Singleton Pattern</i>.
     *
     * @param configIn The concrete <code>StorageI</code> specific configuration
     *                 parameters.
     * @return The concrete StorageI instance.
     * @throws StorageException This is thrown if something is
     *                                missconfigured for the used storage.
     */
    public abstract StorageI getUniqueStorage(Map configIn)
            throws StorageException;

} // end public abstract class AbstractStorageFactory