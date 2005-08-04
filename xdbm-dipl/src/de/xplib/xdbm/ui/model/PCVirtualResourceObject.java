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
 * $Log: PCVirtualResourceObject.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import java.io.StringWriter;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xmldb.api.base.Collection;

import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.xdbm.ui.Application;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class PCVirtualResourceObject implements UIObject, CollectionObject {
    
    private VirtualResourceObject vresObj;
    
    private PCVResource pcvRes;
    
    private String content = null;

    /**
     * 
     */
    public PCVirtualResourceObject(VirtualResourceObject vresObjIn,
                                   PCVResource pcvResIn) {
        super();

        this.vresObj = vresObjIn;
        this.pcvRes  = pcvResIn;
    }
    
    public String getContent() {
        if (this.content != null) {
            return this.content;
        }
        
        
        StringWriter  sw = new StringWriter();
        OutputFormat  of = new OutputFormat();
        of.setIndenting(true);
        of.setIndent(2);
        
        XMLSerializer xs = new XMLSerializer(sw, of);
        try {
            xs.serialize((Document) this.pcvRes.getContentAsDOM());
            sw.flush();
            this.content = sw.toString();
            sw.close();
        } catch (Exception e) {
            e.printStackTrace(Application.err);
        }
        
        
        return this.content;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#getUserObject()
     */
    public Object getUserObject() {
        return this.pcvRes;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#isDeleted()
     */
    public boolean isDeleted() {
        return false;
    }

    /**
     * <Some description here>
     * 
     * 
     * @see de.xplib.xdbm.ui.model.UIObject#setDeleted()
     */
    public void setDeleted() {
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#isChanged()
     */
    public boolean isChanged() {
        return false;
    }

    /**
     * <Some description here>
     * 
     * @param b
     * @see de.xplib.xdbm.ui.model.UIObject#setChanged(boolean)
     */
    public void setChanged(boolean b) {
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.UIObject#isNew()
     */
    public boolean isNew() {
        return false;
    }

    /**
     * <Some description here>
     * 
     * @param b
     * @see de.xplib.xdbm.ui.model.UIObject#setNew(boolean)
     */
    public void setNew(boolean b) {
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.xplib.xdbm.ui.model.CollectionObject#getCollection()
     */
    public Collection getCollection() {
        return this.vresObj.coll;
    }

}
