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
 * $Log: StorageValidationObjectI.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.store;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public interface StorageValidationObjectI extends StorageEntityI {

    /**
     * @label belongs to
     */
    /*#de.xplib.nexd.store.StorageCollectionI Dependency_Link*/

    /**
     * @label used to validate
     */

    /*#de.xplib.nexd.store.StorageDocumentObjectI Dependency_Link1*/

    /**
     * Constant that identifies a <code>StorageValidationObjectImpl</code> as a
     * DTD.
     */
    byte DTD = 1;

    /**
     * Constant that identifies a <code>StorageValidationObjectImpl</code> as a
     * XML-Schema.
     */
    byte XML_SCHEMA = 2;

    /**
     * Getter method for the schema content.
     *
     * @return The schema content.
     */
    String getContent();

    /**
     * Getter method for the type of the schema.
     *
     * @return Type of the schema.
     */
    byte getType();
}