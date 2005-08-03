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
 * $Log$
 */
package de.xplib.nexd.engine.comm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.sixdml.SixdmlResourceSet;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.exceptions.InvalidCollectionDocumentException;
import org.sixdml.exceptions.InvalidQueryException;
import org.sixdml.exceptions.InvalidSchemaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.XMLResource;

import de.fmui.spheon.jsoap.Entry;
import de.fmui.spheon.jsoap.Envelope;
import de.fmui.spheon.jsoap.ParamBag;
import de.fmui.spheon.jsoap.RPCCall;
import de.fmui.spheon.jsoap.SoapArray;
import de.fmui.spheon.jsoap.SoapConfig;
import de.fmui.spheon.jsoap.SoapException;
import de.fmui.spheon.jsoap.transport.http.SoapHttpClient;
import de.xplib.nexd.api.InvalidParentCollectionException;
import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.VirtualResource;
import de.xplib.nexd.api.pcvr.AbstractPCVRFactory;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.api.util.Base64;
import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLParserI;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VariableExistsException;
import de.xplib.nexd.comm.ConnectionException;
import de.xplib.nexd.comm.MaxConnectionException;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.comm.SessionConfigurationException;
import de.xplib.nexd.comm.SessionI;
import de.xplib.nexd.engine.VCLHelper;
import de.xplib.nexd.engine.xapi.AbstractCollection;
import de.xplib.nexd.engine.xapi.CollectionImpl;
import de.xplib.nexd.engine.xapi.VirtualCollectionImpl;
import de.xplib.nexd.engine.xapi.VirtualResourceImpl;
import de.xplib.nexd.engine.xapi.XMLResourceImpl;
import de.xplib.nexd.engine.xapi.pcvr.PCVResourceImpl;
import de.xplib.nexd.xml.DOMDocumentI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision$
 */
public class SOAPSession implements SessionI, NEXDEngineI {
    
    /**
     * Comment for <code>URN</code>
     */
    private static final String URN = "urn:NEXDEngineService";
    
    /**
     * Comment for <code>sc</code>
     */
    private SoapConfig sc;
    
    /**
     * Comment for <code>shc</code>
     */
    private SoapHttpClient shc;
    
    /**
     * Comment for <code>remoteURL</code>
     */
    private URL remoteURL;
    
    /**
     * @param hostIn The remote nexd database host.
     * @param portIn The port where the nexd db waits.
     */
    SOAPSession(final String hostIn, final String portIn) {
        super();
        
        try {
            sc = new SoapConfig();
            
            shc = new SoapHttpClient(sc);
            
            remoteURL = new URL("http://" + hostIn + ":" + portIn);
            
            String nullStr = null;
            sc.addToConfig(nullStr);
            
            System.setProperty(
                    VCLParserI.VCL_PARSER_KEY, 
            "de.xplib.nexd.engine.xapi.vcl.VCLParserImpl");
            
            System.setProperty(
                    AbstractPCVRFactory.FACTORY_KEY,
            "de.xplib.nexd.engine.xapi.pcvr.PCVRFactoryImpl");
            
            System.setProperty(
                    "javax.xml.parsers.DocumentBuilderFactory",
            "de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl");
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * <Some description here>
     * 
     * @return
     * @throws ConnectionException
     * @throws MaxConnectionException
     * @throws SessionConfigurationException
     * @see de.xplib.nexd.comm.SessionI#getEngine()
     */
    public NEXDEngineI getEngine() throws ConnectionException,
            MaxConnectionException, SessionConfigurationException {

        return this;
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
        
        RPCCall call = new RPCCall(sc, URN, "beginTransaction");
        
        this.buildCollectionEnv(call, collIn);
        return (String) this.invokeWithResult(call).get(0);
    }
    
    /**
     * <Some description here>
     * 
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#close()
     */
    public void close() throws XMLDBException {
        // Nothing todo in remote mode
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
        
        RPCCall call = new RPCCall(sc, URN, "commitTransaction");
        
        this.buildCollectionEnv(call, collIn);
        this.invoke(call);

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
        
        RPCCall call = new RPCCall(sc, URN, "containsId");
        
        this.buildCollectionEnv(call, collIn, idIn);
        return ((Boolean) this.invokeWithResult(call).get(0)).booleanValue();
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
                               final String nameIn) throws XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "dropCollection");
        
        this.buildCollectionEnv(call, collIn, nameIn);
        this.invoke(call);
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
        
        RPCCall call = new RPCCall(sc, URN, "dropCollectionSchema");
        
        this.buildCollectionEnv(call, collIn);
        this.invoke(call);
    }
    
