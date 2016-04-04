package tp.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;



@XmlRegistry
public class ObjectFactory {

	private final static QName Position_QNAME = new QName("m2GIL:rest:tp", "Position");
	private final static QName City_QNAME = new QName("m2GIL:rest:tp", "City");
    private final static QName CityManager_QNAME = new QName("m2GIL:rest:tp", "CityManager");
    private final static QName CityList_QNAME = new QName("m2GIL:rest:tp", "CityList");
    private final static QName CityException_QNAME = new QName("m2GIL:rest:tp", "CityException");
    
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for this package
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link City }
     * 
     */
    public City createCity() {
        return new City();
    }

    public Position createPosition() {
        return new Position();
    }
    
    public CityManager createCityManager() {
        return new CityManager();
    }
    
    public CityList createCityList() {
        return new CityList();
    }
    
    public CityException createCityException() {
        return new CityException();
    }
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link City }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "m2GIL:rest:tp", name = "City")
    public JAXBElement<City> createCity(City value) {
        return new JAXBElement<City>(City_QNAME, City.class, null, value);
    }
    
    @XmlElementDecl(namespace = "m2GIL:rest:tp", name = "Position")
    public JAXBElement<Position> createPosition(Position value) {
        return new JAXBElement<Position>(Position_QNAME, Position.class, null, value);
    }
    
    @XmlElementDecl(namespace = "m2GIL:rest:tp", name = "CityManager")
    public JAXBElement<CityManager> createCityManager(CityManager value) {
        return new JAXBElement<CityManager>(CityManager_QNAME, CityManager.class, null, value);
    }
    
    @XmlElementDecl(namespace = "m2GIL:rest:tp", name = "CityList")
    public JAXBElement<CityList> createCityList(CityList value) {
        return new JAXBElement<CityList>(CityList_QNAME, CityList.class, null, value);
    }
    
    @XmlElementDecl(namespace = "m2GIL:rest:tp", name = "CityNotFound")
    public JAXBElement<CityException> createCityException(CityException value) {
    	return new JAXBElement<CityException>(CityException_QNAME, CityException.class, null, value);
    }
}
