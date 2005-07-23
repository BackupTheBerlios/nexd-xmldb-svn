
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
 * $Log: VirtualCollection.java,v $
 * Revision 1.5  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.4  2005/03/26 12:14:20  nexd
 * UML documentation.
 *
 * Revision 1.3  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.api;

import java.io.IOException;
import java.net.URL;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.vcl.VCLSchema;
/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.5 $
 */
public interface VirtualCollection extends Collection {

    /**
     * @backDirected true
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..1
     */
    /*#de.xplib.nexd.api.XSLStylesheet lnkXSLStylesheet*/

    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..*
     */
    /*#_de.xplib.nexd.api.VirtualResource lnkVirtualResource*/
    /**
     * @clientCardinality 1
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#de.xplib.nexd.api.vcl.VCLSchema lnkVCLSchema*/
    
    /**
     * <p>This method returns the {@link VCLSchema} that describes how the 
     * {@link VirtualResource} objects in this <code>VirtualCollection</code>
     * are build.</p>
     * 
     * @return <p>The {@link VCLSchema} that describes how the resources are
     *         build.</p> 
     * @exception XMLDBException
     *            <p>This <code>Exception</code> is thrown, if any database 
     *            specific error occurs. The returned error code is
     *            {@link org.xmldb.api.base.ErrorCodes#VENDOR_ERROR}.</p>
     */
    VCLSchema getVCLSchema() throws XMLDBException;
    
    /**
     * <p>Sets the xsl-stylesheet that will be used to transform the documents
     * in this <code>VirtualCollection</code>. If a xsl-stylesshet already 
     * exists for this <code>VirtualCollection</code> then it is replaced by the
     * new one if it is a valid xsl-sytlesheet.</p>
     * 
     * @param xslIn <p>The <code>URL</code> where the xsl-stylesheet is located
     *              </p>
     * @throws IOException <p>If the given <code>URL</code> cannot be opend.</p>
     * @throws SAXException <p>If it is not a valid xsl-stylesheet.</p>
     * @exception XMLDBException <p>with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br /></p>
     */
    void setStylesheet(URL xslIn) throws IOException, 
                                         SAXException, 
                                         XMLDBException;
    
    /**
     * <p>Unsets the xsl-stylesheet for this <code>VirtualCollection</code>. 
     * Does nothing if no xsl-stylesheet exists for the <code>VirtualCollection
     * </code>.</p> 
     * 
     * @exception XMLDBException <p>with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br /></p>
     */
    void unsetStylesheet() throws XMLDBException;
    
    /**
     * <p>Returns the contents of the xsl-stylesheet for the 
     * <code>VirtualCollection</code> as a DOM {@link Node}.</p>
     *  
     * @return <p>The xsl-stylesheet for the <code>VirtualCollection</code> or 
     *         <code>null</code> if none exists.</p> 
     * @exception XMLDBException <p>with expected error codes.<br />
     *            <code>ErrorCodes.VENDOR_ERROR</code> for any vendor specific 
     *            errors that occur.<br />
     *            <code>ErrorCodes.COLLECTION_CLOSED</code> if the 
     *            <code>close</code> method has been called on the 
     *            <code>Collection</code><br /></p>
     */
    Node showStylesheet() throws XMLDBException;

}
