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
 * $Log: XPathQueryCache.java,v $
 * Revision 1.2  2005/05/11 17:31:41  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 */
package de.xplib.nexd.engine;

import org.apache.commons.collections.map.LRUMap;
import org.sixdml.SixdmlResourceSet;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public final class XPathQueryCache {
    
    /**
     * The default size for cached queries.
     */
    public static final int DEFAULT_SIZE = 400;

    /**
     * Holds singleton instance
     * @label singleton
     */
    private static XPathQueryCache instance;
    
    /**
     * The internal used last recently used cache.
     */
    private final LRUMap cache;
    
    /**
     * The size of each object query cache
     */
    private final int subSize;

    /**
     * prevents instantiation
     * 
     * @param sizeIn The number of cachable queries.
     */
    private XPathQueryCache(final int sizeIn) {
        super();
        
        int sqrt = (int) Math.sqrt(sizeIn);
                
        this.cache = new LRUMap(sqrt);
        this.subSize = sqrt;
    }

    /**
     * Returns the singleton instance.
     * 
     * @return The singleton instance
     */
    public static XPathQueryCache getInstance() {
        return getInstance(DEFAULT_SIZE);
    }

    /**
     * Returns the singleton instance.
     * 
     * @param sizeIn The num,ber cachable xpath queries.
     * @return The singleton instance
     */
    public static XPathQueryCache getInstance(final int sizeIn) {
        if (instance == null) {
            instance = new XPathQueryCache(sizeIn);
        }
        return instance;
    }
    
    /**
     * Returns a cached <code>SixdmlResourceSet</code> for the given params or
     * <code>null</code> if nothing is cached for this combination.
     *  
     * @param keyIn The full path of a collection or resource.
     * @param queryIn The used xpath query.
     * @return A cached resource set or <code>null</code>
     */
    public SixdmlResourceSet get(final String keyIn, final String queryIn) {
        
        SixdmlResourceSet set = null;
        
        LRUMap map = (LRUMap) this.cache.get(keyIn);
        if (map != null) {
            set = (SixdmlResourceSet) map.get(queryIn);
        }
        return set;
    }
    
    /**
     * Adds a <code>SixdmlResourceSet</code> to the cache.
     * 
     * @param keyIn The full path of a collection or resource.
     * @param queryIn The used xpath query.
     * @param setIn The resource set that will be cached.
     */
    public void add(final String keyIn, 
                    final String queryIn, 
                    final SixdmlResourceSet setIn) {
        
        LRUMap map = (LRUMap) this.cache.get(keyIn);
        if (map == null) {
            map = new LRUMap(this.subSize);
            this.cache.put(keyIn, map);
        }
        map.put(queryIn, setIn);
    }
    
    /**
     * Removes a cached resource set.
     * 
     * @param keyIn The full path of a collection or resource.
     */
    public void remove(final String keyIn) {
        this.cache.remove(keyIn);
    }
    
}
