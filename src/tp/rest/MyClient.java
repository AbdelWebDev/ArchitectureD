package tp.rest;

import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import tp.rest.MyClient;
import tp.model.City;
import tp.model.CityList;
import tp.model.CityManager;
import tp.model.Position;


/**
 * @author amri abdelouhab & bouabidi sami
 *
 */
public class MyClient {
	private Service service;
	private JAXBContext jc;

	private static final QName q = new QName("", "");
	private static final String url = "http://127.0.0.1:8084/";

	/*
	 * this application
	 */
	public MyClient() {
		try {
			jc = JAXBContext.newInstance(CityManager.class, City.class,
					Position.class, CityList.class);
		} catch (JAXBException je) {
			System.out.println("Cannot create JAXBContext " + je);
		}
	}

	
	public void searchForCity(String url, Position position) throws JAXBException {
		service = Service.create(q);
		service.addPort(q, HTTPBinding.HTTP_BINDING, url);
		Dispatch<Source> dispatcher = service.createDispatch(q,
				Source.class, Service.Mode.MESSAGE);
		Map<String, Object> requestContext = dispatcher.getRequestContext();
		requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
		Source result = dispatcher.invoke(new JAXBSource(jc, position));
		printSource(result);
	}
	
	
	
	
	/**
	 * cette fonction permet d'ajouter une ville
	 * @param name nom de la ville
	 * @param latitude  latitude de la ville
	 * @param longitude longitude de la ville
	 * @param country nom du pays
	 * @throws JAXBException
	 */
	public void addCity(String name,double latitude,double longitude,String country ) throws JAXBException {
		City city = new City(name,longitude,latitude,country);
		service = Service.create(q);
		service.addPort(q, HTTPBinding.HTTP_BINDING, url);
		Dispatch<Source> dispatcher = service.createDispatch(q,
				Source.class, Service.Mode.MESSAGE);
		Map<String, Object> requestContext = dispatcher.getRequestContext();
		requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
		Source result = dispatcher.invoke(new JAXBSource(jc, city));
		printSource(result);
	}
	
	
	
	/**
	 * la fonction nous récupérent une la liste des ville avec la méthode GET
	 * @param url url d'appele du service avec la méthode GET 
	 * @throws JAXBException
	 */
	public void getCity(String url) throws JAXBException {
		service = Service.create(q);
		service.addPort(q, HTTPBinding.HTTP_BINDING, url);
		Dispatch<Source> dispatcher = service.createDispatch(q,
				Source.class, Service.Mode.MESSAGE);
		Map<String, Object> requestContext = dispatcher.getRequestContext();
		requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "GET");
		Source result = dispatcher.invoke(new JAXBSource(jc, new Position(12,43)));
		printSource(result);
	}
	
	/**
	 * cette fonction prend en parametre l'url d'appele du service avec la methode DELETE de l'api RES
	 * @param url url d'appele du service avec la méthode DELETE
	 * @throws JAXBException
	 */
	public void deleteCity(String url) throws JAXBException {
		service = Service.create(q);
		service.addPort(q, HTTPBinding.HTTP_BINDING, url);
		Dispatch<Source> dispatcher = service.createDispatch(q,
				Source.class, Service.Mode.MESSAGE);
		Map<String, Object> requestContext = dispatcher.getRequestContext();
		requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "DELETE");
		Source result = dispatcher.invoke(new JAXBSource(jc, new Position(12,43)));
		printSource(result);
	}
	
	
	public void printSource(Source s) {
		try {
			System.out.println("============================= Response Received =========================================");
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(s, new StreamResult(System.out));
			System.out.println("\n======================================================================");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	
	public static void main(String args[]) throws Exception {
		MyClient client = new MyClient();
//
		client.getCity("http://127.0.0.1:8084/all");
		client.deleteCity("http://127.0.0.1:8084/all");
		client.getCity("http://127.0.0.1:8084/all");
		client.addCity("Rouen", 49.443889, 1.103333, "FR");
		client.addCity("Mogadiscio", 2.333333, 48.85, "SO");
		client.addCity("Rouen", 49.443889, 1.103333, "FR");
		client.addCity("Bihorel", 49.455278, 1.116944, "FR");
		client.addCity("Londres", 51.504872, -0.07857, "UK");
		client.addCity("Paris", 48.856578, 2.351828, "FR");
		client.addCity("Paris", 43.2, -80.38333, "CA");
		client.getCity("http://127.0.0.1:8084/all");
		client.addCity("Villers-Bocage", 49.083333, -0.65, "FR");
		client.addCity("Villers-Bocage", 50.021858, 2.326126, "FR");
		client.getCity("http://127.0.0.1:8084/all");
		client.deleteCity("http://127.0.0.1:8084/?position=49.083333;-0.65");
		client.getCity("http://127.0.0.1:8084/all");
		client.deleteCity("http://127.0.0.1:8084/?position=51.504872;-0.07857");
		client.deleteCity("http://127.0.0.1:8084/?position=51.504872;-0.07857");
		client.searchForCity("http://127.0.0.1:8084/", new Position(49.443889, 1.103333));
		client.searchForCity("http://127.0.0.1:8084/", new Position(49.083333, -0.65));
		client.searchForCity("http://127.0.0.1:8084/", new Position(43.2, -80.38));
		client.searchForCity("http://127.0.0.1:8084/near", new Position(48.85, 2.34));
		client.searchForCity("http://127.0.0.1:8084/near", new Position(42, 64));
		client.searchForCity("http://127.0.0.1:8084/near", new Position(49.45, 1.11));
		client.getCity("http://127.0.0.1:8084/?ville=Mogadiscio");
		client.getCity("http://127.0.0.1:8084/?ville=Paris");
		client.getCity("http://127.0.0.1:8084/?ville=Hyrule");
		client.deleteCity("http://127.0.0.1:8084/all");
		client.getCity("http://127.0.0.1:8084/all");
	}
}
