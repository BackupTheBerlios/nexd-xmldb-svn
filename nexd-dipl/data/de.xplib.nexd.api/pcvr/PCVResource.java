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
 * $Log: PCVResource.java,v $
 * Revision 1.6  2005/04/22 14:59:41  nexd
 * SOAP support and performance update.
 *
 * Revision 1.5  2005/04/18 17:42:18  nexd
 * Minor API changes, switched from org.w3c.domDocument to org.w3c.dom.Node. 
 * Now the interfaces are compatible with the XMLResource interface.
 *
 * Revision 1.4  2005/04/13 19:06:32  nexd
 * Minor API changes and a documentation update.
 *
 * Revision 1.3  2005/03/31 12:08:39  nexd
 * Advanced VCL support
 *
 * Revision 1.2  2005/03/14 12:22:49  nexd
 * Heavy javadoc, checkstyle and eclipse todo-Task session.
 *
 */
package _de.xplib.nexd.api.pcvr;

import org.xmldb.api.modules.XMLResource;

/**
 * <p>This interface represents a pre compiled virtual resource.</p>
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.6 $
 */
public interface PCVResource extends XMLResource {

    /**
     * <p>Constant that defines the namespace uri for the
     * <code>{@link org.w3c.dom.Node}</code> instances that are used to define
     * the logic part of the precompiled <code>{@link Document}</code>.</p>
     */
    String NAMESPACE_URI = "http://nexd.xplib.de/pcvr/version/1.0";

    /**
     * <p>The used namespace prefix for logic <code>{@link org.w3c.dom.Node}
     * </code> instances.</p>
     */
    String NAMESPACE_PREFIX = "pcvr";

    /**
     * <p>The local name of the main <code>&lt;schema&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code>.</p>
     */
    String NAME_SCHEMA = "schema";

    /**
     * <p>The qualified name for the main <code>&lt;schema&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code> with the namespace prefix.</p>
     */
    String QNAME_SCHEMA = NAMESPACE_PREFIX + ":" + NAME_SCHEMA;

    /**
     * <p>The local name of an <code>&lt;attr&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code>.</p>
     */
    String NAME_ATTR = "attr";

    /**
     * <p>The qualified name for an <code>&lt;attr&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code> with the namespace prefix.</p>
     */
    String QNAME_ATTR = NAMESPACE_PREFIX + ":" + NAME_ATTR;

    /**
     * <p>The local name of a <code>&lt;collection&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code>.</p>
     */
    String NAME_COLLECTION = "collection";

    /**
     * <p>The qualified name for a <code>&lt;collection&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code> with the namespace prefix.</p>
     */
    String QNAME_COLLECTION = NAMESPACE_PREFIX + ":" + NAME_COLLECTION;

    /**
     * <p>The local name of a <code>&lt;variable&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code>.</p>
     */
    String NAME_VARIABLE = "variable";

    /**
     * <p>The qualified name for a <code>&lt;variable&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code> with the namespace prefix.</p>
     */
    String QNAME_VARIABLE = NAMESPACE_PREFIX + ":" + NAME_VARIABLE;

    /**
     * <p>The local name of a <code>&lt;value-of&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code>.</p>
     */
    String NAME_VALUE_OF = "value-of";

    /**
     * <p>The qualified name for a <code>&lt;value-of&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code> with the namespace prefix.</p>
     */
    String QNAME_VALUE_OF = NAMESPACE_PREFIX + ":" + NAME_VALUE_OF;

    /**
     * <p>The local name of a <code>&lt;resource&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code>.</p>
     */
    String NAME_RESOURCE = "resource";

    /**
     * <p>The qualified name for a <code>&lt;resource&gt;</code>
     * <code>{@link org.w3c.dom.Element}</code> with the namespace prefix.</p>
     */
    String QNAME_RESOURCE = NAMESPACE_PREFIX + ":" + NAME_RESOURCE;

    /**
     * <p>The <code>{@link org.w3c.dom.Attr}</code> name that holds the
     * autogenerated resource id.</p>
     */
    String ATTR_SCHEMA_RESNAME = "resourcename";

    /**
     * <p>The <code>{@link org.w3c.dom.Attr}</code> name that holds the
     * path of the <code>{@link org.xmldb.api.base.Collection}</code> to select.
     * </p>
     */
    String ATTR_COLLECTION_MATCH = "match";

    /**
     * <p>The <code>{@link org.w3c.dom.Attr}</code> name that holds the
     * <code>{@link org.xmldb.api.modules.XMLResource}</code> id that is
     * referenced.</p>
     */
    String ATTR_RESOURCE_REFERENCE = "ref";

    /**
     * <p>The <code>{@link org.w3c.dom.Attr}</code> name that holds the name
     * of an attribute that will be created.</p>
     */
    String ATTR_ATTR_NAME = "name";

    /**
     * <p>The <code>{@link org.w3c.dom.Attr}</code> name that holds the value
     * of an attribute that will be created.</p>
     */
    String ATTR_ATTR_VALUE = "value";

    /**
     * <p>The <code>{@link org.w3c.dom.Attr}</code> name that holds the
     * temporary value of a <code>&lt;value-of&gt;</code> <code>
     * {@link org.w3c.dom.Element}</code> instance.
     */
    String ATTR_VALUE_OF_VALUE = "value";

    /**
     * <p>Returns an array with all referenced <code>
     * {@link org.xmldb.api.base.Collection}</code> instances. A <code>
     * {@link org.xmldb.api.base.Collection} is identified by its database
     * path.</p>
     *
     * @return <p>The database paths of all referenced <code>
     *         {@link org.xmldb.api.base.Collection}</code> instances.</p>
     */
    String[] getReferenceCollections();

    /**
     * <p>Returns an array with all <code>
     * {@link org.xmldb.api.modules.XMLResource}</code> instances, that are used
     * by this <code>PCVResource</code>. They are identified by their full
     * qualified database path.</p>
     *
     * @return <p>The full qualified database paths of all linked <code>
     *         {@link org.xmldb.api.modules.XMLResource} instances.</p>
     */
    String[] getReferenceResources();

}