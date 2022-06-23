package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject{
	
	private List<Junction> itinerario;
	private int velocidadMax;
	private int velocidadAct;
	private int cuentaItinerario;
	private VehicleStatus estado;
	private Road road;
	private int localizacion=0;
	private int contaminacion;
	private int contaminacionTotal;
	private int distancia;
    private int ultimoCruce;
	
    
	Vehicle(String id, int maxSpeed, int contClass,List<Junction> itinerary) {
		
		super(id); 
		
		if(maxSpeed<=0)
			throw new IllegalArgumentException (" The speed must be positive");
		else if (contClass<0 || contClass>10)
			throw new IllegalArgumentException (" The contClass must be between 0 and 10");
		else if (itinerary.size()<2)
			throw new IllegalArgumentException (" The contClass must be al least 2");
	
		this.velocidadMax=maxSpeed;
		this.contaminacion=contClass;
		this.itinerario = Collections.unmodifiableList(new ArrayList<>(itinerary));
		this.estado=VehicleStatus.PENDING; //lo inicicializo	
		this.distancia=0;
		this.cuentaItinerario=0;
		this.contaminacionTotal=0;
	}

	@Override
	void advance(int time) {
		if (estado== VehicleStatus.TRAVELING) {
			//para actualizar la localizacion actual
			int l = localizacion;
			localizacion = Math.min(localizacion+velocidadAct,road.getLength());
			
			l=localizacion-l;//para ver lo que ha avanzado realmente en ese tramo
			int conta=contaminacion*l;//lo que ha contaminado en ese tramo
			distancia+=l;//distancia y conta total
			contaminacionTotal+= conta;		
			road.addContamination(conta);//añadimos la contaminacion de la carretera
			
			if(localizacion>=road.getLength()) {
				velocidadAct=0;
				road.getDest().enter(this);
				estado= VehicleStatus.WAITING; 
			}
		}
		
	}
	void moveToNextRoad() {
		//CASOS PARTICULARES CUANDO LLEGA AL FINAL Y CUANDO ES LA PRIMERA VEZ
		 if(estado != VehicleStatus.PENDING && estado != VehicleStatus.WAITING) {
			 throw new IllegalArgumentException("Vehicle's status must be WAITING o PENDING");
		 }
		 else{
			 Road r;
			 if(estado == VehicleStatus.PENDING) {//para cuando todavia no habua empexado a circular
				 Junction thisJ = itinerario.get(0);//se coge el cruce en el que esta
				 Junction nextJ = itinerario.get(1);//se coge el cruce al que va
				 r = thisJ.roadTo(nextJ);//cogemos la carretera que va desde el cruce actual al que hemos dado
				cuentaItinerario++;
				road = r;
				road.enter(this);//vehiculo entra en la road y se añade a la lista de vehiculos
				estado = VehicleStatus.TRAVELING;
			 }
			 else{
				 if(cuentaItinerario==itinerario.size()-1) {//ha llegado al final
					estado=VehicleStatus.ARRIVED;
					road.exit(this);
					road = null;
				
				 }
				 else {
					 Junction thisJ = itinerario.get(cuentaItinerario);
					 Junction nextJ = itinerario.get(cuentaItinerario + 1);
					 r = thisJ.roadTo(nextJ);
					 localizacion = 0;
					 velocidadAct = 0;
					 cuentaItinerario++;
					 road.exit(this);
					 road = r;
					 road.enter(this);
					 estado = VehicleStatus.TRAVELING;
				 }
			}
		}
		
	}

	@Override
	public JSONObject report() {
	
		JSONObject json = new JSONObject();
		json.put("id", _id);
		json.put("speed", velocidadAct);
		json.put("distance", distancia);
		json.put("co2", contaminacionTotal);
		json.put("class", contaminacion);
		json.put("status", estado.toString());
		
		if (estado !=VehicleStatus.PENDING &&estado != VehicleStatus.ARRIVED ) {
			json.put("road", road.getId()); 
			json.put("location", localizacion);
		}
		return json;
	}
	
	void setSpeed(int s) {
		if(s<0) {
			throw new IllegalArgumentException("speed value must be positive");
		}
		if(estado==VehicleStatus.TRAVELING) {
			velocidadAct = Math.min(velocidadMax, s);
		}
	}
	void setContClass(int c) {
		if(c < 0 || c > 10) {
			throw new IllegalArgumentException("conatmination must be a value between 0 and 10");
		}
		contaminacion = c;
	}

	public int getMaxSpeed() {
		return velocidadMax;
	}

	public int getSpeed() {
		return velocidadAct;
	}

	public VehicleStatus getStatus() {
		return estado;
	}
	
	public int getLocation() {
		return localizacion;
	}

	public int getContClass() {
		return contaminacion;
	}
	public int getTotalCO2() {
		return contaminacionTotal;
	}
	public List<Junction> getItinerary(){
		return itinerario;
	}
	public Road getRoad() {
		return road;
	}

}