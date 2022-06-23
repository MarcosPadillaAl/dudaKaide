package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event> {
	
	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	protected NewCityRoadEvent createTheInstance(JSONObject json) {
		
		int time = json.getInt("time");
		String id = json.getString("id");
		String src = json.getString("src");
		String dest = json.getString("dest");
		int length = json.getInt("length");
		int co2limit = json.getInt("co2limit");
		int maxSpeed = json.getInt("maxspeed");
		Weather weather = Weather.valueOf(json.getString("weather").toUpperCase());
		
		
		
		return new NewCityRoadEvent(time, id, src, dest, length, co2limit, maxSpeed, weather);
	}
}
