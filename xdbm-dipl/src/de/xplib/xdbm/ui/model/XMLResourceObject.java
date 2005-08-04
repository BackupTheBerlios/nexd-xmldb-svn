/*
 * Project: xmldb-manager 
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
 * $Log: XMLResourceObject.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import de.xplib.xdbm.ui.Application;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class XMLResourceObject extends XapiResourceObject {
    
    public static final String RESOURCE_TYPE = "XMLResource";
    
    protected String content = null;

    /**
     * @param collIn
     * @param resIn
     * @param typeIn
     */
    public XMLResourceObject(final Collection collIn, final XMLResource resIn) {
        super(collIn, resIn, RESOURCE_TYPE);
        // TODO Auto-generated constructor stub
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.ResourceObject#getContent()
     */
    public String getContent() {
        
        if (this.content == null) {
            
            StringWriter sw = new StringWriter();
            OutputFormat of = new OutputFormat();
            of.setIndenting(true);
            of.setIndent(2);
            
            XMLSerializer xs = new XMLSerializer(sw, of);
            
            try {
                xs.serialize((Document) 
                        ((XMLResource) this.userObject).getContentAsDOM());
                
                sw.flush();
                this.content = sw.toString();
                sw.close();
            } catch (IOException e) {
                e.printStackTrace(Application.err);
            } catch (XMLDBException e) {
                e.printStackTrace(Application.err);
            }
        }
        
        return this.content;
    }

}
