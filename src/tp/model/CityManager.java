package tp.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CityManager {

	private List<City> cities;
	
	public CityManager() {
		this.cities = new LinkedList<City>();
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
	public boolean addCity(City city){
		return cities.add(city);
	}
	
	public boolean removeCity(City city){
		return cities.remove(city);
	}
	
	public CityList searchFor(String cityName) throws CityNotFound {
		CityList villes = new CityList();
		for (City city : cities) {
			if (city.getName().equals(cityName)) {
				villes.addCity(city);
			}
		}
		if (villes.getCities().isEmpty()) {
			throw new CityNotFound();
		}
		return villes;
	}
	
	public City searchExactPosition(Position position) throws CityNotFound {
		for(City city:cities){
			if (position.equals(city.getPosition())){
				System.out.println("Résultat : "+city.toString());
				return city;				
			}
		}
		throw new CityNotFound();
	}
	
	
	
	/**
	 * cette fonction calcule la distance en entre deux postion
	 * @param p1 postion une 
	 * @param p2 postion deux
	 * @return
	 */
	public double distance(Position p1, Position p2) {
		double dist = Double.MIN_VALUE;
		double lat1 = p1.getLatitude() * (Math.PI / 180.0);
		double long1 = p1.getLongitude() * (Math.PI / 180.0);
		double lat2 = p2.getLatitude() * (Math.PI / 180.0);
		double long2 = p2.getLongitude() * (Math.PI / 180.0);

		double longt = long2 - long1;
		double lat = lat2 - lat1;
		
		double x = Math.pow(Math.sin(lat / 2.0), 2.0) 	+ 
		Math.cos(lat1) * Math.cos(lat2) * 
		Math.pow(Math.sin(longt / 2.0), 2.0);
		
		double cst = 2.0 * Math.atan2(Math.sqrt(x), Math.sqrt(1.0 - x));
		
		Double K = 6376.5;
		dist = K * cst;	
	
		
		return dist;
	}
	
	/**
	 * cette fonction retourne la liste des ville proche a moins de 10 KM de la postion
	 * @param position postion du point pour lui calculer les ville qui l'entoure
	 * @return
	 */
	public CityList searchNear(Position position){
		CityList villes = new CityList();
		for (City c : cities) {						
			if (distance(position, c.getPosition()) <= 10) {
				System.out.println("Résultat : "+c.toString());
				villes.addCity(c);
			}
		}
		if(villes.getCities().isEmpty()) {
			System.out.println("Aucune ville trouvée proche de "+position.toString());
		}
		
		return villes;
	}
}
