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
package de.xplib.nexd.store.jdbc;

import de.xplib.nexd.ConfigurationException;
import de.xplib.nexd.store.StorageFactory;
import de.xplib.nexd.store.StorageI;
/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * @author manuel
 */
public class StorageFactoryImpl extends StorageFactory {
	
	/**
	 * 
	 * @return Instance of Storage
     * @throws ConfigurationException This is thrown if the jdbc driver is not
     *                                configured or the class cannot be found.
	 */
	public final StorageI newStorage() throws ConfigurationException {
		return new StorageImpl();
	}
}
