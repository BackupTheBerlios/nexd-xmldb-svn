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
 * $Log: DocumentImpl.java,v $
 * Revision 1.3  2005/05/30 19:17:07  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:39  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.11  2005/04/24 15:00:26  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.10  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.9  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
 *
 * Revision 1.8  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.7  2005/03/14 12:22:48  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 * Revision 1.6  2005/03/01 10:25:11  nexd
 * Advanced namespace and DOM 3 support.
 *
 */
package de.xplib.nexd.engine.xml.dom;

import java.util.List;
import java.util.Stack;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import de.xplib.nexd.engine.store.DocumentObjectI;
import de.xplib.nexd.store.StorageObjectI;
import de.xplib.nexd.xml.DOMDocumentI;
import de.xplib.nexd.xml.DocumentCacheI;

/**
 * The <code>Document</code> interface represents the entire HTML or XML 
 * document. Conceptually, it is the root of the document tree, and provides 
 * the primary access to the document's data.
 * <p>Since elements, text nodes, comments, processing instructions, etc. 
 * cannot exist outside the context of a <code>Document</code>, the 
 * <code>Document</code> interface also contains the factory methods needed 
 * to create these objects. The <code>Node</code> objects created have a 
 * <code>ownerDocument</code> attribute which associates them with the 
 * <code>Document</code> within whose context they were created.
 * <p>See also the <a 
 * href='http://www.w3.org/TR/2002/WD-DOM-Level-3-Core-20020114'
 * >Document Object Model (DOM) Level 3 Core Specification</a>.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DocumentImpl 
    extends AbstractParentNode 
    implements DocumentObjectI {
    
    /**
     * Comment for <code>documentElement</code>
     * @clientCardinality 1
     * @clientRole documentElement
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..1
     */
    private ElementImpl documentElement = null;
    
    /**
     * The location where the <code>Document</code> is stored.
     */
    private String documentURI = null;
    
    /**
     * Comment for <code>doctype</code>
     * @clientCardinality 1
     * @clientRole doctype
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 0..1
     */
    private DocumentTypeImpl doctype = null;
    
    /**
     * Comment for <code>domImpl</code>
     * @clientRole domImpl
     * @directed true
     * @link association
     * @supplierCardinality 1
     */
    private final DOMImplementationImpl domImpl;
    
    /**
     * Comment for <code>standalone</code>
     */
    private boolean standalone = false;
    
    /**
     * Comment for <code>encoding</code>
     */
    private String encoding = null;
    
    /**
     * Comment for <code>version</code>
     */
    private String version = "1.0";

    /**
     * @param domImplIn ..
     */
    protected DocumentImpl(final DOMImplementationImpl domImplIn) {
        super(null, "#document", Node.DOCUMENT_NODE);
        
        this.domImpl = domImplIn;
    }

    /**
     * Returns a duplicate of this node, i.e., serves as a generic copy 
     * constructor for nodes. The duplicate node has no parent; (
     * <code>parentNode</code> is <code>null</code>.) and no user data. User 
     * data associated to the imported node is not carried over. However, if 
     * any <code>UserDataHandlers</code> has been specified along with the 
     * associated data these handlers will be called with the appropriate 
     * parameters before this method returns.
     * <br>Cloning an <code>Element</code> copies all attributes and their 
     * values, including those generated by the XML processor to represent 
     * defaulted attributes, but this method does not copy any children it 
     * contains unless it is a deep clone. This includes text contained in 
     * an the <code>Element</code> since the text is contained in a child 
     * <code>Text</code> node. Cloning an <code>Attribute</code> directly, 
     * as opposed to be cloned as part of an <code>Element</code> cloning 
     * operation, returns a specified attribute (<code>specified</code> is 
     * <code>true</code>). Cloning an <code>Attribute</code> always clones 
     * its children, since they represent its value, no matter whether this 
     * is a deep clone or not. Cloning an <code>EntityReference</code> 
     * automatically constructs its subtree if a corresponding 
     * <code>Entity</code> is available, no matter whether this is a deep 
     * clone or not. Cloning any other type of node simply returns a copy of 
     * this node.
     * <br>Note that cloning an immutable subtree results in a mutable copy, 
     * but the children of an <code>EntityReference</code> clone are readonly
     * . In addition, clones of unspecified <code>Attr</code> nodes are 
     * specified. And, cloning <code>Document</code>, 
     * <code>DocumentType</code>, <code>Entity</code>, and 
     * <code>Notation</code> nodes is implementation dependent.
     * 
     * @param deep If <code>true</code>, recursively clone the subtree under 
     *   the specified node; if <code>false</code>, clone only the node 
     *   itself (and its attributes, if it is an <code>Element</code>).
     * @return The duplicate node.
     * @see org.w3c.dom.Node#cloneNode(boolean)
     */
    public Node cloneNode(final boolean deep) {
        DocumentImpl clone = (DocumentImpl) this.domImpl.createDocument(
                this.getNamespaceURI(), null, null);
        clone.setDocumentURI(this.documentURI);
        
        NodeList nodes = this.getChildNodes();
        for (int i = 0, l = nodes.getLength(); i < l; i++) {
            clone.appendChild(clone.importNode(
                    nodes.item(i).cloneNode(true), true));
        }
        return clone;
    }

    /**
     * The Document Type Declaration (see <code>DocumentType</code>) 
     * associated with this document. For HTML documents as well as XML 
     * documents without a document type declaration this returns 
     * <code>null</code>. The DOM Level 2 does not support editing the 
     * Document Type Declaration.
     * 
     * @return ---
     * @version DOM Level 3
     * @see org.w3c.dom.Document#getDoctype()
     */
    public DocumentType getDoctype() {
        return this.doctype;
    } // end public DocumentType getDoctype()

    /**
     * The <code>DOMImplementation</code> object that handles this document. A 
     * DOM application may use objects from multiple implementations.
     * 
     * @return ---
     * @see org.w3c.dom.Document#getImplementation()
     */
    public DOMImplementation getImplementation() {
        return this.domImpl;
    }

    /**
     * This is a convenience attribute that allows direct access to the child 
     * node that is the document element of the document.
     * <br> This attribute represents the property [document element] defined 
     * in . 
     * 
     * @return ---
     * @see org.w3c.dom.Document#getDocumentElement()
     */
    public Element getDocumentElement() {
        return this.documentElement;
    }

    /**
     * Creates an element of the type specified. Note that the instance 
     * returned implements the <code>Element</code> interface, so attributes 
     * can be specified directly on the returned object.
     * <br>In addition, if there are known attributes with default values, 
     * <code>Attr</code> nodes representing them are automatically created 
     * and attached to the element.
     * <br>To create an element with a qualified name and namespace URI, use 
     * the <code>createElementNS</code> method.
     * 
     * @param tagName The name of the element type to instantiate. For XML, 
     *   this is case-sensitive, otherwise it depends on the case-sentivity 
     *   of the markup language in use. In that case, the name is mapped to 
     *   the canonical form of that markup by the DOM implementation.
     * @return A new <code>Element</code> object with the 
     *   <code>nodeName</code> attribute set to <code>tagName</code>, and 
     *   <code>localName</code>, <code>prefix</code>, and 
     *   <code>namespaceURI</code> set to <code>null</code>.
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified name contains an 
     *   illegal character.
     * @see org.w3c.dom.Document#createElement(java.lang.String)
     */
    public Element createElement(final String tagName) throws DOMException {
        return new ElementImpl(this, tagName);
    }

    /**
     * Creates an empty <code>DocumentFragment</code> object.
     * @return A new <code>DocumentFragment</code>.
     * @see org.w3c.dom.Document#createDocumentFragment()
     */
    public DocumentFragment createDocumentFragment() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates a <code>Text</code> node given the specified string.
     * @param data The data for the node.
     * @return The new <code>Text</code> object.
     * @see org.w3c.dom.Document#createTextNode(java.lang.String)
     */
    public Text createTextNode(final String data) {
        return new TextImpl(this, data);
    }

    /**
     * Creates a <code>Comment</code> node given the specified string.
     * @param data The data for the node.
     * @return The new <code>Comment</code> object.
     * @see org.w3c.dom.Document#createComment(java.lang.String)
     */
    public Comment createComment(final String data) {
        return new CommentImpl(this, data);
    }

    /**
     * Creates a <code>CDATASection</code> node whose value is the specified 
     * string.
     * 
     * @param data The data for the <code>CDATASection</code> contents.
     * @return The new <code>CDATASection</code> object.
     * @exception DOMException
     *   NOT_SUPPORTED_ERR: Raised if this document is an HTML document.
     * @see org.w3c.dom.Document#createCDATASection(java.lang.String)
     */
    public CDATASection createCDATASection(final String data) 
    		throws DOMException {
        
        return new CDATASectionImpl(this, data);
    }

    /**
     * Creates a <code>ProcessingInstruction</code> node given the specified 
     * name and data strings.
     * 
     * @param target The target part of the processing instruction.
     * @param data The data for the node.
     * @return The new <code>ProcessingInstruction</code> object.
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified target contains an 
     *   illegal character.
     *   <br>NOT_SUPPORTED_ERR: Raised if this document is an HTML document.
     * @see org.w3c.dom.Document#createProcessingInstruction(
     * 		java.lang.String, java.lang.String)
     */
    public ProcessingInstruction createProcessingInstruction(
            final String target,
            final String data) throws DOMException {

        return new ProcessingInstructionImpl(this, target, data);
    }

    /**
     * Creates an <code>Attr</code> of the given name. Note that the 
     * <code>Attr</code> instance can then be set on an <code>Element</code> 
     * using the <code>setAttributeNode</code> method. 
     * <br>To create an attribute with a qualified name and namespace URI, use 
     * the <code>createAttributeNS</code> method.
     * 
     * @param name The name of the attribute.
     * @return A new <code>Attr</code> object with the <code>nodeName</code> 
     *   attribute set to <code>name</code>, and <code>localName</code>, 
     *   <code>prefix</code>, and <code>namespaceURI</code> set to 
     *   <code>null</code>. The value of the attribute is the empty string.
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified name contains an 
     *   illegal character.
     * @see org.w3c.dom.Document#createAttribute(java.lang.String)
     */
    public Attr createAttribute(final String name) throws DOMException {
        return new AttrImpl(this, name);
    }

    /**
     * Creates an <code>EntityReference</code> object. In addition, if the 
     * referenced entity is known, the child list of the 
     * <code>EntityReference</code> node is made the same as that of the 
     * corresponding <code>Entity</code> node.If any descendant of the 
     * <code>Entity</code> node has an unbound namespace prefix, the 
     * corresponding descendant of the created <code>EntityReference</code> 
     * node is also unbound; (its <code>namespaceURI</code> is 
     * <code>null</code>). The DOM Level 2 does not support any mechanism to 
     * resolve namespace prefixes.
     * 
     * @param name The name of the entity to reference.
     * @return The new <code>EntityReference</code> object.
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified name contains an 
     *   illegal character.
     *   <br>NOT_SUPPORTED_ERR: Raised if this document is an HTML document.
     * @see org.w3c.dom.Document#createEntityReference(java.lang.String)
     */
    public EntityReference createEntityReference(final String name)
            throws DOMException {
        
        return new EntityReferenceImpl(this, name);
    }

    /**
     * Returns a <code>NodeList</code> of all the <code>Elements</code> with a 
     * given tag name in document order.
     * 
     * @param tagname The name of the tag to match on. The special value "*" 
     *   matches all tags. For XML, this is case-sensitive, otherwise it 
     *   depends on the case-sentivity of the markup language in use.
     * @return A new <code>NodeList</code> object containing all the matched 
     *   <code>Elements</code>.
     * @see org.w3c.dom.Document#getElementsByTagName(java.lang.String)
     */
    public NodeList getElementsByTagName(final String tagname) {
        
        NodeListImpl nodeList;
        if (this.documentElement == null) {
            nodeList = new NodeListImpl(this);
        } else {
            nodeList = (NodeListImpl) 
            this.documentElement.getElementsByTagName(tagname);
            
            if (tagname.equals("*") 
                    || this.documentElement.getTagName().equals(tagname)) {
                
                nodeList.getList().add(0, this.documentElement);
            }
        }
        return nodeList;
    }

    /**
     * Imports a node from another document to this document. The returned 
     * node has no parent; (<code>parentNode</code> is <code>null</code>). 
     * The source node is not altered or removed from the original document; 
     * this method creates a new copy of the source node.
     * <br>For all nodes, importing a node creates a node object owned by the 
     * importing document, with attribute values identical to the source 
     * node's <code>nodeName</code> and <code>nodeType</code>, plus the 
     * attributes related to namespaces (<code>prefix</code>, 
     * <code>localName</code>, and <code>namespaceURI</code>). As in the 
     * <code>cloneNode</code> operation, the source node is not altered. 
     * User data associated to the imported node is not carried over. 
     * However, if any <code>UserDataHandlers</code> has been specified 
     * along with the associated data these handlers will be called with the 
     * appropriate parameters before this method returns.
     * <br>Additional information is copied as appropriate to the 
     * <code>nodeType</code>, attempting to mirror the behavior expected if 
     * a fragment of XML or HTML source was copied from one document to 
     * another, recognizing that the two documents may have different DTDs 
     * in the XML case. The following list describes the specifics for each 
     * type of node. 
     * <dl>
     * <dt>ATTRIBUTE_NODE</dt>
     * <dd>The <code>ownerElement</code> attribute 
     * is set to <code>null</code> and the <code>specified</code> flag is 
     * set to <code>true</code> on the generated <code>Attr</code>. The 
     * descendants of the source <code>Attr</code> are recursively imported 
     * and the resulting nodes reassembled to form the corresponding subtree.
     * Note that the <code>deep</code> parameter has no effect on 
     * <code>Attr</code> nodes; they always carry their children with them 
     * when imported.</dd>
     * <dt>DOCUMENT_FRAGMENT_NODE</dt>
     * <dd>If the <code>deep</code> option 
     * was set to <code>true</code>, the descendants of the source 
     * <code>DocumentFragment</code> are recursively imported and the 
     * resulting nodes reassembled under the imported 
     * <code>DocumentFragment</code> to form the corresponding subtree. 
     * Otherwise, this simply generates an empty 
     * <code>DocumentFragment</code>.</dd>
     * <dt>DOCUMENT_NODE</dt>
     * <dd><code>Document</code> 
     * nodes cannot be imported.</dd>
     * <dt>DOCUMENT_TYPE_NODE</dt>
     * <dd><code>DocumentType</code> 
     * nodes cannot be imported.</dd>
     * <dt>ELEMENT_NODE</dt>
     * <dd>Specified attribute nodes of the 
     * source element are imported, and the generated <code>Attr</code> 
     * nodes are attached to the generated <code>Element</code>. Default 
     * attributes are not copied, though if the document being imported into 
     * defines default attributes for this element name, those are assigned. 
     * If the <code>importNode</code> <code>deep</code> parameter was set to 
     * <code>true</code>, the descendants of the source element are 
     * recursively imported and the resulting nodes reassembled to form the 
     * corresponding subtree.</dd>
     * <dt>ENTITY_NODE</dt>
     * <dd><code>Entity</code> nodes can be 
     * imported, however in the current release of the DOM the 
     * <code>DocumentType</code> is readonly. Ability to add these imported 
     * nodes to a <code>DocumentType</code> will be considered for addition 
     * to a future release of the DOM.On import, the <code>publicId</code>, 
     * <code>systemId</code>, and <code>notationName</code> attributes are 
     * copied. If a <code>deep</code> import is requested, the descendants 
     * of the the source <code>Entity</code> are recursively imported and 
     * the resulting nodes reassembled to form the corresponding subtree.</dd>
     * <dt>
     * ENTITY_REFERENCE_NODE</dt>
     * <dd>Only the <code>EntityReference</code> itself is 
     * copied, even if a <code>deep</code> import is requested, since the 
     * source and destination documents might have defined the entity 
     * differently. If the document being imported into provides a 
     * definition for this entity name, its value is assigned.</dd>
     * <dt>NOTATION_NODE</dt>
     * <dd>
     * <code>Notation</code> nodes can be imported, however in the current 
     * release of the DOM the <code>DocumentType</code> is readonly. Ability 
     * to add these imported nodes to a <code>DocumentType</code> will be 
     * considered for addition to a future release of the DOM.On import, the 
     * <code>publicId</code> and <code>systemId</code> attributes are copied.
     * Note that the <code>deep</code> parameter has no effect on this type 
     * of nodes since they cannot have any children.</dd>
     * <dt>
     * PROCESSING_INSTRUCTION_NODE</dt>
     * <dd>The imported node copies its 
     * <code>target</code> and <code>data</code> values from those of the 
     * source node.Note that the <code>deep</code> parameter has no effect 
     * on this type of nodes since they cannot have any children.</dd>
     * <dt>TEXT_NODE, 
     * CDATA_SECTION_NODE, COMMENT_NODE</dt>
     * <dd>These three types of nodes inheriting 
     * from <code>CharacterData</code> copy their <code>data</code> and 
     * <code>length</code> attributes from those of the source node.Note 
     * that the <code>deep</code> parameter has no effect on these types of 
     * nodes since they cannot have any children.</dd>
     * </dl> 
     * 
     * @param importedNode The node to import.
     * @param deep If <code>true</code>, recursively import the subtree under 
     *   the specified node; if <code>false</code>, import only the node 
     *   itself, as explained above. This has no effect on nodes that cannot 
     *   have any children, and on <code>Attr</code>, and 
     *   <code>EntityReference</code> nodes.
     * @return The imported node that belongs to this <code>Document</code>.
     * @exception DOMException
     *   NOT_SUPPORTED_ERR: Raised if the type of node being imported is not 
     *   supported.
     *   <br>INVALID_CHARACTER_ERR: Raised if one the imported names contain 
     *   an illegal character. This may happen when importing an XML 1.1  
     *   element into an XML 1.0 document, for instance.
     * @since DOM Level 2
     * @see org.w3c.dom.Document#importNode(org.w3c.dom.Node, boolean)
     */
    public Node importNode(final Node importedNode, final boolean deep) 
    		throws DOMException {

        if (importedNode == null) {
            throw new IllegalArgumentException("importedNode cannot be null.");
        }

        Node imported;
        
        switch (importedNode.getNodeType()) {
        	
        	case ELEMENT_NODE:
        	    imported = this.importElement(importedNode, deep);
        	    break;
        	    
        	case ATTRIBUTE_NODE:
        	    imported = this.importAttr(importedNode);
        	    break;
        	    
        	case TEXT_NODE:
        	    imported = this.createTextNode(importedNode.getNodeValue());
        	    break;
        	
        	case CDATA_SECTION_NODE:
                imported = this.createCDATASection(importedNode.getNodeValue());
        	    break;
        	    
        	case ENTITY_REFERENCE_NODE:
        	    imported = this.createEntityReference(
        	            importedNode.getNodeValue());
        	    break;
        	    
        	case COMMENT_NODE:
        	    imported = this.createComment(importedNode.getNodeValue());
        	    break;
        	    
            case PROCESSING_INSTRUCTION_NODE:
                imported = this.createProcessingInstruction(
                        importedNode.getNodeName(), 
                        importedNode.getNodeValue());
                break;
                
            case DOCUMENT_TYPE_NODE:
                imported = this.importDTD(importedNode);
                break;
                
            case DOCUMENT_NODE:
            case ENTITY_NODE:
            case DOCUMENT_FRAGMENT_NODE:
            case NOTATION_NODE: 
        	default:
        	    throw new DOMException(
        	            DOMException.NOT_SUPPORTED_ERR,
        	            this.getNotSupported(importedNode));
        }
        return imported;
    }
    
    /**
     * Imports an element node.
     *  
     * @param nodeIn The element node to import.
     * @param deep Do we import down the tree?
     * @return The imported node.
     */
    private Element importElement(final Node nodeIn, final boolean deep) {
        Element impElem = (Element) nodeIn;
	    
	    ElementImpl elem;
	    if (impElem.getNamespaceURI() == null) {
	        elem = (ElementImpl) this.createElement(
	                impElem.getTagName());
	    } else {
	        elem = (ElementImpl) this.createElementNS(
	                impElem.getNamespaceURI(), impElem.getTagName());
	    }
	    
	    if (impElem.hasAttributes()) {
	        NamedNodeMap attrs = impElem.getAttributes();
            for (int i = 0, size = attrs.getLength(); i < size; i++) {
	            elem.setAttributeNode(
                        (Attr) this.importNode(attrs.item(i), deep));
	        }
	    }
	    
	    if (deep && impElem.hasChildNodes()) {
	        NodeList nodes = impElem.getChildNodes();
            for (int i = 0, size = nodes.getLength(); i < size; i++) {
                elem.appendChild(this.importNode(nodes.item(i), true));
	        }
	    }
	    return elem;
    }
    
    /**
     * Imports an attribute.
     * 
     * @param nodeIn The attribute node to import.
     * @return The imported attribute.
     */
    private Attr importAttr(final Node nodeIn) {
        Attr impAttr = (Attr) nodeIn;
	    
	    Attr attr;
	    if (impAttr.getNamespaceURI() == null) {
	        attr = this.createAttribute(impAttr.getName());
	    } else {
	        attr = this.createAttributeNS(
	                impAttr.getNamespaceURI(), impAttr.getName());
	    }
	    attr.setValue(impAttr.getValue());
	    
	    return attr;
    }
    
    /**
     * Imports a dtd.
     * 
     * @param nodeIn The document type node to import.
     * @return The imported dtd.
     */
    private DocumentType importDTD(final Node nodeIn) {
        DocumentType dtd = this.domImpl.createDocumentType(
                ((DocumentType) nodeIn).getName(),
                ((DocumentType) nodeIn).getPublicId(),
                ((DocumentType) nodeIn).getSystemId());
        ((DocumentTypeImpl) dtd).setInternalSubset(
                ((DocumentType) nodeIn).getInternalSubset());
        
        return dtd;
    }

    /**
     * Creates an element of the given qualified name and namespace URI.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param namespaceURI The namespace URI of the element to create.
     * @param qualifiedName The qualified name of the element type to 
     *   instantiate.
     * @return A new <code>Element</code> object with the following 
     *   attributes:
     * <table border='1'>
     * <tr>
     * <th>Attribute</th>
     * <th>Value</th>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'><code>Node.nodeName</code></td>
     * <td valign='top' rowspan='1' colspan='1'>
     *   <code>qualifiedName</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'><code>Node.namespaceURI</code>
     * </td><td valign='top' rowspan='1' colspan='1'>
     *   <code>namespaceURI</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'><code>Node.prefix</code></td>
     * <td valign='top' rowspan='1' colspan='1'>prefix, extracted 
     *   from <code>qualifiedName</code>, or <code>null</code> if there is 
     *   no prefix</td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'><code>Node.localName</code></td>
     * <td valign='top' rowspan='1' colspan='1'>local name, extracted from 
     *   <code>qualifiedName</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'><code>Element.tagName</code>
     * </td><td valign='top' rowspan='1' colspan='1'>
     *   <code>qualifiedName</code></td>
     * </tr>
     * </table>
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified qualified name 
     *   contains an illegal character, per the XML 1.0 specification .
     *   <br>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is 
     *   malformed per the Namespaces in XML specification, if the 
     *   <code>qualifiedName</code> has a prefix and the 
     *   <code>namespaceURI</code> is <code>null</code>, or if the 
     *   <code>qualifiedName</code> has a prefix that is "xml" and the 
     *   <code>namespaceURI</code> is different from "
     *   http://www.w3.org/XML/1998/namespace" .
     *   <br>NOT_SUPPORTED_ERR: Always thrown if the current document does not 
     *   support the <code>"XML"</code> feature, since namespaces were 
     *   defined by XML.
     * @since DOM Level 2
     * @see org.w3c.dom.Document#createElementNS(
     * 		java.lang.String, java.lang.String)
     */
    public Element createElementNS(final String namespaceURI, 
                                   final String qualifiedName)
            throws DOMException {
        
        return new ElementNSImpl(this, namespaceURI, qualifiedName);
    }

    /**
     * Creates an attribute of the given qualified name and namespace URI.
     * <br>Per , applications must use the value null as the namespaceURI 
     * parameter for methods if they wish to have no namespace.
     * 
     * @param namespaceURI The namespace URI of the attribute to create.
     * @param qualifiedName The qualified name of the attribute to 
     *   instantiate.
     * @return A new <code>Attr</code> object with the following attributes:
     * <table border='1'>
     * <tr>
     * <th>
     *   Attribute</th>
     * <th>Value</th>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'><code>Node.nodeName</code></td>
     * <td valign='top' rowspan='1' colspan='1'>qualifiedName</td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>
     *   <code>Node.namespaceURI</code></td>
     * <td valign='top' rowspan='1' colspan='1'><code>namespaceURI</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>
     *   <code>Node.prefix</code></td>
     * <td valign='top' rowspan='1' colspan='1'>prefix, extracted from 
     *   <code>qualifiedName</code>, or <code>null</code> if there is no 
     *   prefix</td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'><code>Node.localName</code></td>
     * <td valign='top' rowspan='1' colspan='1'>local name, extracted from 
     *   <code>qualifiedName</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'><code>Attr.name</code></td>
     * <td valign='top' rowspan='1' colspan='1'>
     *   <code>qualifiedName</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'><code>Node.nodeValue</code></td>
     * <td valign='top' rowspan='1' colspan='1'>the empty 
     *   string</td>
     * </tr>
     * </table>
     * @exception DOMException
     *   INVALID_CHARACTER_ERR: Raised if the specified qualified name 
     *   contains an illegal character, per the XML 1.0 specification .
     *   <br>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is 
     *   malformed per the Namespaces in XML specification, if the 
     *   <code>qualifiedName</code> has a prefix and the 
     *   <code>namespaceURI</code> is <code>null</code>, if the 
     *   <code>qualifiedName</code> has a prefix that is "xml" and the 
     *   <code>namespaceURI</code> is different from "
     *   http://www.w3.org/XML/1998/namespace", or if the 
     *   <code>qualifiedName</code>, or its prefix, is "xmlns" and the 
     *   <code>namespaceURI</code> is different from "
     *   http://www.w3.org/2000/xmlns/".
     *   <br>NOT_SUPPORTED_ERR: Always thrown if the current document does not 
     *   support the <code>"XML"</code> feature, since namespaces were 
     *   defined by XML.
     * @since DOM Level 2
     * @see org.w3c.dom.Document#createAttributeNS(
     * 		java.lang.String, java.lang.String)
     */
    public Attr createAttributeNS(final String namespaceURI, 
                                  final String qualifiedName)
            throws DOMException {

        return new AttrNSImpl(this, namespaceURI, qualifiedName);
    }

    /**
     * Returns a <code>NodeList</code> of all the <code>Elements</code> with a 
     * given local name and namespace URI in document order.
     * 
     * @param namespaceURI The namespace URI of the elements to match on. The 
     *   special value "*" matches all namespaces.
     * @param localName The local name of the elements to match on. The 
     *   special value "*" matches all local names.
     * @return A new <code>NodeList</code> object containing all the matched 
     *   <code>Elements</code>.
     * @since DOM Level 2
     * @see org.w3c.dom.Document#getElementsByTagNameNS(
     * 		java.lang.String, java.lang.String)
     */
    public NodeList getElementsByTagNameNS(final String namespaceURI, 
                                           final String localName) {
        NodeListImpl nodeList;
        if (this.documentElement == null) {
            nodeList = new NodeListImpl(this);
        } else {
            
            nodeList = (NodeListImpl) 
            this.documentElement.getElementsByTagNameNS(
                    namespaceURI, localName);
            
            if ((localName.equals("*") 
                    || this.documentElement.getTagName().equals(localName))
                    && (namespaceURI.equals("*")
                            || (this.documentElement.getNamespaceURI() != null 
                                    && this.documentElement
                                           .getNamespaceURI().equals(
                                                   namespaceURI)))) {
                
                nodeList.getList().add(0, this.documentElement);
            }
        }
        return nodeList;
    }

    /**
     * Returns the <code>Element</code> whose <code>ID</code> is given by 
     * <code>elementId</code>. If no such element exists, returns 
     * <code>null</code>. Behavior is not defined if more than one element 
     * has this <code>ID</code>. The DOM implementation must have 
     * information that says which attributes are of type ID. Attributes 
     * with the name "ID" are not of type ID unless so defined. 
     * Implementations that do not know whether attributes are of type ID or 
     * not are expected to return <code>null</code>. 
     * 
     * @param elementId The unique <code>id</code> value for an element.
     * @return The matching element.
     * @since DOM Level 2
     * @see org.w3c.dom.Document#getElementById(java.lang.String)
     */
    public Element getElementById(final String elementId) {
        
        Element result = null;
        if (this.documentElement != null) {
            if (this.documentElement.getAttribute("id").equals(elementId)) {
                result = this.documentElement;
            } else {
                NamedNodeMap nnm = this.documentElement.getAttributes();
                for (int j = 0, l1 = nnm.getLength(); j < l1; j++) {
                    if (((AttrImpl) nnm.item(j)).isId() 
                            && nnm.item(j).getNodeValue().equals(elementId)) {
                        
                        result = this.documentElement;
                        break;
                    }
                }
            }
            if (result == null) {
                result = this.findElementById(elementId);
            }
        }
        return result;
    }
    
    /**
     * Finds an Element by its id attribute.
     * 
     * @param elementId The id to find.
     * @return The found Element or null.
     */
    private Element findElementById(final String elementId) {
        
        Stack stack = new Stack();
        stack.push(this.documentElement.getChildNodes());
        
        Element result = null;
        while (!stack.empty() && result == null) {
            NodeList children = (NodeList) stack.pop();
            for (int i = 0, l = children.getLength(); i < l; i++) {
                Node node = children.item(i);
                if (!(node instanceof Element)) {
                    continue;
                }
                if (((Element) node).getAttribute("id").equals(elementId)) {
                    result = (Element) node;
                    break;
                }
                NamedNodeMap nnm = node.getAttributes();
                for (int j = 0, l1 = nnm.getLength(); j < l1; j++) {
                    if (((AttrImpl) nnm.item(j)).isId() 
                            && nnm.item(j).getNodeValue().equals(elementId)) {
                        result = (Element) node;
                        break;
                    }
                }
                stack.push(node.getChildNodes());
            }
        }
        return result;
    }

    /**
     * An attribute specifying, as part of the XML declaration, the encoding 
     * of this document. This is <code>null</code> when unspecified.
     * 
     * @return ---
     * @since DOM Level 3
     * @see org.w3c.dom.Document#getEncoding()
     */
    public String getEncoding() {
        return this.encoding;
    }

    /**
     * An attribute specifying, as part of the XML declaration, the encoding 
     * of this document. This is <code>null</code> when unspecified.
     * 
     * @param encodingIn ---
     * @since DOM Level 3
     * @see org.w3c.dom.Document#setEncoding(java.lang.String)
     */
    public void setEncoding(final String encodingIn) {
        this.encoding = encodingIn;
    } 

    /**
     * An attribute specifying, as part of the XML declaration, whether this 
     * document is standalone.
     * <br> This attribute represents the property [standalone] defined in .
     * 
     * @return --- 
     * @since DOM Level 3
     * @see org.w3c.dom.Document#getStandalone()
     */
    public boolean getStandalone() {
        return this.standalone;
    }

    /**
     * An attribute specifying, as part of the XML declaration, whether this 
     * document is standalone.
     * <br> This attribute represents the property [standalone] defined in .
     * 
     * @param standaloneIn --- 
     * @since DOM Level 3
     * @see org.w3c.dom.Document#setStandalone(boolean)
     */
    public void setStandalone(final boolean standaloneIn) {
        this.standalone = standaloneIn;
    }

    /**
     * An attribute specifying, as part of the XML declaration, the version 
     * number of this document. This is <code>null</code> when unspecified.
     * <br> This attribute represents the property [version] defined in .
     * 
     * @return ---
     * @since DOM Level 3
     * @see org.w3c.dom.Document#getVersion()
     */
    public String getVersion() {
        return this.version;
    }

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
    public void setVersion(final String versionIn) throws DOMException {
        if (!versionIn.equals("1.0")) {
            throw new DOMException(
                    DOMException.NOT_SUPPORTED_ERR,
                    "This implementation supports only XML 1.0");
        }
        this.version = versionIn;
    }

    /**
     * The location of the document or <code>null</code> if undefined.
     * <br>Beware that when the <code>Document</code> supports the feature 
     * "HTML" , the href attribute of the HTML BASE element takes precedence 
     * over this attribute.
     * 
     * @return The uri for this document.
     * @since DOM Level 3
     * @see org.w3c.dom.Document#getDocumentURI()
     */
    public String getDocumentURI() {
        return this.documentURI;
    }

    /**
     * The location of the document or <code>null</code> if undefined.
     * <br>Beware that when the <code>Document</code> supports the feature 
     * "HTML" , the href attribute of the HTML BASE element takes precedence 
     * over this attribute.
     * 
     * @param documentURIIn The uri for this document.
     * @since DOM Level 3
     * @see org.w3c.dom.Document#setDocumentURI(java.lang.String)
     */
    public void setDocumentURI(final String documentURIIn) {
        this.documentURI = documentURIIn;
    }

    /**
     * Changes the <code>ownerDocument</code> of a node, its children, as well 
     * as the attached attribute nodes if there are any. If the node has a 
     * parent it is first removed from its parent child list. This 
     * effectively allows moving a subtree from one document to another. The 
     * following list describes the specifics for each type of node. 
     * <dl>
     * <dt>
     * ATTRIBUTE_NODE</dt>
     * <dd>The <code>ownerElement</code> attribute is set to 
     * <code>null</code> and the <code>specified</code> flag is set to 
     * <code>true</code> on the adopted <code>Attr</code>. The descendants 
     * of the source <code>Attr</code> are recursively adopted.</dd>
     * <dt>
     * DOCUMENT_FRAGMENT_NODE</dt>
     * <dd>The descendants of the source node are 
     * recursively adopted.</dd>
     * <dt>DOCUMENT_NODE</dt>
     * <dd><code>Document</code> nodes cannot 
     * be adopted.</dd>
     * <dt>DOCUMENT_TYPE_NODE</dt>
     * <dd><code>DocumentType</code> nodes cannot 
     * be adopted.</dd>
     * <dt>ELEMENT_NODE</dt>
     * <dd>Specified attribute nodes of the source 
     * element are adopted, and the generated <code>Attr</code> nodes. 
     * Default attributes are discarded, though if the document being 
     * adopted into defines default attributes for this element name, those 
     * are assigned. The descendants of the source element are recursively 
     * adopted.</dd>
     * <dt>ENTITY_NODE</dt>
     * <dd><code>Entity</code> nodes cannot be adopted.</dd>
     * <dt>
     * ENTITY_REFERENCE_NODE</dt>
     * <dd>Only the <code>EntityReference</code> node 
     * itself is adopted, the descendants are discarded, since the source 
     * and destination documents might have defined the entity differently. 
     * If the document being imported into provides a definition for this 
     * entity name, its value is assigned.</dd>
     * <dt>NOTATION_NODE</dt>
     * <dd><code>Notation</code> 
     * nodes cannot be adopted.</dd>
     * <dt>PROCESSING_INSTRUCTION_NODE, TEXT_NODE, 
     * CDATA_SECTION_NODE, COMMENT_NODE</dt>
     * <dd>These nodes can all be adopted. No 
     * specifics.</dd>
     * </dl> Should this method simply return null when it fails? How 
     * "exceptional" is failure for this method?Stick with raising 
     * exceptions only in exceptional circumstances, return null on failure 
     * (F2F 19 Jun 2000).Can an entity node really be adopted?No, neither 
     * can Notation nodes (Telcon 13 Dec 2000).Does this affect keys and 
     * hashCode's of the adopted subtree nodes?If so, what about 
     * readonly-ness of key and hashCode?if not, would appendChild affect 
     * keys/hashCodes or would it generate exceptions if key's are duplicate?
     * Both keys and hashcodes have been dropped.
     * 
     * @param source The node to move into this document.
     * @return The adopted node, or <code>null</code> if this operation 
     *   fails, such as when the source node comes from a different 
     *   implementation.
     * @exception DOMException
     *   NOT_SUPPORTED_ERR: Raised if the source node is of type 
     *   <code>DOCUMENT</code>, <code>DOCUMENT_TYPE</code>.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised when the source node is 
     *   readonly.
     * @since DOM Level 3
     * @see org.w3c.dom.Document#adoptNode(org.w3c.dom.Node)
     */
    public Node adoptNode(final Node source) throws DOMException {
        throw new DOMException(
                DOMException.NOT_SUPPORTED_ERR,
                "The feature [adoptNode()] is not supported");
    }

    /**
     * This method acts as if the document was going through a save and load 
     * cycle, putting the document in a "normal" form. The actual result 
     * depends on the features being set and governing what operations 
     * actually take place. See <code>setNormalizeFeature</code> for details.
     * <br>Noticeably this method normalizes <code>Text</code> nodes, makes 
     * the document "namespace wellformed", according to the algorithm 
     * described below in pseudo code, by adding missing namespace 
     * declaration attributes and adding or changing namespace prefixes, 
     * updates the replacement tree of <code>EntityReference</code> nodes, 
     * normalizes attribute values, etc.
     * <br>See  for details on how namespace declaration attributes and 
     * prefixes are normalized.Any other name? Joe proposes 
     * normalizeNamespaces.normalizeDocument. (F2F 26 Sep 2001)How specific 
     * should this be? Should we not even specify that this should be done 
     * by walking down the tree?Very. See above.What does this do on 
     * attribute nodes?Doesn't do anything (F2F 1 Aug 2000).How does it work 
     * with entity reference subtree which may be broken?This doesn't affect 
     * entity references which are not visited in this operation (F2F 1 Aug 
     * 2000).Should this really be on Node?Yes, but this only works on 
     * Document, Element, and DocumentFragment. On other types it is a 
     * no-op. (F2F 1 Aug 2000).No. Now that it does much more than simply 
     * fixing namespaces it only makes sense on Document (F2F 26 Sep 2001).
     * What happens with read-only nodes?What/how errors should be reported? 
     * Are there any?Through the error reporter.Should this be optional?No.
     * What happens with regard to mutation events?
     * 
     * @since DOM Level 3
     * @see org.w3c.dom.Document#normalizeDocument()
     */
    public void normalizeDocument() {
        if (this.documentElement != null) {
            this.documentElement.normalize();
        }
    }

    /**
     * Query whether setting a feature to a specific value is supported.
     * <br>The feature name has the same form as a DOM <code>hasFeature</code> 
     * string.
     * @param name The name of the feature to check.
     * @param state The requested state of the feature (<code>true</code> or 
     *   <code>false</code>).
     * @return <code>true</code> if the feature could be successfully set to 
     *   the specified value, or <code>false</code> if the feature is not 
     *   recognized or the requested value is not supported. This does not 
     *   change the current value of the feature itself.
     * @since DOM Level 3
     * @see org.w3c.dom.Document#canSetNormalizationFeature(
     * 		java.lang.String, boolean)
     */
    public boolean canSetNormalizationFeature(final String name, 
                                              final boolean state) {
        return false;
    }

    /**
     * Set the state of a feature.Need to specify the list of features.
     * <br>Feature names are valid XML names. Implementation specific features 
     * (extensions) should choose an implementation specific prefix to avoid 
     * name collisions. The following lists feature names that are 
     * recognized by all implementations. However, it is sometimes possible 
     * for a <code>Document</code> to recognize a feature but not to support 
     * setting its value. The following list of recognized features 
     * indicates the definitions of each feature state, if setting the state 
     * to <code>true</code> or <code>false</code> must be supported or is 
     * optional and, which state is the default one:
     * <dl>
     * <dt>
     * <code>"normalize-characters"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[optional]Perform 
     * the W3C Text Normalization of the characters  in the document. </dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[required] (default)Do not perform character 
     * normalization.</dd>
     * </dl></dd>
     * <dt><code>"split-cdata-sections"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[
     * required] (default)Split CDATA sections containing the CDATA section 
     * termination marker ']]&gt;'. When a CDATA section is split a warning 
     * is issued.</dd>
     * <dt><code>false</code></dt>
     * <dd>[required]Signal an error if a 
     * <code>CDATASection</code> contains an unrepresentable character.</dd>
     * </dl></dd>
     * <dt>
     * <code>"expand-entity-references"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[optional]
     * Expand <code>EntityReference</code> nodes when normalizing.</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[required] (default)Keep all 
     * <code>EntityReference</code> nodes in document.</dd>
     * </dl></dd>
     * <dt>
     * <code>"whitespace-in-element-content"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[required
     * ] (default)Keep all white spaces in the document.</dd>
     * <dt><code>false</code></dt>
     * <dd>[
     * optional]Discard white space in element content while normalizing. 
     * The implementation is expected to use the 
     * <code>isWhitespaceInElementContent</code> flag on <code>Text</code> 
     * nodes to determine if a text node should be written out or not.</dd>
     * </dl></dd>
     * <dt>
     * <code>"discard-default-content"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[required] (
     * default)Use whatever information available to the implementation 
     * (i.e. XML schema, DTD, the <code>specified</code> flag on 
     * <code>Attr</code> nodes, and so on) to decide what attributes and 
     * content should be discarded or not. Note that the 
     * <code>specified</code> flag on <code>Attr</code> nodes in itself is 
     * not always reliable, it is only reliable when it is set to 
     * <code>false</code> since the only case where it can be set to 
     * <code>false</code> is if the attribute was created by a Level 1 
     * implementation.How does exactly work? What's the comment about level 
     * 1 implementations?</dd>
     * <dt><code>false</code></dt>
     * <dd>[required]Keep all attributes and 
     * all content.</dd>
     * </dl></dd>
     * <dt><code>"format-canonical"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[optional]
     * Canonicalize the document according to the rules specified in . 
     * Setting this feature to true sets the feature "format-pretty-print" 
     * to false.</dd>
     * <dt><code>false</code></dt>
     * <dd>[required] (default)Do not canonicalize 
     * the document.</dd>
     * </dl></dd>
     * <dt><code>"format-pretty-print"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[
     * optional]Format the document by adding whitespace to produce a 
     * pretty-printed, indented, human-readable form. The exact form of the 
     * transformations is not specified by this specification. Setting this 
     * feature to true sets the feature "format-canonical" to false.</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[required] (default)Do not pretty-print the 
     * document.</dd>
     * </dl></dd>
     * <dt><code>"namespace-declarations"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[
     * required] (default)Include namespace declaration attributes, 
     * specified or defaulted from the schema or the DTD, in the document. 
     * See also the section Declaring Namespaces in .</dd>
     * <dt><code>false</code></dt>
     * <dd>[
     * optional]Discard all namespace declaration attributes. The Namespace 
     * prefixes are retained even if this feature is set to 
     * <code>false</code>.</dd>
     * </dl></dd>
     * <dt><code>"validation"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[optional
     * ]Use the abstract schema to validate the document as it is being 
     * normalized. If validation errors are found the error handler is 
     * notified. Setting it to <code>true</code> also forces the 
     * <code>external-general-entities</code> and 
     * <code>external-parameter-entities</code> features to be 
     * <code>true</code>.) Also note that the <code>validate-if-schema</code>
     *  feature alters the validation behavior when this feature is set to 
     * <code>true</code>.</dd>
     * <dt><code>false</code></dt>
     * <dd>[required] (default)Do not report 
     * validation errors.</dd>
     * </dl></dd>
     * <dt><code>"external-parameter-entities"</code></dt>
     * <dd>
     * <dl>
     * <dt>
     * <code>true</code></dt>
     * <dd>[required]Load external parameter entities.Doesn't 
     * really apply, does it? What does including them mean? Also, false 
     * can't be the default and be optional at the same time.</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[optional] (default)Do not load external parameter 
     * entities.</dd>
     * </dl></dd>
     * <dt><code>"external-general-entities"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[
     * required] (default)Include all external general (text) entities.
     * Doesn't really apply, does it? What does including them mean?</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[optional]Do not include external general entities.</dd>
     * </dl></dd>
     * <dt>
     * <code>"external-dtd-subset"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[required] (default
     * )Load the external DTD subset and also all external parameter 
     * entities.Doesn't really apply, does it? What does loading mean here?</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[optional]Do not load the external DTD subset nor 
     * external parameter entities.</dd>
     * </dl></dd>
     * <dt><code>"validate-if-schema"</code></dt>
     * <dd>
     * <dl>
     * <dt>
     * <code>true</code></dt>
     * <dd>[optional]When both this feature and validation are 
     * <code>true</code>, enable validation only if the document being 
     * processed has a schema (i.e. XML schema, DTD, any other type of 
     * schema, note that this is unrelated to the abstract schema 
     * specification). Documents without schemas are normalized without 
     * validation.How does that interact with the notion of active AS?</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[required] (default)The validation feature alone 
     * controls whether the document is checked for validity. Documents 
     * without a schemas are not valid.</dd>
     * </dl></dd>
     * <dt><code>"validate-against-dtd"</code></dt>
     * <dd>
     * <dl>
     * <dt>
     * <code>true</code></dt>
     * <dd>[optional]Prefer validation against the DTD over any 
     * other schema used with the document.How does that interact with the 
     * notion of active AS?</dd>
     * <dt><code>false</code></dt>
     * <dd>[required] (default)Let the 
     * implementation decide what to validate against if multiple types of 
     * schemas are in use.</dd>
     * </dl></dd>
     * <dt><code>"datatype-normalization"</code></dt>
     * <dd>
     * <dl>
     * <dt>
     * <code>true</code></dt>
     * <dd>[required]Let the (non-DTD) validation process do 
     * its datatype normalization that is defined in the used schema 
     * language.We should define "datatype normalization".</dd>
     * <dt><code>false</code></dt>
     * <dd>[
     * required] (default)Disable datatype normalization. The XML 1.0 
     * attribute value normalization always occurs though.</dd>
     * </dl></dd>
     * <dt>
     * <code>"create-entity-ref-nodes"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[required] (
     * default)Create <code>EntityReference</code> nodes in the document. It 
     * will also set <code>create-entity-nodes</code> to be <code>true</code>
     * .How does that interact with expand-entity-references? ALH suggests 
     * consolidating the two to a single feature called "entity-references" 
     * that is used both for load and save.</dd>
     * <dt><code>false</code></dt>
     * <dd>[optional]Omit 
     * all <code>EntityReference</code> nodes from the document, putting the 
     * entity expansions directly in their place. <code>Text</code> nodes 
     * are into "normal" form. <code>EntityReference</code> nodes to 
     * non-defined entities are still created in the document.</dd>
     * </dl></dd>
     * <dt>
     * <code>"create-entity-nodes"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[required] (default
     * )Create <code>Entity</code> nodes in the document.How does that 
     * interact with expand-entity-references? ALH suggests renaming this 
     * one "entity-nodes", or simply "entities" for consistency.</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[optional]Omit all <code>entity</code> nodes from 
     * the document. It also sets <code>create-entity-ref-nodes</code> to 
     * <code>false</code>.</dd>
     * </dl></dd>
     * <dt><code>"create-cdata-nodes"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[
     * required] (default)Keep <code>CDATASection</code> nodes the document.
     * Name does not work really well in this case. ALH suggests renaming 
     * this to "cdata-sections". It works for both load and save.</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[optional]Transform <code>CDATASection</code> nodes 
     * in the document into <code>Text</code> nodes. The new 
     * <code>Text</code> node is then combined with any adjacent 
     * <code>Text</code> node.</dd>
     * </dl></dd>
     * <dt><code>"comments"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[
     * required] (default)Keep <code>Comment</code> nodes in the document.</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>[required]Discard <code>Comment</code> nodes in the 
     * Document.</dd>
     * </dl></dd>
     * <dt><code>"load-as-infoset"</code></dt>
     * <dd>
     * <dl>
     * <dt><code>true</code></dt>
     * <dd>[optional]
     * Only keep in the document the information defined in the XML 
     * Information Set .This forces the following features to 
     * <code>false</code>: <code>namespace-declarations</code>, 
     * <code>validate-if-schema</code>, <code>create-entity-ref-nodes</code>
     * , <code>create-entity-nodes</code>, <code>create-cdata-nodes</code>.
     * This forces the following features to <code>true</code>: 
     * <code>datatype-normalization</code>, 
     * <code>whitespace-in-element-content</code>, <code>comments</code>.
     * Other features are not changed unless explicity specified in the 
     * description of the features. Note that querying this feature with 
     * <code>getFeature</code> returns <code>true</code> only if the 
     * individual features specified above are appropriately set.Name 
     * doesn't work well here. ALH suggests renaming this to 
     * limit-to-infoset or match-infoset, something like that.</dd>
     * <dt>
     * <code>false</code></dt>
     * <dd>Setting <code>load-as-infoset</code> to 
     * <code>false</code> has no effect.Shouldn't we change this to setting 
     * the relevant options back to their default value?</dd>
     * </dl></dd>
     * </dl>
     * 
     * @param name The name of the feature to set.
     * @param state The requested state of the feature (<code>true</code> or 
     *   <code>false</code>).
     * @exception DOMException
     *   NOT_SUPPORTED_ERR: Raised when the feature name is recognized but the 
     *   requested value cannot be set.
     *   <br>NOT_FOUND_ERR: Raised when the feature name is not recognized.
     * @since DOM Level 3
     * @see org.w3c.dom.Document#setNormalizationFeature(
     * 		java.lang.String, boolean)
     */
    public void setNormalizationFeature(final String name, final boolean state)
            throws DOMException {
        
        throw new DOMException(
                DOMException.NOT_SUPPORTED_ERR,
                "Feature [setNormalizationFeature] is not supported.");
    }

    /**
     * Look up the value of a feature.
     * <br>The feature name has the same form as a DOM <code>hasFeature</code> 
     * string. The recognized features are the same as the ones defined for 
     * <code>setNormalizationFeature</code>.
     * @param name The name of the feature to look up.
     * @return The current state of the feature (<code>true</code> or 
     *   <code>false</code>).
     * @exception DOMException
     *   NOT_FOUND_ERR: Raised when the feature name is not recognized.
     * @since DOM Level 3
     * @see org.w3c.dom.Document#getNormalizationFeature(java.lang.String)
     */
    public boolean getNormalizationFeature(final String name) 
    		throws DOMException {
        return false;
    }

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
     * @exception DOMException
     *   NOT_SUPPORTED_ERR: Raised when the type of the specified node is 
     *   neither <code>ELEMENT_NODE</code> nor <code>ATTRIBUTE_NODE</code>.
     * @since DOM Level 3
     * @see org.w3c.dom.Document#renameNode(
     * 		org.w3c.dom.Node, java.lang.String, java.lang.String)
     */
    public final Node renameNode(final Node node, 
                           final String namespaceURI, 
                           final String name) throws DOMException {
        
        AbstractNode newNode;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            newNode = (AbstractNode) this.createElementNS(namespaceURI, name);
            newNode.setInternalId(((AbstractNode) node).internalId);
            
            Node parent = node.getParentNode();
            newNode.setParentNode((AbstractParentNode) parent);
            
            List list = ((NodeListImpl) parent.getChildNodes()).getList();
            
            list.add(list.indexOf(node), newNode);
            list.remove(node);
        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            newNode = (AbstractNode) this.createAttributeNS(namespaceURI, name);
            newNode.setInternalId(((AbstractNode) node).internalId);
            
            Element elem = ((AttrImpl) node).getOwnerElement();

            elem.removeAttribute(node.getNodeName());
            elem.setAttributeNode((AttrImpl) newNode);
        } else {
            throw new DOMException(
                    DOMException.NOT_SUPPORTED_ERR, 
                    "Only Element and Attr nodes can be renamed.");
        }
        
        this.cache.change((AbstractNode) node, newNode);
        
        return newNode;
    }
    
    //
    // Methods from de.xplib.nexd.engine.xml.dom.AbstractParentNode
    //
    
    /**
     * Removes the child node indicated by <code>oldChild</code> from the list 
     * of children, and returns it.
     * 
     * @param oldChild The node being removed.
     * @return The node removed.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if <code>oldChild</code> is not a child of 
     *   this node.
     *   <br>NOT_SUPPORTED_ERR: if this node if of type <code>Document</code>, 
     *   this exception might be raised if the DOM implementation doesn't 
     *   support the removal of the <code>DocumentType</code> child or the 
     *   <code>Element</code> child.
     * @version DOM Level 3
     * @see org.w3c.dom.Node#removeChild(org.w3c.dom.Node)
     */
    public Node removeChild(final Node oldChild) throws DOMException {

        super.removeChild(oldChild);
        if (oldChild == this.doctype) {
            Object nullIt = null;
            this.doctype = (DocumentTypeImpl) nullIt;
        } else if (oldChild == this.documentElement) {
            Object nullIt = null;
            this.documentElement = (ElementImpl) nullIt;
        }
        
        return oldChild;
    }
    
    /**
     * Inserts the node <code>newChild</code> before the existing child node 
     * <code>refChild</code>. If <code>refChild</code> is <code>null</code>, 
     * insert <code>newChild</code> at the end of the list of children.
     * <br>If <code>newChild</code> is a <code>DocumentFragment</code> object, 
     * all of its children are inserted, in the same order, before 
     * <code>refChild</code>. If the <code>newChild</code> is already in the 
     * tree, it is first removed.
     * @param newChild The node to insert.
     * @param refChild The reference node, i.e., the node before which the 
     *   new node must be inserted.
     * @return The node being inserted.
     * @exception DOMException
     *   HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not 
     *   allow children of the type of the <code>newChild</code> node, or if 
     *   the node to insert is one of this node's ancestors or this node 
     *   itself, or if this node if of type <code>Document</code> and the 
     *   DOM application attempts to insert a second 
     *   <code>DocumentType</code> or <code>Element</code> node.
     *   <br>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created 
     *   from a different document than the one that created this node.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly or 
     *   if the parent of the node being inserted is readonly.
     *   <br>NOT_FOUND_ERR: Raised if <code>refChild</code> is not a child of 
     *   this node.
     *   <br>NOT_SUPPORTED_ERR: if this node if of type <code>Document</code>, 
     *   this exception might be raised if the DOM implementation doesn't 
     *   support the insertion of a <code>DocumentType</code> or 
     *   <code>Element</code> node.
     * @version DOM Level 3
     * @see org.w3c.dom.Node#insertBefore(org.w3c.dom.Node, org.w3c.dom.Node)
     */
    public Node insertBefore(final Node newChild, final Node refChild)
            throws DOMException {
    	
    	Node result = null;
    	if (newChild instanceof Text) {
    		result = newChild;
    	} else {
    		result = super.insertBefore(newChild, refChild);
    	    if (newChild instanceof DocumentTypeImpl) {
                ((DocumentTypeImpl) newChild).ownerDocument = this;
            }
    	}
        return result;
    }
    
    /**
     * <Some description here>
     * 
     * @param nodeIn
     * @throws DOMException
     * @see de.xplib.nexd.engine.xml.dom.AbstractParentNode#acceptNode(
     * 		org.w3c.dom.Node)
     */
    protected int acceptNode(final Node nodeIn) throws DOMException {
        
        short pos = ADD_BOTTOM;
        if (nodeIn instanceof DocumentTypeImpl) {
            if (this.doctype == null) {
                this.doctype = (DocumentTypeImpl) nodeIn;
                this.doctype.setParentNode(this);       
            } else {
                throw new DOMException(
                        DOMException.HIERARCHY_REQUEST_ERR,
                        "There is allready a doctype.");     
            }
            pos = ADD_TOP;
        } else if (nodeIn.getOwnerDocument() == this) {
            if (nodeIn instanceof ElementImpl) {
                if (this.documentElement == null) {
                    this.documentElement = (ElementImpl) nodeIn;
                } else {
                    throw new DOMException(
                            DOMException.HIERARCHY_REQUEST_ERR,
                            "There is allready a document element.");
                }
            } else if (nodeIn instanceof AttrImpl 
                    || nodeIn instanceof CDATASectionImpl
                    || nodeIn instanceof EntityReferenceImpl
                    || nodeIn instanceof TextImpl) {

                throw new DOMException(
                        DOMException.HIERARCHY_REQUEST_ERR,
                        "Nodes of this type[" + nodeIn + "] are not allowed.");
            }
        } else {
            throw new DOMException(
                    DOMException.WRONG_DOCUMENT_ERR,
                    "The node is from another Document.");
        } 
        return pos;
    }
    
    
    
    //
    // Methods inherit from de.xplib.nexd.store.StorageObjectI
    //
    
    /**
     * Comment for <code>resourceId</code>
     */
    private String resourceId =null;
    
    /**
     * The document id.
     */
    private String documentId =null;
    
    /**
     * Getter method for the document id.
     * 
     * @return The document id.
     */
    public String getDocumentId() {
        return this.documentId;
    }
    
    /**
     * Setter method for the document id.
     * 
     * @param docIdIn The document id.
     */
    public void setDocumentId(final String docIdIn) {
        this.documentId = docIdIn;
    }
    
    /**
     * Returns the id of the <code>StorageObjectI</code>. This id is equal
     * with the return value of the <code>SixdmlResource.getId()</code>
     * method.
     * 
     * @return The resource id.
     * @see de.xplib.nexd.store.StorageObjectI#getOID()
     */
    public String getOID() {
        return this.resourceId;
    }

    /**
     * Setter method for the resource id.
     * 
     * @param resIdIn The resource id.
     */
    public void setResourceId(final String resIdIn) {
        this.resourceId = resIdIn;
    }
    
    /**
     * Returns the type of the <code>StorageObjectI</code>. In this case the
     * return value is <code>StorageObjectI.XML</code>.
     * 
     * @return The type of the resource.
     * @see de.xplib.nexd.store.StorageObjectI#getType()
     */
    public byte getType() {
        return StorageObjectI.XML;
    }
    
    /**
     * Used <code>DocumentCacheImpl</code> instance. This object holds all 
     * changed nodes for a later update operation.
     */
    protected DocumentCacheImpl cache = new DocumentCacheImpl();
    
    /**
     * Is the document loading at the moment? If yes don't cache any changed.
     */
    private boolean loading = false;
    
    /**
     * Says that loading was stopped. This has the effect that you cannot change
     * the value of <code>loading</code> anymore.
     */
    private boolean loadingStopped = false;
    
    /**
     * Getter method for the used <code>DocumentCacheImpl</code>, which holds 
     * all changed node for a later storage update.
     * 
     * @return The used <code>DocumentCacheImpl</code> instance.
     */
    public DocumentCacheI getCache() {
        return this.cache;
    }
    
    /**
     * Returns <code>true</code> if the <code>Document</code> is in athe initial
     * loading process.
     * 
     * @return Is the document in loading process?
     */
    public boolean isLoading() {
        return this.loading;
    }
    
    /**
     * Starts the loading process.
     */
    public void startLoading() {
        if (!this.loadingStopped) {
            this.loading = true;
        }
    }
    
    /**
     * Stops the loading process.
     *
     */
    public void stopLoading() {
        this.loading = false;
        this.loadingStopped = true;
    }
    
    
    /**
     * Returns the underlying DOM document.
     * 
     * @return The underlying Document.
     * @see de.xplib.nexd.store.StorageDocumentObjectI#getContent()
     */
    public DOMDocumentI getContent() {
        return this;
    }
    
    /**
     * Returns a exception message for not supported node types.
     * 
     * @param nodeIn The node that is not supported.
     * @return The exception message.
     */
    protected String getNotSupported(final Node nodeIn) {
        return "This nodeType[" + nodeIn.getNodeType() + "] is not supported.";
    }
}
