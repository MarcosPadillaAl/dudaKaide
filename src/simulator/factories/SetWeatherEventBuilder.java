package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {
	
	public SetWeatherEventBuilder() {
		super("set_weather");
	}
	private  List<Pair<String,Weather>> setweather;
	
	@Override
	protected SetWeatherEvent createTheInstance(JSONObject json) {
		

		int time = json.getInt("time");
		
		JSONArray info = json.getJSONArray("info");

		setweather = new ArrayList<Pair<String,Weather>>();
		
		for(int i = 0; i < info.length(); i++) {
			Weather weather = Weather.valueOf(info.getJSONObject(i).getString("weather").toUpperCase());
			String road = info.getJSONObject(i).getString("road");
			
			setweather.add(new Pair<String,Weather>(road, weather));
		}
		
		
		return new SetWeatherEvent(time, setweather);
	}
}
