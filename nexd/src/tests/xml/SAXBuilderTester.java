/*
 * Created on 01-Jun-2003 by eob
 */
package tests.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import de.xplib.nexd.xml.spartadom.BaseDocument;
import de.xplib.nexd.xml.spartadom.SAXBuilder;

/**
 * @version $Revision: 1.1 $
 * @author eob
 */
public class SAXBuilderTester extends TestCase {

    /**
     * @param arg0 ..
     */
    public SAXBuilderTester(final String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    //public void testBasicDefault() throws ParserConfigurationException,
    // SAXException, IOException{
    //    _testBasic( SAXParserFactory.newInstance().newSAXParser() );
    //}

    /**
     * @throws ParserConfigurationException ..
     * @throws SAXException ..
     * @throws IOException ..
     */
    public final void testBasicThermopylae() 
    throws ParserConfigurationException,
            SAXException, IOException {
        System.setProperty("javax.xml.parsers.SAXParserFactory",
                "de.xplib.nexd.xml.jaxp.SAXParserFactoryImpl");
        doTestBasic(SAXParserFactory.newInstance().newSAXParser());
    }

    /**
     * @throws ParserConfigurationException ..
     * @throws SAXException ..
     * @throws IOException ..
     */
    public final void testBasicCrimson() throws ParserConfigurationException,
            SAXException, IOException {
        //System.setProperty("javax.xml.parsers.SAXParserFactory",
        //        "org.apache.crimson.jaxp.SAXParserFactoryImpl");
        //doTestBasic(SAXParserFactory.newInstance().newSAXParser());
    }

    /**
     * @param parser ..
     * @throws ParserConfigurationException ..
     * @throws SAXException ..
     * @throws IOException ..
     */
    public final void doTestBasic(final SAXParser parser)
            throws ParserConfigurationException, SAXException, IOException {
        SAXBuilder builder = new SAXBuilder();
        String xmlS = "<A><B>text<C/>more text</B></A>";
        InputStream xml = new ByteArrayInputStream(xmlS.getBytes());
        parser.parse(xml, builder);
        BaseDocument doc = builder.getParsedDocument();
        assertEquals(xmlS, doc.getDocumentElement().toXml());
    }

}

// $Log: SAXBuilderTester.java,v $
// Revision 1.1 2003/06/19 20:30:05 eobrain
// Unit test.
//