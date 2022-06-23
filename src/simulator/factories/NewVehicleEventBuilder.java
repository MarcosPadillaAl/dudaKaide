package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {
	
	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}
	private List<String> itinerary = new ArrayList<String>();
	@Override
	protected NewVehicleEvent createTheInstance(JSONObject json) {

		int time = json.getInt("time");
		String id = json.getString("id");
		int maxSpeed = json.getInt("maxspeed");
		int co2 = json.getInt("class");
		
		JSONArray Jsonarray = json.getJSONArray("itinerary");

		JSONArray itineraryJSON = json.getJSONArray("itinerary");
		List<String> itinerary = new ArrayList<String>();
		for(int i = 0; i < itineraryJSON.length(); i++) {
			itinerary.add(itineraryJSON.getString(i));
		}
		
		NewVehicleEvent v = new NewVehicleEvent(time, id, maxSpeed, co2, itinerary);
		
		return v; 
	}
}
