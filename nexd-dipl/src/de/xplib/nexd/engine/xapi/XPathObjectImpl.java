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
package de.xplib.nexd.engine.xapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.query.SixdmlXpathObject;
import org.sixdml.query.SixdmlXpathObjectType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.nexd.comm.NEXDEngineI;

/**
 * XPathObjectImpl.java
 *
 * This class represents the results of an XPath query on a document or 
 * collection. Later implementations may want to use this as a base type for a 
 * hierarchy of types based on the XPath type system.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class XPathObjectImpl implements SixdmlXpathObject {
    
    /**
     * The used logging instance.
     */
    private static final Log LOG = LogFactory.getLog(XPathObjectImpl.class);
    
    /**
     * Default <code>null</code> value.
     */
    private static final String NULL = null;
    
    /**
     * The queries <code>ResourceSet</code> instance.
     */
    private final ResourceSet set;
    
    /**
     * The type of this object.
     */
    private final SixdmlXpathObjectType type;
    
    /**
     * The value of this object.
     */
    private final String value;

    /**
     * Construtor.
     * 
     * @param setIn The queried <code>ResourceSet</code> instance.
     * @throws XMLDBException If a database error occures.
     */
    public XPathObjectImpl(final ResourceSet setIn) throws XMLDBException {
        super();
                
        this.set = setIn;
        
        long size = setIn.getSize();
        
        if (size == 0) {
            this.type  = UNKNOWN;
            this.value = NULL;
        } else if (size == 1) {
            
            String pre = NEXDEngineI.QUERY_RESULT_PREFIX + ":";
            
            Document doc = (Document) 
                    ((XMLResource) setIn.getResource(0)).getContentAsDOM();
            Element elem = doc.getDocumentElement();
            if (elem.getTagName().equals(pre + NEXDEngineI.QUERY_RESULT_TAG)) {
                
                String tmp = this.getElementValue(elem);
                
                if (tmp == null) {
                    this.type = UNKNOWN;
                } else if (tmp.toLowerCase(Locale.GERMANY).equals("true") 
                        || tmp.toLowerCase(Locale.GERMANY).equals("false")) {
                    
                    this.type = BOOLEAN;
                    tmp = tmp.toLowerCase(Locale.GERMANY);
                } else {
                    boolean number = true;
                    try {
                        Integer.parseInt(tmp);
                    } catch (NumberFormatException e) {
                        try {
                            Float.parseFloat(tmp);
                        } catch (NumberFormatException e1) {
                            number = false;
                        }
                    }
                    
                    if (number) {
                        this.type = NUMBER;
                    } else {
                        this.type = STRING;
                    }
                }
                this.value = tmp;
            } else {
                this.type  = TREE_FRAGMENT;
                this.value = NULL;
            }
        } else {
            this.type  = NODESET;
            this.value = NULL;
        }
    }

    /**
     * Get the type of this object as a SixdmlXpathObjectType.
     * 
     * @return The type of this object. 
     * @see org.sixdml.query.SixdmlXpathObject#getType()
     */
    public SixdmlXpathObjectType getType() {
        return this.type;
    }

    /**
     * Returns the contents of the object as an XML string if it is a nodeset 
     * or <code>null</code> otherwise.
     *  
     * @return The entire contents of the object as a string if it is a nodeset 
     *         or <code>null</code> otherwise.
     * @see org.sixdml.query.SixdmlXpathObject#getNodeSetAsXML()
     */
    public String getNodeSetAsXML() {

        String xml = null;
        if (this.type == NODESET) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                
                OutputFormat format = new OutputFormat();
                format.setOmitXMLDeclaration(true);

                XMLSerializer ser = new XMLSerializer(format);
                ser.setOutputByteStream(baos);
                
                for (long i = 0, l = this.set.getSize(); i < l; i++) {
                    ser.serialize((Element) ((Document) 
                            ((SixdmlResource) this.set.getResource(i))
                            .getContentAsDOM()).getDocumentElement());
                    baos.write('\n');
                }
                
                xml = baos.toString();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            } catch (XMLDBException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return xml;
    }

    /**
     * Returns the object as a boolean.
     *  
     * @return The object as a boolean.
     * @see org.sixdml.query.SixdmlXpathObject#getObjectAsBoolean()
     */
    public boolean getObjectAsBoolean() {
        boolean bValue = false;
        if (this.type == BOOLEAN) {
            bValue = this.value.equals("true");
        }
        return bValue;
    }

    /**
     * Returns the object as a string. For a nodeset, this method returns the 
     * text form of the first element in the list. To retrieve the XML for the 
     * entire node list, call the getNodeSetAsXML() method.
     *      
     * @return The object as a string.
     * @see #getNodeSetAsXML()
     * @see org.sixdml.query.SixdmlXpathObject#getObjectAsString()
     */
    public String getObjectAsString() {
        String sValue = this.value;
        if (this.type == NODESET || this.type == TREE_FRAGMENT) {
            try {
                Document doc = (Document) 
                        ((XMLResource) this.set.getResource(0)
                                ).getContentAsDOM();
                Element elem = doc.getDocumentElement();
                Node node    = null; 
                
                if (elem.getTagName().startsWith(
                        NEXDEngineI.QUERY_RESULT_PREFIX + ":")) {
                    node = elem.getFirstChild();
                } else {
                    node = elem;
                }
                
                // if it is an element serialize it.
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    
                    OutputFormat format = new OutputFormat();
                    format.setOmitXMLDeclaration(true);
                    format.setIndent(2);
                    format.setIndenting(true);
                    
                    XMLSerializer ser = new XMLSerializer(format);
                    ser.setOutputByteStream(baos);
                    
                    ser.serialize((Element) node);
                    
                    sValue = baos.toString();
                    
                // every other node type, just get content.
                } else {
                    sValue = node.getNodeValue();
                }
            } catch (IOException e) {
                sValue = "";
            } catch (XMLDBException e) {
                sValue = "";
            }
        }
        return sValue;
    }

    /**
     * Returns the object as a double.
     *  
     * @return The object as a double.
     * @see org.sixdml.query.SixdmlXpathObject#getObjectAsNumber()
     */
    public double getObjectAsNumber() {
        double dValue = 0;
        if (this.type == NUMBER) {
            dValue = Double.parseDouble(this.value);
        }
        return dValue;
    }

    /**
     * Returns the object as a DOM node list. If the actual result is not a node
     * list, null is returned.
     * 
     * @return the object as a DOM node list.
     * @see org.sixdml.query.SixdmlXpathObject#getObjectAsNodeSet()
     */
    public NodeList getObjectAsNodeSet() {
        
        NodeList nodeSet = null;
        try {
            final ArrayList list = new ArrayList();
            for (long i = 0, l = this.set.getSize(); i < l; i++) {
                list.add(this.set.getResource(i));
            }
            nodeSet = new NodeList() {
                public int getLength() {
                    return list.size();
                }
                public Node item(final int indexIn) {
                    return (Node) list.get(indexIn);
                }
            };
        } catch (XMLDBException e) {
            LOG.error(e.getMessage(), e);
        }
        return nodeSet;
    }
    
    /**
     * Returns the value for the given <code>elemIn</code>.
     * @param elemIn The element that contains the value as text content or
     *               attribute.
     * @return The text value of the given element.
     */
    protected final String getElementValue(final Element elemIn) {
        
        String val = null;
        
        // single child value
        if (elemIn.hasChildNodes()) {
            val = elemIn.getFirstChild().getNodeValue();
        // just an attribute
        } else {
            NamedNodeMap nnm = elemIn.getAttributes();
            for (int i = 0, l = nnm.getLength(); i < l; i++) {
                String name = nnm.item(i).getNodeName();
                if (!name.startsWith(NEXDEngineI.QUERY_RESULT_PREFIX + ":") 
                        && !name.startsWith("xmlns:")) {
                    
                    val = nnm.item(i).getNodeValue();
                    break;
                }
            }
        }
        return val;
    }

}
