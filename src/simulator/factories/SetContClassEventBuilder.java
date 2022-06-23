package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {
	
	public SetContClassEventBuilder() {
		super("set_cont_class");
	}
	private List<Pair<String,Integer>> setcont;
	@Override
	protected SetContClassEvent createTheInstance(JSONObject json) {

		int time = json.getInt("time");
		JSONArray info = json.getJSONArray("info");

		setcont = new ArrayList<Pair<String,Integer>>();
		
		for(int i = 0; i < info.length(); i++) {
			String road = info.getJSONObject(i).getString("vehicle");
			Integer contaminacion = info.getJSONObject(i).getInt("class");
			setcont.add(new Pair<String,Integer>(road, contaminacion));
		}
		
		
		
		return new SetContClassEvent(time, setcont);
	}
}
