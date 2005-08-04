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
 * $Log: Config.java,v $
 * Revision 1.1  2005/04/12 08:34:21  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.collections.FastHashMap;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.xplib.xdbm.ui.model.Connection;
import de.xplib.xdbm.ui.model.PluginFile;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public final class Config {

    /**
     * Comment for <code>CONFIG_NS</code>
     */
    public static final String CONFIG_NS 
            = "http://xdbm.xplib.de/config/version/1.0";
    
    /**
     * Holds singleton instance
     */
    private static Config instance;
    
    /**
     * Comment for <code>cfgFile</code>
     */
    private File cfgFile;
    
    /**
     * Comment for <code>locale</code>
     */
    private Locale locale;
    
    /**
     * Comment for <code>dbDrivers</code>
     */
    private FastHashMap dbDrivers = new FastHashMap();
    
    /**
     * Comment for <code>dbUris</code>
     */
    private FastHashMap dbUris = new FastHashMap();
    
    /**
     * Connection model.
     */
    private transient Connection connection = null;
    
    /**
     * Comment for <code>connections</code>
     */
    private FastArrayList connections = new FastArrayList();
    
    private FastHashMap plugins = new FastHashMap();
    
    /**
     * Comment for <code>visibleTabs</code>
     */
    private FastHashMap visibleTabs = new FastHashMap();
    
    /**
     * Comment for <code>observers</code>
     */
    private FastArrayList observers = new FastArrayList();

    /**
     * prevents instantiation
     */
    private Config() {
        
        this.dbDrivers.setFast(true);
        this.dbUris.setFast(true);
        this.connections.setFast(true);
        this.plugins.setFast(true);
        this.visibleTabs.setFast(true);
        this.observers.setFast(true);
        
        this.readConfig();
    }

    /**
     * Returns the singleton instance.
     * 
     * @param typeIn The type of manager (ui or commandline)
     * @return The singleton instance
     */
    public static Config getInstance(final String typeIn) {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
    
    /**
     * @return Does any config exists?
     */
    public boolean exists() {
        System.out.println(
                this.cfgFile.getAbsolutePath() + " " + this.cfgFile.exists());
        return this.cfgFile.exists();
    }
    
    /**
     * @return The current Locale
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * @param localeIn The current Locale
     */
    public void setLocale(final Locale localeIn) {
        this.locale = localeIn;
    }
    
    /**
     * @return The map with all database drivers.
     */
    public Map getDrivers() {
        return this.dbDrivers;
    }
    
    /**
     * @return The class name or an empty string.
     */
    public String getDriverClass(final String jarIn) {
        if (this.dbDrivers.containsKey(jarIn)) {
            return (String) this.dbDrivers.get(jarIn);
        }
        return "";
    }
    
    /**
     * Adds a Driver in a jar file to the configutation.
     * 
     * @param jarFileIn The jar file.
     * @param classNameIn The driver class.
     */
    public void putDriver(final String jarFileIn, final String classNameIn) {
        this.dbDrivers.put(jarFileIn, classNameIn);
    }
    
    /**
     * Removes a jar file with its database driver and all connection uris from 
     * the config.
     * 
     * @param jarFileIn The jar file to remove.
     */
    public void removeDriver(final String jarFileIn) {
        if (this.dbDrivers.containsKey(jarFileIn)) {
            this.dbDrivers.remove(jarFileIn);
        }
    }
    
    /**
     * Adds a database uri for a special jar file to the config.
     * 
     * @param jarIn The jar file which is the key.
     * @param uriIn The database uri.
     */
    public void putDatabaseURI(final String jarIn, final String uriIn) {
        if (!this.dbUris.containsKey(jarIn)) {
            FastArrayList list = new FastArrayList();
            list.setFast(true);
            this.dbUris.put(jarIn, list);
        }
        ArrayList uris = (ArrayList) this.dbUris.get(jarIn);
        uris.add(uriIn);
    }
    
    /**
     * @param jarIn The jar file.
     * @return A List with the uris.
     */
    public List getDatabaseURIs(final String jarIn) {
        if (this.dbUris.containsKey(jarIn)) {
            return (List) this.dbUris.get(jarIn);
        }
        return new FastArrayList();
    }
    
    /**
     * @param The connection model.
     */
    public void setConnection(final Connection connectionIn) {
        this.connection = connectionIn;
    }
    
    /**
     * @return The connection model.
     */
    public Connection getConnection() {
        return this.connection;
    }
    
    /**
     * @param connectionIn A new created connection
     */
    public void addConnection(final Connection connectionIn) {
        
        Iterator it = this.connections.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o.equals(connectionIn)) {
                this.connections.remove(o);
                break;
            }
        }
        
        this.connections.add(0, connectionIn);
        
        if (this.connections.size() > 10) {
            this.connections.remove(10);
        }
        
        this.notifyObservers();
    }
    
    /**
     * @return The last 10 database connections.
     */
    public List getConnections() {
        return this.connections;
    }
    
    /**
     * @param container
     * @param tabs
     */
    public void setVisibleTabs(Class container, Class[] tabs) {
        if (this.visibleTabs.containsKey(container.getName())) {
            this.visibleTabs.remove(container.getName());
        }
    }
    
    public Map getPluginFiles() {
        return this.plugins;
    }
    
    public PluginFile getPluginFile(final String id) {
        if (this.plugins.containsKey(id)) {
            return (PluginFile) this.plugins.get(id);
        }
        return null;
    }
    
    /**
     * @param observerIn ...
     */
    public void addObserver(final ConfigObserver observerIn) {
        this.observers.add(observerIn);
    }
    
    /**
     * @param observerIn ....
     */
    public void removeObserver(final ConfigObserver observerIn) {
        if (this.observers.contains(observerIn)) {
            this.observers.remove(observerIn);
        }
    }
    
    /**
     * <Some description here>
     * 
     * @throws Throwable
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable {
        this.save();
        super.finalize();
    }
    
    /**
     * 
     */
    protected void notifyObservers() {
        for (int i = 0, s = this.observers.size(); i < s; i++) {
            ((ConfigObserver) this.observers.get(i)).update(this);
        }
    }
    
    /**
     * 
     */
    public void save() {
        
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance()
                                        .newDocumentBuilder()
                                        .newDocument();
            
            Element config = (Element) doc.appendChild(
                    doc.createElementNS(CONFIG_NS, "config"));
            config.setAttribute("xmlns", CONFIG_NS);
            
            Element ui = (Element) config.appendChild(
                    doc.createElementNS(CONFIG_NS, "ui-settings"));
            
            Node lang = ui.appendChild(
                    doc.createElementNS(CONFIG_NS, "language"));
            lang.appendChild(doc.createTextNode(this.locale.getLanguage()));
            
            Element drivers = (Element) config.appendChild(
                    doc.createElementNS(CONFIG_NS, "drivers"));
            Iterator dIt = this.dbDrivers.keySet().iterator();
            while (dIt.hasNext()) {
                
                String jar = (String) dIt.next();
                
                Element driver = (Element) drivers.appendChild(
                        doc.createElementNS(CONFIG_NS, "driver"));
                
                driver.setAttribute("jar", jar);
                driver.setAttribute("class", (String) this.dbDrivers.get(jar));
                
                if (!this.dbUris.containsKey(jar)) {
                    continue;
                }
                
                String value = "";
                
                ArrayList uris = (ArrayList) this.dbUris.get(jar);
                for (int i = 0, l = uris.size(); i < l; i++) {
                    value += uris.get(i) + "||";
                }
                value = value.substring(0, value.length() - 2);
                
                driver.setAttribute("uris", value);
            }
            
            Element conns = (Element) config.appendChild(doc.createElementNS(
                    CONFIG_NS, "connections"));
            
            for (int i = 0, s = this.connections.size(); i < s; i++) {
                
                Connection conn = (Connection) this.connections.get(i);
                
                Element conne = (Element) conns.appendChild(doc.createElementNS(
                        CONFIG_NS, "connection"));
                conne.setAttribute("jar", conn.getJarFile());
                conne.setAttribute("class", conn.getClassName());
                conne.setAttribute("uri", conn.getUri());
            }
            
            
            Element panels = (Element) config.appendChild(doc.createElementNS(
                    CONFIG_NS, "plugins"));
            
            Iterator it = this.plugins.keySet().iterator();
            while (it.hasNext()) {
                
                PluginFile pluginFile = (PluginFile) this.plugins.get(it.next());
                
                Element elem = (Element) panels.appendChild(doc.createElementNS(
                        CONFIG_NS, "plugin"));
                elem.setAttribute("jar", pluginFile.getJarFile());
                elem.setAttribute("class", pluginFile.getClassName());
                elem.setAttribute("id", pluginFile.getId());
                elem.setAttribute("position", pluginFile.getPosition());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
         
        if (doc != null) {
            
            OutputFormat of = new OutputFormat(doc);
            of.setIndenting(true);
            of.setIndent(1);
            of.setStandalone(true);
            
            StringWriter sw  = new StringWriter();
            XMLSerializer xs = new XMLSerializer(sw, of);
            xs.setOutputCharStream(sw);
            
            try {
                xs.serialize(doc);
                
                sw.flush();
                
                System.out.println(sw.toString());
                
                sw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            
            try {
                FileWriter fw = new FileWriter(this.cfgFile);
                fw.write(sw.toString());
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 
     */
    private void readConfig() {
        
        File etc  = new File("/etc");
        File xdbm = new File("/etc/xdbm");
        File home = new File(System.getenv("HOME") + "/.xdbm");
        
        boolean root = false;
        if (!etc.canWrite()) {
            if (!home.exists()) {
                home.mkdir();
            }
        } else {
            if (!xdbm.exists()) {
                xdbm.mkdir();
            }
            root = true;
        }
        
        File global = new File(xdbm.getAbsolutePath() + "/ui-config.xml");
        File user   = new File(home.getAbsolutePath() + "/ui-config.xml");
        
        this.cfgFile = (root ? global : user);
        
        if (!global.exists() && !user.exists()) {
            // fallback try current location
            System.out.println("conf");
            user = new File("conf/ui-config.xml");
            System.out.println(user.getAbsolutePath());
            if (!user.exists()) {
                return;
            }
            this.cfgFile = user;
        }
        // if exist first read global
        if (global.exists()) {
            this.parseConfig(global);
        }
        // overwrite globals with user settings
        if (user.exists()) {
            this.parseConfig(user);
        }
    }
    
    /**
     * @param fileIn ...
     */
    private void parseConfig(final File fileIn) {
        
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance()
                                                       .newDocumentBuilder();
            
            Document doc = db.parse(fileIn);
            
            //NodeList nl = doc.getElementsByTagNameNS(CONFIG_NS, "language");
            NodeList nl = doc.getElementsByTagName("language");
            if (nl.getLength() > 0) {
                this.locale = new Locale(nl.item(0).getTextContent());
            }
            
            nl = doc.getElementsByTagName("driver");
            for (int i = 0, l = nl.getLength(); i < l; i++) {
                Element driver = (Element) nl.item(i);
                
                String jar    = driver.getAttribute("jar");
                String clazz  = driver.getAttribute("class");
                String[] uris = driver.getAttribute("uris").split("\\|\\|");
                
                
                ArrayList ulist = new ArrayList();
                for (int j = 0; j < uris.length; j++) {
                    ulist.add(uris[j]);
                }
                
                this.dbDrivers.put(jar, clazz);
                this.dbUris.put(jar, ulist);
            }
            
            nl = doc.getElementsByTagName("connection");
            for (int i = 0, l = nl.getLength(); i < l; i++) {
                Element driver = (Element) nl.item(i);
                
                String jar   = driver.getAttribute("jar");
                String clazz = driver.getAttribute("class");
                String uri   = driver.getAttribute("uri");
                
                this.connections.add(new Connection(jar, clazz, uri));
            }
            
            nl = doc.getElementsByTagName("plugin");
            for (int i = 0, l = nl.getLength(); i < l; i++) {
                Element driver = (Element) nl.item(i);
                
                String jar   = driver.getAttribute("jar");
                String clazz = driver.getAttribute("class");
                String id    = driver.getAttribute("id");
                String pos   = driver.getAttribute("position");
                
                this.plugins.put(id, new PluginFile(id, clazz, jar, pos));
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

}
