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
 * $Log: PluginFile.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import de.xplib.xdbm.ui.Plugin;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class PluginFile {
    
    private String id = "";
    private String className = "";
    private String jarFile = "";
    private String position = "";
    
    private URLClassLoader classLoader = null;
    private Plugin plugin = null;

    /**
     * @param id
     * @param className
     * @param jarFile
     */
    public PluginFile(String id, String className, String jarFile, String positionIn) {
        super();
        this.id = id;
        this.className = className;
        this.jarFile = jarFile;
        this.position = positionIn;
    }
    /**
     * @return Returns the className.
     */
    public final String getClassName() {
        return className;
    }
    
    /**
     * @param className The className to set.
     */
    public final void setClassName(String className) {
        this.className = className;
    }
    /**
     * @return Returns the id.
     */
    public final String getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public final void setId(String id) {
        this.id = id;
    }
    /**
     * @return Returns the jarFile.
     */
    public final String getJarFile() {
        return jarFile;
    }
    /**
     * @param jarFile The jarFile to set.
     */
    public final void setJarFile(String jarFile) {
        this.jarFile = jarFile.trim();
    }
    
    public final String getPosition() {
        return this.position;
    }
    
    public final void setPosition(final String positionIn) {
        this.position = positionIn;
    }
    
    public Plugin createPlugin() throws FileNotFoundException,
                                        ClassNotFoundException,
                                        ClassCastException {
        
        if (this.plugin != null) {
            return this.plugin;
        }
        
        Class clazz;
        if (!this.jarFile.equals("")) {
            if (!this.jarFile.startsWith("file://")) {
                this.jarFile = "file://" + this.jarFile;
            }
            try {
                this.classLoader = new URLClassLoader(new URL[] {
                        new URL(this.jarFile)}, this.getClass().getClassLoader());
            } catch (MalformedURLException e) {
                throw new FileNotFoundException(this.jarFile);
            }
            
            clazz = this.classLoader.loadClass(this.className);
            
            
        } else {
            clazz = Class.forName(this.className);
        }
        
        Plugin plugin;
        try {
            plugin = (Plugin) clazz.newInstance();
        } catch (InstantiationException e1) {
            throw new ClassCastException();
        } catch (IllegalAccessException e1) {
            throw new ClassCastException();
        }
        this.plugin = plugin;
        
        return this.plugin;
    }
}
