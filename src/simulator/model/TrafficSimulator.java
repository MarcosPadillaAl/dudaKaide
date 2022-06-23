package simulator.model;

	import java.util.ArrayList;
import java.util.Iterator;
	import java.util.List;

	import org.json.JSONObject;

	import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
		private RoadMap roadMap;
		private List<Event> events;
		private int time;
		private EventComparator cmp;
		//AÑADIDO 2
		private List<TrafficSimObserver> observers;
		//
		public TrafficSimulator() {
			//cmp = new EventComparator();
			this.roadMap = new RoadMap();
			this.events = new SortedArrayList<Event>();//(cmp);
			this.time = 0;
			this.observers = new ArrayList<TrafficSimObserver>();
		}
		
		public void addEvent(Event e) {
			this.events.add(e);
			//CAMBIO2
			for(int i=0;i< observers.size();i++) {
				observers.get(i).onEventAdded(roadMap, events, e, time);
			}
			//
		}
		
		public void advance() {
			time++;
			//CAMBIO2
			for(int i=0;i< observers.size();i++) {
				observers.get(i).onAdvanceStart(roadMap, events, time);
			}
			//
			try {
				Iterator<Event> i = this.events.iterator();
				while(i.hasNext()) {
					Event e = i.next();
					if(time == e.getTime()) {
						e.execute(roadMap);
						i.remove();
					}
				}
				roadMap.advanceJunctions(time);
				roadMap.advanceRoads(time);
			}	catch(Exception e) {
				//CAMBIOS2
				for(TrafficSimObserver t : observers) {
					t.onError(e.getMessage());
				}
			}
			//CAMBIO2
			for(int i=0;i<observers.size();i++) {
				observers.get(i).onAdvanceEnd(roadMap, events,time);
			}
			//
		}
		
		public void reset() {
			roadMap.reset();
			events = new SortedArrayList<Event>();
			time = 0;
			//CAMBIO2
			for(int i=0;i<observers.size();i++) {
				observers.get(i).onReset(roadMap, events, time);
			}
		}
		
		public JSONObject report() {
			JSONObject json = new JSONObject();
			json.put("time", time);
			json.put("state", roadMap.report());
			return json;
		}
//CAMBIOS 2
		@Override
		public void addObserver(TrafficSimObserver o) {
			if(o != null) {
				observers.add(o);
				o.onRegister(roadMap, events, time);
			}
		}

		@Override
		public void removeObserver(TrafficSimObserver o) {
			observers.remove(o);
		}
}

