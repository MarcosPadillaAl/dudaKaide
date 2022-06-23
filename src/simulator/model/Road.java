package simulator.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject{

	//private Junction rcJunc; 
	//private Junction destJunc;
	private int length;
	private int maxSpeed;
	protected int limiteActVel;
	protected int contaminacion;
	protected int contaminacionTot;
	protected Weather weather;//protected???
	private List<Vehicle> vehiculos;
	private Junction srcJuncc;
	private Junction destJuncc;
	private VehicleComparator cmp;
	
	 public Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,int contLimit, int length, Weather weather) {
			super(id);
			if(maxSpeed <= 0 || contLimit < 0 || length <= 0 || srcJunc == null || destJunc == null || weather == null) {
				throw new IllegalArgumentException("maxSpeed es positivo; contLimit es negativo; length es positivo; srcJunc, destJunc y weather son distintos de null; ");
			}
			this.maxSpeed=maxSpeed;
			this.contaminacion=contLimit;
			this.contaminacionTot=0;
			this.limiteActVel=maxSpeed;
			this.srcJuncc=srcJunc;
			this.destJuncc=destJunc;
			this.weather=weather;
			this.length = length;
			this.vehiculos = new ArrayList<Vehicle>();
			//A�adir road como salida de srcJunc
			this.srcJuncc.addOutGoingRoad(this);
			//A�adir road como entrada de destJunc
			this.destJuncc.addIncommingRoad(this);
			this.cmp = new VehicleComparator();//VER ESTO 
	}

	 void enter(Vehicle v) {
		if(v.getSpeed()!=0 ||v.getLocation()!=0) {
			throw new IllegalArgumentException("location and speed must be 0 ");
		}
		this.vehiculos.add(v);
	}
	public void exit(Vehicle v) {
		vehiculos.remove(v);
	}
	public void setWeather(Weather w) {
		if (w==null) {
			throw new IllegalArgumentException("weather must be a real value");
		}
		this.weather=w;
	}
	public void addContamination(int c) {
		if(c < 0) {
			throw new IllegalArgumentException("contamination must be > 0 ");
		}
		this.contaminacionTot += c;
	}
	
	public abstract void reduceTotalContamination();
	public abstract void updateSpeedLimit();
	public abstract int calculateVehicleSpeed(Vehicle v);
	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(int i=0;i<vehiculos.size();i++) {
			vehiculos.get(i).setSpeed(calculateVehicleSpeed(vehiculos.get(i)));
			vehiculos.get(i).advance(time);
		}
		cmp=new VehicleComparator();
		vehiculos.sort(cmp);//VER QUE HACE ESTO Y VER EL COMPARATOR
	}

	@Override
	public JSONObject report() {
		JSONObject json=new JSONObject();
		json.put("id", _id);
		json.put("speedlimit", limiteActVel);
		json.put("weather", weather.toString());
		json.put("co2", contaminacionTot	);
		
		JSONArray vehiclesArray = new JSONArray();
		for(int i=0;i<vehiculos.size();i++) {
			vehiclesArray.put(vehiculos.get(i).getId());
		}
		json.put("vehicles", vehiclesArray);
		return json;
	}
	
	public int getLength() {return length;}
	public Junction getDest() {return destJuncc;}
	public Junction getSrc(){return srcJuncc;}
	public Weather getWeather(){return weather; }
	public int getContLimit(){return contaminacion;}
	public int getMaxSpeed(){return maxSpeed;}
	public int getTotalCO2(){return contaminacionTot;}
	public int getSpeedLimit(){return limiteActVel;}
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(this.vehiculos);
	}
	
}
