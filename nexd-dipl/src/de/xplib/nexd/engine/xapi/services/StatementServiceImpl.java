/*
 * Project: nexd 
 * Copyright (C) 2004  Manuel Pichler <manuel.pichler@xplib.de>
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
package de.xplib.nexd.engine.xapi.services;

import org.sixdml.SixdmlDatabase;
import org.sixdml.command.SixdmlPreparedStatement;
import org.sixdml.command.SixdmlStatement;
import org.sixdml.command.SixdmlStatementService;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.xapi.PreparedStatementImpl;
import de.xplib.nexd.engine.xapi.StatementImpl;

/**
 * StatementServiceImpl.java
 *
 * This is a Service that enables users to create objects for issuing SiXDML 
 * statements over the database. 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class StatementServiceImpl 
    extends AbstractService
    implements SixdmlStatementService {
    
    /**
     * The associated <code>SixdmlDatabase</code>.
     */
    private final SixdmlDatabase database;

    /**
     * Constructor.
     * 
     * @param engineIn The used <code>NEXDEngineI</code> instance.
     * @param dbIn The associated <code>SixdmlDatabase</code> instance.
     */
    public StatementServiceImpl(final NEXDEngineI engineIn,
                                final SixdmlDatabase dbIn) {
        super(engineIn, "SixdmlStatementService", DEFAULT_VERSION);
        
        this.database = dbIn;
    }

    /**
     * Creates an implementation of the <code>SixdmlStatement</code> interface 
     * for executing SiXDML statements over the database. Parameter-less SiXDML 
     * statements should be executed via <code>SixdmlStatement</code> objects 
     * although statements that are executed several times may benefit from 
     * using a <code>SixdmlPreparedStatement</code> if the database implements 
     * certain optimizations. 
     * 
     * @return A <code>SixdmlStatement</code> object.
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.command.SixdmlStatementService#createStatement()
     */
    public SixdmlStatement createStatement() throws XMLDBException {
        return new StatementImpl(this.database);
    }

    /**
     * Creates an implementation of the <code>SixdmlPreparedStatement</code> 
     * interface for executing parameterized SiXDML statements over the 
     * database. <code>SixdmlPreparedStatement</code> objects are also useful 
     * for implementations that support precompilation of SiXDML statements so 
     * that statements that are to be executed multiple times can be stored and 
     * precompiled once in a single object and not many times to increase 
     * efficiency. 
     * 
     * @param stmtIn this is a SiXDML statement that contains one or more ? as 
     *              placeholders for an XPath expression, an XML fragment or 
     *              post-processing operation (such as an XQuery expression)
     * @return A <code>SixdmlPreparedStatement</code> object. 
     * @exception XMLDBException If a database error occurs. 
     * @see org.sixdml.command.SixdmlStatementService#prepareStatement(
     *      java.lang.String)
     */
    public SixdmlPreparedStatement prepareStatement(final String stmtIn)
            throws XMLDBException {

        return new PreparedStatementImpl(stmtIn, this.database);
    }

}
