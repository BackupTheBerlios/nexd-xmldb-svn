/* <blockquote><small> Copyright (C) 2002 Hewlett-Packard Company.
 * This file is part of Sparta, an XML Parser, DOM, and XPath library.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the <a href="doc-files/LGPL.txt">GNU
 * Lesser General Public License</a> as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option)
 * any later version.  This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. </small></blockquote>
 * 
 * @author Eamonn O'Brien-Strain
 */
package de.xplib.nexd.xml;

import java.util.Hashtable;

/**
 * This utility class allows you to configure some low-level behavior of the
 * Sparta code such as caching and String interning. If you do not set any of
 * the configurations here then sparta will use default implementations that
 * will work in smaller JVMs such as J2ME. However if you are running in a
 * bigger JVM then you will get better performance if you ocerride the defaults
 * as described in the method descriptions below. Note that these static methods
 * need to be called <b>before </b> any Sparta classes are loaded, therefore it
 * is best to call them in a static block at the very beginning of your main
 * application class.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
public final class Sparta {

    //////////////////////////////////////////////////////////////

    /**
     * What a CacheFactory generates. Used internally to cache collections of
     * objects.
     */
    public static interface Cache {
        /**
         * @param key ..
         * @return ..
         */
        Object get(Object key);

        /**
         * @param key ..
         * @param value ..
         * @return ..
         */
        Object put(Object key, Object value);

        /**
         * @return ..
         */
        int size();
    }

    /**
     * You should pass an object that implements this interface to
     * setCacheFactory.
     */
    public static interface CacheFactory {
        /**
         * @return ..
         */
        Cache create();
    }

    /**
     *  
     */
    private static class HashtableCache extends Hashtable implements Cache {
    }

    /** 
     * Pass an object that implements this interface to setInternment. 
     */
    public static interface Internment {
        /**
         * @param s ..
         * @return ..
         */
        String intern(String s);
    }

    /**
     * Comment for <code>cacheFactory</code>
     */
    private static CacheFactory cacheFactory = new CacheFactory() {
        public Cache create() {
            return new HashtableCache();
        }
    };

    /**
     * The default internment used internally that does not rely on
     * String.intern being supported by the JVM.
     */
    private static Internment internment = new Internment() {
        private final Hashtable strings = new Hashtable();

        public String intern(final String s) {
            String ss = (String) strings.get(s);
            if (ss == null) {
                strings.put(s, s);
                return s;
            }
            return ss;
        }
    };

    /**
     * Used internally by Sparta code to intern strings because the
     * String.intern method is not supported in older and smaller JVMs.
     * 
     * @param s ..
     * @return ..
     * @see String#intern
     */
    public static String intern(final String s) {
        return internment.intern(s);
    }

    /**
     * Used internally to create a cache.
     * @return .. 
     */
    public static Cache newCache() {
        return cacheFactory.create();
    }

    /**
     * Change the caching to something custom. The default CacheFactory simply
     * creates Hashtables which grow without bound. If you are running Sparta in
     * a long-lived application and you want to avoid memory leaks you should
     * use caches that automatically evict using, for example an LRU mechanism
     * or soft reference. For example if you have a class called LruMap that
     * sub-classes from hava.util.Map then you can tell Sparta to use that by as
     * follows
     * 
     * <PRE>
     * 
     * public class MyApplication { static private class LruCache extends LruMap
     * implements Sparta.Cache {} static{ Sparta.setCacheFactory(new
     * Sparta.CacheFactory(){ public Sparta.Cache create() { return new
     * SoftCache(); } }); } public static void main(String[] args) { ...
     * 
     * </PRE>
     * 
     * @param f ..
     */
    public static void setCacheFactory(final CacheFactory f) {
        cacheFactory = f;
    }

    /**
     * Change the String intern to something custom. For example if you are
     * running on a modern full J2EE or JDSE JVM you almost certainly want to
     * tell Sparta to use the standard String.inter method like this:
     * 
     * <PRE>
     * 
     * public class MyApplication { static{ Sparta.setInternment(new
     * Sparta.Internment(){ public String intern(String s) {return s.intern();}
     * }); } public static void main(String[] args) { ...
     * 
     * </PRE>
     * 
     * @param i ..
     */
    public static void setInternment(final Internment i) {
        internment = i;
    }
    
    
    /**
     * 
     */
    private Sparta() {
        
    }
}