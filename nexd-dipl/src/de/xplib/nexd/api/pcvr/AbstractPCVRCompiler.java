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
 * $Log: AbstractPCVRCompiler.java,v $
 * Revision 1.2  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.1  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.5  2005/05/08 11:59:33  nexd
 * restructuring
 *
 * Revision 1.4  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.3  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 * Revision 1.1  2005/03/01 10:27:44  nexd
 * *** empty log message ***
 *
 */
package de.xplib.nexd.api.pcvr;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualResource;
import de.xplib.nexd.api.util.Base64;

/**
 * <p>
 * This class creates a <code>{@link de.xplib.nexd.api.VirtualResource}</code>
 * from a given <code>{@link de.xplib.nexd.api.pcvr.PCVResource}</code> instance
 * .</p>
 * <p>It uses the DOM <code>{@link org.w3c.dom.Document}</code> that is returned
 * by the <code>{@link de.xplib.nexd.api.pcvr.PCVResource#getContentAsDOM()}
 * </code> method. This class expectes that the content of all temporary
 * <code>@value</code> <code>{@link org.w3c.dom.Attr}</code> are encoded
 * base64 ({@link org.apache.commons.codec.binary.Base64#encode(byte[])}).</p>
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractPCVRCompiler implements PCVRCompiler {

    /**
     * <p>The temporary <code>{@link Document}</code> returned by <code>
     * {@link PCVResource#getContentAsDOM()}</code>.</p>
     */
    private Document doc;

    /**
     * <p>The <code>DocumentBuilder</code> that is used to create new <code>
     * {@link Node}</code> instances.</p>
     */
    private final DocumentBuilder builder;

    /**
     * Constructor.
     *
     * @throws XMLDBException If any database specific error occures.
     */
    public AbstractPCVRCompiler() throws XMLDBException {
        super();

        try {
            this.builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (FactoryConfigurationError e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * <p>This method creates a <code>{@link VirtualResource}</code> instance
     * from the given <code>{@link PCVResource}</code>.</p>
     * <p>All temporary contents of the <code>@value</code>
     * <code>{@link org.w3c.dom.Attr}</code> must be encoded with the base64
     * codec (<code>{@link Base64#encode(byte[])</code>).</p>
     *
     * @param pcvResIn <p>The <code>{@link PCVResource}</code> object that
     *                 should be compiled.</p>
     * @return <p>The resulting <code>{@link VirtualResource}</code>
     *         instance.</p>
     * @throws SAXException <p>If the content of an <code>@value</code> <code>
     *                      {@link org.w3c.dom.Attr}</code> doesn't contains
     *                      valid xml or text.</p>
     * @throws XMLDBException <p>If anything else goes wrong.</p>
     */
    public VirtualResource compile(final PCVResource pcvResIn)
            throws SAXException, XMLDBException {

        this.doc = (Document) pcvResIn.getContentAsDOM().cloneNode(true);

        String resName = this.doc.getDocumentElement().getAttribute(
                PCVResource.ATTR_SCHEMA_RESNAME);

        this.compileVariables();
        this.compileValueOfs();
        this.compileAttributes();
        this.compileCollections();
        this.compileResources();

        Element schema = doc.getDocumentElement();
        NodeList nodes = schema.getChildNodes();

        doc.removeChild(schema);

        for (int i = nodes.getLength() - 1; i >= 0; i--) {
            Node child = nodes.item(i);
            if (child instanceof Element) {
                schema.removeChild(child);

                doc.appendChild(child);
                break;
            }
        }
        return this.createVirtualResource(resName, doc);
    }

    /**
     * Compiles all &lt;pcvr:variable&gt; Elements in a <code>PCVResource</code>
     * object.
     */
    private void compileVariables() {
        NodeList vars = doc.getElementsByTagName(PCVResource.QNAME_VARIABLE);
        for (int i = vars.getLength() - 1; i >= 0; i--) {
            Node var = vars.item(i);
            var.getParentNode().removeChild(var);
        }
    }

    /**
     * Compiles all &lt;pcvr:value-of&gt; Elements in a <code>PCVResource</code>
     * object.
     *
     * @throws SAXException If the value of this Element contains raw xml data
     *                      and this is not really valid, this Exception may
     *                      should never occure.
     * @throws XMLDBException If any database specific error occures.
     */
    private void compileValueOfs() throws SAXException, XMLDBException {
        NodeList vals = doc.getElementsByTagName(PCVResource.QNAME_VALUE_OF);
        for (int i = vals.getLength() - 1; i >= 0; i--) {
            Element val = (Element) vals.item(i);
            String value = val.getAttribute(PCVResource.ATTR_VALUE_OF_VALUE);
            this.parseValue(value, val);
            val.getParentNode().removeChild(val);

        }
    }

    /**
     * This method removes the &lt;pcvr:attr&gt; <code>Element</code>s from the
     * <code>PCVResource</code> object and it decodes the value of the
     * associated attribute.
     */
    private void compileAttributes() {
        NodeList attrs = doc.getElementsByTagName(PCVResource.QNAME_ATTR);
        for (int i = attrs.getLength() - 1; i >= 0; i--) {
            Element elem = (Element) attrs.item(i);
            String name = elem.getAttribute(PCVResource.ATTR_ATTR_NAME);
            String value = elem.getAttribute(PCVResource.ATTR_ATTR_VALUE);

            Element parent = (Element) elem.getParentNode();
            parent.removeAttribute(PCVResource.NAMESPACE_PREFIX + ":" + name);
            parent.setAttribute(name, this.getBase64Value(value));

            parent.removeChild(elem);
        }
    }

    /**
     * Compiles a &lt;pcvr:collection&gt; <code>Element</code>, which means it
     * removes this <code>Element</code> from the result <code>Document</code>.
     */
    private void compileCollections() {
        NodeList colls = doc.getElementsByTagName(PCVResource.QNAME_COLLECTION);
        for (int i = colls.getLength() - 1; i >= 0; i--) {

            Node coll = colls.item(i);
            Node parent = coll.getParentNode();
            Node ref = coll;

            NodeList children = coll.getChildNodes();
            for (int j = children.getLength() - 1; j >= 0; j--) {
                Node child = children.item(j);
                coll.removeChild(child);

                parent.insertBefore(child, ref);

                ref = child;
            }

            parent.removeChild(coll);
        }
    }

    /**
     * Compiles a &lt;pcvr:resource&gt; Element, which means it removes this
     * <code>Element</code> from the internal <code>Document</code>.
     */
    private void compileResources() {
        NodeList ress = doc.getElementsByTagName(PCVResource.QNAME_RESOURCE);
        for (int i = ress.getLength() - 1; i >= 0; i--) {

            Node res = ress.item(i);
            Node parent = res.getParentNode();

            if (((Element) res).getAttribute(
                    PCVResource.ATTR_RESOURCE_REFERENCE).equals("")) {

                parent.removeChild(res);
                continue;
            }

            Node ref = res;

            NodeList children = res.getChildNodes();
            for (int j = children.getLength() - 1; j >= 0; j--) {
                Node child = children.item(j);

                res.removeChild(child);
                parent.insertBefore(child, ref);

                ref = child;
            }

            parent.removeChild(res);
        }
    }

    /**
     * Returns the decoded value for the given <code>encValueIn</code>
     *
     * @param encValueIn The base64 encoded value.
     * @return The decoded value.
     */
    private String getBase64Value(final String encValueIn) {
        return new String(Base64.decode(encValueIn));
    }

    /**
     * <p>Creates an instance of the underlying {@link VirtualResource}
     * implementation. This method implements the GoF Abstract Factory
     * pattern (87).</p>
     *
     * @param nameIn <p>The name or id of the newly created
     *               {@link VirtualResource} object.</p>
     * @param docIn <p>The content of the new {@link VirtualResource} object</p>
     * @return <p>A concrete implementation of {@link VirtualResource}.</p>
     * @throws XMLDBException <p>If anything else goes wrong.</p>
     */
    protected abstract VirtualResource createVirtualResource(String nameIn,
            Document docIn) throws XMLDBException;

    /**
     * <p>Parses the content of a <code>@value</code>
     * <code>{@link org.w3c.dom.Attr}</code> in the given parameter
     * <code>dataIn</code> and inserts it before the given <code>refIn</code>
     * <code>{@link Node}</code> in <code>{@link AbstractPCVRCompiler#doc}
     * </code>.</p>
     *
     * @param dataIn <p>The content of a <code>@value</code> <code>
     *               {@link org.w3c.dom.Attr}.</p>
     * @param refIn <p>The reference <code>{@link Node}</code>, where the parsed
     *              is inserted.</p>
     * @throws SAXException <p>If the content of an <code>@value</code> <code>
     *                      {@link org.w3c.dom.Attr}</code> doesn't contains
     *                      valid xml or text.</p>
     * @throws XMLDBException <p>If anything else goes wrong.</p>
     */
    protected void parseValue(final String dataIn, final Node refIn)
            throws SAXException, XMLDBException {

        String data = new String(Base64.decode(dataIn));

        if (data.indexOf('<') == -1) {
            refIn.getParentNode().insertBefore(this.doc.createTextNode(data),
                    refIn);
            return;
        }

        Node parent = refIn.getParentNode();

        data = "<_>" + data + "</_>";
        try {
            Document tmp = this.builder.parse(new ByteArrayInputStream(data
                    .getBytes()));

            NodeList children = tmp.getDocumentElement().getChildNodes();
            for (int i = 0, l = children.getLength(); i < l; i++) {
                Node node = this.doc.importNode(children.item(i), true);
                parent.insertBefore(node, refIn);
            }
        } catch (IOException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
}
