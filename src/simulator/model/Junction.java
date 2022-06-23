package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {

	private List<Road> listRoadsEntrance;//carreteras entrantes al cruce/cruce es su destino
	private Map<Junction,Road>mapRoadsExit;//cruce esta conectado al cruce j atraves de r
	                                      //que carreteras utilizar para llegar al cruce j
	private List<List<Vehicle>>listRoadsColas;//lista de colas para carreteras entratntes
	                                          //List<Vehicle> representada asi carreteras entrantes
	private Map<Road,List<Vehicle>>mapRoadsCola;//para hacer la busqueda en la cola de la r dada
	                                            //se utiliza para lo de arriba
	private int indiceSemaforo;//num verdes/-1 todas las carreteras tienen tienen semaforo rojo
	private int ultPasoSemaforo;//paso indice ha cambiado de valor
	private LightSwitchingStrategy lsStrategy;//vambiar de color semaforos
	private DequeuingStrategy dqStrategy;//eliminar vehiculos de las colas
	private int xCoor;
	private int yCoor;
	
	Junction(String id,LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		
		if (lsStrategy==null || dqStrategy==null ) throw new IllegalArgumentException ("The strategies can't be null");
		if (xCoor<0 || yCoor<0)  throw new IllegalArgumentException(" X and y must be positive");
		
		this.xCoor=xCoor;
		this.yCoor=yCoor;
		this.indiceSemaforo=-1;
		this.ultPasoSemaforo = 0;
		this.listRoadsEntrance=new ArrayList<Road>();
		this.listRoadsColas = new ArrayList<List<Vehicle>>();
		this.mapRoadsCola = new LinkedHashMap<Road, List<Vehicle>>();
		this.mapRoadsExit = new LinkedHashMap<Junction, Road>();
		//estrategias
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		
		//inicializar mapas lisras y colas
	}
	

	@Override
	void advance(int time) {
		//guardamos en aux la lista de carreteras entrantes(la que contiene los coches)
		
		if(!listRoadsColas.isEmpty()&&indiceSemaforo!=-1) {
			List<Vehicle> aux = listRoadsColas.get(indiceSemaforo);
			//guardamos en vList //SEGUIR CON ESTO
			List<Vehicle> vList = dqStrategy.dequeue(aux);
			for(int i=0;i<vList.size();i++) {
				mapRoadsCola.get(vList.get(i).getRoad()).remove(vList.get(i));
				vList.get(i).moveToNextRoad();
				vList.get(i).setSpeed(0);
			
			listRoadsColas = new ArrayList<List<Vehicle>>(mapRoadsCola.values());
			}
		}
		int newIndex = lsStrategy.chooseNextGreen(listRoadsEntrance, listRoadsColas, indiceSemaforo, ultPasoSemaforo, time);
		if(newIndex != indiceSemaforo) {
			indiceSemaforo = newIndex;
			ultPasoSemaforo = time;
		}
	}
	@Override
	public JSONObject report() {//ESTUDIA ESTO
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put("id", _id);
		
		String green = "none";
		if(indiceSemaforo != -1) {
			green = listRoadsEntrance.get(indiceSemaforo).getId();
		}
		json.put("green", green);
		
		//Queues
		JSONArray queues = new JSONArray();
		
		int i = 0;
		for(int j=0;j<listRoadsEntrance.size();j++) {
			JSONObject q = new JSONObject();
			q.put("road", listRoadsEntrance.get(j).getId());
			
			JSONArray vehicles = new JSONArray();
			for(int w=0;w< listRoadsColas.get(i).size();w++) {
				vehicles.put(listRoadsColas.get(i).get(w).getId());
			}
			i++;
			q.put("vehicles", vehicles);
			queues.put(q);
		}
		
		json.put("queues", queues);
		
		return json;
	}
	public void addIncommingRoad(Road r) {
		if(this!=r.getDest())
			throw new IllegalArgumentException("La carretera no llega a este Cruce");
		else{
			listRoadsEntrance.add(r);
			LinkedList<Vehicle> l = new LinkedList<Vehicle>();
			listRoadsColas.add(l);
			mapRoadsCola.put(r,l);
		}
	}
	public void addOutGoingRoad(Road r) {
		if(this!=r.getSrc()) {//mira que r sea una carretera salinete
			throw new IllegalArgumentException("La carretera no sale de este Cruce");
		}else {
			if(mapRoadsExit.containsKey(r.getDest())) {//mira si el mapa solo tiene esa clave 1 vez,osea que solo se puede accedera ese cruce mediante una carretera
					throw new IllegalArgumentException("Ya existe otra carretera que lleve al mismo Cruce");
			}
			else {
				mapRoadsExit.put(r.getDest(), r);
			}
		}
	}
	public void enter(Vehicle v) {
		Road r=v.getRoad();
		mapRoadsCola.get(r).add(v);
		//COMO ACTUALIZAR LA LISTA -> crear nueva lista con los datos de colaMap (?)
		listRoadsColas= new ArrayList<List<Vehicle>>(mapRoadsCola.values());
		v.setSpeed(0);
	}
	public Road roadTo(Junction j) {//REVISAR ESTO
		if(j == this) {
			return listRoadsEntrance.get(0);
		}
		else {
			return mapRoadsExit.get(j);
		}
	}
	public int getX() {
		return xCoor;
	}

	public int getY() {
		return yCoor;
	}

	public int getGreenLightIndex() {
		return indiceSemaforo;
	}
	public List<Road> getInRoads() {
		// TODO Auto-generated method stub
		return listRoadsEntrance;
	}


	public Object getGreenLightIndexRoads() {
		// TODO Auto-generated method stub
		
		if(indiceSemaforo == -1) {
			return "NONE";
		}
		else {
			return listRoadsEntrance.get(indiceSemaforo).getId();
		}
		
	}
}
	
