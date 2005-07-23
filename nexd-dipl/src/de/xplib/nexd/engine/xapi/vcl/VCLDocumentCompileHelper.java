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
 * $Log: VCLDocumentCompileHelper.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.VCLSchema;

/**
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @stereotype Helper
 * @version $Revision: 1.2 $
 */
public final class VCLDocumentCompileHelper {

    /**
     * 
     */
    private VCLDocumentCompileHelper() {
        super();
    }
    
    /**
     * Creates the schema element for a pre compiled virtual resource.
     * 
     * @param docIn The schema document.
     * @return the create pcvr:schema element.
     */
    public static Element declareSchema(final Document docIn) {
        Element schema = docIn.getDocumentElement();

        String vclPrefix = schema.getPrefix();
        schema.removeAttribute("xmlns:" + vclPrefix);
        schema.setAttribute(
                "xmlns:" + PCVResource.NAMESPACE_PREFIX, 
                PCVResource.NAMESPACE_URI);
        schema.setPrefix(PCVResource.NAMESPACE_PREFIX);
        
        return schema;
    }

    /**
     * Inserts an attribute element after that element, that declares a vcl 
     * attribute.
     * 
     * @param elemIn The element that declares the vcl attribute.
     * @param nameIn The name for the attribute.
     * @param valueIn The value of the attribute.
     * @return The created attriobute element.
     */
    public static Element declaredAttribute(final Element elemIn,
                                            final String nameIn,
                                            final String valueIn) {
        
        Element elem = null;
        
        // try to find a previous defined attribute element and use it
        NodeList nodes = elemIn.getChildNodes();
        for (int i = 0, l = nodes.getLength(); i < l; i++) {
            Node node = nodes.item(i);
            if (node instanceof Element 
                    && node.getNodeName().equals(PCVResource.QNAME_ATTR)
                    && ((Element) node).getAttribute(
                            PCVResource.ATTR_ATTR_NAME).equals(
                                    nameIn)) {
                
                elem = (Element) node;
                break;
            }
        }
        
        // no attribute element exists for name, so declare it!
        if (elem == null) {
            elem = elemIn.getOwnerDocument().createElementNS(
                    PCVResource.NAMESPACE_URI, PCVResource.QNAME_ATTR);
            
            elem.setAttribute(PCVResource.ATTR_ATTR_NAME, nameIn);
            elemIn.insertBefore(elem, elemIn.getFirstChild());
        }
        elem.setAttribute(PCVResource.ATTR_ATTR_VALUE, valueIn);
        
        return elem;
    }
    
    /**
     * Declares a pcvr:resource in a pre compiled virtual resource.
     * 
     * @param elem The collection reference element.
     * @return A new pcvr:resouzce element.
     */
    public static Element declareResource(final Element elem) {
        
        Element instance = null;
        
        NodeList nodes = elem.getChildNodes();
        
        int length = nodes.getLength();
        if (length > 1 && !(nodes.item(0).getNodeName().equals(
                PCVResource.QNAME_RESOURCE))) {
            
            instance = elem.getOwnerDocument().createElementNS(
                    PCVResource.NAMESPACE_URI, PCVResource.QNAME_RESOURCE);
            
            
            for (int i = length - 1; i >= 0; i--) {
                Node node = nodes.item(i);
                elem.removeChild(node);
                instance.insertBefore(node, instance.getFirstChild());
            }
            
            elem.appendChild(instance);
        } else if (length > 0) {
            instance = (Element) nodes.item(0);
            for (int i = length - 1; i > 0; i--) {
                elem.removeChild(nodes.item(i));
            }
        }
        
        return instance;
    }
    
    /**
     * Declares the first pcvr:resource in a pre compiled virtual resource.
     * 
     * @param elem The collection reference element.
     * @return A new pcvr:resouzce element.
     * @throws InvalidVCLSchemaException If the schema doesn't provide the 
     *         minimum requirements for a virtual collection language schema.
     */
    public static Element declareRootResource(final Element elem) 
            throws InvalidVCLSchemaException {
        
        boolean invalid = false;
        
        Element instance = null;
        NodeList nodes = elem.getChildNodes();
        
        for (int i = 0, l = nodes.getLength(); i < l; i++) {
            Node child = nodes.item(i);
            if (child instanceof Element) {
                String nsUri = child.getNamespaceURI();
                
                if (nsUri != null) {
                    if (nsUri.equals(VCLSchema.NAMESPACE_URI)) {
                        continue;
                    } else if (nsUri.equals(PCVResource.NAMESPACE_URI)
                            && child.getNodeName().equals(
                                    PCVResource.QNAME_RESOURCE)) {
                        
                        instance = (Element) child;
                        break;
                    }
                }
                // check that instance is not set
                if (instance != null) {
                    invalid = true;
                    break;
                }
                instance = elem.getOwnerDocument().createElementNS(
                        PCVResource.NAMESPACE_URI, PCVResource.QNAME_RESOURCE);
                elem.insertBefore(instance, child);
                elem.removeChild(child);
                instance.appendChild(child);
            }
        }
        
        if (invalid) {
            throw new InvalidVCLSchemaException();
        }
        
        return instance;
    }
}
