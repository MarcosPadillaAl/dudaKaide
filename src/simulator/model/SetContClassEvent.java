package simulator.model;

import java.util.Iterator;
import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event{
 private List<Pair<String,Integer>> setcont;
	
	public SetContClassEvent(int time, List<Pair<String,Integer>> setcont) {
		super(time);
		if(setcont == null) {
			throw new IllegalArgumentException("ContClas mustn't be null");
		}
		this.setcont = setcont;
	} 
	
	
	@Override
	void execute(RoadMap map) {
		for (int i=0;i< setcont.size();i++){
			Vehicle v = map.getVehicle(setcont.get(i).getFirst());
			if(v == null) {
				throw new IllegalArgumentException("Vehicle not found");
			}
			v.setContClass(setcont.get(i).getSecond());
		}
	}
	public String toString() {
		String string = "Change CO2 Class [";
		for(int i=0;i< setcont.size();i++){
			string +="("+setcont.get(i).getFirst()+","+setcont.get(i).getSecond()+")";
		}
		string+="]";
		return string;
	}
}
