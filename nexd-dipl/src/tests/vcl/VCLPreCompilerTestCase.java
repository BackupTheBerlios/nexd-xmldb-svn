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
 * $Log: VCLPreCompilerTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.vcl;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.sixdml.exceptions.InvalidQueryException;
import org.xmldb.api.base.ResourceSet;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLPreCompiler;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VariableExistsException;
import de.xplib.nexd.comm.NEXDEngineI;
import de.xplib.nexd.engine.NEXDEnginePool;
import de.xplib.nexd.engine.xapi.vcl.VCLParserImpl;
import de.xplib.nexd.engine.xapi.vcl.VCLSchemaVisitorImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class VCLPreCompilerTestCase extends TestCase {
    
    public static final String VCS = "data/vcl-schema/author-articles.no.dtd.vcs";
    
    public static final String VAR_EXISTS_VCS = "data/fails/variable-exists.vcs";
    
    public static final String VAR_UNDECLARED_VCS = "data/fails/variable-undeclared.vcs";
    
    NEXDEngineI engine;

    protected void setUp() throws Exception {
        super.setUp();
        
        engine = NEXDEnginePool.getInstance().getEngine();
        engine.open("sa", "");
    }
    
    protected void tearDown() throws Exception {
        engine.close();
        
        super.tearDown();
    }
    
    public final void testCompileURLNullArgFail() throws Exception {
        
        
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            URL url = null;
            pc.compile(url);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public void testCompileURLSuccess() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        ResourceSet rs = pc.compile(new File(VCS).toURL());
        assertNotNull(rs);
        assertTrue(rs instanceof ResourceSet);
    }
    
    public void testCompileVCLSchemaNullArgFail() throws Exception {
        
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            VCLSchema vcs = null;
            pc.compile(vcs);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
    }

    
    public void testCompileVariableExistFail() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            pc.compile(new File(VAR_EXISTS_VCS).toURL());
            assertTrue(false);
        } catch (VariableExistsException e) {
            assertTrue(true);
        }
        
    }
    
    public void testCompileUndeclaredVariableFail() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            pc.compile(new File(VAR_UNDECLARED_VCS).toURL());
            assertTrue(false);
        } catch (UndeclaredVariableException e) {
            assertTrue(true);
        }
    }
    
    public void testCompileInvalidSchema1Fail() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            pc.compile(new File("data/fails/invalid-schema-1.vcs").toURL());
            assertTrue(false);
        } catch (InvalidVCLSchemaException e) {
            assertTrue(true);
        }
    }
    
    public void testCompileInvalidSchema2Fail() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            pc.compile(new File("data/fails/invalid-schema-2.vcs").toURL());
            assertTrue(false);
        } catch (InvalidVCLSchemaException e) {
            assertTrue(true);
        }
    }
    
    public void testCompileInvalidSchema3Fail() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            pc.compile(new File("data/fails/invalid-schema-3.vcs").toURL());
            assertTrue(false);
        } catch (InvalidVCLSchemaException e) {
            assertTrue(true);
        }
    }
    
    public void testCompileInvalidSchema4Fail() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            pc.compile(new File("data/fails/invalid-schema-4.vcs").toURL());
            assertTrue(false);
        } catch (InvalidVCLSchemaException e) {
            assertEquals(InvalidVCLSchemaException.ATTR_MATCH_REQUIRED, e.getCode());
        }
    }
    
    public void testCompileInvalidCollectionRefFail() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            pc.compile(new File("data/fails/invalid-reference.vcs").toURL());
            assertTrue(false);
        } catch (InvalidCollectionReferenceException e) {
            assertEquals(InvalidCollectionReferenceException.COLLECTION_NOT_EXIST, e.getCode());
            assertTrue(true);
        }
    }
    
    public void testCompileInvalidQueryFail() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
        
        try {
            pc.compile(new File("data/fails/invalid-query.vcs").toURL());
            assertTrue(false);
        } catch (InvalidQueryException e) {
            assertTrue(true);
        }
    }
    
    public void testCompileAllFail() throws Exception {
        VirtualCollection vc = (VirtualCollection) engine.queryCollection("/db/vcl-data/myvc");
        
        VCLPreCompiler pc = new VCLPreCompiler(new VCLParserImpl(), new VCLSchemaVisitorImpl(engine, vc));
            try {
                pc.compile(new File("data/fails/invalid-all.vcs").toURL());
                assertTrue(false);
            } catch (InvalidQueryException e) {
                assertTrue(true);
                return;
            } catch (InvalidCollectionReferenceException e) {
                assertTrue(true);
                return;
            } catch (InvalidVCLSchemaException e) {
                assertTrue(true);
                return;
            } catch (UndeclaredVariableException e) {
                assertTrue(true);
                return;
            } catch (VariableExistsException e) {
                assertTrue(true);
                return;
            }
            assertTrue(false);
        
    }
    
}
