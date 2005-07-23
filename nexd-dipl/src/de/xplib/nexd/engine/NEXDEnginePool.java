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
 * $Log: NEXDEnginePool.java,v $
 * Revision 1.2  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.9  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.8  2005/04/19 07:24:50  nexd
 * Refactoring, now config is a singleton.
 *
 * Revision 1.7  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.6  2005/04/10 13:18:46  nexd
 * New JUnit test cases and minor bug fixes.
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
package de.xplib.nexd.engine;

import java.util.NoSuchElementException;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.engine.config.ConfigurationException;
import de.xplib.nexd.engine.config.EngineConfig;
import de.xplib.nexd.store.AbstractStorageFactory;

/**
 * Represents a singleton.
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @role __Creator
 * @version $Revision: 1.2 $
 */
public final class NEXDEnginePool {

    /**
     * Factory class that is required by the
     * <code>org.apache.commons.pool.ObjectPool</code> for the creation of
     * object instances.
     */
    private class NEXDEngineFactory implements PoolableObjectFactory {

        /**
         * Comment for <code>config</code>
         */
        private final transient EngineConfig config;

        /**
         * @param configIn The configuration.
         */
        public NEXDEngineFactory(final EngineConfig configIn) {
            super();

            this.config = configIn;
        }

        /**
         * Reinitialize an instance to be returned by the pool.
         *
         * @param obj the instance to be activated
         * @see org.apache.commons.pool.PoolableObjectFactory#activateObject(
         *      java.lang.Object)
         */
        public void activateObject(final Object obj) {
        }

        /**
         * Destroys an instance no longer needed by the pool.
         *
         * @param obj the instance to be destroyed
         * @see org.apache.commons.pool.PoolableObjectFactory#destroyObject(
         *      java.lang.Object)
         */
        public void destroyObject(final Object obj) {
        }

        /**
         * Creates an instance that can be returned by the pool.
         *
         * @return an instance that can be returned by the pool.
         * @see org.apache.commons.pool.PoolableObjectFactory#makeObject()
         */
        public Object makeObject() {
            return new NEXDEngineImpl(this.config);
        }

        /**
         * Uninitialize an instance to be returned to the pool.
         *
         * @param obj the instance to be passivated
         * @see org.apache.commons.pool.PoolableObjectFactory#passivateObject(
         *      java.lang.Object)
         */
        public void passivateObject(final Object obj) {
        }

        /**
         * Ensures that the instance is safe to be returned by the pool.
         * Returns <tt>false</tt> if this object should be destroyed.
         *
         * @param obj the instance to be validated
         * @return <tt>false</tt> if this <i>obj</i> is not valid and should
         *         be dropped from the pool, <tt>true</tt> otherwise.
         * @see org.apache.commons.pool.PoolableObjectFactory#validateObject(
         *      java.lang.Object)
         */
        public boolean validateObject(final Object obj) {
            return false;
        }
    }

    /**
     * Unique single instance of <code>StoragePool</code>. The concrete instance
     * is created lazy. This property is part of the GoF Singleton Pattern.
     * @label singleton
     * @see NEXDEnginePool#getInstance()
     */
    private static NEXDEnginePool instance = null;

    /**
     * A pool that contains a user defined amount of <code>NEXDEngineI</code>
     * instance.
     * @associates <{de.xplib.nexd.engine.NEXDEngineImpl}>
     * @clientCardinality 1
     * @directed true
     * @label manages instances
     * @link aggregationByValue
     * @see NEXDEnginePool#getEngine()
     * @supplierCardinality 1..*
     */
    private transient GenericObjectPool enginePool = null;

    /**
     * Singleton Method that creates an unique instance of
     * <code>NEXDEnginePool</code>.  If there is no instance created yet, it
     * will be done on demand.
     *
     * @return A unique instance of <code>NEXDEnginePool</code>.
     * @exception de.xplib.nexd.engine.config.ConfigurationException If
     *            something is wrong configured, or the
     *            <code>nexd-storage.xml</code> file couldn't be found.
     */
    public static NEXDEnginePool getInstance() throws ConfigurationException {
        if (instance == null) {
            instance = new NEXDEnginePool();
        }
        return instance;
    }

    /**
     * Constructor of the <code>NEXDEnginePool</code>. The construtor reads the
     * <code>nexd-engine.xml</code> file and makes all required system settings.
     *
     * @exception de.xplib.nexd.engine.config.ConfigurationException If
     *            something is wrong configured, or the
     *            <code>nexd-engine.xml</code> file couldn't be found.
     */
    protected NEXDEnginePool() throws ConfigurationException {
        super();

        EngineConfig cfg = EngineConfig.getInstance();

        // setup the engine pool
        this.enginePool = new GenericObjectPool(new NEXDEngineFactory(cfg), cfg
                .getMaxConnections());
        this.enginePool.setMaxWait(1000);

        // if not null the the storage factory key, otherwise the developer 
        // has to define this system property.
        if (cfg.getStorage() != null) {
            System.setProperty(AbstractStorageFactory.STORAGE_FACTORY_KEY, cfg
                    .getStorage());
        }
    }

    /**
     * Factory method that creates new instances or returns an existing instance
     * of <code>NEXDEngineImpl</code>. How much instance are available is
     * defined in the <code>nexd-engine.xml</code> configuration file.
     *
     * @exception XMLDBException If something goes wrong during connection
     *                           phase. Or if the maximum number of clients is
     *                           connected to the NEXD XML-Database engine.
     * @return An instance of <code>NEXDEngineImpl</code>
     */
    public NEXDEngineImpl getEngine() throws XMLDBException {

        NEXDEngineImpl engine;
        try {
            engine = (NEXDEngineImpl) this.enginePool.borrowObject();
        } catch (NoSuchElementException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, 1,
                    "The maximum number of clients ["
                            + this.enginePool.getMaxActive()
                            + "] is connected to the database");
        } catch (Exception e) {
            throw new XMLDBException(ErrorCodes.UNKNOWN_ERROR,
                    "An unknown connection error occured.");
        }
        return engine;
    }

    /**
     * Frees the input <code>engine</code>, so that this instance can be used by
     * others.
     *
     * @param engine The instance of <code>NEXDEngineImpl</code> that will be
     *               released and can than be used by others.
     */
    protected void releaseEngine(final NEXDEngineImpl engine) {
        try {
            this.enginePool.returnObject(engine);
        } catch (Exception e) {
            LogFactory.getLog(NEXDEnginePool.class).error(e.getMessage());
        }

        if (this.enginePool.getNumActive() == 0) {
            this.enginePool.clear();
        }
    }

}
