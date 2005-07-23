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

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.sixdml.SixdmlDatabase;
import org.sixdml.command.SixdmlStatement;
import org.sixdml.exceptions.NonWellFormedXMLException;
import org.sixdml.exceptions.SixdmlException;
import org.sixdml.parser.SixdmlLexer;
import org.sixdml.parser.SixdmlParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

import de.xplib.nexd.engine.xml.jaxp.DocumentBuilderFactoryImpl;

/**
 * This class is used to execute SiXDML statements over the database. 
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class StatementImpl implements SixdmlStatement {
    
    /**
     * The associated <code>SixdmlDatabase</code> instance.
     * @associates <{de.xplib.nexd.engine.xapi.DatabaseImpl}>
     * @clientCardinality 0..*
     * @clientRole database
     * @directed true
     * @supplierCardinality 1
     */
    protected SixdmlDatabase database;
    
    /**
     * Constructor.
     * 
     * @param dbIn The associated <code>SixdmlDatabase</code> instance.
     */
    public StatementImpl(final SixdmlDatabase dbIn) {
        super();
        
        this.database = dbIn;
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
    public Document execute(final String statement) 
            throws XMLDBException, SixdmlException {
        
        StringBuffer buf = this.internalExecute(statement);
        try {
            DocumentBuilder builder = DocumentBuilderFactoryImpl
                                      .newInstance().newDocumentBuilder();
            
            return builder.parse(
                    new InputSource(new StringReader(buf.toString())));
        } catch (ParserConfigurationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (FactoryConfigurationError e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (SAXException e) {
            throw new NonWellFormedXMLException(e.getMessage(), e);
        } catch (IOException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }

    /**
     * Executes a SiXDML statement against the database and feeds the results to
     * a SAX handler. This method is best used if the results of the statement 
     * is XML which may be large and the database supports streaming of the 
     * results. 
     * 
     * @param statementIn The statement to execute 
     * @param handlerIn A SAX handler which operates on the results of the 
     *                  statement. 
     * 
     * @exception SixdmlException if the statement is invalid.
     * @exception XMLDBException  if a database error occurs
     * @see org.sixdml.command.SixdmlStatement#execute(
     *      java.lang.String, org.xml.sax.helpers.DefaultHandler)
     */
    public void execute(final String statementIn, 
                        final DefaultHandler handlerIn) throws XMLDBException, 
                                                               SixdmlException {

        StringBuffer buf = this.internalExecute(statementIn);
        
        try {
            SAXParser parser = SAXParserFactoryImpl.newInstance()
                                                   .newSAXParser();
            
            parser.parse(new InputSource(new StringReader(
                    buf.toString())), handlerIn);
            
        } catch (ParserConfigurationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (SAXException e) {
            throw new NonWellFormedXMLException(e.getMessage(), e);
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        } catch (IOException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
        
    }

    /**
     * Executes a SiXDML statement against the database and writes the results 
     * to the specified OutputStream. This method is best used when the results 
     * of the statement are not XML. 
     * 
     * @param statementIn The statement to execute 
     * @param osIn The stream to write the results to
     * @exception SixdmlException If the statement is invalid.
     * @exception XMLDBException  If a database error occurs
     * @see org.sixdml.command.SixdmlStatement#execute(
     *      java.lang.String, java.io.OutputStream)
     */
    public void execute(final String statementIn, final OutputStream osIn)
            throws XMLDBException, SixdmlException {

    	StringBuffer buf = this.internalExecute(statementIn);
        try {
            osIn.write(buf.toString().getBytes());
        } catch (IOException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage()); 
        }
    }

    /**
     * Executes a SiXDML statement against the database and writes the results 
     * using the specified writer. This method is best used when the results of 
     * the statement are not XML. 
     * 
     * @param statementIn the statement to execute 
     * @param writerIn the writer to send results to
     * @exception SixdmlException if the statement is invalid.
     * @exception XMLDBException  if a database error occurs
     * @see org.sixdml.command.SixdmlStatement#execute(
     *      java.lang.String, java.io.Writer)
     */
    public void execute(final String statementIn, final Writer writerIn) 
            throws XMLDBException, SixdmlException {
        
        StringBuffer buf = this.internalExecute(statementIn);
        try {
            writerIn.write(buf.toString());
        } catch (IOException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.getMessage());
        }
    }
    
    /**
     * Executes the statement against the <code>SixdmlDatabase</code>.
     * 
     * @param statementIn The statement.
     * @return The result <code>StringBuffer</code>.
     * @throws SixdmlException If the statement can't be executed.
     */
    protected StringBuffer internalExecute(final String statementIn) 
            throws SixdmlException {
        
        String stmt = statementIn;
        if (!stmt.endsWith(" ;")) {
            if (stmt.endsWith(";")) {
                stmt = stmt.substring(0, stmt.length() - 1);
            }
            stmt += " ;";
        }
        
        StringBuffer strBuf = new StringBuffer(""); 		
    	SixdmlLexer lexer  = new SixdmlLexer(new StringReader(stmt)); 
    	SixdmlParser parser = new SixdmlParser(lexer);

    	try { 
    	    parser.sixdmlStatement(this.database, strBuf);
    	} catch (Exception e) {
    	    //e.printStackTrace();
    	    throw new SixdmlException(e.getMessage(), e); 
    	}
    	return strBuf;
    }

}
