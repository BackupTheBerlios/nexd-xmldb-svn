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

import java.lang.reflect.Constructor;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public abstract class SessionFactory {
    
    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Rev$
     */
    protected class SessionFactoryImpl extends SessionFactory {
     
        /**
         * 
         */
        protected SessionFactoryImpl() {
            super();
        }
        
        public SessionI newSession() {
            
            return new LocalSession();
        }
        
        public SessionI newSession(final String host,
                                   final String port) {
            return null;
        }
    }
    
    /**
     * @return An instance of <code>SessionFactory</code>.
     * @throws SessionConfigurationException ..
     */
    public static SessionFactory newInstance() 
    		throws SessionConfigurationException {
        
        String className = System.getProperty(
                "de.xplib.nexd.comm.SessionFactory");
        if (className == null) {
            className = "de.xplib.nexd.comm.SessionFactory$SessionFactoryImpl";
        }
        
        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new SessionConfigurationException(
                    "Bad classpath, cannot find SessionFactory"
                    + " class [" + className + "].");
        }
        
        SessionFactory factory;
        try {
            try {
                clazz.getDeclaredConstructor(new Class[0]);
                
                factory = (SessionFactory) clazz.newInstance();
            } catch (NoSuchMethodException e) {
                // Create an instance of the inner class SessionFactory
                Constructor c = clazz.getDeclaredConstructor(
                        new Class[]{SessionFactory.class});
                
                factory = (SessionFactory) c.newInstance(
                        new Object[] {new SessionFactory() {
                            public SessionI newSession(final String h, 
                                                       final String p) {
                                return null;
                            }
                            public SessionI newSession() {
                                return null;
                            }
                        }
                    });
            }
        } catch (ClassCastException e) {
            throw new SessionConfigurationException(
                    "Class [" + className + "] doesn't" 
                    + " inherit from SessionFactory.");
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
     * 
     */
    protected SessionFactory() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * @param host
     * @param port
     * @return
     */
    public abstract SessionI newSession(final String host, 
                                        final String port)
                                          throws SessionConfigurationException;
    
    public abstract SessionI newSession() throws SessionConfigurationException;
    
    
    /**
     * @param args ..
     * @throws Exception ..
     */
    public static void main(final String[] args) throws Exception {
        System.out.println(SessionFactory.newInstance());
    }

}
