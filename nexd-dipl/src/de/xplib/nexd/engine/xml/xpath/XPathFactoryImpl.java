package de.xplib.nexd.engine.xml.xpath;

import de.xplib.nexd.xml.xpath.AbstractXPathFactory;
import de.xplib.nexd.xml.xpath.XPathException;
import de.xplib.nexd.xml.xpath.XPathI;

/**
 *
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.3 $
 */
public final class XPathFactoryImpl extends AbstractXPathFactory {

    /**
     * Holds sole instance of the factory
     * @clientCardinality 1
     * @clientRole instance
     * @directed true
     * @link aggregation
     * @stereotype Singleton
     * @supplierCardinality 1
     */
    private static XPathFactoryImpl instance;

    /**
     * @clientCardinality 1
     * @directed true
     * @label creates instance
     * @link aggregation
     * @pattern gof.AbstractFactory
     * @shapeType PatternLink
     * @supplierCardinality 0..*
     * @supplierRole concrete product
     */
    /*#XPath _lnkAbstractXPathImpl*/

    /**
     * Creates concrete product XPathI
     *
     * @param xpathIn The xpath expression.
     * @return An instance of <code>XPathI</code>.
     * @throws XPathException If the given xpath is not valid.
     */
    public XPathI createXPath(final String xpathIn) throws XPathException {
        return XPath.get(xpathIn);
    }

}