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
 * $Log: I18N.java,v $
 * Revision 1.1  2005/04/12 08:34:20  nexd
 * Initial import
 *
 */
package de.xplib.xdbm.util;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.swing.KeyStroke;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.collections.FastHashMap;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public final class I18N {
        
    /**
     * Holds singleton instance
     */
    private static I18N instance;
    
    /**
     * Comment for <code>entries</code>
     */
    private FastHashMap entries = new FastHashMap();
    
    /**
     * Comment for <code>observers</code>
     */
    private FastArrayList observers = new FastArrayList();

    /**
     * prevents instantiation
     */
    private I18N() {
        this.entries.setFast(true);
        this.observers.setFast(true);
        // prevent creation
    }

    /**
     * Returns the singleton instance.
     * @return	the singleton instance
     */
    public static I18N getInstance() {
        if (instance == null) {
            instance = new I18N();
        }
        return instance;
    }
    
    /**
     * @param keyIn ..
     * @return ...
     */
    public String getText(final String keyIn) {
        return this.getValue(keyIn, "text");
    }
    
    /**
     * @param keyIn ..
     * @param replaceIn .--
     * @return ..
     */
    public String getText(final String keyIn, final String[] replaceIn) {
        return this.getValue(keyIn, "text", replaceIn);
    }
    
    /**
     * @param keyIn ..
     * @return ...
     */
    public String getTitle(final String keyIn) {
        return this.getValue(keyIn, "title");
    }
    
    /**
     * @param keyIn ..
     * @param replaceIn .--
     * @return ..
     */
    public String getTitle(final String keyIn, final String[] replaceIn) {
        return this.getValue(keyIn, "title", replaceIn);
    }
    
    /**
     * @param keyIn ..
     * @return ..
     */
    public String getToolTip(final String keyIn) {
        return this.getValue(keyIn, "tooltip");
    }
    
    /**
     * @param keyIn ..
     * @param repIn ..
     * @return ..
     */
    public String getToolTip(final String keyIn, final String[] repIn) {
        return this.getValue(keyIn, "tooltip", repIn);
    }
    
    /**
     * @param keyIn ..
     * @return ..
     */
    public Integer getMnemonic(final String keyIn) {
        String s = this.getValue(keyIn, "mnemonic");
        if (s.equals("")) {
            return null;
        }
        return new Integer((int) s.charAt(0));
    }
    
    /**
     * @param keyIn ..
     * @return ..
     */
    public KeyStroke getAccelerator(final String keyIn) {
        
        String value = this.getValue(keyIn, "accelerator");
        if (value.startsWith(keyIn)) {
            return null;
        }
        
        String[] s = value.toUpperCase().split(",");
        if (s.length == 0 || s[0].equals("")) {
            return null;
        }
        
        int code = (int) s[0].charAt(0);
        int mod  = 0;
        for (int i = 1; i < s.length; i++) {
            char c = s[i].charAt(0);
            if (c == 'S') {
                mod = mod | ActionEvent.SHIFT_MASK;
            } else if (c == 'A') {
                mod = mod | ActionEvent.ALT_MASK; 
            } else if (c == 'C') {
                mod = mod | ActionEvent.CTRL_MASK;
            } else if (c == 'M') {
                mod = mod | ActionEvent.META_MASK;
            }
        }
        return KeyStroke.getKeyStroke(code, mod);
    }
    
    /**
     * @param localeIn ...
     */
    public void setLocale(final Locale localeIn) {
        
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(
              "de/xplib/xdbm/ui/res/i18n/" + localeIn.getLanguage() + ".xml");
        
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance()
                                                       .newDocumentBuilder();
            
            Document doc = db.parse(is);
            this.entries.clear();
            
            NodeList nl  = doc.getDocumentElement().getElementsByTagName(
                    "entry");
            
            for (int i = 0, l = nl.getLength(); i < l; i++) {
                NamedNodeMap nnm = nl.item(i).getAttributes();
                if (nnm == null) {
                    continue;
                }
               
                Node n = nnm.getNamedItem("key");
                if (n == null) {
                    continue;
                }
      
                this.entries.put(n.getNodeValue(), nnm);
            }
            
            this.notifyObservers();
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * @param observerIn ..
     */
    public void addObserver(final I18NObserver observerIn) {
        this.observers.add(observerIn);
    }
    
    /**
     * @param observerIn ..
     */
    public void removeObserver(final I18NObserver observerIn) {
        if (this.observers.contains(observerIn)) {
            this.observers.remove(observerIn);
        }
    }
    
    /**
     * 
     */
    protected void notifyObservers() {
       for (int i = 0, s = this.observers.size(); i < s; i++) {
           ((I18NObserver) this.observers.get(i)).updateI18N(this);
       }
    }
    
    /**
     * @param keyIn ..
     * @param nameIn ..
     * @param replaceIn ..
     * @return ..
     */
    private String getValue(final String keyIn, 
                            final String nameIn, 
                            final String[] replaceIn) {
        
        String value = this.getValue(keyIn, nameIn);
        for (int i = 0; i < replaceIn.length; i++) {
            if (replaceIn[i] != null) {
                value = value.replaceAll("\\{" + i + "\\}", replaceIn[i]);
            }
        }
        return value;
    }
    
    /**
     * @param keyIn ..
     * @param nameIn ..
     * @return ..
     */
    private String getValue(final String keyIn, final String nameIn) {
        if (!this.entries.containsKey(keyIn)) {
            return keyIn;
        }
        Node n = ((NamedNodeMap) this.entries.get(keyIn)).getNamedItem(nameIn);
        if (n == null) {
            return keyIn + ":" + nameIn;
        }
        return n.getNodeValue();
    }

}
