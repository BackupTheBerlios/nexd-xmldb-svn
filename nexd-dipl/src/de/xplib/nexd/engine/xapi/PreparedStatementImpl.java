/*
 * Project: nexd 
 * Copyright (C) 2004  Manuel Pichler <manuel.pichler@xplib.de>
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
package de.xplib.nexd.engine.xapi;

import java.io.OutputStream;
import java.io.Writer;

import org.sixdml.SixdmlDatabase;
import org.sixdml.command.SixdmlPreparedStatement;
import org.sixdml.exceptions.SixdmlException;
import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;
import org.xmldb.api.base.XMLDBException;

/**
 * This class is used to store and execute pre-compiled SiXDML statements over 
 * the database. It is assumed that implementers of this interface will have 
 * the query specified in their constructor.
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class PreparedStatementImpl
    extends StatementImpl
    implements SixdmlPreparedStatement {
    
    /**
     * The Statement.
     */
    private final String statement;
    
    /**
     * Array containing all values for the statement.
     */
    private final Object[] values;

    /**
     * @param statementIn The DDL statement to prepare.
     * @param dbIn The associated <code>SixdmlDatabase</code> instance.
     */
    public PreparedStatementImpl(final String statementIn,
                                 final SixdmlDatabase dbIn) {
        super(dbIn);
        
        this.statement = statementIn;

        // count all ?
        char[] chars = statementIn.toCharArray();
        int size = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '?') {
                ++size;
            }
        }
        this.values    = new Object[size];
    }

    /**
     * Sets the value of the designated parameter with the given object. 
     * Implementations are encouraged to create more specific versions of this 
     * method such as setInt, setNode, setString, and so on. 
     * <br /><br />
     * @param indexIn The index of the parameter whose value is being set. 
     *                   The value for the first parameter is 1, the second 2, 
     *                   etc.
     * @param valueIn The object containing the input parameter value
     * @exception SixdmlException If the statement is invalid.
     * @exception XMLDBException  If a database error occurs
     * @see org.sixdml.command.SixdmlPreparedStatement#setObject(
     *      int, java.lang.Object)
     */
    public void setObject(final int indexIn, final Object valueIn) 
            throws XMLDBException, SixdmlException {
        
        if (indexIn <= this.values.length) {
            this.values[indexIn - 1] = valueIn;
        }
    }

    /**
     * Executes a SiXDML statement against the database and returns the results 
     * as an XML DOM object. This method is best used if the results of the 
     * statement are not large. 
     *    
     * @return The results of the statement as a DOM object.
     * @exception SixdmlException If the statement is invalid.
     * @exception XMLDBException  If a database error occurs
     * @see org.sixdml.command.SixdmlPreparedStatement#execute()
     */
    public Document execute() throws XMLDBException, SixdmlException {
        return this.execute(this.replaceValues());
    }

    /**
     * Executes a SiXDML statement against the database and feeds the results to
     * a SAX handler. This method is best used if the results of the statement 
     * are large and the database supports streaming of the results. 
     *     
     * @param handlerIn A SAX handler which operates on the results of the 
     *                statement.
     * @exception SixdmlException If the statement is invalid.
     * @exception XMLDBException  If a database error occurs
     * @see org.sixdml.command.SixdmlPreparedStatement#execute(
     *      org.xml.sax.helpers.DefaultHandler)
     */
    public void execute(final DefaultHandler handlerIn) 
            throws XMLDBException, SixdmlException {
        
        this.execute(this.replaceValues(), handlerIn);
    }

    /**
     * Executes a SiXDML statement against the database and writes the results 
     * to the specified OutputStream. This method is best used when the results 
     * of the statement are not XML. 
     *     
     * @param osIn The stream to write the results to
     * @exception SixdmlException If the statement is invalid.
     * @exception XMLDBException  If a database error occurs
     * @see org.sixdml.command.SixdmlPreparedStatement#execute(
     *      java.io.OutputStream)
     */
    public void execute(final OutputStream osIn) 
            throws XMLDBException, SixdmlException {
        
        this.execute(this.replaceValues(), osIn);
    }

    /**
     * Executes a SiXDML statement against the database and writes the results 
     * using the specified writer. This method is best used when the results of 
     * the statement are not XML. 
     *     
     * @param writerIn The writer to send results to
     * @exception SixdmlException If the statement is invalid.
     * @exception XMLDBException  If a database error occurs
     * @see org.sixdml.command.SixdmlPreparedStatement#execute(java.io.Writer)
     */
    public void execute(final Writer writerIn) 
            throws XMLDBException, SixdmlException {
        
        this.execute(this.replaceValues(), writerIn);
    }
    
    
    /**
     * Replaces all ? in the statement with the given values
     * @return The prepared statement.
     */
    private String replaceValues() {
        String stmt = this.statement;
        int offset  = 0;
        while (stmt.indexOf('?') != -1 && offset < this.values.length) {
            
            String value = "";
            if (this.values[offset] != null) {
                value = this.values[offset].toString();
            }
            ++offset;
            
            stmt = stmt.replaceFirst("\\?", value);
        }
        return stmt;
    }

}
