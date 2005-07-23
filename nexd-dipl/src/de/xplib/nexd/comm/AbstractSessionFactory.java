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

/*
 * $Log: AbstractSessionFactory.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.6  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.5  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.4  2005/03/26 12:14:20  nexd
 * UML documentation.
 *
 * Revision 1.3  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.comm;

import de.xplib.nexd.engine.comm.SOAPSession;


/**
 * <p>The session factory is used to create a concrete session type, for the
 * access to the xml database NEXD. In the default implementation the two
 * possible session types as {@link SOAPSession} and
 * {@link de.xplib.nexd.engine.comm.LocalSession}.</p>
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public abstract class AbstractSessionFactory {

    /**
     * @return An instance of <code>AbstractSessionFactory</code>.
     * @throws SessionConfigurationException ..
     */
    public static AbstractSessionFactory newInstance()
            throws SessionConfigurationException {

        String className = System
                .getProperty("de.xplib.nexd.comm.AbstractSessionFactory");
        if (className == null) {
            className = "de.xplib.nexd.engine.comm.DefaultSessionFactory";
        }

        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new SessionConfigurationException(
                    "Bad classpath, cannot find AbstractSessionFactory"
                            + " class [" + className + "].");
        }

        AbstractSessionFactory factory;
        try {
            factory = (AbstractSessionFactory) clazz.newInstance();
        } catch (ClassCastException e) {
            throw new SessionConfigurationException("Class [" + className
                    + "] doesn't" + " inherit from AbstractSessionFactory.");
        } catch (InstantiationException e) {
            throw new SessionConfigurationException(
                    "Cannot instantiate class [" + className + "].", e);
        } catch (Exception e) {
            throw new SessionConfigurationException(
                    "Unknown exception for class [" + className + "].", e);
        }
        return factory;
    }

    /**
     * @clientCardinality 1
     * @clientRole session
     * @directed true
     * @label creates instance
     * @link aggregation
     * @supplierCardinality 0..*
     */
    /*#de.xplib.nexd.comm.SessionI lnkSessionI*/

    /**
     * <p>Constructor.</p>
     */
    protected AbstractSessionFactory() {
        super();
    }

    /**
     * Factory method for a remote <code>SessionI</code> instance. A remote
     * session is identified by a host name and a port number.
     *
     * @param host The remote host to contact.
     * @param port The port where the remote session is listening.
     * @return An instance of <code>SessionI</code>
     * @exception SessionConfigurationException If something is missconfigured.
     */
    public abstract SessionI newSession(final String host, final String port)
            throws SessionConfigurationException;

    /**
     * Factory method for a local <code>SessionI</code> instance. This means the
     * API and the real database are running in the same Java Virtual Machine.
     *
     * @return An instance of <code>SessionI</code>
     * @exception SessionConfigurationException If something is missconfigured.
     */
    public abstract SessionI newSession() throws SessionConfigurationException;

}