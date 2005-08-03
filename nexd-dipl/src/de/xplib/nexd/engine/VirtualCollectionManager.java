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
 * $Log: VirtualCollectionManager.java,v $
 * Revision 1.2  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.XMLSerializer;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.exceptions.InvalidQueryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLCollectionReference;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VariableExistsException;
import de.xplib.nexd.engine.xapi.CollectionImpl;
import de.xplib.nexd.engine.xapi.VirtualCollectionImpl;
import de.xplib.nexd.engine.xapi.XMLResourceImpl;
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
public class VirtualCollectionManager {
    
    /**
     * Used system logger.
     */
    private static final Log LOG = LogFactory.getLog(
            VirtualCollectionManager.class);
    
    /**
     * Base XPath query prefix, which addresses the root resource in a 
     * pre compiled virtual resource document.
     * 
     * <code>
     *  /pcvr:schema/pcvr:collection/pcvr:resource
     * </code>
     */
    private static final String BASE_XPATH = "/" + PCVResource.QNAME_SCHEMA
                                           + "/" + PCVResource.QNAME_COLLECTION
                                           + "/" + PCVResource.QNAME_RESOURCE;
    
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
     * The class that handles all virtual resource changes.
     * @clientCardinality 1
     * @clientRole vResManager
     * @directed true
     * @link aggregation
     * @supplierCardinality 1
     */
    private final VirtualResourceManager resManager;

    /**
     * Constructor.
     * 
     * @param engineIn The internal used Engine.
     * @param storageIn The internal used database.
     */
    protected VirtualCollectionManager(final NEXDEngineImpl engineIn,
                                       final StorageI storageIn) {
        super();

        this.engine  = engineIn;
        this.storage = storageIn;
        
        this.resManager = new VirtualResourceManager(engineIn, storageIn);
    }
    
