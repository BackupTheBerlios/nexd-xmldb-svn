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
 * $Log: ServiceConfig.java,v $
 * Revision 1.1  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 */
package de.xplib.nexd.engine.config;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class ServiceConfig {

    /**
     * Comment for <code>maxConnections</code>
     */
    private final int maxConnection;
    
    /**
     * Are remote query operations allowed.
     */
    private final boolean allowRemote;
    
    /**
     * Are remote changes operations allowed?
     */
    private final boolean allowRemoteChanges;

    /**
     * Constructor.
     * 
     * @param maxConnIn The maximum number of open connections.
     * @param remoteIn Are remote operations allowed?
     * @param remoteChangesIn Are remote change operations allowed?
     */
    public ServiceConfig(final int maxConnIn,
                         final boolean remoteIn,
                         final boolean remoteChangesIn) {
        super();
        
        this.maxConnection      = maxConnIn;
        this.allowRemote        = remoteIn;
        this.allowRemoteChanges = remoteChangesIn;
    }
    
    /**
     * Are remote operations allowed?
     * 
     * @return allowed?
     */
    public final boolean isAllowRemote() {
        return allowRemote;
    }
    
    /**
     * Are remote change operations allowed?
     * 
     * @return allowed?
     */
    public final boolean isAllowRemoteChanges() {
        return allowRemoteChanges;
    }
    
    /**
     * The maximum number of open connections.
     * 
     * @return Number of connections.
     */
    public final int getMaxConnection() {
        return maxConnection;
    }
}
