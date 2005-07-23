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
 * $Log: Service.java,v $
 * Revision 1.2  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:42  nexd
 * SOAP support and performance update.
 *
 */
package de.xplib.nexd.engine.http.soap;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidQueryException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
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
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.http.soap.xsd.SixdmlCollectionType;
import de.xplib.nexd.engine.xapi.AbstractCollection;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class Service {
    
    /**
     * The web  serivce descriptor.
     */
    private static String wsdl = null;
    
    /**
     * The wrapped <code>NEXDEngineI</code> instance.
     */
    private final NEXDEngineI engine;
    
    /**
     * Are remote queries allowed?
     */
    private final boolean allowRemote;
    
    /**
     * Are remote changes allowed?
     */
    private final boolean allowRemoteChanges;
    
    /**
     * The local server internet address.
     */
    private final InetAddress address;
    
    /**
     * The remote client address.
     */
    private InetAddress clientAddress;
    
    /**
     * Reads the web service descriptor.
     * 
     * @return The web service descriptor.
     * @throws IOException If the file is not available.
     */
    public static String getWSDL() throws IOException {
        if (wsdl == null) {
            InputStream stream = Service.class.getResourceAsStream(
            "/de/xplib/nexd/engine/http/soap/service.wsdl");

            StringBuilder builder = new StringBuilder();
            int b;
    		byte[] data = new byte[1024];
    		while ((b = stream.read()) != -1) {
    		    builder.append((char) b);
    		}
    		wsdl = builder.toString();
        }
        
        return wsdl;
    }
    
    /**
     * Constructor.
     * 
     * @param engineIn The <code>NEXDEngineImpl</code> instance that is wrapped 
     *                 by this service.
     * @param addressIn The internet address.
     * @param allowRemoteIn Are remote operations allowed?
     * @param allowRemoteChangesIn Are remote change operations allowed?
     */
    public Service(final NEXDEngineI engineIn, 
                   final InetAddress addressIn,
                   final boolean allowRemoteIn,
                   final boolean allowRemoteChangesIn) {
        
        super();
        
        this.engine  = engineIn;
        this.address = addressIn;
        
        this.allowRemote        = allowRemoteIn;
        this.allowRemoteChanges = allowRemoteChangesIn;
    }
    
    /**
     * @param clientAddressIn The client address that starts the current request
     *                        or <code>null</code>.
     */
    public void setClientAddress(final InetAddress clientAddressIn) {
        this.clientAddress = clientAddressIn;
    }

    /**
     * <Some description here>
     * 
     * @param username
     * @param password
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#open(java.lang.String, 
     *                                          java.lang.String)
     */
    public void open(final String username, final String password) 
            throws XMLDBException {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#close()
     */
    public void close() throws XMLDBException {
        // TODO Auto-generated method stub
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param idIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#containsId(
     *      org.sixdml.dbmanagement.SixdmlCollection, java.lang.String)
     */
    public boolean containsId(final SixdmlCollection collIn, final String idIn)
            throws XMLDBException {
                
        this.checkRemoteAccess();
        return this.engine.containsId(collIn, idIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#beginTransaction(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public String beginTransaction(final SixdmlCollection collIn)
            throws XMLDBException {

        this.checkRemoteChangeAccess();
        return this.engine.beginTransaction(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#commitTransaction(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public void commitTransaction(final SixdmlCollection collIn)
            throws XMLDBException {

        this.checkRemoteChangeAccess();
        this.engine.commitTransaction(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param nameIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#storeCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection, java.lang.String)
     */
    public SixdmlCollection storeCollection(final SixdmlCollection collIn,
                                            final String nameIn) 
            throws XMLDBException {
        
        this.checkRemoteChangeAccess();
        return this.engine.storeCollection(collIn, nameIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param nameIn
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#dropCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection, java.lang.String)
     */
    public void dropCollection(final SixdmlCollection collIn, 
                               final String nameIn)
            throws XMLDBException {
        
        this.checkRemoteChangeAccess();
        this.engine.dropCollection(collIn, nameIn);

        SixdmlCollectionType.removeCollection((AbstractCollection) collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collPath
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollection(java.lang.String)
     */
    public SixdmlCollection queryCollection(final String collPath)
            throws XMLDBException {
        
        System.out.println("queryCollection");
        
        this.checkRemoteAccess();
        return this.engine.queryCollection(collPath);
    }

    /**
     * <Some description here>
     * 
     * @param parentIn
     * @param nameIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryChildCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection, java.lang.String)
     */
    public SixdmlCollection queryChildCollection(
            final SixdmlCollection parentIn, final String nameIn) 
            throws XMLDBException {

        this.checkRemoteAccess();
        return this.engine.queryChildCollection(parentIn, nameIn);
    }

    /**
     * <Some description here>
     * 
     * @param childIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryParentCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public SixdmlCollection queryParentCollection(
            final SixdmlCollection childIn) throws XMLDBException {

        this.checkRemoteAccess();
        return this.engine.queryParentCollection(childIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollectionCount(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int queryCollectionCount(final SixdmlCollection collIn)
            throws XMLDBException {

        this.checkRemoteAccess();
        return this.engine.queryCollectionCount(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollections(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public String[] queryCollections(final SixdmlCollection collIn)
            throws XMLDBException {
        
        this.checkRemoteAccess();
        return this.engine.queryCollections(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param schemaIn
     * @throws InvalidCollectionDocumentException
     * @throws InvalidSchemaException
     * @throws IOException
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#storeCollectionSchema(
     *      org.sixdml.dbmanagement.SixdmlCollection, java.net.URL)
     */
    public void storeCollectionSchema(final SixdmlCollection collIn, 
                                      final String schemaIn)
            throws InvalidCollectionDocumentException, 
                   InvalidSchemaException,
                   IOException, 
                   XMLDBException {
        
        this.checkRemoteChangeAccess();
        
        URL url;
        try {
            url = new URL(schemaIn);
        } catch (MalformedURLException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        this.engine.storeCollectionSchema(collIn, url);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#dropCollectionSchema(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public void dropCollectionSchema(final SixdmlCollection collIn)
            throws XMLDBException {
        
        this.checkRemoteChangeAccess();
        this.engine.dropCollectionSchema(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollectionSchema(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public String queryCollectionSchema(final SixdmlCollection collIn)
            throws XMLDBException {
        
        this.checkRemoteAccess();
        return this.engine.queryCollectionSchema(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param nameIn
     * @param schemaIn
     * @return
     * @throws InvalidCollectionReferenceException
     * @throws InvalidParentCollectionException
     * @throws InvalidQueryException
     * @throws InvalidVCLSchemaException
     * @throws IOException
     * @throws SAXException
     * @throws UndeclaredVariableException
     * @throws VariableExistsException
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#storeCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection, 
     *      java.lang.String, java.net.URL)
     */
    public VirtualCollection storeCollection(final SixdmlCollection collIn,
                                             final String nameIn, 
                                             final String schemaIn)
            throws InvalidCollectionReferenceException,
                   InvalidParentCollectionException, 
                   InvalidQueryException,
                   InvalidVCLSchemaException, 
                   IOException, 
                   SAXException,
                   UndeclaredVariableException, 
                   VariableExistsException,
                   XMLDBException {
        
        this.checkRemoteChangeAccess();
        
        URL url;
        try {
            url = new URL(schemaIn);
        } catch (MalformedURLException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        return this.engine.storeCollection(collIn, nameIn, url);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param nameIn
     * @param schemaIn
     * @param xslIn
     * @return
     * @throws InvalidCollectionReferenceException
     * @throws InvalidParentCollectionException
     * @throws InvalidQueryException
     * @throws InvalidVCLSchemaException
     * @throws IOException
     * @throws SAXException
     * @throws UndeclaredVariableException
     * @throws VariableExistsException
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#storeCollection(
     *      org.sixdml.dbmanagement.SixdmlCollection, 
     *      java.lang.String, java.net.URL, java.net.URL)
     */
    public VirtualCollection storeCollection(final SixdmlCollection collIn,
                                             final String nameIn, 
                                             final String schemaIn, 
                                             final String xslIn)
            throws InvalidCollectionReferenceException,
                   InvalidParentCollectionException, 
                   InvalidQueryException,
                   InvalidVCLSchemaException, 
                   IOException, 
                   SAXException,
                   UndeclaredVariableException, 
                   VariableExistsException,
                   XMLDBException {
        
        this.checkRemoteChangeAccess();
        
        URL schema = new URL(schemaIn);
        URL xsl    = new URL(xslIn);
        
        return this.engine.storeCollection(collIn, nameIn, schema, xsl);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param xslIn
     * @throws IOException
     * @throws SAXException
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#storeCollectionStylesheet(
     *      _de.xplib.nexd.api.VirtualCollection, java.net.URL)
     */
    public void storeCollectionStylesheet(final VirtualCollection collIn, 
                                          final String xslIn)
            throws IOException, SAXException, XMLDBException {
        
        this.checkRemoteChangeAccess();
        
        URL xsl = new URL(xslIn);
        this.engine.storeCollectionStylesheet(collIn, xsl);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#dropCollectionStylesheet(
     *      _de.xplib.nexd.api.VirtualCollection)
     */
    public void dropCollectionStylesheet(final VirtualCollection collIn)
            throws XMLDBException {
        
        this.checkRemoteChangeAccess();
        this.engine.dropCollectionStylesheet(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollectionStylesheet(
     *      _de.xplib.nexd.api.VirtualCollection)
     */
    public Node queryCollectionStylesheet(final VirtualCollection collIn)
            throws XMLDBException {

        this.checkRemoteAccess();
        return this.engine.queryCollectionStylesheet(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param resIn
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#storeResource(
     *      org.sixdml.dbmanagement.SixdmlCollection, 
     *      org.sixdml.dbmanagement.SixdmlResource)
     */
    public void storeResource(final SixdmlCollection collIn, 
                              final SixdmlResource resIn)
            throws XMLDBException {

        this.checkRemoteChangeAccess();
        this.engine.storeResource(collIn, resIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param resIn
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#updateResource(
     *      org.sixdml.dbmanagement.SixdmlCollection, 
     *      org.xmldb.api.base.Resource)
     */
    public void updateResource(final SixdmlCollection collIn, 
                               final SixdmlResource resIn)
            throws XMLDBException {

        this.checkRemoteChangeAccess();
        this.engine.updateResource(collIn, resIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param resIn
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#dropResource(
     *      org.sixdml.dbmanagement.SixdmlCollection, 
     *      org.xmldb.api.base.Resource)
     */
    public void dropResource(final SixdmlCollection collIn, 
                             final SixdmlResource resIn)
            throws XMLDBException {

        this.checkRemoteChangeAccess();
        this.engine.dropResource(collIn, resIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param idIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResource(
     *      org.sixdml.dbmanagement.SixdmlCollection, java.lang.String)
     */
    public Resource queryResource(final SixdmlCollection collIn, 
                                  final String idIn)
            throws XMLDBException {
        
        this.checkRemoteAccess();
        
        return this.engine.queryResource(collIn, idIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResourceCount(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public int queryResourceCount(final SixdmlCollection collIn)
            throws XMLDBException {
        
        this.checkRemoteAccess();
        return this.engine.queryResourceCount(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResources(
     *      org.sixdml.dbmanagement.SixdmlCollection)
     */
    public String[] queryResources(final SixdmlCollection collIn)
            throws XMLDBException {
        
        this.checkRemoteAccess();
        return this.engine.queryResources(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param queryIn
     * @return
     * @throws XMLDBException
     * @throws InvalidQueryException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResourcesByXPath(
     *      org.sixdml.dbmanagement.SixdmlCollection, java.lang.String)
     */
    public ResourceSet queryResourcesByXPath(final SixdmlCollection collIn,
                                             final String queryIn) 
            throws XMLDBException, InvalidQueryException {
        
        this.checkRemoteAccess();
        return this.engine.queryResourcesByXPath(collIn, queryIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param idIn
     * @param queryIn
     * @return
     * @throws XMLDBException
     * @throws InvalidQueryException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryResourceByXPath(
     *      org.sixdml.dbmanagement.SixdmlCollection, 
     *      java.lang.String, java.lang.String)
     */
    public ResourceSet queryResourceByXPath(final SixdmlCollection collIn,
                                            final String idIn, 
                                            final String queryIn) 
            throws XMLDBException, InvalidQueryException {

        this.checkRemoteAccess();
        return this.engine.queryResourceByXPath(collIn, idIn, queryIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryVCLSchema(
     *      _de.xplib.nexd.api.VirtualCollection)
     */
    public VCLSchema queryVCLSchema(final VirtualCollection collIn)
            throws XMLDBException {

        this.checkRemoteAccess();
        return this.engine.queryVCLSchema(collIn);
    }

    /**
     * <Some description here>
     * 
     * @param collIn
     * @param idIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryPCVResource(
     *      _de.xplib.nexd.api.VirtualCollection, java.lang.String)
     */
    public PCVResource queryPCVResource(final VirtualCollection collIn, 
                                        final String idIn)
            throws XMLDBException {
        
        this.checkRemoteAccess();
        return this.engine.queryPCVResource(collIn, idIn);
    }
    
    /**
     * Checks that the current client can execute a operation.
     * 
     * @throws XMLDBException If not allowed.
     */
    protected void checkRemoteAccess() throws XMLDBException {
        if (this.clientAddress == null) {
            throw new XMLDBException(
                    ErrorCodes.VENDOR_ERROR, "No remote address");
        }
        if (!this.allowRemote && !this.address.equals(this.clientAddress)) {
            throw new XMLDBException(
                    ErrorCodes.VENDOR_ERROR, 
                    "Operations are only allowed from the local system["
                    + this.address.getHostAddress() + "].");            
        }
    }
    
    /**
     * Checks that the current client can execute a change operation.
     * 
     * @throws XMLDBException If not allowed.
     */
    protected void checkRemoteChangeAccess() throws XMLDBException {
        
        this.checkRemoteAccess();
        
        if (!this.allowRemoteChanges 
                && !this.address.equals(this.clientAddress)) {
            throw new XMLDBException(
                    ErrorCodes.VENDOR_ERROR, 
                    "Change operations are only allowed from the local system["
                    + this.address.getHostAddress() + "].");            
        }
    }

}
