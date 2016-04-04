package tp.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CityList {

	private List<City> list;
	
	public CityList() {
		this.list = new LinkedList<City>();
	}	
		

	public List<City> getCities() {
		return list;
	}

	public void setCities(List<City> cities) {
		this.list = cities;
	}
	
	public boolean addCity(City city){
		return list.add(city);
	}
	
	public boolean removeCity(City city){
		return list.remove(city);
	}
	
	
	public String toString(){
		final StringBuffer buffer = new StringBuffer();
		for (City c : list) {
			buffer.append(c.getName()).append(" in ").append(c.getCountry()).append(" at ").append(c.getPosition()).append( "," );
		}
		return buffer.toString();
	}
	
}
