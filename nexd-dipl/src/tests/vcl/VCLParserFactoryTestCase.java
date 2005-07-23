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
 * $Log: VCLParserFactoryTestCase.java,v $
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 */
package tests.vcl;

import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.vcl.AbstractVCLParser;
import junit.framework.TestCase;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.1 $
 */
public class VCLParserFactoryTestCase extends TestCase {

    
    public void testCreateVCLParserClassNotFoundFail() {
        String parser = System.getProperty(AbstractVCLParser.VCL_PARSER_KEY);
        System.setProperty(AbstractVCLParser.VCL_PARSER_KEY, "no.existing.Parser");
        
        try {
            AbstractVCLParser.createVCLParser();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
        System.setProperty(AbstractVCLParser.VCL_PARSER_KEY, parser);
    }
    
    public void testCreateVCLParserInstantiationFail() {
        String parser = System.getProperty(AbstractVCLParser.VCL_PARSER_KEY);
        System.setProperty(AbstractVCLParser.VCL_PARSER_KEY, "tests.vcl.NonImplementationParser");
        
        try {
            AbstractVCLParser.createVCLParser();
            assertTrue(false);
        } catch (XMLDBException e) {
            assertTrue(true);
        }
        System.setProperty(AbstractVCLParser.VCL_PARSER_KEY, parser);
    }
}
