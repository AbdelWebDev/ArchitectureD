package tp.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CityException {
	
	private String message;
	
	public CityException() {
		this.message = "";
	}
	
	public CityException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
