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
package de.xplib.nexd.engine.xapi.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlTransactionService;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.comm.NEXDEngineI;

/**
 * Simple dirty read transaction for the NEXD XML-Database.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public class TransactionServiceImpl
    extends AbstractService
    implements SixdmlTransactionService {
    
    /**
     * The used logging instance.
     */
    private static final Log LOG = LogFactory.getLog(
            TransactionServiceImpl.class);

    /**
     * Constructor.
     * 
     * @param engineIn The used <code>NEXDEngineI</code> object.
     * @param collIn The associated <code>SixdmlCollectio</code> object.
     */
    public TransactionServiceImpl(final NEXDEngineI engineIn, 
                                  final SixdmlCollection collIn) {
        
        super(engineIn, collIn, "TransactionService", DEFAULT_VERSION);
    }

    /**
     * Returns <code>true</code> if there is currently a transaction open that 
     * is neither committed nor aborted; returns <code>false</code> otherwise.
     * 
     * @return <code>true</code> if there is currently a transaction open that 
     *         is neither committed nor aborted; returns <code>false</code> 
     *         otherwise.
     * @see org.sixdml.dbmanagement.SixdmlTransactionService#isActive()
     */
    public boolean isActive() {
        boolean active = false;
        try {
            active = (this.ctxColl.getProperty(
                    NEXDEngineI.TRANSACTION_ID_KEY) != null);
        } catch (XMLDBException e) {
            LOG.error(e.getMessage(), e);
        }
        return active;
    }

    /**
     * Begin the transaction.
     *
     * @exception XMLDBException With expected error codes.<br />
     *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *  specific errors that occur.<br /> 
     * @see org.xmldb.api.modules.TransactionService#begin()
     */
    public void begin() throws XMLDBException {
        this.engine.beginTransaction(this.ctxColl);
    }

    /**
     * Commit the transaction.
     *
     * @exception XMLDBException With expected error codes.<br />
     *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
     *  specific errors that occur.<br />
     * @see org.xmldb.api.modules.TransactionService#commit()
     */
    public void commit() throws XMLDBException {
        this.engine.commitTransaction(this.ctxColl);
    }

    /**
    * Rollback the transaction.
    *
    * @exception XMLDBException With expected error codes.<br />
    *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *  specific errors that occur.<br />
     * @see org.xmldb.api.modules.TransactionService#rollback()
     */
    public void rollback() throws XMLDBException {
        throw new XMLDBException(
                ErrorCodes.NOT_IMPLEMENTED, "Rollback is not supported");
    }

}
