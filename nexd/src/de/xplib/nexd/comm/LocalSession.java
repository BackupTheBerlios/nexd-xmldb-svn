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
package de.xplib.nexd.comm;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class LocalSession implements SessionI {

    /**
     * 
     */
    public LocalSession() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * <Some description here>
     * 
     * @param username ..
     * @param password ..
     * @throws ConnectionException ..
     * @see de.xplib.nexd.comm.SessionI#open(java.lang.String, java.lang.String)
     */
    public void open(final String username, final String password)
            throws ConnectionException {
        
    }

}
