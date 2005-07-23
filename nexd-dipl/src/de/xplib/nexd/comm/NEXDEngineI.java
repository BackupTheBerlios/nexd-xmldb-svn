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
 * $Log: NEXDEngineI.java,v $
 * Revision 1.3  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.2  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.8  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.7  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 * Revision 1.6  2005/03/01 10:34:37  nexd
 * Advanced VCL support
 *
 */
package de.xplib.nexd.comm;

import java.io.IOException;
import java.net.URL;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidQueryException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.InvalidParentCollectionException;
import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VariableExistsException;


/**
 * Defines the interface of objects the factory method creates.
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public interface NEXDEngineI {
    
    /**
     * Key used to store a transaction id in 
     * {@link org.xmldb.api.base.Configurable#setProperty(
     *        java.lang.String, java.lang.String)}.
     */
    String TRANSACTION_ID_KEY = "nexd.transaction.id.key";
    /**
     * The result id for a non document query.
     */
    String QUERY_RESULT_ID = "resultId";
    /**
     * The namespace for a non element query result.
     */
    String QUERY_RESULT_NS = "http://nexd.xplib.de/query/1.0";
    /**
     * The default prefix for a non element query result.
     */
    String QUERY_RESULT_PREFIX = "nexd";
    /**
     * The element name for a non element query result.
     */
    String QUERY_RESULT_TAG = "result";
    
    /**
     * The qualified name of a query result document.
     */
    String QNAME_QUERY_RESULT = QUERY_RESULT_PREFIX + ":" + QUERY_RESULT_TAG;

    /**
     * Opens a connection to NEXD XML-Database engine.
     * 
     * @param username The username used for authentication.
     * @param password The password used for authentication.
     * @throws XMLDBException If the authentication fails or an internal
     *         error occures.
     */
    void open(String username, String password) throws XMLDBException;
    
    /**
     * @throws XMLDBException ..
     */
    void close() throws XMLDBException;
    
    /**
     * Checks if an id is already used to identify a stored object.
     *  
     * @param collIn The context <code>SixdmlCollection</code>.
     * @param idIn The id to look for.
     * @return <code>true</code> if the id is already used, otherwise 
     *         <code>false</code>.
     * @throws XMLDBException ..
     */
    boolean containsId(SixdmlCollection collIn, String idIn) 
            throws XMLDBException;
    
    /**
     * Starts a new transaction if the given <code>SixdmlCollection</code>
     * instance isn't already in another transaction.
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @return A unique transaction identifier.
     * @throws XMLDBException If the given <code>SixdmlCollection</code> 
     *                        instance is already in another transaction.
     */
    String beginTransaction(SixdmlCollection collIn) throws XMLDBException;
    
    /**
     * Commits an open transaction.
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @throws XMLDBException If the given <code>SixdmlCollection</code> 
     *                        instance isn't in an open transaction.
     */
    void commitTransaction(SixdmlCollection collIn) throws XMLDBException;
    
    /**
     * Stores a new <code>SixdmlCollection</code> in the backend.
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param nameIn The name of the new <code>SixdmlCollection</code>.
     * @return The instance of the new <code>SixdmlCollection</code>.
     * @throws XMLDBException If the <code>SixdmlCollection</code> already 
     *                        exists or something goes wrong in the 
     *                        <code>StorageI</code>.
     */
    SixdmlCollection storeCollection(SixdmlCollection collIn, String nameIn) 
            throws XMLDBException;
    
    /**
     * <p>Removes a <code>StorageCollectionI</code> from the backend.</p>
     * <p>A <code>Collection</code> that isn't an instance of 
     * {@link VirtualCollection} can only be deleted if it doesn't have any 
     * reference from a {@link VirtualCollection}. If there is any reference an
     * <code>XMLDBException</code> is thrown until all referencing objects are 
     * also deleted.</p>
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param nameIn The name of the <code>SixdmlCollection</code> to delete.
     * @throws XMLDBException If the <code>StorageCollectionI</code> doesn't 
     *                        exist or something goes wrong in the 
     *                        <code>StorageI</code>.
     */
    void dropCollection(SixdmlCollection collIn, String nameIn) 
            throws XMLDBException;
    
    /**
     * Selects a <code>Sixdmlollection</code> in the <code>StorageI</code>. If
     * no <code>SixdmlCollection</code> for <code>collPath</code> exists this 
     * method returns <code>null</code>.
     * 
     * @param collPath The path which locates the 
     *                 <code>StorageCollectionI</code>.
     * @return The <code>SixdmlCollection</code> or <code>null</code>.
     * @throws XMLDBException If something <code>StorageI</code> specific 
     *                        goes wrong.
     */
    SixdmlCollection queryCollection(String collPath) throws XMLDBException;
    
    /**
     * Selects a <code>SixdmlCollection</code> that is a child collection of the
     * given <code>SixdmlCollection</code> instance and has the name that is 
     * stored in the parameter <code>nameIn</code>.<br />
     * This method is used to keep existing transaction states in the collection
     * hierarchy. 
     * 
     * @param parentIn The reference parent <code>SixdmlCollection</code> object
     * @param nameIn The name of the child <code>SixdmlCollection</code>.
     * @return The child <code>SixdmlCollection</code> instance.
     * @throws XMLDBException If something <code>StorageI</code> specific 
     *                        goes wrong.
     */
    SixdmlCollection queryChildCollection(SixdmlCollection parentIn, 
                                          String nameIn) throws XMLDBException;
    
    /**
     * Selects a <code>SixdmlCollection</code> that is the parent collection of 
     * the given <code>SixdmlCollection</code> instance.<br />
     * This method is used to keep existing transaction states in the collection
     * hierarchy. 
     * 
     * @param childIn The reference child <code>SixdmlCollection</code> object.
     * @return The parent <code>SixdmlCollection</code> instance.
     * @throws XMLDBException If something <code>StorageI</code> specific 
     *                        goes wrong.
     */
    SixdmlCollection queryParentCollection(SixdmlCollection childIn)
            throws XMLDBException;
    
    /**
     * Returns the number of child collection that are located under 
     * <code>collIn</code>.
     * 
     * @param collIn The context <code>SixdmlCollection</code>.
     * @return The number of child collections.
     * @throws XMLDBException If something <code>StorageI</code> specific goes
     *                        wrong.
     */
    int queryCollectionCount(SixdmlCollection collIn) throws XMLDBException;
    
    /**
     * Returns an array with all names of collections that are located under
     * <code>collIn</code>.
     *  
     * @param collIn The context <code>SixdmlCollection</code>.
     * @return An array with all names.
     * @exception XMLDBException If something <code>StorageI</code> specific 
     *                           goes wrong.
     */
    String[] queryCollections(SixdmlCollection collIn) throws XMLDBException;
    
    /**
     * Stores a XML-Schema or DTD for <code>SixdmlCollection</code> object.
     * This task can only closed successfull, if all <code>SixdmlResource</code>
     * objects match on the given schema. 
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param schemaIn The <code>URL</code> where the schema is. 
     * @throws InvalidCollectionDocumentException If any XML-Document in the 
     *                                            collection is not valid 
     *                                            against the schema.
     * @throws InvalidSchemaException If the schema type if unkown.
     * @throws IOException If the schema can't be found.
     * @throws XMLDBException An database specific exception.
     */
    void storeCollectionSchema(SixdmlCollection collIn, URL schemaIn) 
            throws InvalidCollectionDocumentException,
                   InvalidSchemaException,
                   IOException,
                   XMLDBException;
    
    /**
     * Remove a schema, if it exists, from the given 
     * <code>SixdmlCollection</code>.
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @throws XMLDBException An database specific exception.
     */
    void dropCollectionSchema(SixdmlCollection collIn) throws XMLDBException;
    
    /**
     * Returns the content of a <code>StorageValidationObjectImpl</code> object 
     * for the give <code>SixdmlCollection</code> or <code>null</code>, if no 
     * schema exists.
     * 
     * @param collIn The context <code>SixdmlCollection</code> object.
     * @return The content of the schema or <code>null</code>.
     * @throws XMLDBException An database specific exception.
     */
    String queryCollectionSchema(SixdmlCollection collIn)
            throws XMLDBException;
    
    /**
     * TODO : Some javadoc
     * 
     * @param collIn <p>The context <code>SixdmlCollection</code> instance.</p>
     * @param nameIn <p>The name of the new <code>VirtualCollection</code>.</p>
     * @param schemaIn <p>The <code>URL</code> where the Virtual Collection
     *                 Language Schema can be found.</p> 
     * @return A new {@link VirtualCollection} object.
     * @throws InvalidCollectionReferenceException <p>This is thrown, if a 
     *                       referenced {@link org.xmldb.api.base.Collection} 
     *                       doesn't exist or it is an instance of 
     *                       {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     * @exception InvalidParentCollectionException
     *            If the <code>Collection</code> that is selected as the parent
     *            for the new <code>VirtualCollection</code> is a an instance of
     *            <code>VirtualCollection</code>.
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an 
     *                               invalid syntax.</p> 
     * @throws InvalidVCLSchemaException <p>This is thrown, if the xml Document
     *                                   doesn't match the required structure or
     *                                   it doesn't define all necessary 
     *                                   attributes.</p>
     * @throws IOException <p>If any IO errors occur.</p>
     * @throws SAXException <p>If any parse errors occur.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is 
     *                                     thrown, if a variable is accessed 
     *                                     that was not declared before.</p>
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p> 
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    VirtualCollection storeCollection(SixdmlCollection collIn,
                                      String nameIn,
                                      URL schemaIn)
    	throws InvalidCollectionReferenceException, 
    	       InvalidParentCollectionException,
    	       InvalidQueryException,
    	       InvalidVCLSchemaException,
    	       IOException,
    	       SAXException,
    	       UndeclaredVariableException,
    	       VariableExistsException,
               XMLDBException;
    
    /**
     * TODO : Some javadoc
     * 
     * @param collIn <p>The context <code>SixdmlCollection</code> instance.</p>
     * @param nameIn <p>The name of the new <code>VirtualCollection</code>.</p>
     * @param schemaIn <p>The <code>URL</code> where the Virtual Collection
     *                 Language Schema can be found.</p>
     * @param xslIn <p>The <code>URL</code> where the XSL-Stylesheet can be 
     *              found.</p> 
     * @return A new {@link VirtualCollection} object.
     * @throws InvalidCollectionReferenceException <p>This is thrown, if a 
     *                       referenced {@link org.xmldb.api.base.Collection} 
     *                       doesn't exist or it is an instance of 
     *                       {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     * @exception InvalidParentCollectionException
     *            If the <code>Collection</code> that is selected as the parent
     *            for the new <code>VirtualCollection</code> is a an instance of
     *            <code>VirtualCollection</code>.
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an 
     *                               invalid syntax.</p> 
     * @throws InvalidVCLSchemaException <p>This is thrown, if the xml Document
     *                                   doesn't match the required structure or
     *                                   it doesn't define all necessary 
     *                                   attributes.</p>
     * @throws IOException <p>If any IO errors occur.</p>
     * @throws SAXException <p>If any parse errors occur.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is 
     *                                     thrown, if a variable is accessed 
     *                                     that was not declared before.</p>
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p> 
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    VirtualCollection storeCollection(SixdmlCollection collIn,
                                      String nameIn,
                                      URL schemaIn,
                                      URL xslIn)
    	throws InvalidCollectionReferenceException, 
    	       InvalidParentCollectionException,
    	       InvalidQueryException,
    	       InvalidVCLSchemaException,
    	       IOException,
    	       SAXException,
    	       UndeclaredVariableException,
    	       VariableExistsException,
               XMLDBException;
    
    /**
     * <p>Stores the xsl-stylesheet for a <code>VirtualCollection</code>. The
     * stylesheed will be retrieved from the given <code>URL</code>. If there is
     * allready a xsl-stylesheet it will be replaced by the new one.</p>
     * 
     * @param collIn <p>The context <code>VirtualCollection</code> instance.</p>
     * @param xslIn <p>The <code>URL</code> where the xsl-stylesheet is located.
     *              </p>
     * @exception IOException <p>If the given <code>URL</code> cannot be opend.
     *                         </p>
     * @exception SAXException <p>If it is not a valid xsl-stylesheet.</p>
     * @exception XMLDBException <p>with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br /></p>
     */
    void storeCollectionStylesheet(VirtualCollection collIn, URL xslIn)
            throws IOException, SAXException, XMLDBException;
    
    /**
     * <p>Removes the xsl-stylesheet from the given <code>VirtualCollection
     * </code>. If there is no stylesheet this method does nothing.</p>
     *  
     * @param collIn <p>The context <code>VirtualCollection</code> instance.</p>
     * @exception XMLDBException <p>If something database specific goes wrong.
     *                           </p>
     */
    void dropCollectionStylesheet(VirtualCollection collIn) 
            throws XMLDBException;
    
    /**
     * <p><code>Returns the xsl-stylesheet for the given <code>collIn</code>. If
     * there is no stylesheet this method returns <code>null</code>.</p>
     * 
     * @param collIn <p>The context <code>VirtualCollection</code> instance.</p>
     * @return <p>The xsl-stylesheet as a DOM <code>Node</code> or 
     *         <code>null</code>.</p>
     * @exception XMLDBException <p>If something database specific goes wrong.
     *                           </p>
     */
    Node queryCollectionStylesheet(VirtualCollection collIn) 
             throws XMLDBException;
    
    /**
     * Stores a <code>StorageObjectI</code> instance in the collection that is
     * identified by <code>collIn</code>.
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param resIn The new <code>SixdmlResource</code> to store.
     * @exception XMLDBException If something <code>StorageI</code> specific 
     *                           goes wrong.
     */
    void storeResource(SixdmlCollection collIn, SixdmlResource resIn)
            throws XMLDBException;
    
    /**
     * Updates an existing <code>Resource</code> instance.
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param resIn The <code>Resource</code> to update.
     * @exception XMLDBException If something <code>StorageI</code> specific 
     *                           goes wrong. 
     */
    void updateResource(SixdmlCollection collIn, Resource resIn) 
            throws XMLDBException;
    
    /**
     * Removes a <code>Resource</code> instance from the 
     * <code>SixdmlCollection collIn</code> object.
     * 
     * @param collIn The context <code>SixdmlCollection</code> object.
     * @param resIn The <code>Resource</code> instance to remove.
     * @exception XMLDBException If something <code>StorageI</code> specific 
     *                           goes wrong.
     */
    void dropResource(SixdmlCollection collIn, Resource resIn)
            throws XMLDBException;
    
    /**
     * Returns a <code>Resource</code> for the parameter <code>idIn</code> in 
     * the <code>SixdmlCollection</code> <code>collIn</code> or 
     * <code>null</code> if no resource for <code>idIn</code> exists.
     * 
     * @param collIn The parent <code>SixdmlCollection</code> object.
     * @param idIn The <code>Resource</code> id.
     * @return The <code>Resource</code> instance or <code>null</code>.
     * @throws XMLDBException If something <code>StorageI</code> specific goes
     *                        wrong.
     */
    Resource queryResource(SixdmlCollection collIn, String idIn) 
            throws XMLDBException;
    
    /**
     * Returns the number of <code>Resource</code> objects stored in
     * <code>collIn</code>.
     * 
     * @param collIn The context <code>SixdmlCollection</code> object.
     * @return The number of stored <code>Resource</code> objects.
     * @throws XMLDBException If something <code>StorageI</code> specific goes
     *                        wrong.
     */
    int queryResourceCount(SixdmlCollection collIn) throws XMLDBException;
    
    /**
     * Returns the names of all <code>Resource</code> objects stored in the
     * <code>SixdmlCollection</code> identfied by <code>collIn</code>.
     * 
     * @param collIn The context <code>SixdmlCollection</code> object.
     * @return An array with the <code>Resource</code> names.
     * @throws XMLDBException If something <code>StorageI</code> specific goes
     *                        wrong.
     */
    String[] queryResources(SixdmlCollection collIn) throws XMLDBException;
    
    /**
     * Executes an XPath query against the database engine. This method returns
     * the found <code>Resource</code> objects in a <code>ResourceSet</code>
     * instance.
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param queryIn The query that will be executed against the engine.
     * @return The <code>ResourceSet</code> instance containing all matching
     *         objects.
     * @throws XMLDBException If a storage specific exception occures.
     * @throws InvalidQueryException if the query is not valid XPath.  
     */
    ResourceSet queryResourcesByXPath(SixdmlCollection collIn, String queryIn) 
            throws XMLDBException,
                   InvalidQueryException;
    
    /**
     * Executes an XPath query against a single <code>Resource</code> in a 
     * <code>SixdmlCollection</code>. This method returns the found 
     * <code>Resource</code> objects in a <code>ResourceSet</code> instance.
     * 
     * @param collIn The context <code>SixdmlCollection</code> instance.
     * @param idIn The id of the context <code>SixdmlResource</code>.
     * @param queryIn The query that will be executed against the engine.
     * @return The <code>ResourceSet</code> instance containing all matching
     *         objects.
     * @throws XMLDBException If a storage specific exception occures.
     * @throws InvalidQueryException if the query is not valid XPath.  
     */
    ResourceSet queryResourceByXPath(SixdmlCollection collIn, 
                                     String idIn, 
                                     String queryIn) 
            throws XMLDBException,
                   InvalidQueryException;

    /**
     * <p>Returns the {@link VCLSchema} for the given <code>collIn</code>.</p>
     *  
     * @param collIn <p>The <code>VirtualCollection</code> that wants to 
     *               retrieve its {@link VCLSchema}.</p>
     * @return <p>The {@link VCLSchema} instance.</p> 
     * @exception XMLDBException <p>If a storage specific exception occures.</p>
     */
    VCLSchema queryVCLSchema(VirtualCollection collIn) throws XMLDBException;
    
    /**
     * <p>Returns the {@link PCVResource} for the given <code>idIn</code> in
     * the {@link VirtualCollection} <code>collIn</code>.</p>
     * 
     * @param collIn <p>The context <code>VirtualCollection</code>.</p>
     * @param idIn <p>The id of the {@link _de.xplib.nexd.api.VirtualResource}
     *             that wants to retrieve the {@link PCVResource}.</p>
     * @return <p>The {@link PCVResource} instance.</p>
     * @exception XMLDBException <p>If a storage specific exception occures.</p>
     */
    PCVResource queryPCVResource(VirtualCollection collIn, String idIn)
            throws XMLDBException;

}