    /**
     * <Some description here>
     * 
     * @param collIn
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#dropCollectionStylesheet(
     *      de.xplib.nexd.api.VirtualCollection)
     */
    public void dropCollectionStylesheet(final VirtualCollection collIn)
            throws XMLDBException {

        RPCCall call = new RPCCall(sc, URN, "dropCollectionStylesheet");
        
        this.buildCollectionEnv(call, (VirtualCollection) collIn);
        this.invoke(call);
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
                             final Resource resIn)
            throws XMLDBException {
        
        
        RPCCall call = new RPCCall(sc, URN, "dropResource");
        
        this.buildCollectionEnv(call, collIn);
        this.buildResourceEnv(call, resIn);
        this.invoke(call);
    }
    
    /**
     * <Some description here>
     * 
     * @param username
     * @param password
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#open(
     *      java.lang.String, java.lang.String)
     */
    public void open(final String username, final String password) 
            throws XMLDBException {
        // Nothing todo in remote mode
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
            final SixdmlCollection parentIn,
            final String nameIn) throws XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "queryChildCollection");
        
        this.buildCollectionEnv(call, parentIn);
        
        try {
            Entry method = call.getEnvelope().getBody().getFirstChild();
            Entry name   = new Entry(method, "name");
            
            name.setAttribute("xsi:type", "xsd:string");
            name.setValue(nameIn);
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
        return this.invokeWithCollResult(call);
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
        
        RPCCall call = new RPCCall(sc, URN, "queryCollection");
        try {
            call.setParameter("cp", collPath, String.class);
            
            return this.invokeWithCollResult(call);
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
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
        
        RPCCall call = new RPCCall(sc, URN, "queryCollectionCount");
        
        this.buildCollectionEnv(call, collIn);
        ParamBag bag = this.invokeWithResult(call);
                
        return ((Integer) bag.get(0)).intValue();
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
        
        RPCCall call = new RPCCall(sc, URN, "queryCollections");
        
        this.buildCollectionEnv(call, collIn);
        ParamBag bag = this.invokeWithResult(call);
        
        SoapArray sarray = ((SoapArray) bag.get(0));
        
        String[] names = new String[sarray.getLength()];
        for (int i = 0; i < names.length; i++) {
            names[i] = (String) sarray.get(i); 
        }
        return names;
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
        
        RPCCall call = new RPCCall(sc, URN, "queryCollectionSchema");
        this.buildCollectionEnv(call, collIn);
        
        return (String) this.invokeWithResult(call).get(0);
    }
    
    
    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryCollectionStylesheet(
     *      de.xplib.nexd.api.VirtualCollection)
     */
    public Node queryCollectionStylesheet(final VirtualCollection collIn)
            throws XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "queryCollectionStylesheet");
        
        this.buildCollectionEnv(call, collIn);
        this.invoke(call);
        
        return null;
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
        
        RPCCall call = new RPCCall(sc, URN, "queryParentCollection");
        
        this.buildCollectionEnv(call, childIn);
        return this.invokeWithCollResult(call);
    }
    
    /**
     * <Some description here>
     * 
     * @param collIn
     * @param idIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryPCVResource(
     *      de.xplib.nexd.api.VirtualCollection, java.lang.String)
     */
    public PCVResource queryPCVResource(final VirtualCollection collIn, 
                                        final String idIn)
            throws XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "queryPCVResource");
        
        this.buildCollectionEnv(call, (SixdmlCollection) collIn);
        
        try {
            Entry method = call.getEnvelope().getBody().getFirstChild();
            Entry name   = new Entry(method, "id");
            
            name.setAttribute("xsi:type", "xsd:string");
            name.setValue(idIn);
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
        Envelope renv = this.invoke(call);
        
        return (PCVResource) this.createResource(
                renv.getBody().getFirstChild().getFirstChild(),
                (SixdmlCollection) collIn);
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
                                  final String idIn) throws XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "queryResource");
        
        this.buildCollectionEnv(call, collIn);
        try {
            Entry method = call.getEnvelope().getBody().getFirstChild();
            Entry name   = new Entry(method, "id");
            
            name.setAttribute("xsi:type", "xsd:string");
            name.setValue(idIn);
            
            Entry resp   = this.invoke(call).getBody().getFirstChild();
            Entry result = resp.getFirstChild();
            
            return this.createResource(result, collIn);
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
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
        
        RPCCall call = new RPCCall(sc, URN, "queryResourceByXPath");
        
        this.buildCollectionEnv(call, collIn);
        
        try {
            Entry method = call.getEnvelope().getBody().getFirstChild();
            
            Entry name  = new Entry(method, "id");
            name.setAttribute("xsi:type", "xsd:string");
            name.setValue(idIn);
            
            Entry xpath  = new Entry(method, "xpath");
            xpath.setAttribute("xsi:type", "xsd:string");
            xpath.setValue(queryIn);
            
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
        return this.invokeWithResSetResult(call, collIn);
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
        
        RPCCall call = new RPCCall(sc, URN, "queryResourceCount");
        
        this.buildCollectionEnv(call, collIn);
        return ((Integer) this.invokeWithResult(call).get(0)).intValue();
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
        
        RPCCall call = new RPCCall(sc, URN, "queryResources");
        this.buildCollectionEnv(call, collIn);
        
        ParamBag bag     = this.invokeWithResult(call);
        SoapArray sarray = (SoapArray) bag.get(0);
        
        String[] names = new String[sarray.getLength()];
        for (int i = 0; i < names.length; i++) {
            names[i] = (String) sarray.get(i);
        }
        return names;
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
        
        RPCCall call = new RPCCall(sc, URN, "queryResourcesByXPath");
        
        this.buildCollectionEnv(call, collIn);
        
        try {
            Entry method = call.getEnvelope().getBody().getFirstChild();
            Entry xpath  = new Entry(method, "xpath");
            
            xpath.setAttribute("xsi:type", "xsd:string");
            xpath.setValue(queryIn);
            
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
        return this.invokeWithResSetResult(call, collIn);
    }
    
    /**
     * <Some description here>
     * 
     * @param collIn
     * @return
     * @throws XMLDBException
     * @see de.xplib.nexd.comm.NEXDEngineI#queryVCLSchema(
     *      de.xplib.nexd.api.VirtualCollection)
     */
    public VCLSchema queryVCLSchema(final VirtualCollection collIn)
            throws XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "queryVCLSchema");
        
        this.buildCollectionEnv(call, (SixdmlCollection) collIn);
        
        Envelope renv = this.invoke(call);
        
        return (VCLSchema) this.createResource(
                renv.getBody().getFirstChild().getFirstChild(), 
                (SixdmlCollection) collIn);
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
                                             final URL schemaIn, 
                                             final URL xslIn)
            throws InvalidCollectionReferenceException,
                   InvalidParentCollectionException, 
                   InvalidQueryException,
                   InvalidVCLSchemaException, 
                   IOException, 
                   SAXException,
                   UndeclaredVariableException, 
                   VariableExistsException,
                   XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "storeCollection");
        
        this.buildCollectionEnv(call, collIn, nameIn);
        
        try {
            Entry method = call.getEnvelope().getBody().getFirstChild();
            
            String url = schemaIn.getProtocol() + "://" + schemaIn.getHost();
            if (schemaIn.getPort() != -1) {
                url += ":" + schemaIn.getPort();
            }
            url += schemaIn.getPath();
            
            Entry schema = new Entry(method, "schema");
            schema.setAttribute("xsi:type", "xsd:string");
            schema.setValue(url);
            
            url = xslIn.getProtocol() + "://" + xslIn.getHost();
            if (xslIn.getPort() != -1) {
                url += ":" + xslIn.getPort();
            }
            url += xslIn.getPath();
            
            Entry xsl = new Entry(method, "xsl");
            xsl.setAttribute("xsi:type", "xsd:string");
            xsl.setValue(url);
            
            return (VirtualCollection) this.invokeWithCollResult(call);
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
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
                                             final URL schemaIn)
            throws InvalidCollectionReferenceException,
                   InvalidParentCollectionException, 
                   InvalidQueryException,
                   InvalidVCLSchemaException, 
                   IOException, 
                   SAXException,
                   UndeclaredVariableException, 
                   VariableExistsException,
                   XMLDBException {

        RPCCall call = new RPCCall(sc, URN, "storeCollection");
        
        this.buildCollectionEnv(call, collIn, nameIn);
        
        try {
            Entry method = call.getEnvelope().getBody().getFirstChild();
            
            String url = schemaIn.getProtocol() + "://" + schemaIn.getHost();
            if (schemaIn.getPort() != -1) {
                url += ":" + schemaIn.getPort();
            }
            url += schemaIn.getPath();
            
            Entry schema = new Entry(method, "schema");
            schema.setAttribute("xsi:type", "xsd:string");
            schema.setValue(url);
            
            return (VirtualCollection) this.invokeWithCollResult(call);
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
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
        
        RPCCall call = new RPCCall(sc, URN, "storeCollection");
        
        this.buildCollectionEnv(call, collIn, nameIn);
        
        return this.invokeWithCollResult(call);
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
                                      final URL schemaIn)
            throws InvalidCollectionDocumentException, InvalidSchemaException,
            IOException, XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "storeCollectionSchema");
        
        String url = schemaIn.getProtocol() + "://" + schemaIn.getHost();
        if (schemaIn.getPort() != -1) {
            url += ":" + schemaIn.getPort();
        }
        url += schemaIn.getPath();
        
        
        System.out.println(url);
        
        this.buildCollectionEnv(call, collIn, url);
        
        this.invoke(call);
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
     *      de.xplib.nexd.api.VirtualCollection, java.net.URL)
     */
    public void storeCollectionStylesheet(final VirtualCollection collIn, 
                                          final URL xslIn)
            throws IOException, SAXException, XMLDBException {

        RPCCall call = new RPCCall(sc, URN, "storeCollectionStylesheet");
        

        String url = xslIn.getProtocol() + "://" + xslIn.getHost();
        if (xslIn.getPort() != -1) {
            url += ":" + xslIn.getPort();
        }
        url += xslIn.getPath();
        
        this.buildCollectionEnv(call, collIn, url);
        this.invoke(call);

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
                              SixdmlResource resIn) throws XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "storeResource");
        
        this.buildCollectionEnv(call, collIn);
        this.buildResourceEnv(call, resIn);
        
        this.invoke(call);
        
        resIn = (SixdmlResource) this.queryResource(collIn, resIn.getId());
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
                               final Resource resIn)
            throws XMLDBException {
        
        RPCCall call = new RPCCall(sc, URN, "updateResource");
        
        this.buildCollectionEnv(call, collIn);
        this.buildResourceEnv(call, resIn);
        
        this.invoke(call);
    }
    
    /**
     * @param callIn ..
     * @param collIn ..
     * @throws XMLDBException ..
     */
    private void buildCollectionEnv(final RPCCall callIn, 
                                    final SixdmlCollection collIn)
            throws XMLDBException {
        
        try {
            Entry e = callIn.getEnvelope().getBody().getRootChild();
            Entry coll = new Entry(e, "coll");
            coll.setAttribute("xmlns:ns6", "http://nexd.xplib.de");
            coll.setAttribute("xsi:type", "ns6:SixdmlCollection");
            
            coll.setAttribute("iid", collIn.getProperty("iid"));
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * @param callIn ..
     * @param collIn ..
     * @throws XMLDBException ..
     */
    private void buildCollectionEnv(final RPCCall callIn, 
                                    final VirtualCollection collIn)
            throws XMLDBException {
        
        try {
            Entry e = callIn.getEnvelope().getBody().getRootChild();
            Entry coll = new Entry(e, "collection");
            coll.setAttribute("xmlns:ns6", "http://nexd.xplib.de");
            coll.setAttribute("xsi:type", "ns6:VirtualCollection");
            
            coll.setAttribute("iid", collIn.getProperty("iid"));
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * @param callIn ..
     * @param collIn ..
     * @param nameIn ..
     * @throws XMLDBException ..
     */
    private void buildCollectionEnv(final RPCCall callIn, 
                                    final SixdmlCollection collIn,
                                    final String nameIn) throws XMLDBException {
        
        this.buildCollectionEnv(callIn, collIn);
        
        try {
            Entry method = callIn.getEnvelope().getBody().getFirstChild();
            Entry name   = new Entry(method, "name");
            
            name.setAttribute("xsi:type", "xsd:string");
            name.setValue(nameIn);
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * @param callIn ..
     * @param collIn ..
     * @param nameIn ..
     * @throws XMLDBException ..
     */
    private void buildCollectionEnv(final RPCCall callIn, 
                                    final VirtualCollection collIn,
                                    final String nameIn) throws XMLDBException {
        
        this.buildCollectionEnv(callIn, collIn);

        
        try {
            Entry method = callIn.getEnvelope().getBody().getFirstChild();
            Entry name   = new Entry(method, "name");
            
            name.setAttribute("xsi:type", "xsd:string");
            name.setValue(nameIn);
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * @param callIn ..
     * @param resIn ..
     * @throws XMLDBException ..
     */
    public void buildResourceEnv(final RPCCall callIn, 
                                      final Resource resIn) 
            throws XMLDBException {
        
        
        try {
            Entry method   = callIn.getEnvelope().getBody().getFirstChild();
            Entry resource = new Entry(method, "resource");
            
            String data = "";
            String type;
            if (resIn instanceof XMLResource) {
                data = Base64.encodeBytes(
                        ((String) resIn.getContent()).getBytes());
                if (resIn instanceof VirtualResource) {
                    type = "VirtualResource";
                } else if (resIn instanceof SixdmlResource) {
                    type = "SixdmlResource";
                } else {
                    type = "XMLResource";
                }
            } else if (resIn instanceof BinaryResource) {
                type = "BinaryResource";
            } else {
                type = "Resource";
            }
            
            
            
            resource.setAttribute("xmlns:ns6", "http://nexd.xplib.de");
            resource.setAttribute("xsi:type", "ns6:" + type);
            
            Entry name = new Entry(resource, "name");
            name.setAttribute("xsi:type", "xsd:string");
            name.setValue(resIn.getId());
            
            Entry content = new Entry(resource, "content");
            content.setAttribute("xsi:type", "ns6:DomDocument");
            content.setValue(data);
            
            Entry resType = new Entry(resource, "resourceType");
            resType.setAttribute("xsi:type", "xsd:string");
            resType.setValue(type);
            
            Entry subType = new Entry(resource, "subType");
            subType.setAttribute("xsi:type", "xsd:string");
            subType.setValue(type);
            
            Entry iid   = new Entry(resource, "internalId");
            iid.setAttribute("xsi:type", "xsd:string");
            iid.setValue(resIn.getId());
            
            Entry docId = new Entry(resource, "documentId");
            
            if (resIn instanceof XMLResource) {
                Node node = ((XMLResource) resIn).getContentAsDOM();
                if (node instanceof DOMDocumentI) {
                    docId.setAttribute("xsi:type", "xsd:string");
                    docId.setValue(((DOMDocumentI) node).getDocumentId());
                }
            }
            
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
    }
    
    /**
     * @param entryIn ..
     * @param collIn ..
     * @return ..
     * @throws XMLDBException ..
     */
    private Resource createResource(final Entry entryIn,
                                    final SixdmlCollection collIn) 
            throws XMLDBException {
        
        Resource res = null;
        if (entryIn.getAttribute("xsi:nil") == null
                || !entryIn.getAttribute("xsi:nil").equals("1")) {
            
            String type = entryIn.getChild("resourceType").getValue();
            if (type.equals(XMLResource.RESOURCE_TYPE)) {
                
                String content = entryIn.getChild("content").getValue();
                
                Document doc;
                try {
                    DocumentBuilder docBuilder = DocumentBuilderFactory
                                                 .newInstance()
                                                 .newDocumentBuilder();
                    
                    doc = docBuilder.parse(
                            new ByteArrayInputStream(Base64.decode(content)));
                } catch (Exception e) {
                    throw new XMLDBException(
                            ErrorCodes.VENDOR_ERROR, e.getMessage());
                }
                
                if (doc instanceof DOMDocumentI) {
                    ((DOMDocumentI) doc).setDocumentId(
                            entryIn.getChild("documentId").getValue());
                }
                
                if (collIn instanceof VirtualCollection) {
                    
                    String subType = entryIn.getChild("subType").getValue();
                    if (subType.equals("VCLSchema")) {
                        res = VCLHelper.getVCLSchema(doc);
                    } else if (subType.equals("PCVResource")) {
                        res = new PCVResourceImpl(
                                (VirtualCollection) collIn,
                                doc, entryIn.getChild("name").getValue());
                    } else {
                        res = new VirtualResourceImpl(
                            	(VirtualCollectionImpl) collIn,
                            	doc, entryIn.getChild("name").getValue());
                    }
                } else {
                    XMLResourceImpl xres = new XMLResourceImpl(
                            (CollectionImpl) collIn, 
                            entryIn.getChild("name").getValue());
                    
                    xres.setContentAsDOM(doc);
                    
                    res = xres;
                }
                
                if (entryIn.getName().equals("ResourceSetItem")) {
                    Element docElem = doc.getDocumentElement();
                    
                    docElem.setAttribute(
                            "xmlns:" + QUERY_RESULT_PREFIX,
                            QUERY_RESULT_PREFIX);
                    
                    docElem.setAttribute(
                            QUERY_RESULT_PREFIX + ":" + QUERY_RESULT_ID, 
                            entryIn.getChild("internalId").getValue());
                }
                
            } else if (type.equals(BinaryResource.RESOURCE_TYPE)) {
                throw new XMLDBException(ErrorCodes.NOT_IMPLEMENTED);
            }
        }
        return res;
    }
    
    /**
     * @param callIn ..
     * @return ..
     * @throws XMLDBException ..
     */
    private ParamBag invokeWithResult(final RPCCall callIn) 
            throws XMLDBException {
        
        Envelope renv = this.invoke(callIn);
        
        try {
            return callIn.getResults(renv);
        } catch (SoapException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * @param callIn ..
     * @param collIn ..
     * @return ,.,
     * @throws XMLDBException ..
     */
    private ResourceSet invokeWithResSetResult(final RPCCall callIn,
                                               final SixdmlCollection collIn) 
            throws XMLDBException {
        
        
        Envelope renv = this.invoke(callIn);
        
        Entry response = ((Entry) renv.getBody().getChildren().get(0));
        Entry result   = response.getFirstChild();
        
        SixdmlResourceSet resSet = new SixdmlResourceSet();
        
        List resSetItems = result.getChildren();
        if (resSetItems != null) {
            for (int i = 0, s = resSetItems.size(); i < s; i++) {
            
            	Entry item = (Entry) resSetItems.get(i);
            	resSet.addResource(this.createResource(item, collIn));
        	}
        }
        
        return resSet;
    }
    
    /**
     * @param callIn ..
     * @return ..
     * @throws XMLDBException ..
     */
    private SixdmlCollection invokeWithCollResult(final RPCCall callIn) 
        throws XMLDBException {
        
        Envelope renv = this.invoke(callIn);
        
        List l = ((Entry) renv.getBody().getChildren().get(0)).getChildren();
        
        Entry e = (Entry) l.get(0);
        
        AbstractCollection coll = null;
        if (e.getAttribute("xsi:nil") == null 
                || !e.getAttribute("xsi:nil").equals("1")) {
        
            String name  = e.getChild("name").getValue();
            short type   = Short.parseShort(e.getChild("type").getValue());
            //String iid   = e.getChild("internalId").getValue();
            String path  = e.getChild("path").getValue();
            String trans = e.getChild("transaction").getValue();
        
            if (type == 0) {
                coll = new CollectionImpl(this, null);
            } else {
                coll = new VirtualCollectionImpl(this, null);
            }
            coll.setProperty("name", name);
            //coll.setProperty("iid", iid);
            coll.setProperty("path", path);
            coll.setProperty("transid", trans);
        }
        return coll;
    }
    
    /**
     * @param callIn ..
     * @return ..
     * @throws XMLDBException ..
     */
    private Envelope invoke(final RPCCall callIn) throws XMLDBException {
        try {
            Envelope ret = shc.invokeHTTP(remoteURL, "", callIn.getEnvelope());
            
            if (ret.hasFault()) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR,
                        Integer.parseInt(ret.getFault().getFaultcode()),
                        ret.getFault().getFaultstring());
            }
            
            return ret;
        } catch (Exception e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    
}

