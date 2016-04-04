package tp.model;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class City {

	private String name;
	private Position location;
	private String country;
	
	public City(String name, double latitude, double longitude, String country) {
		this.name = name;
		this.location = new Position(latitude,longitude);
		this.country = country;
	}
	
	public City() {
	}
	
	public Position getPosition() {
		return location;
	}
	public void setPosition(Position position) {
		this.location = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String toString(){
		final StringBuffer buffer = new StringBuffer();
		buffer.append(name).append(" in ").append(country).append(" at ").append(location);
		return buffer.toString();
	}
	
}
