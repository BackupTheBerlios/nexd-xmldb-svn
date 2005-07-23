package de.xplib.nexd.xml.xpath;


/**
 *  
 * @author Manuel Pichler <manuel.pichler@xplib.de>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractXPathFactory {
    
    /**
     * @clientCardinality 1
     * @directed true
     * @label creates instance
     * @link aggregation
     * @supplierCardinality 0..*
     * @shapeType PatternLink
     * @pattern gof.AbstractFactory
     */
    /*#private XPathI lnkXPathI;*/

    /**
     * Comment for <code>FACTORY_KEY</code>
     */
    public static final String FACTORY_KEY = 
        "de.xplib.nexd.xml.xpath.AbstractXPathFactory";
    
    /**
     * GoF singleton instance of this class.
     */
    private static AbstractXPathFactory instance = null;  
    
    /**
     * Singleton an factory method that returns a concrete implementation of 
     * <code>AbstractXPathFactory</code>.
     * 
     * @return A concrete implementation of this class.
     * @throws XPathException If anything goes wrong.
     */
    public static final AbstractXPathFactory getInstance() 
            throws XPathException {
        
        if (instance == null) {
            String className = 
                "de.xplib.nexd.engine.xml.xpath.XPathFactoryImpl";
        	if (System.getProperties().containsKey(FACTORY_KEY)) {
        	    className = System.getProperty(FACTORY_KEY);
        	}
        
        	try {
        	    Class clazz = Class.forName(className);
        	   	instance = (AbstractXPathFactory) clazz.newInstance();
        	} catch (Exception e) {
        	    System.err.println(e);
        	    System.exit(1);
        	    throw new XPathException();
        	}
        }
        return instance;
    }
    
    /**
     * Creates product
     * 
     * @param xpathIn The xpath expression.
     * @return An instance of <code>XPathI</code>.
     * @throws XPathException If the given xpath is not valid.
     */
    public abstract XPathI createXPath(String xpathIn) throws XPathException;

}