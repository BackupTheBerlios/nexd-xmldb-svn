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
 * $Log: AbstractPCVRFactory.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package de.xplib.nexd.api.pcvr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractPCVRFactory {
    
    /**
     * Comment for <code>LOG</code>
     */
    public static final Log LOG = LogFactory.getLog(AbstractPCVRFactory.class);

    /**
     * @clientCardinality 1
     * @directed true
     * @label creates instance
     * @link aggregation
     * @supplierCardinality 0..*
     */
    /*#AbstractPCVRCompiler lnkAbstractPCVRCompiler*/

    /**
     * String that is used to find the concrete <code>AbstractStorageFactory
     * </code> implementation that will be used by the NEXD engine.
     */
    public static final String FACTORY_KEY = 
        "de.xplib.nexd.api.pcvr.AbstractPCVRFactory";

    /**
     * Factory method that returns a concrete implementation of this class.
     *
     * @return A concrete implementation of this class.
     * @throws XMLDBException If any error occures during instantiation of this
     *                        class.
     */
    public static AbstractPCVRFactory newInstance() throws XMLDBException {

        String className = System.getProperty(FACTORY_KEY);
        
        LOG.info("Found AbstractPCVRFactory implementation is " + className);

        try {
            
            Class clazz = Class.forName(className);
            
            return (AbstractPCVRFactory) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (InstantiationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (IllegalAccessException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Returns an instance <code>PCVRCompiler</code> that is provided by this
     * fatcory implementation.
     *
     * @param collIn The <code>VirtualCollection</code> instance, which will be
     *               the context for the <code>PCVRCompile</code> implementation
     * @return A concrete implementation of <code>PCVRCompile</code> that is
     *         associated with the given <code>collIn</code>
     * @throws XMLDBException If anything goes wrong during the instantiation or
     *                        a database specific error occures.
     */
    public abstract PCVRCompiler newPCVRCompiler(final VirtualCollection collIn)
            throws XMLDBException;

    /**
     * Factory method that creates a concrete instance of the
     * <code>PCVResource</code> interface for the given <code>nameIn</code>, the
     * DOM <code>Document</code> and the <code>VirtualCollection</code>.
     *
     * @param collIn The context <code>VirtualCollection</code> instance for the
     *               <code>PCVResource</code> that will be created
     * @param nameIn The name of this new <code>PCVResource</code>.
     * @param pcvDocIn The raw XML data for this new <code>PCVResource</code>
     * @return A concrete implementation of <code>PCVResource</code> that is
     *         associated with the given <code>collIn</code>.
     * @throws XMLDBException If anything goes wrong during the instantiation or
     *                        a database specific error occures.
     */
    public abstract PCVResource newPCVResource(final VirtualCollection collIn,
            final String nameIn, final Document pcvDocIn) throws XMLDBException;
}
