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
 * $Log: VirtualResourceManager.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sixdml.exceptions.InvalidQueryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualResource;
import de.xplib.nexd.api.pcvr.AbstractPCVRFactory;
import de.xplib.nexd.api.pcvr.PCVRCompiler;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLPreCompiler;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VariableExistsException;
import de.xplib.nexd.engine.xapi.CollectionImpl;
import de.xplib.nexd.engine.xapi.VirtualCollectionImpl;
import de.xplib.nexd.engine.xapi.vcl.VCLSchemaVisitorImpl;
import de.xplib.nexd.store.AbstractStorageFactory;
import de.xplib.nexd.store.InternalIdI;
import de.xplib.nexd.store.StorageCollectionI;
import de.xplib.nexd.store.StorageDocumentObjectI;
import de.xplib.nexd.store.StorageException;
import de.xplib.nexd.store.StorageI;
import de.xplib.nexd.store.StorageObjectI;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public final class VirtualResourceManager {
    
    /**
     * Used system logger.
     */
    private static final Log LOG = LogFactory.getLog(
            VirtualResourceManager.class);
    
    /**
     * Regular expression 
     */
    private static final Pattern PATTERN = Pattern.compile(
        "\\$([a-zA-Z0-9_]+)[^a-z^A-Z^0-9°_]*");

    /**
     * XPath query that identifies the first(root) reference in a 
     * <code>PCVResource</code> document.
     */
    private static final String ROOT_RESOURCE_XPATH =
        "/pcvr:schema/pcvr:collection/pcvr:resource/@ref";

	/**
	 * XPath query that find a variable element.
	 */
	private static final String VARIABLE_XPATH =
	    "//" + PCVResource.QNAME_VARIABLE + "[@name='";

    /**
     * The internal used Engine.
     * @clientCardinality 1
     * @clientRole engine
     * @directed true
     * @supplierCardinality 1
     */
    private final NEXDEngineImpl engine;
    
    /**
     * The internal used database.
     */
    private final StorageI storage;

    /**
     * Constructor.
     * 
     * @param engineIn The internal used Engine.
     * @param storageIn The internal used database.
     */
    protected VirtualResourceManager(final NEXDEngineImpl engineIn,
                                     final StorageI storageIn) {
        super();
        
        this.engine  = engineIn;
        this.storage = storageIn;
    }
    
    /**
     * Factory method that create a <code>VirtualResource</code>.
     * 
     * @param vcoll The context <code>VirtualCollection</code> instance.
     * @param pcvrColl The <code>CollectionImpl</code> that contains the pre
     *                 compiled resources. 
     * @param schemaIn The virtual collection language schema that describes the
     *               new collection. 
     * @throws InvalidCollectionReferenceException If the schema references a 
     *         not existing collection or a virtual collection.
     * @throws InvalidQueryException If the used xpath queries are not ok or 
     *         they use a not supported syntax.
     * @throws InvalidVCLSchemaException If the schema doesn't provide the 
     *         minimum requirements for a virtual collection language schema.
     * @throws SAXException ...
     * @throws UndeclaredVariableException If the schema uses not declared 
     *         variables.
     * @throws VariableExistsException If the schema has more than one variable
     *         with the same name.
     * @throws XMLDBException If any database specific error occures.
     */
    protected void create(final VirtualCollectionImpl vcoll,
                          final CollectionImpl pcvrColl,
                          final VCLSchema schemaIn) 
    		throws InvalidCollectionReferenceException,
                   InvalidQueryException,
                   InvalidVCLSchemaException,
                   UndeclaredVariableException,
                   VariableExistsException,
                   SAXException,
                   XMLDBException {
        
        try {
            
            StorageCollectionI svc = vcoll.getStorageCollection();
            StorageCollectionI spcvr = pcvrColl.getStorageCollection();

            VCLPreCompiler preCompiler = new VCLPreCompiler(
                    new VCLSchemaVisitorImpl(this.engine, vcoll));
            
            LOG.debug("Pre compiling vcl schema.");
            ResourceSet pcvrSet = preCompiler.compile(schemaIn);
            
            VirtualResource[] vres = 
                new VirtualResource[(int) pcvrSet.getSize()];
            
            PCVRCompiler pcvrCompiler = AbstractPCVRFactory
                                                .newInstance()
                                                .newPCVRCompiler(vcoll);
            
            LOG.debug("Compiling pcvr's");
            for (int i = 0; i < vres.length; i++) {
                LOG.debug("  " + i + " compiling...");
                vres[i] = pcvrCompiler.compile(
                        (PCVResource) pcvrSet.getResource(i));
            }
            
            AbstractStorageFactory sfactory = this.storage.getFactory();
            
            StorageDocumentObjectI sdObj;
            
            LOG.debug("Storing virtual resources");
            for (int i = 0; i < vres.length; i++) {
                
                PCVResource pcvr = (PCVResource) pcvrSet.getResource(i);
                
                LOG.debug("  " + i + " Storing pcv resource ...");
                
                sdObj = sfactory.createDocumentObject(
                        pcvr.getId(), (Document) pcvr.getContentAsDOM());
                this.storage.storeDocument(spcvr, sdObj);
                
                LOG.debug("  " + i + " Storing virtual resource ...");
                
                sdObj = sfactory.createDocumentObject(
                        vres[i].getId(), (Document) vres[i].getContentAsDOM());
                this.storage.storeDocument(svc, sdObj);
            }
            
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * Factory method that create a <code>VirtualResource</code>.
     * 
     * 
     * @param scollIn The context <code>StorageCollectionI</code> instance for
     *                the virtual collection.
     * @param vcsIn The virtual collection schema as a DOM <code>Document</code>
     * @param idIn The id for the new virtual resource.
     *               new collection. 
     * @throws InvalidCollectionReferenceException If the schema references a 
     *         not existing collection or a virtual collection.
     * @throws InvalidQueryException If the used xpath queries are not ok or 
     *         they use a not supported syntax.
     * @throws InvalidVCLSchemaException If the schema doesn't provide the 
     *         minimum requirements for a virtual collection language schema.
     * @throws SAXException ...
     * @throws UndeclaredVariableException If the schema uses not declared 
     *         variables.
     * @throws VariableExistsException If the schema has more than one variable
     *         with the same name.
     * @throws XMLDBException If any database specific error occures.
     * @throws StorageException If any database specific error occures.
     */
    protected void create(final StorageCollectionI scollIn,
            			  final Document vcsIn,
                          final String idIn) 
            throws InvalidCollectionReferenceException,
                   InvalidQueryException,
                   InvalidVCLSchemaException,
                   UndeclaredVariableException,
                   VariableExistsException,
                   SAXException,
                   StorageException,
                   XMLDBException {
        
        VirtualCollectionImpl vcoll = new VirtualCollectionImpl(
                this.engine, scollIn);

        LOG.debug("Create new VirtualResource for " + idIn);
        
        VCLSchema vcls = VCLHelper.getVCLSchema(vcsIn);
        
        VCLPreCompiler preCompiler = new VCLPreCompiler(
                new VCLSchemaVisitorImpl(
                        this.engine, vcoll, idIn, scollIn.getPath()));
        
        PCVResource pcvrR = (PCVResource) preCompiler.compile(
                vcls).getResource(0);
        
        StorageDocumentObjectI sdObj = this.storage
                                           .getFactory()
                                           .createDocumentObject(
                pcvrR.getId(), (Document) pcvrR.getContentAsDOM());
        
        PCVRCompiler compiler = AbstractPCVRFactory
                                        .newInstance()
                                        .newPCVRCompiler(
                new VirtualCollectionImpl(null, scollIn));
        
        VirtualResource vres = compiler.compile(pcvrR);
        
        String pcvrCollPath = scollIn.getPath() + "/"
        + StorageI.PCVR_DATA_COLLECTION;
        
        this.storage.storeDocument(
                this.storage.queryCollection(
                        pcvrCollPath), sdObj);
        
        StorageDocumentObjectI virObj = this.storage.getFactory()
                                                    .createDocumentObject(
                pcvrR.getId(), (Document) vres.getContentAsDOM());
        this.storage.storeDocument(scollIn, virObj);
    }
    
    /**
     * Removes a <code>VirtualResource</code> or removes a part of it.
     * 
     * @param virCollIn The context <code>VirtualCollectionImpl</code> object.
     * @param pcvrCollIn The <code>CollectionImpl</code> that contains the
     *                   pre compiled virtual resources.
     * @param resIDIn The id of the resource that was delete.
     * @throws XMLDBException If any database specific error occures.
     */
    protected void delete(final VirtualCollectionImpl virCollIn,
                          final CollectionImpl pcvrCollIn,
                          final String resIDIn) throws XMLDBException {
        
        StorageCollectionI virColl  = virCollIn.getStorageCollection();
        StorageCollectionI pcvrColl = pcvrCollIn.getStorageCollection();
        
        try {
            this.storage.dropObject(
                    pcvrColl, this.storage.queryObject(
                            pcvrColl, resIDIn));

            this.storage.dropObject(
                    virColl, this.storage.queryObject(
                            virColl, resIDIn));
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
    }
    
    /**
     * Updates a virtual resource.
     *  
     * @param virCollIn The context VirtualCollectionImpl instance.
     * @param pcvrCollIn The collection with the pre compiled virtual resources.
     * @param subQuery Xpath query used to find referenced resources.
     * @param resPathIn The path of the resource that was changed. 
     * @throws SAXException ...
     * @throws XMLDBException If any database specific error occures.
     */
    protected void update(final VirtualCollectionImpl virCollIn,
                          final CollectionImpl pcvrCollIn,
                          final String subQuery,
                          final String resPathIn) 
            throws SAXException,
                   XMLDBException {
               
        
        try {
            StorageCollectionI virColl  = virCollIn.getStorageCollection();
            StorageCollectionI pcvrColl = pcvrCollIn.getStorageCollection();
            
            StorageObjectI[] soXPaths = this.storage.queryObjectsByXPath(
                    pcvrColl, subQuery);
            
            if (soXPaths.length == 0) {
                return;
            }
            
            AbstractStorageFactory sfactory = this.storage.getFactory();
            
            PCVRCompiler compiler = AbstractPCVRFactory
                                            .newInstance()
                                            .newPCVRCompiler(virCollIn);
            
            for (int j = 0; j < soXPaths.length; j++) {
                StorageDocumentObjectI sdObj = (StorageDocumentObjectI)
                		this.storage.queryObject(
                		        pcvrColl, 
                		        ((StorageDocumentObjectI) 
                		                soXPaths[j])
                		                .getContent().getDocumentId());
                
                Document doc = sdObj.getContent();
                NodeList resources = doc.getElementsByTagName(
                        PCVResource.QNAME_RESOURCE);
                
                for (int k = resources.getLength() - 1; k >= 0; k--) {
                    Element elem = (Element) resources.item(k);
                    if (elem.getAttribute(
                            PCVResource.ATTR_RESOURCE_REFERENCE)
                            .equals(resPathIn)) {
                        
                        elem.getParentNode().removeChild(elem);
                    }
                }
                
                VirtualResource vRes = null;
                vRes = compiler.compile(
                        doc, sdObj.getContent().getDocumentId());
                
                String did = ((StorageDocumentObjectI) 
                        soXPaths[j]).getContent().getDocumentId();

                InternalIdI internalId = this.storage.queryObject(
                        pcvrColl, did).getInternalId();
                
                StorageDocumentObjectI sdXPathObj = sfactory
                        .createDocumentObject(
                                internalId, did, ((StorageDocumentObjectI)
                                        soXPaths[j]).getContent());
                this.storage.updateDocument(pcvrColl, sdXPathObj);
                
                this.storage.dropObject(
                        virColl, this.storage.queryObject(
                                virColl, did));

                StorageDocumentObjectI virObj = this.storage
                                                    .getFactory()
                                                    .createDocumentObject(
                        did, (Document) vRes.getContentAsDOM());
                this.storage.storeDocument(virColl, virObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * Rebuilds a single virtual resource.
     * 
     * @param virCollIn The context virtual collection.
     * @param refCollIn The referenced collection
     * @param vcsIn The virtual collection language schema.
     * @param selectIn The collection select expression
     * @param resIdIn The id of the cahcnged resource. 
     * @throws InvalidCollectionReferenceException If the schema references a 
     *         not existing collection or a virtual collection.
     * @throws InvalidQueryException If the used xpath queries are not ok or 
     *         they use a not supported syntax.
     * @throws InvalidVCLSchemaException If the schema doesn't provide the 
     *         minimum requirements for a virtual collection language schema.
     * @throws SAXException ...
     * @throws UndeclaredVariableException If the schema uses not declared 
     *         variables.
     * @throws VariableExistsException If the schema has more than one variable
     *         with the same name.
     * @throws XMLDBException If any database specific error occures.
     * @throws SAXException ... 
     * @throws StorageException If any database specific error occures.
     */
    protected void rebuildVirtualResource(final VirtualCollectionImpl virCollIn,
                                          final CollectionImpl refCollIn,
                                          final Document vcsIn,
                                          final String selectIn,
                                          final String resIdIn) 
            throws InvalidCollectionReferenceException,
                   InvalidQueryException,
                   InvalidVCLSchemaException,
                   UndeclaredVariableException,
                   VariableExistsException,
                   SAXException,
                   StorageException,
                   XMLDBException {
        
        StorageCollectionI refColl = refCollIn.getStorageCollection();
        
        String pcvrPath = refColl.getPath() + "/"
                        + StorageI.PCVR_DATA_COLLECTION;
        
        LOG.debug("\n####################\n"
                + selectIn + "\n"
                + "Implement sub select"
                + "\n####################");
        
        Matcher matcher = PATTERN.matcher(selectIn);
        
        StorageCollectionI sVirColl = virCollIn.getStorageCollection();
        
        StorageCollectionI pcvrColl = this.storage.queryCollection(pcvrPath);
        
        // skip processing, this doesn't match
        if (!matcher.find()) {
            LOG.info("\n####################\n"
                    + selectIn + "\n"
                    + "Implement sub select"
                    + "\n####################");
            /*
            if (true) {
                
                LOG.info("virCollIn " + virCollIn + "  " + virCollIn.getName());
                LOG.info("refCollIn " + refCollIn + "  " + refCollIn.getName());
                LOG.info("pcvrColl " + pcvrColl + "  " + pcvrColl.getPath());
                
                return;
            }*/
                                    
            String[] names = this.storage.queryObjects(refColl);
            for (int i = 0; i < names.length; i++) {
                this.storage.dropObject(refColl, this.storage.queryObject(refColl, names[i]));
                
                this.storage.dropObject(pcvrColl, this.storage.queryObject(pcvrColl, names[i]));
            }
            
            this.create(new VirtualCollectionImpl(this.engine, refColl), new CollectionImpl(this.engine, pcvrColl), VCLHelper.getVCLSchema(vcsIn));
            
            return;
        }
        
         
        
        Map map = this.getXPathMap(pcvrColl, matcher, selectIn);
        
        Iterator docIDs = map.keySet().iterator();
        while (docIDs.hasNext()) {
            String docId = (String) docIDs.next();
            String xpath = (String) map.get(docId);
            
            if (this.storage.queryObjectByXPath(
                    sVirColl, resIdIn, xpath)
                    .length != 0) {
                
                StorageObjectI[] res = this.storage.queryObjectByXPath(
                        pcvrColl, docId, ROOT_RESOURCE_XPATH);
                
                if (res.length != 0) {
                    
                    String ref = this.getReferenceName(
                            (StorageDocumentObjectI) res[0]);
                    
                    this.storage.dropObject(
                            pcvrColl, this.storage
                            .queryObject(
                                    pcvrColl, docId));
                    this.storage.dropObject(
                            refColl, this.storage.queryObject(refColl, docId));
                    
                    this.create(refColl, vcsIn, ref);
                }
            }
        }
        
    }
    
    /**
     * Returns a <code>Map</code> that contains the input <code>selectIn</code>,
     * but the variable references in this String are replaced with its concrete
     * values.
     *  
     * @param pcvrColl The collection with the pre compiled virtual resources.
     * @param matcher The used regular expression matcher.
     * @param selectIn The xpath select query.
     * @return Map with the prepared xpath query.
     * @throws StorageException If any database specidif error occures.
     */
    private Map getXPathMap(final StorageCollectionI pcvrColl, 
                            final Matcher matcher,
                            final String selectIn) throws StorageException {
        
        FastHashMap map = new FastHashMap();
        map.setFast(true);
        
        do {
            
            // extract the placeholder form the matcher instance.
            String key = matcher.group(1);
            
            // complete the xpath query with the key and execute it.
            StorageObjectI[] sRes = this.storage.queryObjectsByXPath(
                    pcvrColl, VARIABLE_XPATH + key + "']");
            
            // replace the variable references with its values.
            for (int k = 0; k < sRes.length; k++) {
                StorageDocumentObjectI doc = (StorageDocumentObjectI) sRes[k];
                
                String docId = doc.getContent().getDocumentId();
                
                String parsed = selectIn;
                if (map.containsKey(docId)) {
                    parsed = (String) map.get(docId);
                }
                
                parsed = parsed.replaceAll("\\$" + key,
                        this.decodeValue(
                                doc.getContent()
                                   .getDocumentElement()
                                   .getAttribute("value")));
                
                map.put(docId, parsed);
            }
            
        } while (matcher.find());
        
        return map;
    }
    
    /**
     * Returns the reference name of the root resource referece.
     * 
     * @param docIn The storage document that will be analysed.
     * @return The name of the root reference.
     */
    private String getReferenceName(final StorageDocumentObjectI docIn) {
        return new File(docIn.getContent()
                             .getDocumentElement()
                             .getAttribute("ref")).getName();
    }
    
    /**
     * Decodes a String with the base64 codec.
     * 
     * @param valueIn The encoded values.
     * @return The decoded value.
     */
    private String decodeValue(final String valueIn) {
        return new String(Base64.decodeBase64(valueIn.getBytes()));
    }

}
