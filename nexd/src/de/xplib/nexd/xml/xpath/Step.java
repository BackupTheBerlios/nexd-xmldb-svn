/* <blockquote><small> Copyright (C) 2002 Hewlett-Packard Company.
 * This file is part of Sparta, an XML Parser, DOM, and XPath library.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the <a href="doc-files/LGPL.txt">GNU
 * Lesser General Public License</a> as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option)
 * any later version.  This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. </small></blockquote>
 * 
 * @author Eamonn O'Brien-Strain
 */
package de.xplib.nexd.xml.xpath;

import java.io.IOException;

/**
 * One of the steps which, separated by slashes, make up an XPath expression.
 * 
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 * @link http://sparta.sourceforge.net
 */
/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class Step {
    
    /**
     * Comment for <code>DOT</code>
     */
    public static final Step DOT = new Step(
            ThisNodeTest.INSTANCE, TrueExpr.INSTANCE);
    
    /**
     * Comment for <code>multiLevel</code>
     */
    private final boolean multiLevel;
    
    /**
     * @link aggregationByValue
     * @directed
     */
    private final NodeTest nodeTest;
    
    /**
     * @link aggregationByValue
     * @directed
     * @label predicate
     */
    private final BooleanExpr predicate;
    
    /**
     * 
     * @param nodeTestIn ---
     * @param predicateIn ---
     */
    Step(final NodeTest nodeTestIn, final BooleanExpr predicateIn) {
        nodeTest = nodeTestIn;
        predicate = predicateIn;
        multiLevel = false;
    }
    
    /**
     * @param xpath ---
     * @param multiLevelIn --
     * @param toks ---
     * @throws XPathException ---
     * @throws IOException ---
     * @precondition current token is 1st token in the node_test production.
     * @postcondition current tok is tok after last tok of step production
     */
    Step(final XPath xpath, 
         final boolean multiLevelIn, 
         final SimpleStreamTokenizer toks) throws XPathException, IOException {
        
        multiLevel = multiLevelIn;
        
        switch (toks.ttype) {
        case '.':
            if (toks.nextToken() == '.') {
                nodeTest = ParentNodeTest.INSTANCE;
            } else {
                toks.pushBack();
                nodeTest = ThisNodeTest.INSTANCE;
            }
            break;
        case '*':
            nodeTest = AllElementTest.INSTANCE;
            break;
        case '@':
            if (toks.nextToken() != SimpleStreamTokenizer.TT_WORD) {
                throw new XPathException(xpath, "after @ in node test", toks,
                "name");
            }
            nodeTest = new AttrTest(toks.sval);
            break;
        case SimpleStreamTokenizer.TT_WORD:
            if (toks.sval.equals("text")) {
                if (toks.nextToken() != '(' || toks.nextToken() != ')') {
                    throw new XPathException(xpath, "after text", toks, "()");
                }
                nodeTest = TextTest.INSTANCE;
            } else {
                nodeTest = new ElementTest(toks.sval);
            }
        break;
        default:
            throw new XPathException(xpath, "at begininning of step", toks,
            "'.' or '*' or name");
        }
        if (toks.nextToken() == '[') {
            toks.nextToken();
            //current token is first token in expr production
            predicate = ExprFactory.createExpr(xpath, toks);
            //current token is 1st token after expr production
            if (toks.ttype != ']') {
                throw new XPathException(xpath, "after predicate expression",
                        toks, "]");
            }
            toks.nextToken();
        } else {
            predicate = TrueExpr.INSTANCE;
        }
        //current token is token after step production
    }
    
    /**
     * @return ---
     */
    public final NodeTest getNodeTest() {
        return nodeTest;
    }
    
    /**
     * @return ---
     */
    public final BooleanExpr getPredicate() {
        return predicate;
    }
    
    /**
     * Is this step preceeded by a '//' ?
     * 
     * @return ---
     */
    public final boolean isMultiLevel() {
        return multiLevel;
    }
    
    /**
     * Does this step evaluate to a string values (attribute values or text()
     * nodes)
     * 
     * @return ---
     */
    public final boolean isStringValue() {
        return nodeTest.isStringValue();
    }
    
    /**
     * <Some description here>
     * 
     * @return ---
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return nodeTest.toString() + predicate.toString();
    }
}

// $Log: Step.java,v $
// Revision 1.3 2003/07/18 00:01:42 eobrain
// Make compatiblie with J2ME. For example do not use "new"
// java.util classes.
//
// Revision 1.2 2002/12/06 23:39:07 eobrain
// Make objects that are always the same follow the Flyweight Pattern.
//
// Revision 1.1.1.1 2002/08/19 05:04:04 eobrain
// import from HP Labs internal CVS
//
// Revision 1.6 2002/08/18 23:38:59 eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.5 2002/06/14 19:41:29 eob
// Add handling of "text()" in XPath expressions.
//
// Revision 1.4 2002/06/04 05:28:00 eob
// Simplify use of visitor pattern to make code easier to understand.
//
// Revision 1.3 2002/05/23 21:13:43 eob
// Better error reporting.
//
// Revision 1.2 2002/02/04 22:12:14 eob
// Add handling of nodetest for attribute.
//
// Revision 1.1 2002/02/01 02:04:23 eob
// initial
