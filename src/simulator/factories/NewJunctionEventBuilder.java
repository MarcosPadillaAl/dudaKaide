package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}
	private  JSONArray array ;
	int time;
	String id;
	@Override
	protected NewJunctionEvent createTheInstance(JSONObject json) {
		
		time = json.getInt("time");
		id = json.getString("id");
		array = json.getJSONArray("coor");
		int x = array.getInt(0);
		int y = array.getInt(1);
		LightSwitchingStrategy lss = lssFactory.createInstance(json.getJSONObject("ls_strategy"));
		DequeuingStrategy dqs = dqsFactory.createInstance(json.getJSONObject("dq_strategy"));
		
		
		
		return new NewJunctionEvent(time, id, lss, dqs, x, y);
	}
}
