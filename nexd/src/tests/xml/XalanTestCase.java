package tests.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

/**
 * Test examples at <a href="http://www.w3.org/TR/xpath#path-abbrev">W3C </a>
 * using Xalan XPath as a test of the tests used for sparta and fatpath Xpath.
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

public class XalanTestCase extends W3cXPathTestCase {

    /**
     * @param name ..
     * @throws SAXException ..
     * @throws IOException ..
     * @throws ParserConfigurationException ..
     * @throws NoSuchElementException ..
     */
    public XalanTestCase(final String name) throws SAXException, IOException,
            ParserConfigurationException, NoSuchElementException {
        super(name);
    }

    /**
     * @throws IOException ..
     * @throws NoSuchElementException ..
     * @throws TransformerException ..
     */
    public final void testPara() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne, "para");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException sfd
     * @throws NoSuchElementException asdf
     * @throws TransformerException sdf 
     */
    public final void testStar() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne, "*");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException asd
     * @throws NoSuchElementException asd 
     * @throws TransformerException asfd 
     */
    public final void testTextSingle() 
    throws IOException, NoSuchElementException,
            TransformerException {
        Element para111 = (Element) XPathAPI.selectSingleNode(sectionOne,
                "para[@type='error']");
        Text text111 = (Text) XPathAPI.selectSingleNode(para111, "text()");
        assertEquals(PARA_1_1_1, text111.getData());
    }

    /**
     * @throws IOException asdf 
     * @throws NoSuchElementException a as
     * @throws TransformerException asd 
     */
    public final void testTextSplit() 
    throws IOException, NoSuchElementException,
            TransformerException {
        Element para121 = (Element) XPathAPI.selectSingleNode(sectionTwo,
                "para");
        Text text121 = (Text) XPathAPI.selectSingleNode(para121, "text()");
        assertEquals(PARA_1_2_1A, text121.getData());

        NodeIterator nodes = XPathAPI.selectNodeIterator(para121, "text()");

        assertEquals(PARA_1_2_1A, ((Text) nodes.nextNode()).getData());
        assertEquals(PARA_1_2_1B, ((Text) nodes.nextNode()).getData());
        assertNull(nodes.nextNode());

    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException asf 
     * @throws TransformerException asdf 
     */
    public final void testAttr() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne, "@name");

        Attr attr = (Attr) nodes.nextNode();
        assertEquals("Section 1.1", attr.getValue());
    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException sadf 
     * @throws TransformerException sad f
     */
    public final void testParaPosn() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne,
                "para[1]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());
    }

    /**
     * @throws IOException sdf 
     * @throws NoSuchElementException sdf 
     * @throws TransformerException sdf sdf
     */
    public final void testStarPara() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(chapterOne, "*/para");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_2_1A, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException sdf
     * @throws NoSuchElementException sdf 
     * @throws TransformerException sdf 
     */
    public final void testChapter5Section2() throws IOException,
            NoSuchElementException, TransformerException {
        Element element = (Element) XPathAPI.selectSingleNode(document[0],
                "/doc/chapter[5]/section[2]");

        assertEquals("Section 5.2", element.getAttribute("name"));
    }

    /**
     * @throws IOException sedf 
     * @throws NoSuchElementException sdf 
     * @throws TransformerException sefd 
     */
    public final void testChapterPara() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0]
                .getDocumentElement(), "chapter//para");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_2_1A, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_2, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException asdf
     * @throws NoSuchElementException asfd
     * @throws TransformerException sfd 
     */
    public final void testAllPara() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI
                .selectNodeIterator(sectionOne, "//para");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_2_1A, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_2, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException asdf 
     * @throws TransformerException sd f
     */
    public final void testAllOlistPara() 
    		throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0],
                "//olist/item");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(ITEM_1_2_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(ITEM_1_2_2, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException safd
     * @throws NoSuchElementException sdf 
     * @throws TransformerException dsf 
     */
    public final void testDot() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne, ".");

        Element element = (Element) nodes.nextNode();
        assertTrue(element == sectionOne);

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException sdf 
     * @throws NoSuchElementException sdf 
     * @throws TransformerException sdf 
     */
    public final void testDescendentPara() throws IOException,
            NoSuchElementException, TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(chapterOne, ".//para");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_2_1A, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException asd 
     * @throws TransformerException ads 
     */
    public final void testDotDot() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne, "..");

        Element element = (Element) nodes.nextNode();
        assertTrue(element == chapterOne);

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException sadf
     * @throws NoSuchElementException sdf 
     * @throws TransformerException sdf 
     */
    public final void testDotDotSlashAttrLang() throws IOException,
            NoSuchElementException, TransformerException {
        Attr lang = (Attr) XPathAPI.selectSingleNode(chapterOne, "../@lang");

        assertNotNull(lang);
        assertEquals("en", lang.getValue());
    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException asd 
     * @throws TransformerException sdf 
     */
    public final void testParaTypeWarning() throws IOException,
            NoSuchElementException, TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne,
                "para[@type=\"warning\"]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException asf 
     * @throws NoSuchElementException asdf 
     * @throws TransformerException sadf 
     */
    public final void testParaTypeNotWarning() throws IOException,
            NoSuchElementException, TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne,
                "para[@type!=\"warning\"]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException asdf 
     * @throws NoSuchElementException sdf 
     * @throws TransformerException dsf 
     */
    public final void testParaType() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne,
                "para[@type]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException asd 
     * @throws TransformerException dsf 
     */
    public final void testAbs() throws IOException, NoSuchElementException,
            TransformerException {
        Element element = (Element) XPathAPI.selectSingleNode(document[0],
                "/doc/chapter/section[@name='Section 1.2']");

        assertEquals("Section 1.2", element.getAttribute("name"));

    }

    /**
     * @throws IOException asdf 
     * @throws NoSuchElementException asdf 
     * @throws TransformerException dsf 
     */
    public final void testDocRel() throws IOException, NoSuchElementException,
            TransformerException {
        Element element = (Element) XPathAPI.selectSingleNode(document[0],
                "doc/chapter/section[@name='Section 1.2']");

        assertEquals("Section 1.2", element.getAttribute("name"));

    }

    /**
     * @throws IOException asd
     * @throws NoSuchElementException asd
     * @throws TransformerException sadf
     */
    public final void testEquivalence() 
    throws IOException, NoSuchElementException,
            TransformerException {
        Element e1 = (Element) XPathAPI.selectSingleNode(document[0],
                "/doc/chapter/section[@name='Section 1.2']");
        Element e2 = (Element) XPathAPI.selectSingleNode(document[0],
                "doc/chapter/section[@name='Section 1.2']");
        Element e3 = (Element) XPathAPI.selectSingleNode(document[0]
                .getDocumentElement(), "chapter/section[@name='Section 1.2']");

        assertNotNull(e1);
        assertTrue(e1 == e2);
        assertTrue(e1 == e3);
    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException asd 
     * @throws TransformerException asdf 
     */
    public final void testFromDoc1() throws IOException, NoSuchElementException,
            TransformerException {
        Element element = (Element) XPathAPI.selectSingleNode(document[0],
                "doc/chapter");
        assertNotNull(element);
        assertEquals("Chapter 1", element.getAttribute("name"));

    }

    /**
     * @throws IOException awds
     * @throws NoSuchElementException asd 
     * @throws TransformerException asd 
     */
    public final void testFromDoc2() throws IOException, NoSuchElementException,
            TransformerException {
        Element element = (Element) XPathAPI.selectSingleNode(document[0],
                "doc/chapter/section[@name='Section 1.2']");

        assertEquals("Section 1.2", element.getAttribute("name"));

    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException sdf 
     * @throws TransformerException sdf 
     */
    public final void testMetadata() throws IOException, NoSuchElementException,
            TransformerException {
        Element a = (Element) XPathAPI.selectSingleNode(document[1],
                "/MetaData/Child[@name='public']");
        Element b = (Element) XPathAPI
                .selectSingleNode(a, "*[@expires-offset]");
        assertNull(b);

    }

    /**
     * @throws IOException sdf 
     * @throws NoSuchElementException sdf 
     * @throws TransformerException dsf 
     */
    public final void testMetadataRel() 
    	throws IOException, NoSuchElementException,
            TransformerException {
        Element a = (Element) XPathAPI.selectSingleNode(document[1],
                "MetaData/Child[@name='public']");
        Element b = (Element) XPathAPI
                .selectSingleNode(a, "*[@expires-offset]");
        assertNull(b);

    }

    /**
     * @throws IOException asdf 
     * @throws NoSuchElementException sdf 
     * @throws TransformerException sdf 
     */
    public final void testBadNoParent() 
    throws IOException, NoSuchElementException,
            TransformerException {
        Element element = (Element) XPathAPI.selectSingleNode(document[0], 
                "../*");
        assertNull(element);
    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException asdf 
     * @throws TransformerException sdf 
     */
    public final void testPredicateInMiddle() throws IOException,
            NoSuchElementException, TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0],
                "/doc/chapter/section[@name='Section 1.1']/para");

        Element para1 = (Element) nodes.nextNode();
        assertNotNull(para1);
        Text text1 = (Text) para1.getFirstChild();
        assertEquals(PARA_1_1_1, text1.getData());

        Element para2 = (Element) nodes.nextNode();
        assertNotNull(para2);
        Text text2 = (Text) para2.getFirstChild();
        assertEquals(PARA_1_1_2, text2.getData());

        assertNull(nodes.nextNode());

    }

    /**
     * @throws IOException sdf 
     * @throws NoSuchElementException sdf
     * @throws TransformerException dsf 
     */
    public final void testParaPosn2() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne,
                "para[2]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());
    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException asd 
     * @throws TransformerException sdf 
     */
    public final void testPosition1()
    	throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0],
                "/doc/chapter/section/para[1]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_2_1A, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_1, text.getData());

        para = (Element) nodes.nextNode();
        assertNull(para);

    }

    /**
     * @throws IOException sdf 
     * @throws NoSuchElementException sdf 
     * @throws TransformerException fdg 
     */
    public final void testPosition2() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0],
                "/doc/chapter/section/para[2]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_2, text.getData());

        para = (Element) nodes.nextNode();
        assertNull(para);

    }

    /**
     * @throws IOException asd
     * @throws NoSuchElementException asd
     * @throws TransformerException sdf 
     */
    public final void testSlashSlashParaPosn1() throws IOException,
            NoSuchElementException, TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0], 
                "//para[1]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_2_1A, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_1, text.getData());

        para = (Element) nodes.nextNode();
        assertNull(para);

    }

    /**
     * @throws IOException sdf sd
     * @throws NoSuchElementException  sdf
     * @throws TransformerException sdf
     */
    public final void testSlashSlashParaPosn2() throws IOException,
            NoSuchElementException, TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0], 
                "//para[2]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_2, text.getData());

        para = (Element) nodes.nextNode();
        assertNull(para);

    }

    /**
     * @throws IOException asdf 
     * @throws NoSuchElementException asdf
     * @throws TransformerException dsf 
     */
    public final void testDotPosn() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne,
                "self::node()[1]");

        Element element = (Element) nodes.nextNode();
        assertTrue(element == sectionOne);

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException dfg
     * @throws NoSuchElementException dfg
     * @throws TransformerException fd
     */
    public final void testDotDotPosn() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne,
                "parent::node()[1]");

        Element element = (Element) nodes.nextNode();
        assertTrue(element == chapterOne);

        assertNull(nodes.nextNode());
    }

    /**
     * @throws IOException asdf
     * @throws NoSuchElementException dsaf
     * @throws TransformerException sdf
     */
    public final void testStarPosn() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne, "*[1]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());
    }

    /**
     * @throws IOException as
     * @throws NoSuchElementException aws
     * @throws TransformerException asd
     */
    public final void testStarPosn2() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(sectionOne, "*[2]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());
    }

    /**
     * @throws IOException asd
     * @throws NoSuchElementException asd 
     * @throws TransformerException asd
     */
    public final void testStarPosition1() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0],
                "/doc/chapter/section/*[1]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_2_1A, text.getData());

        para = (Element) nodes.nextNode();
        assertEquals("nlist", para.getTagName());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_1, text.getData());

        para = (Element) nodes.nextNode();
        assertNull(para);

    }

    /**
     * @throws IOException asd 
     * @throws NoSuchElementException as 
     * @throws TransformerException asd 
     */
    public final void testStarPosition2() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0],
                "/doc/chapter/section/*[2]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        para = (Element) nodes.nextNode();
        assertEquals("olist", para.getTagName());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_2, text.getData());

        para = (Element) nodes.nextNode();
        assertNull(para);

    }

    /**
     * @throws IOException asd
     * @throws NoSuchElementException sdf
     * @throws TransformerException sdf
     */
    public final void testSlashSlashStarPosn1() throws IOException,
            NoSuchElementException, TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0], "//*[1]");

        Element element = (Element) nodes.nextNode();
        assertEquals("doc", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("chapter", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("section", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("para", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("para", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("br", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("item", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("nlist", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("item", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("section", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("para", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("section", element.getTagName());

        element = (Element) nodes.nextNode();
        assertNull(element);

    }

    /**
     * @throws IOException sdf 
     * @throws NoSuchElementException sdf 
     * @throws TransformerException sdf
     */
    public final void testSlashSlashStarPosn2() throws IOException,
            NoSuchElementException, TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0], "//*[2]");

        Element element = (Element) nodes.nextNode();
        assertEquals("para", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("section", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("olist", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("item", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("item", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("chapter", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("para", element.getTagName());

        element = (Element) nodes.nextNode();
        assertEquals("section", element.getTagName());

        element = (Element) nodes.nextNode();
        assertNull(element);

    }

    /**
     * @throws IOException sd
     * @throws NoSuchElementException sd
     * @throws TransformerException ds
     */
    public final void testText() throws IOException, NoSuchElementException,
            TransformerException {
        Text text111 = (Text) XPathAPI.selectSingleNode(sectionOne,
                "para[@type='error']/text()");
        assertEquals(PARA_1_1_1, text111.getData());
    }

    /**
     * @throws IOException d
     * @throws NoSuchElementException d
     * @throws TransformerException d
     */
    public final void testTextExists() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator i = XPathAPI.selectNodeIterator(sectionOne,
                "para[text()]");
        Element para = (Element) i.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) i.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        assertNull(i.nextNode());
    }

    /**
     * @throws IOException l
     * @throws NoSuchElementException ä
     * @throws TransformerException -. 
     */
    public final void testTextEquals() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator i = XPathAPI.selectNodeIterator(sectionOne,
                "para[text()='" + PARA_1_1_1 + "']");
        Element para = (Element) i.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        assertNull(i.nextNode());
    }

    /**
     * @throws IOException -
     * @throws NoSuchElementException -
     * @throws TransformerException - 
     */
    public final void testTextNotEquals() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator i = XPathAPI.selectNodeIterator(sectionOne,
                "para[text()!='" + PARA_1_1_1 + "']");
        Element para = (Element) i.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        assertNull(i.nextNode());
    }

    /**
     * @throws IOException .-
     * @throws NoSuchElementException .
     * @throws TransformerException ,,
     */
    public final void testLessThan() throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0],
                "/doc/*/section/para[@count<6]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_1_1_2, text.getData());

        para = (Element) nodes.nextNode();
        assertNull(para);

    }

    /**
     * @throws IOException ..
     * @throws NoSuchElementException ..
     * @throws TransformerException ..
     */
    public final void testGreaterThan() 
    throws IOException, NoSuchElementException,
            TransformerException {
        NodeIterator nodes = XPathAPI.selectNodeIterator(document[0],
                "/doc/*/section/para[@count>16]");

        Element para = (Element) nodes.nextNode();
        Text text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_1, text.getData());

        para = (Element) nodes.nextNode();
        text = (Text) para.getFirstChild();
        assertEquals(PARA_2_1_2, text.getData());

        para = (Element) nodes.nextNode();
        assertNull(para);

    }

}