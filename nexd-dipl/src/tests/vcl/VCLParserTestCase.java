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
 * $Log: VCLParserTestCase.java,v $
 * Revision 1.6  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.5  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.4  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.3  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package tests.vcl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import junit.framework.TestCase;
import de.xplib.nexd.api.vcl.AbstractVCLParser;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.VCLAttr;
import de.xplib.nexd.api.vcl.VCLCollectionReference;
import de.xplib.nexd.api.vcl.VCLParserI;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VCLSchemaVisitor;
import de.xplib.nexd.api.vcl.VCLValueOf;
import de.xplib.nexd.api.vcl.VCLVariable;
import de.xplib.nexd.engine.xapi.vcl.VCLParserImpl;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.6 $
 */
public class VCLParserTestCase extends TestCase {
    
    public static final String VCS = "data/vcl-schema/author-articles.no.dtd.vcs";

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        System.setProperty(AbstractVCLParser.VCL_PARSER_KEY, "de.xplib.nexd.engine.xapi.vcl.VCLParserImpl");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public final void testParseNoColl() throws Exception {
        
        File f = new File("data/vcl/invalid.no-root-collref.vcl");
        //System.out.println(f.getAbsolutePath() + " :: " + f.exists());
        
        VCLParserImpl p = new VCLParserImpl();
        try {
            p.parse(f);
            assertTrue(false);
        } catch (InvalidVCLSchemaException e) {
            assertTrue(true);
        }
    }
    
    public final void testParseSimple() throws Exception {
        File f = new File("data/vcl/simple2coll.vcl");
        
        VCLParserImpl p = new VCLParserImpl();
        VCLSchema s = p.parse(f);
        
        System.out.println(s.getCollectionReference().getMatch());
        
        
    }
    
    public void testParseStringFail() throws Exception {
        
        VCLParserI p = AbstractVCLParser.createVCLParser();
        
        try {
            String s = null;
            p.parse(s);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public void testParseStringSuccess() throws Exception {
        
        VCLParserI p = AbstractVCLParser.createVCLParser();
        VCLSchema vcl = p.parse(VCS);
        
        assertNotNull(vcl);
        assertTrue(vcl instanceof VCLSchema);
    }
    
    public void testParseURLFail() throws Exception {
        VCLParserI p = AbstractVCLParser.createVCLParser();
        
        try {
            URL url = null;
            p.parse(url);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public void testParseURLSuccess() throws Exception {
        VCLParserI p = AbstractVCLParser.createVCLParser();
        VCLSchema vcl = p.parse(new File(VCS).toURL());
        
        assertNotNull(vcl);
        assertTrue(vcl instanceof VCLSchema);
    }
    
    public void testParseFileFail() throws Exception {
        VCLParserI p = AbstractVCLParser.createVCLParser();
        
        try {
            File f = null;
            p.parse(f);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public void testParseFileSuccess() throws Exception {

        VCLParserI p = AbstractVCLParser.createVCLParser();
        VCLSchema vcl = p.parse(new File(VCS));
        
        assertNotNull(vcl);
        assertTrue(vcl instanceof VCLSchema);
    }
    
    public void testParseInputStreamFail() throws Exception {
        VCLParserI p = AbstractVCLParser.createVCLParser();
        
        try {
            InputStream is = null;
            p.parse(is);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    public void testParseInputStreamSuccess() throws Exception {

        VCLParserI p = AbstractVCLParser.createVCLParser();
        VCLSchema vcl = p.parse(new File(VCS).toURL().openStream());
        
        assertNotNull(vcl);
        assertTrue(vcl instanceof VCLSchema);
    }
}

class TestVisitor implements VCLSchemaVisitor {
    
    private HashMap data = new HashMap();
    
    public TestVisitor() {
        
    }
    
    public void visit(VCLCollectionReference collRefIn) {

    }
    public void visit(VCLSchema schemaIn) {

    }
    public void visit(VCLValueOf valueOfIn) {

    }
    public void visit(VCLVariable variableIn) {

    }    
    public void visit(VCLAttr attrIn) {

    }
}
