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
 * $Log: JarClassFinder.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class JarClassFinder {

    /**
     * Comment for <code>ucl</code>
     */
    protected URLClassLoader ucl;
    
    /**
     * Comment for <code>jar</code>
     */
    protected JarFile jar;
    
    /**
     * Comment for <code>callback</code>
     */
    private JarClassFinderCallback callback = null;
    
    /**
     * @param pathIn ..
     * @throws IOException ..
     * @throws MalformedURLException ..
     */
    public JarClassFinder(final String pathIn) throws IOException,
                                                      MalformedURLException {
        this(new URL(pathIn));
    }
    
    /**
     * @param fileIn ..
     * @throws IOException ..
     * @throws MalformedURLException ..
     */
    public JarClassFinder(final File fileIn) throws IOException,
                                                    MalformedURLException {
        this(fileIn.toURL());
    }
    
    /**
     * @param urlIn ..
     * @throws IOException ..
     */
    public JarClassFinder(final URL urlIn) throws IOException {
        this.ucl = new URLClassLoader(
                new URL[] {urlIn}, this.getClass().getClassLoader());
        this.jar = new JarFile(urlIn.getFile());
    }
    
    /**
     * @return The number of files
     */
    public int getSize() {
        return this.jar.size();
    }
    
    /**
     * @param callbackIn ..
     */
    public void setCallback(final JarClassFinderCallback callbackIn) {
        this.callback = callbackIn;
    }
    
    /**
     * @param classIn ..
     * @return ..
     */
    public Class findClassByInterface(final Class classIn) {
        int pos = 0;
        Enumeration en = this.jar.entries();
        while (en.hasMoreElements()) {
            JarEntry je = (JarEntry) en.nextElement();
            
            String name = je.getName();
            
            if (this.callback != null) {
                this.callback.call(++pos, name);
            }
            
            if (name.endsWith(".class") && name.indexOf('$') == -1) {
                name = name.substring(0, name.length() - 6);
                name = name.replaceAll("/", ".");
                
                Class c;
                try {
                    c = this.ucl.loadClass(name);
                } catch (ClassNotFoundException e) {
                    continue;
                } catch (IllegalAccessError e1) {
                    continue;
                } catch (NoClassDefFoundError e1)  {
                    continue;
                }
                
                if (c.isInterface() || (c.getModifiers() & Modifier.ABSTRACT)
                        == Modifier.ABSTRACT) {
                    continue;
                }
                
                if (classIn.isAssignableFrom(c)) {
                    return c;
                }
            }
        }
        return null;
    }
    
    /**
     * @param classesIn ..
     * @return ..
     */
    public Class findClassByInterfaces(final Class[] classesIn) {
        return null;
    }
    
    /**
     * @param classIn ..
     * @return ..
     */
    public Class[] findClassesByInterface(final Class classIn) {
        return null;
    }
    
    /**
     * @param classesIn ..
     * @return ..
     */
    public Class[] findClassesByInterfaces(final Class[] classesIn) {
        return null;
    }
    
    /**
     * @return ..
     */
    public ClassLoader getClassLoader() {
        return this.ucl;
    }
    
}
