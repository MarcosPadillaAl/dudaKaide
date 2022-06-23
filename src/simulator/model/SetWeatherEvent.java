package simulator.model;

import java.util.Iterator;
import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
private List<Pair<String,Weather>> setweather;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> setweather) {
		super(time);
		if(setweather == null) {
			throw new IllegalArgumentException("ws no puede ser null");
		}
		this.setweather = setweather;
	} 
	
	@Override
	void execute(RoadMap map) {
		for (int i=0; i<setweather.size();i++){
			Road r = map.getRoad(setweather.get(i).getFirst());
			if(r == null) {
				throw new IllegalArgumentException("Road not found");
			}
			r.setWeather(setweather.get(i).getSecond());
		}
	}
	public String toString() {
		String mostrar = "Change CO2 Class [";
		for (int i=0; i<setweather.size();i++){
			mostrar +="("+setweather.get(i).getFirst()+","+setweather.get(i).getSecond()+")";
		}
		mostrar+="]";
		return mostrar;
	}
}
