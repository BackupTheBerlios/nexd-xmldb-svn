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
 * $Log: XPathSQLBuilder.java,v $
 * Revision 1.3  2005/05/11 18:00:12  nexd
 * Minor changes and corrections.
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:31  nexd
 * restructuring
 *
 * Revision 1.1  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.6  2005/03/31 12:08:40  nexd
 * Advanced VCL support
 *
 * Revision 1.5  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package de.xplib.nexd.engine.store;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xplib.nexd.engine.xml.xpath.AbstractBooleanExpr;
import de.xplib.nexd.engine.xml.xpath.AllElementTest;
import de.xplib.nexd.engine.xml.xpath.AndExpr;
import de.xplib.nexd.engine.xml.xpath.AttrEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrExistsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrGreaterEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrGreaterExpr;
import de.xplib.nexd.engine.xml.xpath.AttrLessEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrLessExpr;
import de.xplib.nexd.engine.xml.xpath.AttrNotEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.AttrTest;
import de.xplib.nexd.engine.xml.xpath.ElementTest;
import de.xplib.nexd.engine.xml.xpath.NotExpr;
import de.xplib.nexd.engine.xml.xpath.OrExpr;
import de.xplib.nexd.engine.xml.xpath.ParentNodeTest;
import de.xplib.nexd.engine.xml.xpath.PositionEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.Step;
import de.xplib.nexd.engine.xml.xpath.TextEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.TextExistsExpr;
import de.xplib.nexd.engine.xml.xpath.TextNotEqualsExpr;
import de.xplib.nexd.engine.xml.xpath.TextTest;
import de.xplib.nexd.engine.xml.xpath.ThisNodeTest;
import de.xplib.nexd.engine.xml.xpath.TrueExpr;
import de.xplib.nexd.engine.xml.xpath.Visitor;
import de.xplib.nexd.store.StorageException;
import de.xplib.nexd.xml.xpath.AbstractXPathFactory;
import de.xplib.nexd.xml.xpath.XPathException;
import de.xplib.nexd.xml.xpath.XPathI;

