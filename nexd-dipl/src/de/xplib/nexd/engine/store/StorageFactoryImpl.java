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
 * $Log: StorageFactoryImpl.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.4  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.store;

import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.xplib.nexd.store.AbstractStorageFactory;
import de.xplib.nexd.store.InternalIdI;
import de.xplib.nexd.store.StorageDocumentObjectI;
import de.xplib.nexd.store.StorageException;
import de.xplib.nexd.store.StorageI;
import de.xplib.nexd.store.StorageValidationObjectI;
/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public class StorageFactoryImpl extends AbstractStorageFactory {

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregation
     * @supplierCardinality 0..*
     * @supplierRole concrete product
     */

    private DocumentObjectI lnkDocumentObjectI;

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregation
     * @supplierCardinality 0..*
     * @supplierRole concrete product
     */

    /*#private StorageValidationObjectImpl lnkStorageValidationObjectImpl*/

    /**
     * @backDirected true
     * @clientCardinality 1
     * @directed true
     * @link aggregation
     * @supplierCardinality 1
     * @supplierRole concrete product
     */
    /*#StorageImpl lnkStorageImpl*/

    /**
     * This method creates the <code>StorageI</code> instance, that is
     * associated with the factory. There is only one unique instance of the
     * <code>Storage</code> in the system, which means that this method also
     * implements the <i>GoF Singleton Pattern</i>.
     *
     * @param configIn The concrete <code>StorageI</code> specific configuration
     *                 parameters.
     * @return Instance of Storage
     * @throws StorageException This is thrown if the jdbc driver is not
     *                          configured or the class cannot be found.
     */
    public final StorageI getUniqueStorage(final Map configIn)
            throws StorageException {

        String jdbcDriver = (String) configIn.get("jdbc-driver");
        if (jdbcDriver == null) {
            throw new StorageException("???");
        }

        String jdbcUrl = (String) configIn.get("jdbc-url");
        if (jdbcUrl == null) {
            throw new StorageException("???");
        }

        return new StorageImpl(this, jdbcDriver, jdbcUrl);
    }

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
    public StorageValidationObjectI createValidationObject(final String cntIn)
            throws StorageException {

        return new StorageValidationObjectImpl(cntIn);
    }

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
    public StorageValidationObjectI createValidationObject(final byte typeIn,
            final String cntIn) throws StorageException {

        return new StorageValidationObjectImpl(typeIn, cntIn);
    }

    /**
     * Factory method that creates a <code>StorageDocumentObjectI</code> for the
     * underlying implementation.
     *
     * @param iidIn The internal id.
     * @param oidIn The unique object id.
     * @param domIn The content for the storage object.
     * @return A concrete implementation of the storage object.
     * @throws StorageException If any database specific error occures.
     * @see de.xplib.nexd.store.AbstractStorageFactory#createDocumentObject(
     *      de.xplib.nexd.store.InternalIdI, java.lang.String,
     *      org.w3c.dom.Document)
     */
    public StorageDocumentObjectI createDocumentObject(final InternalIdI iidIn,
            final String oidIn, final Document domIn) throws StorageException {

        DocumentObjectI docImpl = (DocumentObjectI) this.createDocumentObject(
                oidIn, domIn);
        docImpl.setInternalId(iidIn);

        return docImpl;
    }

    /**
     * Factory method that creates a <code>StorageDocumentObjectI</code> for the
     * underlying implementation.
     *
     * @param oidIn The unique object id.
     * @param domIn The content for the storage object.
     * @return A concrete implementation of the storage object.
     * @throws StorageException If any database specific error occures.
     * @see de.xplib.nexd.store.AbstractStorageFactory#createDocumentObject(
     *      java.lang.String, org.w3c.dom.Document)
     */
    public StorageDocumentObjectI createDocumentObject(final String oidIn,
            final Document domIn) throws StorageException {

        DocumentObjectI domImpl;
        if (domIn instanceof DocumentObjectI) {
            domImpl = (DocumentObjectI) domIn;
        } else {
            DocumentBuilder docBuilder;
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                docBuilder = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new StorageException("Cannot convert input Document.");
            }

            domImpl = (DocumentObjectI) docBuilder.newDocument();

            Node child = domIn.getFirstChild();
            while (child != null) {
                domImpl.appendChild(domImpl.importNode(child, true));
                child = child.getNextSibling();
            }
        }
        domImpl.setResourceId(oidIn);
        domImpl.setDocumentId(oidIn);

        return domImpl;
    }
}