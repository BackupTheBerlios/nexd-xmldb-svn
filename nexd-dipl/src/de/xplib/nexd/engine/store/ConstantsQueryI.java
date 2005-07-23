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
 * $Log: ConstantsQueryI.java,v $
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
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
 * @version $Revision: 1.2 $
 */
public interface ConstantsQueryI extends ConstantsTableI, ConstantsFieldI {

    /**
     * This query is executed to test if a given id is allready used 
     * in the same context.
     */
    String QUERY_CONTAINS_ID = "SELECT coll.id, res.id FROM\n"
                             + "  " + TABLE_COLLECTION + " AS coll,\n"
                             + "  " + TABLE_RESOURCE   + " AS RES\n"
                             + "WHERE\n"
                             + "  (coll.name=? AND coll.pid=?) OR\n"
                             + "  (res.name=? AND res.cid=?)";
    
    /**
     * Comment for <code>QUERY_INSERT_COLL</code>
     */
    String QUERY_INSERT_COLL = "INSERT INTO " + TABLE_COLLECTION + "\n"
                             + "  (" + FIELD_ID + ", " + FIELD_PID 
                             + ", " + FIELD_TYPE + ", " + FIELD_NAME + ")\n"
                             + "VALUES\n"
                             + "  (?, ?, ?, ?)";
    
    /**
     * Comment for <code>QUERY_COLL_COUNT</code>
     */
    String QUERY_COLL_COUNT = "SELECT COUNT(*) AS " + FIELD_COUNT + " FROM\n"
                            + "  " + TABLE_COLLECTION + " AS coll\n"
                            + "WHERE\n"
                            + "  coll." + FIELD_PID + "=?";
    
    /**
     * Comment for <code>QUERY_CHILD_COLLS</code>
     */
    String QUERY_CHILD_COLLS = "SELECT coll." + FIELD_NAME + " FROM\n" 
                             + "  " + TABLE_COLLECTION + " AS coll\n"
                             + "WHERE\n" 
                             + "  coll." + FIELD_PID + "=?";
}
