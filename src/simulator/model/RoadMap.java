package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	private List<Junction> junctionList;
	private List<Road> roadList;
	private List<Vehicle> vehicleList;
	private Map<String, Junction> junctionMap;
	private Map<String, Road> roadMap;
	private Map<String, Vehicle> vehicleMap;
	
	RoadMap(){
		this.junctionList = new ArrayList<Junction>();
		this.roadList = new ArrayList<Road>();
		this.vehicleList = new ArrayList<Vehicle>();
		this.junctionMap = new LinkedHashMap<String,Junction>();
		this.roadMap = new LinkedHashMap<String, Road>();
		this.vehicleMap = new LinkedHashMap<String, Vehicle>();
	}
	
	public void addJunction(Junction j) {
		
		if(junctionMap.containsKey(j.getId())) {
			throw new IllegalArgumentException("Ya existe otro Cruce com el mismo identificador");
		}
		
		junctionList.add(j);
		junctionMap.put(j.getId(), j);
	}
	
	public void addVehicle(Vehicle v) {
		if(vehicleMap.containsKey(v.getId())){
			throw new IllegalArgumentException("Ya existe otro Veh�culo com el mismo identificador");
		}
		List<Junction> l = v.getItinerary();
		for (int i = 0; i < l.size()-1; i++){
			if (l.get(i).roadTo(l.get(i+1)) == null){
				throw new IllegalArgumentException("The itinerary is null.");
			}
		}
		vehicleList.add(v);
		vehicleMap.put(v.getId(), v);
	}
	
	public void addRoad(Road r) {
		
		if(roadMap.containsKey(r.getId())){
			throw new IllegalArgumentException("Ya existe otra Carretera com el mismo identificador");
		}
		roadList.add(r);
		roadMap.put(r.getId(), r);
		
	}
	
	public Junction getJuntion(String id){
		return junctionMap.get(id);
	}
	
	public Road getRoad(String id){
		return roadMap.get(id);
	}
	
	public Vehicle getVehicle(String id){
		return vehicleMap.get(id);
	}
	
	public List<Junction> getJunctions(){
		//solo lectura
		return Collections.unmodifiableList(junctionList);
	}
	
	public List<Road> getRoads(){
		//solo lectura
		return Collections.unmodifiableList(roadList);
	}
	
	public List<Vehicle> getVehicles(){
		//solo lectura
		return Collections.unmodifiableList(vehicleList);
	}
	
	public void reset(){
		vehicleList = new ArrayList<Vehicle>();
		junctionMap = new LinkedHashMap<String,Junction>();
		junctionList = new ArrayList<Junction>();
		roadList = new ArrayList<Road>();
		
		roadMap = new LinkedHashMap<String, Road>();
		vehicleMap = new LinkedHashMap<String, Vehicle>();
	}

	public void advanceJunctions(int time) {
		for(int i=0;i<junctionList.size();i++) {
			junctionList.get(i).advance(time);
		}
	}

	public void advanceRoads(int time) {
		for(int i=0;i<roadList.size();i++) {
			roadList.get(i).advance(time);
		}
	}
	
	public JSONObject report() {
		JSONObject json = new JSONObject();
		
		JSONArray junctions = new JSONArray();
		JSONArray roads = new JSONArray();
		JSONArray vehicles = new JSONArray();
		
		for(int i=0;i<junctionList.size();i++) {
			junctions.put(junctionList.get(i).report());
		}
		for(int i=0;i<roadList.size();i++) {
			roads.put(roadList.get(i).report());
		}
		for(int i=0;i<vehicleList.size();i++) {
			vehicles.put(vehicleList.get(i).report());
		}
		
		json.put("junctions", junctions);
		json.put("roads", roads);
		json.put("vehicles", vehicles);
		return json;
	}
	
}