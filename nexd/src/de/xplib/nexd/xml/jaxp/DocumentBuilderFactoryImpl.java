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
package de.xplib.nexd.xml.jaxp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Defines a factory API that enables applications to obtain a
 * parser that produces DOM object trees from XML documents.
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Rev$
 */
public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {

    /**
     * 
     */
    public DocumentBuilderFactoryImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new instance of a {@link javax.xml.parsers.DocumentBuilder}
     * using the currently configured parameters.
     *
     * @exception ParserConfigurationException if a DocumentBuilder
     * cannot be created which satisfies the configuration requested.
     * @return A new instance of a DocumentBuilder.
     * @see javax.xml.parsers.DocumentBuilderFactory#newDocumentBuilder()
     */
    public DocumentBuilder newDocumentBuilder()
            throws ParserConfigurationException {

        return new DocumentBuilderImpl();
    }

    /**
     * Allows the user to set specific attributes on the underlying
     * implementation.
     * 
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     * @exception IllegalArgumentException thrown if the underlying
     * implementation doesn't recognize the attribute.
     * @see javax.xml.parsers.DocumentBuilderFactory#setAttribute(
     * 		java.lang.String, java.lang.Object)
     */
    public void setAttribute(final String name, final Object value)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    /**
     * Allows the user to retrieve specific attributes on the underlying
     * implementation.
     * 
     * @param name The name of the attribute.
     * @return value The value of the attribute.
     * @exception IllegalArgumentException thrown if the underlying
     * implementation doesn't recognize the attribute.
     * @see javax.xml.parsers.DocumentBuilderFactory#getAttribute(
     *      java.lang.String)
     */
    public Object getAttribute(final String name) 
    		throws IllegalArgumentException {
        
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>Set a feature for this <code>DocumentBuilderFactory</code> and <code>
	 * DocumentBuilder</code>s created by this factory.</p>
	 * 
	 * <p>
	 * Feature names are fully qualified {@link java.net.URI}s.
	 * Implementations may define their own features.
	 * An {@link ParserConfigurationException} is thrown if this 
     * <code>DocumentBuilderFactory</code> or the <code>DocumentBuilder</code>s
	 * it creates cannot support the feature. It is possible for an 
	 * <code>DocumentBuilderFactory</code> to expose a feature value but be 
	 * unable to change its state.
	 * </p>
	 * 
	 * <p>
	 * All implementations are required to support the 
	 * {@link javax.xml.XMLConstants#FEATURE_SECURE_PROCESSING} feature.
	 * When the feature is:</p>
	 * <ul>
	 *   <li>
     *     <code>true</code>: the implementation will limit XML processing to 
     *     conform to implementation limits. Examples include enity expansion 
     *     limits and XML Schema constructs that would consume large amounts of 
     *     resources. If XML processing is limited for security reasons, it will
	 *     be reported via a call to the registered 
	 *     {@link org.xml.sax.ErrorHandler#fatalError(
	 *            SAXParseException exception)}.
     *     See {@link  DocumentBuilder#setErrorHandler(
     *                 org.xml.sax.ErrorHandler errorHandler)}.
	 *   </li>
	 *   <li>
     *     <code>false</code>: the implementation will processing XML according
     *     to the XML specifications without regard to possible implementation 
     *     limits.
	 *   </li>
	 * </ul>
	 * 
	 * @param name Feature name.
     * @param value Is feature state <code>true</code> or <code>false</code>.
	 *  
     * @throws ParserConfigurationException 
     *         if this <code>DocumentBuilderFactory</code> or the 
     *         <code>DocumentBuilder</code>s it creates cannot support this 
     *         feature.
     * @see javax.xml.parsers.DocumentBuilderFactory#setFeature(
     *      java.lang.String, boolean)
     */
    public void setFeature(final String name, final boolean value)
            throws ParserConfigurationException {
        // TODO Auto-generated method stub

    }

    /**
	 * <p>Get the state of the named feature.</p>
	 * 
	 * <p>
	 * Feature names are fully qualified {@link java.net.URI}s.
	 * Implementations may define their own features.
	 * An {@link ParserConfigurationException} is thrown if this 
	 * <code>DocumentBuilderFactory</code> or the
	 * <code>DocumentBuilder</code>s it creates cannot support the feature.
     * It is possible for an <code>DocumentBuilderFactory</code> to expose a 
	 * feature value but be unable to change its state.
	 * </p>
	 * 
	 * @param name Feature name.
	 * @return State of the named feature.
	 * @throws ParserConfigurationException 
	 *         if this <code>DocumentBuilderFactory</code> or the 
	 *         <code>DocumentBuilder</code>s it creates cannot support this 
	 *         feature.
     * @see javax.xml.parsers.DocumentBuilderFactory#getFeature(
     *      java.lang.String)
     */
    public boolean getFeature(final String name) 
    		throws ParserConfigurationException {
        // TODO Auto-generated method stub
        return false;
    }

}
