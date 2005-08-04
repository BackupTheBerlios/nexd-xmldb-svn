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
 * $Log: VCLSchemaObject.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import java.io.StringWriter;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xmldb.api.base.Collection;

import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.xdbm.ui.tree.CollectionTree;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class VCLSchemaObject 
    extends DefaultMutableTreeNode 
    implements CollectionObject, TreeObject {
    
    private VirtualCollectionObject vcoll;
    
    private String content = null;

    /**
     * 
     */
    public VCLSchemaObject(final CollectionTree treeIn,
                          final VirtualCollectionObject vcollIn,
                          final VCLSchema schemaIn) {
        super(schemaIn);
        
        this.vcoll = vcollIn;
    }
    
    public Collection getCollection() {
        return this.vcoll.getCollection();
    }
    
    /**
     * @return
     */
    public String getContent() {
        
        if (this.content == null) {
            try {
                Document doc = (Document) 
                        ((VCLSchema) this.userObject).getContentAsDOM();
                
                OutputFormat of = new OutputFormat(doc);
                of.setIndenting(true);
                of.setIndent(2);
                
                StringWriter sw  = new StringWriter();
                XMLSerializer xs = new XMLSerializer(sw, of);
                
                
                xs.serialize(doc);
                sw.flush();
                this.content = sw.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.content;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#isDeleted()
     */
    public boolean isDeleted() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * <Some description here>
     * 
     * 
     * @see de.xplib.xdbm.ui.model.UIObject#setDeleted()
     */
    public void setDeleted() {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#isChanged()
     */
    public boolean isChanged() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * <Some description here>
     * 
     * @param b
     * @see de.xplib.xdbm.ui.model.UIObject#setChanged(boolean)
     */
    public void setChanged(boolean b) {
        // TODO Auto-generated method stub

    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#isNew()
     */
    public boolean isNew() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * <Some description here>
     * 
     * @param b
     * @see de.xplib.xdbm.ui.model.UIObject#setNew(boolean)
     */
    public void setNew(boolean b) {
        // TODO Auto-generated method stub

    }

}
