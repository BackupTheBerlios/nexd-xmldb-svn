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
 * $Log: AbstractEncoder.java,v $
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 */
package de.xplib.nexd.engine.http.soap.xsd;

import de.fmui.spheon.jsoap.AbstractEncoding;
import de.fmui.spheon.jsoap.EncodingWrapperInterface;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class AbstractEncoder 
    extends AbstractEncoding 
    implements EncodingWrapperInterface {
    
    /**
     * The java type
     */
    protected Class type = null;
    
    /**
     * The soap type
     */
    protected String soapType = "";

    /**
     * <Some description here>
     * 
     * @return
     * @see de.fmui.spheon.jsoap.AbstractEncoding#getType()
     */
    public Class getType() {
        return this.type;
    }

    /**
     * <Some description here>
     * 
     * @return
     * @see de.fmui.spheon.jsoap.AbstractEncoding#getSoapType()
     */
    public String getSoapType() {
        return this.soapType;
    }

    /**
     * <Some description here>
     * 
     * @param typeIn
     * @param soapTypeIn
     * @see de.fmui.spheon.jsoap.EncodingWrapperInterface#set(
     *      java.lang.Class, java.lang.String)
     */
    public void set(final Class typeIn, final String soapTypeIn) {
        this.setType(typeIn);
        this.setSoapType(soapTypeIn);
    }

    /**
     * <Some description here>
     * 
     * @param typeIn
     * @see de.fmui.spheon.jsoap.EncodingWrapperInterface#setType(
     *      java.lang.Class)
     */
    public void setType(final Class typeIn) {
        this.type = typeIn;
    }

    /**
     * <Some description here>
     * 
     * @param soapTypeIn
     * @see de.fmui.spheon.jsoap.EncodingWrapperInterface#setSoapType(
     *      java.lang.String)
     */
    public void setSoapType(final String soapTypeIn) {
        this.soapType = soapTypeIn;
    }

}
