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
 * $Log: DefaultSessionFactory.java,v $
 * Revision 1.2  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:33  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.comm;

import de.xplib.nexd.comm.AbstractSessionFactory;
import de.xplib.nexd.comm.SessionI;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DefaultSessionFactory extends AbstractSessionFactory {

    /**
     * @clientCardinality 1
     * @directed true
     * @label concrete product
     * @link aggregation
     * @supplierCardinality 0..*
     */

    /*#private SOAPSession lnkSOAPSession;*/

    /**
     * @clientCardinality 1
     * @directed true
     * @label concrete product
     * @link aggregation
     * @supplierCardinality 0..*
     */

    /*#private LocalSession lnkLocalSession;*/

    /**
     * <Some description here>
     *
     * @return
     * @see de.xplib.nexd.comm.AbstractSessionFactory#newSession()
     */
    public SessionI newSession() {
        return new LocalSession();
    }

    /**
     * <Some description here>
     *
     * @param host
     * @param port
     * @return
     * @see de.xplib.nexd.comm.AbstractSessionFactory#newSession(
     *      java.lang.String, java.lang.String)
     */
    public SessionI newSession(final String host, final String port) {
        return new SOAPSession(host, port);
    }
}