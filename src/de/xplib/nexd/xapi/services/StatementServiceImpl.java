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
package de.xplib.nexd.xapi.services;

import org.sixdml.command.SixdmlPreparedStatement;
import org.sixdml.command.SixdmlStatement;
import org.sixdml.command.SixdmlStatementService;
import org.xmldb.api.base.XMLDBException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class StatementServiceImpl 
    extends AbstractService
    implements SixdmlStatementService {

    /**
     * 
     */
    public StatementServiceImpl() {
        super("SixdmlStatementService", DEFAULT_VERSION);
    }

    /**
     * <Some description here>
     * 
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.command.SixdmlStatementService#createStatement()
     */
    public SixdmlStatement createStatement() throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <Some description here>
     * 
     * @param query
     * @return
     * @throws org.xmldb.api.base.XMLDBException
     * @see org.sixdml.command.SixdmlStatementService#prepareStatement(
     *      java.lang.String)
     */
    public SixdmlPreparedStatement prepareStatement(final String query)
            throws XMLDBException {
        // TODO Auto-generated method stub
        return null;
    }

}
