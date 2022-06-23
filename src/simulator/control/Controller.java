package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {

	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		
		if(sim == null) {
			throw new IllegalArgumentException("simulator can't be null");
		}
		if(eventsFactory == null) {
			throw new IllegalArgumentException("eventsFactory can't be null");
		}
		this.sim = sim;
		this.eventsFactory = eventsFactory;
		
	} 
	
	//(p91)Lee el JSON y lo traduce
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		
		//Mirar si in tiene un array JSONArray y guardarlo
		//Recorrer el JOSNArray y crear los eventos
		//A�adir el evento creado al simulador (sim.addEvent())
		
		JSONArray jarray = jo.getJSONArray("events");
		if(jarray == null) {
			throw new IllegalArgumentException("In data doesn't contain Event Array");
		}
		
		for(int i = 0; i < jarray.length(); i++) {
			JSONObject json = jarray.getJSONObject(i);
			Event e = eventsFactory.createInstance(json);
			sim.addEvent(e);
			//sim.addEvent(eventsFactory.createInstance(jarray.getJSONObject(i)));
		}
		
	}
	
	//(p92) Ejecuta el simulador "n" ticks llamando al simulador "n" veces y saca los estados "out"
	public void run(int n, OutputStream out) throws InterruptedException {
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();
		if(out != null){
			for(int i=0;i<n;i++) {
				sim.advance();
				jarray.put(sim.report());
			}
			json.put("states", jarray);
			PrintStream p = new PrintStream(out);
			
			p.println(json.toString());
		}else{
			for (int i=0; i<n;i++) {
				sim.advance();
				Thread.sleep(30);
			}
		}
	}
	//CAMBIOS 2
	
	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		sim.addEvent(e);
	}
	public void reset() {
		sim.reset();
	}
	
}
