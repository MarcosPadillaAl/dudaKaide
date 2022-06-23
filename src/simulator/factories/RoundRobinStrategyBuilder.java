package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {

	private int timeSlot;
	
	public RoundRobinStrategyBuilder() {
		super("round_robin_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject json) {
		RoundRobinStrategy robinstrategy;
		if (json!=null&&json.has("timeslot")){
			 robinstrategy=new RoundRobinStrategy(json.getInt("timeslot"));
		}
		else {
			robinstrategy = new RoundRobinStrategy(1);
		}
		
		return robinstrategy;
	}
}
