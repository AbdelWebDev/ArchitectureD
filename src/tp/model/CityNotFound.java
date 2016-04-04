package tp.model;

@SuppressWarnings("serial")
public class CityNotFound extends Exception {
	
	private CityException element;
	
	public CityNotFound() {
		element = new CityException("La ville demandée est introuvable");
	}
	
	public CityException getElement() {
		return element;
	}
}
