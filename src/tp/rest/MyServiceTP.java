package tp.rest;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import tp.model.City;
import tp.model.CityException;
import tp.model.CityList;
import tp.model.CityManager;
import tp.model.CityNotFound;
import tp.model.Position;

/**
 * @author amri abdelouhab & bouabidi sami
 *
 */
@WebServiceProvider
                   
@ServiceMode(value=Service.Mode.MESSAGE)
public class MyServiceTP implements Provider<Source> {
	
	
	private CityManager cityManager = new CityManager();
	private Map<String, String> KK;
	private JAXBContext jc;
	
	@javax.annotation.Resource(type=Object.class)
	protected WebServiceContext wsContext;
	
	public MyServiceTP(){
		try {
            jc = JAXBContext.newInstance(CityManager.class,City.class,Position.class, CityList.class, CityException.class);            
        } catch(JAXBException je) {
            System.out.println("Exception " + je);
            throw new WebServiceException("Cannot create JAXBContext", je);
        }
        cityManager.addCity(new City("Rouen",49.437994,1.132965,"FR"));
        cityManager.addCity(new City("Neuor",12,42,"RR"));
	}
	 
    
 
    public Source invoke(Source source) {
        try{
            MessageContext mc = wsContext.getMessageContext();
            String path = (String)mc.get(MessageContext.PATH_INFO);
            String method = (String)mc.get(MessageContext.HTTP_REQUEST_METHOD);
            System.out.println("\nGot HTTP "+method+" request for "+path);
		    if (method.equals("GET")) {
		    	return get(mc);
		    }
			if (method.equals("POST")) {
			    return post(source, mc);
			}
           	if (method.equals("PUT")) {
				return put(source, mc);
           	}
           	if (method.equals("DELETE")) {
				return delete(source, mc);
           	}
			throw new WebServiceException("Unsupported method:" +method);  
        } catch(JAXBException je) {
            throw new WebServiceException(je);
        }
    }


	private Source put(Source source, MessageContext mc) throws JAXBException {
		
		
		Unmarshaller u = jc.createUnmarshaller();
		City city=(City)u.unmarshal(source);	
			
		cityManager.addCity(city);
		System.out.println("Ajout de : "+city.toString());
		return new JAXBSource(jc, city);
	}


	/**
	 * @param source
	 * @param mc
	 * @return un objet Source
	 * @throws JAXBException
	 */
	private Source delete(Source source, MessageContext mc) throws JAXBException {
		String p = (String) mc.get(MessageContext.PATH_INFO);
		String pr = (String) mc.get(MessageContext.QUERY_STRING);
		Map<String, String> KK = getParameters(pr);
		CityList cl = new CityList();
		
		if (p == null) p = "";		
		if (p.equals("all")) {
			for (City city : cityManager.getCities()) {
				cl.addCity(city);				
			}
		} else {
			// vérifier si la postion correspend  a la ville
			String cities[] = null; // villes passées en parametre
			String positions[] = null; // la postion passée en parametre
			if (KK.containsKey("ville") && !KK.containsKey("position")) {
				cities = KK.get("ville").split("[+]");
			} else if (KK.containsKey("position")) {
				positions = KK.get("position").split("[+]");
			} else {
				return new JAXBSource(jc, new CityException("parametres invalides"));
			}
			
			if (cities != null) {
				for (String c : cities) {
					try {
						cl = cityManager.searchFor(c);
					} catch (CityNotFound cnf) {
						return new JAXBSource(jc, cnf.getElement());
					}
				}
			} else {	
				for (String s : positions) {
					try {
						Double lat = Double.parseDouble(s.split("[;]")[0]);
						Double lon = Double.parseDouble(s.split("[;]")[1]);
						City city = cityManager.searchExactPosition(new Position(lat, lon));
						cl.addCity(city);
					} catch (CityNotFound cnf) {
						return new JAXBSource(jc, cnf.getElement());
					}
				}
				
			}
		}
		
		for (City city : cl.getCities()) {
			System.out.println("Suppression de : "+city.toString());
			cityManager.removeCity(city);
		}
		
		if(cl.getCities().isEmpty()) {
			System.out.println("Aucune ville trouvée pour la suppression");
		}
		
		
		return new JAXBSource(jc, cl);
	}
	
	

	private Source post(Source source, MessageContext mc) throws JAXBException {
		// * rechercher une ville à  partir de sa position
		Unmarshaller u = jc.createUnmarshaller();
		Position position=(Position)u.unmarshal(source);
		Object message;
		String path = (String) mc.get(MessageContext.PATH_INFO);
		
		// *rechercher les villes proches de cette position si l'url de post contient le mot clé "near"
		if (path == null) path = "";
		if (path.contains("near")) {
			System.out.println("Affichage des villes proches de : "+position.toString());
			message = cityManager.searchNear(position);			
		} else {
			System.out.println("Affichage des villes Ã  : "+position.toString());
			try {
				message = cityManager.searchExactPosition(position);				
			} catch (CityNotFound cnf) {
				message = cnf.getElement();
				System.out.println("Aucune ville trouvÃ©e");
			}
		}
		return new JAXBSource(jc, message);
	}

	private Source get(MessageContext mc) throws JAXBException {
		
		
		// * retourner seulement la ville dont le nom est contenu dans l'url d'appel
		// * retourner tous les villes seulement si le chemin d'accés est "all"
		String path = (String) mc.get(MessageContext.PATH_INFO);
		String params = (String) mc.get(MessageContext.QUERY_STRING);
		
		// On construit une MAP pour stocker les parametres de l'URL
		Map<String, String> KV = getParameters(params);
		// On gére le cas de l'url = /
		if (path == null) path = "";
		if (path.equals("all")) {
			CityList cl = new CityList();
			for (City c : cityManager.getCities()) {
				System.out.println("Affichage de : "+c.toString());
				cl.addCity(c);
			}
			if (cl.getCities().isEmpty()) {
				System.out.println("Aucune ville trouvée Ã  afficher");
			}
			return new JAXBSource(jc, cl);
		} else {
			CityList cities;
			try {
				if (KV.containsKey("ville")) {
					// On ne garde que la premiÃ¨re ville passÃ©e en paramÃ¨tre					
					cities = cityManager.searchFor(KV.get("ville").split("[+]")[0]);
					System.out.println("Affichage de : "+cities.toString());
				} else {
					return new JAXBSource(jc, new CityException("ParamÃ¨tres invalides"));
				}
			} catch (CityNotFound cnf) {
				return new JAXBSource(jc, cnf.getElement());
			}
			return new JAXBSource(jc, cities);
		}
	}

	/**
	 * @param params
	 * @return
	 */
	private Map<String, String> getParameters(String params) {
		// On construit une MAP pour stocker les paramÃ¨tres de l'URL
		KK = new HashMap<String, String>();
		if (params != null) {
			for (String kv : params.split("[&]")) {
				KK.put(kv.split("[=]")[0], kv.split("[=]")[1]);
			}
		}
		return KK;
	}
	
	public static void main(String args[]) {
	      Endpoint e = Endpoint.create( HTTPBinding.HTTP_BINDING,
	                                     new MyServiceTP());
	      e.publish("http://127.0.0.1:8084/");
	       // pour arrÃªter : e.stop();
	 }
}
