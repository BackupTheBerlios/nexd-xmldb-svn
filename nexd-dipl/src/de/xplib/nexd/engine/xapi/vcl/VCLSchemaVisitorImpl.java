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
 * $Log: VCLSchemaVisitorImpl.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 * Revision 1.6  2005/04/24 15:00:27  nexd
 * Bugfixes and many performance and coding improvements.
 *
 * Revision 1.5  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.4  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.3  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 * Revision 1.1  2005/03/01 10:27:44  nexd
 * *** empty log message ***
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.codec.binary.Base64;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.sixdml.SixdmlResourceSet;
import org.sixdml.dbmanagement.SixdmlCollection;
import org.sixdml.dbmanagement.SixdmlResource;
import org.sixdml.exceptions.InvalidQueryException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.api.VirtualCollection;
import de.xplib.nexd.api.pcvr.AbstractPCVRFactory;
import de.xplib.nexd.api.pcvr.PCVResource;
import de.xplib.nexd.api.vcl.InvalidCollectionReferenceException;
import de.xplib.nexd.api.vcl.InvalidVCLSchemaException;
import de.xplib.nexd.api.vcl.UndeclaredVariableException;
import de.xplib.nexd.api.vcl.VCLAttr;
import de.xplib.nexd.api.vcl.VCLCollectionReference;
import de.xplib.nexd.api.vcl.VCLNodeIterator;
import de.xplib.nexd.api.vcl.VCLSchema;
import de.xplib.nexd.api.vcl.VCLSchemaCompileVisitor;
import de.xplib.nexd.api.vcl.VCLValueOf;
import de.xplib.nexd.api.vcl.VCLVariable;
import de.xplib.nexd.api.vcl.VariableExistsException;
import de.xplib.nexd.comm.NEXDEngineI;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public class VCLSchemaVisitorImpl implements VCLSchemaCompileVisitor {

    /**
     * @clientCardinality 1
     * @directed true
     * @label visits
     * @supplierCardinality 1
     */

    /*#private VCLSchemaImpl lnkVCLSchemaImpl*/

    /**
     * @clientCardinality 1
     * @directed true
     * @label visits
     * @supplierCardinality 1..*
     */

    /*#-private VCLCollectionReferenceImpl lnkVCLCollectionReferenceImpl*/

    /**
     * @clientRole 1
     * @label visits
     * @supplierRole 0..*
     */
    /*#VCLValueOfImpl lnkVCLValueOfImpl*/

    /**
     * @clientCardinality 1
     * @directed true
     * @label visits
     * @supplierCardinality 0..*
     */
    /*#VCLAttrImpl lnkVCLAttrImpl*/

    /**
     * @clientCardinality 1
     * @directed true
     * @label visits
     * @supplierCardinality 0..*
     */
    /*#VCLVariableImpl lnkVCLVariableImpl*/

    /**
     * @label uses
     */
    /*#de.xplib.nexd.engine.xapi.vcl.VCLDocumentCompileHelper Dependency_Link*/

    /**
     * Comment for <code>singleResId</code>
     */
    private String singleResId = null;

    /**
     * Comment for <code>virtualCollPath</code>
     */
    private String virtualCollPath = null;

    /**
     * Property that is used to identify the naming mode. If this property is
     * set to <code>true</code> the name of the referenced root resource is
     * used. If it is <code>false</code> the value of <code>@name</code> is
     * used.
     */
    private boolean namingMode = false;

    /**
     * <p>The base name for the create {@link PCVResource} objects.</p>
     */
    private String baseName = "";

    /**
     * <p>The postfix or file extension for the created {@link PCVResource}
     * instances.</p>
     */
    private String postfix = null;

    /**
     * Are the resulting resources indexed?
     */
    private boolean enumerate = false;

    /**
     * Current resource index.
     */
    private int currentIndex = 0;

    /**
     * The context virtual collection instance.
     */
    private final VirtualCollection vcoll;

    /**
     * <p>The current context {@link SixdmlCollection}.</p>
     */
    private SixdmlCollection currentColl;

    /**
     * <p>The current context {@link SixdmlResource}.</p>
     */
    private SixdmlResource currentRes;

    /**
     * <p>The id of the current context {@link SixdmlResource}.</p>
     */
    private String currentId;

    /**
     * The used variable context.
     * @clientCardinality 1
     * @clientRole context
     * @directed true
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    private final VariableContext context = new VariableContext();

    /**
     * <p>The {@link XMLSerializer} that is used to transform the
     * {@link org.w3c.dom.Node} instances returns by a XPath query into
     * a <code>java.lang.String</code>.</p>
     */
    private XMLSerializer serializer;

    /**
     * This array list holds all generated <code>{@link PCVResource}</code>
     * instances.
     */
    private final SixdmlResourceSet pcvrSet = new SixdmlResourceSet();

    //private final FastArrayList pcvResources = new FastArrayList();

    /**
     * The concrete <code>NEXDEngineImpl</code> instance.
     */
    private final NEXDEngineI engine;

    /**
     * Constructor.
     *
     * @param engineIn The concrete <code>NEXDEngineImpl</code> instance.
     * @param vcollIn The context virtual collection instance.
     */
    public VCLSchemaVisitorImpl(final NEXDEngineI engineIn,
            final VirtualCollection vcollIn) {
        super();

        this.engine = engineIn;
        this.vcoll = vcollIn;

        OutputFormat format = new OutputFormat();
        format.setIndent(1);
        format.setIndenting(true);
        format.setLineSeparator("\n");
        format.setOmitXMLDeclaration(true);

        this.serializer = new XMLSerializer(format);

        //this.pcvResources.setFast(true);
    }

    /**
     * <p>Constructor.</p>
     *
     * @param engineIn <p>The used <code>NEXDEngineImpl</code> instance.</code>
     * @param vcollIn The context virtual collection instance.
     * @param resIdIn <p>The resource id that identifies the single resource
     *                to compile.</code>
     * @param vcPathIn <p>The collection path for the virtual collection.</p>
     */
    public VCLSchemaVisitorImpl(final NEXDEngineI engineIn,
            final VirtualCollection vcollIn, final String resIdIn,
            final String vcPathIn) {
        this(engineIn, vcollIn);

        this.singleResId = resIdIn;
        this.virtualCollPath = vcPathIn;
    }

    /**
     * <p>Returns the creates pre compiled resources.</p>
     *
     * @return The new created {@link PCVResource} objects.
     * @see de.xplib.nexd.api.vcl.VCLSchemaCompileVisitor#getResources()
     */
    public ResourceSet getResources() {
        return this.pcvrSet;
    }

    /**
     * <p>This method visits an attribute in a Virtual
     * Collection Language Schema, the {@link VCLAttr}.</p>
     *
     * @param attrIn <p>The {@link VCLAttr} node to visit.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an
     *                               invalid syntax.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is
     *                                     thrown, if a variable is accessed
     *                                     that was not declared before.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLAttr)
     */
    public void visit(final VCLAttr attrIn) throws InvalidQueryException,
            UndeclaredVariableException, XMLDBException {

        Expression expr = Expression.createExpression(this.context, attrIn
                .getSelect());

        String value = expr.evaluate();
        if (value == null) {
            value = new String(Base64.encodeBase64(this.queryResource(
                    attrIn.getSelect()).getBytes()));
        }

        //String value = this.evaluateExpression(attrIn.getSelect());

        Attr attr = attrIn.getContentAsDOM();

        attr.setPrefix(PCVResource.NAMESPACE_PREFIX);

        Element owner = attr.getOwnerElement();

        VCLDocumentCompileHelper.declaredAttribute(owner, attrIn.getName(),
                value);
    }

    /**
     * <p>This method visits a collection <code>Element</code> in a Virtual
     * Collection Language Schema, the {@link VCLCollectionReference}.</p>
     *
     * @param collRefIn <p>The {@link VCLCollectionReference} node to visit.</p>
     * @throws InvalidCollectionReferenceException <p>This is thrown, if a
     *                       referenced {@link org.xmldb.api.base.Collection}
     *                       doesn't exist or it is an instance of
     *                       {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an
     *                               invalid syntax.</p>
     * @throws InvalidVCLSchemaException <p>This is thrown, if the xml Document
     *                                   doesn't match the required structure or
     *                                   it doesn't define all necessary
     *                                   attributes.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is
     *                                     thrown, if a variable is accessed
     *                                     that was not declared before.</p>
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLCollectionReference)
     */
    public void visit(final VCLCollectionReference collRefIn)
            throws InvalidCollectionReferenceException, InvalidQueryException,
            InvalidVCLSchemaException, UndeclaredVariableException,
            VariableExistsException, XMLDBException {

        String match = collRefIn.getMatch();

        SixdmlCollection coll = this.engine.queryCollection(match);
        if (coll == null) {
            throw new InvalidCollectionReferenceException(
                    InvalidCollectionReferenceException.COLLECTION_NOT_EXIST);
        }
        if (coll instanceof VirtualCollection) {
            throw new InvalidCollectionReferenceException(
                    InvalidCollectionReferenceException.COLLECTION_IS_VIRTUAL);
        }

        this.currentColl = coll;

        String select = collRefIn.getSelect();

        Expression expr = Expression.createExpression(this.context, select);

        SixdmlResourceSet resSet = this.queryResources(expr);

        Element elem = collRefIn.getContentAsDOM();
        elem.setPrefix(PCVResource.NAMESPACE_PREFIX);

        Element instance = null;

        instance = VCLDocumentCompileHelper.declareResource(elem);

        if (instance == null) {
            return;
        }

        for (int i = 0, l = (int) resSet.getSize(); i < l; i++) {
            this.currentRes = (SixdmlResource) resSet.getResource(i);

            this.currentId = this.currentRes.getId();

            if (i > 0) {
                Element nextInstance = (Element) instance.cloneNode(true);
                instance.getParentNode().insertBefore(nextInstance,
                        instance.getPreviousSibling());
            }

            instance.setAttribute(PCVResource.ATTR_RESOURCE_REFERENCE, match
                    + "/" + this.currentId);

            this.context.push();

            this.performIterations(collRefIn);

            // reset current collection reference
            this.currentColl = coll;

            // reset variables to previouse level
            this.context.popReset();
        }
    }

    /**
     * <p>This method visits the document element of a Virtual Collection
     * Language Schema, the {@link VCLSchema}.</p>
     *
     * @param schemaIn <p>The {@link VCLSchema} node to visit.</p>
     * @throws InvalidCollectionReferenceException <p>This is thrown, if a
     *                       referenced {@link org.xmldb.api.base.Collection}
     *                       doesn't exist or it is an instance of
     *                       {@link _de.xplib.nexd.api.VirtualCollection}.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an
     *                               invalid syntax.</p>
     * @throws InvalidVCLSchemaException <p>This is thrown, if the xml Document
     *                                   doesn't match the required structure or
     *                                   it doesn't define all necessary
     *                                   attributes.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is
     *                                     thrown, if a variable is accessed
     *                                     that was not declared before.</p>
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLSchema)
     */
    public void visit(final VCLSchema schemaIn)
            throws InvalidCollectionReferenceException, InvalidQueryException,
            InvalidVCLSchemaException, UndeclaredVariableException,
            VariableExistsException, XMLDBException {

        VCLCollectionReference collRef = schemaIn.getCollectionReference();

        this.initNamingProperties(schemaIn);

        VCLDocumentCompileHelper.declareSchema((Document) schemaIn
                .getContentAsDOM());

        // {{{ start collection

        String match = collRef.getMatch();

        SixdmlCollection coll = this.engine.queryCollection(match);
        if (coll == null) {
            throw new InvalidCollectionReferenceException(
                    InvalidCollectionReferenceException.COLLECTION_NOT_EXIST);
        }
        if (coll instanceof VirtualCollection) {
            throw new InvalidCollectionReferenceException(
                    InvalidCollectionReferenceException.COLLECTION_IS_VIRTUAL);
        }

        this.currentColl = coll;

        Expression expr = Expression.createExpression(this.context, collRef
                .getSelect());

        SixdmlResourceSet resSet = this.queryResources(coll, expr,
                this.singleResId);

        Element elem = collRef.getContentAsDOM();
        elem.setPrefix(PCVResource.NAMESPACE_PREFIX);

        Element instance = VCLDocumentCompileHelper.declareRootResource(elem);

        if (instance == null) {
            return;
        }

        AbstractPCVRFactory factory = AbstractPCVRFactory.newInstance();

        for (int i = 0, l = (int) resSet.getSize(); i < l; i++) {
            this.currentRes = (SixdmlResource) resSet.getResource(i);
            String rname = this.createName(this.currentRes);

            this.currentId = this.currentRes.getId();

            instance.setAttribute(PCVResource.ATTR_RESOURCE_REFERENCE, match
                    + "/" + this.currentId);

            this.context.pushEmpty();

            this.performIterations(collRef);

            // reset current collection reference
            this.currentColl = coll;

            //this.varStack.pop();
            this.context.pop();

            this.pcvrSet.addResource(factory.newPCVResource(this.vcoll, rname,
                    (Document) schemaIn.getContentAsDOM()));
        }
    }

    /**
     * <p>This method visits a value-of <code>Element</code> in a Virtual
     * Collection Language Schema, the {@link VCLValueOf}.</p>
     *
     * @param valueOfIn <p>The {@link VCLValueOf} node to visit.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an
     *                               invalid syntax.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is
     *                                     thrown, if a variable is accessed
     *                                     that was not declared before.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLValueOf)
     */
    public void visit(final VCLValueOf valueOfIn) throws InvalidQueryException,
            UndeclaredVariableException, XMLDBException {

        Element valElem = valueOfIn.getContentAsDOM();
        valElem.setPrefix(PCVResource.NAMESPACE_PREFIX);

        String select = valueOfIn.getSelect().trim();

        Expression expr = Expression.createExpression(this.context, select);
        String value = expr.evaluate();
        if (value == null) {
            value = new String(Base64.encodeBase64(this.queryResource(select)
                    .getBytes()));
        }

        //String value  = this.evaluateExpression(select);

        valElem.setAttribute("value", value);
    }

    /**
     * <p>This method visits a variable <code>Element</code> in a Virtual
     * Collection Language Schema, the {@link VCLVariable}.</p>
     *
     * @param variableIn <p>The {@link VCLVariable} node to visit.</p>
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an
     *                               invalid syntax.</p>
     * @throws VariableExistsException <p>This <code>Exception</code> is thrown,
     *                                 if a variable with the same name allready
     *                                 exists in the current context.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     * @see de.xplib.nexd.api.vcl.VCLSchemaVisitor#visit(
     *      de.xplib.nexd.api.vcl.VCLVariable)
     */
    public void visit(final VCLVariable variableIn)
            throws InvalidQueryException, VariableExistsException,
            XMLDBException {

        String name = variableIn.getName();
        if (this.context.contains(name)) {
            throw new VariableExistsException();
        }

        Element varElem = variableIn.getContentAsDOM();
        varElem.setPrefix(PCVResource.NAMESPACE_PREFIX);

        String data = new String(Base64.encodeBase64(this.queryResource(
                variableIn.getSelect()).getBytes()));

        this.context.put(name, data);
        varElem.setAttribute("value", data);
    }

    /**
     * Executes the iterations for one collection reference. So it calls the
     * accept method for attributes, variables and value-of's.
     *
     * @param collRefIn The collection reference.
     * @throws InvalidCollectionReferenceException If the schema references a
     *         not existing collection or a virtual collection.
     * @throws InvalidQueryException If the used xpath queries are not ok or
     *         they use a not supported syntax.
     * @throws InvalidVCLSchemaException If the schema doesn't provide the
     *         minimum requirements for a virtual collection language schema.
     * @throws UndeclaredVariableException If the schema uses not declared
     *         variables.
     * @throws VariableExistsException If the schema has more than one variable
     *         with the same name.
     * @throws XMLDBException If any database specific error occures.
     */
    private void performIterations(final VCLCollectionReference collRefIn)
            throws InvalidVCLSchemaException,
            InvalidCollectionReferenceException, InvalidQueryException,
            UndeclaredVariableException, VariableExistsException,
            XMLDBException {

        VCLNodeIterator varIt = collRefIn.getVariables();
        while (varIt.hasNext()) {
            varIt.next().accept(this);
        }

        VCLNodeIterator valIt = collRefIn.getValueOfs();
        while (valIt.hasNext()) {
            valIt.next().accept(this);
        }

        VCLNodeIterator attrIt = collRefIn.getAttributes();
        while (attrIt.hasNext()) {
            attrIt.next().accept(this);
        }

        VCLNodeIterator collIt = collRefIn.getCollectionReferences();
        while (collIt.hasNext()) {
            collIt.next().accept(this);
        }
    }

    /**
     * <p>This method creates the resource name for the given parameter
     * settings.</p>
     *
     * @param resIn <p>The current {@link Resource}.
     * @return <p>The new created name.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    private String createName(final SixdmlResource resIn) 
            throws XMLDBException {

        String name = this.baseName;
        if (this.namingMode) {
            name += resIn.getId();
        }
        String pfix = "";
        if (this.enumerate || this.postfix != null) {
            int index = name.lastIndexOf('.');
            if (index > 0) {
                pfix = name.substring(index);
                name = name.substring(0, index);
            }
            if (this.enumerate) {
                name += "" + (this.currentIndex++);
            }
            if (this.postfix != null) {
                pfix = this.postfix;
            }
            name += pfix;
        }
        return name;
    }

    /**
     * Queries all Resources in a collection and returns all stored xml
     * resources.
     *
     * @param collIn The context collection
     * @return All xml resources.
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    private SixdmlResourceSet queryResources(final SixdmlCollection collIn)
            throws XMLDBException {

        SixdmlResourceSet resSet = new SixdmlResourceSet();

        String[] names = this.engine.queryResources(collIn);
        for (int i = 0; i < names.length; i++) {
            Resource res = this.engine.queryResource(collIn, names[i]);
            if (res instanceof SixdmlResource) {
                resSet.addResource(res);
            }
        }
        return resSet;
    }

    /**
     * Executes a XPath query against the underlying
     * {@link de.xplib.nexd.comm.NEXDEngineI} and returns the result as a
     * <code>java.lang.String</code>.
     *
     * @param queryIn The XPath query to execute.
     * @return The query result as a <code>java.lang.String</code>.
     * @throws InvalidQueryException <p>This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an
     *                               invalid syntax.</p>
     * @throws XMLDBException <p>If any database specific error occures.</p>
     */
    private String queryResource(final String queryIn)
            throws InvalidQueryException, XMLDBException {

        ResourceSet set = this.engine.queryResourceByXPath(this.currentColl,
                this.currentId, queryIn);

        StringWriter strw = new StringWriter();

        for (int i = 0, s = (int) set.getSize(); i < s; i++) {
            Document result = (Document) ((SixdmlResource) set.getResource(i))
                    .getContentAsDOM();

            Element elem = result.getDocumentElement();

            if (elem.getTagName().equals(NEXDEngineI.QNAME_QUERY_RESULT)) {
                if (elem.hasChildNodes()) {
                    NodeList children = elem.getChildNodes();
                    for (int j = 0, l = children.getLength(); j < l; j++) {
                        strw.write(children.item(j).getNodeValue());
                    }
                } else {
                    NamedNodeMap nnm = elem.getAttributes();
                    for (int j = 0, l = nnm.getLength(); j < l; j++) {
                        String nodeName = nnm.item(j).getNodeName();
                        if (!nodeName
                                .startsWith(NEXDEngineI.QUERY_RESULT_PREFIX
                                        + ":")
                                && !nodeName.startsWith("xmlns:")) {
                            strw.write(nnm.item(j).getNodeValue());
                        }
                    }
                }
            } else {

                this.serializer.setOutputCharStream(strw);
                try {
                    this.serializer.serialize(result.getDocumentElement());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        strw.flush();

        return strw.toString();
    }

    /**
     * <p>Executes a XPath query against the underlying
     * {@link de.xplib.nexd.comm.NEXDEngineI} and returns the result as a
     * <code>SixdmlresourceSet</code>.</p>
     *
     * @param select The expression to execute.
     * @return The result set.
     * @throws InvalidQueryException This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an
     *                               invalid syntax.
     * @throws XMLDBException If any database specific error occures.
     * @throws UndeclaredVariableException If the expression references a
     *                                     variable that wasn't defined.
     */
    private SixdmlResourceSet queryResources(final Expression select)
            throws InvalidQueryException, UndeclaredVariableException,
            XMLDBException {

        SixdmlResourceSet resSet = new SixdmlResourceSet();

        if (select.isExpression()) {

            ResourceSet set = this.engine.queryResourcesByXPath(
                    this.currentColl, select.prepare());
            for (int i = 0, l = (int) set.getSize(); i < l; i++) {
                resSet.addResource(this.engine.queryResource(this.currentColl,
                        ((SixdmlResource) set.getResource(i)).getDocumentId()));
            }
        } else {
            resSet = this.queryResources(this.currentColl);
        }
        return resSet;
    }

    /**
     * Executes an xpath expression against a single resource in a collection
     * and returns all matches as a <code>SixdmlResourceSet</code>.
     *
     * @param coll The collection to query.
     * @param selectIn The xpath expression.
     * @param resIDIn The resource id.
     * @return All matches found in a single resource.
     * @throws InvalidQueryException This <code>Exception</code> is thrown,if
     *                               a query is not supported or it uses an
     *                               invalid syntax.
     * @throws XMLDBException If any database specific error occures.
     * @throws UndeclaredVariableException If the expression references a
     *                                     variable that wasn't defined.
     */
    private SixdmlResourceSet queryResources(final SixdmlCollection coll,
            final Expression selectIn, final String resIDIn)
            throws InvalidQueryException, UndeclaredVariableException,
            XMLDBException {

        SixdmlResourceSet resSet;
        if (resIDIn == null) {
            resSet = this.queryResources(selectIn);
        } else {

            resSet = new SixdmlResourceSet();

            Resource res = this.engine.queryResource(coll, resIDIn);
            if (res instanceof SixdmlResource) {
                resSet.addResource(res);
            }

            if (this.enumerate) {
                this.initFreeEnumerateIndex();
            }
        }
        return resSet;
    }

    /**
     * Sets up the virtual resource name properties.
     *
     * @param schemaIn The current vcl-schema.
     */
    private void initNamingProperties(final VCLSchema schemaIn) {

        String name = schemaIn.getName();
        if (name == null) {
            this.namingMode = true;
            this.enumerate = schemaIn.isEnumerate();
        } else {
            this.baseName = name;
            this.enumerate = true;
        }

        String prefix = schemaIn.getPrefix();
        if (prefix != null) {
            this.baseName = prefix + "_" + this.baseName;
        }
        this.postfix = schemaIn.getPostfix();
        if (this.postfix != null) {
            this.postfix = "." + this.postfix;
        }
    }

    /**
     * Finds the next free number for enumerated documents.
     *
     * @throws XMLDBException If any database specific error occures.
     */
    private void initFreeEnumerateIndex() throws XMLDBException {

        SixdmlCollection pcvrColl = this.engine
                .queryCollection(this.virtualCollPath);

        StringBuilder builder = new StringBuilder();

        String[] names = this.engine.queryResources(pcvrColl);

        int enumNr = 0;
        for (int i = 0; i < names.length; i++) {
            int offset = names[i].length() - 1;
            if (!Character.isDigit(names[i].charAt(offset))) {
                offset = names[i].lastIndexOf('.') - 1;

                if (!Character.isDigit(names[i].charAt(offset))) {
                    offset = -1;
                }
            }
            if (offset == -1) {
                enumNr = -1;
                break;
            }

            // reset string builder
            builder.setLength(0);
            while (Character.isDigit(names[i].charAt(offset))) {
                builder.append(names[i].charAt(offset));
                --offset;
            }

            enumNr = Integer.parseInt(builder.toString());
            if (enumNr > this.currentIndex) {
                this.currentIndex = enumNr;
            }
            ++this.currentIndex;
        }

        if (enumNr == -1) {
            throw new XMLDBException(ErrorCodes.UNKNOWN_ERROR,
                    "Cannot determine resource number.");
        }
    }
}
