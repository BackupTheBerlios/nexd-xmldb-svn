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
package de.xplib.nexd.xapi;

import java.io.OutputStream;
import java.io.Writer;

import org.sixdml.command.SixdmlStatement;
import org.sixdml.exceptions.SixdmlException;
import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;
import org.xmldb.api.base.XMLDBException;

/**
 * This class is used to execute SiXDML statements over the database. 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class StatementImpl implements SixdmlStatement {

    /**
     * Constructor
     */
    public StatementImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Executes a SiXDML statement against the database and returns the results 
     * as an XML DOM object. This method is best used if the results of the 
     * statement is XML which is not large. 
     * 
     * @param statement the statement to execute 
     * @return the results of the statement as a DOM object. 
     * 
     * @exception SixdmlException if the statement is invalid.
     * @exception XMLDBException  if a database error occurs
     * @see org.sixdml.command.SixdmlStatement#execute(java.lang.String)
     */
    public Document execute(final String statement) throws XMLDBException,
            SixdmlException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Executes a SiXDML statement against the database and feeds the results to
     * a SAX handler. This method is best used if the results of the statement 
     * is XML which may be large and the database supports streaming of the 
     * results. 
     * 
     * @param statement The statement to execute 
     * @param handler A SAX handler which operates on the results of the 
     *                  statement. 
     * 
     * @exception SixdmlException if the statement is invalid.
     * @exception XMLDBException  if a database error occurs
     * @see org.sixdml.command.SixdmlStatement#execute(
     *      java.lang.String, org.xml.sax.helpers.DefaultHandler)
     */
    public void execute(final String statement, final DefaultHandler handler)
            throws XMLDBException, SixdmlException {
        // TODO Auto-generated method stub

    }

    /**
     * Executes a SiXDML statement against the database and writes the results 
     * to the specified OutputStream. This method is best used when the results 
     * of the statement are not XML. 
     * 
     * @param statement The statement to execute 
     * @param outputStream The stream to write the results to
     * @exception SixdmlException If the statement is invalid.
     * @exception XMLDBException  If a database error occurs
     * @see org.sixdml.command.SixdmlStatement#execute(
     *      java.lang.String, java.io.OutputStream)
     */
    public void execute(final String statement, final OutputStream outputStream)
            throws XMLDBException, SixdmlException {
        // TODO Auto-generated method stub

    }

    /**
     * Executes a SiXDML statement against the database and writes the results 
     * using the specified writer. This method is best used when the results of 
     * the statement are not XML. 
     * 
     * @param statement the statement to execute 
     * @param writer the writer to send results to
     * @exception SixdmlException if the statement is invalid.
     * @exception XMLDBException  if a database error occurs
     * @see org.sixdml.command.SixdmlStatement#execute(
     *      java.lang.String, java.io.Writer)
     */
    public void execute(final String statement, final Writer writer) 
            throws XMLDBException, SixdmlException {
        // TODO Auto-generated method stub

    }

}