/**
 * This class translates a XPath expression into big/fat SQL query. The used 
 * XPath processor {@link de.xplib.nexd.engine.xml.xpath.XPath} doesn't support 
 * the full 1.0 specification, but this features will follow later. 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public class XPathSQLBuilder implements Visitor {
    
    /**
     * The used logger.
     */
    private static final Log LOG = LogFactory.getLog(XPathSQLBuilder.class);
    
    /**
     * The resource id used for the query.
     */
    private final int resId;
    
    /**
     * The collection id used for the query.
     */
    private final int collId;
    
    /**
     * Node index, used for unique table aliases.
     */
    private int nIdx = 0;
    
    /**
     * Is the current node test an absolute <b>/</b> or a multilevel <b>//</b>
     * test. 
     */
    private boolean multi = true;
    
    /**
     * Was the last build part of the query a node test?
     */
    private boolean wasTest = false;
    
    /**
     * Are we in an expression <b>[...]</b>?
     */
    private boolean inExpr = false;
    
    /**
     * The name of the last test part.
     */
    private String lastTest = null;
    
    /**
     * The name of the last expression part.
     */
    private String lastExpr = null;
    
    /**
     * Was the last test an <code>AllElementTest</code>?
     */
    private boolean wasAll = false;
    
    /**
     * The from part of the sql query.
     */
    private final StringBuilder from = new StringBuilder();
    
    /**
     * The where part of the sql query.
     */
    private final StringBuilder where = new StringBuilder();
    
    /**
     * The query itself.
     */
    private String query = null;
    
        
    /**
     * The used XPath processor instance.
     */
    private final XPathI xpath;

    /**
     * Constructor.
     * 
     * @param queryIn The xpath expression, used for the SQL query builder.
     * @param idIn The internal id used for the query.
     * @param isCollIn Is <code>true</code> if the is a query against a 
     *                 <code>StorageCollectionImpl</code> or <code>false</code> 
     *                 if it is a <code>StorageObjectI</code> query.
     * @exception StorageException If the given <code>queryIn</code> is not 
     *                             correct or used a not supported syntax.
     */
    public XPathSQLBuilder(final String queryIn, 
                           final int idIn, 
                           final boolean isCollIn) throws StorageException {
        super();
        
        if (isCollIn) {
            this.collId = idIn;
            this.resId  = -1;
        } else {
            this.collId = -1;
            this.resId  = idIn;
        }
        
        try {
            this.xpath = AbstractXPathFactory.getInstance().createXPath(
                    queryIn);
        } catch (XPathException e) {
            throw new StorageException(
                    StorageException.BAD_XPATH_EXPRESSION,
                    new String[] {e.getMessage()});
        }
    }
    
    /**
     * Evaluates the XPath expression and creates a SQL query for the used 
     * database schema. 
     * 
     * @return The created query.
     * @throws StorageException If the given XPath expression is incorrect or it
     *                          uses a not supported syntax.
     */
    public String buildSQLQuery() throws StorageException {
        
        if (this.query == null) {
            this.from.append("  nexd_resource AS res,\n");
            if (this.collId > -1) {
                this.where.append("  res.cid=" + collId + " AND\n");
            } else {
                this.where.append("  res.id=" + resId + " AND\n");
            }
            
            Iterator itr = this.xpath.getSteps();
            try {
                while (itr.hasNext()) {
                    
                    Step step = (Step) itr.next();
                    
                    this.multi = step.isMultiLevel();
                    
                    if (step.getNodeTest() != null) {
                        step.getNodeTest().accept(this);
                    }
                    step.getPredicate().accept(this);
                    
                    this.multi = false;
                }
            } catch (XPathException e) {
                throw new StorageException(
                        StorageException.BAD_XPATH_EXPRESSION,
                        new String[] {e.getMessage()});
            }
            
            String sFrom = this.from.toString().trim();
            if (sFrom.endsWith(",")) {
                sFrom = sFrom.substring(0, sFrom.length() - 1);
            }
            
            // close possible open epression bracket
            if (this.inExpr) {
                this.inExpr = false;
                this.where.append(")\n");
            }
            
            String sWhere = this.where.toString().trim();
            if (sWhere.endsWith("AND")) {
                sWhere = sWhere.substring(0, sWhere.length() - 3);
            }
            
            this.query = "SELECT " + this.lastTest + ".* FROM\n  " + sFrom 
            + "\nWHERE\n  " + sWhere + "\nGROUP BY\n  "
            + this.lastTest + ".id, "
            + this.lastTest + ".lft, "
            + this.lastTest + ".rgt, "
            + this.lastTest + ".type, "
            + this.lastTest + ".rid";
        }
        return this.query;
    }

    /**
     * Evaluates a <code>AllElementTest</code> <i>(*)</i> expression.
     * 
     * @param testIn The <code>AllElementTest</code> expression.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AllElementTest)
     */
    public void visit(final AllElementTest testIn) {
        
        this.wasAll = true;
        
        // set current state to element
        this.wasTest = true;
        
        if (this.inExpr) {
            this.inExpr = false;
            this.where.append(") AND\n");
        }
        
        // check if this is the first index, if true check for absolute and
        // create doc query part.
        if (this.nIdx == 0) {
            final String node = "n" + this.nIdx;
            final String doc  = "d" + this.nIdx;
            
            this.lastTest = node;
            
            this.appendResourceId(node);
            this.where.append("  ");
            this.where.append(node);
            this.where.append(".lft=1 AND ");
            this.where.append(doc);
            this.where.append(".nid=");
            this.where.append(node);
            this.where.append(".id AND\n");

            this.from.append("  nexd_dom_node AS " + node + ",\n" 
                           + "  nexd_dom_document AS " + doc + ",\n");
            
            ++this.nIdx;
        }
        
        final String node = "n" + this.nIdx;
        final String elem = "e" + this.nIdx;
        
        this.from.append("  nexd_dom_node AS " + node + ",\n"
                       + "  nexd_dom_element AS " + elem + ",\n");
        
        this.appendResourceId(node);
        
        this.where.append("  ");
        this.where.append(elem);
        this.where.append(".nid=");
        this.where.append(node);
        this.where.append(".id AND\n");
        
        // check that current node is child of any other node
        if (this.nIdx > 0) {
            
            this.where.append("  ");
            this.where.append(node);
            this.where.append(".lft>");
            this.where.append(this.lastTest);
            this.where.append(".lft AND ");
            this.where.append(node);
            this.where.append(".rgt<");
            this.where.append(this.lastTest);
            this.where.append(".rgt AND\n");
            
            // check if node is direct child or anywhere in the tree 
            if (!this.multi) {
                this.where.append(
                        "  (SELECT COUNT(id) FROM nexd_dom_node AS tn WHERE\n");
                this.appendResourceId("  tn");
                this.where.append("    tn.lft>");
                this.where.append(this.lastTest);
                this.where.append(".lft AND ");
                this.where.append("tn.lft<");
                this.where.append(node);
                this.where.append(".lft AND tn.rgt>");
                this.where.append(node);
                this.where.append(".rgt AND type=1)=0 AND\n");
            }
        }
        
        this.lastTest = node;
        
        ++this.nIdx;
    }

    /**
     * Evaluates a <code>ThisNodeTest</code> <i>(.)</i> expression.
     * 
     * @param testIn The <code>ThisNodeTest</code> expression
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.ThisNodeTest)
     */
    public void visit(final ThisNodeTest testIn) {
        //System.out.println(testIn.getClass());
        this.wasAll = false;
    }

    /**
     * Evaluates a <code>ParentNodeTest</code> <i>(..)</i> expression.
     * 
     * @param testIn The <code>ParentNodeTest</code> expression
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.ParentNodeTest)
     */
    public void visit(final ParentNodeTest testIn) throws XPathException {
        
        // set current state to element
        this.wasTest = true;
        
        String limit = "0 1";
        String last  = this.lastTest;
        
        if (this.inExpr) {
            this.inExpr = false;
            this.where.append(") AND\n");
            
            if (this.lastTest.equals("n1") && this.wasAll) {
                last  = this.lastExpr;
                limit = "1 1";
            }
        }
        
        String node = "n" + this.nIdx;
        
        this.from.append("  nexd_dom_node AS " + node + ",\n");
        
        this.appendResourceId(node);
        
        this.where.append(node);
        this.where.append(".type=1 AND\n");
        this.where.append("  (SELECT LIMIT ");
        this.where.append(limit);
        this.where.append(" tn.id FROM nexd_dom_node AS tn");
        this.where.append(" WHERE\n");
        this.appendResourceId("  tn");
        this.where.append("    tn.lft<");
        this.where.append(last);
        this.where.append(".lft AND tn.rgt>");
        this.where.append(last);
        this.where.append(".rgt AND tn.type=1 ORDER BY tn.lft DESC)=");
        this.where.append(node);
        this.where.append(".id AND\n");
        
        this.lastTest   = node;
        this.wasTest   = true;
        this.wasAll = false;
        
        ++this.nIdx;
    }

    /**
     * Evaluates an <code>ElementTest</code> <i>(/<b>foobar</b>/)</i> 
     * expression.
     * 
     * @param testIn The <code>ElementTest</code> expression.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.ElementTest)
     */
    public void visit(final ElementTest testIn) {
        
        if (this.inExpr) {
            this.inExpr = false;
            this.where.append(") AND\n");
        }
        
        // check if this is the first index, if true check for absolute and
        // create doc query part.
        if (this.nIdx == 0 && this.xpath.isAbsolute() && !this.multi) {
            final String node = "n" + this.nIdx;
            final String doc  = "d" + this.nIdx;
            
            this.lastTest = node;
            
            this.appendResourceId(node);
            this.where.append("  ");
            this.where.append(node);
            this.where.append(".lft=1 AND ");
            this.where.append(doc);
            this.where.append(".nid=");
            this.where.append(node);
            this.where.append(".id AND\n");

            this.from.append("  nexd_dom_node AS " + node + ",\n" 
                           + "  nexd_dom_document AS " + doc + ",\n");
            
            ++this.nIdx;
        }
        
        final String node = "n" + this.nIdx;
        final String elem = "e" + this.nIdx;
        
        this.from.append("  nexd_dom_node AS " + node + ",\n"
                       + "  nexd_dom_element AS " + elem + ",\n");
        
        this.appendResourceId(node);
        
        this.where.append("  ");
        this.where.append(elem);
        this.where.append(".nid=");
        this.where.append(node);
        this.where.append(".id AND ");
        this.where.append(elem);
        this.where.append(".name='");
        this.where.append(testIn.getTagName());
        this.where.append("' AND\n");
        
        // check that current node is child of any other node
        if (this.nIdx > 0) {
            
            this.where.append("  ");
            this.where.append(node);
            this.where.append(".lft>");
            this.where.append(this.lastTest);
            this.where.append(".lft AND ");
            this.where.append(node);
            this.where.append(".rgt<");
            this.where.append(this.lastTest);
            this.where.append(".rgt AND\n");
            
            // check if node is direct child or anywhere in the tree 
            if (!this.multi) {
                this.where.append(
                        "  (SELECT COUNT(id) FROM nexd_dom_node AS tn WHERE\n");
                this.appendResourceId("  tn");
                this.where.append("    tn.lft>");
                this.where.append(this.lastTest);
                this.where.append(".lft AND ");
                this.where.append("tn.lft<");
                this.where.append(node);
                this.where.append(".lft AND tn.rgt>");
                this.where.append(node);
                this.where.append(".rgt AND type=1)=0 AND\n");
            }
        }
        
        // set current state to element
        this.wasTest   = true;
        this.wasAll = false;
        this.lastTest   = node;
        
        ++this.nIdx;
    }

    /**
     * Evaluates an <code>AttrTest</code> (<i>@...</i>) text.
     * 
     * @param testIn The <code>AttrTest</code> text.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrTest)
     */
    public void visit(final AttrTest testIn) {
        
        if (this.inExpr) {
            this.inExpr = false;
            this.where.append(") AND\n");
        }
        
        boolean wasElem = true;
        
        // check if this is the first index, if true check for absolute and
        // create doc query part.
        if (this.nIdx == 0) {
            final String node = "n" + this.nIdx;
            final String doc  = "d" + this.nIdx;
            
            this.lastTest = node;
            
            this.appendResourceId(node);
            this.where.append("  ");
            this.where.append(node);
            this.where.append(".lft=1 AND ");
            this.where.append(doc);
            this.where.append(".nid=");
            this.where.append(node);
            this.where.append(".id AND\n");

            this.from.append("  nexd_dom_node AS " + node + ",\n" 
                           + "  nexd_dom_document AS " + doc + ",\n");
            
            ++this.nIdx;
            
            wasElem = false;
        }
        
        final String node = "n" + this.nIdx;
        final String attr = "a" + this.nIdx;
        
        this.from.append("  nexd_dom_node AS " + node + ",\n"
                       + "  nexd_dom_attr AS " + attr + ",\n");
        
        this.appendResourceId(node);
        
        this.where.append("  ");
        this.where.append(attr);
        this.where.append(".nid=");
        this.where.append(node);
        this.where.append(".id AND ");
        this.where.append(attr);
        this.where.append(".name='");
        this.where.append(testIn.getAttrName());
        this.where.append("' AND\n");
        
        if (wasElem) {
            this.where.append(
                    "  (SELECT COUNT(id) FROM nexd_dom_node tn WHERE\n");
        	this.appendResourceId("  tn");
        	this.where.append("    tn.lft>");
        	this.where.append(this.lastTest);
        	this.where.append(".lft AND tn.lft<");
        	this.where.append(node);
        	this.where.append(".lft AND tn.rgt>");
        	this.where.append(node);
        	this.where.append(".rgt AND tn.type=1)=0 AND\n");
        }
        
        this.wasTest   = true;
        this.wasAll = false;
        this.lastTest   = node;
        ++this.nIdx;
        
    }

    /**
     * Evaluates a <code>TextTest</code> (<i>text()</i>) expression.
     * 
     * @param testIn The <code>TextTest</code> expression.
     * @see de.xplib.nexd.engine.xml.xpath.NodeTestVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TextTest)
     */
    public void visit(final TextTest testIn) {
        //System.out.println(testIn.getClass());
        
        if (this.inExpr) {
            this.inExpr = false;
            this.where.append(") AND\n");
        }
        
        final String node = "n" + this.nIdx;
        final String text = "t" + this.nIdx;
        
        this.from.append("  nexd_dom_node AS " + node + ",\n"
                       + "  nexd_dom_text AS " + text + ",\n");

        this.appendResourceId(node);
        
        this.where.append("  ");
        this.where.append(text);
        this.where.append(".nid=");
        this.where.append(node);
        this.where.append(".id AND ");
        this.where.append(node);
        this.where.append(".type=3 AND\n");
        
        if (this.nIdx > 0) {
            
            this.where.append("  ");
            this.where.append(node);
            this.where.append(".lft>");
            this.where.append(this.lastTest);
            this.where.append(".lft AND ");
            this.where.append(node);
            this.where.append(".rgt<");
            this.where.append(this.lastTest);
            this.where.append(".rgt AND\n");
        }
        if (!this.wasAll && !this.multi) {
            this.where.append(
                    "  (SELECT COUNT(id) FROM nexd_dom_node tn WHERE\n");
            this.appendResourceId("  tn");
            this.where.append("    tn.lft>");
            this.where.append(this.lastTest);
            this.where.append(".lft AND tn.lft<");
            this.where.append(node);
            this.where.append(".lft AND tn.rgt>");
            this.where.append(node);
            this.where.append(".rgt AND tn.type=1)=0 AND\n");
        }
        this.wasTest   = true;
        this.wasAll = false;
        this.lastTest   = node;
        ++this.nIdx;
    }

    /**
     * Evaluates a <code>TrueExpr</code> (<i>true</i>) expression.
     * 
     * @param exprIn The <code>TrueExpr</code> expression.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TrueExpr)
     */
    public void visit(final TrueExpr exprIn) {
        
        if (!this.wasTest) {
            LOG.debug("Do we need the TrueExpr?");
        }
        this.wasTest = false;
    }

    /**
     * Evaluates an <code>AttrExistsExpr</code> expression.
     * 
     * @param exprIn The <code>AttrExistsExpr</code> expression
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrExistsExpr)
     */
    public void visit(final AttrExistsExpr exprIn) throws XPathException {
        this.buildAttrQuery(exprIn.getAttrName(), null, null);
    }

    /**
     * Evaluates an <code>AttrEqualsExpr</code> (<i>@foo='bar'</i>) expression.
     * 
     * @param exprIn The <code>AttrEqualsExpr</code> expression.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrEqualsExpr)
     */
    public void visit(final AttrEqualsExpr exprIn) throws XPathException {
        this.buildAttrQuery(exprIn.getAttrName(), exprIn.getAttrValue(), "=");
    }

    /**
     * Evaluates an <code>AttrNotEqualsExpr</code> (<i>@foo!='bar'</i>)
     * expression.
     * 
     * @param exprIn The <code>AttrNotEqualsExpr</code> expression.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrNotEqualsExpr)
     */
    public void visit(final AttrNotEqualsExpr exprIn) throws XPathException {
        this.buildAttrQuery(exprIn.getAttrName(), exprIn.getAttrValue(), "!=");
    }

    /**
     * Evaluates an <code>AttrLessExpr</code> (<i>@foo<0</i>) expression.
     * 
     * @param exprIn The <code>AttrLessExpr</code> expression.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrLessExpr)
     */
    public void visit(final AttrLessExpr exprIn) throws XPathException {
        this.buildAttrQuery(
                exprIn.getAttrName(), 
                String.valueOf(exprIn.getAttrValue()), "<");
    }
    
    /**
     * Visits a <code>AttrLessEqualsExpr</code> (<i>&gt;=</i>).
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the expression isn't supported.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrGreaterEqualsExpr)
     */
    public void visit(final AttrLessEqualsExpr exprIn) throws XPathException {
        this.buildAttrQuery(
                exprIn.getAttrName(), 
                String.valueOf(exprIn.getAttrValue()), "<=");
    }

    /**
     * Evaluates an <code>AttrGreaterExpr</code> (<i>@foo>0</i>) expression.
     * 
     * @param exprIn The <code>AttrGreaterExpr</code> expression.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrGreaterExpr)
     */
    public void visit(final AttrGreaterExpr exprIn) throws XPathException {
        this.buildAttrQuery(
                exprIn.getAttrName(), 
                String.valueOf(exprIn.getAttrValue()), ">");
    }
    
    /**
     * Visits a <code>AttrGreaterEqualsExpr</code> (<i>&gt;=</i>).
     * 
     * @param exprIn The expression instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AttrGreaterEqualsExpr)
     */
    public void visit(final AttrGreaterEqualsExpr exprIn) 
    		throws XPathException {
        
        this.buildAttrQuery(
                exprIn.getAttrName(), 
                String.valueOf(exprIn.getAttrValue()), ">=");
    }

    /**
     * Evaluates a <code>TextExistsExpr</code> (<i>text()</code>) expression.
     * 
     * @param exprIn The <code>TextExistsExpr</code> expression.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TextExistsExpr)
     */
    public void visit(final TextExistsExpr exprIn) throws XPathException {
        this.buildTextQuery(null, null);
    }

    /**
     * Evaluates a <code>TextEqualsExpr</code> (<i>text()='foo'</i>) expression.
     * 
     * @param exprIn The <code>TextEqualsExpr</code> expression.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TextEqualsExpr)
     */
    public void visit(final TextEqualsExpr exprIn) throws XPathException {
        this.buildTextQuery(exprIn.getValue(), "=");
    }

    /**
     * Evaluates a <code>TextNotEqualsExpr</code> (<i>text()!='bar'</i>)
     * expression.
     * 
     * @param exprIn The <code>TextNotEqualsExpr</code> expression.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.TextNotEqualsExpr)
     */
    public void visit(final TextNotEqualsExpr exprIn) throws XPathException {
        this.buildTextQuery(exprIn.getValue(), "!=");
    }

    /**
     * Evaluates a <code>PositionEqualsExpr</code> (<i>/foobar[1]</i>)
     * expression.
     * 
     * @param exprIn The <code>PositionEqualsExpr</code> expression.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.PositionEqualsExpr)
     */
    public void visit(final PositionEqualsExpr exprIn) throws XPathException {
        LOG.error("Not supported: " + exprIn.getClass());
    }
    
    /**
     * Visits an <code>AndExpr</code> expression and evaluates it.
     * 
     * @param exprIn The <code>AndExpr</code> instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.AndExpr)
     */
    public void visit(final AndExpr exprIn) throws XPathException {
        
        AbstractBooleanExpr expr = exprIn.getExpr();
        if (expr instanceof NotExpr) {
            ((NotExpr) expr).getExpr().accept(this);
            this.where.append(" AND NOT ");
        } else {
            expr.accept(this);
            this.where.append(" AND ");
        }
    }
    
    /**
     * Visits an <code>OrExpr</code> expression and evaluates it.
     * 
     * @param exprIn The <code>AndExpr</code> instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.OrExpr)
     */
    public void visit(final OrExpr exprIn) throws XPathException {
        AbstractBooleanExpr expr = exprIn.getExpr();
        if (expr instanceof NotExpr) {
            ((NotExpr) expr).getExpr().accept(this);
            this.where.append(" OR NOT ");
        } else {
            expr.accept(this);
            this.where.append(" OR ");
        }
    }
    
    /**
     * Visits an <code>NotExpr</code> expression and evaluates it.
     * 
     * @param exprIn The <code>NotExpr</code> instance.
     * @throws XPathException If the given <code>XPathI</code> is not valid.
     * @see de.xplib.nexd.engine.xml.xpath.BooleanExprVisitor#visit(
     *      de.xplib.nexd.engine.xml.xpath.NotExpr)
     */
    public void visit(final NotExpr exprIn) throws XPathException {
        
    }
    
    /**
     * This method appends the the resource id to the where expression.
     *  
     * @param nodeIn The unique node name.
     */
    protected void appendResourceId(final String nodeIn) {
        if (this.collId > -1) {
            this.where.append("  " + nodeIn + ".rid=res.id");
        } else {
            this.where.append("  " + nodeIn + ".rid=" + this.resId);
        }
        this.where.append(" AND\n");
    }
    
    /**
     * Helper method that generates a all attribute expression parts of the 
     * sql query.
     * 
     * @param nameIn The name of the attribute.
     * @param valueIn The reference value of the attribute or <code>null</code>.
     * @param opIn The operator or <code>null/code>
     */
    protected void buildAttrQuery(final String nameIn,
                                  final String valueIn, 
                                  final String opIn) {
        
        String node = "n" + this.nIdx;
        String attr = "a" + this.nIdx;
        
        this.from.append("  nexd_dom_node AS " + node + ",\n"
                       + "  nexd_dom_attr AS " + attr + ",\n");
        
        if (!this.inExpr) {
            this.where.append("(");
            this.inExpr = true;
        }
        
        this.where.append("  (");
        
        this.appendResourceId(node);
        
        this.where.append("  ");
        this.where.append(attr);
        this.where.append(".nid=");
        this.where.append(node);
        this.where.append(".id AND ");
        this.where.append(attr);
        this.where.append(".name='");
        this.where.append(nameIn);
        this.where.append("' AND ");
        
        if (valueIn == null) {
            this.where.append("\n");
        } else {
            this.where.append(attr);
            this.where.append(".value" + opIn + "'");
            this.where.append(valueIn);
            this.where.append("' AND\n");
        }
        
        this.where.append(node);
        this.where.append(".lft BETWEEN ");
        this.where.append(this.lastTest);
        this.where.append(".lft AND ");
        this.where.append(this.lastTest);
        this.where.append(".rgt AND\n");
        
        this.where.append("  (SELECT COUNT(id) FROM nexd_dom_node tn WHERE\n");
        this.appendResourceId("  tn");
        this.where.append("    tn.lft>");
        this.where.append(this.lastTest);
        this.where.append(".lft AND tn.lft<");
        this.where.append(node);
        this.where.append(".lft AND tn.rgt>");
        this.where.append(node);
        this.where.append(".rgt AND tn.type=1)=0)\n");
        
        this.lastExpr = node;

        ++this.nIdx;
    }
    
    
    /**
     * Helper method that generates a all text() expression parts of the 
     * sql query.
     * 
     * @param valueIn The reference value for the text() or <code>null</code>.
     * @param opIn The used operator or <code>null</code>
     */
    protected void buildTextQuery(final String valueIn, final String opIn) {

        String node = "n" + this.nIdx;
        String text = "t" + this.nIdx;

        this.from.append("  nexd_dom_node AS " + node + ",\n"
                       + "  nexd_dom_text AS " + text + ",\n");

        if (!this.inExpr) {
            this.where.append("(");
            this.inExpr = true;
        }

        this.where.append("  (");

        this.appendResourceId(node);

        this.where.append("  ");
        this.where.append(text);
        this.where.append(".nid=");
        this.where.append(node);
        this.where.append(".id AND ");
        
        if (valueIn != null) {
            this.where.append(text);
            this.where.append(".value" + opIn + "'");
            this.where.append(valueIn);
            this.where.append("' AND\n");
        } else {
            this.where.append("\n");
        }

        this.where.append(node);
        this.where.append(".lft BETWEEN ");
        this.where.append(this.lastTest);
        this.where.append(".lft AND ");
        this.where.append(this.lastTest);
        this.where.append(".rgt AND\n");

        this.where.append("  (SELECT COUNT(id) FROM nexd_dom_node tn WHERE\n");
        this.appendResourceId("  tn");
        this.where.append("    tn.lft>");
        this.where.append(this.lastTest);
        this.where.append(".lft AND tn.lft<");
        this.where.append(node);
        this.where.append(".lft AND tn.rgt>");
        this.where.append(node);
        this.where.append(".rgt AND tn.type=1)=0)\n");

        this.lastExpr = node;

        ++this.nIdx;
    }

}
