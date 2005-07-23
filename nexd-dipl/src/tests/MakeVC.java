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
 * $Log: MakeVC.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 */
package tests;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sixdml.SixdmlDatabase;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.CollectionManagementService;

import de.xplib.nexd.api.VirtualCollectionManagementService;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public final class MakeVC {
    
    /**
     * Comment for <code>LOG</code>
     */
    private static final Log LOG = LogFactory.getLog(MakeVC.class);

    /**
     * @param args ,....
     * @throws Exception ...
     */
    public static void main(final String[] args) throws Exception {
        
        //LogFactory.
        Logger.global.setLevel(Level.FINE);
        
        String name = "de.xplib.nexd.engine.xapi.DatabaseImpl";
        Class clazz = Class.forName(name);
        
        SixdmlDatabase db = (SixdmlDatabase) clazz.newInstance();
        DatabaseManager.registerDatabase(db);
        
        CollectionManagementService cms = (CollectionManagementService) db.getCollection("nexd://localhost./db", "sa", "").getService("CollectionManagementService", "1.0");
        
        Collection c = db.getCollection("nexd://localhost./db/vc-test", "sa", "");
        if (c == null) {
            c = cms.createCollection("vc-test");
        }
        
        try {
            VirtualCollectionManagementService vcms = 
                (VirtualCollectionManagementService) c.getService(
                    "VirtualCollectionManagementService", "1.0");
            /*
            if (db.getCollection("nexd://localhost./db/vcl-data/myvc", "sa", "") != null) {
                System.out.println("Drop Collection");
                cms.removeVirtualCollection("myvc");
            }
            */
            //cms.removeVirtualCollection("manuel");
            
            long total = 0;
            for (int i = 0; i < 1; i++) {
                
                String n1 = "category_" + i;
                
                long start = System.currentTimeMillis();
                /*
                cms.createVirtualCollection(
                        n1, 
                        new URL("file:///home/manuel/workspace/nexd/data/vcl-schema/category-tree-2.no.dtd.vcs"));
                */
                long end = System.currentTimeMillis();
                long time = (end - start);
                total += time;
                float needed = time / 1000;
                
                System.out.println("Seconds needed: " + needed + "  (" + time + ")");
                
                String n2 = "article_" + i;
                
                start = System.currentTimeMillis();
                
                vcms.createVirtualCollection(
                        n2, 
                        new URL("file:///home/manuel/workspace/nexd/data/vcl-schema/author-articles.no.dtd.vcs"));
                //new URL("file:///home/manuel/workspace/nexd/data/vcl-schema/category-tree-2.no.dtd.vcs"));
                end = System.currentTimeMillis();
                time = (end - start);
                total += time;
                needed = time / 1000;
                
                System.out.println("Seconds needed: " + needed + "  (" + time + ")");
                
                System.out.println((i + 1) + ": Sleeping 10 seconds.");
                Thread.sleep(10000);
            }
            
            System.out.println("For 5 virtual collection I need: " + (total / 1000) + "s.");
        } finally {
            //cms.removeCollection("vc-test");
        }
    }
}
