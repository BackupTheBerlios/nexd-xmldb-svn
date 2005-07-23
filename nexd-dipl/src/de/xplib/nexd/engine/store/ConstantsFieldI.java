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
 * $Log: ConstantsFieldI.java,v $
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 */
package de.xplib.nexd.engine.store;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public interface ConstantsFieldI {

    /**
     * Constant used for the "id" field.
     */
    String FIELD_ID = "id";
    
    /**
     * Constant used for the "pid" field.
     */
    String FIELD_PID = "pid";
    
    /**
     * Constant used for the "rid" field.
     */
    String FIELD_RID = "rid";
    
    /**
     * Constant used for the "rgt" field.
     */
    String FIELD_RGT = "rgt";
    
    /**
     * Constant used for the "lft" field.
     */
    String FIELD_LFT = "lft";
    
    /**
     * Constant ised for the "type" field.
     */
    String FIELD_TYPE = "type";
    
    /**
     * Constant used for the "name" field.
     */
    String FIELD_NAME = "name";
    
    /**
     * Constant used for the "rname" field.
     */
    String FIELD_RNAME = "rname";
    
    /**
     * Constant used for the "val" field.
     */
    String FIELD_VALUE = "val";
    
    /**
     * Constant used for "cnt" (count) queries.
     */
    String FIELD_COUNT = "cnt";
}
