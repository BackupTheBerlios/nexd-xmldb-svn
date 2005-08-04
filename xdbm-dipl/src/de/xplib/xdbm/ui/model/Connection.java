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
 * $Log: Connection.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.ui.model;

import java.io.File;


/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class Connection {

    /**
     * The database jar file.
     */
    private String jarFile;
    
    /**
     * The database implementation class.
     */
    private String className;
    
    /**
     * The database uri.
     */
    private String uri;
    
    public Connection(final String jarFileIn,
                      final String classNameIn,
                      final String uriIn) {
        
        this.setJarFile(jarFileIn);
        this.setClassName(classNameIn);
        this.setUri(uriIn);
    }
    
    /**
     * @return Returns the className.
     */
    public final String getClassName() {
        return className;
    }
    
    /**
     * @param classNameIn The className to set.
     */
    public final void setClassName(final String classNameIn) {
        this.className = classNameIn;
    }
    
    /**
     * @return Returns the jarFile.
     */
    public final String getJarFile() {
        String file = this.jarFile;
        if (!file.startsWith("file://")) {
            file = "file://" + new File(this.jarFile).getAbsoluteFile();
        }
        return file;
    }
    
    /**
     * @param jarFileIn The jarFile to set.
     */
    public final void setJarFile(final String jarFileIn) {
        this.jarFile = jarFileIn;
    }
    
    /**
     * @return Returns the uri.
     */
    public final String getUri() {
        return uri;
    }
    
    /**
     * @param uriIn The uri to set.
     */
    public final void setUri(final String uriIn) {
        this.uri = uriIn;
    }
    
    /**
     * <Some description here>
     * 
     * @param objIn
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object objIn) {
        if (!(objIn instanceof Connection)) {
            return false;
        }
        
        Connection cmp = (Connection) objIn;
        return (cmp.className.equals(this.className)
                && cmp.jarFile.equals(this.jarFile)
                && cmp.uri.equals(this.uri));
    }
}
