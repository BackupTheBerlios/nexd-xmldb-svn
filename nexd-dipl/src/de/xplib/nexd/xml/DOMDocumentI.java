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
 * $Log: DOMDocumentI.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package de.xplib.nexd.xml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public interface DOMDocumentI extends Document, DOMNodeI {

    /**
     * @clientCardinality 1
     * @clientRole cache
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    /*#public DocumentCacheI lnkDocumentCacheI;*/

    /**
     * An attribute specifying, as part of the XML declaration, the encoding
     * of this document. This is <code>null</code> when unspecified.
     *
     * @return ---
     * @since DOM Level 3
     * @see org.w3c.dom.Document#getEncoding()
     */
    String getEncoding();

    /**
     * An attribute specifying, as part of the XML declaration, the encoding
     * of this document. This is <code>null</code> when unspecified.
     *
     * @param encodingIn ---
     * @since DOM Level 3
     * @see org.w3c.dom.Document#setEncoding(java.lang.String)
     */
    void setEncoding(String encodingIn);

    /**
     * An attribute specifying, as part of the XML declaration, whether this
     * document is standalone.
     * <br> This attribute represents the property [standalone] defined in .
     *
     * @return ---
     * @since DOM Level 3
     * @see org.w3c.dom.Document#getStandalone()
     */
    boolean getStandalone();

    /**
     * An attribute specifying, as part of the XML declaration, whether this
     * document is standalone.
     * <br> This attribute represents the property [standalone] defined in .
     *
     * @param standaloneIn ---
     * @since DOM Level 3
     * @see org.w3c.dom.Document#setStandalone(boolean)
     */
    void setStandalone(boolean standaloneIn);

    /**
     * An attribute specifying, as part of the XML declaration, the version
     * number of this document. This is <code>null</code> when unspecified.
     * <br> This attribute represents the property [version] defined in .
     *
     * @return ---
     * @since DOM Level 3
     * @see org.w3c.dom.Document#getVersion()
     */
    String getVersion();

    /**
     * An attribute specifying, as part of the XML declaration, the version
     * number of this document. This is <code>null</code> when unspecified.
     * <br> This attribute represents the property [version] defined in .
     *
     * @param versionIn ---
     * @exception DOMException
     *   NOT_SUPPORTED_ERR: Raised if the version is set to a value that is
     *   not supported by this <code>Document</code>.
     * @since DOM Level 3
     * @see org.w3c.dom.Document#setVersion(java.lang.String)
     */
    void setVersion(String versionIn) throws DOMException;

    /**
     * Getter method for the used <code>DocumentCacheImpl</code>, which holds
     * all changed node for a later storage update.
     *
     * @return The used <code>DocumentCacheImpl</code> instance.
     * @associate DocumentCacheI
     */
    DocumentCacheI getCache();

    /**
     * Getter method for the document id.
     *
     * @return The document id.
     */
    String getDocumentId();

    /**
     * Setter method for the document id.
     *
     * @param docIdIn The document id.
     */
    void setDocumentId(String docIdIn);

    /**
     * Setter method for the resource id.
     *
     * @param resIdIn The resource id.
     */
    void setResourceId(String resIdIn);

    /**
     * Starts the loading process.
     */
    //void startLoading();

    /**
     * Stops the loading process.
     *
     */
    //void stopLoading();

    /**
     * Rename an existing node. When possible this simply changes the name of
     * the given node, otherwise this creates a new node with the specified
     * name and replaces the existing node with the new node as described
     * below. This only applies to nodes of type <code>ELEMENT_NODE</code>
     * and <code>ATTRIBUTE_NODE</code>.
     * <br>When a new node is created, the following operations are performed:
     * the new node is created, any registered event listener is registered
     * on the new node, any user data attached to the old node is removed
     * from that node, the old node is removed from its parent if it has
     * one, the children are moved to the new node, if the renamed node is
     * an <code>Element</code> its attributes are moved to the new node, the
     * new node is inserted at the position the old node used to have in its
     * parent's child nodes list if it has one, the user data that was
     * attached to the old node is attach to the new node, the user data
     * event <code>NODE_RENAMED</code> is fired.
     * <br>When the node being renamed is an <code>Attr</code> that is
     * attached to an <code>Element</code>, the node is first removed from
     * the <code>Element</code> attributes map. Then, once renamed, either
     * by modifying the existing node or creating a new one as described
     * above, it is put back.
     * <br>In addition, when the implementation supports the feature
     * "MutationEvents", each mutation operation involved in this method
     * fires the appropriate event, and in the end the event
     * <code>ElementNameChanged</code> or <code>AttributeNameChanged</code>
     * is fired.Should this throw a HIERARCHY_REQUEST_ERR?
     *
     * @param node The node to rename.
     * @param namespaceURI The new namespaceURI.
     * @param name The new qualified name.
     * @return The renamed node. This is either the specified node or the new
     *   node that was created to replace the specified node.
     */
    Node renameNode(Node node, String namespaceURI, String name);

}