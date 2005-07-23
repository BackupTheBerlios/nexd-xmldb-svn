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
package de.xplib.nexd.engine.xml.jaxp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.FastHashMap;

/**
 * Defines a factory API that enables applications to obtain a
 * parser that produces DOM object trees from XML documents.
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @stereotype Factory
 * @version $Rev$
 */
public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
    
    /**
     * @clientCardinality 1
     * @directed true
     * @label creates instance
     * @link aggregation
     * @supplierCardinality 0..*
     */
    /*#DocumentBuilderImpl lnkDocumentBuilderImpl*/

    /**
     * Attributes for the underlying parser. This is not used at the moment.
     */
    private final FastHashMap attributes = new FastHashMap();
    
    /**
     * Features for the underlying parser. This is not used at the moment.
     */
    private final FastHashMap features = new FastHashMap();

    /**
     * 
     */
    public DocumentBuilderFactoryImpl() {
        super();
        
        this.attributes.setFast(true);
        this.features.setFast(true);
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
     * @param nameIn The name of the attribute.
     * @param valueIn The value of the attribute.
     * @exception IllegalArgumentException thrown if the underlying
     * implementation doesn't recognize the attribute.
     * @see javax.xml.parsers.DocumentBuilderFactory#setAttribute(
     * 		java.lang.String, java.lang.Object)
     */
    public void setAttribute(final String nameIn, final Object valueIn)
            throws IllegalArgumentException {

        this.attributes.put(nameIn, valueIn);
    }

    /**
     * Allows the user to retrieve specific attributes on the underlying
     * implementation.
     * 
     * @param nameIn The name of the attribute.
     * @return value The value of the attribute.
     * @exception IllegalArgumentException thrown if the underlying
     * implementation doesn't recognize the attribute.
     * @see javax.xml.parsers.DocumentBuilderFactory#getAttribute(
     *      java.lang.String)
     */
    public Object getAttribute(final String nameIn) 
    		throws IllegalArgumentException {
        
        return this.attributes.get(nameIn);
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
	 *            org.xml.sax.SAXParseException exception)}.
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
	 * @param nameIn Feature name.
     * @param valueIn Is feature state <code>true</code> or <code>false</code>.
	 *  
     * @throws ParserConfigurationException 
     *         if this <code>DocumentBuilderFactory</code> or the 
     *         <code>DocumentBuilder</code>s it creates cannot support this 
     *         feature.
     * @see javax.xml.parsers.DocumentBuilderFactory#setFeature(
     *      java.lang.String, boolean)
     */
    public void setFeature(final String nameIn, final boolean valueIn)
            throws ParserConfigurationException {

        this.features.put(nameIn, (valueIn ? Boolean.TRUE : Boolean.FALSE));
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
	 * @param nameIn Feature name.
	 * @return State of the named feature.
	 * @throws ParserConfigurationException 
	 *         if this <code>DocumentBuilderFactory</code> or the 
	 *         <code>DocumentBuilder</code>s it creates cannot support this 
	 *         feature.
     * @see javax.xml.parsers.DocumentBuilderFactory#getFeature(
     *      java.lang.String)
     */
    public boolean getFeature(final String nameIn) 
    		throws ParserConfigurationException {

        return ((Boolean) this.features.get(nameIn)).booleanValue();
    }

}
