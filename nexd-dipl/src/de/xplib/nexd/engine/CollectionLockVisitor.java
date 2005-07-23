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
 * $Log: CollectionLockVisitor.java,v $
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.3  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.2  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
 *
 * Revision 1.1  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 */
package de.xplib.nexd.engine;

import java.util.Iterator;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.exceptions.InvalidQueryException;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLAttr;
import de.xplib.nexd.api.vcl.VCLCollectionReference;
import de.xplib.nexd.api.vcl.VCLNodeIterator;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VCLSchemaVisitor;
import de.xplib.nexd.api.vcl.VCLValueOf;
import de.xplib.nexd.api.vcl.VCLVariable;
import de.xplib.nexd.api.vcl.VariableExistsException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class CollectionLockVisitor implements VCLSchemaVisitor {
    
    /**
     * System logger instance.
     */
    private final Log log = LogFactory.getLog(CollectionLockVisitor.class);
    
    /**
     * <p><code>ArrayList</code> that holds all used 
     * <code>SixdmlCollection</code>s.</p> 
     */
    private FastArrayList colls;
    
    /**
     * Comment for <code>engine</code>
     * @backDirected true
     * @clientCardinality 1
     * @clientRole engine
     * @directed true
     * @supplierCardinality 1
     */
    private final NEXDEngineImpl engine;
    
    /**
     * Comment for <code>paths</code>
     */
    private FastArrayList paths = null;
    
    /**
     * <p>Constructor.</p>
     * 
     * @param engineIn <p>The {@link NEXDEngineImpl} with database logic.</p>
     */
    public CollectionLockVisitor(final NEXDEngineImpl engineIn) {
        super();
        
        this.engine = engineIn;
    }
    
    /**
     * <p>Opens an transaction for all <code>SixdmlCollection</code>s that are
     * referenced by the given <code>schemaIn</code>.</p>
     *   
     * @param schemaIn <code>The Schema that is used top lock the 
     *                 <code>SixdmlCollection</code> instances.</p> 
     * @throws InvalidCollectionReferenceException <p>If schema has an error</p>
     * @throws InvalidQueryException <p>If schema has an error</p>
     * @throws InvalidVCLSchemaException <p>If schema has an error</p>
     * @throws UndeclaredVariableException <p>If schema has an error</p>
     * @throws VariableExistsException <p>If schema has an error</p>
     * @throws XMLDBException <p>If something database specific goes wrong.</p>
     */
    public void lock(final VCLSchema schemaIn)
	        throws InvalidCollectionReferenceException, 
	               InvalidQueryException,
	               InvalidVCLSchemaException, 
	               UndeclaredVariableException,
	               VariableExistsException, 
	               XMLDBException {
        
        this.colls = new FastArrayList();
        this.colls.setFast(true);
        
        this.paths = new FastArrayList();
        this.paths.setFast(true);
        
        log.debug("Starting lock.");
        
        schemaIn.accept(this);
    }
    
    /**
     * <p>This method relases or commits all <code>SixdmlCollection</code> 
     * instances.</p>
     * 
     * @throws XMLDBException <p>If something database specific goes wrong.</p>
     */
    public void release() throws XMLDBException {
        Iterator collections = this.colls.iterator();
        while (collections.hasNext()) {
            this.engine.commitTransaction(
                    (SixdmlCollection) collections.next());
        }
    }
    
    /**
     * <p>Returns all <code>SixdmlCollection</code> instances that are in a
     * transaction.</p>
     *  
     * @return <p>All <code>SixdmlCollection</code> instances in transaction</p>
     */
    public SixdmlCollection[] getCollections() {
        
        SixdmlCollection[] copy = new SixdmlCollection[this.colls.size()];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = (SixdmlCollection) this.colls.get(i);
        }
        
        return copy;
    }
    
    /**
     * <p>Returns all collection paths.</p>
     * 
     * @return <p>The collection paths.</p>
     */
    public String[] getPaths() {
        
        String[] copy = new String[this.paths.size()];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = (String) this.paths.get(i);
        }
        return copy;
    }
    

    /**
     * <Some description here>
     * 
     * @param schemaIn
     * @throws InvalidCollectionReferenceException
     * @throws InvalidQueryException
     * @throws InvalidVCLSchemaException
     * @throws UndeclaredVariableException
     * @throws VariableExistsException
     * @throws XMLDBException
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLSchema)
     */
    public void visit(final VCLSchema schemaIn)
            throws InvalidCollectionReferenceException, 
                   InvalidQueryException,
                   InvalidVCLSchemaException, 
                   UndeclaredVariableException,
                   VariableExistsException, 
                   XMLDBException {
        
        log.debug("I am visited by: " + schemaIn);
        
        VCLCollectionReference collRef = schemaIn.getCollectionReference();
        
        collRef.accept(this);
        
    }

    /**
     * <Some description here>
     * 
     * @param collRefIn
     * @throws InvalidCollectionReferenceException
     * @throws InvalidQueryException
     * @throws InvalidVCLSchemaException
     * @throws UndeclaredVariableException
     * @throws VariableExistsException
     * @throws XMLDBException
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLCollectionReference)
     */
    public void visit(final VCLCollectionReference collRefIn)
            throws InvalidCollectionReferenceException, 
                   InvalidQueryException,
                   InvalidVCLSchemaException, 
                   UndeclaredVariableException,
                   VariableExistsException, 
                   XMLDBException {
        
        String match = collRefIn.getMatch();
        
        log.debug("I am visited by: " + collRefIn + ".getMatch() := " + match);
        if (this.paths.indexOf(match) == -1) { 
            
            log.debug("The match[" + match + "] is not indexed, so doit.");
            
            SixdmlCollection coll = this.engine.queryCollection(match);
            
            this.colls.add(coll);
            this.engine.beginTransaction(coll);
            
            this.paths.add(match);
        }

        VCLNodeIterator collRefs = collRefIn.getCollectionReferences();
        
        while (collRefs.hasNext()) {
            collRefs.next().accept(this);
        }
    }

    /**
     * <Some description here>
     * 
     * @param attrIn
     * @throws InvalidQueryException
     * @throws UndeclaredVariableException
     * @throws XMLDBException
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLAttr)
     */
    public void visit(final VCLAttr attrIn) 
            throws InvalidQueryException,
                   UndeclaredVariableException, 
                   XMLDBException {
    }

    /**
     * <Some description here>
     * 
     * @param valueOfIn
     * @throws InvalidQueryException
     * @throws UndeclaredVariableException
     * @throws XMLDBException
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLValueOf)
     */
    public void visit(final VCLValueOf valueOfIn) 
            throws InvalidQueryException,
                   UndeclaredVariableException, 
                   XMLDBException {
    }

    /**
     * <Some description here>
     * 
     * @param variableIn
     * @throws InvalidQueryException
     * @throws VariableExistsException
     * @throws XMLDBException
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLVariable)
     */
    public void visit(final VCLVariable variableIn) 
            throws InvalidQueryException,
                   VariableExistsException, 
                   XMLDBException {
    }

}
