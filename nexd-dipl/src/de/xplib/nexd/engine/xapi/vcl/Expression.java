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
 * $Log: Expression.java,v $
 * Revision 1.3  2005/05/30 19:17:08  nexd
 * UML documentation update....
 *
 * Revision 1.2  2005/05/11 17:31:40  nexd
 * Refactoring and extended test cases
 *
 * Revision 1.1  2005/05/08 11:59:32  nexd
 * restructuring
 *
 */
package de.xplib.nexd.engine.xapi.vcl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import de.xplib.nexd.api.vcl.UndeclaredVariableException;

/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public class Expression {
    
    /**
     * Regular expression that matches on variable references, which were placed
     * in a xpath expression.
     * 
     * <pre>
     *   //foo/bar[@foobar='$test_var']
     * </pre>
     */
    public static final String INLINE_PATTERN = 
        "\\$([a-zA-Z0-9_]+)[^a-z^A-Z^0-9°_]*";
    
    /**
     * Regular expression that matches on a standalone variable reference. This
     * means that the checked <code>{@link String}</code> only contains the 
     * variable name and nothing else. 
     */
    public static final String STANDALONE_PATTERN = "^\\$([a-zA-Z0-9_]+)$";
    
    /**
     * Pre compiled regular expression that matches on inline variable
     * references.
     * 
     * @see VCLSchemaImpl#INLINE_PATTERN 
     */
    private static final Pattern VAR_PATTERN = Pattern.compile(INLINE_PATTERN);
    
    /**
     * The used variable context.
     * @clientCardinality 0..*
     * @clientRole context
     * @directed true
     * @supplierCardinality 1
     */
    private final VariableContext context;
    
    /**
     * The string epression.
     */
    private final String expr;
    
    /**
     * Factory that returns a concrete expression instance.
     * 
     * @param contextIn The variable context.
     * @param exprIn The string expression.
     * @return A concrete expression instance
     */
    public static Expression createExpression(final VariableContext contextIn, 
                                              final String exprIn) {
        
        return new Expression(contextIn, exprIn);
    }

    /**
     * Constructor
     * 
     * @param contextIn The variable context.
     * @param exprIn The string expression. 
     */
    protected Expression(final VariableContext contextIn, final String exprIn) {
        super();
        
        this.context = contextIn;
        this.expr    = exprIn.trim();
    }
    
    /**
     * <p>Replaces the variable references in the given expression with the
     * current value.</p>
     * 
     * @return <p>The prepared expression.</p>
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is 
     *                                     thrown, if a variable is accessed 
     *                                     that was not declared before.</p>
     */
    public String prepare() 
            throws UndeclaredVariableException {
        
        Matcher matcher = VAR_PATTERN.matcher(this.expr);
        
        String expression = this.expr;
        
        // we have a match, prepare the input expression        
        if (matcher.find()) {
            
            StringBuilder builder = new StringBuilder(this.expr);
            
            // Is any variable undeclared?
            boolean undeclared = false;
            
            do {
                String key = matcher.group(1);
                
                if (!this.context.contains(key)) {
                    undeclared = true;
                    break;
                }
                String value = this.getVariableValue(key);
                
                builder.replace(matcher.start(), matcher.end(1), value);
            } while (matcher.find());
            
            if (undeclared) {
                throw new UndeclaredVariableException();
            }
            
            expression = builder.toString();
        }
        return expression;
    }
    
    /**
     * <p>Evaluates an expression, which can be an XPath query or a 
     * variable reference and returns the value.</p>
     * 
     * @return <p>The value for the expression.</p> 
     * @throws UndeclaredVariableException <p>This <code>Exception</code> is 
     *                                     thrown, if a variable is accessed 
     *                                     that was not declared before.</p>
     */
    public String evaluate() throws UndeclaredVariableException {
        
        String value = null;
        // select is a variable expression
        if (this.expr.matches("^\\$([a-zA-Z0-9_]*)$")) {
            
            String expression = this.expr.substring(1);
            if (!this.context.contains(expression)) {
                throw new UndeclaredVariableException();
            }
            value = this.context.get(expression);
        }
        
        return value;
    }
    
    /**
     * Is this really an expression or is it empty?
     * 
     * @return Is it a expression or empty?
     */
    public boolean isExpression() {
        return !this.expr.equals("");
    }
    
    /**
     * Returns the base64 decoded value from the variable context.
     * @param key The variable key or name.
     * @return The base64 decodes value.
     */
    private String getVariableValue(final String key) {
        return new String(Base64.decodeBase64(this.context.getBytes(key)));
    }

}
