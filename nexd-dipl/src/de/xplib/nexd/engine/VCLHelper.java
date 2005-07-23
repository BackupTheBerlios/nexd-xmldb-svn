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
 * $Log: VCLHelper.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.vcl.AbstractVCLParser;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.VCLCollectionReference;
import de.xplib.nexd.api.vcl.VCLParserI;
import de.xplib.nexd.api.vcl.VCLSchema;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public final class VCLHelper {
    
    /**
     * Constructor.
     */
    private VCLHelper() {
        super();
    }
    
    /**
     * Returns the <code>VCLSchema</code> for the given <code>Document</code>.
     * 
     * @param vcsIn The DOM representation of the vcl-schema.
     * @return An instance of <code>VCLSchema</code> for the given 
     *         <code>vcsIn</code>.
     * @throws XMLDBException If any database specific error occures.
     */
    public static VCLSchema getVCLSchema(final Document vcsIn) 
            throws XMLDBException {
        
        try {
            VCLParserI parser = AbstractVCLParser.getInstance();
            
            StringWriter writer = new StringWriter();
            XMLSerializer ser = new XMLSerializer();
            ser.setOutputCharStream(writer);
            
            ser.serialize(vcsIn);
            
            writer.flush();
            
            VCLSchema schema = parser.parse(new ByteArrayInputStream(
                    writer.getBuffer().toString().getBytes()));
            
            writer.close();
            
            return schema;
        } catch (IOException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (SAXException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (InvalidVCLSchemaException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * Reads all &lt;...:collection&gt; <code>Element</code>s from the input 
     * DOM <code>Document</code> and returns them as <code>NodeList</code>.
     * 
     * @param vcsIn The DOM representation of the vcl-schema.
     * @return A list with all &lt;...:collection&gt; <code>Element</code>s,
     */
    public static NodeList getCollectionReferences(final Document vcsIn) {
        
        String prefix    = extractNamespacePrefix(vcsIn);
        Element vclElem  = vcsIn.getDocumentElement();
        
        return vclElem.getElementsByTagName(
                prefix + ":" + VCLCollectionReference.ELEM_NAME);
    }

    /**
     * Returns the namespace prefix that is used for the virtual collection
     * language tags.
     * 
     * @param vcsIn The DOM representation of the vcl-schema.
     * @return The namespace for the virtual collection language tags.
     */
    public static String extractNamespacePrefix(final Document vcsIn) {
        
        Element vclElem  = vcsIn.getDocumentElement();
        NamedNodeMap nnm = vclElem.getAttributes();
        
        String prefix = VCLSchema.NAMESPACE_PREFIX;
        for (int j = 0, l = nnm.getLength(); j < l; j++) {
            Node node = nnm.item(j);
            if (node.getNodeName().startsWith("xmlns:")
                    && node.getNodeValue().equals(
                            VCLSchema.NAMESPACE_URI)) {
                
                prefix = node.getNodeName().substring(6);
                break;
            }
        }
        return prefix;
    }
}
