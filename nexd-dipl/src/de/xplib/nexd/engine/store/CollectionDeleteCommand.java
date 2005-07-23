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
 * $Log: CollectionDeleteCommand.java,v $
 * Revision 1.1  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 */
package de.xplib.nexd.engine.store;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class CollectionDeleteCommand {
    
    /**
     * Comment for <code>PREFIX</code>
     */
    private static final String COMMAND = 
        "DELETE FROM " + ConstantsTableI.TABLE_COLLECTION + " WHERE id=";
    
    /**
     * Comment for <code>conn</code>
     */
    private final Connection conn;

    /**
     * Constructor.
     * 
     * @param connIn The used database connection.
     */
    public CollectionDeleteCommand(final Connection connIn) {
        super();
        
        this.conn = connIn;
    }
    
    
    /**
     * @param collIdIn The collection id.
     * @return The num,ber of affected rows.
     * @throws SQLException If any sql specific goes wrong.
     */
    public int execute(final int collIdIn) throws SQLException {
        Statement stmt = this.conn.createStatement();
        
        return stmt.executeUpdate(COMMAND + collIdIn);
    }

}
