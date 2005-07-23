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
package de.xplib.nexd.engine;

import java.util.Iterator;
import java.util.Random;

import org.apache.commons.collections.FastHashMap;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public final class TransactionManager {
    
    /**
     * Unique <code>Transaction</code> instance used for the GoF Singleton
     * pattern.
     * @label singleton
     */
    private static TransactionManager instance;
    
    /**
     * A <code>FastHashMap</code> object containing the transaction ids and
     * the corresponding paths.
     */
    private final transient FastHashMap openTrans;
    
    /**
     * GoF Singleton pattern in a special synchronized version.
     * 
     * @return The unique instance of <code>TransactionManager</code>.
     */
    public static TransactionManager getInstance() {
        if (instance == null) {
            synchronized (TransactionManager.class) {
                instance = new TransactionManager();
            }
        }
        return instance;
    }

    /**
     * Constructor.
     */
    private TransactionManager() {
        super();
        
        this.openTrans = new FastHashMap();
        this.openTrans.setFast(true);
    }
    
    
    /**
     * Starts a new transaction, if possible. This means the given 
     * <code>pathIn</code> isn't already in another transaction.
     * 
     * @param pathIn The start path for the transaction.
     * @return A unique transaction identifier.
     * @throws XMLDBException If the given <code>pathIn</code> is already part
     *                        of another transaction.
     */
    public String begin(final String pathIn) throws XMLDBException {
        
        if (this.hasTransaction(pathIn)) {
            throw new XMLDBException(
                    ErrorCodes.VENDOR_ERROR, 
                    "The collection for path [" + pathIn 
                    + "] is locked by a transaction.");
        }
        
        String transId = null;
        
        synchronized (this.openTrans) {
            
            Random random = new Random();
            
            do {
                transId = Integer.toHexString(random.nextInt());
                if (this.openTrans.containsKey(transId)) {
                    continue;
                }
            } while (transId == null);
            
            this.openTrans.put(transId, pathIn);
        }
        
        return transId;
    }
    
    /**
     * Commits an open transaction and releases the blocked resources.
     * 
     * @param transIdIn The transaction identifier.
     * @throws XMLDBException If the given <code>transIdIn</code> doesn't exist.
     */
    public void commit(final String transIdIn) throws XMLDBException {
        synchronized (this.openTrans) {
            if (!this.openTrans.containsKey(transIdIn)) {
                throw new XMLDBException(
                        ErrorCodes.VENDOR_ERROR, 
                        "No matching transaction found.");
            }
            this.openTrans.remove(transIdIn);
        }
    }
    
    /**
     * Checks the given <code>pathIn</code> parameter for an existing 
     * transaction.
     * 
     * @param pathIn The path to check.
     * @return Returns <code>true</code> if the path is in a transaction, 
     *         otherwise it returns <code>false</code>.
     */
    public boolean hasTransaction(final String pathIn) {
        
        boolean hasTrans = false;
        synchronized (this.openTrans) {
            Iterator ite = this.openTrans.values().iterator();
            while (ite.hasNext()) {
                if (pathIn.startsWith((String) ite.next())) {
                    hasTrans = true;
                    break;
                }
            }
        }
        return hasTrans;
    }

}
