/*
 * Project: xmldb-manager 
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
 * $Log: Client.java,v $
 * Revision 1.1  2005/04/12 08:34:26  nexd
 * Initial import
 *
 */
package de.xplib.xdbm;

import de.xplib.xdbm.ui.Application;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class Client {
    
    /**
     * 
     */
    protected Client() {
        
    }

    /**
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        
        Application.getInstance();
    }
}