    /**
     * Factory method that create a <code>VirtualCollection</code>.
     * 
     * @param path The path for the new <code>VirtualCollection</code>.
     * @param schema The virtual collection language schema that describes the
     *               new collection. 
     * @return The newly created <code>VirtualCollection</code>.
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
    protected VirtualCollection create(final String path, 
                                       final VCLSchema schema) 
            throws InvalidCollectionReferenceException,
                   InvalidQueryException,
                   InvalidVCLSchemaException,
                   SAXException,
                   UndeclaredVariableException,
                   VariableExistsException,
                   XMLDBException {
        
        CollectionLockVisitor clv = new CollectionLockVisitor(this.engine);
        
        try {
            // Try lock here, so we can be sure that all collections will
            // be relased in the finally block.
            clv.lock(schema);
            
            // We must clone here because the visitor changes the original
            StorageDocumentObjectI sdObj = this.storage
                                               .getFactory()
                                               .createDocumentObject(
                    StorageI.VCL_SCHEMA_RESOURCE, 
                    (Document) schema.getContentAsDOM().cloneNode(true));
            
            LOG.debug("Storing vcl collection");
            StorageCollectionI svc = this.storage.storeCollection(
                    path, StorageCollectionI.TYPE_VIRTUAL_COLLECTION); 
            VirtualCollectionImpl vcoll = new VirtualCollectionImpl(
                    this.engine, svc);
                        
            LOG.debug("Storing pcvr collection");
            StorageCollectionI spcvr = this.storage.storeCollection(
                    path + "/" + StorageI.PCVR_DATA_COLLECTION, 
                    StorageCollectionI.TYPE_COLLECTION);
            CollectionImpl pcvr = new CollectionImpl(this.engine, spcvr);
            
//          Create schema collection and store schema
            StorageCollectionI ssc = this.storage.storeCollection(
                    path + "/" + StorageI.VCL_SCHEMA_COLLECTION, 
                    StorageCollectionI.TYPE_COLLECTION);
            
            LOG.debug("Storing vcl schema resource");
            this.storage.storeDocument(ssc, sdObj);
            
//          Store all collection 2 virtual collection references.
            LOG.debug("Storing collection references");
            SixdmlCollection[] colls = clv.getCollections();
            for (int i = 0; i < colls.length; i++) {
                LOG.debug("  " + i + " Storing .... " + colls[i].getName());
                this.storage.storeCollectionReference(
                        this.engine.getStorageCollection(colls[i]), svc);
                
            }
            
            this.resManager.create(vcoll, pcvr, schema);
/*
            // At least we store a not null stylesheet
            if (xslIn != null) {
                this.storeCollectionStylesheet(vcoll, xslIn);
            }
           */
            return vcoll;
        } catch (StorageException e) {
            e.printStackTrace();
            // if something goes wrong try to delete the virtual collection and 
            // all its stuff
            try {
                this.storage.dropCollection(path);
            } catch (StorageException e1) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, e1.getMessage());
            }
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } finally {
            clv.release();
        }
    }
    
    /**
     * Updates a <code>VirtualCollection</code> that depends on the given 
     * <code>CollectionImpl</code> and <code>XMLResourceImpl</code>.
     * 
     * @param ctxCollIn The context <code>CollectionImpl</code> object.
     * @param resIn The resource that was changed, may be it was deleted or the
     *              structure was modified. 
     * @throws XMLDBException If any database specific error occures.
     */
    protected void update(final CollectionImpl ctxCollIn,
            		      final XMLResourceImpl resIn) 
            throws XMLDBException {
        
        try {
            Object result = this.storage.queryObject(
                    ctxCollIn.getStorageCollection(), resIn.getId());
            

            
            LOG.info("Input Collection: " + ctxCollIn);
            
            if (result == null) {
                LOG.info("updateDelete()");
                this.updateDelete(ctxCollIn, resIn);
            } else {
                LOG.info("updateInsert()");
                this.updateInsert(ctxCollIn, resIn);
            }
            
            StorageCollectionI ctxColl = ctxCollIn.getStorageCollection();
            
            StorageCollectionI[] cref = this.storage.queryCollectionReferences(
                    ctxColl);
            
            for (int i = 0; i < cref.length; i++) {
                
                VirtualCollectionImpl virColl = new VirtualCollectionImpl(
                        this.engine, cref[i]);
                
            Node xsl = this.engine.queryCollectionStylesheet(virColl);
            
            LOG.info("Trying xsl update " + ctxCollIn.getName() + " : " + xsl);
            
            if (xsl != null && xsl instanceof Document) {
                
                LOG.info("Update xsl");
                
                this.engine.dropCollectionStylesheet(virColl);

                File tmp = File.createTempFile("tmp_", ".xsl");
                tmp.deleteOnExit();
                
                FileWriter fw = new FileWriter(tmp);
                
                XMLSerializer xser = new XMLSerializer();
                xser.setOutputCharStream(fw);
                xser.serialize((Document) xsl);
                
                URL url = tmp.toURL();
                
                this.engine.storeCollectionStylesheet(virColl, url);
            }
            }
        } catch (InvalidCollectionReferenceException e) {
            LOG.error(e.getMessage(), e);
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (InvalidQueryException e) {
            LOG.error(e.getMessage(), e);
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (InvalidVCLSchemaException e) {
            LOG.error(e.getMessage(), e);
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (SAXException e) {
            LOG.error(e.getMessage(), e);
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (StorageException e) {
            LOG.fatal(e.getMessage(), e);
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (UndeclaredVariableException e) {
            LOG.error(e.getMessage(), e);
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (VariableExistsException e) {
            LOG.error(e.getMessage(), e);
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * Deletes or modifies a <code>VirtualCollection</code> that depends on the 
     * given <code>CollectionImpl</code> and <code>XMLResourceImpl</code>.
     * 
     * @param ctxCollIn The context <code>CollectionImpl</code> object.
     * @param resIn The resource that was deleted. 
     * @throws XMLDBException If any database specific error occures.
     * @throws SAXException ...
     */
    private void updateDelete(final CollectionImpl ctxCollIn,
                              final XMLResourceImpl resIn) 
            throws SAXException,
                   XMLDBException {
        
        try {
            StorageCollectionI ctxColl = ctxCollIn.getStorageCollection();
            
            StorageCollectionI[] cref = this.storage.queryCollectionReferences(
                    ctxColl);
            
            if (cref != null && cref.length != 0) {
                
                String resPath = ctxColl.getPath() + "/" + resIn.getId();
                
                String matchExpr = "[@" + PCVResource.ATTR_RESOURCE_REFERENCE
                                 + "='" + resPath + "']";
                
                String rootQuery = BASE_XPATH + matchExpr;
                
                String subQuery = BASE_XPATH
                                + "//" + PCVResource.QNAME_COLLECTION
                                + "/" + PCVResource.QNAME_RESOURCE
                                + matchExpr;
                
                for (int i = 0; i < cref.length; i++) {
                    this.handleReference(cref[i], rootQuery, subQuery, resPath);
                }
            }
        } catch (StorageException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
    }
    
    
    /**
     * Deletes or modifies a <code>VirtualCollection</code> that depends on the 
     * given <code>CollectionImpl</code> and <code>XMLResourceImpl</code>.
     * 
     * @param ctxCollIn The context <code>CollectionImpl</code> object.
     * @param resIn The resource that was deleted. 
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
    private void updateInsert(final CollectionImpl ctxCollIn,
                              final XMLResourceImpl resIn) 
            throws InvalidCollectionReferenceException, 
                   InvalidQueryException,
                   InvalidVCLSchemaException,
                   SAXException,
                   StorageException,
                   UndeclaredVariableException,
                   VariableExistsException,
                   XMLDBException {
        
        StorageCollectionI ctxColl = ctxCollIn.getStorageCollection();
        VirtualCollectionImpl virColl = new VirtualCollectionImpl(
                this.engine, ctxColl);
        
        StorageCollectionI[] cref = 
            this.storage.queryCollectionReferences(ctxColl);
        LOG.info("cref is = " + cref);
        
        // nothing todo
        if (cref == null || cref.length == 0) {
            return;
        }

        String path = ctxColl.getPath();
        LOG.info("path id = " + path);
        
        for (int i = 0; i < cref.length; i++) {
            
            String vcsPath = cref[i].getPath() + "/" 
                           + StorageI.VCL_SCHEMA_COLLECTION;

            StorageCollectionI schemaColl = this.storage.queryCollection(
                    vcsPath);
            
            Document vclSchema = (Document) this.storage
                .queryObject(
                        schemaColl, StorageI.VCL_SCHEMA_RESOURCE);
            
            NodeList crefList = VCLHelper.getCollectionReferences(vclSchema);

            for (int j = 0, l = crefList.getLength(); j < l; j++) {
                Element crefElem = (Element) crefList.item(j);
                
                LOG.info("crefElem = path : " + crefElem.getAttribute(
                        VCLCollectionReference.ATTR_MATCH) + " = " + path);
                
                if (!crefElem.getAttribute(
                        VCLCollectionReference.ATTR_MATCH).equals(
                                path)) {
                    continue;
                }
                
                String select = crefElem.getAttribute(
                        VCLCollectionReference.ATTR_SELECT);
                
                CollectionImpl coll = new CollectionImpl(this.engine, cref[i]);
                                        
                if (select.equals("")) {
                    
                    LOG.info("resManager.create()");
                    this.resManager.create(cref[i], vclSchema, resIn.getId());
                } else {
                    
                    LOG.info("resManager.rebuild()");
                    this.resManager.rebuildVirtualResource(
                            virColl, coll, vclSchema, select, resIn.getId());
                }
            }
            
        }
    }

    
    /**
     * Processes a single resource reference in a collection reference.
     * 
     * @param cref The referenced <code>StorageCollectionI</code> object.
     * @param rootQuery XPath query that is used to check if the change affects
     *                  a root reference.
     * @param subQuery XPath query that is used to check if the change affects
     *                 a sub reference.
     * @param resPath The path of the resource.
     * @throws StorageException If any database specific error occures.
     * @throws SAXException ..
     * @throws XMLDBException If any database specific error occures.
     */
    private void handleReference(final StorageCollectionI cref,
                                 final String rootQuery,
                                 final String subQuery,
                                 final String resPath) 
            throws StorageException,
                   SAXException,
                   XMLDBException {

        VirtualCollectionImpl vColl = new VirtualCollectionImpl(
                this.engine, cref);
        
        String path = cref.getPath() + "/" 
                    + StorageI.PCVR_DATA_COLLECTION; 
        StorageCollectionI sPcvrColl = this.storage.queryCollection(
                path);
                            
        CollectionImpl pcvrColl = new CollectionImpl(
                this.engine, sPcvrColl);
    
        // query dependent root objects, for deleted resource
        StorageObjectI[] srObjs = this.storage.queryObjectsByXPath(
                sPcvrColl, rootQuery);
System.out.println("handleRef");
        // no root objects belong to the deleted resource, so update
        if (srObjs.length == 0) {
System.out.println("handleRef:update");
            this.resManager.update(
				        vColl, pcvrColl, subQuery, resPath);
        // there is one or more object(s), delete it(them)
        } else {
System.out.println("handleRef:delete");
            for (int j = 0; j < srObjs.length; j++) {
                String docId = ((StorageDocumentObjectI) srObjs[j])
                        .getContent().getDocumentId();
                
                this.resManager.delete(vColl, pcvrColl, docId);
            }
        }
    }
}
