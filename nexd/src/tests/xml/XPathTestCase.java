package tests.xml;

import junit.framework.TestCase;

/**
 * Test examples at <a href="http://www.w3.org/TR/xpath#path-abbrev">W3C </a>
 * 
 * <blockquote><small>Copyright (C) 2002 Hewlett-Packard Company. This file is
 * part of Sparta, an XML Parser, DOM, and XPath library. This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. </small> </blockquote>
 * 
 * @version $Date: 2002/10/30 23:18:43 $ $Revision: 1.9 $
 * @author Eamonn O'Brien-Strain
 */

public abstract class XPathTestCase extends TestCase {

    /**
     * @param name -.
     */
    protected XPathTestCase(final String name) {
        super(name);
    }

    /**
     * Comment for <code>PARA_1_1_1</code>
     */
    protected static final String PARA_1_1_1 = 
        "1st para of 1st sect of 1st cha";

    /**
     * Comment for <code>PARA_1_1_2</code>
     */
    protected static final String PARA_1_1_2 =
        "2nd para of 1st sect of 1st cha";

    /**
     * Comment for <code>PARA_1_2_1a</code>
     */
    protected static final String PARA_1_2_1A = 
        "1st part of para 1.2.1";

    /**
     * Comment for <code>PARA_1_2_1b</code>
     */
    protected static final String PARA_1_2_1B = 
        "2nd part of para 1.2.1";

    /**
     * Comment for <code>PARA_2_1_1</code>
     */
    protected static final String PARA_2_1_1 = 
        "1st para of 1st sect of 1st cha";

    /**
     * Comment for <code>PARA_2_1_2</code>
     */
    protected static final String PARA_2_1_2 = 
        "2nd para of 1st sect of 1st cha";

    /**
     * Comment for <code>ITEM_1_2_1</code>
     */
    protected static final String ITEM_1_2_1 = 
        "1st item of 2nd sect of 1st cha";

    /**
     * Comment for <code>ITEM_1_2_2</code>
     */
    protected static final String ITEM_1_2_2 = 
        "2nd item of 2nd sect of 1st cha";

    /**
     * Comment for <code>ITEM_1_3_1</code>
     */
    protected static final String ITEM_1_3_1 = 
        "1st item of 1st sect of 1st cha";

    /**
     * Comment for <code>ITEM_1_3_2</code>
     */
    protected static final String ITEM_1_3_2 = 
        "2nd item of 1st sect of 1st cha";

    /**
     * Comment for <code>XML</code>
     */
    protected static final String[] XML = {
            "<doc lang='en' count='1'>"
                    + "  <chapter name='Chapter 1' count='2'>"
                    + "    <section name='Section 1.1' count='3'>"
                    + "      <para type='error' count='4'>" + PARA_1_1_1
                    + "</para>" + "      <para type='warning' count='5'>"
                    + PARA_1_1_2 + "</para>" + "    </section>"
                    + "    <section name='Section 1.2' count='6'>"
                    + "      <para count='7'>" + PARA_1_2_1A
                    + "<br count='8'/>" + PARA_1_2_1B + "</para>"
                    + "      <olist id='foo' count='9'>"
                    + "        <item count='10'>" + ITEM_1_2_1 + "</item>"
                    + "        <item count='11'>" + ITEM_1_2_2 + "</item>"
                    + "      </olist>" + "    </section>"
                    + "    <section name='Section 1.3' count='12'>"
                    + "      <nlist count='13'>" + "        <item count='14'>"
                    + ITEM_1_3_1 + "</item>" + "        <item count='15'>"
                    + ITEM_1_3_2 + "</item>" + "      </nlist>"
                    + "    </section>" + "  </chapter>"
                    + " <!-- My Comment -->"
                    + "  <chapter name='Chapter 2' count='16'>"
                    + "    <section name='Section 2.1' count='16.5'>"
                    + "      <para count='17'>" + PARA_2_1_1 + "</para>"
                    + "      <para count='18'>" + PARA_2_1_2 + "</para>"
                    + "    </section>" + "  </chapter>"
                    + "  <chapter name='Chapter 3' count='19'/>"
                    + "  <chapter name='Chapter 4' count='20'/>"
                    + "  <chapter name='Chapter 5' count='21'>"
                    + "    <section name='Section 5.1' count='22'/>"
                    + "    <section name='Section 5.2' count='23'/>"
                    + "  </chapter>" + "</doc>",

            "<MetaData>" + "  <Child name=\"public\">"
                    + "    <AgileAccess host=\"localhost:8080\"/>"
                    + "  </Child>" + "</MetaData>" };

    /**
     *  
     * @author Manuel Pichler <manuel.pichler@xplib.de>
     * @version $Rev$
     */
    protected static class NoSuchElementException extends Exception {
    }
}

