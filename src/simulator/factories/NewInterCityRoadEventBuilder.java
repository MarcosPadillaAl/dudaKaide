package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event> {
	
	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected NewInterCityRoadEvent createTheInstance(JSONObject json) {

		int time = json.getInt("time");
		String id = json.getString("id");
		String src = json.getString("src");
		String dest = json.getString("dest");
		int length = json.getInt("length");
		int co2limit = json.getInt("co2limit");
		int maxSpeed = json.getInt("maxspeed");
		Weather weather = Weather.valueOf(json.getString("weather").toUpperCase());
		
		
		
		return new NewInterCityRoadEvent(time, id, src, dest, length, co2limit, maxSpeed, weather);
	}
}
